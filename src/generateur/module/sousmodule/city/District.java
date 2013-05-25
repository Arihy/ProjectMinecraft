/**
 * @author Rémy Peru
 */

package generateur.module.sousmodule.city;

import generateur.factory.FactoryHouse;
import generateur.map.MapFragment;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;
import java.util.Random;
import org.bukkit.Material;

public class District {
	int districtSize;

    public int coordX;
    public int coordZ;

    RoadType roadType;
    DistrictType districtType;

    public boolean hasRemparts = false;
    public boolean isCreated = false;

    Random random;

    @SuppressWarnings("unchecked")
	List<RoadType>[] voisinsPossibles = new List[4];
    boolean[][] collisionMatrix;
    
    MapFragment map;

    /**
     * Constructeur
     * @param map
     * @param startX
     * @param startZ
     * @param roadType
     * @param districtSize
     * @param districtType
     */
    public District(MapFragment map,int startX, int startZ, RoadType roadType, int districtSize, DistrictType districtType, Random random)
    { 
        //Initialise les variables
    	this.map = map;
        this.districtSize = districtSize;
        this.roadType = roadType;
        this.districtType = districtType;
        this.random = random;

        coordX = startX;
        coordZ = startZ;

        collisionMatrix = new boolean[districtSize][districtSize];
        for(int i = 0; i < districtSize; i++)
            for(int j = 0; j < districtSize; j++)
                collisionMatrix[i][j] = true;

        if (districtType == DistrictType.RICH)
            hasRemparts = true;
        
        for(int i = 0 ; i < 4 ; i++) // Initialise les ArrayList
        	voisinsPossibles[i] = new ArrayList<RoadType>();
        
    }

