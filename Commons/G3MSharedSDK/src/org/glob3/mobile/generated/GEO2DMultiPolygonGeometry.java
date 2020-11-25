package org.glob3.mobile.generated;
//
//  GEO2DMultiPolygonGeometry.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

//
//  GEO2DMultiPolygonGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//



//class GEO2DPolygonData;


public class GEO2DMultiPolygonGeometry extends GEO2DGeometry
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