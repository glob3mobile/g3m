//
//  PlanetRenderContext.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/4/16.
//
//

#include "PlanetRenderContext.hpp"

#include "TilesRenderParameters.hpp"
#include "LayerTilesRenderParameters.hpp"


const long long PlanetRenderContext::getTileTextureDownloadPriority(const int tileLevel) const {
  return (_tilesRenderParameters->_incrementalTileQuality
          ? _tileTextureDownloadPriority + _layerTilesRenderParameters->_maxLevel - tileLevel
          : _tileTextureDownloadPriority + tileLevel);
}
