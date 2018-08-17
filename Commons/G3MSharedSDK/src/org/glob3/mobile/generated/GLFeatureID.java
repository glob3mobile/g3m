package org.glob3.mobile.generated;import java.util.*;

//
//  GLFeature.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

//
//  GLFeature.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 27/10/12.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
  GLF_POINT_SHAPE,
	GLF_COLOR_RANGE,
	GLF_DYNAMIC_COLOR_RANGE,
	GLF_TRANSPARENCY_DISTANCE_THRESHOLD;

	public int getValue()
	{
		return this.ordinal();
	}

	public static GLFeatureID forValue(int value)
	{
		return values()[value];
	}
}
