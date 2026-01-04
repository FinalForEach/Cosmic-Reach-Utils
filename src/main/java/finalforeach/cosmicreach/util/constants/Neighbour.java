package finalforeach.cosmicreach.util.constants;

public enum Neighbour 
{
	NX_NY_NZ(-1, -1, -1),
	NX_NY_ZZ(-1, -1, 0),
	NX_NY_PZ(-1, -1, 1),
	NX_ZY_NZ(-1, 0, -1),
	NX_ZY_ZZ(-1, 0, 0),
	NX_ZY_PZ(-1, 0, 1),
	NX_PY_NZ(-1, 1, -1),
	NX_PY_ZZ(-1, 1, 0),
	NX_PY_PZ(-1, 1, 1),

	ZX_NY_NZ(0, -1, -1),
	ZX_NY_ZZ(0, -1, 0),
	ZX_NY_PZ(0, -1, 1),
	ZX_ZY_NZ(0, 0, -1),
	ZX_ZY_PZ(0, 0, 1),
	ZX_PY_NZ(0, 1, -1),
	ZX_PY_ZZ(0, 1, 0),
	ZX_PY_PZ(0, 1, 1),

	PX_NY_NZ(1, -1, -1),
	PX_NY_ZZ(1, -1, 0),
	PX_NY_PZ(1, -1, 1),
	PX_ZY_NZ(1, 0, -1),
	PX_ZY_ZZ(1, 0, 0),
	PX_ZY_PZ(1, 0, 1),
	PX_PY_NZ(1, 1, -1),
	PX_PY_ZZ(1, 1, 0),
	PX_PY_PZ(1, 1, 1);
	private int xOff, yOff, zOff;

	public static final Neighbour[] ALL_NEIGHBOURS = Neighbour.values();
	Neighbour(int xOff, int yOff, int zOff) 
	{
		this.xOff = xOff;
		this.yOff = yOff;
		this.zOff = zOff;
	}
	
	public int getXOffset() 
	{
		return xOff;
	}
	
	public int getYOffset() 
	{
		return yOff;
	}
	
	public int getZOffset() 
	{
		return zOff;
	}
}
