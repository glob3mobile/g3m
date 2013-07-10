package org.glob3.mobile.generated; 
//
//  GEOSymbolizationContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

//
//  GEOSymbolizationContext.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//


//class GEOSymbolizer;
//class MeshRenderer;
//class MarksRenderer;
//class ShapesRenderer;
//class GEOTileRasterizer;

public class GEOSymbolizationContext
{
  private final GEOSymbolizer _symbolizer;

  private MeshRenderer _meshRenderer;
  private ShapesRenderer _shapesRenderer;
  private MarksRenderer _marksRenderer;
  private GEOTileRasterizer _geoTileRasterizer;

  public GEOSymbolizationContext(GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOTileRasterizer geoTileRasterizer)
  {
     _symbolizer = symbolizer;
     _meshRenderer = meshRenderer;
     _shapesRenderer = shapesRenderer;
     _marksRenderer = marksRenderer;
     _geoTileRasterizer = geoTileRasterizer;

  }

  public final GEOSymbolizer getSymbolizer()
  {
    return _symbolizer;
  }

  public final MeshRenderer getMeshRenderer()
  {
    return _meshRenderer;
  }

  public final ShapesRenderer getShapesRenderer()
  {
    return _shapesRenderer;
  }

  public final MarksRenderer getMarksRenderer()
  {
    return _marksRenderer;
  }

  public final GEOTileRasterizer getGEOTileRasterizer()
  {
    return _geoTileRasterizer;
  }

}