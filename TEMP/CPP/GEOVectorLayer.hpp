//
//  GEOVectorLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/8/14.
//
//

#ifndef __G3MiOSSDK__GEOVectorLayer__
#define __G3MiOSSDK__GEOVectorLayer__

#include "VectorLayer.hpp"

#include "GEORasterSymbol.hpp"
#include "URL.hpp"
#include "QuadTree.hpp"

class TileImageContribution;
class GEOVectorTileImageProvider;

class GEOVectorLayer : public VectorLayer {
private:
  QuadTree _quadTree;

  mutable GEOVectorTileImageProvider* _tileImageProvider;

public:

  GEOVectorLayer(const std::vector<const LayerTilesRenderParameters*>& parametersVector,
                 const float                                           transparency   = 1.0f,
                 const LayerCondition*                                 condition      = NULL,
                 std::vector<const Info*>*                             layerInfo      = new std::vector<const Info*>());

  GEOVectorLayer(const int                 mercatorFirstLevel = 2,
                 const int                 mercatorMaxLevel   = 18,
                 const int                 wgs84firstLevel    = 0,
                 const int                 wgs84maxLevel      = 18,
                 const float               transparency       = 1.0f,
                 const LayerCondition*     condition          = NULL,
                 std::vector<const Info*>* layerInfo          = new std::vector<const Info*>());

  ~GEOVectorLayer();

  const Sector getDataSector() const;

  std::string getLayerType() const {
    return "GEOVectorLayer";
  }

  bool rawIsEquals(const Layer* that) const {
    return false;
  }

  URL getFeatureInfoURL(const Geodetic2D& position,
                        const Sector& sector) const {
    return URL();
  }

  RenderState getRenderState();

  const std::string description() const;

  GEOVectorLayer* copy() const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  void clear();

  void addSymbol(const GEORasterSymbol* symbol);

  const TileImageContribution* contribution(const Tile* tile) const;

  const QuadTree& getQuadTree() const {
    return _quadTree;
  }

  const std::vector<URL*> getDownloadURLs(const Tile* tile) const;
  
};

#endif
