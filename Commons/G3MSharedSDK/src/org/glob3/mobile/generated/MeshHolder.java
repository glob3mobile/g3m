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
  }

  public final int getVertexCount()
  {
    return _mesh.getVertexCount();
  }

  public final Vector3D getVertex(int i)
  {
    return _mesh.getVertex(i);
  }

  public final void render(G3MRenderContext rc)
  {
    _mesh.render(rc);
  }

  public final Extent getExtent()
  {
    return _mesh.getExtent();
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _mesh.isTransparent(rc);
  }

  public final void notifyGLClientChildrenParentHasChanged()
  {
    _mesh.actualizeGLGlobalState(this);
  }
  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
  }
  public final void modifyGPUProgramState(GPUProgramState progState)
  {
  }

  //Scene Graph Node
  public final void rawRender(G3MRenderContext rc, GLStateTreeNode myStateTreeNode)
  {
    //TODO: Implement
    //It's necessary a holder with the Scene Graph approach
  }
  public final boolean isInsideCameraFrustum(G3MRenderContext rc)
  {
    //TODO: Implement
    return true;
  }
  public final void modifiyGLState(GLState state)
  {
    //TODO: Implement
  }

}