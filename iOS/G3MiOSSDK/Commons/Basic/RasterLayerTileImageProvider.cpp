//
//  RasterLayerTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#include "RasterLayerTileImageProvider.hpp"

#include "RasterLayer.hpp"
#include "TileImageListener.hpp"
#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"

class RasterLayerTileImageProvider_IImageDownloadListener : public IImageDownloadListener {

};

TileImageContribution RasterLayerTileImageProvider::contribution(const Tile* tile) {
  return _layer->contribution(tile);
}

void RasterLayerTileImageProvider::create(const Tile* tile,
                                          const Vector2I& resolution,
                                          long long tileDownloadPriority,
                                          TileImageListener* listener,
                                          bool deleteListener) {
#warning Diego at work!

////const URL& url
////const TimeInterval& timeToCache
////bool readExpired
//
//  const long long requestId = _downloader->requestImage(const URL& url,
//                                                        tileDownloadPriority,
//                                                        const TimeInterval& timeToCache,
//                                                        bool readExpired,
//                                                        new RasterLayerTileImageProvider_IImageDownloadListener(),
//                                                        true /* deleteListener */);
//
////  aa;

  std::set<std::string> errors;
  errors.insert("Not yet implemented");

  listener->imageCreationError(tile, errors);
  if (deleteListener) {
    delete listener;
  }
}

void RasterLayerTileImageProvider::cancel(const Tile* tile) {
//  aa;
#warning Diego at work!
}
