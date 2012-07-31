package org.glob3.mobile.generated; 
public enum CenterStrategy
{
  NoCenter,
  AveragedVertex,
  FirstVertex,
  GivenCenter;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CenterStrategy forValue(int value)
	{
		return values()[value];
	}
}