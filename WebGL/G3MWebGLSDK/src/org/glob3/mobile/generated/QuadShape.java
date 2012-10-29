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
//class Mesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IImage;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;

public class QuadShape extends Shape
{
  private Mesh createMesh(RenderContext rc)
  {
  
	final float halfWidth = (float) _width / 2.0f;
	final float halfHeight = (float) _height / 2.0f;
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	vertices.add(-halfWidth, +halfHeight, 0);
	vertices.add(-halfWidth, -halfHeight, 0);
	vertices.add(+halfWidth, +halfHeight, 0);
	vertices.add(+halfWidth, -halfHeight, 0);
  
	IntBufferBuilder indices = new IntBufferBuilder();
	indices.add(0);
	indices.add(1);
	indices.add(2);
	indices.add(3);
  
	IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), true, Vector3D.zero(), vertices.create(), indices.create(), 2, new Color(Color.white()));
  
	final IGLTextureId texId = getTextureId(rc);
	if (texId == null)
	{
	  return im;
	}
  
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
	texCoords.add(0, 0);
	texCoords.add(0, 1);
	texCoords.add(1, 0);
	texCoords.add(1, 1);
  
	TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true);
  
	return new TexturedMesh(im, true, texMap, true);
  }
  private Mesh getMesh(RenderContext rc)
  {
	if (_mesh == null)
	{
	  _mesh = createMesh(rc);
	}
	return _mesh;
  }

  private Mesh _mesh;

  private final String _textureFilename;
  private IImage _textureImage;
  private final boolean _autoDeleteTextureImage;

  private final int _width;
  private final int _height;

  private IGLTextureId getTextureId(RenderContext rc)
  {
	if (_textureImage == null)
	{
	  return null;
	}
  
	IGLTextureId texId = null;
	texId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _textureFilename, false);
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

  public QuadShape(Geodetic3D position, IImage textureImage, boolean autoDeleteTextureImage, String textureFilename, int width, int height)
  {
	  super(position);
	  _mesh = null;
	  _textureFilename = textureFilename;
	  _textureImage = textureImage;
	  _autoDeleteTextureImage = autoDeleteTextureImage;
	  _width = width;
	  _height = height;

  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

  public final void render(RenderContext rc)
  {
	int __diego_at_work;
	Mesh mesh = getMesh(rc);
	if (mesh != null)
	{
	  GL gl = rc.getGL();
  
	  gl.pushMatrix();
	  gl.loadMatrixf(MutableMatrix44D.identity());
  
	  final Vector3D cartesianPosition = rc.getPlanet().toCartesian(_position);
	  MutableMatrix44D translationMatrix = MutableMatrix44D.createTranslationMatrix(cartesianPosition);
  
	  gl.multMatrixf(translationMatrix);
  
	  mesh.render(rc);
  
	  gl.popMatrix();
	}
  }

}