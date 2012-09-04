package org.glob3.mobile.generated; 
public enum GLError
{
  NoError,
  InvalidEnum,
  InvalidValue,
  InvalidOperation,
  OutOfMemory,
  UnknownError;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLError forValue(int value)
	{
		return values()[value];
	}
}