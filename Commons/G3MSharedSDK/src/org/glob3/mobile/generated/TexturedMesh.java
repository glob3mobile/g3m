package org.glob3.mobile.generated;
//
//  TexturedMesh.cpp
//  G3M
//
//  Created by José Miguel S N on 12/07/12.
//

//
//  TexturedMesh.hpp
//  G3M
//
//  Created by José Miguel S N on 12/07/12.
//



//class TextureMapping;


public class TexturedMesh extends Mesh
{
  private Mesh _mesh;
  private final TextureMapping _textureMapping;
  private final boolean _ownedMesh;
  private final boolean _ownedTexMapping;
  private final boolean _transparent;

  private GLState _glState;

  private void createGLState()
  {
  }



  public TexturedMesh(Mesh mesh, boolean ownedMesh, TextureMapping textureMapping, boolean ownedTexMapping, boolean transparent)
  {
     _mesh = mesh;
     _ownedMesh = ownedMesh;
     _textureMapping = textureMapping;
     _ownedTexMapping = ownedTexMapping;
     _transparent = transparent;
     _glState = new GLState();
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
  
    _glState._release();
  
    super.dispose();
  }

  public final BoundingVolume getBoundingVolume()
  {
    return (_mesh == null) ? null : _mesh.getBoundingVolume();
  }

  public final int getVerticesCount()
  {
    return _mesh.getVerticesCount();
  }

  public final Vector3D getVertex(int index)
  {
    return _mesh.getVertex(index);
  }

  public final void getVertex(int index, MutableVector3D result)
  {
    _mesh.getVertex(index, result);
  }

  public final Color getColor(int index)
  {
    return _mesh.getColor(index);
  }

  public final void getColor(int index, MutableColor result)
  {
    _mesh.getColor(index, result);
  }

  public final TextureMapping getTextureMapping()
  {
    return _textureMapping;
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _transparent;
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState)
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning To Diego: As a textureMapping could be used by more than 1 TexturedMesh (I think) it's necessary to check glState consistency at every frame.Otherwise you should store somehow a list of every user of the mapping in order to change their states when any parameter of the mapping is updated.Method modifyGLState() is now much lighter though.
    _textureMapping.modifyGLState(_glState);
  
    _glState.setParent(parentState);
    _mesh.render(rc, _glState);
  }

}