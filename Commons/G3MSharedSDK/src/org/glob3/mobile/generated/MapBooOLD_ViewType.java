package org.glob3.mobile.generated; 
public enum MapBooOLD_ViewType
{
  VIEW_RUNTIME,
  VIEW_EDITION_PREVIEW,
  VIEW_PRESENTATION;

   public int getValue()
   {
      return this.ordinal();
   }

   public static MapBooOLD_ViewType forValue(int value)
   {
      return values()[value];
   }
}