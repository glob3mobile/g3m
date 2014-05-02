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


TiledVectorLayerTileImageProvider::GEOJSONBufferParser::~GEOJSONBufferParser() {
  _imageAssembler->deletedParser();

  delete _buffer;
  delete _geoObject;
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferParser::cancel() {
  _imageAssembler = NULL;
  delete _buffer;
  _buffer = NULL;
}


void TiledVectorLayerTileImageProvider::GEOJSONBufferParser::runInBackground(const G3MContext* context) {
  if ((_imageAssembler != NULL) && (_buffer != NULL)) {
    _geoObject = GEOJSONParser::parseJSON(_buffer);
  }
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferParser::onPostExecute(const G3MContext* context) {
  if (_imageAssembler != NULL) {
    _imageAssembler->parsedGEOObject(_geoObject);
  }
  _geoObject = NULL;
}


void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onDownload(const URL& url,
                                                                                  IByteBuffer* buffer,
                                                                                  bool expired) {
  _imageAssembler->bufferDownloaded(buffer);
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onError(const URL& url) {
  _imageAssembler->bufferDownloadError(url);
}

void TiledVectorLayerTileImageProvider::GEOJSONBufferDownloadListener::onCancel(const URL& url) {
  _imageAssembler->bufferDownloadCanceled();
}



TiledVectorLayerTileImageProvider::ImageAssembler::ImageAssembler(TiledVectorLayerTileImageProvider* tileImageProvider,
                                                                  const std::string&                 tileId,
                                                                  const TileImageContribution*       contribution,
                                                                  TileImageListener*                 listener,
                                                                  bool                               deleteListener,
                                                                  const Vector2I&                    imageResolution,
                                                                  IDownloader*                       downloader,
                                                                  const IThreadUtils*                threadUtils) :
_tileImageProvider(tileImageProvider),
_tileId(tileId),
_contribution(contribution),
_listener(listener),
_deleteListener(deleteListener),
_imageResolution(imageResolution),
_downloader(downloader),
_threadUtils(threadUtils),
_canceled(false),
_downloadListener(NULL),
_downloadRequestId(-1),
_parser(NULL),
_symbolizer(NULL)
{

}

void TiledVectorLayerTileImageProvider::ImageAssembler::start(const TiledVectorLayer* layer,
                                                              const Tile*             tile,
                                                              long long               tileDownloadPriority,
                                                              bool                    logDownloadActivity) {
  _downloadListener = new GEOJSONBufferDownloadListener(this);

  _symbolizer = layer->symbolizerCopy();

  _downloadRequestId = layer->requestGEOJSONBuffer(tile,
                                                   _downloader,
                                                   tileDownloadPriority,
                                                   logDownloadActivity,
                                                   _downloadListener,
                                                   true /* deleteListener */);

}

TiledVectorLayerTileImageProvider::ImageAssembler::~ImageAssembler() {
  if (_deleteListener) {
    delete _listener;
  }
  TileImageContribution::deleteContribution(_contribution);
  delete _symbolizer;
}

void TiledVectorLayerTileImageProvider::ImageAssembler::cancel() {
  _canceled = true;
  if (_downloadRequestId >= 0) {
    _downloader->cancelRequest(_downloadRequestId);
    _downloadRequestId = -1;
  }
  if (_parser != NULL) {
    _parser->cancel();
  }

#warning TODO call listener cancel
  _listener->imageCreationCanceled(_tileId);
  _tileImageProvider->requestFinish(_tileId);
}

void TiledVectorLayerTileImageProvider::ImageAssembler::bufferDownloaded(IByteBuffer* buffer) {
  _downloadListener = NULL;
  _downloadRequestId = -1;

  if (!_canceled) {
    _parser = new GEOJSONBufferParser(this, buffer);
    _threadUtils->invokeAsyncTask(_parser,
                                  true);
  }

#warning Diego at work!
}

void TiledVectorLayerTileImageProvider::ImageAssembler::bufferDownloadError(const URL& url) {
  _downloadListener = NULL;
  _downloadRequestId = -1;

  _listener->imageCreationError(_tileId,
                                "Download error - " + url.getPath());
  _tileImageProvider->requestFinish(_tileId);
}

void TiledVectorLayerTileImageProvider::ImageAssembler::bufferDownloadCanceled() {
  _downloadListener = NULL;
  _downloadRequestId = -1;

  // do nothing here, the cancel() method already notified the listener
//  _listener->imageCreationCanceled(_tileId);
//  _tileImageProvider->requestFinish(_tileId);
}

void TiledVectorLayerTileImageProvider::ImageAssembler::parsedGEOObject(GEOObject* geoObject) {
  if (geoObject == NULL) {
    _listener->imageCreationError(_tileId, "GEOJSON parser error");
    if (_deleteListener) {
      delete _listener;
    }
  }
  else {
#warning Diego at work!

    ICanvas* canvas = IFactory::instance()->createCanvas();

    canvas->initialize(_imageResolution._x, _imageResolution._y);

    const GEORasterProjection* projection;
    int tileLevel;
    geoObject->rasterize(_symbolizer, canvas, projection, tileLevel);

//    void rasterize(ICanvas*                   canvas,
//                   const GEORasterProjection* projection,
//                   int tileLevel) const;

    delete canvas;


    ILogger::instance()->logInfo("Parsed geojson");

    _listener->imageCreationError(_tileId,
                                  "NOT YET IMPLEMENTED");

    TileImageContribution::deleteContribution(_contribution);
    _contribution = NULL;

    _tileImageProvider->requestFinish(_tileId);

    delete geoObject;
  }
}

void TiledVectorLayerTileImageProvider::ImageAssembler::deletedParser() {
  _parser = NULL;
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

  ImageAssembler* assembler = new ImageAssembler(this,
                                                 tileId,
                                                 contribution,
                                                 listener,
                                                 deleteListener,
                                                 resolution,
                                                 _downloader,
                                                 _threadUtils);

  _assemblers[tileId] = assembler;

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
    _assembler.cancelRequest();
  }
#endif
}

void TiledVectorLayerTileImageProvider::requestFinish(const std::string& tileId) {
#ifdef C_CODE
  if (_assemblers.find(tileId) != _assemblers.end()) {
    ImageAssembler* assembler = _assemblers[tileId];

    delete assembler;

    _assemblers.erase(tileId);
  }
#endif
#ifdef JAVA_CODE
  final ImageAssembler assembler = _assemblers.remove(tileId);
  if (assembler != null) {
    _assembler.dispose();
  }
#endif
}
