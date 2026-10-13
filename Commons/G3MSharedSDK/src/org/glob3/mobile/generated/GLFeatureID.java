package org.glob3.mobile.generated;
public enum GLFeatureID
{
  GLF_BILLBOARD,
  GLF_VIEWPORT_EXTENT,
  GLF_GEOMETRY,
  GLF_MODEL,
  GLF_PROJECTION,
  GLF_MODEL_TRANSFORM,
  GLF_TEXTURE,
  GLF_COLOR,
  GLF_FLATCOLOR,
  GLF_TEXTURE_ID,
  GLF_TEXTURE_COORDS,
  GLF_DIRECTION_LIGTH,
  GLF_VERTEX_NORMAL,
  GLF_MODEL_VIEW,
  GLF_BLENDING_MODE,
  GLF_CAMERA_POSITION,
  GLF_RIBBON_WIDTH,
  GLF_RIBBON_SIDE,
  GLF_STARS,
  GLF_COLOR_GRADE;

   public int getValue()
   {
      return this.ordinal();
   }

   public static GLFeatureID forValue(int value)
   {
      return values()[value];
   }
}