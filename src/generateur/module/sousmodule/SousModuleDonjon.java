package generateur.module.sousmodule;

import generateur.factory.FactoryDonjon;
import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

public class SousModuleDonjon extends SousModule
{
	@Override
	protected void generate(MapFragment mapFragment, int groundHeight,Random random)
	{
		System.out.println("generation donjon en x:"+mapFragment.getX()+" z:"+mapFragment.getZ());
		//TODO test
		try		// TODO test
		{
			FactoryDonjon.generateUnderground(mapFragment, random);
		}
		catch (IOException e)		// TODO test
		{
	
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