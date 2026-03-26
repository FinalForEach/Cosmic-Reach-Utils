package finalforeach.cosmicreach.util.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Segment;

public final class GameMath
{
	public static float distanceSegmentBoundingBox(Segment segment, BoundingBox boundingBox)
	{
		// Clamped origin and destination

		float dist = distanceBoundingBoxPoint(boundingBox, segment.a);
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceBoundingBoxPoint(boundingBox, segment.b));
		if (dist == 0)
		{
			return 0;
		}

		// Projected center

		final Vector3 a = segment.a;
		final Vector3 b = segment.b;

		float dirX = b.x - a.x;
		float dirY = b.y - a.y;
		float dirZ = b.z - a.z;

		float originToCenterX = boundingBox.getCenterX() - a.x;
		float originToCenterY = boundingBox.getCenterY() - a.y;
		float originToCenterZ = boundingBox.getCenterZ() - a.z;

		float ab_x = b.x - a.x;
		float ab_y = b.y - a.y;
		float ab_z = b.z - a.z;

		float ab_dot_ab = (float) (Math.pow(ab_x, 2) + Math.pow(ab_y, 2) + Math.pow(ab_z, 2));
		float projectionScalar = Vector3.dot(originToCenterX, originToCenterY, originToCenterZ, ab_x, ab_y, ab_z)
				/ ab_dot_ab;
		projectionScalar = MathUtils.clamp(projectionScalar, 0, 1); // Must be >= 0 & <= 1, as it is a segment

		float projectedCenterX = a.x + projectionScalar * dirX;
		float projectedCenterY = a.y + projectionScalar * dirY;
		float projectedCenterZ = a.z + projectionScalar * dirZ;

		dist = Math.min(dist,
				distanceBoundingBoxPoint(boundingBox, projectedCenterX, projectedCenterY, projectedCenterZ));
		if (dist == 0)
		{
			return 0;
		}

		// Vertices

		// Vertices
		final var min = boundingBox.min;
		final var max = boundingBox.max;

