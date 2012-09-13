package org.glob3.mobile.generated; 
//
//  TileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class RenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTessellator;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class InitializationContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TilesRenderParameters;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic3D;


public abstract class TileTexturizer
{
  public void dispose()
  {
  }

  public abstract boolean isReady(RenderContext rc);

  public abstract void initialize(InitializationContext ic, TilesRenderParameters parameters);

  public abstract Mesh texturize(RenderContext rc, TileRenderContext trc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh);

  public abstract void tileToBeDeleted(Tile tile, Mesh mesh);

  public abstract void tileMeshToBeDeleted(Tile tile, Mesh mesh);

  public abstract boolean tileMeetsRenderCriteria(Tile tile);

  public abstract void justCreatedTopTile(RenderContext rc, Tile tile);

  public abstract void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved);

  public abstract void onTerrainTouchEvent(EventContext ec, Geodetic3D position, Tile tile);

}