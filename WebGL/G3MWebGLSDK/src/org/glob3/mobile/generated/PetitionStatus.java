package org.glob3.mobile.generated; 
public enum PetitionStatus
{
  STATUS_PENDING,
  STATUS_DOWNLOADED,
  STATUS_CANCELED;

	public int getValue()
	{
		return this.ordinal();
	}

	public static PetitionStatus forValue(int value)
	{
		return values()[value];
	}
}