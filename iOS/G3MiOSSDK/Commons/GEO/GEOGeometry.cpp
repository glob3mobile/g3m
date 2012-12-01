//
//  GEOGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEOGeometry.hpp"

#include "Mesh.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "Camera.hpp"

GEOGeometry::~GEOGeometry() {
  delete _mesh;
}

Mesh* GEOGeometry::create2DBoundaryMesh(std::vector<Geodetic2D*>* coordinates,
                                        Color* color,
                                        float lineWidth,
                                        const G3MRenderContext* rc) {
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          rc->getPlanet(),
                                          Geodetic2D::zero());

  const int coordinatesCount = coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    Geodetic2D* coordinate = coordinates->at(i);
    vertices.add(*coordinate);
  }

  return new DirectMesh(GLPrimitive::lineStrip(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        lineWidth,
                        color);
}

Mesh* GEOGeometry::getMesh(const G3MRenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

void GEOGeometry::render(const G3MRenderContext* rc) {
  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    const Extent* extent = mesh->getExtent();

    if ( extent->touches( rc->getCurrentCamera()->getFrustumInModelCoordinates() ) ) {
      mesh->render(rc);
    }
  }
}
