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

  private boolean contains(java.util.ArrayList<Geodetic2D> coordinates, Geodetic2D point)
  {
    int sidesCrossedMovingRight = 0;
    final int coordinatesCount = coordinates.size();
    int previousIndex = coordinatesCount - 1;
  
    for (int index = 0; index < coordinatesCount; index++)
    {
  
      Geodetic2D firstCoordinate = coordinates.get(previousIndex);
      Geodetic2D secondCoordinate = coordinates.get(index);
  
      if (!firstCoordinate.isEquals(secondCoordinate))
      {
        if (firstCoordinate.isEquals(point))
        {
          return true;
        }
  
        final double pointLatInDegrees = point._latitude._degrees;
        final double firstCoorLatInDegrees = firstCoordinate._latitude._degrees;
        final double secondCoorLatInDegrees = secondCoordinate._latitude._degrees;
  
        final double pointLonInDegrees = point._longitude._degrees;
        final double firstCoorLonInDegrees = firstCoordinate._longitude._degrees;
        final double secondCoorLonInDegrees = secondCoordinate._longitude._degrees;
  
  
        if ((firstCoorLatInDegrees <= pointLatInDegrees && pointLatInDegrees < secondCoorLatInDegrees) || (secondCoorLatInDegrees <= pointLatInDegrees && pointLatInDegrees < firstCoorLatInDegrees))
        {
  
          double intersectLongitudInDegrees = (secondCoorLonInDegrees - firstCoorLonInDegrees) * (pointLatInDegrees - firstCoorLatInDegrees) / (secondCoorLatInDegrees - firstCoorLatInDegrees) + firstCoorLonInDegrees;
  
  
          if (pointLonInDegrees <= intersectLongitudInDegrees)
          {
            sidesCrossedMovingRight++;
          }
        }
      }
      previousIndex = index;
    }
    if (sidesCrossedMovingRight == 0)
    {
      return false;
    }
  
    return sidesCrossedMovingRight % 2 == 1;
  }

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
    if (_holesCoordinatesArray != null)
    {
      final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
      for (int j = 0; j < holesCoordinatesArraySize; j++)
      {
        final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
        result += holeCoordinates.size();
      }
    }
    return result;
  }

  public final boolean contains(Geodetic2D point)
  {
    if (getSector().contains(point))
    {
      if (_holesCoordinatesArray != null)
      {
        final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
        for (int j = 0; j < holesCoordinatesArraySize; j++)
        {
          final java.util.ArrayList<Geodetic2D> holeCoordinates = _holesCoordinatesArray.get(j);
          if (contains(holeCoordinates, point))
          {
            return false;
          }
        }
      }
  
      return contains(getCoordinates(), point);
    }
  
    return false;
  }

}