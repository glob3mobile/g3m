package org.glob3.mobile.generated;
//
//  Mesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//

//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//



//class Vector3D;
//class BoundingVolume;
//class G3MRenderContext;
//class GLState;


public abstract class Mesh
{
  public static class MeshUserData
  {
    public void dispose()
    {
    }
  }

  private boolean _enable;

  private MeshUserData _userData;

  protected Mesh()
  {
     _enable = true;
     _userData = null;
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
    if (_userData != null)
       _userData.dispose();
  }

  public final Mesh.MeshUserData getUserData()
  {
    return _userData;
  }

  public final void setUserData(MeshUserData userData)
  {
    if (_userData != userData)
    {
      if (_userData != null)
         _userData.dispose();
      _userData = userData;
    }
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
