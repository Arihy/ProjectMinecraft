package generateur.module.sousmodule;

import generateur.map.MapFragment;

import java.awt.Rectangle;
import java.util.Random;

import generateur.module.sousmodule.city.City;

public class SousModuleVille2 extends SousModule{

	@Override
	protected void generate(MapFragment mapFragment, int groundHeight, Random random)
	{

		int width=mapFragment.getZoneSizeX();
		int length=mapFragment.getZoneSizeZ();
		
		Rectangle mapRectangle = new Rectangle(0, 0 , width , length);
		
		try 
		{
			//Calcule taille ville
			int districtSize = 55;
			
			if(Math.min(width, length) <= districtSize)
				throw new Exception("MapFragment trop petit pour construire une ville");
			else
			{
				int posX = random.nextInt(width - districtSize);
				int posZ = random.nextInt(length - districtSize);
				
				posX = 2;
				posZ = 2;
				
				City testCity;
				testCity = new City(mapFragment, mapRectangle, posX, posZ, districtSize, random);
			
				Rectangle cityRectangle = testCity.GetRectangle();
				
				if(mapRectangle.contains(cityRectangle))
				{  
			        //Genere la ville
			        System.out.println("Generation de la ville en x:"+mapFragment.getX()+" z:"+mapFragment.getZ());
			        testCity.Generate();
			        System.out.println("Ville genere");
				}
				else
					System.out.println("La ville est en dehors de la map");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	public int getMinSizeX() {
		// TODO Auto-generated method stub
		return 60;
	}

	@Override
	public int getMaxSizeX() {
		// TODO Auto-generated method stub
		return 300;
	}

	@Override
	public int getMinSizeZ() {
		// TODO Auto-generated method stub
		return 60;
	}

	@Override
	public int getMaxSizeZ() {
		// TODO Auto-generated method stub
		return 300;
	}

}
