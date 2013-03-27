//
//  GEOSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#include "GEOSymbol.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "ShortBufferBuilder.hpp"
#include "DirectMesh.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "Color.hpp"

Mesh* GEOSymbol::createLine2DMesh(const std::vector<Geodetic2D*>* coordinates,
                                  const Color& lineColor,
                                  float lineWidth,
                                  double deltaHeight,
                                  const Ellipsoid* ellipsoid) {
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          ellipsoid,
                                          Geodetic2D::zero());

  const int coordinatesCount = coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    const Geodetic2D* coordinate = coordinates->at(i);
    vertices.add(coordinate->latitude(),
                 coordinate->longitude(),
                 deltaHeight);
  }

  return new DirectMesh(GLPrimitive::lineStrip(),
                        true,
                        vertices.getCenter(),
                        vertices.create(),
                        lineWidth,
                        1,
                        new Color(lineColor));
}

Mesh* GEOSymbol::createLines2DMesh(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                   const Color& lineColor,
                                   float lineWidth,
                                   double deltaHeight,
                                   const Ellipsoid* ellipsoid) {

  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          ellipsoid,
                                          Geodetic2D::zero());
  ShortBufferBuilder indices;

  const int coordinatesArrayCount = coordinatesArray->size();
  short index = 0;
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
    const int coordinatesCount = coordinates->size();
    for (int j = 0; j < coordinatesCount; j++) {
      const Geodetic2D* coordinate = coordinates->at(j);

      vertices.add(coordinate->latitude(),
                   coordinate->longitude(),
                   deltaHeight);

      indices.add(index);
      if ((j > 0) && (j < (coordinatesCount-1))) {
        indices.add(index);
      }
      index++;
    }
  }

  return new IndexedMesh(GLPrimitive::lines(),
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         lineWidth,
                         1,
                         new Color(lineColor));
}
