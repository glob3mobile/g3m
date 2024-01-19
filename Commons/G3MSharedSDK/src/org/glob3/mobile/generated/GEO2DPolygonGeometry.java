package org.glob3.mobile.generated;
//
//  GEO2DPolygonGeometry.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEO2DPolygonGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//



//class Geodetic2D;
//class GEO2DPolygonData;
//class GEOSymbol;

public class GEO2DPolygonGeometry extends GEO2DGeometry
{
  private final GEO2DPolygonData _polygonData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  public GEO2DPolygonGeometry(GEO2DPolygonData polygonData)
  {
     _polygonData = polygonData;
  }

  public void dispose()
  {
    if (_polygonData != null)
    {
      _polygonData._release();
    }
    super.dispose();
  }

  public final GEO2DPolygonData getPolygonData()
  {
    return _polygonData;
  }

  public final java.util.ArrayList<Geodetic2D> getCoordinates()
  {
    return _polygonData.getCoordinates();
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> getHolesCoordinatesArray()
  {
    return _polygonData.getHolesCoordinatesArray();
  }

  public final boolean contain(Geodetic2D point)
  {
    if (_polygonData != null)
    {
      return _polygonData.contains(point);
    }
    return false;
  }

}