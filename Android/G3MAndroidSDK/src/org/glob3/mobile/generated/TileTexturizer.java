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
//class ITimer;

public abstract class TileTexturizer
{

  public abstract Mesh texturize(RenderContext rc, Tile tile, TileTessellator tessellator, Mesh tessellatorMesh, Mesh previousMesh, ITimer timer);

  public abstract void tileToBeDeleted(Tile tile);

  public void dispose()
  {

  }


  public abstract boolean tileMeetsRenderCriteria(Tile tile);

  public abstract void justCreatedTopTile(Tile tile);

  public abstract boolean isReadyToRender(RenderContext rc);

}