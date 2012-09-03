package org.glob3.mobile.generated; 
public enum GLFeature
{
  PolygonOffsetFill,
  DepthTest,
  Blend,
  CullFacing;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLFeature forValue(int value)
	{
		return values()[value];
	}
}