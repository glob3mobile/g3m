package org.glob3.mobile.generated;
//
//  GEO3DPolygonData.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//

//
//  GEO3DPolygonData.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//




public class GEO3DPolygonData extends GEO3DCoordinatesData
{
  private final java.util.ArrayList<java.util.ArrayList<Geodetic3D>> _holesCoordinatesArray;

  private boolean contains(java.util.ArrayList<Geodetic3D> coordinates, Geodetic3D point)
  {
    int sidesCrossedMovingRight = 0;
    final int coordinatesCount = coordinates.size();
    int previousIndex = coordinatesCount - 1;
  
    for (int index = 0; index < coordinatesCount; index++)
    {
  
      Geodetic3D firstCoordinate = coordinates.get(previousIndex);
      Geodetic3D secondCoordinate = coordinates.get(index);
  
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

  public GEO3DPolygonData(java.util.ArrayList<Geodetic3D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic3D>> holesCoordinatesArray)
  {
     super(coordinates);
     _holesCoordinatesArray = holesCoordinatesArray;
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic3D>> getHolesCoordinatesArray()
  {
    return _holesCoordinatesArray;
  }

  public final boolean contains(Geodetic3D point)
  {
    if (getSector().contains(point._latitude, point._longitude))
    {
      if (_holesCoordinatesArray != null)
      {
        final int holesCoordinatesArraySize = _holesCoordinatesArray.size();
        for (int j = 0; j < holesCoordinatesArraySize; j++)
        {
          final java.util.ArrayList<Geodetic3D> holeCoordinates = _holesCoordinatesArray.get(j);
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
