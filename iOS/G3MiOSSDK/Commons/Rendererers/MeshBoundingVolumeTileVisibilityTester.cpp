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


bool MeshBoundingVolumeTileVisibilityTester::isVisible(const G3MRenderContext* rc,
                                                       const PlanetRenderContext* prc,
                                                       Tile* tile) const {
  const Mesh* mesh = tile->getTessellatorMesh(rc, prc);
  if (mesh == NULL) {
    return false;
  }

  return mesh->getBoundingVolume()->touchesFrustum(prc->_frustumInModelCoordinates);
}

void MeshBoundingVolumeTileVisibilityTester::onTileHasChangedMesh(const Tile* tile) const {

}

void MeshBoundingVolumeTileVisibilityTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {

}

void MeshBoundingVolumeTileVisibilityTester::renderStarted() const {

}
