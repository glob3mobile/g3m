package org.glob3.mobile.generated;
//
//  GEOMeshes.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/6/18.
//

//
//  GEOMeshes.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 11/6/18.
//



//class Mesh;


public class GEOMeshes extends GEOObject
{
  private java.util.ArrayList<Mesh> _meshes = new java.util.ArrayList<Mesh>();

  public GEOMeshes(java.util.ArrayList<Mesh> meshes)
  {
     _meshes = meshes;
  
  }

  public void dispose()
  {
    for (int i = 0; i < _meshes.size(); i++)
    {
      if (_meshes.get(i) != null)
         _meshes.get(i).dispose();
    }
  }

  public final void rasterize(GEORasterSymbolizer symbolizer, ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
  // do nothing
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
  // do nothing
  }

  public final int symbolize(VectorStreamingRenderer.VectorSet vectorSet, VectorStreamingRenderer.Node node)
  {
    final int result = vectorSet.symbolizeMeshes(node, _meshes);
    _meshes.clear(); // moved meshes ownership to vectorSet
    return result;
  }

}