package generateur;

import generateur.module.Module;

import java.util.concurrent.Semaphore;

public class RunnableStepGeometry implements Runnable
{
	private int mSeed,mGroundHeight;
	private Module mModule;
	private Semaphore mSemaphoreSync;
	private Semaphore mSemaphoreLimit;
	
	public RunnableStepGeometry(Semaphore semaphoreSync,Semaphore semaphoreLimit,Module module,int seed,int groundHeight)
	{
		mSeed = seed;
		mGroundHeight = groundHeight;
		mModule = module;
		mSemaphoreSync = semaphoreSync;
		mSemaphoreLimit = semaphoreLimit;
	}
	@Override
	public void run()
	{
		try
		{
			mSemaphoreLimit.acquire();
			mModule.generateGround(mGroundHeight, mSeed);
			mSemaphoreLimit.release();
			mSemaphoreSync.release();
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException();
		}
		
	}

}
