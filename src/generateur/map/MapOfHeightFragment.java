package generateur.map;

public class MapOfHeightFragment extends Fragment
{
	private int[][] mMapOfHeight;
	
	public MapOfHeightFragment(int[][] mapOfHeight,int px,int pz,int zoneX,int zoneZ)
	{
		super(px,pz,zoneX,zoneZ);
		mMapOfHeight = mapOfHeight;
	}
	public int[][] getWorkingMatrice()
	{
		return mMapOfHeight;
	}
	public int getMapHeight()
	{
		return 256; // TODO hard
	}
	public int getHeight(int x,int z)
	{
		checkZone(x, z);
		return mMapOfHeight[getX()+x][getZ()+z];
	}
	public void setHeight(int x,int z,int height)
	{
		checkZone(x, z);
		mMapOfHeight[getX()+x][getZ()+z] = height;
	}
	private void checkZone(int x,int z)
	{
		if (x < 0 || x >= getZoneSizeX())
			throw new RuntimeException("OutRangException x : "+x+" zoneSizeX : "+getZoneSizeX());
		if (z < 0 || z >= getZoneSizeZ())
			throw new RuntimeException("OutRangException z : "+z+" zoneSizeZ : "+getZoneSizeZ());
	}
}
