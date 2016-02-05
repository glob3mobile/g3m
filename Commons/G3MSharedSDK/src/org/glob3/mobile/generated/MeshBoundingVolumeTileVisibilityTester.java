package org.glob3.mobile.generated; 
//
//  MeshBoundingVolumeTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

//
//  MeshBoundingVolumeTileVisibilityTester.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//




public class MeshBoundingVolumeTileVisibilityTester extends TileVisibilityTester
{

  public void dispose()
  {
    super.dispose();
  }

  public final boolean isVisible(G3MRenderContext rc, PlanetRenderContext prc, Tile tile)
  {
    final Mesh mesh = tile.getTessellatorMesh(rc, prc);
    if (mesh == null)
    {
      return false;
    }
  
    return mesh.getBoundingVolume().touchesFrustum(prc._frustumInModelCoordinates);
  }

  public final void onTileHasChangedMesh(Tile tile)
  {
  
  }

  public final void onLayerTilesRenderParametersChanged(LayerTilesRenderParameters ltrp)
  {
  
  }

  public final void renderStarted()
  {
  
  }

}