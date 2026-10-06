package finalforeach.cosmicreach.util.constants;

import com.badlogic.gdx.math.MathUtils;

public class SubFaceBitmask
{
	public static final int FULL_FACE = 0xFFFF;
	public static final int EMPTY_FACE = 0x0000;

	
	// 4x4 bitmask for faces to be culled
	public static int computeFaceMask(float minU, float maxU, float minV, float maxV)
	{
		float e = 0.0001f;
		int u1 = MathUtils.clamp((int) Math.floor(minU * 4.0f + e), 0, 4);
		int u2 = MathUtils.clamp((int) Math.ceil(maxU * 4.0f - e), 0, 4);
		int v1 = MathUtils.clamp((int) Math.floor(minV * 4.0f + e), 0, 4);
		int v2 = MathUtils.clamp((int) Math.ceil(maxV * 4.0f - e), 0, 4);

		if (u2 <= u1 || v2 <= v1)
			return EMPTY_FACE;

		int mask = 0;
		for (int v = v1; v < v2; v++)
		{
			int rowBits = ((1 << (u2 - u1)) - 1) << u1;
			mask |= (rowBits << (v * 4));
		}
		return mask;
	}

	// 4x4 bitmask for faces to cull others
	public static int computeOccluderMask(float minU, float maxU, float minV, float maxV)
	{
		float e = 0.0001f;
		int u1 = MathUtils.clamp((int) Math.ceil(minU * 4.0f - e), 0, 4);
		int u2 = MathUtils.clamp((int) Math.floor(maxU * 4.0f + e), 0, 4);
		int v1 = MathUtils.clamp((int) Math.ceil(minV * 4.0f - e), 0, 4);
		int v2 = MathUtils.clamp((int) Math.floor(maxV * 4.0f + e), 0, 4);

		if (u2 <= u1 || v2 <= v1)
			return EMPTY_FACE;

		int mask = 0;
		for (int v = v1; v < v2; v++)
		{
			int rowBits = ((1 << (u2 - u1)) - 1) << u1;
			mask |= (rowBits << (v * 4));
		}
		return mask;
	}
}
