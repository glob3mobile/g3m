//
//  RasterLayerTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/22/14.
//
//

#include "RasterLayerTileImageProvider.hpp"

#include "IImageDownloadListener.hpp"
#include "TileImageListener.hpp"
#include "URL.hpp"
#include "RasterLayer.hpp"
#include "Tile.hpp"
#include "IDownloader.hpp"
#include "ErrorHandling.hpp"

class RLTIP_ImageDownloadListener : public IImageDownloadListener {
private:
  RasterLayerTileImageProvider* _rasterLayerTileImageProvider;
  const std::string             _tileID;
  TileImageListener*            _listener;
  const bool                    _deleteListener;
#ifdef C_CODE
  const TileImageContribution*  _contribution;
#endif
#ifdef JAVA_CODE
  private TileImageContribution _contribution;
#endif

public:
  RLTIP_ImageDownloadListener(RasterLayerTileImageProvider* rasterLayerTileImageProvider,
                              const std::string&            tileID,
                              const TileImageContribution*  contribution,
                              TileImageListener*            listener,
                              bool                          deleteListener) :
  _rasterLayerTileImageProvider(rasterLayerTileImageProvider),
  _tileID(tileID),
  _contribution(contribution),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~RLTIP_ImageDownloadListener() {
    _rasterLayerTileImageProvider->requestFinish(_tileID);

    if (_deleteListener) {
      delete _listener;
    }

    TileImageContribution::releaseContribution(_contribution);
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void onDownload(const URL& url,
                  IImage* image,
                  bool expired) {
    const TileImageContribution* contribution = _contribution;
    _contribution = NULL; // moves ownership of _contribution to _listener
    _listener->imageCreated(_tileID,
                            image,
                            url._path,
                            contribution);
  }

  void onError(const URL& url) {
    _listener->imageCreationError(_tileID,
                                  "Download error - " + url._path);
  }

  void onCancel(const URL& url) {
    _listener->imageCreationCanceled(_tileID);
  }

  void onCanceledDownload(const URL& url,
                          IImage* image,
                          bool expired) {
    // do nothing
  }
};

RasterLayerTileImageProvider::~RasterLayerTileImageProvider() {
#ifdef C_CODE
  for (std::map<const std::string, long long>::iterator iter = _requestsIdsPerTile.begin();
       iter != _requestsIdsPerTile.end();
       ++iter) {
    const long long requestID = iter->second;
    _downloader->cancelRequest(requestID);
  }
#endif
#ifdef JAVA_CODE
  for (java.util.Map.Entry<String, Long> entry : _requestsIdsPerTile.entrySet()) {
    _downloader.cancelRequest(entry.getValue());
  }

  super.dispose();
#endif
}

const TileImageContribution* RasterLayerTileImageProvider::contribution(const Tile* tile) {
  return (_layer == NULL) ? NULL : _layer->contribution(tile);
}

void RasterLayerTileImageProvider::create(const Tile* tile,
                                          const TileImageContribution* contribution,
                                          const Vector2S& resolution,
                                          long long tileTextureDownloadPriority,
                                          bool logDownloadActivity,
                                          TileImageListener* listener,
                                          bool deleteListener,
                                          FrameTasksExecutor* frameTasksExecutor) {
  const std::string tileID = tile->_id;

  const long long requestID = _layer->requestImage(tile,
                                                   _downloader,
                                                   tileTextureDownloadPriority,
                                                   logDownloadActivity,
                                                   new RLTIP_ImageDownloadListener(this,
                                                                                   tileID,
                                                                                   contribution,
                                                                                   listener,
                                                                                   deleteListener),
                                                   true /* deleteListener */);

  if (requestID >= 0) {
    _requestsIdsPerTile[tileID] = requestID;
  }
}

void RasterLayerTileImageProvider::cancel(const std::string& tileID) {
#ifdef C_CODE
  if (_requestsIdsPerTile.find(tileID) != _requestsIdsPerTile.end()) {
    const long long requestID = _requestsIdsPerTile[tileID];

    _downloader->cancelRequest(requestID);

    _requestsIdsPerTile.erase(tileID);
  }
#endif
#ifdef JAVA_CODE
  final Long requestID = _requestsIdsPerTile.remove(tileID);
  if (requestID != null) {
    _downloader.cancelRequest(requestID);
  }
#endif
}

void RasterLayerTileImageProvider::requestFinish(const std::string& tileID) {
  _requestsIdsPerTile.erase(tileID);
}

void RasterLayerTileImageProvider::layerDeleted(const RasterLayer* layer) {
  if (layer != _layer) {
    THROW_EXCEPTION("Logic error");
  }
  _layer = NULL;
}
