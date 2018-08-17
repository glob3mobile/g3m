package org.glob3.mobile.generated;//
//  EllipsoidShape.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//

//
//  EllipsoidShape.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 02/13/13.
//
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Color;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FloatBufferBuilderFromGeodetic;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FloatBufferBuilderFromCartesian2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FloatBufferBuilderFromCartesian3D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TextureIDReference;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;



public class EllipsoidShape extends AbstractMeshShape
{

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final Ellipsoid _ellipsoid;
//  const Quadric    _quadric;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public Ellipsoid _ellipsoid = new internal();
//  private final Quadric _quadric;
//#endif

  private URL _textureURL = new URL();

  /*const double _radiusX;
  const double _radiusY;
  const double _radiusZ;*/

  private final short _resolution;

  private final float _borderWidth;

  private final boolean _texturedInside;

  private final boolean _mercator;

  private final boolean _withNormals;

  private Color _surfaceColor;
  private Color _borderColor;

  private Mesh createBorderMesh(G3MRenderContext rc, FloatBufferBuilderFromGeodetic vertices)
  {
  
	// create border indices for horizontal lines
	ShortBufferBuilder indices = new ShortBufferBuilder();
	short delta = (short)(2 *_resolution - 1);
	for (short j = 1; j<_resolution-1; j++)
	{
	  for (short i = 0; i<2 *_resolution-2; i++)
	  {
		indices.add((short)(i+j *delta));
		indices.add((short)(i+1+j *delta));
	  }
	}
  
	// create border indices for vertical lines
	for (short i = 0; i<2 *_resolution-2; i++)
	{
	  for (short j = 0; j<_resolution-1; j++)
	  {
		indices.add((short)(i+j *delta));
		indices.add((short)(i+(j+1)*delta));
	  }
	}
  
	Color borderColor;
	if (_borderColor != null)
	{
	  borderColor = new Color(_borderColor);
	}
	else
	{
	  if (_surfaceColor != null)
	  {
		borderColor = new Color(_surfaceColor);
	  }
	  else
	  {
		borderColor = Color.newFromRGBA(1, 0, 0, 1);
	  }
	}
  
	return new IndexedMesh(GLPrimitive.lines(), vertices.getCenter(), vertices.create(), true, indices.create(), true, (_borderWidth < 1) ? 1 : _borderWidth, 1, borderColor);
  }
  private Mesh createSurfaceMesh(G3MRenderContext rc, FloatBufferBuilderFromGeodetic vertices, FloatBufferBuilderFromCartesian2D texCoords, FloatBufferBuilderFromCartesian3D normals)
  {
  
	// create surface indices
	ShortBufferBuilder indices = new ShortBufferBuilder();
	short delta = (short)(2 *_resolution - 1);
  
	// create indices for textupe mapping depending on the flag _texturedInside
	if (!_texturedInside)
	{
	  for (short j = 0; j<_resolution-1; j++)
	  {
		if (j>0)
			indices.add((short)(j *delta));
		for (short i = 0; i<2 *_resolution-1; i++)
		{
		  indices.add((short)(i+j *delta));
		  indices.add((short)(i+(j+1)*delta));
		}
		indices.add((short)((2 *_resolution-2)+(j+1)*delta));
	  }
	}
	else
	{
	  for (short j = 0; j<_resolution-1; j++)
	  {
		if (j>0)
			indices.add((short)((j+1)*delta));
		for (short i = 0; i<2 *_resolution-1; i++)
		{
		  indices.add((short)(i+(j+1)*delta));
		  indices.add((short)(i+j *delta));
		}
		indices.add((short)((2 *_resolution-2)+j *delta));
	  }
	}
  
	// create mesh
	Color surfaceColor = (_surfaceColor == null) ? null : new Color(_surfaceColor);
	Mesh im = new IndexedMesh(GLPrimitive.triangleStrip(), vertices.getCenter(), vertices.create(), true, indices.create(), true, (_borderWidth < 1) ? 1 : _borderWidth, 1, surfaceColor, null, 1, true, _withNormals? normals.create() : null);
  
	final TextureIDReference texId = getTextureId(rc);
	if (texId == null)
	{
	  return im;
	}
  
	TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
	return new TexturedMesh(im, true, texMap, true, true);
  
  }

