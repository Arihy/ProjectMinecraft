package generateur.module.sousmodule;

import generateur.map.MapFragment;
import generateur.module.Module;

import java.util.Random;

public abstract class SousModule
{
	private Module mModule;
	
	public void setParentModule(Module module)
	{
		mModule = module;
	}
	public Module getParentModule()
	{
		return mModule;
	}
	public void generateEntities(MapFragment mapFragment,int groundHeight, Random random)
	{
			generate(mapFragment,groundHeight, random);
	}
	protected abstract void generate(MapFragment mapFragment,int groundHeight, Random random);
	public abstract int getMinSizeX();
	public abstract int getMinSizeZ();
	public abstract int getMaxSizeX();
	public abstract int getMaxSizeZ();
}
