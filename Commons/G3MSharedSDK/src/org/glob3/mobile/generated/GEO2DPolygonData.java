package org.glob3.mobile.generated; 
//
//  GEO2DPolygonData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

//
//  GEO2DPolygonData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//


//class Geodetic2D;


public class GEO2DPolygonData extends Disposable
{
  private final java.util.ArrayList<Geodetic2D> _coordinates;
  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GEO2DPolygonData(GEO2DPolygonData that);

  public GEO2DPolygonData(java.util.ArrayList<Geodetic2D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray)
  {
     _coordinates = coordinates;
     _holesCoordinatesArray = holesCoordinatesArray;
  }

  public void dispose()
  {
  
    super.dispose();
  
  }

  public final java.util.ArrayList<Geodetic2D> getCoordinates()
  {
    return _coordinates;
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getHolesCoordinatesArray()
  {
    return _holesCoordinatesArray;
  }

}