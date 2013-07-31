package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////

public enum GLCameraGroupFeatureType
{
  F_PROJECTION,
  F_CAMERA_MODEL,
  F_MODEL_TRANSFORM;

   public int getValue()
   {
      return this.ordinal();
   }

   public static GLCameraGroupFeatureType forValue(int value)
   {
      return values()[value];
   }
}