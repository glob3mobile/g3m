package org.glob3.mobile.generated; 
//
//  GEOPolygonRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEOPolygonRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//




//class GEO2DPolygonData;


public class GEOPolygonRasterSymbol extends GEORasterSymbol
{
  private final GEO2DPolygonData _polygonData;
  private final GEO2DLineRasterStyle    _lineStyle;
  private final GEO2DSurfaceRasterStyle _surfaceStyle;

  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    final boolean rasterSurface = _surfaceStyle.apply(canvas);
    final boolean rasterBoundary = _lineStyle.apply(canvas);
  
    rasterPolygon(_polygonData, rasterSurface, rasterBoundary, canvas, projection);
  }


  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel)
  {
     this(polygonData, lineStyle, surfaceStyle, minTileLevel, -1);
  }
  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
  {
     this(polygonData, lineStyle, surfaceStyle, -1, -1);
  }
  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel, int maxTileLevel)
  {
     super(minTileLevel, maxTileLevel);
     _polygonData = polygonData;
     _lineStyle = lineStyle;
     _surfaceStyle = surfaceStyle;
    if (_polygonData != null)
    {
      _polygonData._retain();
    }
  }

  public void dispose()
  {
    if (_polygonData != null)
    {
      _polygonData._release();
    }
    super.dispose();
  }

  public final Sector getSector()
  {
    return (_polygonData == null) ? null : _polygonData.getSector();
  }

}