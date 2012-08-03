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
  
  // create vertices coordinates
  std::vector<MutableVector3D> vertices;
  int resol_1 = _resolution - 1;
  for (int j=0; j<_resolution; j++) {
    for (int i=0; i<_resolution; i++) {
      addVertex(planet, &vertices, sector.getInnerPoint((double)i/resol_1, (double)j/resol_1));
    }
  }
  
  // create indices
  std::vector<int> indices;
  for (int j=0; j<resol_1; j++) {
    if (j > 0) {
      indices.push_back(j*_resolution);
    }
    for (int i = 0; i < _resolution; i++) {
      indices.push_back(j*_resolution + i);
      indices.push_back(j*_resolution + i + _resolution);
    }
    indices.push_back(j*_resolution + 2*_resolution - 1);
  }
  
  // create skirts
  if (_skirted) {
    
    // compute skirt height
    const Vector3D sw = planet->toVector3D(sector.getSW());
    const Vector3D nw = planet->toVector3D(sector.getNW());
    const double skirtHeight = nw.sub(sw).length() * 0.05;
    
    indices.push_back(0);
    int posS = _resolution * _resolution;
    
    // west side
    for (int j=0; j<resol_1; j++) {
      const Geodetic3D g(sector.getInnerPoint(0.0, (double)j/resol_1), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(j*_resolution);
      indices.push_back(posS++);
    }
    
    // south side
    for (int i=0; i<resol_1; i++) {
      const Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 1.0), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(resol_1*_resolution + i);
      indices.push_back(posS++);
    }
    
    // east side
    for (int j=resol_1; j>0; j--) {
      const Geodetic3D g(sector.getInnerPoint(1.0, (double)j/resol_1), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(j*_resolution + resol_1);
      indices.push_back(posS++);
    }
    
    // north side
    for (int i=resol_1; i>0; i--) {
      const Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 0.0), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(i);
      indices.push_back(posS++);
    }
    
    // last triangles
    indices.push_back(0);
    indices.push_back(_resolution*_resolution);
  }

//  const Color *color = new Color(Color::fromRGBA(1.0, 1.0, 1.0, 1.0));
  const Color *color = new Color(Color::fromRGBA((float) 0.1, (float) 0.1, (float) 0.1, (float) 1.0));
  const Vector3D center = planet->toVector3D(sector.getCenter());
  
  //return new IndexedMesh(vertices, TriangleStrip, GivenCenter, center, indices, color);
  return IndexedMesh::CreateFromVector3D(vertices, TriangleStrip, GivenCenter, center, indices, color);
}


std::vector<MutableVector2D>* EllipsoidalTileTessellator::createUnitTextCoords() const {
  
  std::vector<MutableVector2D>* textCoords = new std::vector<MutableVector2D>();
  
  const int n = _resolution;
  
  float* u = new float[n*n];
  float* v = new float[n*n];
  
  for (int j=0; j<n; j++) {
    for (int i=0; i<n; i++) {
      int pos = j*n + i;
      u[pos] = (float) i / (n-1);
      v[pos] = (float) j / (n-1);	
    }
  }
  
  for (int j=0; j<n; j++) {
    for (int i=0; i<n; i++) {
      int pos = j*n + i;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
  }
  
  // create skirts 
  if (_skirted) {
    // west side
    for (int j=0; j<n-1; j++) {
      int pos = j*n;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
    
    // south side
    for (int i=0; i<n-1; i++) {
      int pos = (n-1)*n + i;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
    
    // east side
    for (int j=n-1; j>0; j--) {
      int pos = j*n + n - 1;
      textCoords->push_back(MutableVector2D(u[pos], v[pos]));
    }
    
    // north side
    for (int i=n-1; i>0; i--) {
      int pos = i;
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
  int resol_1 = _resolution - 1;  
  int posS = 0;
  
  // compute offset for vertices
  const Vector3D sw = planet->toVector3D(sector.getSW());
  const Vector3D nw = planet->toVector3D(sector.getNW());
  const double offset = nw.sub(sw).length() * 1e-3;
  
  // west side
  for (int j=0; j<resol_1; j++) {
    const Geodetic3D g(sector.getInnerPoint(0.0, (double)j/resol_1), offset); 
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // south side
  for (int i=0; i<resol_1; i++) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 1.0), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // east side
  for (int j=resol_1; j>0; j--) {
    const Geodetic3D g(sector.getInnerPoint(1.0, (double)j/resol_1), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // north side
  for (int i=resol_1; i>0; i--) {
    const Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 0.0), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // create TexturedMesh
  const Color *color = new Color(Color::fromRGBA((float) 1.0, (float) 0.0, (float) 0.0, (float) 1.0));
  const Vector3D center = planet->toVector3D(sector.getCenter());
  
  //return new IndexedMesh(vertices, LineLoop, GivenCenter, center, indices, color); 
  return IndexedMesh::CreateFromVector3D(vertices, LineLoop, GivenCenter, center, indices, color); 
}
