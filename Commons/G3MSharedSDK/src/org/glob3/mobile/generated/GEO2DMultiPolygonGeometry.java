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

  protected final java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    return sc.getSymbolizer().createSymbols(this);
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
           polygonData.dispose();
      }
      _polygonsData = null;
    }
  }

  public final java.util.ArrayList<GEO2DPolygonData> getPolygonsData()
  {
    return _polygonsData;
  }

}