//
//  ChessboardLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

#include "ChessboardLayer.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "RenderState.hpp"
#include "URL.hpp"
#include "LayerCondition.hpp"
#include "ChessboardTileImageProvider.hpp"

ChessboardLayer* ChessboardLayer::newMercator(const Color&          backgroundColor,
                                              const Color&          boxColor,
                                              const int             splits,
                                              const Sector&         dataSector,
                                              const int             firstLevel,
                                              const int             maxLevel,
                                              const float           transparency,
                                              const LayerCondition* condition,
                                              const std::string&    disclaimerInfo) {
  return new ChessboardLayer(backgroundColor,
                             boxColor,
                             splits,
                             dataSector,
                             LayerTilesRenderParameters::createDefaultMercator(firstLevel, maxLevel),
                             transparency,
                             condition,
                             disclaimerInfo);
}

RenderState ChessboardLayer::getRenderState() {
  return RenderState::ready();
}

std::vector<Petition*> ChessboardLayer::createTileMapPetitions(const G3MRenderContext* rc,
                                                               const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                               const Tile* tile) const {
  std::vector<Petition*> petitions;
  return petitions;
}

URL ChessboardLayer::getFeatureInfoURL(const Geodetic2D& position,
                                       const Sector& sector) const {
  return URL();
}

ChessboardLayer* ChessboardLayer::copy() const {
  return new ChessboardLayer(_backgroundColor,
                             _boxColor,
                             _splits,
                             _dataSector,
                             _parameters->copy(),
                             _transparency,
                             (_condition == NULL) ? NULL : _condition->copy(),
                             _disclaimerInfo);
}

TileImageProvider* ChessboardLayer::createTileImageProvider(const G3MRenderContext* rc,
                                                            const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  return new ChessboardTileImageProvider(_backgroundColor, _boxColor, _splits);
}

bool ChessboardLayer::rawIsEquals(const Layer* that) const {
  const ChessboardLayer* t = (const ChessboardLayer*) that;

  if (!_backgroundColor.isEquals(t->_backgroundColor)) {
    return  false;
  }

  if (!_boxColor.isEquals(t->_boxColor)) {
    return  false;
  }

  if (_splits != t->_splits) {
    return false;
  }

  return _dataSector.isEquals(t->_dataSector);
}
