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


bool MeshBoundingVolumeTileVisibilityTester::isVisible(const Tile* tile,
                                                       const G3MRenderContext* rc,
                                                       long long nowInMS,
                                                       const Frustum* frustumInModelCoordinates) const {
  const Mesh* mesh = tile->getCurrentTessellatorMesh();
  if (mesh == NULL) {
    return false;
  }

  return mesh->getBoundingVolume()->touchesFrustum(frustumInModelCoordinates);
}

void MeshBoundingVolumeTileVisibilityTester::onTileHasChangedMesh(const Tile* tile) const {

}

void MeshBoundingVolumeTileVisibilityTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {

}

void MeshBoundingVolumeTileVisibilityTester::renderStarted() const {

}
