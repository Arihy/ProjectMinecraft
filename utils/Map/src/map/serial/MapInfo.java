package map.serial;

import java.io.Serializable;
import java.util.HashMap;

public class MapInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int[] size;
	public int[] spawn;
	public int regionSizeX,regionSizeZ;
	public HashMap<String, Object> hashmap;

}
