//
//  G3MCBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "G3MCBuilder.hpp"

#include "ILogger.hpp"
#include "CompositeRenderer.hpp"
#include "TileRenderer.hpp"

#include "EllipsoidalTileTessellator.hpp"
#include "MultiLayerTileTexturizer.hpp"
#include "TilesRenderParameters.hpp"
#include "DownloadPriority.hpp"
#include "G3MWidget.hpp"
#include "SimpleCameraConstrainer.hpp"
#include "CameraRenderer.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "BusyMeshRenderer.hpp"
#include "GInitializationTask.hpp"
#include "PeriodicalTask.hpp"
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "IJSONParser.hpp"
#include "JSONObject.hpp"
#include "JSONString.hpp"
#include "JSONArray.hpp"
#include "G3MCSceneDescription.hpp"
#include "IThreadUtils.hpp"
#include "Color.hpp"
#include "OSMLayer.hpp"
#include "MapQuestLayer.hpp"
#include "BingMapsLayer.hpp"
#include "CartoDBLayer.hpp"
#include "MapBoxLayer.hpp"
#include "WMSLayer.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "IWebSocketListener.hpp"
#include "IWebSocket.hpp"

G3MCBuilder::G3MCBuilder(const URL& serverURL,
                         const URL& tubesURL,
                         bool useWebSockets,
                         const std::string& sceneId,
                         G3MCSceneChangeListener* sceneListener) :
_serverURL(serverURL),
_tubesURL(tubesURL),
_useWebSockets(useWebSockets),
_sceneId(sceneId),
_sceneTimestamp(-1),
_sceneBaseLayer(NULL),
_sceneOverlayLayer(NULL),
_sceneUser(""),
_sceneName(""),
_sceneDescription(""),
_sceneBackgroundColor( Color::newFromRGBA(0, 0, 0, 1) ),
_gl(NULL),
_g3mWidget(NULL),
_storage(NULL),
_threadUtils(NULL),
_layerSet( new LayerSet() ),
_downloader(NULL),
_sceneListener(sceneListener),
_gpuProgramManager(NULL),
_isSceneTubeOpen(false),
_sceneTubeWebSocket(NULL)
{
  
}

GPUProgramManager* G3MCBuilder::getGPUProgramManager() {
  if (_gpuProgramManager == NULL) {
    _gpuProgramManager = createGPUProgramManager();
  }
  return _gpuProgramManager;
}

IDownloader* G3MCBuilder::getDownloader() {
  if (_downloader == NULL) {
    _downloader = createDownloader();
  }
  return _downloader;
}

IThreadUtils* G3MCBuilder::getThreadUtils() {
  if (_threadUtils == NULL) {
    _threadUtils = createThreadUtils();
  }
  return _threadUtils;
}

void G3MCBuilder::setGL(GL *gl) {
  if (_gl) {
    ILogger::instance()->logError("LOGIC ERROR: _gl already initialized");
    return;
  }
  if (!gl) {
    ILogger::instance()->logError("LOGIC ERROR: _gl cannot be NULL");
    return;
  }
  _gl = gl;
}

GL* G3MCBuilder::getGL() {
  if (!_gl) {
    ILogger::instance()->logError("Logic Error: _gl not initialized");
  }
  
  return _gl;
}

TileRenderer* G3MCBuilder::createTileRenderer() {
  const TileTessellator* tessellator = new EllipsoidalTileTessellator(true);
  
  ElevationDataProvider* elevationDataProvider = NULL;
  const float verticalExaggeration = 1;
  TileTexturizer* texturizer = new MultiLayerTileTexturizer();
  TileRasterizer* tileRasterizer = NULL;

  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;
  const bool renderIncompletePlanet = false;
  const URL incompletePlanetTexureURL("", false);
  
  const TilesRenderParameters* parameters = new TilesRenderParameters(renderDebug,
                                                                      useTilesSplitBudget,
                                                                      forceFirstLevelTilesRenderOnStart,
                                                                      incrementalTileQuality,
                                                                      renderIncompletePlanet,
                                                                      incompletePlanetTexureURL);
  
  const bool showStatistics = false;
  long long texturePriority = DownloadPriority::HIGHER;
  
  return new TileRenderer(tessellator,
                          elevationDataProvider,
                          verticalExaggeration,
                          texturizer,
                          tileRasterizer,
                          _layerSet,
                          parameters,
                          showStatistics,
                          texturePriority);
}

