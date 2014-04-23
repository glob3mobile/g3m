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
//#include "IDownloader.hpp"
#include "IImageDownloadListener.hpp"
#include "URL.hpp"
#include "TimeInterval.hpp"
#include "Tile.hpp"

class RLTIP_ImageDownloadListener : public IImageDownloadListener {
private:
  const std::string           _tileId;
  const TileImageContribution _contribution;

  TileImageListener* _listener;
  const bool         _deleteListener;

public:
  RLTIP_ImageDownloadListener(const std::string&           tileId,
                              const TileImageContribution& contribution,
                              TileImageListener*           listener,
                              bool                         deleteListener) :
  _tileId(tileId),
  _contribution(contribution),
  _listener(listener),
  _deleteListener(deleteListener)
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
    _listener->imageCreated(_tileId,
                            image,
                            url.getPath(),
                            _contribution);
  }

  void onError(const URL& url) {
    _listener->imageCreationError(_tileId,
                                  "Download error - " + url.getPath());
  }

  void onCancel(const URL& url) {
    _listener->imageCreationCanceled(_tileId);
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }
};

TileImageContribution RasterLayerTileImageProvider::contribution(const Tile* tile) {
  return _layer->contribution(tile);
}

void RasterLayerTileImageProvider::create(const Tile* tile,
                                          const TileImageContribution& contribution,
                                          const Vector2I& resolution,
                                          long long tileDownloadPriority,
                                          TileImageListener* listener,
                                          bool deleteListener) {
#warning Diego at work!

  const std::string tileId = tile->_id;

  const long long requestId = _layer->requestImage(tile,
                                                   _downloader,
                                                   tileDownloadPriority,
                                                   new RLTIP_ImageDownloadListener(tileId,
                                                                                   contribution,
                                                                                   listener,
                                                                                   deleteListener),
                                                   true /* deleteListener */);


//  aa;

//  listener->imageCreationError(tile->_id, "Not yet implemented");
//  if (deleteListener) {
//    delete listener;
//  }

}

void RasterLayerTileImageProvider::cancel(const Tile* tile) {
#warning Diego at work!
}
