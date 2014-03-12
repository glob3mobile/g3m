package org.glob3.mobile.generated; 
public enum RenderType
{
  REGULAR_RENDER,
  Z_BUFFER_RENDER;

   public int getValue()
   {
      return this.ordinal();
   }

   public static RenderType forValue(int value)
   {
      return values()[value];
   }
}