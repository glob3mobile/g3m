//
//  MeshBoundingVolumeTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#include "MeshBoundingVolumeTileVisibilityTester.hpp"

#include "G3MContext.hpp"
#include "Tile.hpp"
#include "Mesh.hpp"
#include "PlanetRenderContext.hpp"


bool MeshBoundingBoxTileVisibilityTester::isVisible(const G3MRenderContext* rc,
                                                    const PlanetRenderContext* prc,
                                                    Tile* tile) const {
  const Mesh* mesh = tile->getTessellatorMesh(rc, prc);
  if (mesh == NULL) {
    return false;
  }
  
  return mesh->getBoundingBox()->touchesFrustum(prc->_frustumInModelCoordinates);
}


bool MeshBoundingOrientedBoxTileVisibilityTester::isVisible(const G3MRenderContext* rc,
                                                    const PlanetRenderContext* prc,
                                                    Tile* tile) const {
  const Mesh* mesh = tile->getTessellatorMesh(rc, prc);
  if (mesh == NULL) {
    return false;
  }
  
  return mesh->getBoundingOrientedBox()->touchesFrustum(prc->_frustumInModelCoordinates);
}

