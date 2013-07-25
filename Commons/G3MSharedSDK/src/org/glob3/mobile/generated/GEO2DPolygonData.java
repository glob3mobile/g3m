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

public class GEO2DPolygonData
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
    final int coordinatesCount = _coordinates.size();
    for (int i = 0; i < coordinatesCount; i++)
    {
      Geodetic2D coordinate = _coordinates.get(i);
      if (coordinate != null)
         coordinate.dispose();
    }
    _coordinates = null;
  
  
    if (_holesCoordinatesArray != null)
    {
      final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
      for (int j = 0; j < holesCoordinatesArraySize; j++)
      {
        final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
  
        final int holeCoordinatesCount = holeCoordinates.size();
        for (int i = 0; i < holeCoordinatesCount; i++)
        {
          final Geodetic2D holeCoordinate = holeCoordinates.get(i);
  
          if (holeCoordinate != null)
             holeCoordinate.dispose();
        }
  
        holeCoordinates = null;
      }
      _holesCoordinatesArray = null;
    }
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