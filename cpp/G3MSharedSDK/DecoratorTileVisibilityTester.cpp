//
//  DecoratorTileVisibilityTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/28/16.
//
//

#include "DecoratorTileVisibilityTester.hpp"

#include "ErrorHandling.hpp"


DecoratorTileVisibilityTester::DecoratorTileVisibilityTester(TileVisibilityTester* tileVisibilityTester) :
_tileVisibilityTester(tileVisibilityTester)
{
  if (_tileVisibilityTester == NULL) {
    THROW_EXCEPTION("NULL NOT ALLOWED");
  }
}

DecoratorTileVisibilityTester::~DecoratorTileVisibilityTester() {
  delete _tileVisibilityTester;
#ifdef JAVA_CODE
  super.dispose();
#endif
}


void DecoratorTileVisibilityTester::onTileHasChangedMesh(const Tile* tile) const {
  _tileVisibilityTester->onTileHasChangedMesh(tile);
}

void DecoratorTileVisibilityTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  _tileVisibilityTester->onLayerTilesRenderParametersChanged(ltrp);
}
