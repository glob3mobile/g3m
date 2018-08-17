package org.glob3.mobile.generated;//
//  TileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//

//
//  TileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTessellator;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerTilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MEventContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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
