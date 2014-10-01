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
  const std::string             _tileId;
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
                              const std::string&            tileId,
                              const TileImageContribution*  contribution,
                              TileImageListener*            listener,
                              bool                          deleteListener) :
  _rasterLayerTileImageProvider(rasterLayerTileImageProvider),
  _tileId(tileId),
  _contribution(contribution),
  _listener(listener),
  _deleteListener(deleteListener)
  {
  }

  ~RLTIP_ImageDownloadListener() {
    _rasterLayerTileImageProvider->requestFinish(_tileId);

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
    _listener->imageCreated(_tileId,
                            image,
                            url._path,
                            contribution);
  }

  void onError(const URL& url) {
    _listener->imageCreationError(_tileId,
                                  "Download error - " + url._path);
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

RasterLayerTileImageProvider::~RasterLayerTileImageProvider() {
#ifdef C_CODE
  std::map<std::string, long long>::iterator iter;
  for (iter = _requestsIdsPerTile.begin();
       iter != _requestsIdsPerTile.end();
       ++iter) {
    const long long requestId = iter->second;
    _downloader->cancelRequest(requestId);
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
                                          const Vector2I& resolution,
                                          long long tileDownloadPriority,
                                          bool logDownloadActivity,
                                          TileImageListener* listener,
                                          bool deleteListener,
                                          FrameTasksExecutor* frameTasksExecutor) {
  const std::string tileId = tile->_id;

  const long long requestId = _layer->requestImage(tile,
                                                   _downloader,
                                                   tileDownloadPriority,
                                                   logDownloadActivity,
                                                   new RLTIP_ImageDownloadListener(this,
                                                                                   tileId,
                                                                                   contribution,
                                                                                   listener,
                                                                                   deleteListener),
                                                   true /* deleteListener */);

  if (requestId >= 0) {
    _requestsIdsPerTile[tileId] = requestId;
  }
}

void RasterLayerTileImageProvider::cancel(const std::string& tileId) {
#ifdef C_CODE
  if (_requestsIdsPerTile.find(tileId) != _requestsIdsPerTile.end()) {
    const long long requestId = _requestsIdsPerTile[tileId];

    _downloader->cancelRequest(requestId);

    _requestsIdsPerTile.erase(tileId);
  }
#endif
#ifdef JAVA_CODE
  final Long requestId = _requestsIdsPerTile.remove(tileId);
  if (requestId != null) {
    _downloader.cancelRequest(requestId);
  }
#endif
}

void RasterLayerTileImageProvider::requestFinish(const std::string& tileId) {
  _requestsIdsPerTile.erase(tileId);
}

void RasterLayerTileImageProvider::layerDeleted(const RasterLayer* layer) {
  if (layer != _layer) {
    THROW_EXCEPTION("Logic error");
  }
  _layer = NULL;
}
