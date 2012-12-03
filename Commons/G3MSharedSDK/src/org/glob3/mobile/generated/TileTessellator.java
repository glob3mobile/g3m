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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class G3MRenderContext;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Tile;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;

public abstract class TileTessellator
{
  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isReady(const G3MRenderContext *rc) const = 0;
  public abstract boolean isReady(G3MRenderContext rc);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createMesh(const G3MRenderContext* rc, const Tile* tile) const = 0;
  public abstract Mesh createMesh(G3MRenderContext rc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual Mesh* createDebugMesh(const G3MRenderContext* rc, const Tile* tile) const = 0;
  public abstract Mesh createDebugMesh(G3MRenderContext rc, Tile tile);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual IFloatBuffer* createUnitTextCoords() const = 0;
  public abstract IFloatBuffer createUnitTextCoords();

}