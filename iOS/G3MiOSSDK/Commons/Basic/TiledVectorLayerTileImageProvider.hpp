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
#include "Sector.hpp"
#include "IImageListener.hpp"

class TiledVectorLayerTileImageProvider : public TileImageProvider {
private:

  class ImageAssembler;

  class CanvasImageListener : public IImageListener {
  private:
    ImageAssembler*   _imageAssembler;
    const std::string _imageId;
  public:
    CanvasImageListener(ImageAssembler* imageAssembler,
                        const std::string& imageId) :
    _imageAssembler(imageAssembler),
    _imageId(imageId)
    {
    }

    void imageCreated(const IImage* image);
  };

  class GEOJSONBufferRasterizer : public GAsyncTask {
  private:
    ImageAssembler* _imageAssembler;
    IByteBuffer*    _buffer;
    const bool      _isBSON;
    ICanvas*        _canvas;
    const int       _imageWidth;
    const int       _imageHeight;

#ifdef C_CODE
    const GEORasterSymbolizer* _symbolizer;
#endif
#ifdef JAVA_CODE
    private GEORasterSymbolizer _symbolizer;
#endif
    const Sector _tileSector;
    const bool   _tileIsMercator;
    const int    _tileLevel;

    const std::string _imageId;

  public:
    GEOJSONBufferRasterizer(ImageAssembler* imageAssembler,
                            IByteBuffer* buffer,
                            bool isBSON,
                            const int imageWidth,
                            const int imageHeight,
                            const GEORasterSymbolizer* symbolizer,
                            const Sector& tileSector,
                            const bool   tileIsMercator,
                            const int    tileLevel,
                            const std::string& imageId) :
    _imageAssembler(imageAssembler),
    _buffer(buffer),
    _imageWidth(imageWidth),
    _imageHeight(imageHeight),
    _symbolizer(symbolizer),
    _tileSector(tileSector),
    _tileIsMercator(tileIsMercator),
    _tileLevel(tileLevel),
    _imageId(imageId),
    _isBSON(isBSON),
    _canvas(NULL)
    {
    }

    ~GEOJSONBufferRasterizer();

    void runInBackground(const G3MContext* context);

    void onPostExecute(const G3MContext* context);

    void cancel();

    void deletedImageAssembler() {
      _imageAssembler = NULL;
    }

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

    void deletedImageAssembler() {
      _imageAssembler = NULL;
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

    GEOJSONBufferRasterizer* _rasterizer;

#ifdef C_CODE
    const GEORasterSymbolizer* _symbolizer;
#endif
#ifdef JAVA_CODE
    private GEORasterSymbolizer _symbolizer;
#endif

    const Sector _tileSector;
    const bool   _tileIsMercator;
    const int    _tileLevel;

  public:
    ImageAssembler(TiledVectorLayerTileImageProvider* tileImageProvider,
                   const Tile*                        tile,
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

    void bufferDownloaded(IByteBuffer* buffer,
                          bool isBSON,
                          const std::string& imageId);
    void bufferDownloadError(const URL& url);
    void bufferDownloadCanceled();

    void rasterizedGEOObject(ICanvas* canvas,
                             const std::string& imageId);
    void deletedRasterizer();

    void imageCreated(const IImage* image,
                      const std::string& imageId);
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
