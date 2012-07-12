package org.glob3.mobile.client.generated; 
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

  private final String _textureFilename;
  private int _textureId;

  private int _numIndexes;

  private byte[] _indexes;
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
  
	_numIndexes = 2 * (res - 1) * (res + 1);
	_indexes = new byte[_numIndexes];
  
	int n = 0;
	for (int j = 0; j < res - 1; j++)
	{
	  if (j > 0)
		  _indexes[n++] = (byte)(j * res);
	  for (int i = 0; i < res; i++)
	  {
		_indexes[n++] = (byte)(j * res + i);
		_indexes[n++] = (byte)(j * res + i + res);
	  }
	  _indexes[n++] = (byte)(j * res + 2 * res - 1);
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

  public SimplePlanetRenderer(String textureFilename)
  {
	  _latRes = 16;
	  _lonRes = 16;
	  _textureFilename = textureFilename;
	  _textureId = -1;
	_indexes = null;
	_vertices = null;
  }
  public void dispose()
  {
	_indexes = null;
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
  
	if (_textureId < 1)
	{
	  _textureId = rc.getTexturesHandler().getTextureIdFromFileName(rc, _textureFilename, 2048, 1024);
	}
  
	if (_textureId < 1)
	{
	  rc.getLogger().logError("Can't load file %s", _textureFilename);
	  return DefineConstants.MAX_TIME_TO_RENDER;
	}
  
  
	// insert pointers
	gl.enableVertices();
	gl.enableTextures();
	gl.enableTexture2D();
  
	gl.bindTexture(_textureId);
	gl.vertexPointer(3, 0, _vertices);
	gl.setTextureCoordinates(2, 0, _texCoors);
  
	// draw a red sphere
	gl.color((float) 1, (float) 0, (float) 0, 1);
	gl.drawTriangleStrip(_numIndexes, _indexes);
  
	gl.disableTexture2D();
	gl.disableTextures();
	gl.disableVertices();
  
	return DefineConstants.MAX_TIME_TO_RENDER;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(int width, int height)
  {

  }


}