		dist = Math.min(dist, distanceSegmentPoint(segment, min.x, min.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceSegmentPoint(segment, min.x, min.y, max.z));
		if (dist == 0)
		{
			return 0;
		}

		dist = Math.min(dist, distanceSegmentPoint(segment, min.x, max.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceSegmentPoint(segment, min.x, max.y, max.z));
		if (dist == 0)
		{
			return 0;
		}

		dist = Math.min(dist, distanceSegmentPoint(segment, max.x, min.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceSegmentPoint(segment, max.x, min.y, max.z));
		if (dist == 0)
		{
			return 0;
		}

		dist = Math.min(dist, distanceSegmentPoint(segment, max.x, max.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceSegmentPoint(segment, max.x, max.y, max.z));
		return dist;
	}

	public static float distanceRayBoundingBox(Ray ray, BoundingBox boundingBox)
	{
		final var rayOrigin = ray.origin;
		final var rayDir = ray.direction;
		// Clamped origin
		float dist = distanceBoundingBoxPoint(boundingBox, rayOrigin);
		if (dist == 0)
		{
			return 0;
		}

		// Projected center
		float originToCenterX = boundingBox.getCenterX() - rayOrigin.x;
		float originToCenterY = boundingBox.getCenterY() - rayOrigin.y;
		float originToCenterZ = boundingBox.getCenterZ() - rayOrigin.z;

		float projectionScalar = rayDir.dot(originToCenterX, originToCenterY, originToCenterZ);
		projectionScalar = Math.max(0, projectionScalar); // Must be >= 0 since it is a ray not a line

		float projectedCenterX = rayOrigin.x + projectionScalar * rayDir.x;
		float projectedCenterY = rayOrigin.y + projectionScalar * rayDir.y;
		float projectedCenterZ = rayOrigin.z + projectionScalar * rayDir.z;

		dist = Math.min(dist,
				distanceBoundingBoxPoint(boundingBox, projectedCenterX, projectedCenterY, projectedCenterZ));
		if (dist == 0)
		{
			return 0;
		}

		// Vertices
		final var min = boundingBox.min;
		final var max = boundingBox.max;

		dist = Math.min(dist, distanceRayPoint(ray, min.x, min.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceRayPoint(ray, min.x, min.y, max.z));
		if (dist == 0)
		{
			return 0;
		}

		dist = Math.min(dist, distanceRayPoint(ray, min.x, max.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceRayPoint(ray, min.x, max.y, max.z));
		if (dist == 0)
		{
			return 0;
		}

		dist = Math.min(dist, distanceRayPoint(ray, max.x, min.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceRayPoint(ray, max.x, min.y, max.z));
		if (dist == 0)
		{
			return 0;
		}

		dist = Math.min(dist, distanceRayPoint(ray, max.x, max.y, min.z));
		if (dist == 0)
		{
			return 0;
		}
		dist = Math.min(dist, distanceRayPoint(ray, max.x, max.y, max.z));
		return dist;
	}

	public static float distanceBoundingBoxPoint(BoundingBox boundingBox, Vector3 point)
	{
		final var min = boundingBox.min;
		final var max = boundingBox.max;
		float clampedX = MathUtils.clamp(point.x, min.x, max.x);
		float clampedY = MathUtils.clamp(point.y, min.y, max.y);
		float clampedZ = MathUtils.clamp(point.z, min.z, max.z);

		return point.dst(clampedX, clampedY, clampedZ);
	}

	public static float distanceBoundingBoxPoint(BoundingBox boundingBox, float pointX, float pointY, float pointZ)
	{
		final var min = boundingBox.min;
		final var max = boundingBox.max;
		float clampedX = MathUtils.clamp(pointX, min.x, max.x);
		float clampedY = MathUtils.clamp(pointY, min.y, max.y);
		float clampedZ = MathUtils.clamp(pointZ, min.z, max.z);

		return Vector3.dst(pointX, pointY, pointZ, clampedX, clampedY, clampedZ);
	}

	public static float distanceRayPoint(Ray ray, Vector3 point)
	{
		return distanceRayPoint(ray, point.x, point.y, point.z);
	}

	public static float distanceRayPoint(Ray ray, float pointX, float pointY, float pointZ)
	{
		final var rayOrigin = ray.origin;
		final var rayDir = ray.direction;
		float originToPointX = pointX - rayOrigin.x;
		float originToPointY = pointY - rayOrigin.y;
		float originToPointZ = pointZ - rayOrigin.z;

		float projectionScalar = rayDir.dot(originToPointX, originToPointY, originToPointZ);
		projectionScalar = Math.max(0, projectionScalar); // Must be >= 0 since it is a ray not a line

		float projectedX = rayOrigin.x + projectionScalar * rayDir.x;
		float projectedY = rayOrigin.y + projectionScalar * rayDir.y;
		float projectedZ = rayOrigin.z + projectionScalar * rayDir.z;

		return Vector3.dst(pointX, pointY, pointZ, projectedX, projectedY, projectedZ);
	}

	public static float distanceSegmentPoint(Segment segment, Vector3 point)
	{
		return distanceSegmentPoint(segment, point.x, point.y, point.z);
	}

	public static float distanceSegmentPoint(Segment segment, float pointX, float pointY, float pointZ)
	{
		final var a = segment.a;
		final var b = segment.b;
		float originToPointX = pointX - a.x;
		float originToPointY = pointY - a.y;
		float originToPointZ = pointZ - a.z;

		float dirX = b.x - a.x;
		float dirY = b.y - a.y;
		float dirZ = b.z - a.z;

		float ab_x = b.x - a.x;
		float ab_y = b.y - a.y;
		float ab_z = b.z - a.z;

		float ab_dot_ab = (float) (Math.pow(ab_x, 2) + Math.pow(ab_y, 2) + Math.pow(ab_z, 2));
		float projectionScalar = Vector3.dot(originToPointX, originToPointY, originToPointZ, ab_x, ab_y, ab_z)
				/ ab_dot_ab;
		projectionScalar = MathUtils.clamp(projectionScalar, 0, 1); // Must be >= 0 & <= 1, as it is a segment

		float projectedX = a.x + projectionScalar * dirX;
		float projectedY = a.y + projectionScalar * dirY;
		float projectedZ = a.z + projectionScalar * dirZ;

		return Vector3.dst(pointX, pointY, pointZ, projectedX, projectedY, projectedZ);
	}

	public static float norDot(Vector3 a, Vector3 b)
	{
		return a.dot(b) / (a.len() * b.len());
	}

	public static void alignVectorTowardTarget(Vector3 a, Vector3 target, float dotThreshold)
	{
		if (a.isZero())
		{
			a.set(target);
			return;
		}
		if (target.isZero())
		{
			return;
		}

		float al = a.len();
		float tl = target.len();
		float dot = a.dot(target) / (al * tl);

		if (dot < dotThreshold)
		{
			float epsilon = dotThreshold - dot;
			a.nor().add(target.x * epsilon / tl, target.y * epsilon / tl, target.z * epsilon / tl).nor().scl(al);
		}
	}

	public static Vector3 alignAngleVectorTowardTarget(Vector3 a, Vector3 target, float maxAngleDeg)
	{
		float angleAx = (a.x % 360 + 360) % 360;
		float angleAy = (a.y % 360 + 360) % 360;
		float angleAz = (a.z % 360 + 360) % 360;

		float angleTx = (target.x % 360 + 360) % 360;
		float angleTy = (target.y % 360 + 360) % 360;
		float angleTz = (target.z % 360 + 360) % 360;

		float dx = Math.abs(angleAx - angleTx);
		float dy = Math.abs(angleAy - angleTy);
		float dz = Math.abs(angleAz - angleTz);

		if (dx > 180)
		{
			dx = 360 - dx;
		}
		if (dy > 180)
		{
			dy = 360 - dy;
		}
		if (dz > 180)
		{
			dz = 360 - dz;
		}

		if (dx > maxAngleDeg)
		{
			if (angleAx < angleTx)
			{
				angleAx = angleTx - maxAngleDeg;
			} else
			{
				angleAx = angleTx + maxAngleDeg;
			}
		}
		if (dy > maxAngleDeg)
		{
			if (angleAy < angleTy)
			{
				angleAy = angleTy - maxAngleDeg;
			} else
			{
				angleAy = angleTy + maxAngleDeg;
			}
		}

		if (dz > maxAngleDeg)
		{
			if (angleAz < angleTz)
			{
				angleAz = angleTz - maxAngleDeg;
			} else
			{
				angleAz = angleTz + maxAngleDeg;
			}
		}

		a.x = angleAx;
		a.y = angleAy;
		a.z = angleAz;

		return a;
	}

	public static float bilinear(float valueX1Y1, float valueX2Y1, float valueX1Y2, float valueX2Y2, float x1, float x2,
			float y1, float y2, float x, float y)
	{
		// interpolate along y = y1 (top edge)
		float interpY1 = ((x2 - x) / (x2 - x1)) * valueX1Y1 + ((x - x1) / (x2 - x1)) * valueX2Y1;
		// interpolate along y = y2 (bottom edge)
		float interpY2 = ((x2 - x) / (x2 - x1)) * valueX1Y2 + ((x - x1) / (x2 - x1)) * valueX2Y2;
		// interpolate between the two edges
		return ((y2 - y) / (y2 - y1)) * interpY1 + ((y - y1) / (y2 - y1)) * interpY2;
	}

	public static float bilinearNormalized(float valueX1Y1, float valueX2Y1, float valueX1Y2, float valueX2Y2, float u,
			float v)
	{
		// u and v are in [0,1], representing the relative position in the cell
		return bilinear(valueX1Y1, valueX2Y1, valueX1Y2, valueX2Y2, 0, 1, 0, 1, u, v);
	}

	public static float cosineLerp(float a, float b, float t)
	{
		float t2 = (1f - (float) Math.cos(t * Math.PI)) * 0.5f;
		return a * (1f - t2) + b * t2;
	}

	public static float cosineBilinear(float valueX1Y1, float valueX2Y1, float valueX1Y2, float valueX2Y2, float x1,
			float x2, float y1, float y2, float x, float y)
	{
		float tx = (x - x1) / (x2 - x1);
		float ty = (y - y1) / (y2 - y1);
		// interpolate along y = y1 (top edge)
		float interpY1 = cosineLerp(valueX1Y1, valueX2Y1, tx);
		// interpolate along y = y2 (bottom edge)
		float interpY2 = cosineLerp(valueX1Y2, valueX2Y2, tx);

		// interpolate between the two edges
		return cosineLerp(interpY1, interpY2, ty);
	}

	public static float cosineBilinearNormalized(float valueX1Y1, float valueX2Y1, float valueX1Y2, float valueX2Y2,
			float u, float v)
	{
		// u and v are in [0,1], representing the relative position in the cell
		return cosineBilinear(valueX1Y1, valueX2Y1, valueX1Y2, valueX2Y2, 0, 1, 0, 1, u, v);
	}

	public static void getRandomPointOnBoundingBox(BoundingBox bb, Vector3 outVec)
	{
		Vector3 min = bb.min;
		Vector3 max = bb.max;

		// Randomly choose which face to pick (0..5)
		int face = MathUtils.random(5);

		switch (face)
		{
		case 0:
			// minX
			outVec.set(min.x, MathUtils.random(min.y, max.y), MathUtils.random(min.z, max.z));
			break;
		case 1:
			// maxX
			outVec.set(max.x, MathUtils.random(min.y, max.y), MathUtils.random(min.z, max.z));
			break;
		case 2:
			// minY
			outVec.set(MathUtils.random(min.x, max.x), min.y, MathUtils.random(min.z, max.z));
			break;
		case 3:
			// maxY
			outVec.set(MathUtils.random(min.x, max.x), max.y, MathUtils.random(min.z, max.z));
			break;
		case 4:
			// minZ
			outVec.set(MathUtils.random(min.x, max.x), MathUtils.random(min.y, max.y), min.z);
			break;
		case 5:
			// maxZ
			outVec.set(MathUtils.random(min.x, max.x), MathUtils.random(min.y, max.y), max.z);
			break;
		}
	}

}
