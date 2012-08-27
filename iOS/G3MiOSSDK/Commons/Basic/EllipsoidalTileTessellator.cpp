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


Mesh* EllipsoidalTileTessellator::createMesh(const RenderContext* rc,
                                             const Tile* tile) const {
  
  const Sector sector = tile->getSector();
  const Planet* planet = rc->getPlanet();
  
  /*
   very crude draft of a posible latitude-based dynamic resolution to avoid the
   oversampling in the poles. (dgd)
   */
  //int resolution = _resolution * tile->getSector().getCenter().latitude().sinus();
  //if (resolution < 0) {
  //  resolution *= -1;
  //}
  const int resolution = _resolution;
  const int resolutionMinus1 = resolution - 1;
  
  // create vertices coordinates
  std::vector<MutableVector3D> vertices;
  for (int j = 0; j < resolution; j++) {
    for (int i = 0; i < resolution; i++) {
      addVertex(planet,
                &vertices,
                sector.getInnerPoint((double) i / resolutionMinus1,
                                     (double) j / resolutionMinus1));
    }
  }
  
  // create indices
  std::vector<int> indices;
  for (int j = 0; j < resolutionMinus1; j++) {
    if (j > 0) {
      indices.push_back(j*resolution);
    }
    for (int i = 0; i < resolution; i++) {
      indices.push_back(j*resolution + i);
      indices.push_back(j*resolution + i + resolution);
    }
    indices.push_back(j*resolution + 2*resolutionMinus1);
  }
  
  // create skirts
  if (_skirted) {
    
    // compute skirt height
    const Vector3D sw = planet->toVector3D(sector.getSW());
    const Vector3D nw = planet->toVector3D(sector.getNW());
    const double skirtHeight = nw.sub(sw).length() * 0.05;
    
    indices.push_back(0);
    int posS = resolution * resolution;
    
    // west side
    for (int j = 0; j < resolutionMinus1; j++) {
      const Geodetic3D g(sector.getInnerPoint(0, (double)j/resolutionMinus1),
                         -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(j*resolution);
      indices.push_back(posS++);
    }
    
    // south side
    for (int i = 0; i < resolutionMinus1; i++) {
      const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 1),
                         -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(resolutionMinus1*resolution + i);
      indices.push_back(posS++);
    }
    
    // east side
    for (int j = resolutionMinus1; j > 0; j--) {
      const Geodetic3D g(sector.getInnerPoint(1, (double)j/resolutionMinus1),
                         -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(j*resolution + resolutionMinus1);
      indices.push_back(posS++);
    }
    
    // north side
    for (int i = resolutionMinus1; i > 0; i--) {
      const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 0),
                         -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(i);
      indices.push_back(posS++);
    }
    
    // last triangles
    indices.push_back(0);
    indices.push_back(resolution*resolution);
  }
  
  const Color *color = new Color(Color::fromRGBA((float) 0.1, (float) 0.1, (float) 0.1, (float) 1.0));
  const Vector3D center = planet->toVector3D(sector.getCenter());
  
  return IndexedMesh::createFromVector3D(vertices, TriangleStrip, GivenCenter, center, indices, color);
}


std::vector<MutableVector2D>* EllipsoidalTileTessellator::createUnitTextCoords() const {
  
  std::vector<MutableVector2D>* textCoords = new std::vector<MutableVector2D>();
  
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
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
  }
  
  // create skirts
  if (_skirted) {
    // west side
    for (int j = 0; j < resolutionMinus1; j++) {
      const int pos = j*resolution;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
    
    // south side
    for (int i = 0; i < resolutionMinus1; i++) {
      const int pos = resolutionMinus1 * resolution + i;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
    
    // east side
    for (int j = resolutionMinus1; j > 0; j--) {
      const int pos = j*resolution + resolutionMinus1;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
    
    // north side
    for (int i = resolutionMinus1; i > 0; i--) {
      const int pos = i;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
  }
  
  // free temp memory
  delete[] u;
  delete[] v;
  
  return textCoords;
}


Mesh* EllipsoidalTileTessellator::createDebugMesh(const RenderContext* rc,
                                                  const Tile* tile) const
{
  const Sector sector = tile->getSector();
  const Planet* planet = rc->getPlanet();
  
  // create vectors
  std::vector<MutableVector3D> vertices;
  std::vector<MutableVector2D> texCoords;
  std::vector<int> indices;
  const int resolutionMinus1 = _resolution - 1;
  int posS = 0;
  
  // compute offset for vertices
  const Vector3D sw = planet->toVector3D(sector.getSW());
  const Vector3D nw = planet->toVector3D(sector.getNW());
  const double offset = nw.sub(sw).length() * 1e-3;
  
  // west side
  for (int j = 0; j < resolutionMinus1; j++) {
    const Geodetic3D g(sector.getInnerPoint(0, (double)j/resolutionMinus1), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // south side
  for (int i = 0; i < resolutionMinus1; i++) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 1), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // east side
  for (int j = resolutionMinus1; j > 0; j--) {
    const Geodetic3D g(sector.getInnerPoint(1, (double)j/resolutionMinus1), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // north side
  for (int i = resolutionMinus1; i > 0; i--) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resolutionMinus1, 0), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  const Color *color = new Color(Color::fromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0));
  const Vector3D center = planet->toVector3D(sector.getCenter());
  
  return IndexedMesh::createFromVector3D(vertices, LineLoop, GivenCenter, center, indices, color);
}
