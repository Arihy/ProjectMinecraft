package generateur.factory;

import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.material.Leaves;
import org.bukkit.material.Tree;

class FactoryBIG_TREE extends FactoryTree {

	public static void generate(MapFragment map, int px, int pz, TreeType TT,
			int hauteurMax, Random random) {

		try {
			/* MARGE NECESSAIRE */
			int espace = 5;
			if (!(px - espace < 0 || pz - espace < 0 || px + espace > map.getZoneSizeX() || pz + espace > map
					.getZoneSizeZ())) {
				Leaves feuille = new Leaves();
				feuille.setSpecies(TreeSpecies.GENERIC);
				Tree arbre = new Tree();
				arbre.setSpecies(TreeSpecies.GENERIC);
				int hauteurSol = map.getHeight(px, pz, false);
				int height = 0;
				if (hauteurMax > 15 + hauteurSol)
					height = 10 + random.nextInt(5);
				else
					height = hauteurMax - 1;
				int hauteurTronc = height / 3 + random.nextInt(height / 3);
				for (int i = 0; i < hauteurTronc; i++) {
					map.setBlock(px, pz, i + hauteurSol,
							(short) arbre.getItemTypeId());
				}
				for (int i = hauteurTronc + 1; i <= height; i++) {
					placerFeuille(map, px, pz, i + hauteurSol, feuille);
				}
				for (int i = 3 + random.nextInt(2); i < height; i++) {
					genererEtageFeuille(map, px, pz, i + hauteurSol, espace, feuille,
							random);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void genererEtageFeuille(MapFragment map, int px, int pz,
			int niveau, int espace, Leaves feuille, Random random) {
		int largeurX = 2 + random.nextInt(espace-2);
		int largeurZ = 2 + random.nextInt(espace-2);
		for (int i = px - largeurX; i < px + largeurX; i++) {
			for (int j = pz - largeurZ; j < pz + largeurZ; j++) {
				placerFeuille(map, i, j, niveau, feuille);
			}
		}
	}

}
