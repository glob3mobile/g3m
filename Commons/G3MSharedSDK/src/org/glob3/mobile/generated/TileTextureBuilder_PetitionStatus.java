package org.glob3.mobile.generated; 
//#define TILE_DOWNLOAD_PRIORITY 1000000000

public enum TileTextureBuilder_PetitionStatus
{
  STATUS_PENDING,
  STATUS_DOWNLOADED,
  STATUS_CANCELED;

   public int getValue()
   {
      return this.ordinal();
   }

   public static TileTextureBuilder_PetitionStatus forValue(int value)
   {
      return values()[value];
   }
}