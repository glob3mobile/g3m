package org.glob3.mobile.generated; 
public enum FrameBufferContent
{
  EMPTY_FRAMEBUFFER,
  REGULAR_FRAME,
  DEPTH_IMAGE;

   public int getValue()
   {
      return this.ordinal();
   }

   public static FrameBufferContent forValue(int value)
   {
      return values()[value];
   }
}