//
//  ChessboardLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/15/14.
//
//

#ifndef __G3MiOSSDK__ChessboardLayer__
#define __G3MiOSSDK__ChessboardLayer__

#include "ProceduralLayer.hpp"
#include "Sector.hpp"
#include "Color.hpp"

class ChessboardLayer : public ProceduralLayer {
private:
  const Sector _dataSector;

  const Color _backgroundColor;
  const Color _boxColor;
  const int   _splits;


public:

  ChessboardLayer(const std::vector<const LayerTilesRenderParameters*> parametersVector,
                  const Color&                                         backgroundColor = Color::white(),
                  const Color&                                         boxColor        = Color::fromRGBA(0.9f, 0.9f, 0.35f, 1.0f),
                  const int                                            splits          = 8,
                  const Sector&                                        dataSector      = Sector::fullSphere(),
                  const float                                          transparency    = 1.0f,
                  const LayerCondition*                                condition       = NULL,
                  std::vector<const Info*>*                            layerInfo       = new std::vector<const Info*>()) :
  ProceduralLayer(parametersVector,
                  transparency,
                  condition,
                  layerInfo),
  _dataSector(dataSector),
  _backgroundColor(backgroundColor),
  _boxColor(boxColor),
  _splits(splits)
  {
  }

  ChessboardLayer(const int                 mercatorFirstLevel = 2,
                  const int                 mercatorMaxLevel   = 18,
                  const int                 wgs84firstLevel    = 0,
                  const int                 wgs84maxLevel      = 18,
                  const Color&              backgroundColor    = Color::white(),
                  const Color&              boxColor           = Color::fromRGBA(0.9f, 0.9f, 0.35f, 1.0f),
                  const int                 splits             = 8,
                  const Sector&             dataSector         = Sector::fullSphere(),
                  const float               transparency       = 1.0f,
                  const LayerCondition*     condition          = NULL,
                  std::vector<const Info*>* layerInfo          = new std::vector<const Info*>());

  std::string getLayerType() const {
    return "ChessboardLayer";
  }

  RenderState getRenderState();

  const Sector getDataSector() const {
    return _dataSector;
  }

  const std::string description() const {
    return "[ChessboardLayer]";
  }

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  ChessboardLayer* copy() const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  bool rawIsEquals(const Layer* that) const;

  const std::vector<URL*> getDownloadURLs(const Tile* tile) const;
  
};

#endif
