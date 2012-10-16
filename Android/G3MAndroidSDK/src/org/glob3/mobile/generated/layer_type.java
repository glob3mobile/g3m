package org.glob3.mobile.generated; 
public enum layer_type
{
  WMS,
  THREED,
  PANO;

	public int getValue()
	{
		return this.ordinal();
	}

	public static layer_type forValue(int value)
	{
		return values()[value];
	}
}