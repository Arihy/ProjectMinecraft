package generateur.module;

import generateur.map.MapFragment;
import generateur.map.MapOfHeightFragment;

import java.io.IOException;
import java.util.Random;

import utils.perlin.Perlin;
import utils.perlin.Perlin.INTERPOLATION_TYPE;

public class ModuleMarais extends Module
{
	public ModuleMarais(Random random)
	{
		super(random);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onGenerateGround(MapOfHeightFragment mapOfHeightFragment,int groundHeight, int seed, Random random)
	{
		for (int i=0;i<mapOfHeightFragment.getZoneSizeX();i++)
		{
			for (int j=0;j<mapOfHeightFragment.getZoneSizeZ();j++)
			{
				if (i == 0 || i == mapOfHeightFragment.getZoneSizeX()-1 || j == 0 || j == mapOfHeightFragment.getZoneSizeZ()-1)
					mapOfHeightFragment.setHeight(i, j, groundHeight);
				else
					mapOfHeightFragment.setHeight(i, j, groundHeight-2+(random.nextInt(10)+mapOfHeightFragment.getX()+mapOfHeightFragment.getZ())%6);
			}
		}
		Perlin.perlin(mapOfHeightFragment.getWorkingMatrice(),mapOfHeightFragment.getX(),mapOfHeightFragment.getZ(),mapOfHeightFragment.getZoneSizeX(),mapOfHeightFragment.getZoneSizeZ(), 10, 0.5f,INTERPOLATION_TYPE.linear);
	}
	@Override
	public void onGenerateEntities(MapFragment mapFragment, int groundHeight,int seed, Random random) throws IOException
	{
		for (int i=0;i<mapFragment.getZoneSizeX();i++)
		{
			for (int j=0;j<mapFragment.getZoneSizeZ();j++)
			{
				int height = mapFragment.getHeight(i, j, true);
				if ( height < groundHeight)
				{
					for (int k=height;k<groundHeight;k++)
					{
						mapFragment.setBlock(i, j, k, (short) org.bukkit.Material.WATER.getId());
					}
				}
			}
		}
		super.onGenerateEntities(mapFragment, groundHeight, seed, random);
	}
	@Override
	public void onChoosingSubModules(Populator chooser)
	{
		// TODO Auto-generated method stub
		
	}
	
}
