package org.glob3.mobile.generated; 
//
//  GEO2DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEO2DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//class Geodetic2D;

public class GEO2DMultiLineStringGeometry extends GEOMultiLineStringGeometry
{
  private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _coordinatesArray;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    return sc.getSymbolizer().createSymbols(this);
  }


  public GEO2DMultiLineStringGeometry(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
     _coordinatesArray = coordinatesArray;

  }

  public void dispose()
  {
    final int coordinatesArrayCount = _coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = _coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        Geodetic2D coordinate = coordinates.get(j);
        if (coordinate != null)
           coordinate.dispose();
      }
      coordinates = null;
    }
  
    _coordinatesArray = null;
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getCoordinatesArray()
  {
    return _coordinatesArray;
  }


}