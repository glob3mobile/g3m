package org.glob3.mobile.generated; 
public enum StrokeJoin
{
  JOIN_MITER,
  JOIN_ROUND,
  JOIN_BEVEL;

   public int getValue()
   {
      return this.ordinal();
   }

   public static StrokeJoin forValue(int value)
   {
      return values()[value];
   }
}