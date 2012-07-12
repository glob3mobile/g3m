//
//  EllipsoidalTileTessellator_v2.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "EllipsoidalTileTessellator_v2.hpp"

#include "Tile.hpp"
#include "Context.hpp"
#include "IndexedMesh.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"


Mesh* EllipsoidalTileTessellator_v2::createMesh(const RenderContext* rc,
                                             const Tile* tile) const {
  int ___agustin_at_work;
  
  const int texID = rc->getTexturesHandler()->getTextureIdFromFileName(rc, _textureFilename, 2048, 1024);
  
  if (texID < 1) {
    rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
    return NULL;
  }

  const Sector sector = tile->getSector();
  const Planet* planet = rc->getPlanet();

  // create vertices coordinates
  std::vector<MutableVector3D> vertices;
  std::vector<MutableVector2D> texCoords;
  unsigned int resol_1 = _resolution - 1;
  for (unsigned int j=0; j<_resolution; j++)
    for (unsigned int i=0; i<_resolution; i++) 
      addVertex(planet, &vertices, &texCoords, sector.getInnerPoint((double)i/resol_1, (double)j/resol_1));
  
  // create indices
  std::vector<unsigned int> indices;
  for (unsigned int j=0; j<resol_1; j++) {
    if (j>0) indices.push_back(j*_resolution);
    for (unsigned int i = 0; i < _resolution; i++) {
      indices.push_back(j*_resolution + i);
      indices.push_back(j*_resolution + i + _resolution);
    }
    indices.push_back(j*_resolution + 2*_resolution - 1);
  }

  // create skirts
  const double skirtHeight = 5e5;
  if (_skirted) {
    indices.push_back(0);
    unsigned int posS = _resolution * _resolution;
    
    // west side
    for (unsigned int j=0; j<resol_1; j++) {
      Geodetic3D g(sector.getInnerPoint(0.0, (double)j/resol_1), -skirtHeight);
      addVertex(planet, &vertices, &texCoords, g);
      indices.push_back(j*_resolution);
      indices.push_back(posS++);
    }
    
    // south side
    for (unsigned int i=0; i<resol_1; i++) {
      Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 1.0), -skirtHeight);
      addVertex(planet, &vertices, &texCoords, g);
      indices.push_back(resol_1*_resolution + i);
      indices.push_back(posS++);
    }
    
    // east side
    for (unsigned int j=resol_1; j>0; j--) {
      Geodetic3D g(sector.getInnerPoint(1.0, (double)j/resol_1), -skirtHeight);
      addVertex(planet, &vertices, &texCoords, g);
      indices.push_back(j*_resolution + resol_1);
      indices.push_back(posS++);
    }

    // north side
    for (unsigned int i=resol_1; i>0; i--) {
      Geodetic3D g(sector.getInnerPoint((double)i/resol_1, 0.0), -skirtHeight);
      addVertex(planet, &vertices, &texCoords, g);
      indices.push_back(i);
      indices.push_back(posS++);
    }
    
    // last triangles
    indices.push_back(0);
    indices.push_back(_resolution*_resolution);
  }
  
  
  // create TexturedMesh
  return new TexturedMesh(new IndexedMesh(vertices, TriangleStrip, indices, NULL), true, new TextureMapping(texID, texCoords)); 
  
}
