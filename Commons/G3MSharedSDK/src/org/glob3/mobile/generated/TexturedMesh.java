package org.glob3.mobile.generated;import java.util.*;

//
//  TexturedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//

//
//  TexturedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//





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

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: BoundingVolume* getBoundingVolume() const
  public final BoundingVolume getBoundingVolume()
  {
	return (_mesh == null) ? null : _mesh.getBoundingVolume();
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void rawRender(const G3MRenderContext* rc, const GLState* parentState) const
  public final void rawRender(G3MRenderContext rc, GLState parentState)
  {
	tangible.RefObject<GLState> tempRef__glState = new tangible.RefObject<GLState>(_glState);
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning To Diego: As a textureMapping could be used by more than 1 TexturedMesh (I think) it's necessary to check glState consistency at every frame.Otherwise you should store somehow a list of every user of the mapping in order to change their states when any parameter of the mapping is updated.Method modifyGLState() is now much lighter though.
	_textureMapping.modifyGLState(tempRef__glState);
	_glState = tempRef__glState.argvalue;
  
	_glState.setParent(parentState);
	_mesh.render(rc, _glState);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void showNormals(boolean v) const
  public final void showNormals(boolean v)
  {
	_mesh.showNormals(v);
  }
}
