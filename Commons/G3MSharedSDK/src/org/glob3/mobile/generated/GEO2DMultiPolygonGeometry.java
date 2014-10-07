package org.glob3.mobile.generated; 
//
//  GEO2DMultiPolygonGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

//
//  GEO2DMultiPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//



//class GEO2DPolygonData;


public class GEO2DMultiPolygonGeometry extends GEOGeometry2D
{
  private java.util.ArrayList<GEO2DPolygonData> _polygonsData;

  protected final java.util.ArrayList<GEOSymbol> createSymbols(GEOSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected final java.util.ArrayList<GEORasterSymbol> createRasterSymbols(GEORasterSymbolizer symbolizer)
  {
    return symbolizer.createSymbols(this);
  }

  protected static java.util.ArrayList<GEO2DPolygonData> copy(java.util.ArrayList<GEO2DPolygonData> polygonsData)
  {
    if (polygonsData == null)
    {
      return null;
    }
    java.util.ArrayList<GEO2DPolygonData> result = new java.util.ArrayList<GEO2DPolygonData>();
    final int size = polygonsData.size();
    for (int i = 0; i < size; i++)
    {
      GEO2DPolygonData each = polygonsData.get(i);
      if (each != null)
      {
        each._retain();
      }
      result.add(each);
    }
    return result;
  }



  public GEO2DMultiPolygonGeometry(java.util.ArrayList<GEO2DPolygonData> polygonsData)
  {
     _polygonsData = polygonsData;
  }

  public void dispose()
  {
    if (_polygonsData != null)
    {
      final int polygonsDataSize = _polygonsData.size();
      for (int i = 0; i < polygonsDataSize; i++)
      {
        GEO2DPolygonData polygonData = _polygonsData.get(i);
        if (polygonData != null)
        {
          polygonData._release();
        }
      }
      _polygonsData = null;
    }
  
    super.dispose();
  }

  public final java.util.ArrayList<GEO2DPolygonData> getPolygonsData()
  {
    return _polygonsData;
  }

  public final long getCoordinatesCount()
  {
    return (_polygonsData == null) ? 0 : _polygonsData.size();
  }

  public final GEO2DMultiPolygonGeometry deepCopy()
  {
    return new GEO2DMultiPolygonGeometry(copy(_polygonsData));
  }

  public final boolean contain(Geodetic2D point)
  {
    if (_polygonsData == null)
    {
      return false;
    }
    final int polygonsDataSize = _polygonsData.size();
    for (int i = 0; i < polygonsDataSize; i++)
    {
      GEO2DPolygonData polygonData = _polygonsData.get(i);
      if (polygonData.contains(point))
      {
        return true;
      }
    }
  
    return false;
  }
}