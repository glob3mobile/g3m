package org.glob3.mobile.generated;import java.util.*;

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
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;


public class QuadShape extends AbstractMeshShape
{
  private URL _textureURL = new URL();
  private final float _width;
  private final float _height;
  private final Color _color;

  private boolean _textureRequested;
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final IImage _textureImage;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public IImage _textureImage = new internal();
//#endif

  private TextureIDReference getTextureId(G3MRenderContext rc)
  {
	if (_textureImage == null)
	{
	  return null;
	}
  
	final TextureIDReference texId = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _textureURL._path, false);
  
	if (_textureImage != null)
		_textureImage.dispose();
	_textureImage = null;
  
	if (texId == null)
	{
	  rc.getLogger().logError("Can't load texture %s", _textureURL._path.c_str());
	}
  
	return texId;
  }

  private final boolean _withNormals;

  protected final Mesh createMesh(G3MRenderContext rc)
  {
	if (!_textureRequested)
	{
	  _textureRequested = true;
	  if (_textureURL._path.length() != 0)
	  {
		rc.getDownloader().requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), true, new QuadShape_IImageDownloadListener(this), true);
	  }
	}
  
	final float halfWidth = _width / 2.0f;
	final float halfHeight = _height / 2.0f;
  
	final float left = -halfWidth;
	final float right = +halfWidth;
	final float bottom = -halfHeight;
	final float top = +halfHeight;
  
	FloatBufferBuilderFromCartesian3D vertices = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
	vertices.add(left, bottom, 0);
	vertices.add(right, bottom, 0);
	vertices.add(left, top, 0);
	vertices.add(right, top, 0);
  
	Color color = (_color == null) ? null : new Color(_color);
	Mesh im = null;
	if (_withNormals)
	{
	  FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
	  normals.add(0.0, 0.0, 1.0);
	  normals.add(0.0, 0.0, 1.0);
	  normals.add(0.0, 0.0, 1.0);
	  normals.add(0.0, 0.0, 1.0);
  
	  im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color, null, (float)1.0, true, normals.create());
  
	  if (normals != null)
		  normals.dispose();
	}
	else
	{
	  im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color);
	}
  
	if (vertices != null)
		vertices.dispose();
  
	final TextureIDReference texId = getTextureId(rc);
	if (texId == null)
	{
	  return im;
	}
  
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
	texCoords.add(0, 1);
	texCoords.add(1, 1);
	texCoords.add(0, 0);
	texCoords.add(1, 0);
  
	TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
	return new TexturedMesh(im, true, texMap, true, true);
  }

  public QuadShape(Geodetic3D position, AltitudeMode altitudeMode, URL textureURL, float width, float height, boolean withNormals)
  {
	  super(position, altitudeMode);
	  _textureURL = new URL(textureURL);
	  _width = width;
	  _height = height;
	  _textureRequested = false;
	  _textureImage = null;
	  _color = null;
	  _withNormals = withNormals;

  }

  public QuadShape(Geodetic3D position, AltitudeMode altitudeMode, IImage textureImage, float width, float height, boolean withNormals)
  {
	  super(position, altitudeMode);
	  _textureURL = new URL(new URL("", false));
	  _width = width;
	  _height = height;
	  _textureRequested = true;
	  _textureImage = textureImage;
	  _color = null;
	  _withNormals = withNormals;

  }


  public QuadShape(Geodetic3D position, AltitudeMode altitudeMode, float width, float height, Color color, boolean withNormals)
  {
	  super(position, altitudeMode);
	  _textureURL = new URL(new URL("", false));
	  _width = width;
	  _height = height;
	  _textureRequested = false;
	  _textureImage = null;
	  _color = color;
	  _withNormals = withNormals;

  }
  public void dispose()
  {
	if (_color != null)
		_color.dispose();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	super.dispose();
//#endif
  
  }

  public final void imageDownloaded(IImage image)
  {
	_textureImage = image;
  
	cleanMesh();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: java.util.ArrayList<double> intersectionsDistances(const Planet* planet, const Vector3D& origin, const Vector3D& direction) const
  public final java.util.ArrayList<Double> intersectionsDistances(Planet planet, Vector3D origin, Vector3D direction)
  {
	java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
	return intersections;
  }

}
