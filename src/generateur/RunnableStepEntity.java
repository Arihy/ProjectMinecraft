package generateur;

import generateur.module.Module;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class RunnableStepEntity implements Runnable
{
	private int mSeed,mGroundHeight;
	private Module mModule;
	private Semaphore mSemaphoreSync;
	
	public RunnableStepEntity(Semaphore semaphoreSync,Module module,int seed,int groundHeight)
	{
		mSeed = seed;
		mGroundHeight = groundHeight;
		mModule = module;
		mSemaphoreSync = semaphoreSync;
	}
	@Override
	public void run()
	{
		try
		{
			mModule.generateEntities(mGroundHeight, mSeed);
			mSemaphoreSync.release();
		}
		catch (IOException e)
		{
			throw new RuntimeException("PB");
		}

	}
	
}
