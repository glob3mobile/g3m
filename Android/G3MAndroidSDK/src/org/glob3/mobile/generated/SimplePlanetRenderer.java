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




public class SimplePlanetRenderer extends Renderer
{


  private final IImage _textureImage;
  private int _textureID;

  private int _numIndex;

  private byte[] _index;
  private float[] _vertices;
  private float[] _texCoors;

  private final int _latRes;
  private final int _lonRes;


  private void createVertices(Planet planet)
  {
	//VERTICES
	_vertices = new float[_latRes *_lonRes * 3];
  
	double lonRes1 = (double)(_lonRes-1);
	double latRes1 = (double)(_latRes-1);
	int p = 0;
	for(double i = 0.0; i < _lonRes; i++)
	{
	  Angle lon = Angle.fromDegrees((i * 360 / lonRes1) -180);
	  for (double j = 0.0; j < _latRes; j++)
	  {
		Angle lat = Angle.fromDegrees((j * 180.0 / latRes1) -90.0);
		Geodetic2D g = new Geodetic2D(lat, lon);
  
		Vector3D v = planet.toVector3D(g);
		_vertices[p++] = (float) v.x(); //Vertices
		_vertices[p++] = (float) v.y();
		_vertices[p++] = (float) v.z();
	  }
	}
  }
  private void createMeshIndex()
  {
	int res = _lonRes;
  
	_numIndex = 2 * (res - 1) * (res + 1);
	_index = new byte[_numIndex];
  
	int n = 0;
	for (int j = 0; j < res - 1; j++)
	{
	  if (j > 0)
		  _index[n++] = (byte)(j * res);
	  for (int i = 0; i < res; i++)
	  {
		_index[n++] = (byte)(j * res + i);
		_index[n++] = (byte)(j * res + i + res);
	  }
	  _index[n++] = (byte)(j * res + 2 * res - 1);
	}
  }
  private void createTextureCoordinates()
  {
	_texCoors = new float[_latRes *_lonRes * 2];
  
	double lonRes1 = (double)(_lonRes-1);
	double latRes1 = (double)(_latRes-1);
	int p = 0;
	for(double i = 0.0; i < _lonRes; i++)
	{
	  double u = (i / lonRes1);
	  for (double j = 0.0; j < _latRes; j++)
	  {
		double v = 1.0 - (j / latRes1);
		_texCoors[p++] = (float) u;
		_texCoors[p++] = (float) v;
	  }
	}
  }

  public SimplePlanetRenderer(IImage image)
  {
	  _latRes = 16;
	  _lonRes = 16;
	  _textureImage = image;
	  _textureID = -1;
	_index = null;
	_vertices = null;
  }
  public void dispose()
  {
	_index = null;
	_vertices = null;
  }

  public final void initialize(InitializationContext ic)
  {
	if (ic == null)
		return;
	final Planet planet = ic.getPlanet();
  
	createVertices(planet);
  
	createMeshIndex();
  
	createTextureCoordinates();
  
  }

  public final int render(RenderContext rc)
  {
  
	// obtaing gl object reference
	IGL gl = rc.getGL();
  
	if (_textureImage != null && _textureID < 1)
	{
	  _textureID = gl.uploadTexture(_textureImage, 2048, 1024);
	}
  
	// insert pointers
	gl.enableTextures();
	gl.enableTexture2D();
  
	gl.bindTexture(_textureID);
	gl.vertexPointer(3, 0, _vertices);
	gl.setTextureCoordinates(2, 0, _texCoors);
  
	// draw a red sphere
	gl.color((float) 1, (float) 0, (float) 0, 1);
	gl.drawTriangleStrip(_numIndex, _index);
  
	gl.disableTexture2D();
	gl.disableTextures();
  
	return 0;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	  return false;
  }

  public final boolean onResizeViewportEvent(int width, int height)
  {
	  return false;
  }


}