//
//  SimplePlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "SimplePlanetRenderer.hpp"

SimplePlanetRenderer::SimplePlanetRenderer(const IImage* image):
_latRes(16),//FOR NOW THEY MUST BE EQUAL
_lonRes(16),
_textureImage(image),
_textureID(-1)
{
  _index = NULL;
  _vertices = NULL;
}

SimplePlanetRenderer::~SimplePlanetRenderer()
{
  delete[] _index;
  delete[] _vertices;
}

void SimplePlanetRenderer::initialize(const InitializationContext* ic)
{
  if (ic == NULL) return;
  const Planet *planet = ic->getPlanet();
  
  createVertices(*planet);
  
  createMeshIndex();
  
  createTextureCoordinates();
  
}

void SimplePlanetRenderer::createVertices(const Planet& planet)
{
  //VERTICES
  _vertices = new float[_latRes *_lonRes * 3];
  
  double lonRes1 = (double) (_lonRes-1), latRes1 = (double) (_latRes-1);
  int p = 0;
  for(double i = 0.0; i < _lonRes; i++){
    Angle lon = Angle::fromDegrees( (i * 360 / lonRes1) -180);
    for (double j = 0.0; j < _latRes; j++) {
      Angle lat = Angle::fromDegrees( (j * 180.0 / latRes1)  -90.0 );
      Geodetic2D g(lat, lon);
      
      Vector3D v = planet.toVector3D(g);
      _vertices[p++] = (float) v.x();//Vertices
      _vertices[p++] = (float) v.y();
      _vertices[p++] = (float) v.z();
    }
  }
}



void SimplePlanetRenderer::createMeshIndex()
{
  int res = _lonRes;
  
  _numIndex = 2 * (res - 1) * (res + 1);
  _index = new unsigned char[_numIndex];
  
  int n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) _index[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      _index[n++] = (char) (j * res + i);
      _index[n++] = (char) (j * res + i + res);
    }
    _index[n++] = (char) (j * res + 2 * res - 1);
  }
}

void SimplePlanetRenderer::createTextureCoordinates()
{
  _texCoors = new float[_latRes *_lonRes * 2];
  
  double lonRes1 = (double) (_lonRes-1), latRes1 = (double) (_latRes-1);
  int p = 0;
  for(double i = 0.0; i < _lonRes; i++){
    double u = (i / lonRes1);
    for (double j = 0.0; j < _latRes; j++) {
      double v = 1.0 - (j / latRes1);
      _texCoors[p++] = (float) u;
      _texCoors[p++] = (float) v;
    }
  }
}


int SimplePlanetRenderer::render(const RenderContext* rc){
  
  // obtaing gl object reference
  IGL *gl = rc->getGL();
  
  if (_textureImage != NULL && _textureID < 1)
  {
    _textureID = gl->uploadTexture(*_textureImage, 2048, 1024);
  }
  
  // insert pointers
  gl->enableTexture2D();
  
  gl->bindTexture(_textureID);
  gl->vertexPointer(3, 0, _vertices);
  gl->setTextureCoordinates(2, 0, _texCoors); 
  
  // draw a red sphere
  gl->color((float) 1, (float) 0, (float) 0);
  gl->drawTriangleStrip(_numIndex, _index);
  
  return 0;
}
