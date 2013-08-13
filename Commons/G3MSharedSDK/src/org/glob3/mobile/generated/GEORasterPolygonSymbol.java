package org.glob3.mobile.generated; 
//
//  GEORasterPolygonSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEORasterPolygonSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//




//class GEO2DPolygonData;


public class GEORasterPolygonSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _lineStyle;
  private final GEO2DSurfaceRasterStyle   _surfaceStyle;

  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;


  public GEORasterPolygonSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel)
  {
     this(polygonData, lineStyle, surfaceStyle, minTileLevel, -1);
  }
  public GEORasterPolygonSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
  {
     this(polygonData, lineStyle, surfaceStyle, -1, -1);
  }
  public GEORasterPolygonSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel, int maxTileLevel)
  {
     super(calculateSectorFromCoordinates(polygonData.getCoordinates()), minTileLevel, maxTileLevel);
     _coordinates = copyCoordinates(polygonData.getCoordinates());
     _holesCoordinatesArray = copyCoordinatesArray(polygonData.getHolesCoordinatesArray());
     _lineStyle = lineStyle;
     _surfaceStyle = surfaceStyle;
  
  }

  public void dispose()
  {
  
    JAVA_POST_DISPOSE
  }

  public final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    final boolean rasterSurface = _surfaceStyle.apply(canvas);
    final boolean rasterBoundary = _lineStyle.apply(canvas);
  
    rasterPolygon(_coordinates, _holesCoordinatesArray, rasterSurface, rasterBoundary, canvas, projection);
  }

}