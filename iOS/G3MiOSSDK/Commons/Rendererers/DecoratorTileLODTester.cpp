//
//  DecoratorTileLODTester.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/26/16.
//
//

#include "DecoratorTileLODTester.hpp"

#include "ErrorHandling.hpp"


DecoratorTileLODTester::DecoratorTileLODTester(TileLODTester* tileLODTester) :
_tileLODTester(tileLODTester)
{
  if (_tileLODTester == NULL) {
    THROW_EXCEPTION("NULL NOT ALLOWED");
  }
}

DecoratorTileLODTester::~DecoratorTileLODTester() {
  delete _tileLODTester;
#ifdef JAVA_CODE
  super.dispose();
#endif
}


void DecoratorTileLODTester::onTileHasChangedMesh(const Tile* tile) const {
  _tileLODTester->onTileHasChangedMesh(tile);
}

void DecoratorTileLODTester::onLayerTilesRenderParametersChanged(const LayerTilesRenderParameters* ltrp) {
  _tileLODTester->onLayerTilesRenderParametersChanged(ltrp);
}
