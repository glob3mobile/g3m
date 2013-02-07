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
//class Mesh;
//class Tile;
//class MutableVector2D;
//class IFloatBuffer;

public abstract class TileTessellator {
  public void dispose() {
  }

  public abstract boolean isReady(G3MRenderContext rc);

  public abstract Mesh createMesh(G3MRenderContext rc, Tile tile);

  public abstract Mesh createDebugMesh(G3MRenderContext rc, Tile tile);

  public abstract IFloatBuffer createUnitTextCoords();

}