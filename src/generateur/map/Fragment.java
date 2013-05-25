package generateur.map;

public abstract class Fragment
{
	private int mX,mZ,mZoneX,mZoneZ;
	
	public Fragment(int px,int pz,int zoneX,int zoneZ)
	{
		mX = px;
		mZ = pz;
		mZoneX = zoneX;
		mZoneZ = zoneZ;
	}
	public int getX()
	{
		return mX;
	}
	public int getZ()
	{
		return mZ;
	}
	public int getZoneSizeX()
	{
		return mZoneX;
	}
	public int getZoneSizeZ()
	{
		return mZoneZ;
	}
}
