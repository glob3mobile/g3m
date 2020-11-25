package org.glob3.mobile.generated;
//
//  TileTexturizer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/07/12.
//

//
//  TileTexturizer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 12/07/12.
//



//class Mesh;
//class G3MRenderContext;
//class Tile;
//class TileTessellator;
//class G3MContext;
//class TilesRenderParameters;
//class Geodetic3D;
//class LayerSet;
//class LayerTilesRenderParameters;
//class G3MEventContext;
//class PlanetRenderContext;


public abstract class TileTexturizer
{
  public void dispose()
  {
  }

  public abstract RenderState getRenderState(LayerSet layerSet);

  public abstract void initialize(G3MContext context, TilesRenderParameters parameters);

  public abstract Mesh texturize(G3MRenderContext rc, PlanetRenderContext prc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh);

  public abstract void tileToBeDeleted(Tile tile, Mesh mesh);

  public abstract void tileMeshToBeDeleted(Tile tile, Mesh mesh);

  public abstract void justCreatedTopTile(G3MRenderContext rc, Tile tile, LayerSet layerSet);

  public abstract void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved);

  public abstract boolean onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile, LayerSet layerSet);

}