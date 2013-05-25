package utils.lcg;


public class RandomLCG
{
	public static int generationParLcg(int x,int y,int seed,int min,int max)
	{
		if (max-min == 0)
			return max;
		return min+(Math.abs(lcg((x+y)%7,(int)(x),(int)(y),seed)))%(max-min);
	}
	private static int lcg(int index, int a,int b,int seed)
	{
		if (index == 0)
			return seed;
		else
			return ((a*lcg(index-1,a, b, seed))+b);
	}
}
