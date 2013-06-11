package org.glob3.mobile.generated; 
//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  TexturedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class TexturedMesh extends Mesh
{
  private Mesh _mesh;
  private final TextureMapping _textureMapping;
  private final boolean _ownedMesh;
  private final boolean _ownedTexMapping;
  private final boolean _transparent;

  private GLState _glState = new GLState();

  private void createGLState()
  {
    GLGlobalState globalState = _glState.getGLGlobalState();
    _textureMapping.modifyGLGlobalState(globalState);
    if (_transparent)
    {
      globalState.enableBlend();
      globalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
    }
  
    GPUProgramState progState = _glState.getGPUProgramState();
    _textureMapping.modifyGPUProgramState(progState);
  }



  public TexturedMesh(Mesh mesh, boolean ownedMesh, TextureMapping textureMapping, boolean ownedTexMapping, boolean transparent)
  {
     _mesh = mesh;
     _ownedMesh = ownedMesh;
     _textureMapping = textureMapping;
     _ownedTexMapping = ownedTexMapping;
     _transparent = transparent;
    //addChild((Mesh*)mesh); //New and only child (not const)!!
    createGLState();
  }

  public void dispose()
  {
    if (_ownedMesh)
    {
      if (_mesh != null)
         _mesh.dispose();
    }
    if (_ownedTexMapping)
    {
      if (_textureMapping != null)
         _textureMapping.dispose();
    }
  }

  public final void render(G3MRenderContext rc)
  {
    _mesh.render(rc);
  }

  public final Extent getExtent()
  {
    return (_mesh == null) ? null : _mesh.getExtent();
  }

  public final int getVertexCount()
  {
    return _mesh.getVertexCount();
  }

  public final Vector3D getVertex(int i)
  {
    return _mesh.getVertex(i);
  }

  public final TextureMapping getTextureMapping()
  {
    return _textureMapping;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _transparent;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    _glState.setParent(parentState);
    _mesh.render(rc, _glState);
  }
}