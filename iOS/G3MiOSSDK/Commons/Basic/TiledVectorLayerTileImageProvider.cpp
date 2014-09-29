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
#include "GEOJSONParser.hpp"
#include "GEOObject.hpp"
#include "GEORasterSymbolizer.hpp"
#include "IFactory.hpp"
#include "ICanvas.hpp"
#include "GEORasterProjection.hpp"
#include "ErrorHandling.hpp"

TiledVectorLayerTileImageProvider::GEOJSONBufferRasterizer::~GEOJSONBufferRasterizer() {
  if (_imageAssembler != NULL) {
    _imageAssembler->deletedRasterizer();
  }

  delete _symbolizer;
  delete _buffer;
  delete _geoObject;
  if (_geoObjectHolder != NULL) {
    _geoObjectHolder->_release();
  }
  delete _canvas;

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferRasterizer::cancel() {
  _imageAssembler = NULL;
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferRasterizer::rasterizeGEOObject(const GEOObject* geoObject) {
  const long long coordinatesCount = geoObject->getCoordinatesCount();
  if (coordinatesCount > 5000) {
    ILogger::instance()->logWarning("GEOObject for tile=\"%s\" has with too many vertices=%d",
                                    _tileId.c_str(),
                                    coordinatesCount);
  }

  const GEORasterProjection* projection = new GEORasterProjection(_tileSector,
                                                                  _tileIsMercator,
                                                                  _imageWidth,
                                                                  _imageHeight);
  geoObject->rasterize(_symbolizer,
                       _canvas,
                       projection,
                       _tileLevel);

  delete projection;
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferRasterizer::runInBackground(const G3MContext* context) {
  if (_imageAssembler != NULL) {
    _canvas = IFactory::instance()->createCanvas();
    _canvas->initialize(_imageWidth, _imageHeight);

    if (_geoObjectHolder != NULL) {
      rasterizeGEOObject(_geoObjectHolder->_geoObject);
    }
    else if (_buffer != NULL) {
      if (_buffer->size() > 0) {
        const bool isBSON = IStringUtils::instance()->endsWith(_url._path, "bson");
        const bool showStatistics = false;
        _geoObject = (isBSON
                      ? GEOJSONParser::parseBSON(_buffer, showStatistics)
                      : GEOJSONParser::parseJSON(_buffer, showStatistics));
        if (_geoObject != NULL) {
          rasterizeGEOObject(_geoObject);
        }
      }
    }
  }

//  if ((_imageAssembler != NULL) && (_buffer != NULL)) {
//    _canvas = IFactory::instance()->createCanvas();
//    _canvas->initialize(_imageWidth, _imageHeight);
//
//    if (_buffer->size() > 0) {
//      const bool isBSON = IStringUtils::instance()->endsWith(_url._path, "bson");
//
//      if (_geoObject2 == NULL) {
//        const bool showStatistics = false;
//        _geoObject = (isBSON
//                      ? GEOJSONParser::parseBSON(_buffer, showStatistics)
//                      : GEOJSONParser::parseJSON(_buffer, showStatistics));
//      }
//
//      if (_geoObject != NULL) {
//        const long long coordinatesCount = _geoObject->getCoordinatesCount();
//        if (coordinatesCount > 5000) {
//          ILogger::instance()->logWarning("GEOObject for tile=\"%s\" has with too many vertices=%d",
//                                          _tileId.c_str(),
//                                          coordinatesCount
//                                          );
//        }
//
//        if (_imageAssembler != NULL) {
//          const GEORasterProjection* projection = new GEORasterProjection(_tileSector,
//                                                                          _tileIsMercator,
//                                                                          _imageWidth,
//                                                                          _imageHeight);
//          _geoObject->rasterize(_symbolizer,
//                                _canvas,
//                                projection,
//                                _tileLevel);
//
//          delete projection;
//        }
//        // delete geoObject;
//      }
//    }
//  }
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferRasterizer::onPostExecute(const G3MContext* context) {
  if (_imageAssembler != NULL) {
    ICanvas* canvas = _canvas;
    _canvas = NULL;  // moves ownership of _canvas to _imageAssembler

    GEOObject* transferedGEOObject;
    if (_geoObjectFromCache) {
      // This _geoObject had come from the cache, no need to transfer to put into the cache again
      // the _geoObject will be removed in the destructor
      transferedGEOObject = NULL;
    }
    else {
      transferedGEOObject = _geoObject;
      _geoObject = NULL; // moves ownership of _geoObject to _imageAssembler
    }

    _imageAssembler->rasterizedGEOObject(_url,
                                         transferedGEOObject,
                                         canvas);
  }
}


void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onDownload(const URL& url,
                                                                                  IByteBuffer* buffer,
                                                                                  bool expired) {
  if (_imageAssembler == NULL) {
    delete buffer;
  }
  else {
    _imageAssembler->bufferDownloaded(url,
                                      buffer);
  }
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onError(const URL& url) {
  if (_imageAssembler != NULL) {
    _imageAssembler->bufferDownloadError(url);
  }
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onCancel(const URL& url) {
  if (_imageAssembler != NULL) {
    _imageAssembler->bufferDownloadCanceled();
  }
}



TiledVectorLayerTileImageProvider::ImageAssembler::ImageAssembler(TiledVectorLayerTileImageProvider* tileImageProvider,
                                                                  const Tile*                        tile,
                                                                  const TileImageContribution*       contribution,
                                                                  TileImageListener*                 listener,
                                                                  bool                               deleteListener,
                                                                  const Vector2I&                    imageResolution,
                                                                  IDownloader*                       downloader,
                                                                  const IThreadUtils*                threadUtils) :
_tileImageProvider(tileImageProvider),
_tileId(tile->_id),
_tileSector(tile->_sector),
_tileIsMercator(tile->_mercator),
_tileLevel(tile->_level),
_contribution(contribution),
_listener(listener),
_deleteListener(deleteListener),
_imageWidth(imageResolution._x),
_imageHeight(imageResolution._y),
_downloader(downloader),
_threadUtils(threadUtils),
_canceled(false),
_downloadListener(NULL),
_downloadRequestId(-1),
_rasterizer(NULL),
_symbolizer(NULL)
{
}

void TiledVectorLayerTileImageProvider::ImageAssembler::start(const TiledVectorLayer* layer,
                                                              const Tile*             tile,
                                                              long long               tileDownloadPriority,
                                                              bool                    logDownloadActivity) {
//  _downloadListener = new GEOJSONBufferDownloadListener(this);
//
//  _symbolizer = layer->symbolizerCopy();

//  _downloadRequestId = layer->requestGEOJSONBuffer(tile,
//                                                   _downloader,
//                                                   tileDownloadPriority,
//                                                   logDownloadActivity,
//                                                   _downloadListener,
//                                                   true /* deleteListener */);


  TiledVectorLayer::RequestGEOJSONBufferData* requestData = layer->getRequestGEOJSONBufferData(tile);

//  GEOObject* geoObject = _tileImageProvider->getGEOObjectFor(requestData->_url);
  const GEOObjectHolder* geoObjectHolder = _tileImageProvider->getGEOObjectFor(requestData->_url);
  if (geoObjectHolder == NULL) {
    _symbolizer = layer->symbolizerCopy();
    _downloadListener = new GEOJSONBufferDownloadListener(this);

    if (logDownloadActivity) {
      ILogger::instance()->logInfo("Downloading %s", requestData->_url._path.c_str());
    }
    _downloadRequestId = _downloader->requestBuffer(requestData->_url,
                                                    tileDownloadPriority,
                                                    requestData->_timeToCache,
                                                    requestData->_readExpired,
                                                    _downloadListener,
                                                    true /* deleteListener */);
  }
  else {
//    geoObjectDownloaded(geoObject,
//                        _symbolizer);
//    aa

    const GEORasterSymbolizer* symbolizer = layer->symbolizerCopy();

    _rasterizer = new GEOJSONBufferRasterizer(this,
                                              requestData->_url,
                                              NULL, // buffer,
                                              geoObjectHolder,
                                              _imageWidth,
                                              _imageHeight,
                                              symbolizer,
                                              _tileId,
                                              _tileSector,
                                              _tileIsMercator,
                                              _tileLevel);
    _threadUtils->invokeAsyncTask(_rasterizer,
                                  true);
  }

  delete requestData;
}

TiledVectorLayerTileImageProvider::ImageAssembler::~ImageAssembler() {
  if (_downloadListener != NULL) {
    _downloadListener->deletedImageAssembler();
  }
  if (_rasterizer != NULL) {
    _rasterizer->deletedImageAssembler();
  }
  if (_deleteListener) {
    delete _listener;
  }
  TileImageContribution::releaseContribution(_contribution);
  delete _symbolizer;
}

void TiledVectorLayerTileImageProvider::ImageAssembler::cancel() {
  _canceled = true;
  if (_downloadRequestId >= 0) {
    _downloader->cancelRequest(_downloadRequestId);
    _downloadRequestId = -1;
  }
  if (_rasterizer != NULL) {
    _rasterizer->cancel();
  }

  _listener->imageCreationCanceled(_tileId);
  _tileImageProvider->requestFinish(_tileId);
}


void TiledVectorLayerTileImageProvider::ImageAssembler::bufferDownloaded(const URL& url,
                                                                         IByteBuffer* buffer) {
  _downloadListener = NULL;
  _downloadRequestId = -1;

  if (_canceled) {
    delete buffer;
  }
  else {
    const GEORasterSymbolizer* symbolizer = _symbolizer;
    _symbolizer = NULL; // moves ownership of _symbolizer to GEOJSONBufferRasterizer

    _rasterizer = new GEOJSONBufferRasterizer(this,
                                              url,
                                              buffer,
                                              NULL, // geoObject
                                              _imageWidth,
                                              _imageHeight,
                                              symbolizer,
                                              _tileId,
                                              _tileSector,
                                              _tileIsMercator,
                                              _tileLevel);
    _threadUtils->invokeAsyncTask(_rasterizer,
                                  true);
  }
}

void TiledVectorLayerTileImageProvider::ImageAssembler::bufferDownloadError(const URL& url) {
  _downloadListener = NULL;
  _downloadRequestId = -1;

  _listener->imageCreationError(_tileId,
                                "Download error - " + url._path);
  _tileImageProvider->requestFinish(_tileId);
}

void TiledVectorLayerTileImageProvider::ImageAssembler::bufferDownloadCanceled() {
  _downloadListener = NULL;
  _downloadRequestId = -1;
}

void TiledVectorLayerTileImageProvider::CanvasImageListener::imageCreated(const IImage* image) {
  _imageAssembler->imageCreated(image,
                                _imageId);
}

void TiledVectorLayerTileImageProvider::ImageAssembler::imageCreated(const IImage* image,
                                                                     const std::string& imageId) {
  // retain the _contribution before calling the listener, as it takes full ownership of the contribution
  TileImageContribution::retainContribution(_contribution);

  _listener->imageCreated(_tileId,
                          image,
                          imageId,
                          _contribution);
  _tileImageProvider->requestFinish(_tileId);
}

void TiledVectorLayerTileImageProvider::ImageAssembler::rasterizedGEOObject(const URL& url,
                                                                            GEOObject* geoObject,
                                                                            ICanvas* canvas) {

  if (geoObject != NULL) {
    _tileImageProvider->takeGEOObjectFor(url,
                                         geoObject);
  }

  if (canvas == NULL) {
    _listener->imageCreationError(_tileId, "GEOJSON parser error");
    if (_deleteListener) {
      delete _listener;
    }
  }
  else {
    canvas->createImage(new CanvasImageListener(this, url._path),
                        true /* autodelete */);

    delete canvas;
  }
}

void TiledVectorLayerTileImageProvider::ImageAssembler::deletedRasterizer() {
  _rasterizer = NULL;
}

const TileImageContribution* TiledVectorLayerTileImageProvider::contribution(const Tile* tile) {
  return (_layer == NULL) ? NULL : _layer->contribution(tile);
}

void TiledVectorLayerTileImageProvider::create(const Tile* tile,
                                               const TileImageContribution* contribution,
                                               const Vector2I& resolution,
                                               long long tileDownloadPriority,
                                               bool logDownloadActivity,
                                               TileImageListener* listener,
                                               bool deleteListener,
                                               FrameTasksExecutor* frameTasksExecutor) {

  ImageAssembler* assembler = new ImageAssembler(this,
                                                 tile,
                                                 contribution,
                                                 listener,
                                                 deleteListener,
                                                 resolution,
                                                 _downloader,
                                                 _threadUtils);

  _assemblers[tile->_id] = assembler;

  assembler->start(_layer,
                   tile,
                   tileDownloadPriority,
                   logDownloadActivity);
}

void TiledVectorLayerTileImageProvider::cancel(const std::string& tileId) {
#ifdef C_CODE
  if (_assemblers.find(tileId) != _assemblers.end()) {
    ImageAssembler* assembler = _assemblers[tileId];

    assembler->cancel();
  }
#endif
#ifdef JAVA_CODE
  final ImageAssembler assembler = _assemblers.get(tileId);
  if (assembler != null) {
    assembler.cancel();
  }
#endif
}

void TiledVectorLayerTileImageProvider::requestFinish(const std::string& tileId) {
#ifdef C_CODE
  if (_assemblers.find(tileId) != _assemblers.end()) {
    ImageAssembler* assembler = _assemblers[tileId];

    _assemblers.erase(tileId);

    delete assembler;
  }
#endif
#ifdef JAVA_CODE
  final ImageAssembler assembler = _assemblers.remove(tileId);
  if (assembler != null) {
    assembler.dispose();
  }
#endif
}

TiledVectorLayerTileImageProvider::CacheEntry::~CacheEntry() {
  _geoObjectHolder->_release();
}

TiledVectorLayerTileImageProvider::~TiledVectorLayerTileImageProvider() {

  for (std::list<CacheEntry*>::iterator it = _geoObjectsCache.begin();
       it != _geoObjectsCache.end();
       ++it) {
    CacheEntry* entry = *it;
    delete entry;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

TiledVectorLayerTileImageProvider::GEOObjectHolder::~GEOObjectHolder() {
  delete _geoObject;
}

void TiledVectorLayerTileImageProvider::takeGEOObjectFor(const URL& url,
                                                         GEOObject* geoObject) {
  if (_geoObjectsCache.size() > 48) {
    CacheEntry* lastEntry = _geoObjectsCache.back();
    _geoObjectsCache.pop_back();
    delete lastEntry;
  }
  _geoObjectsCache.push_front(new CacheEntry(url._path,
                                             geoObject));
}

const TiledVectorLayerTileImageProvider::GEOObjectHolder* TiledVectorLayerTileImageProvider::getGEOObjectFor(const URL& url) {
//  _geoObjectsCacheRequests++;
  const std::string path = url._path;
  for (std::list<CacheEntry*>::iterator it = _geoObjectsCache.begin();
       it != _geoObjectsCache.end();
       ++it) {
    CacheEntry* entry = *it;
    if (entry->_path == path) {
//      _geoObjectsCacheHits++;

      // move hit entry to the top of the cache (LRU rules)
#ifdef C_CODE
      it = _geoObjectsCache.erase(it);
#endif
#ifdef JAVA_CODE
      it.remove();
#endif
      _geoObjectsCache.push_front(entry);

      const GEOObjectHolder* geoObjectHolder = entry->_geoObjectHolder;
      geoObjectHolder->_retain();
      return geoObjectHolder;
    }
  }
  return NULL;
}

void TiledVectorLayerTileImageProvider::layerDeleted(const TiledVectorLayer* layer) {
  if (layer != _layer) {
    THROW_EXCEPTION("Logic error");
  }
  _layer = NULL;
}
