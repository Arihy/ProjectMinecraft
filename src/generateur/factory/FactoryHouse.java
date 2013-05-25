package generateur.factory;

import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Material;

public class FactoryHouse {
	public static void generate(MapFragment map, int largeur, int profondeur,
			int nbEtage, int oriX, int oriZ, Random random) {
		if (largeur > 6 && profondeur > 6) {
			try {
				/* Mise à plat */
				int ground = map.getHeight(oriX+1, oriZ+1, false);
				for (int i = oriX; i <= oriX + largeur; i++) {
					for (int j = oriZ; j <= oriZ + profondeur; j++) {
						for (int k = map.getHeight(i, j, false); k < ground; k++)
							map.setBlock(i, j, k, (short) Material.DIRT.getId());
						if(i!=oriX && i!= oriX+largeur && j!=oriZ && j!= oriZ+profondeur)
							map.setBlock(i, j, ground,
								(short) Material.COBBLESTONE.getId());
					}
				}
				/* Murs + étages */
				int etage = 0;
				while (etage < nbEtage) {
					for (int k = 1; k <= 5; k++) {
						for (int i = oriX + 1; i < oriX + largeur; i++) {
							int fenetre = 1;
							if (i != oriX + 1 && i != oriX + largeur - 1
									&& k == 2)
								fenetre = random.nextInt(5);
							if (fenetre != 0)
								map.setBlock(i, oriZ + 1, ground + 4 * etage
										+ k,
										(short) Material.COBBLESTONE.getId());
							else map.setBlock(i, oriZ + 1, ground + 4 * etage
									+ k,
									(short) Material.GLASS.getId());
							if (i != oriX + 1 && i != oriX + largeur - 1 && k==2)
								fenetre = random.nextInt(5);
							if (fenetre != 0)
								map.setBlock(i, oriZ + profondeur - 1, ground
										+ 4 * etage + k,
										(short) Material.COBBLESTONE.getId());
							else map.setBlock(i, oriZ + profondeur - 1, ground
									+ 4 * etage + k,
									(short) Material.GLASS.getId());
						}
						for (int j = oriZ + 1; j < oriZ + profondeur; j++) {
							int fenetre = 1;
							if (j != oriZ + 1 && j != oriZ + profondeur - 1 && k==2)
								fenetre = random.nextInt(5);
							if (fenetre != 0)
								map.setBlock(oriX+1, j, ground + 4 * etage + k,
										(short) Material.COBBLESTONE.getId());
							else map.setBlock(oriX+1, j, ground + 4 * etage + k,
									(short) Material.GLASS.getId());
							if (j != oriZ + 1 && j != oriZ + largeur - 1 && k==2)
								fenetre = random.nextInt(5);
							if (fenetre != 0)
								map.setBlock(oriX + largeur - 1, j, ground + 4
										* etage + k,
										(short) Material.COBBLESTONE.getId());
							else map.setBlock(oriX + largeur - 1, j, ground + 4
									* etage + k,
									(short) Material.GLASS.getId());
						}
					}
					for (int i = oriX + 1; i < oriX + largeur; i++) {
						for (int j = oriZ + 1; j < oriZ + profondeur; j++) {
							map.setBlock(i, j, ground + 4 * (etage + 1),
									(short) Material.COBBLESTONE.getId());
						}
					}
					etage++;
				}
				/* TOIT */
				for (int i = oriX+1; i < oriX + largeur; i++) {
					for (int j = oriZ+1; j < oriZ + profondeur; j++) {
						map.setBlock(i, j, ground + 4
								* (etage) + 2,
								(short) Material.WOOD.getId());
					}
				}
				/* BORDURES */
				for (int i = oriX; i <= oriX + largeur; i++) {
					map.setBlock(i, oriZ, ground + 4 * etage +1,
							(short) Material.WOOD.getId());
					map.setBlock(i, oriZ + profondeur, ground + 4 * etage +1,
							(short) Material.WOOD.getId());
				}
				for (int j = oriZ; j <= oriZ + profondeur; j++) {
					map.setBlock(oriX, j, ground + 4 * etage+1,
							(short) Material.WOOD.getId());
					map.setBlock(oriX + largeur, j, ground + 4 * etage+1,
							(short) Material.WOOD.getId());
				}
				
				/* PORTE */
				int mur=random.nextInt(4);
				int nPorte=0;
				switch(mur) {
				case 0:
					nPorte=random.nextInt(largeur-4);
					map.setBlock(oriX+nPorte+2, oriZ+1, ground+1,
							(short) Material.AIR.getId());
					map.setBlock(oriX+nPorte+2, oriZ+1, ground+2,
							(short) Material.AIR.getId());
					map.setBlock(oriX+nPorte+2, oriZ, ground,
							(short) Material.WOOD_STAIRS.getId());
					break;
				case 1:
					nPorte=random.nextInt(largeur-4);
					map.setBlock(oriX+nPorte+2, oriZ+profondeur-1, ground+1,
							(short) Material.AIR.getId());
					map.setBlock(oriX+nPorte+2, oriZ+profondeur-1, ground+2,
							(short) Material.AIR.getId());
					map.setBlock(oriX+nPorte+2, oriZ+profondeur, ground,
							(short) Material.WOOD_STAIRS.getId());
					break;
				case 2:
					nPorte=random.nextInt(profondeur-4);
					map.setBlock(oriX+1, oriZ+2+nPorte, ground+1,
							(short) Material.AIR.getId());
					map.setBlock(oriX+1, oriZ+2+nPorte, ground+2,
							(short) Material.AIR.getId());
					map.setBlock(oriX, oriZ+2+nPorte, ground,
							(short) Material.WOOD_STAIRS.getId());
					
					break;
				case 3: 
					nPorte=random.nextInt(profondeur-4);
					map.setBlock(oriX+largeur-1, oriZ+2+nPorte, ground+1,
							(short) Material.AIR.getId());
					map.setBlock(oriX+largeur-1, oriZ+2+nPorte, ground+2,
							(short) Material.AIR.getId());
					map.setBlock(oriX+largeur, oriZ+2+nPorte, ground,
							(short) Material.WOOD_STAIRS.getId());
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
