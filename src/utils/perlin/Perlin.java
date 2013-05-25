package utils.perlin;

public abstract class Perlin
{
	public static enum INTERPOLATION_TYPE {linear,noLinear};
	public static void perlin(int[][] matrice, int octave,float depersistance,INTERPOLATION_TYPE typeOfInterpolation)
	{
		perlin(matrice, 0, 0, matrice.length, matrice[0].length, octave, depersistance,typeOfInterpolation);
	}
	public static void perlin(int[][] matrice, int x,int z,int zoneX,int zoneZ,int octave,float depersistance,INTERPOLATION_TYPE typeOfInterpolation)
	{
		//System.out.println("perlin sur x:"+x+" z:"+z+" zoneX:"+zoneX+" zoneZ:"+zoneZ);
		Calque calquePrincipal = new Calque(matrice, 1);
		Calque[] sousCalques = new Calque[octave];

		for (int o=0;o<octave;o++)
		{
			sousCalques[o] = new Calque(zoneX,zoneZ, (float) Math.pow(depersistance,o+1));
			genererCalque(calquePrincipal,x,z,sousCalques[o],(int) Math.pow(2, o+1),typeOfInterpolation);
		}
		affiner(calquePrincipal, sousCalques,x,z, octave);
		lissage(calquePrincipal,x,z,zoneX,zoneZ);
	}
	private static void genererCalque(Calque calquePrincipal ,int px,int pz,Calque sousCalque,int frequence,INTERPOLATION_TYPE typeOfInterpolation)
	{
		for (int x=0;x<sousCalque.sizeX();x++)
		{
			for (int z=0;z<sousCalque.sizeZ();z++)
			{
				sousCalque.setValeur(interpoler(calquePrincipal,px,pz,x,z,sousCalque.sizeX(),sousCalque.sizeZ(),frequence,typeOfInterpolation), x, z);
			}
		}
	}
	private static int interpoler(Calque calquePrincipal,int princpalX,int principalZ,int x,int y,int sizeX,int sizeZ,int frequence,INTERPOLATION_TYPE typeOfInterpolation)
	{
		int borne1x, borne1y, borne2x, borne2y, q;
		float pasX = (float)sizeX/frequence;

		q = (int) (x/pasX);
		borne1x = (int) (q*pasX);
		borne2x = (int) ((q + 1)*pasX);

		if(borne2x >= sizeX)
			borne2x = sizeX-1;

		float pasY = (float)sizeZ/frequence;

		q = (int) (y/pasY);
		borne1y = (int) (q*pasY);
		borne2y = (int) ((q + 1)*pasY);

		if(borne2y >= sizeZ)
			borne2y = sizeZ-1;

		borne1x += princpalX;
		borne2x += princpalX;
		borne1y += principalZ;
		borne2y += principalZ;

		//System.out.println(borne1x+" "+borne2x+" "+borne1y+" "+borne2y);

		int b00,b01,b10,b11;
		b00 = calquePrincipal.getValeur(borne1x, borne1y);
		b01 = calquePrincipal.getValeur(borne1x,borne2y);
		b10 = calquePrincipal.getValeur(borne2x,borne1y);
		b11 = calquePrincipal.getValeur(borne2x,borne2y);

		int v1;
		int v2;
		int fin = 0;

		if (typeOfInterpolation == INTERPOLATION_TYPE.linear)
		{
			v1 = interpolateLinear(b00, b01, borne2y-borne1y, (principalZ+y)-borne1y);
			v2 = interpolateLinear(b10, b11, borne2y-borne1y, (principalZ+y)-borne1y);
			fin = interpolateLinear(v1, v2, borne2x-borne1x , (princpalX+x)-borne1x);
		}
		else if (typeOfInterpolation == INTERPOLATION_TYPE.noLinear)
		{
			v1 = interpolateNoLinear(b00, b01, borne2y-borne1y, (principalZ+y)-borne1y);
			v2 = interpolateNoLinear(b10, b11, borne2y-borne1y, (principalZ+y)-borne1y);
			fin = interpolateNoLinear(v1, v2, borne2x-borne1x , (princpalX+x)-borne1x);
		}
		return fin;
	}
	private static int interpolateLinear(int y1, int y2, int n, int delta)
	{
		if (n!=0)
			return y1 + delta*(y2-y1)/n;
		else
			return y1;
	}
	private static int interpolateNoLinear(int y1, int y2, int n, int delta)
	{
		if (n==0)
			return y1;
		if (n==1)
			return y2;

		float a = (float)delta/n;

		float v1 = (float) (3*Math.pow(1-a, 2) - 2*Math.pow(1-a,3));
		float v2 = (float) (3*Math.pow(a, 2)   - 2*Math.pow(a, 3));

		return (int) (y1*v1 + y2*v2);
	}
	private static void affiner(Calque calqueRecepteur,Calque[] calques,int x,int z,int octave)
	{
		// ajout des calques successifs
		for (int i=0; i<calques[0].sizeX(); i++ )
			for (int j=0; j<calques[0].sizeZ(); j++)
			{
				for (int n=0; n < octave; n++)
				{
					if (n==0)
						calqueRecepteur.setValeur((int) (calques[n].getValeur(i, j)*calques[n].getPersistance()), x+i, z+j);
					else
						calqueRecepteur.setValeur((int) (calqueRecepteur.getValeur(x+i, z+j) + (calques[n].getValeur(i, j)*calques[n].getPersistance())), x+i, z+j);
				}
			}
	}
	private static void lissage(Calque calque,int px,int pz,int zoneX,int zoneZ)
	{
		int[][] lissage = new int[zoneX][zoneZ];
		for (int x=0; x < zoneX; x++)
			for (int y=0; y < zoneZ; y++)
			{
				int a=0;
				int n=0;
				for (int k=x-5; k<=x+5;k++)
					for (int l=y-5; l<=y+5;l++)
						if ((k >= 0) && (k<zoneX) &&(l>=0) && (l<zoneZ))
						{
							n++;
							a += calque.getValeur(px+k, pz+l);
						}
				lissage[x][y] = a/n;
			}
		for (int i=0;i<lissage.length;i++)
		{
			for (int j=0;j<lissage[0].length;j++)
			{
				calque.setValeur(lissage[i][j], px+i, pz+j);
			}
		}
	}

}