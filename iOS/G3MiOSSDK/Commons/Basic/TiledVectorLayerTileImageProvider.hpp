//
//  TiledVectorLayerTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/30/14.
//
//

#ifndef __G3MiOSSDK__TiledVectorLayerTileImageProvider__
#define __G3MiOSSDK__TiledVectorLayerTileImageProvider__

#include "TileImageProvider.hpp"

#include <map>
class TiledVectorLayer;
class IDownloader;
#include "IBufferDownloadListener.hpp"


class TiledVectorLayerTileImageProvider : public TileImageProvider {
private:

  class GEOJSONBufferDownloadListener : public IBufferDownloadListener {
  private:
    TiledVectorLayerTileImageProvider* _tiledVectorLayerTileImageProvider;
    const std::string                  _tileId;
    const TileImageContribution*       _contribution;
    TileImageListener*                 _listener;
    const bool                         _deleteListener;

  public:
    GEOJSONBufferDownloadListener(TiledVectorLayerTileImageProvider* tiledVectorLayerTileImageProvider,
                                  const std::string&                 tileId,
                                  const TileImageContribution*       contribution,
                                  TileImageListener*                 listener,
                                  bool                               deleteListener) :
    _tiledVectorLayerTileImageProvider(tiledVectorLayerTileImageProvider),
    _tileId(tileId),
    _contribution(contribution),
    _listener(listener),
    _deleteListener(deleteListener)
    {
    }

    ~GEOJSONBufferDownloadListener();

    void onDownload(const URL& url,
                    IByteBuffer* buffer,
                    bool expired);

    void onError(const URL& url);

    void onCancel(const URL& url);

    void onCanceledDownload(const URL& url,
                            IByteBuffer* buffer,
                            bool expired) {
      // do nothing
    }

  };

  const TiledVectorLayer* _layer;
  IDownloader*            _downloader;

  std::map<const std::string, long long> _requestsIdsPerTile;

public:

  TiledVectorLayerTileImageProvider(const TiledVectorLayer* layer,
                                    IDownloader* downloader) :
  _layer(layer),
  _downloader(downloader)
  {
  }


  const TileImageContribution* contribution(const Tile* tile);

  void create(const Tile* tile,
              const TileImageContribution* contribution,
              const Vector2I& resolution,
              long long tileDownloadPriority,
              bool logDownloadActivity,
              TileImageListener* listener,
              bool deleteListener,
              FrameTasksExecutor* frameTasksExecutor);

  void cancel(const std::string& tileId);

  void requestFinish(const std::string& tileId);

};

#endif
