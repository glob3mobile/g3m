//
//  BoundingVolumeTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/08/16.
//
//

#include "BoundingVolumeTileVisibilityTester.hpp"

#include "G3MContext.hpp"
#include "Tile.hpp"
#include "PlanetRenderContext.hpp"
#include "BoundingVolume.hpp"


bool BoundingVolumeTileVisibilityTester::isVisible(const G3MRenderContext* rc,
                                                       const PlanetRenderContext* prc,
                                                       Tile* tile) const {
  return tile->getBoundingVolume()->touchesFrustum(prc->_frustumInModelCoordinates);
}

void BoundingVolumeTileVisibilityTester::onTileHasChangedMesh(const Tile* tile) const {

}

void BoundingVolumeTileVisibilityTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {

}

void BoundingVolumeTileVisibilityTester::renderStarted() const {

}
