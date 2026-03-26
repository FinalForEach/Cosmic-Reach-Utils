package finalforeach.cosmicreach.util.constants;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public enum Direction
{
	NEG_X(-1, 0, 0), POS_X(1, 0, 0), NEG_Y(0, -1, 0), POS_Y(0, 1, 0), NEG_Z(0, 0, -1), POS_Z(0, 0, 1);

	public static final Direction[] ALL_DIRECTIONS = Direction.values();
	public static final Direction[] ALL_POS_AXIS = new Direction[] { POS_X, POS_Y, POS_Z };
	public static final Direction[] VERT_AXIS = new Direction[] { NEG_Y, POS_Y };

	private int xOff, yOff, zOff;
	private Direction[] allExceptThis;
	private Direction[] allExceptOpposite;
	private Direction[] justThis = new Direction[] { this };
	private Direction oppositeDirection;

	public Direction[] asArray()
	{
		return justThis;
	}

	public Direction[] allOtherDirections()
	{
		if (allExceptThis == null)
		{
			var a = new Array<Direction>(Direction.class);
			a.addAll(ALL_DIRECTIONS);
			a.removeValue(this, true);
			allExceptThis = a.toArray();
		}
		return allExceptThis;
	}

	public Direction[] allDirectionsButOpposite()
	{
		if (allExceptOpposite == null)
		{
			var a = new Array<Direction>(Direction.class);
			a.addAll(ALL_DIRECTIONS);
			a.removeValue(getOpposite(), true);
			allExceptOpposite = a.toArray();
		}
		return allExceptOpposite;
	}

	public Direction getOpposite()
	{
		if (oppositeDirection == null)
		{
			oppositeDirection = switch (this)
					{
					case NEG_X -> POS_X;
					case NEG_Y -> POS_Y;
					case NEG_Z -> POS_Z;
					case POS_X -> NEG_X;
					case POS_Y -> NEG_Y;
					case POS_Z -> NEG_Z;
					default -> throw new IllegalArgumentException("Unexpected value: " + this);
					};
		}
		return oppositeDirection;
	}

	Direction(int xOff, int yOff, int zOff)
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

	public boolean isXAxis()
	{
		return xOff != 0;
	}

	public boolean isYAxis()
	{
		return yOff != 0;
	}

	public boolean isZAxis()
	{
		return zOff != 0;
	}

	public static Direction randomDirection(Direction[] directions)
	{
		return directions[MathUtils.random(0, directions.length - 1)];
	}

	public static Direction randomDirection()
	{
		return randomDirection(ALL_DIRECTIONS);
	}

	public static Direction fromStr(String param)
	{
		return switch (param)
				{
				case "NegX": {
					yield NEG_X;
				}
				case "PosX": {
					yield POS_X;
				}
				case "NegY": {
					yield NEG_Y;
				}
				case "PosY": {
					yield POS_Y;
				}
				case "NegZ": {
					yield NEG_Z;
				}
				case "PosZ": {
					yield POS_Z;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + param);
				};
	}

	public Vector3 setValues(Vector3 vec3)
	{
		vec3.set(xOff, yOff, zOff);
		return vec3;
	}

}