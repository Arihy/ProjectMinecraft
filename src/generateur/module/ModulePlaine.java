package generateur.module;

import generateur.map.MapOfHeightFragment;
import generateur.module.sousmodule.SousModuleForet;
import generateur.module.sousmodule.SousModuleLac;

import java.util.Random;

import utils.perlin.Perlin;
import utils.perlin.Perlin.INTERPOLATION_TYPE;

public class ModulePlaine extends Module
{
	public ModulePlaine(Random random)
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
					mapOfHeightFragment.setHeight(i, j, groundHeight-2+(random.nextInt(10)+mapOfHeightFragment.getX()+mapOfHeightFragment.getZ())%8);
			}
		}
		Perlin.perlin(mapOfHeightFragment.getWorkingMatrice(),mapOfHeightFragment.getX(),mapOfHeightFragment.getZ(),mapOfHeightFragment.getZoneSizeX(),mapOfHeightFragment.getZoneSizeZ(), 10, 0.5f,INTERPOLATION_TYPE.linear);
	}
	@Override
	public void onChoosingSubModules(Populator chooser)
	{
		chooser.setMaxSousModule(3);
		chooser.setSousModuleWeight(SousModuleForet.class, 0.5f);
		chooser.setSousModuleWeight(SousModuleLac.class, 0.5f);
	}
}
