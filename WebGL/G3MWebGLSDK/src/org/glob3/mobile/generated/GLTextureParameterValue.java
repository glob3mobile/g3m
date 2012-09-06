package org.glob3.mobile.generated; 
public enum GLTextureParameterValue
{
  Linear,
  ClampToEdge;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLTextureParameterValue forValue(int value)
	{
		return values()[value];
	}
}