package org.glob3.mobile.generated;
//
//  MeasureHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//

//
//  MeasureHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//



//class Measure;
//class Geodetic3D;
//class Vector3D;
//class Angle;


public abstract class MeasureHandler
{

  public void dispose()
  {

  }

  public abstract void onVertexDeselection(Measure measure);

  public abstract void onVertexSelection(Measure measure, Geodetic3D geodetic, Vector3D cartesian, int selectedIndex);

  public abstract String getAngleLabel(Measure measure, int vertexIndex, Angle angle);

  public abstract String getDistanceLabel(Measure measure, int vertexIndexFrom, int vertexIndexTo, double distanceInMeters);

}