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
  
    double minLatInRadians = coordinate0._latitude._radians;
    double maxLatInRadians = minLatInRadians;
  
    double minLonInRadians = coordinate0._longitude._radians;
    double maxLonInRadians = minLonInRadians;
  
    for (int i = 1; i < size; i++)
    {
      final Geodetic2D coordinate = _coordinates.get(i);
  
      final double latInRadians = coordinate._latitude._radians;
      if (latInRadians < minLatInRadians)
      {
        minLatInRadians = latInRadians;
      }
      if (latInRadians > maxLatInRadians)
      {
        maxLatInRadians = latInRadians;
      }
  
      final double lonInRadians = coordinate._longitude._radians;
      if (lonInRadians < minLonInRadians)
      {
        minLonInRadians = lonInRadians;
      }
      if (lonInRadians > maxLonInRadians)
      {
        maxLonInRadians = lonInRadians;
      }
    }
  
    Sector result = new Sector(Geodetic2D.fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001), Geodetic2D.fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  
    //  int __REMOVE_DEBUG_CODE;
    //  for (int i = 0; i < size; i++) {
    //    const Geodetic2D* coordinate = coordinates->at(i);
    //    if (!result->contains(*coordinate)) {
    //      printf("xxx\n");
    //    }
    //  }
  
    return result;
  }

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
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