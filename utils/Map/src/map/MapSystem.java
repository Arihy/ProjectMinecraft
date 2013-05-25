package map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import map.serial.MapInfo;
import map.serial.MapRegion;

public abstract class MapSystem
{
	private String mURL;
	private MapInfo mMap; // données de la map actuelle

	/**
	 * recupere la taille de la map actuelle sous forme de tableau
	 * [0] : Taille sur l'axe X
	 * [1] : Taille sur l'axe Z
	 * [2] : Taille sur l'axe Y
	 * @return un tableau simple de la taille de la map
	 */
	public int[] getSize()
	{
		checkLoaded();
		return mMap.size;
	}
	/**
	 * recupere la position du spawn sous forme de tableau
	 * [0] : position X
	 * [1] : position Z
	 * [2] : position Y
	 * @return un tableau simple de la position du spawn
	 */
	public int[] getSpawn()
	{
		checkLoaded();
		return mMap.spawn;
	}
	/**
	 * recupere l'url associé a la map
	 * @return le path complet de la sauvegarde
	 */
	public String getURL()
	{
		checkLoaded();
		return mURL;
	}
	/**
	 * Creer une nouvelle map
	 * @param mapURL path complet pour la sauvegarde (position+nom+extension)
	 * @param blocksX Nombre de blocks sur l'axe X
	 * @param blocksZ Nombre de blocks sur l'axe Z
	 * @param mapHeight Nombre de blocks en hauteur
	 * @throws IOException si impossible d'ecrire la sauvegarde
	 */
	public void create(String mapURL, int blocksX,int blocksZ, int mapHeight) throws IOException
	{
		mMap = new MapInfo();
		mMap.regionSizeX = blocksX;
		mMap.regionSizeZ = blocksZ;
		mMap.size = new int[3];
		mMap.size[0] = blocksX;
		mMap.size[1] = blocksZ;
		mMap.size[2] = mapHeight;
		mURL = mapURL;
		mMap.hashmap = new HashMap<String, Object>();
		setSpawn(0, 0, 0);

		File folder = new File(mapURL);
		if (folder.exists())
		{
			for (File f : folder.listFiles())
				f.delete();
		}
		folder.delete();
		if (!folder.mkdir())
			throw new RuntimeException("impossible de creer le dossier");
		saveInfo();
	}

