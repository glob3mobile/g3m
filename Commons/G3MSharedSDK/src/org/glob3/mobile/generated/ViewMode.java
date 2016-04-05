package org.glob3.mobile.generated; 
public enum ViewMode
{
  MONO,
  STEREO;

   public int getValue()
   {
      return this.ordinal();
   }

   public static ViewMode forValue(int value)
   {
      return values()[value];
   }
}