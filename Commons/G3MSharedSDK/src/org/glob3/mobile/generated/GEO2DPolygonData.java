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


///#include <vector>
///#include "RCObject.hpp"
//class Geodetic2D;

public class GEO2DPolygonData extends GEO2DCoordinatesData
{
  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;

  public void dispose()
  {
    super.dispose();
  }

  public GEO2DPolygonData(java.util.ArrayList<Geodetic2D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray)
  {
     super(coordinates);
     _holesCoordinatesArray = holesCoordinatesArray;
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getHolesCoordinatesArray()
  {
    return _holesCoordinatesArray;
  }

  public final long getCoordinatesCount()
  {
    long result = super.getCoordinatesCount();
    final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
    for (int j = 0; j < holesCoordinatesArraySize; j++)
    {
      final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
      result += holeCoordinates.size();
    }
    return result;
  }

}