const Planet* G3MCBuilder::createPlanet() {
  return Planet::createEarth();
}

std::vector<ICameraConstrainer*>* G3MCBuilder::createCameraConstraints() {
  std::vector<ICameraConstrainer*>* cameraConstraints = new std::vector<ICameraConstrainer*>;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints->push_back(scc);
  
  return cameraConstraints;
}

CameraRenderer* G3MCBuilder::createCameraRenderer() {
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());
  
  return cameraRenderer;
}

Renderer* G3MCBuilder::createBusyRenderer() {
  return new BusyMeshRenderer(Color::newFromRGBA(0, 0, 0, 1));
}

MapQuestLayer* G3MCBuilder::parseMapQuestLayer(const JSONObject* jsonBaseLayer,
                                               const TimeInterval& timeToCache) const {
  const std::string imagery = jsonBaseLayer->getAsString("imagery", "<imagery not present>");
  if (imagery.compare("OpenAerial") == 0) {
    return MapQuestLayer::newOpenAerial(timeToCache);
  }

  // defaults to OSM
  return MapQuestLayer::newOSM(timeToCache);
}

BingMapsLayer* G3MCBuilder::parseBingMapsLayer(const JSONObject* jsonBaseLayer,
                                               const TimeInterval& timeToCache) const {
  const std::string key = jsonBaseLayer->getAsString("key", "");
  const std::string imagerySet = jsonBaseLayer->getAsString("imagerySet", "Aerial");

  return new BingMapsLayer(imagerySet, key, timeToCache);
}

CartoDBLayer* G3MCBuilder::parseCartoDBLayer(const JSONObject* jsonBaseLayer,
                                             const TimeInterval& timeToCache) const {
  const std::string userName = jsonBaseLayer->getAsString("userName", "");
  const std::string table    = jsonBaseLayer->getAsString("table",    "");

  return new CartoDBLayer(userName, table, timeToCache);
}

MapBoxLayer* G3MCBuilder::parseMapBoxLayer(const JSONObject* jsonBaseLayer,
                                           const TimeInterval& timeToCache) const {
  const std::string mapKey = jsonBaseLayer->getAsString("mapKey", "");

  return new MapBoxLayer(mapKey, timeToCache);
}

WMSLayer* G3MCBuilder::parseWMSLayer(const JSONObject* jsonBaseLayer) const {

  const std::string mapLayer = jsonBaseLayer->getAsString("layerName", "");
  const URL mapServerURL = URL(jsonBaseLayer->getAsString("server", ""), false);
  const std::string versionStr = jsonBaseLayer->getAsString("version", "");
  WMSServerVersion mapServerVersion = WMS_1_1_0;
  if (versionStr.compare("WMS_1_3_0") == 0) {
    mapServerVersion = WMS_1_3_0;
  }
  const std::string queryLayer = jsonBaseLayer->getAsString("queryLayer", "");
  const std::string style = jsonBaseLayer->getAsString("style", "");
  const URL queryServerURL = URL("", false);
  const WMSServerVersion queryServerVersion = mapServerVersion;
  const double lowerLat = jsonBaseLayer->getAsNumber("lowerLat", -90.0);
  const double lowerLon = jsonBaseLayer->getAsNumber("lowerLon", -180.0);
  const double upperLat = jsonBaseLayer->getAsNumber("upperLat", 90.0);
  const double upperLon = jsonBaseLayer->getAsNumber("upperLon", 180.0);
  const Sector sector = Sector(Geodetic2D(Angle::fromDegrees(lowerLat), Angle::fromDegrees(lowerLon)),
                               Geodetic2D(Angle::fromDegrees(upperLat), Angle::fromDegrees(upperLon)));
  std::string imageFormat = jsonBaseLayer->getAsString("imageFormat", "image/png");
  if (imageFormat.compare("JPG") == 0) {
    imageFormat = "image/jpeg";
  }
  const std::string srs = jsonBaseLayer->getAsString("projection", "EPSG_4326");
  LayerTilesRenderParameters* layerTilesRenderParameters = NULL;
  if (srs.compare("EPSG_4326") == 0) {
    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultNonMercator(Sector::fullSphere());
  }
  else if (srs.compare("EPSG_900913") == 0) {
    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultMercator(0, 17);
  }
  const bool isTransparent = jsonBaseLayer->getAsBoolean("transparent", false);
  const double expiration = jsonBaseLayer->getAsNumber("expiration", 0);
  const long long milliseconds = IMathUtils::instance()->round(expiration);
  const TimeInterval timeToCache = TimeInterval::fromMilliseconds(milliseconds);
  const bool readExpired = jsonBaseLayer->getAsBoolean("acceptExpiration", false);

  return new WMSLayer(mapLayer,
                      mapServerURL,
                      mapServerVersion,
                      queryLayer,
                      queryServerURL,
                      queryServerVersion,
                      sector,
                      imageFormat,
                      (srs.compare("EPSG_4326") == 0) ? "EPSG:4326" : "EPSG:900913",
                      style,
                      isTransparent,
                      NULL,
                      timeToCache,
                      readExpired,
                      layerTilesRenderParameters);
}


