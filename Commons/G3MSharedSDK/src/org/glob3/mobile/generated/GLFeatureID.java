package org.glob3.mobile.generated;
//
//  GLFeature.cpp
//  G3M
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

//
//  GLFeature.hpp
//  G3M
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//





//class Camera;


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
  GLF_CAMERA_POSITION;

   public int getValue()
   {
      return this.ordinal();
   }

   public static GLFeatureID forValue(int value)
   {
      return values()[value];
   }
}