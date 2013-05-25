package generateur.module;

import generateur.SurfaceManager;
import generateur.SurfaceManager.Room;
import generateur.map.MapFragment;
import generateur.map.MapOfHeightFragment;
import generateur.map.MapZone;
import generateur.module.sousmodule.SousModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Module
{
	private MapZone mMapZone;
	private ArrayList<SousModule> mSousModules;
	private Random mRandom;
	public static int PADDING_SIZE = 4;

	public Module(Random random)
	{
		mRandom = random;
	}
	public void attachToMapZone(MapZone mapZone)
	{
		mMapZone = mapZone;
	}
	public void generateGround(int groundHeight,int seed)
	{
		MapOfHeightFragment fragment = mMapZone.getMapOfHeightFragment(this);
		onGenerateGround(fragment, groundHeight, seed, mRandom);
		correctHeightZones(fragment, groundHeight);
	}
	public abstract void onGenerateGround(MapOfHeightFragment mapOfHeightFragment,int groundHeight,int seed,Random random);
	public void generateEntities(int groundHeight,int seed) throws IOException
	{
		MapFragment fragment = mMapZone.getMapFragment(this);
		onGenerateEntities(fragment, groundHeight, seed, mRandom);
		smoothContours(fragment, groundHeight+1, PADDING_SIZE);
		mMapZone.freeMapFragment(fragment);
	}
	public void onGenerateEntities(MapFragment mapFragment,int groundHeight,int seed,Random random) throws IOException
	{
		Populator chooser = new Populator();
		onChoosingSubModules(chooser);
		mSousModules = chooser.getResult(random);
		SurfaceManager surfaceManager = new SurfaceManager(PADDING_SIZE, PADDING_SIZE, mapFragment.getZoneSizeX()-(PADDING_SIZE*2), mapFragment.getZoneSizeZ()-(PADDING_SIZE*2));
		for (SousModule sm : mSousModules)
		{
			if (sm.getMinSizeX() < 1 || sm.getMinSizeZ() < 1)
				throw new IllegalArgumentException("extends of "+SousModule.class.getName()+" must return for getMinSizeX() and getMinSizeZ() a value > 0");
			if (sm.getMaxSizeX()-sm.getMinSizeX() < 0)
				throw new IllegalArgumentException("extends of "+SousModule.class.getName()+" must have their getMaxSizeX()-getMinSizeX() >= 0");
			if (sm.getMaxSizeZ()-sm.getMinSizeZ() < 0)
				throw new IllegalArgumentException("extends of "+SousModule.class.getName()+" must have their getMaxSizeZ()-getMinSizeZ() >= 0");
			int randSizeX = sm.getMinSizeX()+random.nextInt(1+sm.getMaxSizeX()-sm.getMinSizeX());
			int randSizeZ = sm.getMinSizeZ()+random.nextInt(1+sm.getMaxSizeZ()-sm.getMinSizeZ());

			ArrayList<Room> freeRooms = surfaceManager.getFreeRoomsByX();
			freeRooms.addAll(surfaceManager.getFreeRoomsByZ());
			int i=0;
			while(i<freeRooms.size())
			{
				Room r = freeRooms.get(i);
				if (r.sizeX < randSizeX || r.sizeZ < randSizeZ)
				{
					freeRooms.remove(r);
				}
				else
					++i;
			}
			if (freeRooms.size() > 0)
			{
				int randRoom = random.nextInt(freeRooms.size());
				Room room = freeRooms.get(randRoom);
				int randX = random.nextInt(1+room.sizeX-randSizeX);
				int randZ = random.nextInt(1+room.sizeZ-randSizeZ);

				surfaceManager.registerRoom(randX, randZ, randSizeX, randSizeZ);
				MapFragment subFragment = mapFragment.getSubMapFragment(randX, randZ, randSizeX, randSizeZ);
				sm.generateEntities(subFragment,groundHeight, random);
				subFragment.freeRegionCaches();
			}
		}
	}
	public abstract void onChoosingSubModules(Populator chooser);
	
	private void correctHeightZones(MapOfHeightFragment fragment,int groundHeight)
	{
		int sum = 0;
		for (int i=0;i<fragment.getZoneSizeX();++i)
		{
			sum += fragment.getHeight(i, 0);
			sum += fragment.getHeight(i, fragment.getZoneSizeZ()-1);
		}
		for (int j=1;j<fragment.getZoneSizeZ()-1;++j)
		{
			sum += fragment.getHeight(0, j);
			sum += fragment.getHeight(fragment.getZoneSizeX()-1, j);
		}
		int avg = sum / ( (2*fragment.getZoneSizeX())+ (2*fragment.getZoneSizeZ()) - 4); // -4 pour pas repasser sur les meme points (croisements des lignes)
		if (avg != groundHeight)
		{
			int correctValue = groundHeight-avg;
			for (int i=0;i<fragment.getZoneSizeX();++i)
			{
				for (int j=0;j<fragment.getZoneSizeZ();++j)
				{
					fragment.setHeight(i, j, fragment.getHeight(i, j)+correctValue);
				}
			}
		}
	}
	private void smoothContours(MapFragment map,int hauteurCible,int epaisseurContour) throws IOException
	{
		if(epaisseurContour<1)
			return;
		int xMap=map.getZoneSizeX();
		int zMap=map.getZoneSizeZ();
		int deltaOuest = 0,deltaEst = 0,deltaNord = 0,deltaSud = 0,h;
		for(int parcours=epaisseurContour;parcours<Math.max(xMap,zMap)-epaisseurContour;parcours++)
		{
			for(int parcoursContour=0;parcoursContour<epaisseurContour;parcoursContour++)
			{
				if(parcours<zMap-epaisseurContour)
				{
					if(parcoursContour==0)
					{
						/*OUEST*/
						h=map.getHeight(0, parcours,true);
						deltaOuest=hauteurCible-h;
						adapteHauteur(map,deltaOuest,h,0,parcours);
						/*EST*/
						h=map.getHeight(xMap-1, parcours,true);
						deltaEst=hauteurCible-h;
						adapteHauteur(map,deltaEst,h,xMap-1,parcours);
					}
					else
					{
						adapteHauteur(map,(int)((float)deltaOuest/(float)epaisseurContour*(epaisseurContour-parcoursContour)),map.getHeight(parcoursContour, parcours,true),parcoursContour,parcours);
						adapteHauteur(map,(int)((float)deltaEst/(float)epaisseurContour*(epaisseurContour-parcoursContour)),map.getHeight(xMap-parcoursContour-1, parcours,true),xMap-parcoursContour-1,parcours);
					}
				}
				if(parcours<xMap-epaisseurContour)
				{
					if(parcoursContour==0)
					{
						/*NORD*/
						h=map.getHeight(parcours, zMap-1,true);
						deltaNord=hauteurCible-h;
						adapteHauteur(map,deltaNord,h,parcours,zMap-1);
						/*SUD*/
						h=map.getHeight(parcours, 0,true);
						deltaSud=hauteurCible-h;
						adapteHauteur(map,deltaSud,h,parcours,0);
					}
					else
					{
						adapteHauteur(map,(int)((float)deltaNord/(float)epaisseurContour*(epaisseurContour-parcoursContour)),map.getHeight(parcours, zMap-parcoursContour-1,true),parcours,zMap-parcoursContour-1);
						adapteHauteur(map,(int)((float)deltaSud/(float)epaisseurContour*(epaisseurContour-parcoursContour)),map.getHeight(parcours, parcoursContour,true),parcours,parcoursContour);	
					}
				}
			}
		}
	}
	private void adapteHauteur(MapFragment map,int delta,int hBase,int x,int z) throws IOException
	{
		if(delta==0) // si aucun decallage 
			return;
		if(delta>0)
		{
			short id=0;
			for(int h=hBase;h>=0;h--)
			{
				id=(short) map.getBlock(x, z, h);
				map.setBlock(x, z, h+delta,id);
			}
			for(int i=0;delta<0;i++)
			{
				map.setBlock(x,z,i,id);
			}
		}
		else
		{
			for(int h=hBase;h>hBase+delta;h--)
			{
				if(h+delta>=0)
					map.setBlock(x,z,h+delta,(short) map.getBlock(x, z, h));
				map.setBlock(x, z, h, (short)0);
			}
		}
	}
}