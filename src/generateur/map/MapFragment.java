package generateur.map;

import java.io.IOException;

import map.MapCached;

public class MapFragment extends Fragment
{
	private MapCached mMap;
	private int mRegionCacheID;
	private int[][] mCaches;
	private boolean mIsSubfragment;

	public MapFragment(MapCached map,int px,int pz,int zoneX,int zoneZ)
	{
		super(px,pz,zoneX,zoneZ);
		mMap = map;
		prepareCaches();
	}
	private MapFragment(MapCached map,int px,int pz,int zoneX,int zoneZ,int[][] cache) // TODO a faire pour recup les caches parent (pas de duplication)
	{
		super(px,pz,zoneX,zoneZ);
		mMap = map;
		mCaches = cache;
		mIsSubfragment = true;
	}
	private void prepareCaches()
	{
		int regionX = (getX()/mMap.getZoneWidth())*mMap.getZoneWidth();
		int regionZ = (getZ()/mMap.getZoneHeight())*mMap.getZoneHeight();
		int nbZonesX = (int) Math.ceil((float)(getZoneSizeX()+(getX()-regionX))/(float)mMap.getZoneWidth());
		int nbZonesZ = (int) Math.ceil((float)(getZoneSizeZ()+(getZ()-regionZ))/(float)mMap.getZoneHeight());
		mCaches = new int[nbZonesX][nbZonesZ];
		for (int i=0;i<nbZonesX;++i)
		{
			for (int j=0;j<nbZonesZ;++j)
			{
				try
				{
					mCaches[i][j] = mMap.loadRegionInCache(regionX+i*mMap.getZoneWidth(), regionZ+j*mMap.getZoneHeight());
				}
				catch (IOException e)
				{
					System.out.println("Failed to load region in cache for MapFragment");
					e.printStackTrace();
				}
			}
		}

	}
	private int getCache(int x,int z)
	{
		int regionX = (getX()/mMap.getZoneWidth())*mMap.getZoneWidth();
		int regionZ = (getZ()/mMap.getZoneHeight())*mMap.getZoneHeight();
		int nbZonesX = (x-regionX)/mMap.getZoneWidth();
		int nbZonesZ = (z-regionZ)/mMap.getZoneHeight();
		return mCaches[nbZonesX][nbZonesZ];
	}
	public void freeRegionCaches() throws IOException
	{
		if (!mIsSubfragment)
			for (int[] l : mCaches)
				for (int id : l)
					mMap.unloadRegionFromCache(id);
	}
	public int getMapHeight()
	{
		return mMap.getSize()[2];
	}
	public int getHeight(int x,int z,boolean topToBottom) throws IOException
	{
		checkZone(x, z);
		return mMap.getHeight(getCache(getX()+x, getZ()+z),getX()+x, getZ()+z, topToBottom);
	}
	public int getBlock(int x,int z,int y) throws IOException
	{
		checkZone(x, z);
		return mMap.getBlock(getCache(getX()+x, getZ()+z),getX()+x, getZ()+z, y);
	}
	public void setBlock(int x,int z,int y,short ID) throws IOException
	{
		checkZone(x, z);
		mMap.setBlock(getCache(getX()+x, getZ()+z),getX()+x, getZ()+z, y,ID);
	}
	public MapFragment getSubMapFragment(int x,int z,int sizeX,int sizeZ)
	{
		if (x < 0 || x >= getZoneSizeX())
			throw new IllegalArgumentException("x must be inside the fragment (0 <= x < getZoneSizeX()");
		if (z < 0 || z >= getZoneSizeZ())
			throw new IllegalArgumentException("z must be inside the fragment (0 <= z < getZoneSizeZ()");
		if (sizeX < 0 || sizeZ < 0)
			throw new IllegalArgumentException("sizeX and sizeZ must be > 0");
		if (x+sizeX > getZoneSizeX())
			throw new IllegalArgumentException("x+sizeX must be inside the fragment (x+sizeX <= zoneSizeX) here : x:"+x+" sizeX:"+sizeX+" zoneSizeX:"+getZoneSizeX());
		if (z+sizeZ > getZoneSizeZ())
			throw new IllegalArgumentException("z+sizeZ must be inside the fragment (z+sizeZ <= zoneSizeZ) here : z:"+z+" sizeZ:"+sizeZ+" zoneSizeZ:"+getZoneSizeZ());

		return buildMapFragment(getX()+x, getZ()+z, sizeX, sizeZ);
	}
	private MapFragment buildMapFragment(int x,int z,int sizeX,int sizeZ)
	{
		int regionX = (x/mMap.getZoneWidth())*mMap.getZoneWidth();
		int regionZ = (z/mMap.getZoneHeight())*mMap.getZoneHeight();
		int nbZonesX = (int) Math.ceil((float)(sizeX+x-regionX)/(float)mMap.getZoneWidth());
		int nbZonesZ = (int) Math.ceil((float)(sizeZ+z-regionZ)/(float)mMap.getZoneHeight());
		int[][] cache = new int[nbZonesX][nbZonesZ];
		for (int i=0;i<cache.length;++i)
		{
			for (int j=0;j<cache[i].length;++j)
			{
				cache[i][j] = getCache(x+i*mMap.getZoneWidth(), z+j*mMap.getZoneHeight());
			}
		}
		MapFragment fragment = new MapFragment(mMap,x, z, sizeX, sizeZ,cache);
		return fragment;
	}
	private void checkZone(int x,int z)
	{
		if (x < 0 || x >= getZoneSizeX())
			throw new RuntimeException("OutRangException x : "+x+" zoneSizeX : "+getZoneSizeX());
		if (z < 0 || z >= getZoneSizeZ())
			throw new RuntimeException("OutRangException z : "+z+" zoneSizeZ : "+getZoneSizeZ());
	}
}