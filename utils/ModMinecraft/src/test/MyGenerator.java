package test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import map.Map;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class MyGenerator extends ChunkGenerator
{
	private ArrayList<Map> mMapListLoaded;

	public MyGenerator( )
	{
		mMapListLoaded = new ArrayList<Map>();
	}

	@Override
	public boolean canSpawn(World world, int x, int z)
	{
		return super.canSpawn(world, x, z);
	}
	private void setBlockInChunk(short[][] result, int x, int y, int z, short blkid) 
	{
		if (result[y >> 4] == null) 
		{
			result[y >> 4] = new short[4096];
		}
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
	}
	@SuppressWarnings("unused")
	private short getBlockInSection(short[][] result, int x, int y, int z)
	{
		if (result[y >> 4] == null) 
		{
			return (short)0;
		}
		return result[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
	}
	private Map getMapLoaded(World world)
	{
		Map map = null;
		for (Map m : mMapListLoaded)
		{
			if (m.getURL().equals(world.getName()))
				map = m;
		}
		if (map == null)
		{
			map = new Map();
			try
			{
				map.load(world.getName());
				int[] spawn = map.getSpawn();
				world.setSpawnLocation(spawn[0], spawn[1], map.getHeight(spawn[0], spawn[1], true));
				mMapListLoaded.add(map);
			}
			catch (IOException e)
			{
				throw new RuntimeException("Failed to load "+world.getName());
			}			
		}
		return map;
	}
	@Override
	public short[][] generateExtBlockSections(World world,Random random, int x, int z, BiomeGrid biomes)
	{
		Map map = getMapLoaded(world);
		int nbSections = world.getMaxHeight() / 16;
		short[][] result = new short[nbSections][];
		if (map.getSize()[2] != world.getMaxHeight())
			throw new RuntimeException("Bad Height  map:"+map.getSize()[2]+" world:"+world.getMaxHeight());
		try
		{
			for (int px=0;px<16;px++)
			{
				for (int pz=0;pz<16;pz++)
				{
					for (int py=0;py<map.getSize()[2];py++)
					{
						short val;

						val = map.getBlock((x*16)+px, (z*16)+pz, py);

						if (val < 0)
						{
							if (py == 0)
								setBlockInChunk(result, px, py, pz, (short) 49);
							else if (py == 1)
								setBlockInChunk(result, px, py, pz, (short) 8);
							else
								setBlockInChunk(result, px, py, pz, (short)0);
						}
						else
							setBlockInChunk(result, px, py, pz, val);	
					}
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}