Layer* G3MCBuilder::parseLayer(const JSONBaseObject* jsonBaseObjectLayer) const {

  if (jsonBaseObjectLayer->asNull() != NULL) {
    return NULL;
  }

  const TimeInterval defaultTimeToCache = TimeInterval::fromDays(30);

  const JSONObject* jsonBaseLayer = jsonBaseObjectLayer->asObject();
  if (jsonBaseLayer == NULL) {
    ILogger::instance()->logError("Layer is not a json object");
    return NULL;
  }

  const std::string layerType = jsonBaseLayer->getAsString("layer", "<layer not present>");
  if (layerType.compare("OSM") == 0) {
    return new OSMLayer(defaultTimeToCache);
  }
  else if (layerType.compare("MapQuest") == 0) {
    return parseMapQuestLayer(jsonBaseLayer, defaultTimeToCache);
  }
  else if (layerType.compare("BingMaps") == 0) {
    return parseBingMapsLayer(jsonBaseLayer, defaultTimeToCache);
  }
  else if (layerType.compare("CartoDB") == 0) {
    return parseCartoDBLayer(jsonBaseLayer, defaultTimeToCache);
  }
  else if (layerType.compare("MapBox") == 0) {
    return parseMapBoxLayer(jsonBaseLayer, defaultTimeToCache);
  }
  else if (layerType.compare("WMS") == 0) {
    return parseWMSLayer(jsonBaseLayer);
  }
  else {
    ILogger::instance()->logError("Unsupported layer type \"%s\"", layerType.c_str());
    return NULL;
  }
}


void G3MCBuilder::parseSceneDescription(const std::string& json,
                                        const URL& url) {
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(json, true);

  if (jsonBaseObject == NULL) {
    ILogger::instance()->logError("Can't parse SceneJSON from %s",
                                  url.getPath().c_str());
  }
  else {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    if (jsonObject == NULL) {
      ILogger::instance()->logError("Invalid SceneJSON (1)");
    }
    else {
      const JSONString* error = jsonObject->getAsString("error");
      if (error == NULL) {
        const int timestamp = (int) jsonObject->getAsNumber("ts", 0);

        if (getSceneTimestamp() != timestamp) {
          const JSONString* jsonUser = jsonObject->getAsString("user");
          if (jsonUser != NULL) {
            setSceneUser(jsonUser->value());
          }

          //id

          const JSONString* jsonName = jsonObject->getAsString("name");
          if (jsonName != NULL) {
            setSceneName(jsonName->value());
          }

          const JSONString* jsonDescription = jsonObject->getAsString("description");
          if (jsonDescription != NULL) {
            setSceneDescription(jsonDescription->value());
          }

          const JSONString* jsonBGColor = jsonObject->getAsString("bgColor");
          if (jsonBGColor != NULL) {
            const Color* bgColor = Color::parse(jsonBGColor->value());
            if (bgColor == NULL) {
              ILogger::instance()->logError("Invalid format in attribute 'bgColor' (%s)",
                                            jsonBGColor->value().c_str());
            }
            else {
              setSceneBackgroundColor(*bgColor);
              delete bgColor;
            }
          }

          const JSONBaseObject* jsonBaseLayer = jsonObject->get("baseLayer");
          if (jsonBaseLayer != NULL) {
            setSceneBaseLayer( parseLayer(jsonBaseLayer) );
          }

          const JSONBaseObject* jsonOverlayLayer = jsonObject->get("overlayLayer");
          if (jsonOverlayLayer != NULL) {
            setSceneOverlayLayer( parseLayer(jsonOverlayLayer) );
          }

          //tags

          setSceneTimestamp(timestamp);
        }
      }
      else {
        ILogger::instance()->logError("Server Error: %s",
                                      error->value().c_str());
      }
    }

    delete jsonBaseObject;
  }

}


