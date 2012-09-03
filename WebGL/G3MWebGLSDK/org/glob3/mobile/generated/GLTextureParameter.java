package org.glob3.mobile.generated; 
public enum GLTextureParameter
{
  MinFilter,
  MagFilter,
  WrapS,
  WrapT;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLTextureParameter forValue(int value)
	{
		return values()[value];
	}
}