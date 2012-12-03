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
  private final Mesh _mesh;
  private final TextureMapping _textureMapping;
  private final boolean _ownedMesh;
  private final boolean _ownedTexMapping;
  private final boolean _transparent;



  public TexturedMesh(Mesh mesh, boolean ownedMesh, TextureMapping textureMapping, boolean ownedTexMapping, boolean transparent)

  {
	  _mesh = mesh;
	  _ownedMesh = ownedMesh;
	  _textureMapping = textureMapping;
	  _ownedTexMapping = ownedTexMapping;
	  _transparent = transparent;
//    GLState* state = _mesh->getGLState();
//    state->enableTextures();
//    state->enableTexture2D();
//    if (_transparent) {
//      state->enableBlend();
//    }
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const G3MRenderContext* rc, const GLState& parentState) const
  public final void render(G3MRenderContext rc, GLState parentState)
  {
	GL gl = rc.getGL();
  
	//gl->enableTextures();
	//gl->enableTexture2D();
  
	GLState state = new GLState(parentState);
	state.enableTextures();
	state.enableTexture2D();
	if (_transparent)
	{
	  state.enableBlend();
	}
  
	gl.setState(state);
	_textureMapping.bind(rc);
  
	_mesh.render(rc, state);
  
	//gl->disableTexture2D();
	//gl->disableTextures();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* getExtent() const
  public final Extent getExtent()
  {
	return (_mesh == null) ? null : _mesh.getExtent();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
  public final int getVertexCount()
  {
	return _mesh.getVertexCount();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
  public final Vector3D getVertex(int i)
  {
	return _mesh.getVertex(i);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const TextureMapping* const getTextureMapping() const
  public final TextureMapping getTextureMapping()
  {
	return _textureMapping;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const G3MRenderContext* rc) const
  public final boolean isTransparent(G3MRenderContext rc)
  {
	return _transparent;
  }
}