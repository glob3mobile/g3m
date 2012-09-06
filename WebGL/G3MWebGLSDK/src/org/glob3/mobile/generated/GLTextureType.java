package org.glob3.mobile.generated; 
public enum GLTextureType
{
  Texture2D;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLTextureType forValue(int value)
	{
		return values()[value];
	}
}