class G3MCBuilder_SceneDescriptionBufferListener : public IBufferDownloadListener {
private:
  G3MCBuilder* _builder;

public:
  G3MCBuilder_SceneDescriptionBufferListener(G3MCBuilder* builder) :
  _builder(builder)
  {
  }
  
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {

    _builder->parseSceneDescription(buffer->getAsString(), url);
    delete buffer;
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Can't download SceneJSON from %s",
                                  url.getPath().c_str());
  }
  
  void onCancel(const URL& url) {
    // do nothing
  }
  
  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
  
};


class G3MCBuilder_PollingScenePeriodicalTask : public GTask {
private:
  G3MCBuilder* _builder;
  
  long long _requestId;
  
  
  URL getURL() const {
    const int sceneTimestamp = _builder->getSceneTimestamp();
    
    const URL _sceneDescriptionURL = _builder->createPollingSceneDescriptionURL();
    
    if (sceneTimestamp < 0) {
      return _sceneDescriptionURL;
    }
    
    IStringBuilder* ib = IStringBuilder::newStringBuilder();
    
    ib->addString(_sceneDescriptionURL.getPath());
    ib->addString("?lastTs=");
    ib->addInt(sceneTimestamp);
    
    const std::string path = ib->getString();
    
    delete ib;
    
    return URL(path, false);
  }
  
  
public:
  G3MCBuilder_PollingScenePeriodicalTask(G3MCBuilder* builder) :
  _builder(builder),
  _requestId(-1)
  {
    
  }
  
  void run(const G3MContext* context) {    
    IDownloader* downloader = context->getDownloader();
    if (_requestId >= 0) {
      downloader->cancelRequest(_requestId);
    }
    
    _requestId = downloader->requestBuffer(getURL(),
                                           DownloadPriority::HIGHEST,
                                           TimeInterval::zero(),
                                           true,
                                           new G3MCBuilder_SceneDescriptionBufferListener(_builder),
                                           true);
  }
};


void G3MCBuilder::recreateLayerSet() {
  _layerSet->removeAllLayers(false);

  if (_sceneBaseLayer != NULL) {
    _layerSet->addLayer(_sceneBaseLayer);
  }

  if (_sceneOverlayLayer != NULL) {
    _layerSet->addLayer(_sceneOverlayLayer);
  }
}

void G3MCBuilder::setSceneBaseLayer(Layer* baseLayer) {
  if (baseLayer == NULL) {
    ILogger::instance()->logError("Base Layer can't be NULL");
    return;
  }

  if (_sceneBaseLayer != baseLayer) {
    delete _sceneBaseLayer;
    _sceneBaseLayer = baseLayer;
    
    recreateLayerSet();
    
    if (_sceneListener != NULL) {
      _sceneListener->onBaseLayerChanged(_sceneBaseLayer);
    }
  }
}

void G3MCBuilder::setSceneOverlayLayer(Layer* overlayLayer) {
  if (_sceneOverlayLayer != overlayLayer) {
    delete _sceneOverlayLayer;
    _sceneOverlayLayer = overlayLayer;
    
    recreateLayerSet();
    
    if (_sceneListener != NULL) {
      _sceneListener->onOverlayLayerChanged(_sceneOverlayLayer);
    }
  }
}

const URL G3MCBuilder::createSceneTubeURL() const {
  const std::string tubesPath = _tubesURL.getPath();

  return URL(tubesPath + "/scene/" + _sceneId, false);
}

