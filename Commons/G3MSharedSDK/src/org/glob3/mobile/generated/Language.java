package org.glob3.mobile.generated; 
public enum Language
{
  English,
  Spanish,
  German,
  French,
  Italian,
  Dutch;

	public int getValue()
	{
		return this.ordinal();
	}

	public static Language forValue(int value)
	{
		return values()[value];
	}
}