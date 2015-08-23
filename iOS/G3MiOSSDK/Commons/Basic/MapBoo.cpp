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
#include "URLTemplateLayer.hpp"
#include "VectorStreamingRenderer.hpp"
#include "MarksRenderer.hpp"
#include "GEOFeature.hpp"
#include "GEO2DPointGeometry.hpp"
#include "Mark.hpp"


MapBoo::MapBoo(IG3MBuilder* builder,
               const URL&   serverURL,
               MBHandler*   handler) :
_builder(builder),
_serverURL(serverURL),
_handler(handler),
_layerSet(NULL)
{
  _layerSet = new LayerSet();
  _layerSet->addLayer( new ChessboardLayer() );

  _builder->getPlanetRendererBuilder()->setLayerSet( _layerSet );

  _markRenderer = new MarksRenderer(false, // readyWhenMarksReady
                                    true, // renderInReverse
                                    true // progressiveInitialization
                                    );
  _builder->addRenderer(_markRenderer);

  _vectorStreamingRenderer = new VectorStreamingRenderer(_markRenderer);
  _builder->addRenderer(_vectorStreamingRenderer);

  _downloader  = _builder->getDownloader();
  _threadUtils = _builder->getThreadUtils();
}

MapBoo::~MapBoo() {
  delete _builder;
  delete _handler;
}