  private boolean _textureRequested;
  private IImage _textureImage;
  private TextureIDReference getTextureId(G3MRenderContext rc)
  {
  
	if (_texId == null)
	{
	  if (_textureImage == null)
	  {
		return null;
	  }
  
	  _texId = rc.getTexturesHandler().getTextureIDReference(_textureImage, GLFormat.rgba(), _textureURL._path, false);
  
	  if (_textureImage != null)
		  _textureImage.dispose();
	  _textureImage = null;
	}
  
	if (_texId == null)
	{
	  rc.getLogger().logError("Can't load texture %s", _textureURL._path.c_str());
	}
  
	if (_texId == null)
	{
	  return null;
	}
  
	return _texId.createCopy(); //The copy will be handle by the TextureMapping
  }

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
  private final TextureIDReference _texId;
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  private TextureIDReference _texId = new TextureIDReference();
//#endif

  protected final Mesh createMesh(G3MRenderContext rc)
  {
	if (!_textureRequested)
	{
	  _textureRequested = true;
	  if (_textureURL._path.length() != 0)
	  {
		rc.getDownloader().requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), true, new EllipsoidShape_IImageDownloadListener(this), true);
	  }
	}
  
	final EllipsoidalPlanet ellipsoid = new EllipsoidalPlanet(new Ellipsoid(Vector3D.zero, _ellipsoid._radii));
	final Sector sector = new Sector(Sector.fullSphere());
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithGivenCenter(&ellipsoid, Vector3D::zero);
	FloatBufferBuilderFromGeodetic vertices = FloatBufferBuilderFromGeodetic.builderWithGivenCenter(ellipsoid, new Vector3D(Vector3D.zero));
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
  
	FloatBufferBuilderFromCartesian3D normals = FloatBufferBuilderFromCartesian3D.builderWithoutCenter();
  
	final short resolution2Minus2 = (short)(2 *_resolution-2);
	final short resolutionMinus1 = (short)(_resolution-1);
  
	for (int j = 0; j < _resolution; j++)
	{
	  for (int i = 0; i < 2 *_resolution-1; i++)
	  {
		final double u = (double) i / resolution2Minus2;
		final double v = (double) j / resolutionMinus1;
  
		final Geodetic2D innerPoint = sector.getInnerPoint(u, v);
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: vertices->add(innerPoint);
		vertices.add(new Geodetic2D(innerPoint));
  
		if (_withNormals)
		{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Vector3D n = ellipsoid.geodeticSurfaceNormal(innerPoint);
		  Vector3D n = ellipsoid.geodeticSurfaceNormal(new Geodetic2D(innerPoint));
		  normals.add(n);
		}
  
		final double vv = _mercator ? MercatorUtils.getMercatorV(innerPoint._latitude) : v;
  
		texCoords.add((float) u, (float) vv);
	  }
	}
  
  
	Mesh surfaceMesh = createSurfaceMesh(rc, vertices, texCoords, normals);
  
	Mesh resultMesh;
	if (_borderWidth > 0)
	{
	  CompositeMesh compositeMesh = new CompositeMesh();
	  compositeMesh.addMesh(surfaceMesh);
	  compositeMesh.addMesh(createBorderMesh(rc, vertices));
	  resultMesh = compositeMesh;
	}
	else
	{
	  resultMesh = surfaceMesh;
	}
  
	if (vertices != null)
		vertices.dispose();
	if (normals != null)
		normals.dispose();
  
	return resultMesh;
  }

  public EllipsoidShape(Geodetic3D position, AltitudeMode altitudeMode, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, Color surfaceColor, Color borderColor)
  {
	  this(position, altitudeMode, radius, resolution, borderWidth, texturedInside, mercator, surfaceColor, borderColor, true);
  }
  public EllipsoidShape(Geodetic3D position, AltitudeMode altitudeMode, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, Color surfaceColor)
  {
	  this(position, altitudeMode, radius, resolution, borderWidth, texturedInside, mercator, surfaceColor, null, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: EllipsoidShape(Geodetic3D* position, AltitudeMode altitudeMode, const Vector3D& radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, const Color& surfaceColor, Color* borderColor = null, boolean withNormals = true) : AbstractMeshShape(position, altitudeMode), _ellipsoid(new Ellipsoid(Vector3D::zero, radius)), _textureURL(URL("", false)), _resolution(resolution < 3 ? 3 : resolution), _borderWidth(borderWidth), _texturedInside(texturedInside), _mercator(mercator), _surfaceColor(new Color(surfaceColor)), _borderColor(borderColor), _textureRequested(false), _textureImage(null), _withNormals(withNormals), _texId(null)
  public EllipsoidShape(Geodetic3D position, AltitudeMode altitudeMode, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, Color surfaceColor, Color borderColor, boolean withNormals)
//  _quadric(Quadric::fromEllipsoid(_ellipsoid)),
  {
	  super(position, altitudeMode);
	  _ellipsoid = new Ellipsoid(Vector3D.zero, radius);
	  _textureURL = new URL(new URL("", false));
	  _resolution = resolution < 3 ? 3 : resolution;
	  _borderWidth = borderWidth;
	  _texturedInside = texturedInside;
	  _mercator = mercator;
	  _surfaceColor = new Color(surfaceColor);
	  _borderColor = borderColor;
	  _textureRequested = false;
	  _textureImage = null;
	  _withNormals = withNormals;
	  _texId = null;

  }

  public EllipsoidShape(Geodetic3D position, AltitudeMode altitudeMode, Planet planet, URL textureURL, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator)
  {
	  this(position, altitudeMode, planet, textureURL, radius, resolution, borderWidth, texturedInside, mercator, true);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: EllipsoidShape(Geodetic3D* position, AltitudeMode altitudeMode, const Planet* planet, const URL& textureURL, const Vector3D& radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, boolean withNormals = true) : AbstractMeshShape(position, altitudeMode), _ellipsoid(new Ellipsoid(Vector3D::zero, radius)), _textureURL(textureURL), _resolution(resolution < 3 ? 3 : resolution), _borderWidth(borderWidth), _texturedInside(texturedInside), _mercator(mercator), _surfaceColor(null), _borderColor(null), _textureRequested(false), _textureImage(null), _withNormals(withNormals), _texId(null)
  public EllipsoidShape(Geodetic3D position, AltitudeMode altitudeMode, Planet planet, URL textureURL, Vector3D radius, short resolution, float borderWidth, boolean texturedInside, boolean mercator, boolean withNormals)
//  _quadric(Quadric::fromEllipsoid(_ellipsoid)),
  {
	  super(position, altitudeMode);
	  _ellipsoid = new Ellipsoid(Vector3D.zero, radius);
	  _textureURL = new URL(textureURL);
	  _resolution = resolution < 3 ? 3 : resolution;
	  _borderWidth = borderWidth;
	  _texturedInside = texturedInside;
	  _mercator = mercator;
	  _surfaceColor = null;
	  _borderColor = null;
	  _textureRequested = false;
	  _textureImage = null;
	  _withNormals = withNormals;
	  _texId = null;

  }


  public void dispose()
  {
	if (_ellipsoid != null)
		_ellipsoid.dispose();
	if (_surfaceColor != null)
		_surfaceColor.dispose();
	if (_borderColor != null)
		_borderColor.dispose();
  
	if (_texId != null)
		_texId.dispose(); //Releasing texture
  
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
	//  MutableMatrix44D* M = createTransformMatrix(_planet);
	//  const Quadric transformedQuadric = _quadric.transformBy(*M);
	//  delete M;
	//  return transformedQuadric.intersectionsDistances(origin, direction);
	return new java.util.ArrayList<Double>();
  }

}
