//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#include "EllipsoidalTileTessellator.hpp"

#include "Tile.hpp"
#include "Context.hpp"
#include "IndexedTriangleStripMesh.hpp"

//#include "math.h"

Mesh* EllipsoidalTileTessellator::createMesh(const RenderContext* rc,
                                             const Tile* tile) const {
  int ___diego_at_work;
  
  const int texID = rc->getTexturesHandler()->getTextureIdFromFileName(rc, _textureFilename, 2048, 1024);
  
  if (texID < 1) {
    rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
    return NULL;
  }

  
  const Sector sector = tile->getSector();
  
  const Geodetic2D lower = sector.lower();
  const Geodetic2D upper = sector.upper();
  
  const Geodetic2D g2(upper.latitude(), lower.longitude());
  const Geodetic2D g3(lower.latitude(), upper.longitude());
  
  const Planet* planet = rc->getPlanet();

  std::vector<MutableVector3D> vertices;
  std::vector<MutableVector2D> texCoords;
  addVertex(planet, &vertices, &texCoords, lower); 
  addVertex(planet, &vertices, &texCoords, g3); 
  addVertex(planet, &vertices, &texCoords, g2); 
  addVertex(planet, &vertices, &texCoords, upper); 
  
  
  std::vector<unsigned char> indexes;
  indexes.push_back(0);
  indexes.push_back(1);
  indexes.push_back(2);
  indexes.push_back(3);
  
//  double r = (rand() % 100) / 100.0;
//  double g = (rand() % 100) / 100.0;
//  double b = (rand() % 100) / 100.0;
//  const Color color = Color::fromRGB(r, g, b, 1);
//  
//  return new IndexedTriangleStripMesh(vertices, indexes, color);
  return new IndexedTriangleStripMesh(vertices, indexes, texID, texCoords);
}
