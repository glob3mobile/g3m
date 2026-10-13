//
//  DebugTilesLayer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/1/26.
//
//

#ifndef __G3M__DebugTilesLayer__
#define __G3M__DebugTilesLayer__

#include "ProceduralLayer.hpp"
#include "GFont.hpp"
#include "Color.hpp"

// Transparent tiles with the ID, sector and bounds of each tile drawn on them, to put over any other layer
class DebugTilesLayer : public ProceduralLayer {
private:
  const GFont _font;
  const Color _color;
  const bool  _showIDLabel;
  const bool  _showSectorLabels;
  const bool  _showTileBounds;

protected:
  const std::string getLayerType() const {
    return "DebugTilesLayer";
  }

  bool rawIsEquals(const Layer* that) const;

public:

  DebugTilesLayer(const std::vector<const LayerTilesRenderParameters*> parametersVector,
                  const GFont&                                         font,
                  const Color&                                         color,
                  const bool                                           showIDLabel,
                  const bool                                           showSectorLabels,
                  const bool                                           showTileBounds,
                  const float                                          transparency,
                  const LayerCondition*                                condition,
                  std::vector<const Info*>*                            layerInfo) :
  ProceduralLayer(parametersVector,
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

  DebugTilesLayer();

  DebugTilesLayer(const GFont&              font,
                  const Color&              color,
                  const bool                showIDLabel,
                  const bool                showSectorLabels,
                  const bool                showTileBounds,
                  const int                 mercatorFirstLevel = 2,
                  const int                 mercatorMaxLevel   = 18,
                  const int                 wgs84firstLevel    = 0,
                  const int                 wgs84maxLevel      = 18,
                  const float               transparency       = 1.0f,
                  const LayerCondition*     condition          = NULL,
                  std::vector<const Info*>* layerInfo          = new std::vector<const Info*>());

  RenderState getRenderState();

  const Sector getDataSector() const;

  const std::string description() const {
    return "[DebugTilesLayer]";
  }

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  DebugTilesLayer* copy() const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  const std::vector<URL*> getDownloadURLs(const Tile* tile) const;

};

#endif
