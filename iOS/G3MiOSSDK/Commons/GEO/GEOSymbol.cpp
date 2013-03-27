//
//  GEOSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#include "GEOSymbol.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "DirectMesh.hpp"
#include "GLConstants.hpp"
#include "Planet.hpp"
#include "Color.hpp"
#include "IndexedMesh.hpp"
#include "ShortBufferBuilder.hpp"

Mesh* GEOSymbol::createLine2DMesh(const std::vector<Geodetic2D*>* coordinates,
                                  const Color& lineColor,
                                  float lineWidth,
                                  double deltaHeight,
                                  const G3MRenderContext* rc) {
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          rc->getPlanet(),
                                          Geodetic2D::zero());

  const int coordinatesCount = coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    Geodetic2D* coordinate = coordinates->at(i);
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


//Mesh* GEO2DMultiLineStringGeometry::createMesh(const G3MRenderContext* rc,
//                                               const GEOSymbolizer* symbolizer) {
//////  CompositeMesh* composite = new CompositeMesh();
//////  const int coordinatesArrayCount = _coordinatesArray->size();
//////  for (int i = 0; i < coordinatesArrayCount; i++) {
//////    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
//////
//////    Color* color = Color::newFromRGBA(1, 1, 0, 1);
//////    const float lineWidth = 2;
//////
//////    composite->addMesh( create2DBoundaryMesh(coordinates, color, lineWidth, rc) );
//////  }
//////  return composite;
////
////  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
////                                          rc->getPlanet(),
////                                          Geodetic2D::zero());
////
////  const int coordinatesArrayCount = _coordinatesArray->size();
////  for (int i = 0; i < coordinatesArrayCount; i++) {
////    std::vector<Geodetic2D*>* coordinates = _coordinatesArray->at(i);
////    const int coordinatesCount = coordinates->size();
////    for (int j = 0; j < coordinatesCount; j++) {
////      Geodetic2D* coordinate = coordinates->at(j);
////      vertices.add(*coordinate);
////      if ((j > 0) && (j < (coordinatesCount-1))) {
////        vertices.add(*coordinate);
////      }
////    }
////  }
////
////  Color* color = Color::newFromRGBA(1, 1, 1, 1);
////  const float lineWidth = 2;
////
////  return new DirectMesh(GLPrimitive::lines(),
////                        true,
////                        vertices.getCenter(),
////                        vertices.create(),
////                        lineWidth,
////                        1,
////                        color);
//
//  return symbolizer->createMesh(rc, this);
//}


Mesh* GEOSymbol::createLines2DMesh(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                                   const Color& lineColor,
                                   float lineWidth,
                                   double deltaHeight,
                                   const G3MRenderContext* rc) {

//  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
//                                          rc->getPlanet(),
//                                          Geodetic2D::zero());
//
//  const int coordinatesArrayCount = coordinatesArray->size();
//  for (int i = 0; i < coordinatesArrayCount; i++) {
//    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
//    const int coordinatesCount = coordinates->size();
//    for (int j = 0; j < coordinatesCount; j++) {
//      Geodetic2D* coordinate = coordinates->at(j);
//      vertices.add(*coordinate, deltaHeight);
//      if ((j > 0) && (j < (coordinatesCount-1))) {
//        vertices.add(*coordinate, deltaHeight);
//      }
//    }
//  }
//
//  return new DirectMesh(GLPrimitive::lines(),
//                        true,
//                        vertices.getCenter(),
//                        vertices.create(),
//                        lineWidth,
//                        1,
//                        new Color(lineColor));

  
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          rc->getPlanet(),
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
