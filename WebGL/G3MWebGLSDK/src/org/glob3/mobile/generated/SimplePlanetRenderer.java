package org.glob3.mobile.generated; 
//
//  SimplePlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  SimplePlanetRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;

public class SimplePlanetRenderer extends Renderer
{

  private final String _textureFilename;
  private final int _texWidth;
  private final int _texHeight;

  private final int _latRes;
  private final int _lonRes;

  private Mesh _mesh;


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* createVertices(const Planet& planet) const
  private IFloatBuffer createVertices(Planet planet)
  {
	//Vertices with Center in zero
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, Vector3D.zero());
	final double lonRes1 = (double)(_lonRes-1);
	final double latRes1 = (double)(_latRes-1);
	for(double i = 0.0; i < _lonRes; i++)
	{
	  final Angle lon = Angle.fromDegrees((i * 360 / lonRes1) -180);
	  for (double j = 0.0; j < _latRes; j++)
	  {
		final Angle lat = Angle.fromDegrees((j * 180.0 / latRes1) -90.0);
		final Geodetic2D g = new Geodetic2D(lat, lon);
  
		vertices.add(g);
	  }
	}
  
	return vertices.create();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IIntBuffer* createMeshIndex() const
  private IIntBuffer createMeshIndex()
  {
	IntBufferBuilder indices = new IntBufferBuilder();
  
	final int res = _lonRes;
	for (int j = 0; j < res - 1; j++)
	{
	  if (j > 0)
	  {
		indices.add((int)(j * res));
	  }
	  for (int i = 0; i < res; i++)
	  {
		indices.add(j * res + i);
		indices.add(j * res + i + res);
	  }
	  indices.add(j * res + 2 * res - 1);
	}
  
	return indices.create();
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* createTextureCoordinates() const
  private IFloatBuffer createTextureCoordinates()
  {
	FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
	final double lonRes1 = (double)(_lonRes-1);
	final double latRes1 = (double)(_latRes-1);
	//int p = 0;
	for(double i = 0.0; i < _lonRes; i++)
	{
	  double u = (i / lonRes1);
	  for (double j = 0.0; j < _latRes; j++)
	  {
		final double v = 1.0 - (j / latRes1);
		texCoords.add((float)u, (float)v);
	  }
	}
  
	return texCoords.create();
  }

  private boolean initializeMesh(RenderContext rc)
  {
  
  
	final Planet planet = rc.getPlanet();
	IIntBuffer ind = createMeshIndex();
	IFloatBuffer ver = createVertices(planet);
	IFloatBuffer texC = null;
	FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();
  
	final boolean colorPerVertex = false;
  
  
  
	//COLORS PER VERTEX
	IFloatBuffer vertexColors = null;
	if (colorPerVertex)
	{
	  int numVertices = _lonRes * _lonRes * 4;
	  for(int i = 0; i < numVertices;)
	  {
  
		float val = (float)(0.5 + IMathUtils.instance().sin((float)(2.0 * IMathUtils.instance().pi() * ((float) i) / numVertices)) / 2.0);
  
		colors.add(val, (float)0.0, (float)(1.0 - val), (float)1.0);
	  }
	  vertexColors = colors.create();
	}
  
	//FLAT COLOR
	Color flatColor = null;
	if (false)
	{
	  flatColor = new Color(Color.fromRGBA((float) 0.0, (float) 1.0, (float) 0.0, (float) 1.0));
	}
  
	IndexedMesh im = new IndexedMesh(GLPrimitive.triangleStrip(), true, Vector3D.zero(), ver, ind, flatColor, vertexColors);
  
	//TEXTURED
	if (true)
	{
  
	  IImage image = rc.getFactory().createImageFromFileName(_textureFilename);
  
	  final IImage scaledImage = rc.getTextureBuilder().createTextureFromImage(rc.getGL(), rc.getFactory(), image, _texWidth, _texHeight);
	  if (image != scaledImage)
	  {
		rc.getFactory().deleteImage(image);
	  }
  
	  final IGLTextureId texId = rc.getTexturesHandler().getGLTextureId(scaledImage, GLFormat.rgba(), _textureFilename, false);
  
	  rc.getFactory().deleteImage(scaledImage);
  
	  if (texId == null)
	  {
		rc.getLogger().logError("Can't load file %s", _textureFilename);
		return false;
	  }
	  texC = createTextureCoordinates();
  
	  TextureMapping texMap = new SimpleTextureMapping(texId, texC, true);
  
	  _mesh = new TexturedMesh(im, true, texMap, true);
	}
  
  
  
	return true;
  }

  public SimplePlanetRenderer(String textureFilename)
  {
	  _latRes = 30;
	  _lonRes = 30;
	  _textureFilename = textureFilename;
	  _mesh = null;
	  _texWidth = 2048;
	  _texHeight = 1024;
  }
  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

  public final void initialize(InitializationContext ic)
  {
  
  }

  public final void render(RenderContext rc)
  {
	if (_mesh == null)
	{
	  if (!initializeMesh(rc))
	  {
		return;
	  }
	}
  
	_mesh.render(rc);
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }

  public final void start()
  {

  }

  public final void stop()
  {

  }

  public final void onResume(InitializationContext ic)
  {

  }

  public final void onPause(InitializationContext ic)
  {

  }


}