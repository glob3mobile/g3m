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
#include "URL.hpp"
#include "TimeInterval.hpp"


class RLTIP_ImageDownloadListener : public IImageDownloadListener {
private:
  TileImageListener* _listener;
  const bool         _deleteListener;
  const float        _layerAlpha;

public:
  RLTIP_ImageDownloadListener(TileImageListener* listener,
                              bool               deleteListener,
                              float              layerAlpha) :
  _listener(listener),
  _deleteListener(deleteListener),
  _layerAlpha(layerAlpha)
  {
  }

  ~RLTIP_ImageDownloadListener() {
    if (_deleteListener) {
      delete _listener;
    }
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
#warning Diego at work
//    _listener->imageCreated(<#const Tile *tile#>,
//                            image,
//                            url.getPath(),
//                            <#const Sector &imageSector#>,
//                            <#const RectangleF &imageRectangle#>,
//                            _layerAlpha);
  }

  void onError(const URL& url) {
#warning Diego at work
//    _listener->imageCreationError(<#const Tile *tile#>,
//                                  "Download error - " + url.getPath());
  }

  void onCancel(const URL& url) {
#warning Diego at work
//    _listener->imageCreationCanceled(const Tile *tile);
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
#warning Diego at work
  }

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

  /*
  const URL          url;
  const TimeInterval timeToCache;
  const bool         readExpired;
  const float        layerAlpha;
  const Sector&      imageSector;
  const RectangleF&  imageRectangle;
   */

//  const long long requestId = _downloader->requestImage(url,
//                                                        tileDownloadPriority,
//                                                        timeToCache,
//                                                        readExpired,
//                                                        new RLTIP_ImageDownloadListener(listener,
//                                                                                        deleteListener,
//                                                                                        layerAlpha),
//                                                        true /* deleteListener */);

  listener->imageCreationError(tile, "Not yet implemented");
  if (deleteListener) {
    delete listener;
  }

}

void RasterLayerTileImageProvider::cancel(const Tile* tile) {
#warning Diego at work!
}
