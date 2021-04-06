package org.glob3.mobile.generated;
//
//  Mesh.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 27/06/12.
//

//
//  Mesh.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 27/06/12.
//



//class Vector3D;
//class BoundingVolume;
//class G3MRenderContext;
//class GLState;
//class MutableVector3D;
//class MutableColor;
//class Color;


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

  private String _token;

  protected Mesh()
  {
     _enable = true;
     _userData = null;
     _token = "";
  }

  public final void setToken(String token)
  {
    _token = token;
  }

  public final String getToken()
  {
    return _token;
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

  public abstract int getVerticesCount();

  public abstract Vector3D getVertex(int index);

  public abstract void getVertex(int index, MutableVector3D result);

  public abstract Color getColor(int index);

  public abstract void getColor(int index, MutableColor result);

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

}