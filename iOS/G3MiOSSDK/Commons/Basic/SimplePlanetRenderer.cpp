//
//  SimplePlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "SimplePlanetRenderer.hpp"

SimplePlanetRenderer::SimplePlanetRenderer(const std::string textureFilename):
_latRes(16),//FOR NOW THEY MUST BE EQUAL
_lonRes(16),
_textureFilename(textureFilename),
_mesh(NULL)
{
}

SimplePlanetRenderer::~SimplePlanetRenderer()
{
  delete _mesh;
}

void SimplePlanetRenderer::initialize(const InitializationContext* ic)
{
}

float * SimplePlanetRenderer::createVertices(const Planet& planet)
{
  //VERTICES
  float* _vertices = new float[_latRes *_lonRes * 3];
  
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
  
  return _vertices;
}



unsigned char* SimplePlanetRenderer::createMeshIndex()
{
  int res = _lonRes;
  
  int _numIndexes = 2 * (res - 1) * (res + 1);
  unsigned char *_indexes = new unsigned char[_numIndexes];
  
  int n = 0;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) _indexes[n++] = (char) (j * res);
    for (int i = 0; i < res; i++) {
      _indexes[n++] = (char) (j * res + i);
      _indexes[n++] = (char) (j * res + i + res);
    }
    _indexes[n++] = (char) (j * res + 2 * res - 1);
  }
  
  return _indexes;
}

float* SimplePlanetRenderer::createTextureCoordinates()
{
  float* _texCoords = new float[_latRes *_lonRes * 2];
  
  double lonRes1 = (double) (_lonRes-1), latRes1 = (double) (_latRes-1);
  int p = 0;
  for(double i = 0.0; i < _lonRes; i++){
    double u = (i / lonRes1);
    for (double j = 0.0; j < _latRes; j++) {
      double v = 1.0 - (j / latRes1);
      _texCoords[p++] = (float) u;
      _texCoords[p++] = (float) v;
    }
  }
  
  return _texCoords;
}


int SimplePlanetRenderer::render(const RenderContext* rc){
  
  //GENERATING MESH
  if (_mesh == NULL){
  
    int texID = rc->getTexturesHandler()->getTextureIdFromFileName(rc, _textureFilename, 2048, 1024);
    
    if (texID < 1) {
      rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      return MAX_TIME_TO_RENDER;
    }
    
    const Planet *planet = rc->getPlanet();
    
    float * ver = createVertices(*planet);
    
    int res = _lonRes;
    int numIndexes = 2 * (res - 1) * (res + 1);
    unsigned char * ind = createMeshIndex();
    
    float * texC = createTextureCoordinates();
    
    _mesh = new IndexedTriangleStripMesh(true, ver, ind, numIndexes, texID, texC);
    
  }
  
  _mesh->render(rc);

  return MAX_TIME_TO_RENDER;
}
