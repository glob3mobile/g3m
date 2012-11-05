package org.glob3.mobile.generated; 
//
//  QuadShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  QuadShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;

public class QuadShape extends MeshShape
{
  private final String _textureFilename;
  private IImage _textureImage;
  private final boolean _autoDeleteTextureImage;

  private final float _width;
  private final float _height;

  private IGLTextureId getTextureId(RenderContext rc)
  {
	if (_textureImage == null)
	{
	  return null;
	}
  
	final IGLTextureId texId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _textureFilename, false);
  
	if (_autoDeleteTextureImage)
	{
	  rc.getFactory().deleteImage(_textureImage);
	  _textureImage = null;
	}
  
	if (texId == null)
	{
	  rc.getLogger().logError("Can't load file %s", _textureFilename);
	}
  
	return texId;
  }

  protected final Mesh createMesh(RenderContext rc)
  {
  
	final float halfWidth = (float) _width / 2.0f;
	final float halfHeight = (float) _height / 2.0f;
  
	final float left = -halfWidth;
	final float right = +halfWidth;
	final float bottom = -halfHeight;
	final float top = +halfHeight;
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	vertices.add(left, bottom, 0);
	vertices.add(right, bottom, 0);
	vertices.add(left, top, 0);
	vertices.add(right, top, 0);
  
	IntBufferBuilder indices = new IntBufferBuilder();
	indices.add(0);
	indices.add(1);
	indices.add(2);
	indices.add(3);
  
  
	final Vector3D center = Vector3D.zero();
  
	IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), true, center, vertices.create(), indices.create(), 1);
  
	final IGLTextureId texId = getTextureId(rc);
	if (texId == null)
	{
	  return im;
	}
  
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
	texCoords.add(0, 1);
	texCoords.add(1, 1);
	texCoords.add(0, 0);
	texCoords.add(1, 0);
  
	TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true);
  
	return new TexturedMesh(im, true, texMap, true, true);
  }

  public QuadShape(Geodetic3D position, IImage textureImage, boolean autoDeleteTextureImage, String textureFilename, float width, float height)
  {
	  super(position);
	  _textureFilename = textureFilename;
	  _textureImage = textureImage;
	  _autoDeleteTextureImage = autoDeleteTextureImage;
	  _width = width;
	  _height = height;

  }

  public void dispose()
  {

  }

}