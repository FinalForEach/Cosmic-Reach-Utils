package finalforeach.cosmicreach.util.constants;

public class AdjacentBitmask 
{
	public static final int NEG_X = 0x1;
	public static final int POS_X = 0x2;
	public static final int NEG_Y = 0x4;
	public static final int POS_Y = 0x8;
	public static final int NEG_Z = 0x10;
	public static final int POS_Z = 0x20;

	public static final int ALL_ADJACENT_BITMASKS = NEG_X | POS_X  | NEG_Y | POS_Y | NEG_Z | POS_Z;

}
