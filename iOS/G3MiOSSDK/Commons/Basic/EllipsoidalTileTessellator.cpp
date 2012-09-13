//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "EllipsoidalTileTessellator.hpp"

#include "Tile.hpp"
#include "Context.hpp"
#include "IndexedMesh.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"

#include "FloatBufferBuilder.hpp"
#include "IntBufferBuilder.hpp"

#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "FloatBufferBuilderFromGeodetic.hpp"
#include "SimpleFloatBufferBuilder.hpp"

Mesh* EllipsoidalTileTessellator::createMesh(const RenderContext* rc,
                                             const Tile* tile) const {
  
  const Sector sector = tile->getSector();
  const Planet* planet = rc->getPlanet();
  
  const int resolution = _resolution;
  const int resolutionMinus1 = resolution - 1;
  
  // create vertices coordinates
#ifdef C_CODE
  FloatBufferBuilderFromGeodetic vertices(GivenCenter, planet, sector.getCenter());
#else
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy.GivenCenter, planet, sector.getCenter());
#endif
  for (int j = 0; j < resolution; j++) {
    for (int i = 0; i < resolution; i++) {
      Geodetic2D innerPoint = sector.getInnerPoint((double) i / resolutionMinus1,
                                                   (double) j / resolutionMinus1);
      
      vertices.add(innerPoint);
    }
  }
  
  // create indices
  IntBufferBuilder indices;
  for (int j = 0; j < resolutionMinus1; j++) {
    if (j > 0) {
      indices.add(j*resolution);
    }
    for (int i = 0; i < resolution; i++) {
      indices.add(j*resolution + i);
      indices.add(j*resolution + i + resolution);
    }
    indices.add(j*resolution + 2*resolution - 1);
  }
  
  // create skirts
  if (_skirted) {
    
    // compute skirt height
    const Vector3D sw = planet->toCartesian(sector.getSW());
    const Vector3D nw = planet->toCartesian(sector.getNW());
    const double skirtHeight = nw.sub(sw).length() * 0.05;
    
    indices.add(0);
    int posS = resolution * resolution;
    
    // west side
    for (int j = 0; j < resolutionMinus1; j++) {
      const Geodetic3D g(sector.getInnerPoint(0, (double)j/resolutionMinus1),
                         -skirtHeight);
      vertices.add(g);
      
      indices.add(j*resolution);
      indices.add(posS++);
    }
    
    // south side
    for (int i = 0; i < resolutionMinus1; i++) {
      const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 1),
                         -skirtHeight);
      vertices.add(g);
      
      indices.add(resolutionMinus1*resolution + i);
      indices.add(posS++);
    }
    
    // east side
    for (int j = resolutionMinus1; j > 0; j--) {
      const Geodetic3D g(sector.getInnerPoint(1, (double)j/resolutionMinus1),
                         -skirtHeight);
      vertices.add(g);
      
      indices.add(j*resolution + resolutionMinus1);
      indices.add(posS++);
    }
    
    // north side
    for (int i = resolutionMinus1; i > 0; i--) {
      const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 0),
                         -skirtHeight);
      vertices.add(g);
      
      indices.add(i);
      indices.add(posS++);
    }
    
    // last triangle
    indices.add(0);
    indices.add(resolution*resolution);
  }
  
  const Color *color = new Color(Color::fromRGBA((float) 0.1, (float) 0.1, (float) 0.1, (float) 1.0));
  
#ifdef C_CODE
  return new IndexedMesh(TriangleStrip,
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         color);
#endif
#ifdef JAVA_CODE
  return new IndexedMesh(GLPrimitive.TriangleStrip,
                         true,
                         vertices.getCenter(),
                         vertices.create(),
                         indices.create(),
                         color);
#endif
}


IFloatBuffer* EllipsoidalTileTessellator::createUnitTextCoords() const {
  
  FloatBufferBuilderFromCartesian2D textCoords;
  
  const int resolution       = _resolution;
  const int resolutionMinus1 = resolution - 1;
  
  float* u = new float[resolution * resolution];
  float* v = new float[resolution * resolution];
  
  for (int j = 0; j < resolution; j++) {
    for (int i = 0; i < resolution; i++) {
      const int pos = j*resolution + i;
      u[pos] = (float) i / resolutionMinus1;
      v[pos] = (float) j / resolutionMinus1;
    }
  }
  
  for (int j = 0; j < resolution; j++) {
    for (int i = 0; i < resolution; i++) {
      const int pos = j*resolution + i;
      textCoords.add( u[pos], v[pos] );
    }
  }
  
  // create skirts
  if (_skirted) {
    // west side
    for (int j = 0; j < resolutionMinus1; j++) {
      const int pos = j*resolution;
      textCoords.add( u[pos], v[pos] );
    }
    
    // south side
    for (int i = 0; i < resolutionMinus1; i++) {
      const int pos = resolutionMinus1 * resolution + i;
      textCoords.add( u[pos], v[pos] );
    }
    
    // east side
    for (int j = resolutionMinus1; j > 0; j--) {
      const int pos = j*resolution + resolutionMinus1;
      textCoords.add( u[pos], v[pos] );
    }
    
    // north side
    for (int i = resolutionMinus1; i > 0; i--) {
      const int pos = i;
      textCoords.add( u[pos], v[pos] );
    }
  }
  
  // free temp memory
  delete[] u;
  delete[] v;
  
  return textCoords.create();
}


Mesh* EllipsoidalTileTessellator::createDebugMesh(const RenderContext* rc,
                                                  const Tile* tile) const
{
  const Sector sector = tile->getSector();
  const Planet* planet = rc->getPlanet();
  
  const int resolutionMinus1 = _resolution - 1;
  int posS = 0;
  
  // compute offset for vertices
  const Vector3D sw = planet->toCartesian(sector.getSW());
  const Vector3D nw = planet->toCartesian(sector.getNW());
  const double offset = nw.sub(sw).length() * 1e-3;
  
  // create vectors
#ifdef C_CODE
  FloatBufferBuilderFromGeodetic vertices(GivenCenter, planet, sector.getCenter());
#else
  FloatBufferBuilderFromGeodetic vertices(CenterStrategy.GivenCenter, planet, sector.getCenter());
#endif
  // create indices
  IntBufferBuilder indices;
  
  // west side
  for (int j = 0; j < resolutionMinus1; j++) {
    const Geodetic3D g(sector.getInnerPoint(0, (double)j/resolutionMinus1), offset);
    
    vertices.add(g);
    indices.add(posS++);
  }
  
  // south side
  for (int i = 0; i < resolutionMinus1; i++) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 1), offset);
    
    vertices.add(g);
    indices.add(posS++);
  }
  
  // east side
  for (int j = resolutionMinus1; j > 0; j--) {
    const Geodetic3D g(sector.getInnerPoint(1, (double)j/resolutionMinus1), offset);
    
    vertices.add(g);
    indices.add(posS++);
  }
  
  // north side
  for (int i = resolutionMinus1; i > 0; i--) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 0), offset);
    
    vertices.add(g);
    indices.add(posS++);
  }
  
  const Color *color = new Color(Color::fromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0));
  const Vector3D center = planet->toCartesian(sector.getCenter());
  
#ifdef C_CODE
  return new IndexedMesh(LineLoop,
                         true,
                         center,
                         vertices.create(),
                         indices.create(),
                         color);
#endif
#ifdef JAVA_CODE
  return new IndexedMesh(GLPrimitive.LineLoop,
                         true,
                         center,
                         vertices.create(),
                         indices.create(),
                         color);
#endif
}
