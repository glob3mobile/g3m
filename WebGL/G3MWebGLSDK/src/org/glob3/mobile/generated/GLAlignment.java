package org.glob3.mobile.generated; 
public enum GLAlignment
{
  Unpack,
  Pack;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLAlignment forValue(int value)
	{
		return values()[value];
	}
}