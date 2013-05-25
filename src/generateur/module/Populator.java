package generateur.module;

import generateur.module.sousmodule.SousModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Populator
{
	private HashMap<Class<? extends SousModule>, Float> mSousModules;
	private int mMaxSousModule = 5;

	public Populator()
	{
		mSousModules = new HashMap<Class<? extends SousModule>, Float>();
	}
	public void setMaxSousModule(int nb)
	{
		if (nb < 0)
			throw new IllegalArgumentException("must be > 0");
		mMaxSousModule = nb;
	}
	public ArrayList<SousModule> getResult(Random random)
	{
		ArrayList<SousModule> sousModules = new ArrayList<SousModule>();

		if (mSousModules.size() == 0)
			return sousModules;
		float totalWeight = 0.0f;
		for (Float weight : mSousModules.values())
		{
			totalWeight += weight;
		}
		while (sousModules.size() < mMaxSousModule)
		{
			float randomValue = random.nextFloat()*totalWeight;
			for (Class<? extends SousModule> sousModuleClass : mSousModules.keySet())
			{
				float weight = mSousModules.get(sousModuleClass);
				if (randomValue >= 0 && randomValue < weight)
				{
					try
					{
						sousModules.add(sousModuleClass.newInstance());
						break;
					}
					catch (InstantiationException e)
					{
						e.printStackTrace();
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
				else
					randomValue -= weight;
			}
		}
		return sousModules;
	}
	public void setSousModuleWeight(Class<? extends SousModule> sousModuke, float weight)
	{
		mSousModules.put(sousModuke, weight);
	}
}
