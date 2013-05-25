package map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import map.serial.MapRegion;

public class Map extends MapSystem
{
	private MapRegion mRegion; // données de la map actuelle
	private boolean mToSave;

	/**
	 * ecrit les dernieres données en cache
	 * @throws IOException si il est impossible d'ecrire sur le path
	 */
	public void flush() throws IOException
	{
		checkLoaded();
		saveInfo();
		saveRegion();
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
	public short getBlock(int blockX, int blockZ, int blockY) throws IOException
	{
		checkLoaded();
		if (blockX < 0 || blockX >= getSize()[0] || blockZ < 0 || blockZ >= getSize()[1] || blockY < 0 || blockY >= getSize()[2])
			return -1;
		else
		{
			checkRegion(blockX, blockZ,false);
			return readRegion(mRegion,blockX, blockZ, blockY);
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
	public void setBlock(int blockX,int blockZ,int blockY,short value) throws IOException
	{
		//System.out.println(blockX+" "+blockZ+" "+blockY);
		checkLoaded();
		checkRegion(blockX,blockZ,true);
		writeRegion(mRegion,blockX, blockZ, blockY, value);
		mToSave = true;
	}
	private void saveRegion() throws IOException
	{
		if (mToSave)
		{
			File file = new File(getURL()+File.separator+mRegion.regionX+"-"+mRegion.regionZ+".serial");
			if (file.exists())
				file.delete();
			FileOutputStream	fos = new FileOutputStream(file); // creation d'un flux de fichier en ecriture
			ObjectOutputStream oos = new ObjectOutputStream(fos); // creation d'un flux d'objet sur le flux de fichier
			oos.writeObject(mRegion); // ecriture de l'objet de type MapSerial sur le flux de fichier
			oos.flush(); // forcer l'ecriture des dernieres donné sur le tempon
			oos.close(); // fermeture du flux d'objet
			fos.close(); // fermeture du flux de fichier
			mToSave = false;
		}
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
	public int getHeight(int x,int z,boolean FromTopToBottom) throws IOException
	{
		checkLoaded();

		if (FromTopToBottom)
		{
			for (int i=getSize()[2]-1;i>=0;i--)
			{
				if (getBlock(x, z, i) != 0)
					return i+1;

			}
		}
		else
		{
			for (int i=0;i<getSize()[2];i++)
			{
				if (getBlock(x, z, i) == 0)
					return i;

			}
		}
		return 0;
	}
	private void checkRegion(int blockX,int blockZ,boolean create) throws IOException
	{
		int regionX = blockX/getZoneWidth(), regionZ = blockZ/getZoneHeight();
		//	System.out.println(blockX+" "+mMap.regionSizeX+" "+regionX);
		if (mRegion == null || mRegion.regionX != regionX || mRegion.regionZ != regionZ ) // si pas de region charg� ou qu'une coordonn�e est fausse
		{
			if (!loadRegion(regionX, regionZ) && create)
			{
				createRegion(regionX, regionZ);
			}
		}
	}
	private void createRegion(int x,int z) throws IOException
	{
		saveRegion();
		int regionSizeX = getZoneWidth();
		int regionSizeZ = getZoneHeight();
		if ((x*regionSizeX)+regionSizeX > getSize()[0])
			regionSizeX = getSize()[0]-(x*regionSizeX);
		if ((z*regionSizeZ)+regionSizeZ > getSize()[1])
			regionSizeZ = getSize()[1]-(z*regionSizeZ);
		mRegion = new MapRegion();
		mRegion.blocks = new short[getZoneWidth()*getZoneHeight()*getSize()[2]];
		mRegion.sizeX = regionSizeX;
		mRegion.sizeZ = regionSizeZ;
		mRegion.regionX = x;
		mRegion.regionZ = z;
	}
	private boolean loadRegion(int regionX,int regionZ) throws IOException
	{
		saveRegion();
		try
		{
			File fileRegion = new File(getURL()+File.separator+regionX+"-"+regionZ+".serial");
			if (fileRegion.exists())
			{
				FileInputStream fichier = new FileInputStream(fileRegion); // Creation d'un flux de fichier en lecture
				ObjectInputStream ois = new ObjectInputStream(fichier); // Creation d'un flux d'objet en lecture sur le flux de fichier
				mRegion = (MapRegion) ois.readObject(); // Lecture sur le flux d'objet et cast en MapSerial

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