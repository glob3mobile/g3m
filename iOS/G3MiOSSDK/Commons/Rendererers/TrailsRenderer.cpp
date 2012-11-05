//
//  TrailsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#include "TrailsRenderer.hpp"

#include "Mesh.hpp"
#include "IndexedMesh.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "IntBufferBuilder.hpp"
#include "Planet.hpp"
#include "GLConstants.hpp"

Trail::~Trail() {
  delete _mesh;
}

Mesh* Trail::createMesh(const Planet* planet) {
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          planet,
                                          Geodetic3D::fromDegrees(0, 0, 0));
  IntBufferBuilder indices;
  
  for (int i = 0; i < _positions.size(); i++) {
#ifdef C_CODE
    vertices.add( *(_positions[i]) );
#endif
#ifdef JAVA_CODE
	  vertices.add( _positions.get(i) );
#endif
    
    indices.add(i);
  }
  
  return new IndexedMesh(GLPrimitive::lineStrip(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         _lineWidth,
                         new Color(_color));
}

Mesh* Trail::getMesh(const Planet* planet) {
  if (_positionsDirty || (_mesh == NULL)) {
    delete _mesh;
    
    _mesh = createMesh(planet);
  }
  return _mesh;
}

void Trail::render(const RenderContext* rc) {
  if (_visible) {
    Mesh* mesh = getMesh(rc->getPlanet());
    if (mesh != NULL) {
      mesh->render(rc);
    }
  }
}

TrailsRenderer::~TrailsRenderer() {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    delete trail;
  }
  _trails.clear();
}

void TrailsRenderer::addTrail(Trail* trail) {
  _trails.push_back(trail);
}

void TrailsRenderer::render(const RenderContext* rc) {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    trail->render(rc);
  }
}
