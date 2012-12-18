//
//  GEO2DMultiLineStringGeometry.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEO2DMultiLineStringGeometry.hpp"

#include "Geodetic2D.hpp"
//#include "CompositeMesh.hpp"
#include "Color.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"


GEO2DMultiLineStringGeometry::~GEO2DMultiLineStringGeometry() {
  const int coordinatesArrayCount = _coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
    const int coordinatesCount = coordinates->size();
    for (int j = 0; j < coordinatesCount; j++) {
      Geodetic2D* coordinate = coordinates->at(j);
      delete coordinate;
    }
    delete coordinates;
  }

  delete _coordinatesArray;
}

Mesh* GEO2DMultiLineStringGeometry::createMesh(const G3MRenderContext* rc) {
//  CompositeMesh* composite = new CompositeMesh();
//  const int coordinatesArrayCount = _coordinatesArray->size();
//  for (int i = 0; i < coordinatesArrayCount; i++) {
//    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
//
//    Color* color = Color::newFromRGBA(1, 1, 0, 1);
//    const float lineWidth = 2;
//
//    composite->addMesh( create2DBoundaryMesh(coordinates, color, lineWidth, rc) );
//  }
//  return composite;

  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          rc->getPlanet(),
                                          Geodetic2D::zero());

  const int coordinatesArrayCount = _coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
    const int coordinatesCount = coordinates->size();
    for (int j = 0; j < coordinatesCount; j++) {
      Geodetic2D* coordinate = coordinates->at(j);
      vertices.add(*coordinate);
      if ((j > 0) && (j < (coordinatesCount-1))) {
        vertices.add(*coordinate);
      }
    }
  }

  Color* color = Color::newFromRGBA(1, 1, 1, 1);
  const float lineWidth = 2;

  return new DirectMesh(GLPrimitive::lines(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        lineWidth,
                        color);
}
