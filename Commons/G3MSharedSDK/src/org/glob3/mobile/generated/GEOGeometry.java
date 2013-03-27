package org.glob3.mobile.generated; 
//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

//
//  GEOGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//



//class Geodetic2D;
//class Mesh;
//class Color;
//class GEOSymbol;
//class GEOFeature;

public abstract class GEOGeometry extends GEOObject
{
  private java.util.ArrayList<Mesh> _meshes;

  private java.util.ArrayList<Mesh> createMeshes(G3MRenderContext rc, GEOSymbolizer symbolizer)
  {
  
    java.util.ArrayList<GEOSymbol> symbols = createSymbols(rc, symbolizer);
    if (symbols == null)
    {
      return null;
    }
  
    java.util.ArrayList<Mesh> meshes = new java.util.ArrayList<Mesh>();
  
    final int symbolsSize = symbols.size();
    for (int i = 0; i < symbolsSize; i++)
    {
      GEOSymbol symbol = symbols.get(i);
  
      Mesh mesh = symbol.createMesh(rc);
      if (mesh != null)
      {
        meshes.add(mesh);
      }
      if (symbol != null)
         symbol.dispose();
    }
    symbols = null;
  
    return meshes;
  }

  private GEOFeature _feature;

  protected java.util.ArrayList<Mesh> getMeshes(G3MRenderContext rc, GEOSymbolizer symbolizer)
  {
    if (_meshes == null)
    {
      _meshes = createMeshes(rc, symbolizer);
    }
    return _meshes;
  }

  protected abstract java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizer symbolizer);

//  Mesh* create2DBoundaryMesh(std::vector<Geodetic2D*>* coordinates,
//                             Color* color,
//                             float lineWidth,
//                             const G3MRenderContext* rc);

  public GEOGeometry()
  {
     _meshes = null;
     _feature = null;

  }

  public final void render(G3MRenderContext rc, GLState parentState, GEOSymbolizer symbolizer)
  {
  //  Mesh* mesh = getMesh(rc, symbolizer);
  
    java.util.ArrayList<Mesh> meshes = getMeshes(rc, symbolizer);
  
    if (meshes != null)
    {
  
      final Frustum frustum = rc.getCurrentCamera().getFrustumInModelCoordinates();
  
      final int meshesCount = _meshes.size();
      for (int i = 0; i < meshesCount; i++)
      {
        Mesh mesh = meshes.get(0);
        if (mesh != null)
        {
          final Extent extent = mesh.getExtent();
  
          if (extent.touches(frustum))
          {
            GLState state = new GLState(parentState);
            state.disableDepthTest();
            mesh.render(rc, state);
          }
        }
      }
    }
  }

  public void dispose()
  {
    if (_meshes != null)
    {
      final int meshesCount = _meshes.size();
      for (int i = 0; i < meshesCount; i++)
      {
        if (_meshes.get(0) != null)
           _meshes.get(0).dispose();
      }
      _meshes = null;
    }
  }

  public final void setFeature(GEOFeature feature)
  {
    if (_feature != feature)
    {
      if (_feature != null)
         _feature.dispose();
      _feature = feature;
    }
  }

  public final GEOFeature getFeature()
  {
    return _feature;
  }

}