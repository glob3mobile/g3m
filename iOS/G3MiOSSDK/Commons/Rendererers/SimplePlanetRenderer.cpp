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

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "ShortBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "FloatBufferBuilderFromColor.hpp"
#include "IGLTextureId.hpp"
#include "GLConstants.hpp"

SimplePlanetRenderer::SimplePlanetRenderer(IImage* image):
_image(image),
_latRes(30), _lonRes(30), //FOR NOW THEY MUST BE EQUAL
_mesh(NULL)
{
}

SimplePlanetRenderer::~SimplePlanetRenderer() {
  delete _mesh;
  IFactory::instance()->deleteImage(_image);
}

void SimplePlanetRenderer::initialize(const G3MContext* context) {

}

IFloatBuffer* SimplePlanetRenderer::createVertices(const Planet* planet) const {
  //Vertices with Center in zero
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::givenCenter(), planet, Vector3D::zero());
  const double lonRes1 = (double) (_lonRes-1);
  const double latRes1 = (double) (_latRes-1);
  for(double i = 0.0; i < _lonRes; i++) {
    const Angle lon = Angle::fromDegrees( (i * 360 / lonRes1) -180);
    for (double j = 0.0; j < _latRes; j++) {
      const Angle lat = Angle::fromDegrees( (j * 180.0 / latRes1)  -90.0 );
      const Geodetic2D g(lat, lon);

      vertices.add(g);
    }
  }

  return vertices.create();
}

IShortBuffer* SimplePlanetRenderer::createMeshIndex() const {
  ShortBufferBuilder indices;

  const int res = _lonRes;
  for (int j = 0; j < res - 1; j++) {
    if (j > 0) {
      indices.add((short) (j * res));
    }
    for (int i = 0; i < res; i++) {
      indices.add((short) (j * res + i));
      indices.add((short) (j * res + i + res));
    }
    indices.add((short) (j * res + 2 * res - 1));
  }

  return indices.create();
}

IFloatBuffer* SimplePlanetRenderer::createTextureCoordinates() const {
  FloatBufferBuilderFromCartesian2D texCoords;
  const double lonRes1 = (double) (_lonRes-1);
  const double latRes1 = (double) (_latRes-1);
  //int p = 0;
  for(double i = 0.0; i < _lonRes; i++) {
    const double u = (i / lonRes1);
    for (double j = 0.0; j < _latRes; j++) {
      const double v = 1.0 - (j / latRes1);
      texCoords.add((float)u, (float)v);
    }
  }

  return texCoords.create();
}

Mesh* SimplePlanetRenderer::createMesh(const G3MRenderContext* rc) {
  IShortBuffer* indices = createMeshIndex();
  IFloatBuffer* vertices = createVertices( rc->getPlanet() );

  //COLORS PER VERTEX
  IFloatBuffer* vertexColors = NULL;
  const bool colorPerVertex = false;
  if (colorPerVertex) {
    const IMathUtils* mu = IMathUtils::instance();
    FloatBufferBuilderFromColor colors;

    const int numVertices = _lonRes * _lonRes * 4;
    for (int i = 0; i < numVertices; i++) {
      const float val = (float) (0.5 + mu->sin( (float) (2.0 * PI * ((float) i) / numVertices) ) / 2.0);

      colors.add(val, (float)0.0, (float)(1.0 - val), (float)1.0);
    }
    vertexColors = colors.create();
  }

  //FLAT COLOR
  Color * flatColor = NULL;
  //  if (false) {
  //    flatColor = new Color( Color::fromRGBA(0.0, 1.0, 0.0, 1.0) );
  //  }

  IndexedMesh *indexedMesh = new IndexedMesh(GLPrimitive::triangleStrip(),
                                             true,
                                             Vector3D::zero(),
                                             vertices,
                                             indices,
                                             1,
                                             1,
                                             flatColor,
                                             vertexColors);

  //TEXTURED
  const IGLTextureId* texId = rc->getTexturesHandler()->getGLTextureId(_image,
                                                                       GLFormat::rgba(),
                                                                       "SimplePlanetRenderer-Texture",
                                                                       false);

  if (texId == NULL) {
    rc->getLogger()->logError("Can't load texture to GPU");
    delete indexedMesh;
    return NULL;
  }

  // the image is not needed as it's already uploaded to the GPU
  IFactory::instance()->deleteImage(_image);
  _image = NULL;

  IFloatBuffer* texCoords = createTextureCoordinates();

  TextureMapping* textureMapping = new SimpleTextureMapping(texId,
                                                            texCoords,
                                                            true,
                                                            false);

  return new TexturedMesh(indexedMesh, true, textureMapping, true, false);
}

void SimplePlanetRenderer::render(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  if (_mesh != NULL) {
    _mesh->render(rc, NULL);
  }
}
