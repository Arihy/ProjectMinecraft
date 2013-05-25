package generateur.module.sousmodule;

import generateur.map.MapFragment;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;

public class SousModuleVille extends SousModule {

	static int maxLargeurMaison=8;
	static int maxProfondeurMaison=8;
	
	@Override
	protected void generate(MapFragment mapFragment, int groundHeight, Random random)
	{
		
		//int marge=Math.min(tailleX, tailleZ)/20;
		int tailleX=mapFragment.getZoneSizeX();
		int tailleZ=mapFragment.getZoneSizeZ();
		
		boolean mapCity[][]=new boolean[tailleX][tailleZ];
		for(int i=0; i<tailleX; i++)
			for(int j=0; j<tailleZ; j++)
				mapCity[i][j]=false;
		ArrayList<Point> listeOrigines = new ArrayList<Point>();
		System.out.println("City : "+tailleX+" : "+tailleZ);
		//generate(mapFragment, random, random.nextInt(2), random.nextInt(5)+3, mapCity, marge, marge, tailleX-2*marge, tailleZ-2*marge, listeOrigines);
		generate(mapFragment, random, random.nextInt(2), random.nextInt(5)+3, mapCity, 0, 0, tailleX, tailleZ, listeOrigines);
		
		// Implantation de maisons
		/*for(Point p : listeOrigines) {
			int largeur=0, profondeur=0;
			// Largeur (axe X)
			while (p.x+largeur<tailleX && largeur<maxLargeurMaison && mapCity[p.x+largeur][p.y]) {
				largeur++;
			}
			while (p.y+profondeur<tailleZ && profondeur<maxProfondeurMaison && mapCity[p.x][p.y+profondeur]) {
				profondeur++;
			}
			if(largeur>0 && profondeur>0)
				FactoryHouse.generate(mapFragment, largeur, profondeur, random.nextInt(3)+1, p.x, p.y);
			for(Point p2 : listeOrigines)
				if(p2!=p && p2.x<=p.x+largeur && p2.x<=p.y+profondeur)
					listeOrigines.remove(p2);
		}*/
	}

	private void generate(MapFragment mapFragment, Random random, int choix, int rang, boolean map[][], int oriX, int oriZ, int tailleX, int tailleZ, ArrayList<Point> listeOrigines) {
		if(tailleX/2>maxLargeurMaison && tailleZ/2>maxProfondeurMaison)
			if(rang>0) {
				switch(choix) {
					case 0 : {
						/* ----- */
						for(int k=oriX; k<oriX+tailleX; k++) {
							if(maxProfondeurMaison>tailleZ)
								for(int e=1; e<maxProfondeurMaison; e++) {
									if(tailleZ/2+e<tailleZ) map[k][oriZ+tailleZ/2+e]=true;
									if(tailleZ/2-e>oriZ) map[k][oriZ+tailleZ/2-e]=true;
								}
							try {
								mapFragment.setBlock(k, oriZ+tailleZ/2, mapFragment.getHeight(k, oriZ+tailleZ/2, true)-1,(short) Material.GRAVEL.getId());
							} catch (IOException e1) {
								e1.printStackTrace();	
							}
							listeOrigines.add(new Point(k, oriZ+tailleZ/2+1));
							listeOrigines.add(new Point(k, oriZ+tailleZ/2-1));
						}
						if (rang>1) {
							generate(mapFragment, random, random.nextInt(2), rang-1, map, oriX, oriZ, tailleX, tailleZ/2, listeOrigines);
							generate(mapFragment, random, random.nextInt(2), rang-1, map, oriX, oriZ+tailleZ/2, tailleX, tailleZ/2, listeOrigines);
						}
						break;
					}
					case 1 : {
						/* 
						 * |
						 * |
						 * 
						 */
						for(int k=oriZ; k<oriZ+tailleZ; k++) {
							if(maxLargeurMaison>tailleX)
								for(int e=1; e<maxLargeurMaison; e++) {
									if(tailleX/2+e<tailleX) map[oriX+tailleX/2+e][k]=true;
									if(tailleX/2-e>oriX) map[oriX+tailleX/2-e][k]=true;
								}
							try {
								mapFragment.setBlock(oriX+tailleX/2, k, mapFragment.getHeight(oriX+tailleX/2, k, true)-1,(short) Material.GRAVEL.getId());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							listeOrigines.add(new Point(oriX+tailleX/2+1, k));
							listeOrigines.add(new Point(oriX+tailleX/2-1, k));
						}
						if(rang>1) {
							generate(mapFragment, random, random.nextInt(2), rang-1, map, oriX, oriZ, tailleX/2, tailleZ, listeOrigines);
							generate(mapFragment, random, random.nextInt(2), rang-1, map, oriX+tailleX/2, oriZ, tailleX/2, tailleZ, listeOrigines);
						}
						break;
					}
				}
			
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
		return 50;
	}

	@Override
	public int getMinSizeZ()
	{
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	public int getMaxSizeZ()
	{
		// TODO Auto-generated method stub
		return 200;
	}

}
