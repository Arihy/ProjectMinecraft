package generateur;

import java.util.ArrayList;

public class SurfaceManager
{
	private int mX,mZ;
	private int mSizeX,mSizeZ;
	private ArrayList<Room> mRooms;

	public SurfaceManager(int pX,int pZ,int sizeX,int sizeZ)
	{
		mX = pX;
		mZ = pZ;
		mSizeX = sizeX;
		mSizeZ = sizeZ;
		mRooms = new ArrayList<SurfaceManager.Room>();
	}
	public int getAreaX()
	{
		return mX;
	}
	public int getAreaZ()
	{
		return mZ;
	}
	public int getAreaSizeX()
	{
		return mSizeX;
	}
	public int getAreaSizeZ()
	{
		return mSizeZ;
	}
	public ArrayList<Room> getRooms()
	{
		return mRooms;
	}
	public ArrayList<Room> getFreeRoomsByX()
	{
		ArrayList<Room> rooms = new ArrayList<SurfaceManager.Room>();
		Room roomSubject = new Room(mX,mZ,mSizeX,mSizeZ);
		int nextX,nextZ;
		boolean goodPos;
		while(true)
		{
			nextX = -1;
			nextZ = -1;
			goodPos  = true;

			for (Room r : mRooms)
			{
				if (r.x >= roomSubject.x)
				{
					if (nextX == -1)
						nextX = r.x+r.sizeX;
					else
						nextX = Math.min(nextX, r.x+r.sizeX);
				}
				if (r.z >= roomSubject.z)
				{
					if (nextZ == -1)
						nextZ = r.z+r.sizeZ;
					else
						nextZ = Math.min(nextZ, r.z+r.sizeZ);
				}
				if (reduce(roomSubject, r))
				{
					if (roomSubject.sizeX == 0 || roomSubject.sizeZ == 0)
					{
						goodPos = false;
						break;
					}
					nextX = Math.min(nextX, roomSubject.x+roomSubject.sizeX);
					nextZ = Math.min(nextZ, roomSubject.z+roomSubject.sizeZ);
				}
			}
			for (Room r : rooms)
			{
				if (r.x >= roomSubject.x)
				{
					if (nextX == -1)
						nextX = r.x+r.sizeX;
					else
						nextX = Math.min(nextX, r.x+r.sizeX);
				}
				if (r.z >= roomSubject.z)
				{
					if (nextZ == -1)
						nextZ = r.z+r.sizeZ;
					else
						nextZ = Math.min(nextZ, r.z+r.sizeZ);
				}
				if (reduce(roomSubject, r))
				{
					if (roomSubject.sizeX == 0 || roomSubject.sizeZ == 0)
					{
						goodPos = false;
						break;
					}
					nextX = Math.min(nextX, roomSubject.x+roomSubject.sizeX);
					nextZ = Math.min(nextZ, roomSubject.z+roomSubject.sizeZ);
				}
			}
			
			if (goodPos)
			{
				rooms.add(roomSubject);
			}
			roomSubject.x = nextX;
			if (roomSubject.x+roomSubject.sizeX >= mSizeX || roomSubject.x == -1)
			{
				roomSubject.x = mX;
				roomSubject.z = nextZ;
			}
			if (roomSubject.z+roomSubject.sizeZ >= mSizeZ || roomSubject.z == -1)
			{
				break;
			}

		}
		return rooms;
	}
	public ArrayList<Room> getFreeRoomsByZ()
	{
		ArrayList<Room> rooms = new ArrayList<SurfaceManager.Room>();
		Room roomSubject = new Room(mX,mZ,mSizeX,mSizeZ);
		int nextX,nextZ;
		boolean goodPos;
		while(true)
		{
			nextX = -1;
			nextZ = -1;
			goodPos  = true;

			for (Room r : mRooms)
			{
				if (r.x >= roomSubject.x)
				{
					if (nextX == -1)
						nextX = r.x+r.sizeX;
					else
						nextX = Math.min(nextX, r.x+r.sizeX);
				}
				if (r.z >= roomSubject.z)
				{
					if (nextZ == -1)
						nextZ = r.z+r.sizeZ;
					else
						nextZ = Math.min(nextZ, r.z+r.sizeZ);
				}
				if (reduce(roomSubject, r))
				{
					if (roomSubject.sizeX == 0 || roomSubject.sizeZ == 0)
					{
						goodPos = false;
						break;
					}
					nextX = Math.min(nextX, roomSubject.x+roomSubject.sizeX);
					nextZ = Math.min(nextZ, roomSubject.z+roomSubject.sizeZ);
				}
			}
			for (Room r : rooms)
			{
				if (r.x >= roomSubject.x)
				{
					if (nextX == -1)
						nextX = r.x+r.sizeX;
					else
						nextX = Math.min(nextX, r.x+r.sizeX);
				}
				if (r.z >= roomSubject.z)
				{
					if (nextZ == -1)
						nextZ = r.z+r.sizeZ;
					else
						nextZ = Math.min(nextZ, r.z+r.sizeZ);
				}
				if (reduce(roomSubject, r))
				{
					if (roomSubject.sizeX == 0 || roomSubject.sizeZ == 0)
					{
						goodPos = false;
						break;
					}
					nextX = Math.min(nextX, roomSubject.x+roomSubject.sizeX);
					nextZ = Math.min(nextZ, roomSubject.z+roomSubject.sizeZ);
				}
			}
			
			if (goodPos)
			{
				rooms.add(roomSubject);
			}
			roomSubject.z = nextZ;
			if (roomSubject.z+roomSubject.sizeZ >= mSizeZ || roomSubject.z == -1)
			{
				roomSubject.z = mZ;
				roomSubject.x = nextX;
			}
			if (roomSubject.x+roomSubject.sizeX >= mSizeX || roomSubject.x == -1)
			{
				roomSubject.x = mX;
				break;
			}

		}
		return rooms;
	}
	public void registerRoom(Room room)
	{
		mRooms.add(room);
	}
	public Room registerRoom(int x,int z,int sizeX,int sizeZ)
	{
		Room room = new Room(x,z,sizeX,sizeZ);
		registerRoom(room);
		return room;
	}
	public Room alocateNextRoom(int sizeX,int sizeZ)
	{
		int x=mX,z=mZ;
		int nextX,nextZ;
		boolean goodPos;
		do
		{
			nextX = -1;
			nextZ = -1;
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
				if ( goodPos && ((r.x <= x && x-r.x-r.sizeX < 0) || (r.x > x && x+sizeX > r.x)) )
				{
					if ((r.z <= z && z-r.z-r.sizeZ < 0) || (r.z > z && z+sizeZ > r.z))
					{
						goodPos = false;
					}
				}





			}
			if (goodPos)
			{
				Room room = new Room();
				room.x = x;
				room.z = z;
				room.sizeX = sizeX;
				room.sizeZ = sizeZ;
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
				break;
			}

		}while (!goodPos);
		return null;
	}
	private int inCollision(Room roomSubject,Room room)
	{
		if ((room.x <= roomSubject.x && roomSubject.x-room.x-room.sizeX < 0))
		{
			if ((room.z <= roomSubject.z && roomSubject.z-room.z-room.sizeZ < 0))
			{
				return 1;
			}
			else if ((room.z > roomSubject.z && roomSubject.z+roomSubject.sizeZ > room.z))
			{
				return 2;
			}
		}
		else if ((room.x > roomSubject.x && roomSubject.x+roomSubject.sizeX > room.x))
		{
			if ((room.z <= roomSubject.z && roomSubject.z-room.z-room.sizeZ < 0))
			{
				return 3;
			}
			else if ((room.z > roomSubject.z && roomSubject.z+roomSubject.sizeZ > room.z))
			{
				return 4;
			}
		}
		return 0; 
	}
	private boolean reduce(Room roomSubject, Room room)
	{
		int result = inCollision(roomSubject, room);

		if (result == 2)
		{
			roomSubject.sizeZ = room.z-roomSubject.z;
			return true;
		}
		else if (result == 3)
		{
			roomSubject.sizeX = room.z+roomSubject.x;
			return true;
		}
		else if (result == 4)
		{
			int calcSizeX = room.x - roomSubject.x;
			int calcSizeZ = room.z - roomSubject.z;
			if (roomSubject.sizeX - calcSizeX < roomSubject.sizeZ - calcSizeZ)
			{
				roomSubject.sizeX = calcSizeX;
			}
			else
			{
				roomSubject.sizeZ = calcSizeZ;

			}
			return true;
		}
		return false;
	}
	public class Room
	{
		public int x,z,sizeX,sizeZ;

		public Room()
		{

		}
		public Room(int x,int z,int sizeX,int sizeZ)
		{
			this.x = x;
			this.z = z;
			this.sizeX = sizeX;
			this.sizeZ = sizeZ;
		}
	}
}
