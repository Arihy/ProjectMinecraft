package test;

import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class GeneratorPlugin extends JavaPlugin implements Listener
{
	private ChunkGenerator mMyGenerator;
	
	@Override
	public void onEnable()
	{
		mMyGenerator = new MyGenerator();
		super.onEnable();
	}
	@Override
	public void onDisable()
	{
		super.onDisable();
	}
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) 
	{
		return mMyGenerator;
	}
}
