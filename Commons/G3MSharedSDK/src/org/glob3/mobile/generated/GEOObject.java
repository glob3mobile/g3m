package org.glob3.mobile.generated;
//
//  GEOObject.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOObject.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//



//class GEORasterSymbolizer;
//class ICanvas;
//class GEORasterProjection;
//class GEOSymbolizer;
//class MeshRenderer;
//class ShapesRenderer;
//class MarksRenderer;
//class GEOVectorLayer;


public abstract class GEOObject
{
  protected GEOObject()
  {
  }

  public void dispose()
  {
  }

  public abstract void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel);

  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer);

  public abstract int symbolize(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node);

}