const URL G3MCBuilder::createPollingSceneDescriptionURL() const {
  const std::string serverPath = _serverURL.getPath();
  
  return URL(serverPath + "/scenes/" + _sceneId, false);
}


class G3MCBuilder_TubeWatchdogPeriodicalTask : public GTask {
private:
  G3MCBuilder* _builder;

public:
  G3MCBuilder_TubeWatchdogPeriodicalTask(G3MCBuilder* builder) :
  _builder(builder)
  {
  }

  void run(const G3MContext* context) {
    if (!_builder->isSceneTubeOpen()) {
      _builder->openSceneTube(context);
    }
  }

};


std::vector<PeriodicalTask*>* G3MCBuilder::createPeriodicalTasks() {
  std::vector<PeriodicalTask*>* periodicalTasks = new std::vector<PeriodicalTask*>();

  if (_useWebSockets) {
    periodicalTasks->push_back(new PeriodicalTask(TimeInterval::fromSeconds(2),
                                                  new G3MCBuilder_TubeWatchdogPeriodicalTask(this)));
  }
  else {
    periodicalTasks->push_back(new PeriodicalTask(TimeInterval::fromSeconds(2),
                                                  new G3MCBuilder_PollingScenePeriodicalTask(this)));
  }

  return periodicalTasks;
}

IStorage* G3MCBuilder::getStorage() {
  if (_storage == NULL) {
    _storage = createStorage();
  }
  return _storage;
}

class G3MCBuilder_SceneTubeListener : public IWebSocketListener {
private:
  G3MCBuilder* _builder;

public:
  G3MCBuilder_SceneTubeListener(G3MCBuilder* builder) :
  _builder(builder)
  {
  }

  ~G3MCBuilder_SceneTubeListener() {
  }

  void onOpen(IWebSocket* ws) {
    ILogger::instance()->logError("Tube '%s' opened!",
                                  ws->getURL().getPath().c_str());
    _builder->setSceneTubeOpened(true);
  }

  void onError(IWebSocket* ws,
               const std::string& error) {
    ILogger::instance()->logError("Error '%s' on Tube '%s'",
                                  error.c_str(),
                                  ws->getURL().getPath().c_str());
  }

  void onMesssage(IWebSocket* ws,
                  const std::string& message) {
    _builder->parseSceneDescription(message, ws->getURL());
  }

  void onClose(IWebSocket* ws) {
    ILogger::instance()->logError("Tube '%s' closed!",
                                  ws->getURL().getPath().c_str());
    _builder->setSceneTubeOpened(false);
  }
};

class G3MCBuilder_SceneTubeConnector : public GInitializationTask {
private:
  G3MCBuilder* _builder;

public:
  G3MCBuilder_SceneTubeConnector(G3MCBuilder* builder) :
  _builder(builder)
  {
  }

  void run(const G3MContext* context) {
    _builder->openSceneTube(context);
  }

  bool isDone(const G3MContext* context) {
    return true;
  }
};

void G3MCBuilder::openSceneTube(const G3MContext* context) {
  const bool autodeleteListener  = true;
  const bool autodeleteWebSocket = true;

  _sceneTubeWebSocket = context->getFactory()->createWebSocket(createSceneTubeURL(),
                                                               new G3MCBuilder_SceneTubeListener(this),
                                                               autodeleteListener,
                                                               autodeleteWebSocket);
}


GInitializationTask* G3MCBuilder::createInitializationTask() {
  return _useWebSockets ? new G3MCBuilder_SceneTubeConnector(this) : NULL;
}

