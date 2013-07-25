package org.glob3.mobile.generated; 
//
//  GEO2DPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEO2DPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//


//class Geodetic2D;

public class GEO2DPolygonGeometry extends GEOGeometry2D
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    return sc.getSymbolizer().createSymbols(this);
  }

  public GEO2DPolygonGeometry(java.util.ArrayList<Geodetic2D> coordinates)
  {
     _coordinates = coordinates;
     _holesCoordinatesArray = null;
  }

  public GEO2DPolygonGeometry(java.util.ArrayList<Geodetic2D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray)
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