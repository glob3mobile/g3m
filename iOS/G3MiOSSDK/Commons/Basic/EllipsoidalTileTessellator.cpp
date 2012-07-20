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
  unsigned int resol_1 = _resolution - 1;
  for (unsigned int j=0; j<_resolution; j++) {
    for (unsigned int i=0; i<_resolution; i++) {
      addVertex(planet, &vertices, sector.getInnerPoint((double)i/resol_1, (double)j/resol_1));
    }
  }
  
  // create indices
  std::vector<unsigned int> indices;
  for (unsigned int j=0; j<resol_1; j++) {
    if (j > 0) {
      indices.push_back(j*_resolution);
    }
    for (unsigned int i = 0; i < _resolution; i++) {
      indices.push_back(j*_resolution + i);
      indices.push_back(j*_resolution + i + _resolution);
    }
    indices.push_back(j*_resolution + 2*_resolution - 1);
  }
  
  // create skirts
  if (_skirted) {
    
    // compute skirt height
    Vector3D sw = planet->toVector3D(sector.getSW());
    Vector3D nw = planet->toVector3D(sector.getNW());
    double skirtHeight = nw.sub(sw).length() * 0.05;
    
    indices.push_back(0);
    unsigned int posS = _resolution * _resolution;
    
    // west side
    for (unsigned int j=0; j<resol_1; j++) {
      Geodetic3D g(sector.getInnerPoint(0.0, (double)j/resol_1), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(j*_resolution);
      indices.push_back(posS++);
    }
    
    // south side
    for (unsigned int i=0; i<resol_1; i++) {
      Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 1.0), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(resol_1*_resolution + i);
      indices.push_back(posS++);
    }
    
    // east side
    for (unsigned int j=resol_1; j>0; j--) {
      Geodetic3D g(sector.getInnerPoint(1.0, (double)j/resol_1), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(j*_resolution + resol_1);
      indices.push_back(posS++);
    }
    
    // north side
    for (unsigned int i=resol_1; i>0; i--) {
      Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 0.0), -skirtHeight);
      addVertex(planet, &vertices, g);
      indices.push_back(i);
      indices.push_back(posS++);
    }
    
    // last triangles
    indices.push_back(0);
    indices.push_back(_resolution*_resolution);
  }

//  Color *color = new Color(Color::fromRGBA(1.0, 1.0, 1.0, 1.0));
  Color *color = new Color(Color::fromRGBA(0.1, 0.1, 0.1, 1.0));
  Vector3D center = planet->toVector3D(sector.getCenter());
  return new IndexedMesh(vertices, TriangleStrip, GivenCenter, center, indices, color);
}


Mesh* EllipsoidalTileTessellator::createDebugMesh(const RenderContext* rc,
                                                  const Tile* tile) const 
{
  const Sector sector = tile->getSector();
  const Planet* planet = rc->getPlanet();
  
  // create vectors
  std::vector<MutableVector3D> vertices;
  std::vector<MutableVector2D> texCoords;
  std::vector<unsigned int> indices;
  unsigned int resol_1 = _resolution - 1;  
  unsigned int posS = 0;
  
  // compute offset for vertices
  Vector3D sw = planet->toVector3D(sector.getSW());
  Vector3D nw = planet->toVector3D(sector.getNW());
  double offset = nw.sub(sw).length() * 1e-3;

  // west side
  for (unsigned int j=0; j<resol_1; j++) {
    Geodetic3D g(sector.getInnerPoint(0.0, (double)j/resol_1), offset); 
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
 
  // south side
  for (unsigned int i=0; i<resol_1; i++) {
    Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 1.0), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // east side
  for (unsigned int j=resol_1; j>0; j--) {
    Geodetic3D g(sector.getInnerPoint(1.0, (double)j/resol_1), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }
  
  // north side
  for (unsigned int i=resol_1; i>0; i--) {
    Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 0.0), offset);
    addVertex(planet, &vertices, g);
    indices.push_back(posS++);
  }

  // create TexturedMesh
  Color *color = new Color(Color::fromRGBA(1.0, 0.0, 0.0, 1.0));
  Vector3D center = planet->toVector3D(sector.getCenter());
  return new IndexedMesh(vertices, LineLoop, GivenCenter, center, indices, color); 
}

