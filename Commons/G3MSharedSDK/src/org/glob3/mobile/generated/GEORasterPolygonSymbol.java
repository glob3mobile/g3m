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





public class GEORasterPolygonSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _lineStyle;

  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;
  private final GEO2DSurfaceRasterStyle _surfaceStyle = new GEO2DSurfaceRasterStyle();

  public final void rasterize(ICanvas canvas, GEORasterProjection projection)
  {
    final boolean rasterSurface = _surfaceStyle.apply(canvas);
    final boolean rasterBoundary = _lineStyle.apply(canvas);
  
    rasterPolygon(_coordinates, _holesCoordinatesArray, rasterSurface, rasterBoundary, canvas, projection);
  }


  public GEORasterPolygonSymbol(java.util.ArrayList<Geodetic2D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
  {
     super(calculateSectorFromCoordinates(coordinates));
     _coordinates = copyCoordinates(coordinates);
     _holesCoordinatesArray = copyCoordinatesArray(holesCoordinatesArray);
     _lineStyle = lineStyle;
     _surfaceStyle = new GEO2DSurfaceRasterStyle(surfaceStyle);
  
  }

}