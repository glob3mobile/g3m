package org.glob3.mobile.generated; 
//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEOMultiLineRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



public class GEOMultiLineRasterSymbol extends GEORasterSymbol
{
  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;

  private static Sector calculateSector(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
  
    final IMathUtils mu = IMathUtils.instance();
  
    final double maxDouble = mu.maxDouble();
    final double minDouble = mu.minDouble();
  
    double minLatInDegrees = maxDouble;
    double maxLatInDegrees = minDouble;
  
    double minLonInDegrees = maxDouble;
    double maxLonInDegrees = minDouble;
  
    final int coordinatesArrayCount = coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        final Geodetic2D coordinate = coordinates.get(j);
  
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
    }
  
    if ((minLatInDegrees == maxDouble) || (maxLatInDegrees == minDouble) || (minLonInDegrees == maxDouble) || (maxLonInDegrees == minDouble))
    {
      return null;
    }
  
    return new Sector(Geodetic2D.fromDegrees(minLatInDegrees, minLonInDegrees), Geodetic2D.fromDegrees(maxLatInDegrees, maxLonInDegrees));
  
  }

  private GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Sector sector)
  {
     super(sector);
     _coordinatesArray = coordinatesArray;
  }

  public GEOMultiLineRasterSymbol(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
     super(calculateSector(coordinatesArray));
     _coordinatesArray = coordinatesArray;
  }

  public final GEOMultiLineRasterSymbol createSymbol()
  {
    return new GEOMultiLineRasterSymbol(_coordinatesArray, new Sector(_sector));
  }

}