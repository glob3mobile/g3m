package org.glob3.mobile.generated; 
//
//  GEOSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

//
//  GEOSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//



//class Mesh;
//class G3MRenderContext;
//class Color;
//class Geodetic2D;


public abstract class GEOSymbol
{
  protected final Mesh createLine2DMesh(java.util.ArrayList<Geodetic2D> coordinates, Color lineColor, float lineWidth, double deltaHeight, G3MRenderContext rc)
  {
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), rc.getPlanet(), Geodetic2D.zero());
  
    final int coordinatesCount = coordinates.size();
    for (int i = 0; i < coordinatesCount; i++)
    {
      Geodetic2D coordinate = coordinates.get(i);
      vertices.add(coordinate.latitude(), coordinate.longitude(), deltaHeight);
    }
  
    return new DirectMesh(GLPrimitive.lineStrip(), true, vertices.getCenter(), vertices.create(), lineWidth, 1, new Color(lineColor));
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
  
  
  protected final Mesh createLines2DMesh(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray, Color lineColor, float lineWidth, double deltaHeight, G3MRenderContext rc)
  {
  
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
  
  
    FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), rc.getPlanet(), Geodetic2D.zero());
    ShortBufferBuilder indices = new ShortBufferBuilder();
  
    final int coordinatesArrayCount = coordinatesArray.size();
    short index = 0;
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        final Geodetic2D coordinate = coordinates.get(j);
  
        vertices.add(coordinate.latitude(), coordinate.longitude(), deltaHeight);
  
        indices.add(index);
        if ((j > 0) && (j < (coordinatesCount-1)))
        {
          indices.add(index);
        }
        index++;
      }
    }
  
    return new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), lineWidth, 1, new Color(lineColor));
  
  }


  public void dispose()
  {
  }

  public abstract Mesh createMesh(G3MRenderContext rc);

}