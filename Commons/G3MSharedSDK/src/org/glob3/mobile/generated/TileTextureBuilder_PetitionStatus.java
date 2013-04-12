package org.glob3.mobile.generated; 
//#include "G3MError.hpp"
//#include "G3MError.hpp"


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