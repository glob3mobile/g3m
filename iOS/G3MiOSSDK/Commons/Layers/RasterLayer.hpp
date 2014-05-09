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
class IDownloader;
class IImageDownloadListener;
#include "TimeInterval.hpp"

class RasterLayer : public Layer {
protected:
  const TimeInterval _timeToCache;
  const bool         _readExpired;

  RasterLayer(const TimeInterval&               timeToCache,
              const bool                        readExpired,
              const LayerTilesRenderParameters* parameters,
              const float                       transparency,
              const LayerCondition*             condition);

  const TimeInterval getTimeToCache() const {
    return _timeToCache;
  }

  bool getReadExpired() const {
    return _readExpired;
  }

  virtual const TileImageContribution* rawContribution(const Tile* tile) const = 0;

  virtual const URL createURL(const Tile* tile) const = 0;

public:
  bool isEquals(const Layer* that) const;

  TileImageProvider* createTileImageProvider(const G3MRenderContext* rc,
                                             const LayerTilesRenderParameters* layerTilesRenderParameters) const;

  const TileImageContribution* contribution(const Tile* tile) const;

  long long requestImage(const Tile* tile,
                         IDownloader* downloader,
                         long long tileDownloadPriority,
                         bool logDownloadActivity,
                         IImageDownloadListener* listener,
                         bool deleteListener) const;

};

#endif
