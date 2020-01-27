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

ChessboardLayer::ChessboardLayer(const int                 mercatorFirstLevel,
                                 const int                 mercatorMaxLevel,
                                 const int                 wgs84firstLevel,
                                 const int                 wgs84maxLevel,
                                 const Color&              backgroundColor,
                                 const Color&              boxColor,
                                 const int                 splits,
                                 const Sector&             dataSector,
                                 const float               transparency,
                                 const LayerCondition*     condition,
                                 std::vector<const Info*>* layerInfo) :
ProceduralLayer(LayerTilesRenderParameters::createDefaultMultiProjection(mercatorFirstLevel,
                                                                         mercatorMaxLevel,
                                                                         wgs84firstLevel,
                                                                         wgs84maxLevel),
                transparency,
                condition,
                layerInfo),
_dataSector(dataSector),
_backgroundColor(backgroundColor),
_boxColor(boxColor),
_splits(splits)
{
}

RenderState ChessboardLayer::getRenderState() {
  return RenderState::ready();
}

URL ChessboardLayer::getFeatureInfoURL(const Geodetic2D& position,
                                       const Sector& sector) const {
  return URL();
}

ChessboardLayer* ChessboardLayer::copy() const {
  return new ChessboardLayer(createParametersVectorCopy(),
                             _backgroundColor,
                             _boxColor,
                             _splits,
                             _dataSector,
                             _transparency,
                             (_condition == NULL) ? NULL : _condition->copy(),
                             _layerInfo);
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

const std::vector<URL*> ChessboardLayer::getDownloadURLs(const Tile* tile) const {
  std::vector<URL*> result;
  return result;
}
