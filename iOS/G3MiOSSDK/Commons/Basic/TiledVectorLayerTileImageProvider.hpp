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
#include <list>

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

  class GEOObjectHolder;

  class GEOJSONBufferRasterizer : public GAsyncTask {
  private:
#ifdef C_CODE
    const URL       _url;
#endif
#ifdef JAVA_CODE
    private final URL _url;
#endif
    ImageAssembler*        _imageAssembler;
    IByteBuffer*           _buffer;
    const GEOObjectHolder* _geoObjectHolder;
    GEOObject*             _geoObject;
    const bool             _geoObjectFromCache;
    ICanvas*               _canvas;
    const int              _imageWidth;
    const int              _imageHeight;

#ifdef C_CODE
    const GEORasterSymbolizer* _symbolizer;
#endif
#ifdef JAVA_CODE
    private GEORasterSymbolizer _symbolizer;
#endif
    const std::string _tileId;
    const Sector      _tileSector;
    const bool        _tileIsMercator;
    const int         _tileLevel;

    void rasterizeGEOObject(const GEOObject* geoObject);


  public:
    GEOJSONBufferRasterizer(ImageAssembler*            imageAssembler,
                            const URL&                 url,
                            IByteBuffer*               buffer,          // buffer or
                            const GEOObjectHolder*     geoObjectHolder, // GEOObjectHolder, never both
                            const int                  imageWidth,
                            const int                  imageHeight,
                            const GEORasterSymbolizer* symbolizer,
                            const std::string&         tileId,
                            const Sector&              tileSector,
                            const bool                 tileIsMercator,
                            const int                  tileLevel) :
    _imageAssembler(imageAssembler),
    _url(url),
    _buffer(buffer),
    _geoObjectHolder(geoObjectHolder),
    _geoObjectFromCache( geoObjectHolder != NULL ),
    _imageWidth(imageWidth),
    _imageHeight(imageHeight),
    _symbolizer(symbolizer),
    _tileId(tileId),
    _tileSector(tileSector),
    _tileIsMercator(tileIsMercator),
    _tileLevel(tileLevel),
    _canvas(NULL),
    _geoObject(NULL)
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

    void bufferDownloaded(const URL& url,
                          IByteBuffer* buffer);
    void bufferDownloadError(const URL& url);
    void bufferDownloadCanceled();

    void rasterizedGEOObject(const URL& url,
                             GEOObject* geoObject,
                             ICanvas* canvas);
    void deletedRasterizer();

    void imageCreated(const IImage* image,
                      const std::string& imageId);

  };


#ifdef C_CODE
  const TiledVectorLayer* _layer;
#endif
#ifdef JAVA_CODE
  private TiledVectorLayer _layer;
#endif
  IDownloader*            _downloader;
  const IThreadUtils*     _threadUtils;

  std::map<const std::string, ImageAssembler*> _assemblers;

  class GEOObjectHolder : public RCObject {
  public:
    const GEOObject* _geoObject;

    GEOObjectHolder(const GEOObject* geoObject) :
    _geoObject(geoObject)
    {
    }

    ~GEOObjectHolder();

  };

  class CacheEntry {
  public:
    const std::string      _path;
    const GEOObjectHolder* _geoObjectHolder;

    CacheEntry(const std::string& path,
               const GEOObject*   geoObject) :
    _path(path),
    _geoObjectHolder(new GEOObjectHolder(geoObject))
    {
    }

    ~CacheEntry();
  };

  std::list<CacheEntry*> _geoObjectsCache;

protected:
  ~TiledVectorLayerTileImageProvider();

public:

  TiledVectorLayerTileImageProvider(const TiledVectorLayer* layer,
                                    IDownloader*            downloader,
                                    const IThreadUtils*     threadUtils) :
  _layer(layer),
  _downloader(downloader),
  _threadUtils(threadUtils)
  {
  }

  void layerDeleted(const TiledVectorLayer* layer);

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

  const GEOObjectHolder* getGEOObjectFor(const URL& url);
  void takeGEOObjectFor(const URL& url,
                        GEOObject* geoObject);

};

#endif
