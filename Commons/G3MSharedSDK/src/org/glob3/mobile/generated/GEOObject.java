package org.glob3.mobile.generated;
//
//  GEOObject.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

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
//class GEOSymbolizer;
//class MeshRenderer;
//class ShapesRenderer;
//class MarksRenderer;
//class GEOVectorLayer;


public abstract class GEOObject
{
  private Sector _sector;

  protected abstract Sector calculateSector();

  protected GEOObject()
  {
     _sector = null;

  }

  public void dispose()
  {
    _sector = null;
  }

  public abstract void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel);

  public abstract void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer);

  public abstract long getCoordinatesCount();

  public abstract GEOObject deepCopy();

  public abstract long createFeatureMarks(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node);

  public final Sector getSector()
  {
    if (_sector == null)
    {
      _sector = calculateSector();
    }
    return _sector;
  }

}
