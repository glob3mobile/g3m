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



//class GEOSymbol;
//class GEOFeature;
//class GPUProgramState;

public abstract class GEOGeometry extends GEOObject
{
  private GEOFeature _feature;

  protected abstract java.util.ArrayList<GEOSymbol> createSymbols(G3MRenderContext rc, GEOSymbolizationContext sc);

  public GEOGeometry()
  {
     _feature = null;

  }

<<<<<<< HEAD
  public final void render(G3MRenderContext rc, GLState parentState, GPUProgramState parentProgramState, GEOSymbolizer symbolizer)
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
            mesh.render(rc, state, parentProgramState);
          }
        }
      }
    }
  }


  //#include "G3MError.hpp"
  //#include "G3MError.hpp"
  
=======
>>>>>>> webgl-port
  public void dispose()
  {
  
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

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    java.util.ArrayList<GEOSymbol> symbols = createSymbols(rc, sc);
    if (symbols == null)
    {
      return;
    }
  
    final int symbolsSize = symbols.size();
    for (int i = 0; i < symbolsSize; i++)
    {
      final GEOSymbol symbol = symbols.get(i);
  
      symbol.symbolize(rc, sc);
  
      if (symbol != null)
         symbol.dispose();
    }
  
    symbols = null;
  }

}