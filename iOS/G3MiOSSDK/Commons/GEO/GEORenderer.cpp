//
//  GEORenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#include "GEORenderer.hpp"

#include "GEOObject.hpp"
#include "GEOSymbolizer.hpp"
#include "ILogger.hpp"
#include "Context.hpp"
#include "Camera.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "GEOJSONParser.hpp"
#include "IThreadUtils.hpp"
#include "MeshRenderer.hpp"
#include "ShapesRenderer.hpp"
#include "MarksRenderer.hpp"

class GEORenderer_ObjectSymbolizerPair {
public:
  const GEOObject*     _geoObject;
  const GEOSymbolizer* _symbolizer;

  GEORenderer_ObjectSymbolizerPair(GEOObject* geoObject,
                                   GEOSymbolizer* symbolizer) :
  _geoObject(geoObject),
  _symbolizer(symbolizer)
  {

  }

  ~GEORenderer_ObjectSymbolizerPair() {
    delete _geoObject;
    delete _symbolizer;
  }
};


GEORenderer::~GEORenderer() {
  delete _defaultSymbolizer;

  const int childrenCount = _children.size();
  for (int i = 0; i < childrenCount; i++) {
    GEORenderer_ObjectSymbolizerPair* pair = _children[i];
    delete pair;
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GEORenderer::addGEOObject(GEOObject* geoObject,
                               GEOSymbolizer* symbolizer) {
  if ( (symbolizer == NULL) && (_defaultSymbolizer == NULL) ) {
    ILogger::instance()->logError("Can't add a geoObject without a symbolizer if the defaultSymbolizer was not given in the GEORenderer constructor");
    delete geoObject;
  }
  else {
    _children.push_back( new GEORenderer_ObjectSymbolizerPair(geoObject, symbolizer) );
  }
}

void GEORenderer::render(const G3MRenderContext* rc, GLState* glState) {
  const int childrenCount = _children.size();
  if (childrenCount > 0) {
    for (int i = 0; i < childrenCount; i++) {
      const GEORenderer_ObjectSymbolizerPair* pair = _children[i];

      if (pair->_geoObject != NULL) {
        const GEOSymbolizer* symbolizer = (pair->_symbolizer == NULL) ? _defaultSymbolizer : pair->_symbolizer;

        pair->_geoObject->symbolize(rc,
                                    symbolizer,
                                    _meshRenderer,
                                    _shapesRenderer,
                                    _marksRenderer,
                                    _geoTileRasterizer);
      }

      delete pair;
    }
    _children.clear();
  }
}


class GEORenderer_GEOObjectParserAsyncTask : public GAsyncTask {
private:
#ifdef C_CODE
  const URL          _url;
#endif
#ifdef JAVA_CODE
  public final URL _url;
#endif

  IByteBuffer*   _buffer;
  GEORenderer*   _geoRenderer;
  GEOSymbolizer* _symbolizer;

  const bool _isBSON;

  GEOObject* _geoObject;

public:
  GEORenderer_GEOObjectParserAsyncTask(const URL& url,
                                       IByteBuffer* buffer,
                                       GEORenderer* geoRenderer,
                                       GEOSymbolizer* symbolizer,
                                       bool isBSON) :
  _url(url),
  _buffer(buffer),
  _geoRenderer(geoRenderer),
  _symbolizer(symbolizer),
  _isBSON(isBSON),
  _geoObject(NULL)
  {
  }

  ~GEORenderer_GEOObjectParserAsyncTask() {
    delete _buffer;
//    delete _geoObject;
  }

  void runInBackground(const G3MContext* context) {
//    ILogger::instance()->logInfo("Parsing GEOObject buffer from \"%s\" (%db)",
//                                 _url.getPath().c_str(),
//                                 _buffer->size());

    if (_isBSON) {
      _geoObject = GEOJSONParser::parseBSON(_buffer);
    }
    else {
      _geoObject = GEOJSONParser::parseJSON(_buffer);
    }

    delete _buffer;
    _buffer = NULL;
  }

  void onPostExecute(const G3MContext* context) {
    if (_geoObject == NULL) {
      ILogger::instance()->logError("Error parsing GEOJSON from \"%s\"", _url.getPath().c_str());
    }
    else {
//      ILogger::instance()->logInfo("Adding GEOObject to _geoRenderer");
      _geoRenderer->addGEOObject(_geoObject, _symbolizer);
      _geoObject = NULL;
    }
  }
};


class GEORenderer_GEOObjectBufferDownloadListener : public IBufferDownloadListener {
private:
  GEORenderer*        _geoRenderer;
  GEOSymbolizer*      _symbolizer;
  const IThreadUtils* _threadUtils;
  const bool          _isBSON;

public:
  GEORenderer_GEOObjectBufferDownloadListener(GEORenderer* geoRenderer,
                                              GEOSymbolizer* symbolizer,
                                              const IThreadUtils* threadUtils,
                                              bool isBSON) :
  _geoRenderer(geoRenderer),
  _symbolizer(symbolizer),
  _threadUtils(threadUtils),
  _isBSON(isBSON)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    ILogger::instance()->logInfo("Downloaded GEOObject buffer from \"%s\" (%db)",
                                 url.getPath().c_str(),
                                 buffer->size());

    _threadUtils->invokeAsyncTask(new GEORenderer_GEOObjectParserAsyncTask(url,
                                                                           buffer,
                                                                           _geoRenderer,
                                                                           _symbolizer,
                                                                           _isBSON),
                                  true);
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Error downloading \"%s\"", url.getPath().c_str());
  }

  void onCancel(const URL& url) {
    ILogger::instance()->logInfo("Canceled download of \"%s\"", url.getPath().c_str());
  }

  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
};