    /**
     * Génere le district
     * @throws Exception 
     */
    public void Generate() throws Exception
    {
        if (!isCreated)
        {
        	//le sol du chateau
        	if(districtType == DistrictType.RICH)
        	{		
        		//Chercher hauteur min sol
        		int groundHeight = 10000;
        		
        		try {
        		//gauche
        		for (int x = coordX; x < coordX + districtSize; x++)
                        for (int z = 0; z < 3; z++)
                        	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
        		//droite
                for (int x = coordX; x < coordX + districtSize; x++)
                        for (int z = 0 ; z < 3 ; z++)
                        	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
              //haut
                for (int z = coordZ; z < coordZ + districtSize; z++)
                        for (int x = 0; x < 3; x++)
                        	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
              //bas
                for (int z = coordZ; z < coordZ + districtSize; z++)
                        for (int x = 0; x < 3; x++)
                        	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		LevelArea(groundHeight);
        		Ground(groundHeight);
        	}
	
	        //Les routes
			MainRoad();
	
			//Les remparts
	        if (hasRemparts)
	            Remparts(10);
	
	        //Les maisons
	        PlaceBuilding();
	
	        isCreated = true;
        }
        else
        	throw new Exception("Ce district existe déja");
    }

    //
    /**
     * Retourne un tableau de liste correspondant aux types de routes que l'on peut 
     * construire en fonction de sa position par rapport au district
     * @return
     */
    public List<RoadType>[] GetNeighboursRoadList()
    {
        switch (roadType)
        {
            case VERTICALE:
                voisinsPossibles[Direction.BAS.ordinal()] = asList(RoadType.VERTICALE, RoadType.CROISEMENT);
                voisinsPossibles[Direction.HAUT.ordinal()] = asList( RoadType.VERTICALE, RoadType.CROISEMENT, RoadType.TOURNANT_BAS_GAUCHE, RoadType.TOURNANT_BAS_DROITE );
                voisinsPossibles[Direction.GAUCHE.ordinal()] = asList();
                voisinsPossibles[Direction.DROITE.ordinal()] = asList();
                break;
            case HORIZONTALE:
            	voisinsPossibles[Direction.BAS.ordinal()] = asList();
                voisinsPossibles[Direction.HAUT.ordinal()] = asList();
                voisinsPossibles[Direction.GAUCHE.ordinal()] = asList( RoadType.HORIZONTALE, RoadType.CROISEMENT, RoadType.TOURNANT_BAS_DROITE );
                voisinsPossibles[Direction.DROITE.ordinal()] = asList( RoadType.HORIZONTALE, RoadType.CROISEMENT, RoadType.TOURNANT_BAS_GAUCHE ); 
                break;
            case CROISEMENT:
                voisinsPossibles[Direction.BAS.ordinal()] = asList( RoadType.VERTICALE );
                voisinsPossibles[Direction.HAUT.ordinal()] = asList( RoadType.VERTICALE );
                voisinsPossibles[Direction.GAUCHE.ordinal()] = asList( RoadType.HORIZONTALE );
                voisinsPossibles[Direction.DROITE.ordinal()] = asList( RoadType.HORIZONTALE ); 
                break;
            case PLACE:
                voisinsPossibles[Direction.BAS.ordinal()] = asList( RoadType.VERTICALE, RoadType.CROISEMENT );
                voisinsPossibles[Direction.HAUT.ordinal()] = asList( RoadType.VERTICALE, RoadType.CROISEMENT );
                voisinsPossibles[Direction.GAUCHE.ordinal()] = asList( RoadType.HORIZONTALE, RoadType.CROISEMENT );
                voisinsPossibles[Direction.DROITE.ordinal()] = asList( RoadType.HORIZONTALE, RoadType.CROISEMENT );
                break;
            case TOURNANT_BAS_GAUCHE:
                voisinsPossibles[Direction.BAS.ordinal()] = asList( RoadType.VERTICALE, RoadType.CROISEMENT );
                voisinsPossibles[Direction.HAUT.ordinal()] = asList();
                voisinsPossibles[Direction.GAUCHE.ordinal()] = asList( RoadType.HORIZONTALE, RoadType.CROISEMENT );
                voisinsPossibles[Direction.DROITE.ordinal()] = asList();
                break;
            case TOURNANT_BAS_DROITE:
                voisinsPossibles[Direction.BAS.ordinal()] = asList( RoadType.VERTICALE, RoadType.CROISEMENT );
                voisinsPossibles[Direction.HAUT.ordinal()] = asList();
                voisinsPossibles[Direction.GAUCHE.ordinal()] = asList();
                voisinsPossibles[Direction.DROITE.ordinal()] = asList( RoadType.HORIZONTALE, RoadType.CROISEMENT );
                break;
        }

        return voisinsPossibles;
    }
    
    /**
     * Génere le sol
     * @throws IOException 
     */
	private void Ground(int groundHeight) throws IOException
    {
		//Place le sol du chateau
    	if(districtType == DistrictType.RICH)
    	{
	        for (int x = coordX + 1; x < coordX + districtSize - 1; x++)
	            for (int z = coordZ + 1; z < coordZ + districtSize - 1; z++)
	                    SetID(x, groundHeight - 1, z, (short) Material.STONE.getId());
    	}
    }

    /**
     * Place les routes dans le district
     * @throws Exception 
     */
	@SuppressWarnings("incomplete-switch")
	private void MainRoad() throws Exception
    {
        int center = (districtSize + 1) / 2;
        int offsetRoad = center - 1 - 2;

        switch (roadType)
        {
            case VERTICALE:
                for (int x = coordX; x < coordX + districtSize; x++)
                    for (int z = coordZ + offsetRoad; z < coordZ + offsetRoad + 5; z++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix(x, z);
                    }
                break;

            case HORIZONTALE:
                for (int z = coordZ; z < coordZ + districtSize; z++)
                    for (int x = coordX + offsetRoad; x < coordX + offsetRoad + 5; x++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix( x, z);
                    }
                break;

            case CROISEMENT:
                for (int x = coordX; x < coordX + districtSize; x++)
                    for (int z = coordZ + offsetRoad; z < coordZ + offsetRoad + 5; z++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix(x, z);
                    }

                for (int z = coordZ; z < coordZ + districtSize; z++)
                    for (int x = coordX + offsetRoad; x < coordX + offsetRoad + 5; x++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix( x, z);
                    }
                break;

            case TOURNANT_BAS_GAUCHE:
                for (int x = coordX; x < coordX + districtSize/2 + 3; x++) //Pour former un route complète
                    for (int z = coordZ + offsetRoad; z < coordZ + offsetRoad + 5; z++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix(x, z);
                    }
                for (int z = coordZ; z < coordZ + districtSize/2; z++)
                    for (int x = coordX + offsetRoad; x < coordX + offsetRoad + 5; x++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix(x, z);
                    }
                break;

            case TOURNANT_BAS_DROITE:
                for (int x = coordX; x < coordX + districtSize / 2 + 3; x++) //Pour former un route complète
                    for (int z = coordZ + offsetRoad; z < coordZ + offsetRoad + 5; z++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix(x, z);
                    }

                for (int z = coordZ + districtSize / 2; z < coordZ + districtSize; z++)
                    for (int x = coordX + offsetRoad; x < coordX + offsetRoad + 5; x++)
                    {
                        SetID(x, map.getHeight(x, z, true) - 1, z, (short) Material.GRAVEL.getId());
                        UpdateCollisionMatrix(x, z);
                    }
                break;

        }
    }

    /**
     * Selectionne les endroits possibles pour construire des batiments et les créés au hasard
     */
    private void PlaceBuilding()
    {
    	int nbBuilding = 0;
    	
        for (int i = 0; i <= 500; i++)
        {
            //Dimensions du batiment
            int length = 10 + random.nextInt(16 - 10);
            int width = 10 + random.nextInt(16 - 10);
            width = length;
            int nbEtages = 1 + random.nextInt(3 - 1);
            int espaceEntreBuildings = 5;

            boolean isBuildable = true;

            int spotX;
            int spotZ;

            //Il faut prendre en compte les créneaux
            if(districtType == DistrictType.RICH)
            {
            	spotX = (coordX + 6) + random.nextInt(districtSize - (width + 6));
                spotZ = (coordZ + 6) + random.nextInt(districtSize - (length + 6));
            }
            else
            {
            	spotX = (coordX + 1) + random.nextInt(districtSize - (width + 1));
                spotZ = (coordZ + 1) + random.nextInt(districtSize - (length + 1));
            }
            	
	        for (int x = spotX; x < spotX + width; x++)
	            for (int z = spotZ; z < spotZ + length; z++)
	                if (collisionMatrix[x - coordX][z - coordZ] == false)
	                    isBuildable = false;
	            
	        if (isBuildable)
	        {
	        	FactoryHouse.generate(map, width - espaceEntreBuildings, length - espaceEntreBuildings, nbEtages, spotX, spotZ, random);
	            
		        for (int x = spotX; x < spotX + width; x++)
		            for (int z = spotZ; z < spotZ + length; z++)
		            	UpdateCollisionMatrix(x, z);
		        
		        nbBuilding++;
	        }
	        
	        if(nbBuilding >= 10)
	        	break;
        }
    }

    /**
     * Met a jour la matrice des collisions
     * @param x
     * @param z
     */
    private void UpdateCollisionMatrix(int x, int z)
    {
        int i = x - coordX;
        int j = z - coordZ;

        collisionMatrix[i][j] = false;
    }

    /**
     * Crée des remparts autour du district
     * @param rempartHeight
     */
    private void Remparts(int rempartHeight)
    {
		//Chercher hauteur min sol pour remparts
		int groundHeight = 10000;
		
		try {
		//Remparts gauche
		for (int x = coordX + 1; x < coordX + districtSize - 1; x++)
                for (int z = 1; z < 4; z++)
                	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
		//Remparts droite
        for (int x = coordX + 1; x < coordX + districtSize - 1; x++)
                for (int z = 0 ; z < 3 ; z++)
                	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
      //Remparts haut
        for (int z = coordZ + 1; z < coordZ + districtSize - 1; z++)
                for (int x = 0; x < 3; x++)
                	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
      //Remparts bas
        for (int z = coordZ + 1; z < coordZ + districtSize - 1; z++)
                for (int x = 1; x < 4; x++)
                	groundHeight = Math.min(groundHeight, map.getHeight(x, z, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		//Génération

        //Remparts gauche
        for (int x = coordX + 1; x < coordX + districtSize - 1; x++)
            for (int y = 0; y < rempartHeight; y++)
                for (int z = 1; z < 4; z++)
                {
                    SetID(x, groundHeight + y, coordZ + z, (short) Material.SMOOTH_BRICK.getId());
                    UpdateCollisionMatrix(x, coordZ + z);
                }
        //Créneaux
        for (int x = coordX; x < coordX + districtSize; x++)
            for (int y = 0; y < 2; y++)
                SetID(x, groundHeight + rempartHeight - 1 + y, coordZ, (short) Material.SMOOTH_BRICK.getId());
        
        for (int x = coordX; x < coordX + districtSize; x += 2)
            SetID(x, groundHeight + rempartHeight + 1, coordZ, (short) Material.SMOOTH_BRICK.getId());
        
        for (int x = coordX + 4; x < coordX + districtSize - 4; x++)
            for (int y = 0; y < 2; y++)
                SetID(x, groundHeight + rempartHeight - 1 + y, coordZ + 4, (short) Material.SMOOTH_BRICK.getId());
        
        for (int x = coordX + 4; x < coordX + districtSize - 4; x += 2)
            SetID(x, groundHeight + rempartHeight + 1, coordZ + 4, (short) Material.SMOOTH_BRICK.getId());



        //Remparts droite
        for (int x = coordX + 1; x < coordX + districtSize - 1; x++)
            for (int y = 0; y < rempartHeight; y++)
                for (int z = 0 ; z < 3 ; z++)
                {
                    SetID(x, groundHeight + y, coordZ + districtSize - z - 2, (short) Material.SMOOTH_BRICK.getId());
                    UpdateCollisionMatrix(x, coordZ + districtSize - z - 2);
                }
        //Créneaux
        for (int x = coordX; x < coordX + districtSize; x++)
            for (int y = 0; y < 2; y++)
                SetID(x, groundHeight + rempartHeight - 1 + y, coordZ + districtSize - 1, (short) Material.SMOOTH_BRICK.getId());
        
        for (int x = coordX; x < coordX + districtSize; x += 2)
            SetID(x, groundHeight + rempartHeight + 1, coordZ + districtSize - 1, (short) Material.SMOOTH_BRICK.getId());
        
        for (int x = coordX + 4; x < coordX + districtSize - 4; x++)
            for (int y = 0; y < 2; y++)
                SetID(x, groundHeight + rempartHeight - 1 + y, coordZ + districtSize - 5, (short) Material.SMOOTH_BRICK.getId());
        
        for (int x = coordX + 4; x < coordX + districtSize - 4; x += 2)
            SetID(x, groundHeight + rempartHeight + 1, coordZ + districtSize - 5, (short) Material.SMOOTH_BRICK.getId());


        //Remparts bas
        for (int z = coordZ + 1; z < coordZ + districtSize - 1; z++)
            for (int y = 0; y < rempartHeight; y++)
                for (int x = 1; x < 4; x++)
                {
                    SetID(coordX + x, groundHeight + y, z, (short) Material.SMOOTH_BRICK.getId());
                    UpdateCollisionMatrix(coordX + x, z);
                }
        //Créneaux
        for (int z = coordZ; z < coordZ + districtSize; z++)
            for (int y = 0; y < 2; y++)
                SetID(coordX, groundHeight + rempartHeight - 1 + y, z, (short) Material.SMOOTH_BRICK.getId());
        
        for (int z = coordZ; z < coordZ + districtSize; z += 2)
            SetID(coordX, groundHeight + rempartHeight + 1, z, (short) Material.SMOOTH_BRICK.getId());
        
        for (int z = coordZ + 4; z < coordZ + districtSize - 4; z++)
            for (int y = 0; y < 2; y++)
                SetID(coordX + 4, groundHeight + rempartHeight - 1 + y, z, (short) Material.SMOOTH_BRICK.getId());
        
        for (int z = coordZ + 4; z < coordZ + districtSize - 4; z += 2)
            SetID(coordX + 4, groundHeight + rempartHeight + 1, z, (short) Material.SMOOTH_BRICK.getId());

        //Remparts haut
        for (int z = coordZ + 1; z < coordZ + districtSize - 1; z++)
            for (int y = 0; y < rempartHeight; y++)
                for (int x = 0; x < 3; x++)
                {
                    SetID(coordX + districtSize - x - 2, groundHeight + y, z, (short) Material.SMOOTH_BRICK.getId());
                    UpdateCollisionMatrix(coordX + districtSize - x - 2, z);
                }
        //Créneaux
        for (int z = coordZ; z < coordZ + districtSize; z++)
            for (int y = 0; y < 2; y++)
                SetID(coordX + districtSize - 1, groundHeight + rempartHeight - 1 + y, z, (short) Material.SMOOTH_BRICK.getId());
        
        for (int z = coordZ; z < coordZ + districtSize; z += 2)
            SetID(coordX + districtSize - 1, groundHeight + rempartHeight + 1, z, (short) Material.SMOOTH_BRICK.getId());
        
        for (int z = coordZ + 4; z < coordZ + districtSize - 4; z++)
            for (int y = 0; y < 2; y++)
                SetID(coordX + districtSize - 5, groundHeight + rempartHeight - 1 + y, z, (short) Material.SMOOTH_BRICK.getId());
        
        for (int z = coordZ + 3 + 1; z < coordZ + districtSize - 3 - 1; z += 2)
            SetID(coordX + districtSize - 5, groundHeight + rempartHeight + 1, z, (short) Material.SMOOTH_BRICK.getId());

        
        //Créer les portes des remparts
        //gauche
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
                for (int z = 1; z < 4; z++)
                    SetID(coordX + (int)Math.floor((double)(districtSize / 2)) - 2 + x, groundHeight + y, coordZ + z, (short) Material.AIR.getId());

        //droite
        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++)
                for (int z = 1; z < 4; z++)
                    SetID(coordX + (int)Math.floor((double)(districtSize / 2)) - 2 + x, groundHeight + y, coordZ + districtSize - z - 1, (short) Material.AIR.getId());

        //bas
        for (int z = 0; z < 5; z++)
            for (int y = 0; y < 5; y++)
                for (int x = 1; x < 4; x++)
                    SetID(coordX + x, groundHeight + y, coordZ + (int)Math.floor((double)(districtSize / 2)) - 2 + z, (short) Material.AIR.getId());

        //haut
        for (int z = 0; z < 5; z++)
            for (int y = 0; y < 5; y++)
                for (int x = 1; x < 4; x++)
                    SetID(coordX + districtSize - x - 1, groundHeight + y, coordZ + (int)Math.floor((double)(districtSize / 2)) - 2 + z, (short) Material.AIR.getId());
    }  

    /**
     * Apelle la fonction setBlocks de la classe map avec les bons parametres
     * @param x
     * @param y
     * @param z
     * @param material
     */
    private void SetID(int x, int y, int z, short material)
    {
    	try {
			map.setBlock(x,z,y,material);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Renvoie un rectangle de la taille du district
     * @return
     */
    public Rectangle GetRectangle()
    {
    	return new Rectangle(coordX, coordZ, districtSize, districtSize);
    }
    
    private void LevelArea(int groundHeight)
    {
    	for (int x = coordX; x < coordX + districtSize; x++)
            for (int z = coordZ; z < coordZ + districtSize; z++)
            	 for (int y = 0; y < 10; y++)
                    SetID(x, groundHeight + y, z, (short) Material.AIR.getId());
    }
}
