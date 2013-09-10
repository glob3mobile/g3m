package org.glob3.mobile.generated; 
public enum TouchEventType
{
  Down,
  Up,
  Move,
  LongPress,
  DownUp;

   public int getValue()
   {
      return this.ordinal();
   }

   public static TouchEventType forValue(int value)
   {
      return values()[value];
   }
}