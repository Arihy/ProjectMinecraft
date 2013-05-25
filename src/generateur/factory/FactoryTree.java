package generateur.factory;

import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

import org.bukkit.TreeType;
import org.bukkit.material.Leaves;

public class FactoryTree {
	
	public static void generate(MapFragment map, int px, int pz, TreeType TT, int hauteurMax, Random random) {
		if(TT==TreeType.BIG_TREE) {
			FactoryBIG_TREE.generate(map, px, pz, TT, hauteurMax, random);
		}
	}
	
	protected static void placerFeuille(MapFragment map, int px, int pz, int py, Leaves LT) {
		try
		{
			if(map.getBlock(px, pz, py)==0)
				map.setBlock(px, pz, py, (short)LT.getItemTypeId());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
