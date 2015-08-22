//
//  MapBoo.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/21/15.
//
//

#include "MapBoo.hpp"

#include "IG3MBuilder.hpp"
#include "PlanetRendererBuilder.hpp"
#include "ChessboardLayer.hpp"
#include "IDownloader.hpp"
#include "IJSONParser.hpp"
#include "JSONArray.hpp"
#include "JSONObject.hpp"
#include "JSONString.hpp"
#include "JSONNumber.hpp"


MapBoo::MapBoo(IG3MBuilder* builder,
               const URL& serverURL) :
_builder(builder),
_serverURL(serverURL),
_layerSet(NULL)
{
  _layerSet = new LayerSet();
  _layerSet->addLayer( new ChessboardLayer() );

  _builder->getPlanetRendererBuilder()->setLayerSet( _layerSet );

  _downloader  = _builder->getDownloader();
  _threadUtils = _builder->getThreadUtils();
}

MapBoo::~MapBoo() {
  delete _builder;
}

void MapBoo::requestMaps(MapsHandler* handler,
                         bool deleteHandler) {

  _downloader->requestBuffer(URL(_serverURL, "/public/map/"),
                             DownloadPriority::HIGHEST,
                             TimeInterval::zero(),
                             false, // readExpired
                             new MapsBufferDownloadListener(handler, deleteHandler, _threadUtils),
                             true);
}

void MapBoo::MapsBufferDownloadListener::onDownload(const URL& url,
                                                    IByteBuffer* buffer,
                                                    bool expired) {
  _threadUtils->invokeAsyncTask(new MapsParserAsyncTask(_handler,
                                                        _deleteHandler,
                                                        buffer),
                                true);
  _handler = NULL; // moves ownership to MapsParserAsyncTask
}

void MapBoo::MapsBufferDownloadListener::onError(const URL& url) {
  _handler->onDownloadError();

  if (_deleteHandler) {
    delete _handler;
    _handler = NULL;
  }
}

MapBoo::MapsBufferDownloadListener::~MapsBufferDownloadListener() {
  if (_deleteHandler && (_handler != NULL)) {
    delete _handler;
  }
}

MapBoo::MapsParserAsyncTask::~MapsParserAsyncTask() {
  delete _buffer;

  for (int i = 0; i < _maps.size(); i++) {
    const MBMap* map = _maps[i];
    delete map;
  }

  if (_deleteHandler && (_handler != NULL)) {
    delete _handler;
  }
}

void MapBoo::MapsParserAsyncTask::runInBackground(const G3MContext* context) {
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);

  delete _buffer; _buffer = NULL; // release some memory

  if (jsonBaseObject != NULL) {
    const JSONArray* jsonArray = jsonBaseObject->asArray();
    if (jsonArray != NULL) {
      _parseError = false;

      for (int i = 0; i < jsonArray->size(); i++) {
        const MBMap* map = MBMap::fromJSON( jsonArray->get(i) );
        if (map == NULL) {
          _parseError = true;
          break;
        }
        _maps.push_back( map );
      }
    }

    delete jsonBaseObject;
  }
}

void MapBoo::MapsParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_parseError) {
    _handler->onParseError();
  }
  else {
    _handler->onMaps(_maps);
    _maps.clear(); // moved maps ownership to _handler
  }
}

const MapBoo::MBMap* MapBoo::MBMap::fromJSON(const JSONBaseObject* jsonBaseObject) {
  if (jsonBaseObject == NULL) {
    return NULL;
  }

  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string                   id          = jsonObject->get("id")->asString()->value();
  const std::string                   name        = jsonObject->get("name")->asString()->value();
  std::vector<const MapBoo::MBLayer*> layers      = parseLayers( jsonObject->get("layerSet")->asArray() );
  std::vector<std::string>            datasetsIDs = parseDatasetsIDs( jsonObject->get("datasets")->asArray() );
  const int                           timestamp   = (int) jsonObject->get("timestamp")->asNumber()->value();

  return new MBMap(id, name, layers, datasetsIDs, timestamp);
}

std::vector<const MapBoo::MBLayer*> MapBoo::MBMap::parseLayers(const JSONArray* jsonArray) {
  std::vector<const MapBoo::MBLayer*> result;
  for (int i = 0; i < jsonArray->size(); i++) {
    const MBLayer* layer = MBLayer::fromJSON( jsonArray->get(i) );
    if (layer != NULL) {
      result.push_back( layer );
    }
  }
  return result;
}

MapBoo::MBMap::~MBMap() {
  for (int i = 0; i < _layers.size(); i++) {
#ifdef C_CODE
    const MBLayer* layer = _layers[i];
    delete layer;
#endif
#ifdef JAVA_CODE
    final MBLayer layer = _layers.get(i);
    if (layer != null)
      layer.dispose();
#endif
  }
}

std::vector<std::string> MapBoo::MBMap::parseDatasetsIDs(const JSONArray* jsonArray) {
  std::vector<std::string> result;
  for (int i = 0; i < jsonArray->size(); i++) {
    result.push_back( jsonArray->get(i)->asString()->value() );
  }
  return result;
}

const MapBoo::MBLayer* MapBoo::MBLayer::fromJSON(const JSONBaseObject* jsonBaseObject) {
  if (jsonBaseObject == NULL) {
    return NULL;
  }

  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string type = jsonObject->get("type")->asString()->value();
  const std::string url  = jsonObject->getAsString("url", "");

  return new MapBoo::MBLayer(type, url);
}

MapBoo::MBLayer::~MBLayer() {
}


void MapBoo::MBLayer::createG3MLayer() {
#warning Diego at work!
}
