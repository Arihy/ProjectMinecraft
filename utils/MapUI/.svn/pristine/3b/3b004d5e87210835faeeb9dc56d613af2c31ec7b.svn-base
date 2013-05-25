package projet;

import java.io.IOException;

import map.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PreviewMap extends Actor{

	private Map map;

	private Texture mapBuffer;
	
	private int nbXRegion;
	private int nbZRegion;

	private int hauteurMax=0;
	private int hauteurMin=Integer.MAX_VALUE;

	public PreviewMap(Map map_,int x_,int y_,int width,int height) throws IOException{
		
		map=map_;

		nbXRegion=map.getSize()[0]/map.getZoneWidth();
		nbZRegion=map.getSize()[1]/map.getZoneHeight();

		setX(x_);
		setY(y_);
		setWidth(width);
		setHeight(height);

		setOriginX(width/2);
		setOriginY(height/2);
		setScaleX(1);
		setScaleY(1);

		for(int xRegion=0;xRegion<nbXRegion;xRegion++)
			for(int zRegion=0;zRegion<nbZRegion;zRegion++)
				for(int x=0;x<map.getZoneWidth();x++)
					for(int z=0;z<map.getZoneHeight();z++)
					{
						hauteurMax=Math.max(hauteurMax,map.getHeight(xRegion*map.getZoneWidth()+x, zRegion*map.getZoneHeight()+z,false)-1);
						hauteurMin=Math.min(hauteurMin,map.getHeight(xRegion*map.getZoneWidth()+x, zRegion*map.getZoneHeight()+z, false)-1-profondeurEau(xRegion*map.getZoneWidth()+x,zRegion*map.getZoneHeight()+z,map.getHeight(xRegion*map.getZoneWidth()+x, zRegion*map.getZoneHeight()+z,false)-1));
					}
		
		creeMapBuffer();
	}

	@Override
	public void draw(SpriteBatch s,float parentAlpha){
		s.draw(mapBuffer,getX(),getY(),getWidth(),getHeight());
	}

	private void creeMapBuffer()
	{
		Pixmap p=new Pixmap(map.getSize()[0],map.getSize()[1],Pixmap.Format.RGB888);
		
		for(int xRegion=0;xRegion<nbXRegion;xRegion++)
			for(int zRegion=0;zRegion<nbZRegion;zRegion++)
				for(int x=0;x<map.getZoneWidth();x++)
					for(int z=0;z<map.getZoneHeight();z++)
						try {
							afficheCase(p,xRegion*map.getZoneWidth()+x,zRegion*map.getZoneHeight()+z);
						} catch (IOException e) {
							e.printStackTrace();
						}
		
		mapBuffer=new Texture(p);
		p.dispose();
	}
	
	private void afficheCase(Pixmap p,int x,int z) throws IOException
	{
		int y=map.getHeight(x, z, false)-1;
		short id=map.getBlock(x,z,y);
		Color c;
		if(id!=8)
		{
			c=materialIdToColor(id);
			c.mul(0.25f+0.75f*(((float)(y-hauteurMin)/(float)(hauteurMax-hauteurMin))));
			c.a=1;
		}
		else
		{
			/****Case eau****/
			c=materialIdToColor(id);
			c.mul(0.3f+0.7f*(((float)(y-profondeurEau(x,z,y)-hauteurMin)/(float)(hauteurMax-hauteurMin))));
			c.a=1;
		}
		p.setColor(c);
		p.drawPixel(x,z);
	}

	private int profondeurEau(int x,int z,int y)
	{
		int cpt=1;
		short id=0;
		while(y-cpt>=0)
		{
			try {
				id=map.getBlock(x, z, y-cpt);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(id!=8)
				break;
			cpt++;
		}
		return cpt;
	}

	/*
	 * Assigne une couleur a afficher pour un Material ID donn�.
	 */
	private Color materialIdToColor(short id)
	{
		if(id==2)
			return new Color(Color.GREEN);
		else if(id==8)
			return new Color(0f/255f,120f/255f,255f/255f,1);
		else if(id==12)
			return new Color(Color.YELLOW);
		else if(id==3)
			return new Color(171f/255f,70f/255f,0f/255f,1);
		else if(id==5||id==17)
			return new Color(188f/255f,125f/255f,15f/255f,1);
		else if(id==18)
			return new Color(0,0.9f,0.8f,1);
		else if(id==98)
			return new Color(Color.GRAY);
		return new Color(Color.BLACK);
	}

}
