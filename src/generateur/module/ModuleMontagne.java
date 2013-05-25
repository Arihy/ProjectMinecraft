package generateur.module;

import generateur.factory.FactoryTree;
import generateur.map.MapFragment;
import generateur.map.MapOfHeightFragment;

import java.io.IOException;
import java.util.Random;

import org.bukkit.TreeType;

import utils.perlin.Perlin;
import utils.perlin.Perlin.INTERPOLATION_TYPE;

public class ModuleMontagne extends Module
{
	public ModuleMontagne(Random random)
	{
		super(random);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onGenerateGround(MapOfHeightFragment mapOfHeightFragment,int groundHeight, int seed, Random random)
	{
		for(int i=0; i<mapOfHeightFragment.getZoneSizeX(); i++)
			for(int j=0; j<mapOfHeightFragment.getZoneSizeZ(); j++) {
				if(i==0 || i==mapOfHeightFragment.getZoneSizeX()-1 || j==0 || j==mapOfHeightFragment.getZoneSizeZ()-1)
					mapOfHeightFragment.setHeight(i, j, groundHeight);
				else mapOfHeightFragment.setHeight(i, j, groundHeight+random.nextInt(mapOfHeightFragment.getMapHeight()-groundHeight));
			}

		/* Lissage + mise à niveau */
		Perlin.perlin(mapOfHeightFragment.getWorkingMatrice(), mapOfHeightFragment.getX(), mapOfHeightFragment.getZ(), mapOfHeightFragment.getZoneSizeX(), mapOfHeightFragment.getZoneSizeZ(), 30, 0.5f,INTERPOLATION_TYPE.noLinear);
	}
	@Override
	public void onGenerateEntities(MapFragment mapFragment, int groundHeight,int seed, Random random) throws IOException
	{
		try
		{
			int probArbre;
			int mapHeightMax=mapFragment.getMapHeight();
			for(int i=0+5; i<mapFragment.getZoneSizeX(); i++) // TODO modifier sinon on sort de la zone limité
				for(int j=0+5; j<mapFragment.getZoneSizeZ(); j++) 
				{
					int	heightMax = mapFragment.getHeight(i, j,false);
					probArbre=random.nextInt(30*(100*heightMax)/mapHeightMax);
					if(probArbre==0) {
						FactoryTree.generate(mapFragment, i, j, TreeType.BIG_TREE, mapHeightMax, random);
					}
				}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		super.onGenerateEntities(mapFragment, groundHeight, seed, random);
	}
	
	@Override
	public void onChoosingSubModules(Populator chooser)
	{
		// TODO Auto-generated method stub
		
	}
}
