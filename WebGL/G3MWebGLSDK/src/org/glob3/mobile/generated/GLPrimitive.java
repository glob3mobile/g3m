package org.glob3.mobile.generated; 
public enum GLPrimitive
{
  TriangleStrip,
  Lines,
  LineLoop,
  Points;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLPrimitive forValue(int value)
	{
		return values()[value];
	}
}