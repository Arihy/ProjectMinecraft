package generateur.module;

import generateur.map.MapOfHeightFragment;
import generateur.module.sousmodule.SousModuleDonjon;
import generateur.module.sousmodule.SousModuleForet;
import generateur.module.sousmodule.SousModuleLac;
import generateur.module.sousmodule.SousModuleVille;
import generateur.module.sousmodule.SousModuleVille2;

import java.util.Random;

import utils.perlin.Perlin;
import utils.perlin.Perlin.INTERPOLATION_TYPE;

public class ModulePlaineConstruction extends Module
{
	public ModulePlaineConstruction(Random random)
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
		Perlin.perlin(mapOfHeightFragment.getWorkingMatrice(),mapOfHeightFragment.getX(),mapOfHeightFragment.getZ(),mapOfHeightFragment.getZoneSizeX(),mapOfHeightFragment.getZoneSizeZ(), 10, 0.5f,INTERPOLATION_TYPE.linear);	}
	@Override
	public void onChoosingSubModules(Populator chooser)
	{
		chooser.setMaxSousModule(5);
		chooser.setSousModuleWeight(SousModuleDonjon.class, 0.4f);		
		chooser.setSousModuleWeight(SousModuleVille.class, 0.f);
		chooser.setSousModuleWeight(SousModuleLac.class, 0.2f);
		chooser.setSousModuleWeight(SousModuleVille2.class, 0.40f);
	}
}
