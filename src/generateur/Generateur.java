package generateur;

import generateur.map.MapZone;
import generateur.module.Module;
import generateur.module.ModuleMarais;
import generateur.module.ModuleMontagne;
import generateur.module.ModulePlaine;
import generateur.module.ModulePlaineConstruction;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;

import map.MapCached;

public class Generateur
{
	private short[][] mEnvTemp,mEnvRain;
	private static int DECAL_TEMP = 20;
	private static int DECAL_RAIN = 30;
	private Semaphore mSemaphoreSync;

	public MapCached generate(int nbThreads,int sizeX,int sizeZ,int sizeY, int nbRegionX,int nbRegionZ,int groundHeight,String url,int seed) throws IOException
	{
		MapZone mapZone = new MapZone(nbRegionX*nbRegionZ);
		Random random = new Random(seed);
		System.out.println("Step 1 : Creation table des regions");
		step1Regions(mapZone,sizeX,sizeZ,sizeY,nbRegionX,nbRegionZ,random);
		System.out.println("Step 2 : Generation du terrain");
		step2Geometry(mapZone,nbThreads,groundHeight,seed,random);
		System.out.println("Step 3 : Generation des entitÃ©es");
		step3Entities(mapZone,url,groundHeight,seed,random);
		System.out.println("Generation TerminÃ©");

		return mapZone.getMap();
	}
	private void step1Regions(MapZone mapZone,int sizeX,int sizeZ,int sizeY,int nbRegionX,int nbRegionZ, Random random)
	{
		mapZone.prepareRegions(sizeX, sizeZ, sizeY, nbRegionX, nbRegionZ); // generation des regions 
		buildRegionEnvs(mapZone,random); // generation du tableau de temperature et de précipitation
		//printEnvs();

		for (int i=0;i<mapZone.getNbRegionX();++i)
		{
			for (int j=0;j<mapZone.getNbRegionZ();++j)
			{
				mapZone.setModuleAt(i, j, getModule(mEnvTemp[i][j], mEnvRain[i][j],random)); // positionne le module generé en fonction de l'environnement
			}
		}
		mapZone.zoneFusion(); // fusion des regions
	}
	private void step2Geometry(MapZone mapZone,int nbThreadsLimit,int groundHeight,int seed,Random random) throws IOException
	{
		mapZone.buildMapOfHeight(); // creation de la map des hauteurs
		mapZone.startParcour(); // initialisation du parcour
		Module module;
		mSemaphoreSync = new Semaphore(0); // initialisation du semaphore a 0 
		Semaphore semaphoreLimit = new Semaphore(nbThreadsLimit); // initialisation du semaphore qui permet de limiter le nombres de threads en execution simultanée
		int nbThreads = 0; // nombres de thread lancé init a 0
		while ((module = mapZone.getNextModule()) != null) // tant qu'il existe un module non explorés
		{
			Thread thread = new Thread(new RunnableStepGeometry(mSemaphoreSync,semaphoreLimit,module, seed, groundHeight)); // creation d'un thread de géometrie avec les parametres necessaires
			++nbThreads; // +1 thread de creé
			thread.start(); // lancement du thread
		}		
		try
		{
			mSemaphoreSync.acquire(nbThreads); // si tous les threads on terminés, on avance
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void step3Entities(MapZone mapZone,String url,int groundHeight,int seed,Random random) throws IOException
	{
		System.out.println("   - convertion : Map de hauteurs vers Map d'entitÃ©es");
		mapZone.buildMap(url); // convertion de map de hauteurs en map (du projet Map) avec l'url de la map
		System.out.println("   - generation des entitÃ©es");
		mapZone.startParcour(); // initialisation du parcour
		Module module;
		mSemaphoreSync = new Semaphore(0); // initialisation du semaphore a 0 
		int nbThreads = 0;
		while ((module = mapZone.getNextModule()) != null) // tant qu'il existe un module non explorés
		{
			Thread thread = new Thread(new RunnableStepEntity(mSemaphoreSync,module, seed, groundHeight)); // creation d'un thread d'entité avec les parametres necessaires
			++nbThreads; // +1 thread de creé
			thread.start(); // lancement du thread
		}
		try
		{
			mSemaphoreSync.acquire(nbThreads);// si tous les threads on terminés, on avance
			mapZone.flush(); // ecrit les derniers elements qui sont encore en cache
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void printEnvs()
	{
		String str = "";
		for (int i=0;i<mEnvTemp.length;i++)
		{
			for (int j=0;j<mEnvTemp[i].length;j++)
			{
				str += mEnvTemp[i][j]+" ";
			}
			str += '\n';
		}
		str += '\n';
		for (int i=0;i<mEnvRain.length;i++)
		{
			for (int j=0;j<mEnvTemp[i].length;j++)
			{
				str += mEnvRain[i][j]+" ";
			}
			str += '\n';
		}
		System.out.println(str);
	}
	/**
	 * Permet de generer un tableau d'environnement (temperature et précipitation)
	 * @param mapZone une instance de MapZone pour recuperer les parametres de la map
	 * @param random le random crée a partir du seed de la map
	 */
	private void buildRegionEnvs(MapZone mapZone,Random random)
	{
		mEnvTemp = new short[mapZone.getNbRegionX()][mapZone.getNbRegionZ()];
		mEnvRain = new short[mapZone.getNbRegionX()][mapZone.getNbRegionZ()];

		mEnvTemp[0][0] = (short) random.nextInt(101); // on genere la premiere temperature dans le coin gauche
		mEnvRain[0][0] =  (short) (random.nextInt(1+getMaxRain(mEnvTemp[0][0]))); // on genere la premiere valeur de précipitation a partir de la temperature

		for (int i=1;i<mEnvTemp.length;i++) // pour toutes les regions sur la premiere ligne (sauf la premiere case)
		{
			mEnvTemp[i][0] = (short) getRandomedValue(random, 0, 100, mEnvTemp[i-1][0], DECAL_TEMP); // on genere une temperature en fonction de la case precedente
			mEnvRain[i][0] = (short) getRandomedValue(random, 0, getMaxRain(mEnvTemp[i][0]), mEnvRain[i-1][0], DECAL_RAIN); // on genere une valeur de précipitation en fonction de la case précedente et de la temperature
		}
		for (int i=1;i<mEnvTemp[0].length;i++) // pour toutes les regions sur la premiere colonne (sauf premiere case)
		{
			mEnvTemp[0][i] = (short) getRandomedValue(random, 0, 100, mEnvTemp[0][i-1], DECAL_TEMP); // on genere une temperature en fonction de la case precedente
			mEnvRain[0][i] = (short) getRandomedValue(random, 0, getMaxRain(mEnvTemp[0][i]), mEnvRain[0][i-1], DECAL_RAIN);// on genere une valeur de précipitation en fonction de la case précedente et de la temperature
		}
		for (int i=1;i<mEnvTemp.length;i++) // pour toutes les cases restantes en x
		{
			for (int j=1;j<mEnvTemp[i].length;j++)// pour toutes les cases restantes en z
			{
				mEnvTemp[i][j] = (short) getRandomedValue(random, 0, 100, (mEnvTemp[i-1][j]+mEnvTemp[i][j-1])/2, DECAL_TEMP);// on genere une temperature en fonction de la case precedente au dessus et celle de gauche
				mEnvRain[i][j] = (short) getRandomedValue(random, 0, getMaxRain(mEnvTemp[i][j]), (mEnvRain[i-1][j]+mEnvRain[i][j-1])/2, DECAL_RAIN);// on genere une valeur de précipitation en fonction de la case précedente haut et gauche et de la temperature
			}
		}
	}
	/**
	 * a partir des parametres, genere un module
	 * @param temperature temperature de la zone
	 * @param rain précipitation de la zone
	 * @param random random crée a partir du seed
	 * @return retourne le module generé
	 */
	private Module getModule(int temperature,int rain,Random random)
	{
		Random moduleRandom = new Random(random.nextInt()); // cree un random pour ne pas modifier les generations si on modifie le code de cette fonction
		
		// le code si dessous est modifié pour chaque module ajouté au generateur car c'est lui qui determine quel module se trouve a tel endroit
		if (rain > 50)
			return new ModuleMarais(moduleRandom);
		else if (temperature <= 100 && temperature > 50)
		{
			if (moduleRandom.nextInt(3) == 0)
				return new ModulePlaineConstruction(moduleRandom);
			else
				return new ModulePlaine(moduleRandom);
		}
		else 
			return new ModuleMontagne(moduleRandom);
	}
	/**
	 * genere une valeur aleatoire en respectant :  min <= center-decal <= "nouvelle valeur" <= center+decal <= max
	 * @param random random generé par le seed
	 * @param min valeur minimum possible
	 * @param max valeur maximum possible
	 * @param center valeur a partir de la quelle la nouvelle valeur sera calculé
	 * @param decal le decallage maximum possible
	 * @return une nouvelle valeur
	 */
	private int getRandomedValue(Random random, int min,int max, int center, int decal)
	{
		int value = center+random.nextInt(2*decal)-decal; // generation compris entre : center-decal <= valeur <= center+decal
		if (value < min) // si en dessous du min
			value = min; // correction sur le min
		else if (value > max) // si au dessus du max
			value = max; // correction sur le max
		return value;
	}
	/**
	 * determine la précipitation maxiumum en fonction de la temperature
	 * 
	 * la courbe est de la meme forme que celle de notch pour les précipitation par rapport aux temperatures
	 * 
	 * @param temperature la temperature qui determinera la précipitation
	 * @return la précipitation maxiumum possible
	 */
	private short getMaxRain(short temperature)
	{
		float temperatureForLog = ((temperature*9f)/100)+1; // transformation pour etre entre 0 et 9 + doit etre compris entre 1 et 10 (d'ou le +1) 
		return (short) (100*Math.log10(temperatureForLog)); // logarithme10 + transformation pour etre compris entre 0 et 100
	}
}