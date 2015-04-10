package org.glob3.mobile.generated; 
public enum HorizontalAlignment
{
  Left,
  Center,
  Right;

   public int getValue()
   {
      return this.ordinal();
   }

   public static HorizontalAlignment forValue(int value)
   {
      return values()[value];
   }
}