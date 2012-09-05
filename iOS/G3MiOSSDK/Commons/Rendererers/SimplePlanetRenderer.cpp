//
//  SimplePlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "SimplePlanetRenderer.hpp"

#include "Geodetic2D.hpp"
#include "Planet.hpp"
#include "TexturesHandler.hpp"

SimplePlanetRenderer::SimplePlanetRenderer(const std::string textureFilename):
_latRes(30),//FOR NOW THEY MUST BE EQUAL
_lonRes(30),
_textureFilename(textureFilename),
_mesh(NULL),
//_texWidth(2048 / 2),
_texWidth(2048),
_texHeight(1024)
{
}

SimplePlanetRenderer::~SimplePlanetRenderer()
{
  delete _mesh;
}

void SimplePlanetRenderer::initialize(const InitializationContext* ic)
{
  
}

#ifdef C_CODE
float* SimplePlanetRenderer::createVertices(const Planet& planet)
#else
float[] SimplePlanetRenderer::createVertices(const Planet& planet)
#endif
{
  //VERTICES
  float* vertices = new float[_latRes *_lonRes * 3];
  
  const double lonRes1 = (double) (_lonRes-1);
  const double latRes1 = (double) (_latRes-1);
  int verticesIndex = 0;
  for(double i = 0.0; i < _lonRes; i++){
    const Angle lon = Angle::fromDegrees( (i * 360 / lonRes1) -180);
    for (double j = 0.0; j < _latRes; j++) {
      const Angle lat = Angle::fromDegrees( (j * 180.0 / latRes1)  -90.0 );
      const Geodetic2D g(lat, lon);
      
      const Vector3D v = planet.toCartesian(g);
      vertices[verticesIndex++] = (float) v.x();//Vertices
      vertices[verticesIndex++] = (float) v.y();
      vertices[verticesIndex++] = (float) v.z();
    }
  }
  
  return vertices;
}


#ifdef C_CODE
int* SimplePlanetRenderer::createMeshIndex()
#else
int[] SimplePlanetRenderer::createMeshIndex()
#endif
{
  const unsigned int res = _lonRes;
  
  const int numIndexes = (2 * (res - 1) * (res + 1)) -1;
  int* indexes = new int[numIndexes];
  
  unsigned int n = 0;
  for (unsigned int j = 0; j < res - 1; j++) {
    if (j > 0) indexes[n++] = (int) (j * res);
    for (unsigned int i = 0; i < res; i++) {
      indexes[n++] = (int) (j * res + i);
      indexes[n++] = (int) (j * res + i + res);
    }
    indexes[n++] = (int) (j * res + 2 * res - 1);
  }
  
  return indexes;
}

#ifdef C_CODE
float* SimplePlanetRenderer::createTextureCoordinates()
#else
float[] SimplePlanetRenderer::createTextureCoordinates()
#endif
{
  float* texCoords = new float[_latRes *_lonRes * 2];
  
  const double lonRes1 = (double) (_lonRes-1), latRes1 = (double) (_latRes-1);
  int p = 0;
  for(double i = 0.0; i < _lonRes; i++){
    double u = (i / lonRes1);
    for (double j = 0.0; j < _latRes; j++) {
      const double v = 1.0 - (j / latRes1);
      texCoords[p++] = (float) u;
      texCoords[p++] = (float) v;
    }
  }
  
  return texCoords;
}

bool SimplePlanetRenderer::initializeMesh(const RenderContext* rc) {
  
  
  const Planet* planet = rc->getPlanet();
  
  const int res = _lonRes;
  const int numIndexes = (2 * (res - 1) * (res + 1)) -1;
  
#ifdef C_CODE
  int*   ind = createMeshIndex();
  float* ver = createVertices(*planet);
  float* texC = NULL;
  float* colors = NULL;
  float* normals = NULL;
#endif 
#ifdef JAVA_CODE
  int ind[] = createMeshIndex();
  float ver[] = createVertices(planet);
  float texC[] = null;
  float colors[] = null;
  float normals[] = null;
#endif
  
  //TEXTURED
  GLTextureId texId = GLTextureId::invalid();
  if (true){
    texId = rc->getTexturesHandler()->getGLTextureIdFromFileName(_textureFilename, _texWidth, _texHeight, true);
    if (!texId.isValid()) {
      rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      return false;
    }
    texC = createTextureCoordinates();
  }
  
  //COLORS PER VERTEX
  if (false){
    int numVertices = res * res * 4;
    colors = new float[numVertices];
    for(int i = 0; i < numVertices; ){
    
      float val = (float) (0.5 + GMath.sin( (float) (2.0 * GMath.pi() * ((float) i) / numVertices) ) / 2.0);
      
      colors[i++] = val;
      colors[i++] = 0;
      colors[i++] = (float) (1.0 - val);
      colors[i++] = 1;
    }
  }
  
  //FLAT COLOR
  Color * flatColor = NULL;
  if (false){
    flatColor = new Color( Color::fromRGBA(0.0, 1.0, 0.0, 1.0) );
  }
  
  if (false){
    int numVertices = res * res * 3;
    normals = new float[numVertices];
    for(int i = 0; i < numVertices; ){
      normals[i++] = (float) 1.0;
      normals[i++] = (float) 1.0;
      normals[i++] = (float) 1.0;
    }
  }

#ifdef C_CODE
  IndexedMesh *im = IndexedMesh::createFromVector3D(true, TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                                    _latRes *_lonRes, ver, 
                                                    ind, numIndexes, flatColor, colors, (float)0.5, normals);
#else
  IndexedMesh *im = IndexedMesh::createFromVector3D(true, GLPrimitive.TriangleStrip, NoCenter, Vector3D(0,0,0), 
                                                    _latRes *_lonRes, ver, 
                                                    ind, numIndexes, flatColor, colors, (float)0.5, normals);
#endif
  
  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texC,
                                                    true);
  
  _mesh = new TexturedMesh(im, true, texMap, true);
  
  return true;
}

void SimplePlanetRenderer::render(const RenderContext* rc){
  if (_mesh == NULL){
    if (!initializeMesh(rc)) {
      return;
    }
  }
  
  _mesh->render(rc);
}
