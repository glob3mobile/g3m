//
//  GEOMeshSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/13.
//
//

#include "GEOMeshSymbol.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "ShortBufferBuilder.hpp"
#include "DirectMesh.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "Color.hpp"

#include "MeshRenderer.hpp"

Mesh* GEOMeshSymbol::createLine2DMesh(const std::vector<Geodetic2D*>* coordinates,
                                      const Color& lineColor,
                                      float lineWidth,
                                      double deltaHeight,
                                      const Planet* planet) const {

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);

  const int coordinatesCount = coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    const Geodetic2D* coordinate = coordinates->at(i);
    vertices->add(coordinate->_latitude,
                  coordinate->_longitude,
                  deltaHeight);
  }

  Mesh* result = new DirectMesh(GLPrimitive::lineStrip(),
                                true,
                                vertices->getCenter(),
                                vertices->create(),
                                lineWidth,
                                1,
                                new Color(lineColor),
                                NULL,
                                0.0f,
                                false);

  delete vertices;

  return result;
}

Mesh* GEOMeshSymbol::createLines2DMesh(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                       const Color& lineColor,
                                       float lineWidth,
                                       double deltaHeight,
                                       const Planet* planet) const {

  FloatBufferBuilderFromGeodetic* vertices = FloatBufferBuilderFromGeodetic::builderWithFirstVertexAsCenter(planet);
  ShortBufferBuilder indices;

  const int coordinatesArrayCount = coordinatesArray->size();
  short index = 0;
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
    const int coordinatesCount = coordinates->size();
    for (int j = 0; j < coordinatesCount; j++) {
      const Geodetic2D* coordinate = coordinates->at(j);

      vertices->add(coordinate->_latitude,
                    coordinate->_longitude,
                    deltaHeight);

      indices.add(index);
      if ((j > 0) && (j < (coordinatesCount-1))) {
        indices.add(index);
      }
      index++;
    }
  }

  Mesh* result = new IndexedMesh(GLPrimitive::lines(),
                                 true,
                                 vertices->getCenter(),
                                 vertices->create(),
                                 indices.create(),
                                 lineWidth,
                                 1,
                                 new Color(lineColor),
                                 NULL,
                                 0.0f,
                                 false);

  delete vertices;

  return result;
}

bool GEOMeshSymbol::symbolize(const G3MRenderContext* rc,
                              const GEOSymbolizer*    symbolizer,
                              MeshRenderer*           meshRenderer,
                              ShapesRenderer*         shapesRenderer,
                              MarksRenderer*          marksRenderer,
                              GEOTileRasterizer*      geoTileRasterizer) const {
  if (meshRenderer == NULL) {
    ILogger::instance()->logError("Can't simbolize with Mesh, MeshRenderer was not set");
  }
  else {
    Mesh* mesh = createMesh(rc);
    if (mesh != NULL) {
      meshRenderer->addMesh(mesh);
    }
  }
  return true;
}
