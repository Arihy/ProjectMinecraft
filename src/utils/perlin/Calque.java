package utils.perlin;

public class Calque
{
	private int[][] mMatrice;
	private float mPersistance;
	
	public Calque(int[][] matrice,float persistance)
	{
		mMatrice = matrice;
		mPersistance = persistance;
	}
	public Calque(int tailleX,int tailleZ,float persistance)
	{
		mMatrice = new int[tailleX][tailleZ];
		mPersistance = persistance;
	}
	public void setMatrice(int[][] matrice)
	{
		if (matrice == null)
			throw new NullPointerException("Pas de matrice");
		mMatrice = matrice;
	}
	public int[][] getMatrice()
	{
		return mMatrice;
	}
	public void setValeur(int valeur,int x,int z)
	{
		mMatrice[x][z] = valeur;
	}
	public int getValeur(int x,int z)
	{
		return mMatrice[x][z];
	}
	public float getPersistance()
	{
		return mPersistance;
	}
	public int sizeX()
	{
		return mMatrice.length;
	}
	public int sizeZ()
	{
		return mMatrice[0].length;
	}
}
