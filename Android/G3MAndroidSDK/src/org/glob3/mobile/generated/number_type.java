package org.glob3.mobile.generated; 
public enum number_type
{
  int_type,
  long_type,
  float_type,
  double_type;

	public int getValue()
	{
		return this.ordinal();
	}

	public static number_type forValue(int value)
	{
		return values()[value];
	}
}