package org.glob3.mobile.generated;
//
//  GEO2DCoordinatesData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

//
//  GEO2DCoordinatesData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//



//class Geodetic2D;
//class Sector;


public class GEO2DCoordinatesData extends RCObject
{
  private final java.util.ArrayList<Geodetic2D> _coordinates;

  private Sector _sector;

  private Sector calculateSector()
  {
    final int size = _coordinates.size();
    if (size == 0)
    {
      return null;
    }
  
    final Geodetic2D coordinate0 = _coordinates.get(0);
  
    double minLatRad = coordinate0._latitude._radians;
    double maxLatRad = minLatRad;
  
    double minLonRad = coordinate0._longitude._radians;
    double maxLonRad = minLonRad;
  
    for (int i = 1; i < size; i++)
    {
      final Geodetic2D coordinate = _coordinates.get(i);
  
      final double latRad = coordinate._latitude._radians;
      if (latRad < minLatRad)
      {
        minLatRad = latRad;
      }
      if (latRad > maxLatRad)
      {
        maxLatRad = latRad;
      }
  
      final double lonRad = coordinate._longitude._radians;
      if (lonRad < minLonRad)
      {
        minLonRad = lonRad;
      }
      if (lonRad > maxLonRad)
      {
        maxLonRad = lonRad;
      }
    }
  
    final double lowerLatRadians = (minLatRad == maxLatRad) ? minLatRad - 0.0001 : minLatRad;
    final double upperLatRadians = (minLatRad == maxLatRad) ? maxLatRad + 0.0001 : maxLatRad;
  
    final double lowerLonRadians = (minLonRad == maxLonRad) ? minLonRad - 0.0001 : minLonRad;
    final double upperLonRadians = (minLonRad == maxLonRad) ? maxLonRad + 0.0001 : maxLonRad;
  
    return Sector.newFromRadians(lowerLatRadians, lowerLonRadians, upperLatRadians, upperLonRadians);
  }

  public void dispose()
  {
    _sector = null;
    super.dispose();
  }

  public GEO2DCoordinatesData(java.util.ArrayList<Geodetic2D> coordinates)
  {
     _coordinates = coordinates;
     _sector = null;
  }

  public final java.util.ArrayList<Geodetic2D> getCoordinates()
  {
    return _coordinates;
  }

  public final Sector getSector()
  {
    if (_sector == null)
    {
      _sector = calculateSector();
    }
    return _sector;
  }

  public final int size()
  {
    return (_coordinates == null) ? 0 : _coordinates.size();
  }

  public final Geodetic2D get(int index)
  {
    return _coordinates.get(index);
  }

  public long getCoordinatesCount()
  {
    return (_coordinates == null) ? 0 : _coordinates.size();
  }

}
