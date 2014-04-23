//
//  RasterLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#ifndef __G3MiOSSDK__RasterLayer__
#define __G3MiOSSDK__RasterLayer__

#include "Layer.hpp"

#include "TileImageContribution.hpp"
class TimeInterval;


class RasterLayer : public Layer {
protected:
  const long long _timeToCacheMS;
  const bool      _readExpired;

  RasterLayer(LayerCondition* condition,
              const TimeInterval& timeToCache,
              bool readExpired,
              const LayerTilesRenderParameters* parameters,
              float transparency);

  const TimeInterval getTimeToCache() const;

  bool getReadExpired() const {
    return _readExpired;
  }

  virtual const TileImageContribution rawContribution(const Tile* tile) const = 0;

public:
  bool isEquals(const Layer* that) const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  const TileImageContribution contribution(const Tile* tile) const;

};

#endif
