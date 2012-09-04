package org.glob3.mobile.generated; 
public enum GLVariable
{
  Viewport;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLVariable forValue(int value)
	{
		return values()[value];
	}
}