G3MWidget* G3MCBuilder::create() {
  if (_g3mWidget != NULL) {
    ILogger::instance()->logError("The G3MWidget was already created, can't be created more than once");
    return NULL;
  }
  
  
  CompositeRenderer* mainRenderer = new CompositeRenderer();

  TileRenderer* tileRenderer = createTileRenderer();
  mainRenderer->addRenderer(tileRenderer);
  
  std::vector<ICameraConstrainer*>* cameraConstraints = createCameraConstraints();
  
  GInitializationTask* initializationTask = createInitializationTask();
  
  std::vector<PeriodicalTask*>* periodicalTasks = createPeriodicalTasks();

  ICameraActivityListener* cameraActivityListener = NULL;

  _g3mWidget = G3MWidget::create(getGL(),
                                 getStorage(),
                                 getDownloader(),
                                 getThreadUtils(),
                                 cameraActivityListener,
                                 createPlanet(),
                                 *cameraConstraints,
                                 createCameraRenderer(),
                                 mainRenderer,
                                 createBusyRenderer(),
                                 *_sceneBackgroundColor,
                                 false,      // logFPS
                                 false,      // logDownloaderStatistics
                                 initializationTask,
                                 true,       // autoDeleteInitializationTask
                                 *periodicalTasks,
                                 getGPUProgramManager());
  delete cameraConstraints;
  delete periodicalTasks;
  
  return _g3mWidget;
}

class G3MCBuilder_ScenesDescriptionsBufferListener : public IBufferDownloadListener {
private:
  G3MCBuilderScenesDescriptionsListener* _listener;
  const bool _autoDelete;
  
public:
  G3MCBuilder_ScenesDescriptionsBufferListener(G3MCBuilderScenesDescriptionsListener* listener,
                                               bool autoDelete) :
  _listener(listener),
  _autoDelete(autoDelete)
  {
    
  }
  
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    
    const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(buffer);
    
    if (jsonBaseObject == NULL) {
      ILogger::instance()->logError("Can't parse ScenesDescriptionJSON from %s",
                                    url.getPath().c_str());
      onError(url);
    }
    else {
      const JSONArray* jsonScenesDescriptions = jsonBaseObject->asArray();
      if (jsonScenesDescriptions == NULL) {
        ILogger::instance()->logError("ScenesDescriptionJSON: invalid format (1)");
        onError(url);
      }
      else {
        std::vector<G3MCSceneDescription*>* scenesDescriptions = new std::vector<G3MCSceneDescription*>();
        
        const int size = jsonScenesDescriptions->size();
        
        for (int i = 0; i < size; i++) {
          const JSONObject* jsonSceneDescription = jsonScenesDescriptions->getAsObject(i);
          if (jsonSceneDescription == NULL) {
            ILogger::instance()->logError("ScenesDescriptionJSON: invalid format (2) at index #%d", i);
          }
          else {
            const std::string id          = jsonSceneDescription->getAsString("id",          "<invalid id>");
            const std::string user        = jsonSceneDescription->getAsString("user",        "<invalid user>");
            const std::string name        = jsonSceneDescription->getAsString("name",        "<invalid name>");
            const std::string description = jsonSceneDescription->getAsString("description", "");
            const std::string iconURL     = jsonSceneDescription->getAsString("iconURL",     "<invalid iconURL>");
            
            std::vector<std::string> tags;
            const JSONArray* jsonTags = jsonSceneDescription->getAsArray("tags");
            if (jsonTags == NULL) {
              ILogger::instance()->logError("ScenesDescriptionJSON: invalid format (3) at index #%d", i);
            }
            else {
              const int tagsCount = jsonTags->size();
              for (int j = 0; j < tagsCount; j++) {
                const std::string tag = jsonTags->getAsString(j, "");
                if (tag.size() > 0) {
                  tags.push_back(tag);
                }
              }
            }
            
            scenesDescriptions->push_back( new G3MCSceneDescription(id,
                                                                    user,
                                                                    name,
                                                                    description,
                                                                    iconURL,
                                                                    tags) );
            
          }
        }
        
        _listener->onDownload(scenesDescriptions);
        if (_autoDelete) {
          delete _listener;
        }
      }
      
      delete jsonBaseObject;
    }
    
    delete buffer;
  }
  
  void onError(const URL& url) {
    _listener->onError();
    if (_autoDelete) {
      delete _listener;
    }
  }
  
  void onCancel(const URL& url) {
    // do nothing
  }
  
  void onCanceledDownload(const URL& url,
                          IByteBuffer* buffer,
                          bool expired) {
    // do nothing
  }
  
};

const URL G3MCBuilder::createScenesDescriptionsURL() const {
  const std::string serverPath = _serverURL.getPath();
  
  return URL(serverPath + "/scenes/", false);
}


