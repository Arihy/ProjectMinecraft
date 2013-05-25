package generateur;

import java.util.ArrayList;

public class AreaManager
{
	private int mX,mZ,mY;
	private int mSizeX,mSizeZ,mSizeY;
	private ArrayList<Room> mRooms;

	public AreaManager(int pX,int pZ,int pY,int sizeX,int sizeZ,int sizeY)
	{
		mX = pX;
		mZ = pZ;
		mY = pY;
		mSizeX = sizeX;
		mSizeZ = sizeZ;
		mSizeY = sizeY;
		mRooms = new ArrayList<AreaManager.Room>();
	}
	public int getAreaX()
	{
		return mX;
	}
	public int getAreaZ()
	{
		return mZ;
	}
	public int getAreaY()
	{
		return mY;
	}
	public int getAreaSizeX()
	{
		return mSizeX;
	}
	public int getAreaSizeZ()
	{
		return mSizeZ;
	}
	public int getAreaSizeY()
	{
		return mSizeY;
	}
	public ArrayList<Room> getRooms()
	{
		return mRooms;
	}
	public Room alocateRoom(int sizeX,int sizeZ,int sizeY)
	{
		// TODO moyen d'optimise en limitant la recherche par rappart a la position et la taille sur chaque axe
		//      (pb actuel trop de parcour)
		
		int x=mX,z=mZ,y=mY;
		int nextX,nextZ,nextY;
		boolean goodPos;
		do
		{
			nextX = -1;
			nextZ = -1;
			nextY = -1;
			goodPos  = true;

			for (Room r : mRooms)
			{
				if (r.x >= x)
				{
					if (nextX == -1)
						nextX = r.x+r.sizeX;
					else
						nextX = Math.min(nextX, r.x+r.sizeX);
				}
				if (r.z >= z)
				{
					if (nextZ == -1)
						nextZ = r.z+r.sizeZ;
					else
						nextZ = Math.min(nextZ, r.z+r.sizeZ);
				}
				if (r.y >= y)
				{
					if (nextY == -1)
						nextY = r.y+r.sizeY;
					else
						nextY = Math.min(nextY, r.y+r.sizeY);
				}
				if ( goodPos && ((r.x <= x && x-r.x-r.sizeX < 0) || (r.x > x && x+sizeX > r.x)) )
				{
					if ((r.z <= z && z-r.z-r.sizeZ < 0) || (r.z > z && z+sizeZ > r.z))
					{
						if ((r.y <= y && y-r.y-r.sizeY < 0) || (r.y > y && y+sizeY > r.y))
						{
							goodPos = false;
						}
					}
				}
			}
			if (goodPos)
			{
				Room room = new Room();
				room.x = x;
				room.z = z;
				room.y = y;
				room.sizeX = sizeX;
				room.sizeZ = sizeZ;
				room.sizeY = sizeY;
				mRooms.add(room);
				return room;
			}
			x = nextX;
			if (x+sizeX >= mSizeX || x == -1)
			{
				x = mX;
				z = nextZ;
			}
			if (z+sizeZ >= mSizeZ || z == -1)
			{
				x = mX;
				z = mZ;
				y = nextY;
			}
			if (y+sizeY >= mSizeY || y == -1)
			{
				break;
			}

		}while (!goodPos);
		return null;
	}
	public class Room
	{
		public int x,z,y,sizeX,sizeZ,sizeY;
	}
}
