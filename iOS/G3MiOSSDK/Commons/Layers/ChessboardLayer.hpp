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


  ChessboardLayer(const Color&                      backgroundColor,
                  const Color&                      boxColor,
                  const int                         splits,
                  const Sector&                     dataSector,
                  const LayerTilesRenderParameters* parameters,
                  const float                       transparency,
                  const LayerCondition*             condition,
                  const std::string&                disclaimerInfo) :
  ProceduralLayer(parameters,
                  transparency,
                  condition,
                  disclaimerInfo),
  _dataSector(dataSector),
  _backgroundColor(backgroundColor),
  _boxColor(boxColor),
  _splits(splits)
  {
  }

public:

  static ChessboardLayer* newMercator(const Color&          backgroundColor = Color::white(),
                                      const Color&          boxColor        = Color::fromRGBA(0.9f, 0.9f, 0.35f, 1),
                                      const int             splits          = 8,
                                      const Sector&         dataSector      = Sector::fullSphere(),
                                      const int             firstLevel      = 2,
                                      const int             maxLevel        = 18,
                                      const float           transparency    = 1,
                                      const LayerCondition* condition       = NULL,
                                      const std::string&    disclaimerInfo  = "");

  static ChessboardLayer* newWGS84(const Color&          backgroundColor = Color::white(),
                                   const Color&          boxColor        = Color::fromRGBA(0.9f, 0.9f, 0.35f, 1),
                                   const int             splits          = 8,
                                   const Sector&         dataSector      = Sector::fullSphere(),
                                   const int             firstLevel      = 2,
                                   const int             maxLevel        = 18,
                                   const float           transparency    = 1,
                                   const LayerCondition* condition       = NULL,
                                   const std::string&    disclaimerInfo  = "");

  std::string getLayerType() const {
    return "ChessboardLayer";
  }

  RenderState getRenderState();

  const Sector getDataSector() const {
    return _dataSector;
  }

  std::vector<Petition*> createTileMapPetitions(const G3MRenderContext* rc,
                                                const LayerTilesRenderParameters* layerTilesRenderParameters,
                                                const Tile* tile) const;

  const std::string description() const {
    return "[ChessboardLayer]";
  }

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const;

  ChessboardLayer* copy() const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  bool rawIsEquals(const Layer* that) const;
  
};

#endif
