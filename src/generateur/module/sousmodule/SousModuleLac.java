package generateur.module.sousmodule;

import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Material;

import utils.perlin.Perlin;
import utils.perlin.Perlin.INTERPOLATION_TYPE;

public class SousModuleLac extends SousModule {
	@Override
	public int getMinSizeX() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int getMaxSizeX() {
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	public int getMinSizeZ() {
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int getMaxSizeZ() {
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	protected void generate(MapFragment mapFragment, int groundHeight,
			Random random) {
		System.out.println("Generation Lac (" + mapFragment.getX() + ";"
				+ mapFragment.getZ() + ")");

		int margeX = mapFragment.getZoneSizeX() / 5;
		int margeZ = mapFragment.getZoneSizeZ() / 5;

		int relief[][] = new int[mapFragment.getZoneSizeX()][mapFragment
				.getZoneSizeZ()];
		/* Hauteur Moyenne */
		int HauteurMoy = 10;
		
		for (int x = 0; x < mapFragment.getZoneSizeX(); x++)
			for (int z = 0; z < mapFragment.getZoneSizeZ(); z++) {
				if (x <= margeX || x >= mapFragment.getZoneSizeX() - margeX
						|| z <= margeZ
						|| z >= mapFragment.getZoneSizeZ() - margeZ)
					relief[x][z] = HauteurMoy+HauteurMoy/5;
				else
					relief[x][z] = random.nextInt(20);
			}

		Perlin.perlin(relief, 0, 0, mapFragment.getZoneSizeX(),
				mapFragment.getZoneSizeZ(), 30, 0.5f,
				INTERPOLATION_TYPE.noLinear);

		/* Generation */
		for (int x = 0; x < mapFragment.getZoneSizeX(); x++)
			for (int z = 0; z < mapFragment.getZoneSizeZ(); z++) {
				int sol;
				try {
					sol = mapFragment.getHeight(x, z, false);
					if (relief[x][z] >= HauteurMoy)
						for (int k = sol; k <= sol + relief[x][z] - HauteurMoy; k++)
							try {
								mapFragment.setBlock(x, z, k,
										(short) Material.DIRT.getId());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					else
						for (int k = sol-1; k > sol - (HauteurMoy - relief[x][z]); k--)
							try {
								if (relief[x][z] > 7)
									mapFragment.setBlock(x, z, k,
											(short) Material.AIR.getId());
								else if(k<groundHeight)
									mapFragment.setBlock(x, z, k,
											(short) Material.WATER.getId());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
