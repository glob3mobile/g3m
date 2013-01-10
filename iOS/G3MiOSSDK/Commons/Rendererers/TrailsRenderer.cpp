//
//  TrailsRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#include "TrailsRenderer.hpp"

#include "Mesh.hpp"
#include "DirectMesh.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "Planet.hpp"
#include "GLConstants.hpp"

Trail::~Trail() {
  delete _mesh;
}

Mesh* Trail::createMesh(const Planet* planet) {
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          planet,
                                          Geodetic3D::fromDegrees(0, 0, 0));

  for (int i = 0; i < _positions.size(); i++) {
#ifdef C_CODE
    vertices.add( *(_positions[i]) );
#endif
#ifdef JAVA_CODE
	  vertices.add( _positions.get(i) );
#endif
  }

  return new DirectMesh(GLPrimitive::lineStrip(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        _lineWidth,
                        1,
                        new Color(_color));
}

Mesh* Trail::getMesh(const Planet* planet) {
  if (_positionsDirty || (_mesh == NULL)) {
    delete _mesh;

    _mesh = createMesh(planet);
  }
  return _mesh;
}

void Trail::render(const G3MRenderContext* rc,
                   const GLState& parentState) {
  if (_visible) {
    Mesh* mesh = getMesh(rc->getPlanet());
    if (mesh != NULL) {
      mesh->render(rc, parentState);
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

void TrailsRenderer::render(const G3MRenderContext* rc,
                            const GLState& parentState) {
  const int trailsCount = _trails.size();
  for (int i = 0; i < trailsCount; i++) {
    Trail* trail = _trails[i];
    trail->render(rc, parentState);
  }
}
