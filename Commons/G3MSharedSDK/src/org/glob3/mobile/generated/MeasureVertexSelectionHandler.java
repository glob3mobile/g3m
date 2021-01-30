package org.glob3.mobile.generated;
//
//  MeasureVertexSelectionHandler.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//

//
//  MeasureVertexSelectionHandler.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/30/21.
//


//class Measure;
//class Geodetic3D;
//class Vector3D;


public abstract class MeasureVertexSelectionHandler
{

  public void dispose()
  {

  }

  public abstract void onVertexDeselection(Measure measure);

  public abstract void onVertexSelection(Measure measure, Geodetic3D geodetic, Vector3D cartesian, int selectedIndex);

}