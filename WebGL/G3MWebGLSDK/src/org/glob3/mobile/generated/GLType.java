package org.glob3.mobile.generated; 
public enum GLType
{
  Float,
  UnsignedByte,
  UnsignedInt,
  Int;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLType forValue(int value)
	{
		return values()[value];
	}
}