void GEORenderer::requestBuffer(const URL& url,
                                GEOSymbolizer* symbolizer,
                                long long priority,
                                const TimeInterval& timeToCache,
                                bool readExpired,
                                bool isBSON) {
//  ILogger::instance()->logInfo("Requesting GEOObject from \"%s\"", url.getPath().c_str());
  IDownloader* downloader = _context->getDownloader();
  downloader->requestBuffer(url,
                            priority,
                            timeToCache,
                            readExpired,
                            new GEORenderer_GEOObjectBufferDownloadListener(this,
                                                                            symbolizer,
                                                                            _context->getThreadUtils(),
                                                                            isBSON),
                            true);
}

void GEORenderer::drainLoadQueue() {
  const int loadQueueSize = _loadQueue.size();
  for (int i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    requestBuffer(item->_url,
                  item->_symbolizer,
                  item->_priority,
                  item->_timeToCache,
                  item->_readExpired,
                  item->_isBSON);
    delete item;
  }

  _loadQueue.clear();
}

void GEORenderer::cleanLoadQueue() {
  const int loadQueueSize = _loadQueue.size();
  for (int i = 0; i < loadQueueSize; i++) {
    LoadQueueItem* item = _loadQueue[i];
    delete item;
  }
  
  _loadQueue.clear();
}

void GEORenderer::onChangedContext() {
  if (_context != NULL) {
    drainLoadQueue();
  }
}

void GEORenderer::onLostContext() {
  if (_context == NULL) {
    cleanLoadQueue();
  }
}

void GEORenderer::loadJSON(const URL& url,
                           GEOSymbolizer* symbolizer,
                           long long priority,
                           const TimeInterval& timeToCache,
                           bool readExpired) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           symbolizer,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           false /* isBson */));
  }
  else {
    requestBuffer(url,
                  symbolizer,
                  priority,
                  timeToCache,
                  readExpired,
                  false /* isBson */);
  }
}

void GEORenderer::loadBSON(const URL& url,
                           GEOSymbolizer* symbolizer,
                           long long priority,
                           const TimeInterval& timeToCache,
                           bool readExpired) {
  if (_context == NULL) {
    _loadQueue.push_back(new LoadQueueItem(url,
                                           symbolizer,
                                           priority,
                                           timeToCache,
                                           readExpired,
                                           true /* isBson */));
  }
  else {
    requestBuffer(url,
                  symbolizer,
                  priority,
                  timeToCache,
                  readExpired,
                  true /* isBson */);
  }
}

void GEORenderer::setEnable(bool enable) {
  DefaultRenderer::setEnable(enable);

  if (_meshRenderer) {
    _meshRenderer->setEnable(enable);
  }
  if (_shapesRenderer) {
    _shapesRenderer->setEnable(enable);
  }
  if (_marksRenderer) {
    _marksRenderer->setEnable(enable);
  }
  if (_geoTileRasterizer) {
    _geoTileRasterizer->setEnable(enable);
  }
}
