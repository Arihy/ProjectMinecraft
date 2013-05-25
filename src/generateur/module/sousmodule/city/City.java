/**
 * @author Rémy Peru
 */

package generateur.module.sousmodule.city;

import generateur.map.MapFragment;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class City {

	MapFragment map;
	int startX, startZ;
	int districtSize;
	Rectangle mapRectangle;

	Random random;

	List<District> districts = new ArrayList<District>();
	List<District> districtsAvailable = new ArrayList<District>();
	    
	/**
	 * Constructeur
	 * @param map
	 * @param mapRectangle
	 * @param startX
	 * @param startZ
	 * @param districtSize
	 * @throws Exception
	 */
	public City(MapFragment map, Rectangle mapRectangle, int startX, int startZ, int districtSize, Random random) throws Exception
	{
	    //Coordonnées à partir desquels la ville sera construite
	    this.startX = startX;
	    this.startZ = startZ;
	    this.districtSize = districtSize;
	    this.map = map;
	    this.mapRectangle = mapRectangle;
	    this.random = random;

	    //Crée le chateau de départ
	    District districtDepart = new District(map, startX, startZ, RoadType.PLACE, districtSize, DistrictType.RICH, random);
	    if( mapRectangle.contains(districtDepart.GetRectangle()) )
	    {	    	
		    districts.add(districtDepart); //Ajoute le district de départ à liste des districts
		    AddNeighbourgs(districtDepart); // Ajoute tout ses voisins a la liste des districts possibles à créer
	
		    int nbDistricts = 8 + random.nextInt(15-8); // Nombre de district sans compter le quartier riche (chateau)
		    for (int i = 0; i < nbDistricts; i++)
		    {
		        //Supprime tout les districts possibles qui sont déja crées pour éviter de créer 2 districts au même endroit
		    	for (District d : districts)
		            for (int j = 0; j < districtsAvailable.size(); j++)
		            	if ( districtsAvailable.get(j).GetRectangle().equals(d.GetRectangle()) )
		                    districtsAvailable.remove(j);
	
		    	if(districtsAvailable.size() > 0)
		    	{
			        //Selectionne au hasard un district a construire de la liste des districts disponibles
			        int randomDistrict = random.nextInt(districtsAvailable.size());
		
			        //Ajoute le district choisi précédemment à liste des districts
			        districts.add(districtsAvailable.get(randomDistrict)); 
		
			        // Ajoute tout ses voisins a la liste des districts possibles à créer
			        AddNeighbourgs( districts.get( districts.size() - 1 ) );
		
			        //Supprime le district choisi de la liste des districts possibles à créer
			        districtsAvailable.remove(randomDistrict);
		    	}
		    }
	    }
	    else
	    	throw new Exception("districtDepart hors de la map");
	}

	/**
	 * Ajoute les voisins d'un district choisi à la liste des districts possibles
	 * si ils sont a l'interieur de la map
	 * @param d
	 */
	public void AddNeighbourgs(District d)
	{
	    List<RoadType>[] neighboursRoadList = d.GetNeighboursRoadList();
	    District testDistrict;
	    int coordX = d.coordX;
	    int coordZ = d.coordZ;
	    
	    if (neighboursRoadList[Direction.BAS.ordinal()].size() != 0)
	    {
	    	testDistrict = new District(map ,coordX - districtSize, coordZ, neighboursRoadList[Direction.BAS.ordinal()].get(random.nextInt(neighboursRoadList[Direction.BAS.ordinal()].size())), districtSize, DistrictType.POOR, random);
	    	if(mapRectangle.contains(testDistrict.GetRectangle()))
	    		districtsAvailable.add(testDistrict);
	    }
	    if (neighboursRoadList[Direction.HAUT.ordinal()].size() != 0)
	    {
	    	testDistrict = new District(map, coordX + districtSize, coordZ, neighboursRoadList[Direction.HAUT.ordinal()].get(random.nextInt(neighboursRoadList[Direction.HAUT.ordinal()].size())), districtSize, DistrictType.POOR, random);
	    	if(mapRectangle.contains(testDistrict.GetRectangle()))
	    		districtsAvailable.add(testDistrict);
	    }
	    if (neighboursRoadList[Direction.GAUCHE.ordinal()].size() != 0)
	    {
	    	testDistrict = new District(map, coordX, coordZ - districtSize, neighboursRoadList[Direction.GAUCHE.ordinal()].get(random.nextInt(neighboursRoadList[Direction.GAUCHE.ordinal()].size())), districtSize, DistrictType.POOR, random);
	    	if(mapRectangle.contains(testDistrict.GetRectangle()))
	    		districtsAvailable.add(testDistrict);
	    }
	    if (neighboursRoadList[Direction.DROITE.ordinal()].size() != 0)
	    {
	    	testDistrict = new District(map, coordX, coordZ + districtSize, neighboursRoadList[Direction.DROITE.ordinal()].get(random.nextInt(neighboursRoadList[Direction.DROITE.ordinal()].size())), districtSize, DistrictType.POOR, random);
	    	if(mapRectangle.contains(testDistrict.GetRectangle()))
	    		districtsAvailable.add(testDistrict);
	    } 
	}

	/**
	 * Génere la ville sur la carte
	 */
	public void Generate()
	{
			try 
			{
				for (District d : districts)
					d.Generate();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * Renvoie un rectangle englobant de la ville
	 * @return
	 */
	public Rectangle GetRectangle()
	{ 
	    int minX = startX, maxX = startX;
	    int minZ = startZ, maxZ = startZ;

	    for (District d : districts)
	    {
	        minX = Math.min(minX, d.coordX);
	        maxX = Math.max(maxX, d.coordX + districtSize);

	        minZ = Math.min(minZ, d.coordZ);
	        maxZ = Math.max(maxZ, d.coordZ + districtSize);
	    }

	    //System.out.println("minX = "+ minX +", maxX = "+ maxX +", minZ = "+ minZ +", maxZ = "+ maxZ);
	    return new Rectangle(minX, minZ, maxX-minX, maxZ-minZ);
	 }	     
}
