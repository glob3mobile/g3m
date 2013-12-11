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

public abstract class Mesh
{
  private boolean _enable;
  public Mesh()
  {
     _enable = true;
  }

  public final void setEnable(boolean enable)
  {
    _enable = enable;
  }

  public final boolean isEnable()
  {
    return _enable;
  }

  public void dispose()
  {
  }

  public abstract int getVertexCount();

  public abstract Vector3D getVertex(int i);

  public abstract BoundingVolume getBoundingVolume();

  public abstract boolean isTransparent(G3MRenderContext rc);

  public abstract void rawRender(G3MRenderContext rc, GLState parentGLState);

  public final void render(G3MRenderContext rc, GLState parentGLState)
  {
    if (_enable)
    {
      rawRender(rc, parentGLState);
    }
  }

  public abstract void showNormals(boolean v);

}