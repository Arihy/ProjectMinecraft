package generateur.map;

import generateur.module.Module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import map.MapCached;

public class MapZone
{
	private static int TYPE_FRAGMENT_MAP = 0, TYPE_FRAGMENT_MAP_OF_HEIGHT = 1;
	private MapCached mMap;
	private int[][] mMapOfheight;
	private Module[][] mModules;
	private int mSizeX,mSizeZ,mSizeY,mNbRegionX,mNbRegionZ,mRegionSizeX,mRegionSizeZ;
	private ArrayList<Module> mPileParcour;
	private int mParcourPositionX,mParcourPositionZ;
	private int mCacheSize;

	public MapZone(int cacheSize)
	{
		mCacheSize = cacheSize;
	}

	public int getNbRegionX()
	{
		return mNbRegionX;
	}
	public int getNbRegionZ()
	{
		return mNbRegionZ;
	}
	public MapCached getMap()
	{
		return mMap;
	}
	public void prepareRegions(int sizeX,int sizeZ,int sizeY,int nbRegionX,int nbRegionZ)
	{
		mSizeX = sizeX;
		mSizeZ = sizeZ;
		mSizeY = sizeY;
		mNbRegionX = nbRegionX;
		mNbRegionZ = nbRegionZ;
		mRegionSizeX = sizeX/nbRegionX;
		mRegionSizeZ = sizeZ/nbRegionZ;
		mModules = new Module[mNbRegionX][mNbRegionZ];
	}
	public void buildMapOfHeight()
	{
		mMap = null;
		mMapOfheight = new int[mSizeX][mSizeZ];
	}
	public void buildMap(String url) throws IOException
	{
		mMap = new MapCached(mCacheSize);
		mMap.create(url, mSizeX,mSizeZ, mSizeY,mRegionSizeX,mRegionSizeZ);
		remplissage(mMapOfheight, mMap);
		mMapOfheight = null;
	}
	public MapFragment getMapFragment(Module module) throws IOException
	{
		Fragment fragment =  getFragment(module, TYPE_FRAGMENT_MAP);
		if (fragment != null)
			return (MapFragment) fragment;
		return null;
	}
	public MapOfHeightFragment getMapOfHeightFragment(Module module)
	{
		try
		{
			Fragment fragment = getFragment(module, TYPE_FRAGMENT_MAP_OF_HEIGHT);
			if (fragment != null)
				return (MapOfHeightFragment) fragment;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public void freeMapFragment(MapFragment mapFragment) throws IOException
	{
		mapFragment.freeRegionCaches();
	}
	private Fragment getFragment(Module module,int TYPE_OF_FRAGMENT) throws IOException
	{
		int[] pos = getModuleInCoord(module);
		int[] size = getModuleSizeInCoord(pos[0], pos[1]);

		int reelSizeX = size[0]*mRegionSizeX;
		int reelSizeZ = size[1]*mRegionSizeZ;
		int reelPX = pos[0]*mRegionSizeX;
		int reelPZ = pos[1]*mRegionSizeZ;

		if (pos[0] != -1)
		{
			if (TYPE_OF_FRAGMENT == TYPE_FRAGMENT_MAP)
			{
				return new MapFragment(mMap, reelPX, reelPZ, reelSizeX, reelSizeZ);
			}
			else if (TYPE_OF_FRAGMENT == TYPE_FRAGMENT_MAP_OF_HEIGHT)
				return new MapOfHeightFragment(mMapOfheight, reelPX, reelPZ, reelSizeX, reelSizeZ);
		}
		return null;
	}
	public void setModuleAt(int px,int pz,Module module)
	{
		mModules[px][pz] = module;
		module.attachToMapZone(this);
	}
	public Module getModuleAt(int px,int pz)
	{
		return mModules[px][pz];
	}
	public void startParcour() // par exemple
	{
		// TODO pas de securite semaphore attention
		mPileParcour = new ArrayList<Module>();
		mParcourPositionX = mParcourPositionZ = 0;
	}
	public Module getNextModule() // par exemple
	{
		Module module = null;
		while (module == null && mParcourPositionZ < mModules[0].length)
		{
			if (!mPileParcour.contains(mModules[mParcourPositionX][mParcourPositionZ]))
			{
				module = mModules[mParcourPositionX][mParcourPositionZ];
				mPileParcour.add(module);
			}
			++mParcourPositionX;
			if (mParcourPositionX == mModules.length)
			{
				mParcourPositionX = 0;
				++mParcourPositionZ;
			}
		}
		return module;
	}

	public void zoneFusion()
	{
		ArrayList<Module> lock = new ArrayList<Module>();
		
		/*
		System.out.println("\n    Types");
		printModulesType();
		System.out.println("\n    Types");
		*/
		startParcour();
		Module module;
		while ((module = getNextModule()) != null)
		{
			expandModule(module,lock);
			lock.add(module);
		}
		/*
		printModulesType();
		System.out.println("\n    Instances");
		printModules();
		*/

	}
	private void expandModule(Module module,ArrayList<Module> lockedModule)
	{
		int[] pos = getModuleInCoord(module);
		int[] size = getModuleSizeInCoord(pos[0], pos[1]);

		//System.out.println("px: "+pos[0]+" pz: "+pos[1]/*+" sizex: "+size[0]+" sizez: "+size[1]*/);

		boolean action;
		do
		{
			action = false;

			if (pos[0]+size[0] < mModules.length) // extension en X
			{
				int j = 0;
				while (j < size[1])
				{
					if (mModules[pos[0]+size[0]][j+pos[1]].getClass() != mModules[pos[0]][pos[1]].getClass() || lockedModule.contains(mModules[pos[0]+size[0]][j+pos[1]]))
						break;
					++j;
				}
				if (j == size[1])
				{
					for (int fz = 0;fz<size[1];++fz)
					{
						mModules[pos[0]+size[0]][fz+pos[1]] = mModules[pos[0]][pos[1]];
					}
					++size[0];
					action = true;
				}
			}

			if (pos[1]+size[1] < mModules[pos[0]].length) // extension en Z
			{
				int x = 0;
				while (x < size[0])
				{
					if (mModules[x+pos[0]][pos[1]+size[1]].getClass() != mModules[pos[0]][pos[1]].getClass() || lockedModule.contains(mModules[x+pos[0]][pos[1]+size[1]]))
						break;
					++x;
				}
				if (x == size[0])
				{
					//System.out.println("    valide");
					for (int fx = 0;fx<size[0];++fx)
					{
						//System.out.println("      ecrase x:"+(fx+pos[0])+" z:"+(pos[1]+size[1]));
						mModules[fx+pos[0]][pos[1]+size[1]] = mModules[pos[0]][pos[1]];
					}
					++size[1];
					action = true;
				}
			}
		}while(action);
	}
	private void printModulesType()
	{
		HashMap<Class<? extends Module>, Integer> hash = new HashMap<Class<? extends Module>, Integer>();
		int cpt = 0;
		
			for (int j=0;j<mModules[0].length;++j)
			{
				for (int i=0;i<mModules.length;++i)
				{
				hash.put(mModules[i][j].getClass(), cpt);
				++cpt;
			}
		}
		
			for (int j=0;j<mModules[0].length;++j)
			{
				for (int i=0;i<mModules.length;++i)
				{
				System.out.print("["+ (hash.get(mModules[i][j].getClass()) < 10 ?  "0" : "")+hash.get(mModules[i][j].getClass())+"]");
			}
			System.out.println();
		}
	}
	private void printModules()
	{
		HashMap<Module, Integer> hash = new HashMap<Module, Integer>();
		int cpt = 0;
		
			for (int j=0;j<mModules[0].length;++j)
			{
				for (int i=0;i<mModules.length;++i)
				{
				hash.put(mModules[i][j], cpt);
				++cpt;
			}
		}
		
			for (int j=0;j<mModules[0].length;++j)
			{
				for (int i=0;i<mModules.length;++i)
				{
				System.out.print("["+ (hash.get(mModules[i][j]) < 10 ?  "0" : "")+hash.get(mModules[i][j])+"]");
			}
			System.out.println();
		}
	}
	private int[] getModuleInCoord(Module module)
	{
		int px = -1,pz = -1;
		parcourTotal: // etiquette
			for (int i=0;i<mModules.length;++i)
			{
				for (int j=0;j<mModules[i].length;++j)
				{
					if (mModules[i][j] == module)
					{
						px = i;
						pz = j;
						break parcourTotal;
					}
				}
			}
		return new int[]{px,pz};
	}
	private int[] getModuleSizeInCoord(int px,int pz)
	{
		int sizeX = 0,sizeZ = 0;

		for (int i=px;i<mModules.length;++i)
		{
			if (mModules[i][pz] == mModules[px][pz])
				sizeX = i+1 - px;
			else
				break;
		}
		for (int j=pz;j<mModules[px].length;++j)
		{
			if (mModules[px][j] == mModules[px][pz])
				sizeZ = j+1 - pz;
			else
				break;
		}
		return new int[]{sizeX,sizeZ};
	}
	
	public void flush() throws IOException
	{
		mMap.flush();
	}
	private void remplissage(int[][] matrice,MapCached map) throws IOException
	{
		int tx = 0,tz = 0;
		int sizeX = 1,sizeZ = 1;
		while (sizeX > 0)
		{
			sizeX = Math.min(map.getZoneWidth(), mSizeX-(tx*map.getZoneWidth()));
			sizeZ = 1;
			tz = 0;
			if (sizeX > 0)
			{
				while (sizeZ > 0)
				{
					sizeZ = Math.min(map.getZoneHeight(), mSizeZ-(tz*map.getZoneHeight()));
					if (sizeZ > 0)
					{
						int regionCacheID = mMap.loadRegionInCache(tx*map.getZoneWidth(), tz*map.getZoneHeight());

						for (int i=tx*map.getZoneWidth();i<(tx*map.getZoneWidth())+sizeX;i++)
						{
							for (int j=tz*map.getZoneHeight();j<(tz*map.getZoneHeight())+sizeZ;j++)
							{
								//TODO cree un remplisseur multithread
								for (int k=0;k<matrice[i][j];k++)
								{
									if (k==0)
										map.setBlock(regionCacheID,i, j, k, (short)49);
									else
										map.setBlock(regionCacheID,i, j, k, (short)2);
									//	System.out.println(map.getBlock(i, j, k));
								}
							}
						}
						mMap.unloadRegionFromCache(regionCacheID);
					}
					tz++;
				}
			}
			tx++;
		}
	}
}