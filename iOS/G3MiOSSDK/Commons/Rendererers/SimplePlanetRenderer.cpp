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
#include "IFactory.hpp"
#include "TextureBuilder.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "IntBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "FloatBufferBuilderFromColor.hpp"

#include "IGLTextureId.hpp"

SimplePlanetRenderer::SimplePlanetRenderer(const std::string textureFilename):
_latRes(30),//FOR NOW THEY MUST BE EQUAL
_lonRes(30),
_textureFilename(textureFilename),
_mesh(NULL),
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

IFloatBuffer* SimplePlanetRenderer::createVertices(const Planet& planet) const
{
  //Vertices with Center in zero
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(), &planet, Vector3D::zero());
  const double lonRes1 = (double) (_lonRes-1);
  const double latRes1 = (double) (_latRes-1);
  for(double i = 0.0; i < _lonRes; i++){
    const Angle lon = Angle::fromDegrees( (i * 360 / lonRes1) -180);
    for (double j = 0.0; j < _latRes; j++) {
      const Angle lat = Angle::fromDegrees( (j * 180.0 / latRes1)  -90.0 );
      const Geodetic2D g(lat, lon);
      
      vertices.add(g);
    }
  }
  
  return vertices.create();
}

IIntBuffer* SimplePlanetRenderer::createMeshIndex() const
{
  IntBufferBuilder indices;
  
  const int res = _lonRes;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0){
      indices.add((int) (j * res));
    }
    for (int i = 0; i < res; i++) {
      indices.add(j * res + i);
      indices.add(j * res + i + res);
    }
    indices.add(j * res + 2 * res - 1);
  }
  
  return indices.create();
}

IFloatBuffer* SimplePlanetRenderer::createTextureCoordinates() const
{
  FloatBufferBuilderFromCartesian2D texCoords;
  const double lonRes1 = (double) (_lonRes-1);
  const double latRes1 = (double) (_latRes-1);
  //int p = 0;
  for(double i = 0.0; i < _lonRes; i++){
    double u = (i / lonRes1);
    for (double j = 0.0; j < _latRes; j++) {
      const double v = 1.0 - (j / latRes1);
      texCoords.add((float)u, (float)v);
    }
  }
  
  return texCoords.create();
}

bool SimplePlanetRenderer::initializeMesh(const RenderContext* rc) {
  
  
  const Planet* planet = rc->getPlanet();
  IIntBuffer* ind = createMeshIndex();
  IFloatBuffer* ver = createVertices(*planet);
  IFloatBuffer* texC = NULL;
  FloatBufferBuilderFromColor colors;
  
  const bool colorPerVertex = false;
  

  
  //COLORS PER VERTEX
  IFloatBuffer* vertexColors = NULL;
  if (colorPerVertex){
    int numVertices = _lonRes * _lonRes * 4;
    for(int i = 0; i < numVertices; ){
      
      float val = (float) (0.5 + GMath.sin( (float) (2.0 * GMath.pi() * ((float) i) / numVertices) ) / 2.0);
      
      colors.add(val, (float)0.0, (float)(1.0 - val), (float)1.0);
    }
    vertexColors = colors.create();
  }
  
  //FLAT COLOR
  Color * flatColor = NULL;
  if (false){
    flatColor = new Color( Color::fromRGBA(0.0, 1.0, 0.0, 1.0) );
  }
  
  IndexedMesh *im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                    true,
                                    Vector3D::zero(),
                                    ver,
                                    ind,
                                    1,
                                    flatColor,
                                    vertexColors);
  
  //TEXTURED
  if (true){
    
    IImage* image = rc->getFactory()->createImageFromFileName(_textureFilename);
    
    const IImage* scaledImage = rc->getTextureBuilder()->createTextureFromImage(rc->getGL(), 
                                                                                rc->getFactory(), 
                                                                                image, _texWidth,
                                                                                _texHeight);
    if (image != scaledImage){
      rc->getFactory()->deleteImage(image);
    }
    
    const IGLTextureId* texId = rc->getTexturesHandler()->getGLTextureId(scaledImage, GLFormat::rgba(),
                                                                         _textureFilename, false);
    
    rc->getFactory()->deleteImage(scaledImage);
    
    if (texId == NULL) {
      rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
      return false;
    }
    texC = createTextureCoordinates();
    
    TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                      texC,
                                                      true);
    
    _mesh = new TexturedMesh(im, true, texMap, true, false);
  }
  

  
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
