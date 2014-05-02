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
#include "IThreadUtils.hpp"
class GEOObject;
class GEORasterSymbolizer;
#include "Vector2I.hpp"

class TiledVectorLayerTileImageProvider : public TileImageProvider {
private:

  class ImageAssembler;

  class GEOJSONBufferParser : public GAsyncTask {
  private:
    ImageAssembler* _imageAssembler;
    IByteBuffer*    _buffer;
    GEOObject*      _geoObject;

  public:
    GEOJSONBufferParser(ImageAssembler* imageAssembler,
                        IByteBuffer* buffer) :
    _imageAssembler(imageAssembler),
    _buffer(buffer),
    _geoObject(NULL)
    {
    }

    ~GEOJSONBufferParser();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

    void cancel();

  };


  class GEOJSONBufferDownloadListener : public IBufferDownloadListener {
  private:
    ImageAssembler* _imageAssembler;

  public:
    GEOJSONBufferDownloadListener(ImageAssembler* imageAssembler) :
    _imageAssembler(imageAssembler)
    {
    }

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


  class ImageAssembler {
  private:
    TiledVectorLayerTileImageProvider* _tileImageProvider;
    const std::string                  _tileId;
    TileImageListener*                 _listener;
    const bool                         _deleteListener;
    IDownloader*                       _downloader;
    const IThreadUtils*                _threadUtils;
    const int                          _imageWidth;
    const int                          _imageHeight;

#ifdef C_CODE
    const TileImageContribution*       _contribution;
#endif
#ifdef JAVA_CODE
    private TileImageContribution _contribution;
#endif


    bool _canceled;

    GEOJSONBufferDownloadListener* _downloadListener;
    long long _downloadRequestId;

    GEOJSONBufferParser* _parser;

#ifdef C_CODE
    const GEORasterSymbolizer* _symbolizer;
#endif
#ifdef JAVA_CODE
    private GEORasterSymbolizer _symbolizer;
#endif

  public:
    ImageAssembler(TiledVectorLayerTileImageProvider* tileImageProvider,
                   const std::string&                 tileId,
                   const TileImageContribution*       contribution,
                   TileImageListener*                 listener,
                   bool                               deleteListener,
                   const Vector2I&                    imageResolution,
                   IDownloader*                       downloader,
                   const IThreadUtils*                threadUtils);

    ~ImageAssembler();

    void start(const TiledVectorLayer* layer,
               const Tile*             tile,
               long long               tileDownloadPriority,
               bool                    logDownloadActivity);

    void cancel();

    void bufferDownloaded(IByteBuffer* buffer);
    void bufferDownloadError(const URL& url);
    void bufferDownloadCanceled();

    void parsedGEOObject(GEOObject* geoObject);
    void deletedParser();
  };


  const TiledVectorLayer* _layer;
  IDownloader*            _downloader;
  const IThreadUtils*     _threadUtils;

  std::map<const std::string, ImageAssembler*> _assemblers;

public:

  TiledVectorLayerTileImageProvider(const TiledVectorLayer* layer,
                                    IDownloader*            downloader,
                                    const IThreadUtils*     threadUtils) :
  _layer(layer),
  _downloader(downloader),
  _threadUtils(threadUtils)
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
