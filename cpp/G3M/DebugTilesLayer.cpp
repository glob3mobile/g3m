//
//  DebugTilesLayer.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/1/26.
//
//

#include "DebugTilesLayer.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "RenderState.hpp"
#include "URL.hpp"
#include "Sector.hpp"
#include "LayerCondition.hpp"
#include "DebugTileImageProvider.hpp"

DebugTilesLayer::DebugTilesLayer() :
ProceduralLayer(LayerTilesRenderParameters::createDefaultMultiProjection(2,  // mercatorFirstLevel
                                                                         18, // mercatorMaxLevel
                                                                         0,  // wgs84firstLevel
                                                                         18  // wgs84maxLevel
                                                                         ),
                1.0f, // transparency
                NULL, // condition
                new std::vector<const Info*>()),
_font(GFont::monospaced(15)),
_color(Color::YELLOW),
_showIDLabel(true),
_showSectorLabels(true),
_showTileBounds(true)
{
}

DebugTilesLayer::DebugTilesLayer(const GFont&              font,
                                 const Color&              color,
                                 const bool                showIDLabel,
                                 const bool                showSectorLabels,
                                 const bool                showTileBounds,
                                 const int                 mercatorFirstLevel,
                                 const int                 mercatorMaxLevel,
                                 const int                 wgs84firstLevel,
                                 const int                 wgs84maxLevel,
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
_font(font),
_color(color),
_showIDLabel(showIDLabel),
_showSectorLabels(showSectorLabels),
_showTileBounds(showTileBounds)
{
}

RenderState DebugTilesLayer::getRenderState() {
  return RenderState::ready();
}

const Sector DebugTilesLayer::getDataSector() const {
  return Sector::FULL_SPHERE;
}

URL DebugTilesLayer::getFeatureInfoURL(const Geodetic2D& position,
                                       const Sector& sector) const {
  return URL::nullURL();
}

DebugTilesLayer* DebugTilesLayer::copy() const {
  return new DebugTilesLayer(createParametersVectorCopy(),
                             _font,
                             _color,
                             _showIDLabel,
                             _showSectorLabels,
                             _showTileBounds,
                             _transparency,
                             (_condition == NULL) ? NULL : _condition->copy(),
                             _layerInfo);
}

TileImageProvider* DebugTilesLayer::createTileImageProvider(const G3MRenderContext* rc,
                                                            const LayerTilesRenderParameters* layerTilesRenderParameters) const {
  return new DebugTileImageProvider(_font, _color, _showIDLabel, _showSectorLabels, _showTileBounds);
}

bool DebugTilesLayer::rawIsEquals(const Layer* that) const {
  const DebugTilesLayer* t = (const DebugTilesLayer*) that;

  // GFont has no isEquals, so the font is not compared
  if (!_color.isEquals(t->_color)) {
    return false;
  }

  if (_showIDLabel != t->_showIDLabel) {
    return false;
  }

  if (_showSectorLabels != t->_showSectorLabels) {
    return false;
  }

  return (_showTileBounds == t->_showTileBounds);
}

const std::vector<URL*> DebugTilesLayer::getDownloadURLs(const Tile* tile) const {
  std::vector<URL*> result;
  return result;
}
