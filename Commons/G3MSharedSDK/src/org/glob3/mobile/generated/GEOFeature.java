package org.glob3.mobile.generated;
//
//  GEOFeature.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOFeature.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//




//class GEOGeometry;
//class JSONBaseObject;
//class JSONObject;
//class GPUProgramState;
//class GLGlobalState;
//class GEOSymbolizer;

public class GEOFeature extends GEOObject
{
  private final JSONBaseObject _id;
  private final GEOGeometry _geometry;
  private final JSONObject _properties;


  public GEOFeature(JSONBaseObject id, GEOGeometry geometry, JSONObject properties)
  {
     _id = id;
     _geometry = geometry;
     _properties = properties;
    if (_geometry != null)
    {
      _geometry.setFeature(this);
    }
  }

  public void dispose()
  {
    if (_id != null)
       _id.dispose();
    if (_geometry != null)
       _geometry.dispose();
    if (_properties != null)
       _properties.dispose();
    super.dispose();
  }

  public final JSONObject getProperties()
  {
    return _properties;
  }

  public final GEOGeometry getGeometry()
  {
    return _geometry;
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
    if (_geometry != null)
    {
      _geometry.symbolize(rc, symbolizer, meshRenderer, shapesRenderer, marksRenderer, geoVectorLayer);
    }
  }

  public final void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
    if (_geometry != null)
    {
      _geometry.rasterize(symbolizer, canvas, projection, tileLevel);
    }
  }

  public final int symbolize(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    return (_geometry == null) ? 0 : _geometry.symbolize(vectorSet, node);
  }

}