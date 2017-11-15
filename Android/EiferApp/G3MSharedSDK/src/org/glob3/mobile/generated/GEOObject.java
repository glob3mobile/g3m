package org.glob3.mobile.generated; 
//
//  GEOObject.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//


//class GEORasterSymbolizer;
//class ICanvas;
//class GEORasterProjection;
//class G3MRenderContext;
//class GEOSymbolizer;
//class MeshRenderer;
//class ShapesRenderer;
//class MarksRenderer;
//class GEOVectorLayer;



public abstract class GEOObject
{
  public void dispose()
  {
  }

  public abstract void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel);

  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer);

  public abstract long getCoordinatesCount();

  public abstract GEOObject deepCopy();

  public abstract long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node);

}