void MapBoo::requestMaps(MBMapsHandler* handler,
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
    MBMap* map = _maps[i];
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
        MBMap* map = MBMap::fromJSON( jsonArray->get(i) );
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

MapBoo::MBMap* MapBoo::MBMap::fromJSON(const JSONBaseObject* jsonBaseObject) {
  if (jsonBaseObject == NULL) {
    return NULL;
  }

  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string               id        = jsonObject->get("id")->asString()->value();
  const std::string               name      = jsonObject->get("name")->asString()->value();
  std::vector<MapBoo::MBLayer*>   layers    = parseLayers( jsonObject->get("layerSet")->asArray() );
  std::vector<MapBoo::MBDataset*> datasets  = parseDatasets( jsonObject->get("datasets")->asArray() );
  const int                       timestamp = (int) jsonObject->get("timestamp")->asNumber()->value();

  return new MBMap(id, name, layers, datasets, timestamp);
}

std::vector<MapBoo::MBLayer*> MapBoo::MBMap::parseLayers(const JSONArray* jsonArray) {
  std::vector<MapBoo::MBLayer*> result;
  for (int i = 0; i < jsonArray->size(); i++) {
    MBLayer* layer = MBLayer::fromJSON( jsonArray->get(i) );
    if (layer != NULL) {
      result.push_back( layer );
    }
  }
  return result;
}

MapBoo::MBMap::~MBMap() {
  for (int i = 0; i < _datasets.size(); i++) {
    MBDataset* dataset = _datasets[i];
    delete dataset;
  }

  for (int i = 0; i < _layers.size(); i++) {
    MBLayer* layer = _layers[i];
    delete layer;
  }
}

std::vector<MapBoo::MBDataset*> MapBoo::MBMap::parseDatasets(const JSONArray* jsonArray) {
  std::vector<MapBoo::MBDataset*> result;
  for (int i = 0; i < jsonArray->size(); i++) {
    MBDataset* dataset = MBDataset::fromJSON( jsonArray->get(i) );
    if (dataset != NULL) {
      result.push_back( dataset );
    }
  }
  return result;
}

MapBoo::MBLayer* MapBoo::MBLayer::fromJSON(const JSONBaseObject* jsonBaseObject) {
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


void MapBoo::MBLayer::apply(LayerSet* layerSet) const {
  if (_type == "URLTemplate") {
    URLTemplateLayer* layer = URLTemplateLayer::newMercator(_url,
                                                            Sector::fullSphere(),
                                                            false,                // isTransparent
                                                            1,                    // firstLevel
                                                            18,                   // maxLevel
                                                            TimeInterval::fromDays(30));

    layerSet->addLayer(layer);
  }
  else {
    ILogger::instance()->logError("MapBoo::MBLayer: unknown type \"%s\"", _type.c_str());
  }
}


void MapBoo::requestMap() {
  _downloader->requestBuffer(URL(_serverURL, "/public/map/" + _mapID),
                             DownloadPriority::HIGHEST,
                             TimeInterval::zero(),
                             false, // readExpired
                             new MapBufferDownloadListener(this, _threadUtils),
                             true);
}

void MapBoo::setMapID(const std::string& mapID) {
  if (_mapID != mapID) {
    _mapID = mapID;
    requestMap();
  }
}

void MapBoo::setMap(MapBoo::MBMap* map) {
  const std::string mapID = map->getID();
  if (_mapID != mapID) {
    _mapID = mapID;

    applyMap(map);
  }
}

void MapBoo::applyMap(MapBoo::MBMap* map) {
  // clean current map
  _vectorStreamingRenderer->removeAllVectorSets();
  _layerSet->removeAllLayers(true);

  map->apply(_serverURL, _layerSet, _vectorStreamingRenderer);

  // just in case nobody put a layer
  if (_layerSet->size() == 0) {
    _layerSet->addLayer( new ChessboardLayer() );
  }

  if (_handler != NULL) {
    _handler->onSelectedMap(map);
  }

  delete map;
}


void MapBoo::MBMap::apply(const URL&               serverURL,
                          LayerSet*                layerSet,
                          VectorStreamingRenderer* vectorStreamingRenderer) {
  for (int i = 0; i < _layers.size(); i++) {
    MBLayer* layer = _layers[i];
    layer->apply(layerSet);
  }

  for (int i = 0; i < _datasets.size(); i++) {
    MBDataset* dataset = _datasets[i];
    dataset->apply(serverURL, vectorStreamingRenderer);
  }
}

void MapBoo::MapBufferDownloadListener::onDownload(const URL& url,
                                                   IByteBuffer* buffer,
                                                   bool expired) {
  _threadUtils->invokeAsyncTask(new MapParserAsyncTask(_mapboo, buffer),
                                true);
}

void MapBoo::MapBufferDownloadListener::onError(const URL& url) {
  _mapboo->onMapDownloadError();
}

MapBoo::MapParserAsyncTask::~MapParserAsyncTask() {
  delete _buffer;
  delete _map;
}

void MapBoo::MapParserAsyncTask::runInBackground(const G3MContext* context) {
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(_buffer);

  delete _buffer; _buffer = NULL; // release some memory

  _map = MBMap::fromJSON( jsonBaseObject );

  delete jsonBaseObject;
}

void MapBoo::MapParserAsyncTask::onPostExecute(const G3MContext* context) {
  if (_map == NULL) {
    _mapboo->onMapParseError();
  }
  else {
    _mapboo->onMap(_map);
    _map = NULL; // moved ownership to _mapboo
  }
}

void MapBoo::onMapDownloadError() {
  if (_handler != NULL) {
    _handler->onMapDownloadError();
  }
}

void MapBoo::onMapParseError() {
  if (_handler != NULL) {
    _handler->onMapParseError();
  }
}

void MapBoo::onMap(MapBoo::MBMap* map) {
  applyMap(map);
}

MapBoo::MBDataset* MapBoo::MBDataset::fromJSON(const JSONBaseObject* jsonBaseObject) {
  if (jsonBaseObject == NULL) {
    return NULL;
  }

  const JSONObject* jsonObject = jsonBaseObject->asObject();
  if (jsonObject == NULL) {
    return NULL;
  }

  const std::string        id               = jsonObject->get("id")->asString()->value();
  const std::string        name             = jsonObject->get("name")->asString()->value();
  std::vector<std::string> labelingCriteria = jsonObject->getAsArray("labelingCriteria")->asStringVector();
  std::vector<std::string> infoCriteria     = jsonObject->getAsArray("infoCriteria")->asStringVector();
  const int                timestamp        = (int) jsonObject->get("timestamp")->asNumber()->value();

  return new MBDataset(id,
                       name,
                       labelingCriteria,
                       infoCriteria,
                       timestamp);
}

MapBoo::MBDataset::~MBDataset() {

}


class XXXVectorSetSymbolizer : public VectorStreamingRenderer::VectorSetSymbolizer {
public:
  Mark* createMark(const GEO2DPointGeometry* geometry) const {
    const GEOFeature* feature = geometry->getFeature();

    const JSONObject* properties = feature->getProperties();

    const std::string label = properties->getAsString("name")->value();
    const Geodetic3D  position( geometry->getPosition(), 0);

    //    double maxPopulation = 22315474;
    //    double population = properties->getAsNumber("population")->value();
    //    float labelFontSize = (float) (14.0 * (population / maxPopulation) + 16.0) ;

    float labelFontSize = 18;

    Mark* mark = new Mark(label,
                          position,
                          ABSOLUTE,
                          0, // minDistanceToCamera
                          labelFontSize
                          // Color::newFromRGBA(1, 1, 0, 1)
                          );
    mark->setZoomInAppears(true);
    return mark;
  }
};


void MapBoo::MBDataset::apply(const URL&               serverURL,
                              VectorStreamingRenderer* vectorStreamingRenderer) const {
  std::string properties = "";
  for (int i = 0; i < _labelingCriteria.size(); i++) {
    properties += _labelingCriteria[i] + "|";
  }
  for (int i = 0; i < _infoCriteria.size(); i++) {
    properties += _infoCriteria[i] + "|";
  }

  vectorStreamingRenderer->addVectorSet(URL(serverURL, "/public/VectorialStreaming/"),
                                        _id,
                                        properties,
                                        new XXXVectorSetSymbolizer(),
                                        true,  // deleteSymbolizer
                                        DownloadPriority::HIGHER,
                                        TimeInterval::zero(),
                                        true,  // readExpired
                                        true,  // verbose
                                        false  // haltOnError
                                        );
}
