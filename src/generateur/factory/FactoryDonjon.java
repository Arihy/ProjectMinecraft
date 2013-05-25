package generateur.factory;

import generateur.AreaManager;
import generateur.AreaManager.Room;
import generateur.map.MapFragment;

import java.io.IOException;
import java.util.Random;

public abstract class FactoryDonjon
{
	public static void generateCrumaTower(MapFragment map,Random random)
	{

	}
	public static void generateForgotenMine(MapFragment map,Random random)
	{

	}
	public static void generateUnderground(MapFragment map,Random random) throws IOException
	{
		if (map.getZoneSizeX() > 100 && map.getZoneSizeZ() > 100)
		{
			int sizeX = (int) (100 + Math.min(100, map.getZoneSizeX()-100)*random.nextFloat());
			int sizeZ = (int) (100 + Math.min(100, map.getZoneSizeZ()-100)*random.nextFloat());
			int sizeY = (int) (50 + random.nextFloat()*((sizeX+sizeZ)/4));

			System.out.println("donjon size : "+sizeX+" "+sizeZ+" "+sizeY);
			
			int posX = (int) ((map.getZoneSizeX()-sizeX)*random.nextFloat());
			int posZ = (int) ((map.getZoneSizeZ()-sizeZ)*random.nextFloat());
			int posY = map.getHeight(posX, posZ, true);// TODO test 0 en Y

			boolean edit = true;

			AreaManager area = new AreaManager(posX,posZ,posY,sizeX, sizeZ, sizeY); 

			while (edit)
			{
				int rSizeX = (int) (10+random.nextFloat()*20);
				int rSizeZ = (int) (10+random.nextFloat()*20);
				int rSizeY = (int) (6+(Math.round(random.nextFloat()*2f))+((rSizeX+rSizeZ)/(2*50)));

				Room room = area.alocateRoom(rSizeX, rSizeZ, rSizeY);

				if (room != null)
				{
					//System.out.println("room ok : "+room.x+" "+room.z+" "+room.y+" "+room.sizeX+" "+room.sizeZ+" "+room.sizeY);
					envelopBedrock(map, room);
					int rand = random.nextInt(100);
					if (room.y == posY)
					{
						if (rand < 30)
							generateRoomMagma(map,room);
						else if (rand >= 30 && rand < 40)
							generateRoomSanctuar(map,room);
						else if (rand >= 40 && rand < 45)
							generateRoomTreasor(map,room);
						else
							generateRoomEmpty(map,room);
					}
					else
					{
						if (rand >= 30 && rand < 40)
							generateRoomSanctuar(map,room);
						else if (rand >= 40 && rand < 45)
							generateRoomTreasor(map,room);
						else
							generateRoomEmpty(map,room);
					}
				}
				else
				{
					edit = false;
				}
			}
		}
	}
	public static void generateBlackRock(MapFragment map,Random random)
	{

	}
	public static void generateFortress(MapFragment map,Random random)
	{

	}


	private static void generateRoomSanctuar(MapFragment map,Room room)
	{
		
	}
	private static void generateRoomEmpty(MapFragment map,Room room)
	{

	}
	private static void generateRoomTreasor(MapFragment map,Room room)
	{

	}
	private static void generateRoomMagma(MapFragment map,Room room)
	{

	}
	private static void envelopBedrock(MapFragment map,Room room) throws IOException
	{
		for (int x=room.x;x<room.sizeX+room.x;++x)
		{
			for (int z=room.z;z<room.sizeZ+room.z;++z)
			{
				map.setBlock(x, z, room.y, (short) 7);
				map.setBlock(x, z, room.y+room.sizeY-1, (short) 7);
			}
			for (int y=room.y;y<room.sizeY+room.y;++y)
			{
				map.setBlock(x, room.z, y,(short) 7);
				map.setBlock(x, room.z+room.sizeZ-1,y, (short) 7);
				for (int z=room.z;z<room.sizeZ+room.z;++z)
				{
					map.setBlock(room.x, z,y, (short) 7);
					map.setBlock(room.x+room.sizeX-1, z, y, (short) 7);
				}
			}
		}
	}

}
