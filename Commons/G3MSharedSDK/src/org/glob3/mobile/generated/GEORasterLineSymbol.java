package org.glob3.mobile.generated; 
//
//  GEORasterLineSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEORasterLineSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//


//class Geodetic2D;

public class GEORasterLineSymbol extends GEORasterSymbol
{
  private final java.util.ArrayList<Geodetic2D> _coordinates;

  private static Sector calculateSector(java.util.ArrayList<Geodetic2D> coordinates)
  {
    final int size = coordinates.size();
    if (size == 0)
    {
      return null;
    }
  
    final Geodetic2D coordinate0 = coordinates.get(0);
  
    double minLatInDegrees = coordinate0.latitude().degrees();
    double maxLatInDegrees = minLatInDegrees;
  
    double minLonInDegrees = coordinate0.longitude().degrees();
    double maxLonInDegrees = minLatInDegrees;
  
    for (int i = 1; i < size; i++)
    {
      final Geodetic2D coordinate = coordinates.get(i);
  
      final double latInDegrees = coordinate.latitude().degrees();
      if (latInDegrees < minLatInDegrees)
      {
        minLatInDegrees = latInDegrees;
      }
      else if (latInDegrees > maxLatInDegrees)
      {
        maxLatInDegrees = latInDegrees;
      }
  
      final double lonInDegrees = coordinate.longitude().degrees();
      if (lonInDegrees < minLonInDegrees)
      {
        minLonInDegrees = lonInDegrees;
      }
      else if (lonInDegrees > maxLonInDegrees)
      {
        maxLonInDegrees = lonInDegrees;
      }
    }
  
    return new Sector(Geodetic2D.fromDegrees(minLatInDegrees, minLonInDegrees), Geodetic2D.fromDegrees(maxLatInDegrees, maxLonInDegrees));
  }

  private GEORasterLineSymbol(java.util.ArrayList<Geodetic2D> coordinates, Sector sector)
  {
     super(sector);
     _coordinates = coordinates;
  }

  public GEORasterLineSymbol(java.util.ArrayList<Geodetic2D> coordinates)
  {
     super(calculateSector(coordinates));
     _coordinates = coordinates;
  }

  public final GEORasterLineSymbol createSymbol()
  {
    return new GEORasterLineSymbol(_coordinates, new Sector(_sector));
  }


}