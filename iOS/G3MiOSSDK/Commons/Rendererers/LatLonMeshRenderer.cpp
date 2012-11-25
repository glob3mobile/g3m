//
//  LatLonMeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


#include "LatLonMeshRenderer.hpp"
#include "GL.hpp"

#include "FloatBufferBuilderFromGeodetic.hpp"
#include "IntBufferBuilder.hpp"
#include "IIntBuffer.hpp"
#include "IndexedMesh.hpp"



LatLonMeshRenderer::~LatLonMeshRenderer()
{
  delete _mesh;
}


void LatLonMeshRenderer::initialize(const G3MContext* context)
{
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy::firstVertex(),
                                          context->getPlanet(),
                                          Geodetic2D::zero());
  vertices.add(Geodetic3D::fromDegrees(28.753213, -17.898788, 500) );
  vertices.add(Geodetic3D::fromDegrees(28.680347, -17.898788, 500) );
  vertices.add(Geodetic3D::fromDegrees(28.753213, -17.83287,  500) );
  vertices.add(Geodetic3D::fromDegrees(28.680347, -17.83287,  500) );
  
  IntBufferBuilder index;
  for (int i = 0; i < 4; i++) {
    index.add(i);
  }

  Color *flatColor = new Color(Color::fromRGBA((float)1.0, (float)1.0, (float)0.0, (float)1.0));
  
  _mesh = new IndexedMesh(GLPrimitive::triangleStrip(),
                          true,
                          vertices.getCenter(),
                          vertices.create(),
                          index.create(),
                          1,
                          flatColor);

}  


void LatLonMeshRenderer::render(const G3MRenderContext* rc) {  
  _mesh->render(rc);
}
