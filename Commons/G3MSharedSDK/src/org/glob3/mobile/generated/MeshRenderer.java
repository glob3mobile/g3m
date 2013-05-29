package org.glob3.mobile.generated; 
//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

//
//  MeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//





//class Mesh;


//C++ TO JAVA CONVERTER TODO TASK: Multiple inheritance is not available in Java:
public class MeshRenderer extends LeafRenderer, GLClient
{
  private java.util.ArrayList<Mesh> _meshes = new java.util.ArrayList<Mesh>();
  private boolean _dirtyGLGlobalStates;

  public MeshRenderer()
  {
     _dirtyGLGlobalStates = false;
  }

  public void dispose()
  {
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      if (mesh != null)
         mesh.dispose();
    }
  }

  public final void addMesh(Mesh mesh)
  {
    _meshes.add(mesh);
    _dirtyGLGlobalStates = true;
  }

  public final void clearMeshes()
  {
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      if (mesh != null)
         mesh.dispose();
    }
    _meshes.clear();
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void initialize(G3MContext context)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void render(G3MRenderContext rc)
  {
    final Frustum frustum = rc.getCurrentCamera().getFrustumInModelCoordinates();
  
    if (_dirtyGLGlobalStates)
    {
      actualizeGLGlobalState(rc.getCurrentCamera());
      _dirtyGLGlobalStates = true;
    }
  
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      final Extent extent = mesh.getExtent();
  
      if (extent.touches(frustum))
      {
        mesh.render(rc);
      }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void start(G3MRenderContext rc)
  {

  }

  public final void stop(G3MRenderContext rc)
  {

  }

  public final void notifyGLClientChildrenParentHasChanged()
  {
    final int meshesCount = _meshes.size();
    for (int i = 0; i < meshesCount; i++)
    {
      Mesh mesh = _meshes.get(i);
      mesh.actualizeGLGlobalState(this);
    }
  }
  public final void modifyGLGlobalState(GLGlobalState GLGlobalState)
  {
    GLGlobalState.enableDepthTest();
  }
  public final void modifyGPUProgramState(GPUProgramState progState)
  {
    progState.setUniformValue("EnableTexture", false);
    progState.setUniformValue("PointSize", (float)1.0);
    progState.setUniformValue("ScaleTexCoord", new Vector2D(1.0,1.0));
    progState.setUniformValue("TranslationTexCoord", new Vector2D(0.0,0.0));
  }

}