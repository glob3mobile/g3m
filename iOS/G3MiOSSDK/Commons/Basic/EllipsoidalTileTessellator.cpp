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
#include "IndexedMesh.hpp"
#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"

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
  const Planet* planet = rc->getPlanet();

  std::vector<MutableVector3D> vertices;
  std::vector<MutableVector2D> texCoords;
  addVertex(planet, &vertices, &texCoords, sector.getSW()); 
  addVertex(planet, &vertices, &texCoords, sector.getSE()); 
  addVertex(planet, &vertices, &texCoords, sector.getNW()); 
  addVertex(planet, &vertices, &texCoords, sector.getNE()); 
  
  std::vector<unsigned int> indexes;
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
//  return new IndexedMesh(vertices, TriangleStrip, indexes, NULL/*, texID, texCoords*/);
  
  
  return new TexturedMesh(new IndexedMesh(vertices, TriangleStrip, indexes, NULL), new TextureMapping(texID, texCoords)); 
  
}
