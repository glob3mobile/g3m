package org.glob3.mobile.generated; 
public enum GLBlendFactor
{
  SrcAlpha,
  OneMinusSrcAlpha;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLBlendFactor forValue(int value)
	{
		return values()[value];
	}
}