void G3MCBuilder::requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener* listener,
                                            bool autoDelete) {
  getDownloader()->requestBuffer(createScenesDescriptionsURL(),
                                 DownloadPriority::HIGHEST,
                                 TimeInterval::zero(),
                                 true,
                                 new G3MCBuilder_ScenesDescriptionsBufferListener(listener, autoDelete),
                                 true);
}

int G3MCBuilder::getSceneTimestamp() const {
  return _sceneTimestamp;
}

void G3MCBuilder::setSceneTimestamp(const int timestamp) {
  _sceneTimestamp = timestamp;
}

void G3MCBuilder::setSceneUser(const std::string& user) {
  if (_sceneUser.compare(user) != 0) {
    _sceneUser = user;
    
    if (_sceneListener != NULL) {
      _sceneListener->onUserChanged(_sceneUser);
    }
  }
}

void G3MCBuilder::setSceneName(const std::string& name) {
  if (_sceneName.compare(name) != 0) {
    _sceneName = name;
    
    if (_sceneListener != NULL) {
      _sceneListener->onNameChanged(_sceneName);
    }
  }
}

void G3MCBuilder::setSceneDescription(const std::string& description) {
  if (_sceneDescription.compare(description) != 0) {
    _sceneDescription = description;

    if (_sceneListener != NULL) {
      _sceneListener->onDescriptionChanged(_sceneDescription);
    }
  }
}

void G3MCBuilder::setSceneBackgroundColor(const Color& backgroundColor) {
  if (!_sceneBackgroundColor->isEqualsTo(backgroundColor)) {
    delete _sceneBackgroundColor;
    _sceneBackgroundColor = new Color(backgroundColor);

    if (_g3mWidget != NULL) {
      _g3mWidget->setBackgroundColor(*_sceneBackgroundColor);
    }

    if (_sceneListener != NULL) {
      _sceneListener->onBackgroundColorChanged(*_sceneBackgroundColor);
    }
  }
}

class G3MCBuilder_ChangeSceneIdTask : public GTask {
private:
  G3MCBuilder*      _builder;
  const std::string _sceneId;
  
public:
  G3MCBuilder_ChangeSceneIdTask(G3MCBuilder* builder,
                                const std::string& sceneId) :
  _builder(builder),
  _sceneId(sceneId)
  {
  }
  
  void run(const G3MContext* context) {
    _builder->rawChangeScene(_sceneId);
  }
};

void G3MCBuilder::changeScene(const std::string& sceneId) {
  if (sceneId.compare(_sceneId) != 0) {
    getThreadUtils()->invokeInRendererThread(new G3MCBuilder_ChangeSceneIdTask(this, sceneId),
                                             true);
  }
}

void G3MCBuilder::resetScene(const std::string& sceneId) {
  _sceneId = sceneId;

  _sceneTimestamp = -1;

  delete _sceneBaseLayer;
  _sceneBaseLayer = NULL;

  delete _sceneOverlayLayer;
  _sceneOverlayLayer = NULL;

  _sceneUser = "";

  _sceneName = "";

  _sceneDescription = "";

  delete _sceneBackgroundColor;
  _sceneBackgroundColor = Color::newFromRGBA(0, 0, 0, 1);
}

void G3MCBuilder::resetG3MWidget() {
  _layerSet->removeAllLayers(false);

  if (_g3mWidget != NULL) {
    _g3mWidget->setBackgroundColor(*_sceneBackgroundColor);

    // force inmediate ejecution of PeriodicalTasks
    _g3mWidget->resetPeriodicalTasksTimeouts();
  }
}

void G3MCBuilder::setSceneTubeOpened(bool open) {
  if (_isSceneTubeOpen != open) {
    _isSceneTubeOpen = open;
    if (!_isSceneTubeOpen) {
      _sceneTubeWebSocket = NULL;
    }
  }
}

void G3MCBuilder::rawChangeScene(const std::string& sceneId) {
  if (sceneId.compare(_sceneId) != 0) {
    resetScene(sceneId);

    resetG3MWidget();

    if (_sceneListener != NULL) {
      _sceneListener->onSceneChanged(sceneId);
    }

    if (_sceneTubeWebSocket != NULL) {
      _sceneTubeWebSocket->close();
    }
  }
}
