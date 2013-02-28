package org.glob3.mobile.generated; 
//
//  TileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  TileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//


//class G3MRenderContext;
//class Planet;
//class Mesh;
//class Tile;
//class MutableVector2D;
//class IFloatBuffer;
//class ElevationData;


public abstract class TileTessellator
{
  public void dispose()
  {
  }

  public abstract boolean isReady(G3MRenderContext rc);

  public abstract Mesh createTileMesh(Planet planet, Vector2I resolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean debug);

  public abstract Vector2I getTileMeshResolution(Planet planet, Vector2I resolution, Tile tile, boolean debug);

  public abstract Mesh createTileDebugMesh(Planet planet, Vector2I resolution, Tile tile);

  public abstract IFloatBuffer createTextCoords(Vector2I resolution, Tile tile, boolean mercator);

}