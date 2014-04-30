//
//  TiledVectorLayerTileImageProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#include "TiledVectorLayerTileImageProvider.hpp"
#include "TiledVectorLayer.hpp"
#include "Tile.hpp"
#include "IDownloader.hpp"
#include "TileImageListener.hpp"
#include "TileImageContribution.hpp"

TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::~GEOJSONBufferDownloadListener() {
  _tiledVectorLayerTileImageProvider->requestFinish(_tileId);

  if (_deleteListener) {
    delete _listener;
  }

  TileImageContribution::deleteContribution(_contribution);

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onDownload(const URL& url,
                                                                                  IByteBuffer* buffer,
                                                                                  bool expired) {
//  _listener->imageCreated(_tileId,
//                          image,
//                          url.getPath(),
//                          _contribution);
//  _contribution = NULL;
#warning Diego at work!
  _listener->imageCreationError(_tileId,
                                "NOT YET IMPLEMENTED");

  delete buffer;
  TileImageContribution::deleteContribution(_contribution);
  _contribution = NULL;
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onError(const URL& url) {
  _listener->imageCreationError(_tileId,
                                "Download error - " + url.getPath());
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onCancel(const URL& url) {
  _listener->imageCreationCanceled(_tileId);
}


const TileImageContribution* TiledVectorLayerTileImageProvider::contribution(const Tile* tile) {
  return _layer->contribution(tile);
}

void TiledVectorLayerTileImageProvider::create(const Tile* tile,
                                               const TileImageContribution* contribution,
                                               const Vector2I& resolution,
                                               long long tileDownloadPriority,
                                               bool logDownloadActivity,
                                               TileImageListener* listener,
                                               bool deleteListener,
                                               FrameTasksExecutor* frameTasksExecutor) {
  const std::string tileId = tile->_id;

  const long long requestId = _layer->requestGEOJSONBuffer(tile,
                                                           _downloader,
                                                           tileDownloadPriority,
                                                           logDownloadActivity,
                                                           new GEOJSONBufferDownloadListener(this,
                                                                                             tileId,
                                                                                             contribution,
                                                                                             listener,
                                                                                             deleteListener),
                                                           true /* deleteListener */);

  if (requestId >= 0) {
    _requestsIdsPerTile[tileId] = requestId;
  }
}

void TiledVectorLayerTileImageProvider::cancel(const std::string& tileId) {
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

void TiledVectorLayerTileImageProvider::requestFinish(const std::string& tileId) {
  _requestsIdsPerTile.erase(tileId);
}
