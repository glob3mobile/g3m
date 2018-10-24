package org.glob3.mobile.generated;
//
//  GEO3DPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//

//
//  GEO3DPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//


//class Geodetic3D;

//class GEO3DPolygonData;

public class GEO3DPolygonGeometry extends GEO3DGeometry
{
  private final GEO3DPolygonData _polygonData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final Sector calculateSector()
  {
    return (_polygonData == null) ? null : _polygonData.getSector();
  }

  public GEO3DPolygonGeometry(GEO3DPolygonData polygonData)
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

  public final GEO3DPolygonData getPolygonData()
  {
    return _polygonData;
  }

  public final java.util.ArrayList<Geodetic3D> getCoordinates()
  {
    return _polygonData.getCoordinates();
  }

  public final java.util.ArrayList<java.util.ArrayList<Geodetic3D>> getHolesCoordinatesArray()
  {
    return _polygonData.getHolesCoordinatesArray();
  }

  public final long getCoordinatesCount()
  {
    return (_polygonData == null) ? 0 : _polygonData.getCoordinatesCount();
  }

  public final GEO3DPolygonGeometry deepCopy()
  {
    if (_polygonData != null)
    {
      _polygonData._retain();
    }
    return new GEO3DPolygonGeometry(_polygonData);
  }

  public final boolean contain(Geodetic3D point)
  {
    if (_polygonData != null)
    {
      return _polygonData.contains(point);
    }
    return false;
  }

}