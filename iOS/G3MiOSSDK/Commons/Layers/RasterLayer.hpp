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
#include "TimeInterval.hpp"
class IDownloader;
class IImageDownloadListener;
class RasterLayerTileImageProvider;

class RasterLayer : public Layer {
private:
  mutable RasterLayerTileImageProvider* _tileImageProvider;

protected:
#ifdef C_CODE
  const LayerTilesRenderParameters* _parameters;
#endif
#ifdef JAVA_CODE
  protected LayerTilesRenderParameters _parameters;
#endif

#ifdef C_CODE
  const TimeInterval _timeToCache;
#endif
#ifdef JAVA_CODE
  protected final TimeInterval _timeToCache;
#endif
  const bool _readExpired;

  RasterLayer(const TimeInterval&               timeToCache,
              const bool                        readExpired,
              const LayerTilesRenderParameters* parameters,
              const float                       transparency,
              const LayerCondition*             condition,
              std::vector<const Info*>*         layerInfo);

  const TimeInterval getTimeToCache() const {
    return _timeToCache;
  }

  bool getReadExpired() const {
    return _readExpired;
  }

  virtual const TileImageContribution* rawContribution(const Tile* tile) const = 0;

  virtual const URL createURL(const Tile* tile) const = 0;

  void setParameters(const LayerTilesRenderParameters* parameters);

  ~RasterLayer();

  const Tile* getParentTileOfSuitableLevel(const Tile* tile) const;

public:
  const std::vector<const LayerTilesRenderParameters*> getLayerTilesRenderParametersVector() const;

  void selectLayerTilesRenderParameters(int index);

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

  const std::vector<URL*> getDownloadURLs(const Tile* tile) const;

};

#endif
