package org.glob3.mobile.generated; 
//
//  Mesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//class Vector3D;
//class GPUProgramState;

public abstract class Mesh extends Disposable
{

  public void dispose()
  {
    JAVA_POST_DISPOSE
  }

  public abstract int getVertexCount();

  public abstract Vector3D getVertex(int i);

  public abstract BoundingVolume getBoundingVolume();

  public abstract boolean isTransparent(G3MRenderContext rc);

  public abstract void render(G3MRenderContext rc, GLState parentGLState);

}