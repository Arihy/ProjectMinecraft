package map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Semaphore;

import map.serial.MapRegion;

public class MapCached extends MapSystem
{
	private MapRegion[] mRegionsCached;
	private boolean[] mToSave;
	private Semaphore mSemaphoreCacheLimit;
	private Semaphore mSemaphoreLock;

	public MapCached(int cacheSize)
	{
		if (cacheSize <= 0)
			throw new IllegalArgumentException("cacheSize must be > 0");
		mSemaphoreLock = new Semaphore(1);
		mRegionsCached = new MapRegion[cacheSize];
		mSemaphoreCacheLimit = new Semaphore(cacheSize);
		mToSave = new boolean[cacheSize];
	}
	/**
	 * Recupere la taille du cache de cette objet
	 * @return la taille du cache
	 */
	public int getCacheSize()
	{
		return mRegionsCached.length;
	}

	/**
	 * ecrit les dernieres données en cache
	 * @throws IOException si il est impossible d'ecrire sur le path
	 */
	public void flush() throws IOException
	{
		checkLoaded();
		saveInfo();
		saveRegions();
	}
	
	
	
	/**
	 * Permet d'obtenir le nombre de block en hauteur sur une zone donnée
	 *  (le calcul se fait du haut vers le bas sur FromTopToBottom est a true)
	 * @param x position X
	 * @param z positoon Z
	 * @param FromTopToBottom Si le calcul se fait en partant du haut ou du bas
	 * @return le nombre de block sur la zone indiqué
	 * @throws IOException  si la map n'est plus accessible
	 */
	public int getHeight(int regionCacheID,int x,int z,boolean FromTopToBottom) throws IOException
	{
		checkLoaded();

		if (FromTopToBottom)
		{
			for (int i=getSize()[2]-1;i>=0;i--)
			{
				if (getBlock(regionCacheID,x, z, i) != 0)
					return i+1;

			}
		}
		else
		{
			for (int i=0;i<getSize()[2];i++)
			{
				if (getBlock(regionCacheID,x, z, i) == 0)
					return i;

			}
		}
		return 0;
	}
	/**
	 * Demande de charger la region qui contient ce block en parametre
	 * Attention : le thread peut se blocker pour attendre qu'une place en cache soit disponible
	 * ou parce que un autre thread est en train de charger ou decharger une region
	 * @param blockX la position en X du block
	 * @param blockZ la position en Z du block
	 * @return l'identificateur du cache aloué
	 * @throws IOException si l'acces a la map est impossible sur le disque
	 */
	public int loadRegionInCache(int blockX,int blockZ) throws IOException
	{
		try
		{
			mSemaphoreCacheLimit.acquire(); // attend qu'une place en cache se libere et verouille au passage
			mSemaphoreLock.acquire(); // attend qu'un thread termine son operation et verouille au passage
			int regionX = blockX/getZoneWidth(), regionZ = blockZ/getZoneHeight();
			for (int i=0;i<mRegionsCached.length;i++) // parcour tous les caches
			{
				if (mRegionsCached[i] == null) // si celui-ci est libre
				{
					if (!loadRegion(i, regionX, regionZ)) // on essai de charger la region souhait�
					{ //si le chargement a rat�
						createRegion(i, regionX, regionZ); // on creer la region
					}
					mSemaphoreLock.release(); // et on deverouille pour laisser la place aux autres threads
					return i; // on retourne l'id du cache (qui est l'index du tableau)
				}
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			throw new RuntimeException("InterruptedThreadException");
		}
		return -1;
	}
	/**
	 * Demande la decharge d'une region en cache grace a l'identificateur recu pendant le chargement en cache
	 * Attention : la thread peut se blocker pour attendre qu'un autre thread termine de charger ou decharger une region
	 * @param regionCacheID l'identificateur du cache a decharger
	 * @throws IOException si l'acces a la map est impossible sur le disque
	 */
	public void unloadRegionFromCache(int regionCacheID) throws IOException
	{
		try
		{
			mSemaphoreLock.acquire(); // attend qu'un thread termine son operation et verouille au passage
			if (mRegionsCached[regionCacheID] != null) // si il existe bien quelque chose dans ce cache
			{
				saveRegion(regionCacheID); // on sauvegarde les donn�es qui s'y trouve
				mRegionsCached[regionCacheID] = null; // on libere la place
				mSemaphoreCacheLimit.release(); // on relache un ticket pour prevenir qu'une place est libre
			}
			mSemaphoreLock.release();// et on deverouille pour laisser la place aux autres threads
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			throw new RuntimeException("InterruptedThreadException");
		}

	}
	/**
	 * permet de recuperer un bloc dans la map
	 * (le bloc vaut -1 si il est en dehor de la map)
	 * @param blockX Position X du bloc recherché
	 * @param blockZ Position Z du bloc recherché
	 * @param blockY Position Y du bloc recherché
	 * @return la valeur du bloc recherché
	 * @throws IOException si la map n'est plus accessible
	 */
	public short getBlock(int regionCacheID,int blockX, int blockZ, int blockY) throws IOException
	{
		checkLoaded();
		if (blockX < 0 || blockX >= getSize()[0] || blockZ < 0 || blockZ >= getSize()[1] || blockY < 0 || blockY >= getSize()[2])
			return -1;
		else
		{
			if (!checkRegion(regionCacheID,blockX,blockZ,true))
				throw new IllegalStateException("bad region");
			return readRegion(mRegionsCached[regionCacheID],blockX, blockZ, blockY);
		}
	}
	
	/**
	 * Modifie un bloc dans la map
	 * @param blockX Position X du bloc a modifier
	 * @param blockZ Position Z du bloc a modifier
	 * @param blockY Position Y du bloc a modifier
	 * @param value Nouvelle valeur du bloc
	 * @throws IOException si la map n'est plus accessible
	 */
	public void setBlock(int regionCacheID,int blockX,int blockZ,int blockY,short value) throws IOException
	{
		//System.out.println(blockX+" "+blockZ+" "+blockY);
		checkLoaded();
		if (!checkRegion(regionCacheID,blockX,blockZ,true))
			throw new IllegalStateException("bad region");
		writeRegion(mRegionsCached[regionCacheID],blockX, blockZ, blockY, value);
		mToSave[regionCacheID] = true;
	}
	private void saveRegions() throws IOException
	{
		for (int i=0;i<mRegionsCached.length;i++)
		{
			if (mRegionsCached[i] != null)
				saveRegion(i);
		}
	}
	private void saveRegion(int regionCacheID) throws IOException
	{
		//System.out.println("SAVING region "+region.regionX+":"+region.regionZ);
		if (mToSave[regionCacheID])
		{
			File file = new File(getURL()+File.separator+mRegionsCached[regionCacheID].regionX+"-"+mRegionsCached[regionCacheID].regionZ+".serial");
			if (file.exists())
				file.delete();
			FileOutputStream	fos = new FileOutputStream(file); // creation d'un flux de fichier en ecriture
			ObjectOutputStream oos = new ObjectOutputStream(fos); // creation d'un flux d'objet sur le flux de fichier
			oos.writeObject(mRegionsCached[regionCacheID]); // ecriture de l'objet de type MapSerial sur le flux de fichier
			oos.flush(); // forcer l'ecriture des dernieres donné sur le tempon
			oos.close(); // fermeture du flux d'objet
			fos.close(); // fermeture du flux de fichier
			mToSave[regionCacheID] = false;
		}
	}
	private boolean checkRegion(int regionCacheID,int blockX,int blockZ,boolean create) throws IOException
	{
		int regionX = blockX/getZoneWidth(), regionZ = blockZ/getZoneHeight();
		//	System.out.println(blockX+" "+mMap.regionSizeX+" "+regionX);
		if (mRegionsCached.length > 0 && mRegionsCached[regionCacheID] != null && mRegionsCached[regionCacheID].regionX == regionX && mRegionsCached[regionCacheID].regionZ == regionZ  )
		{
			return true;
		}
		return false;
	}
	private void createRegion(int regionPos,int x,int z) throws IOException
	{
		//System.out.println("create x:"+x+" z:"+z);
		int regionSizeX = getZoneWidth();
		int regionSizeZ = getZoneHeight();
		if ((x*regionSizeX)+regionSizeX > getSize()[0])
			regionSizeX = getSize()[0]-(x*regionSizeX);
		if ((z*regionSizeZ)+regionSizeZ > getSize()[1])
			regionSizeZ = getSize()[1]-(z*regionSizeZ);
		MapRegion region = new MapRegion();
		region.blocks = new short[getZoneWidth()*getZoneHeight()*getSize()[2]];
		region.sizeX = regionSizeX;
		region.sizeZ = regionSizeZ;
		region.regionX = x;
		region.regionZ = z;
		mRegionsCached[regionPos] = region;
	}
	private boolean loadRegion(int regionPos,int regionX,int regionZ) throws IOException
	{
		//	System.out.println("loadRegion x:"+regionX+" z:"+regionZ);
		try
		{
			File fileRegion = new File(getURL()+File.separator+regionX+"-"+regionZ+".serial");
			if (fileRegion.exists())
			{
				FileInputStream fichier = new FileInputStream(fileRegion); // Creation d'un flux de fichier en lecture
				ObjectInputStream ois = new ObjectInputStream(fichier); // Creation d'un flux d'objet en lecture sur le flux de fichier
				mRegionsCached[regionPos] = (MapRegion) ois.readObject();// Lecture sur le flux d'objet et cast en MapSerial

				ois.close(); // fermeture du flux d'objet
				fichier.close(); // fermeture du flux de fichier
				return true;
			}
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
