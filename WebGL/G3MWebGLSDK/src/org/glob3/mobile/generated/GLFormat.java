package org.glob3.mobile.generated; 
public enum GLFormat
{
  RGBA;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLFormat forValue(int value)
	{
		return values()[value];
	}
}