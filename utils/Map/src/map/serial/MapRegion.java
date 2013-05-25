package map.serial;

import java.io.Serializable;

public class MapRegion implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int regionX,regionZ;
	public int sizeX,sizeZ;
	public short[] blocks;
}