	/**
	 * Creer une nouvelle map
	 * @param mapURL path complet pour la sauvegarde (position+nom+extension)
	 * @param blocksX Nombre de blocks sur l'axe X
	 * @param blocksZ Nombre de blocks sur l'axe Z
	 * @param mapHeight Nombre de blocks en hauteur
	 * @param regionSizeX Taille de decoupe pour les regions en X
	 * @param regionSizeZ Taille de decoupe pour les regions en Z
	 * @throws IOException si impossible d'ecrire la sauvegarde
	 */
	public void create(String mapURL, int blocksX,int blocksZ, int mapHeight, int regionSizeX,int regionSizeZ) throws IOException
	{
		mMap = new MapInfo();
		mMap.regionSizeX = regionSizeX;
		mMap.regionSizeZ = regionSizeZ;
		mMap.size = new int[3];
		mMap.size[0] = blocksX;
		mMap.size[1] = blocksZ;
		mMap.size[2] = mapHeight;
		mURL = mapURL;
		mMap.hashmap = new HashMap<String, Object>();
		setSpawn(0, 0, 0);

		File folder = new File(mapURL);
		if (folder.exists())
		{
			for (File f : folder.listFiles())
				f.delete();
		}
		folder.delete();
		if (!folder.mkdir())
			throw new RuntimeException("impossible de creer le dossier");
		saveInfo();
	}
	/**
	 * definit un spawn
	 * @param x Position bloc X
	 * @param z Position bloc Z
	 * @param y Position bloc Y
	 */
	public void setSpawn(int x,int z, int y)
	{
		mMap.spawn = new int[3];
		mMap.spawn[0] = x;
		mMap.spawn[1] = z;
		mMap.spawn[2] = y;
	}
	/**
	 * recupere la taille sur l'axe X de chaque zone
	 * @return la taille des regions sur l'axe X
	 */
	public int getZoneWidth()
	{
		return mMap.regionSizeX;
	}
	/**
	 * recupere la taille sur l'axe Z de chaque zone
	 * @return la taille des regions sur l'axe Z
	 */
	public int getZoneHeight()
	{
		return mMap.regionSizeZ;
	}
	/**
	 * Charge une map depuis un fichier serialisé
	 * @param mapURL Path complet (position+nom+extension) du fichier
	 * @throws IOException si le fichier n'est pas trouvé
	 */
	public void load(String mapURL) throws IOException
	{
		try
		{
			File fileInfo = new File(mapURL+File.separator+"info.serial");
			FileInputStream fichier = new FileInputStream(fileInfo); // Creation d'un flux de fichier en lecture
			ObjectInputStream ois = new ObjectInputStream(fichier); // Creation d'un flux d'objet en lecture sur le flux de fichier
			mMap = (MapInfo) ois.readObject(); // Lecture sur le flux d'objet et cast en MapSerial

			ois.close(); // fermeture du flux d'objet
			fichier.close(); // fermeture du flux de fichier
			mURL = mapURL;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return vrai si une map est chargé (par load ou par create)
	 */
	public boolean isLoaded()
	{
		return mMap!=null;
	}
	/**
	 * Permet de stocker un object quelconque dans la map clef-->valeur (exemple les biomes);
	 * @param name Le nom qui sera associé a l'objet
	 * @param object L'objet a stocker
	 */
	public synchronized void setExtra(String name, Object object)
	{
		checkLoaded();
		mMap.hashmap.put(name, object);
	}
	/**
	 * Permet de recupere un objet stocké en extra grace a sa clef
	 * @param name La clef utilisé pendant le stockage
	 * @return L'object stocké grace a sa clef
	 */
	public Object getExtra(String name)
	{
		checkLoaded();
		return mMap.hashmap.get(name);
	}
	protected void saveInfo() throws IOException
	{
		File file = new File(mURL+File.separator+"info.serial");
		if (file.exists())
			file.delete();
		FileOutputStream fos = new FileOutputStream(file); // creation d'un flux de fichier en ecriture
		ObjectOutputStream oos = new ObjectOutputStream(fos); // creation d'un flux d'objet sur le flux de fichier
		oos.writeObject(mMap); // ecriture de l'objet de type MapSerial sur le flux de fichier
		oos.flush(); // forcer l'ecriture des dernieres donné sur le tempon
		oos.close(); // fermeture du flux d'objet
		fos.close(); // fermeture du flux de fichier
	}
	
	/**
	 * permet de verifier si une map est bien chargé
	 */
	protected void checkLoaded()
	{
		if (!isLoaded())
			throw new IllegalStateException("No map loaded");
	}
	
	// Fonctions de manipulation

	/**
	 * Permet de lire un bloc dans la region actuelle
	 * @param bx position X du bloc recherché
	 * @param bz position Z du bloc recherché
	 * @param by position Y du bloc recherché
	 * @return la valeur du bloc recherché
	 */
	protected short readRegion(MapRegion region,int bx,int bz,int by)
	{
		int position = ((bx%mMap.regionSizeX)*(region.sizeZ*mMap.size[2])) + ((bz%mMap.regionSizeZ)*mMap.size[2]) + by;
		if (position < 0 ||position >= region.blocks.length)
			throw new ArrayIndexOutOfBoundsException("position block x:"+bx+" z:"+bz+" y:"+by);
		return region.blocks[position];
	}
	/**
	 *  Permet d'écrire un bloc dans la region actuelle
	 * @param bx Position X du bloc a modifier
	 * @param bz Position Z du bloc a modifier
	 * @param by Position Y du bloc a modifier
	 * @param val Nouvelle valeur du bloc
	 */
	protected void writeRegion(MapRegion region,int bx,int bz,int by,short val)
	{
		//position    		                       axe X                                 axe Z                     axeY
		int position = ((bx%mMap.regionSizeX)*(region.sizeZ*mMap.size[2])) + ((bz%mMap.regionSizeZ)*mMap.size[2]) + by;
		if (position < 0 || position >= region.blocks.length)
		{
			throw new ArrayIndexOutOfBoundsException("position block x:"+bx+" z:"+bz+" y:"+by);
		}
		region.blocks[position] = val;
	}
}
