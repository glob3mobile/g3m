package org.glob3.mobile.generated; 
public enum GLBufferType
{
  ColorBuffer,
  DepthBuffer;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLBufferType forValue(int value)
	{
		return values()[value];
	}
}