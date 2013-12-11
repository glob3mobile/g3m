package org.glob3.mobile.generated; 
//
//  MeshHolder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  MeshHolder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//



///#include "GPUProgramState.hpp"

public class MeshHolder extends Mesh
{
  private Mesh _mesh;

  public MeshHolder(Mesh mesh)
  {
     _mesh = mesh;

  }

  public final void setMesh(Mesh mesh)
  {
    if (_mesh != mesh)
    {
      if (_mesh != null)
         _mesh.dispose();
      _mesh = mesh;
    }
  }

  public void dispose()
  {
    if (_mesh != null)
       _mesh.dispose();
  super.dispose();

  }

  public final int getVertexCount()
  {
    return _mesh.getVertexCount();
  }

  public final Vector3D getVertex(int i)
  {
    return _mesh.getVertex(i);
  }

  public final BoundingVolume getBoundingVolume()
  {
    return _mesh.getBoundingVolume();
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _mesh.isTransparent(rc);
  }

  public final void rawRender(G3MRenderContext rc, GLState parentGLState)
  {
    _mesh.render(rc, parentGLState);
  }

  public final void showNormals(boolean v)
  {
    _mesh.showNormals(v);
  }

}