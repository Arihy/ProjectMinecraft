package generateur.module.sousmodule;

import generateur.factory.FactoryTree;
import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.TreeType;

public class SousModuleForet extends SousModule {
	protected void generate(MapFragment mapFragment, int groundHeight, Random random)
	{
		try
		{
			System.out.println("Generation foret ("+mapFragment.getX()+";"+mapFragment.getZ()+")");
			int probArbre;
			int mapHeightMax=mapFragment.getMapHeight();
			for(int i=0; i<mapFragment.getZoneSizeX(); i++)
				for(int j=0; j<mapFragment.getZoneSizeZ(); j++) 
				{
					int heightMax;
					heightMax = mapFragment.getHeight(i, j,false);
					if(mapFragment.getBlock(i, j, heightMax-1)!=Material.WATER.getId()) {
						if(heightMax<0 || mapHeightMax<0)
							System.out.println("Height max ou mapHeightMax negatif");
						else {
							probArbre=random.nextInt(30*(100*heightMax)/mapHeightMax);
							if(probArbre==0) {
								FactoryTree.generate(mapFragment, i, j, TreeType.BIG_TREE, mapHeightMax, random);
							}
						}
					}
				}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getMinSizeX()
	{
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int getMaxSizeX()
	{
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	public int getMinSizeZ()
	{
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int getMaxSizeZ()
	{
		// TODO Auto-generated method stub
		return 200;
	}
}
