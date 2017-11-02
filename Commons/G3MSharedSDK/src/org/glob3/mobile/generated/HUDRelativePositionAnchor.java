package org.glob3.mobile.generated;
public enum HUDRelativePositionAnchor
{
  VIEWPORT_WIDTH,
  VIEWPORT_HEIGHT;

   public int getValue()
   {
      return this.ordinal();
   }

   public static HUDRelativePositionAnchor forValue(int value)
   {
      return values()[value];
   }
}
