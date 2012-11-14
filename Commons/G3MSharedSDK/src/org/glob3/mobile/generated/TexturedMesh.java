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

  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void render(const RenderContext* rc) const
  public final void render(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	if (_transparent)
	{
	  gl.enableBlend();
	}
  
	gl.enableTextures();
	gl.enableTexture2D();
  
	_textureMapping.bind(rc);
  
	_mesh.render(rc);
  
	gl.disableTexture2D();
	gl.disableTextures();
  
	if (_transparent)
	{
	  gl.disableBlend();
	}
  
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

}