//
//  MapBooBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "MapBooBuilder.hpp"

#include "ILogger.hpp"
#include "CompositeRenderer.hpp"
#include "PlanetRenderer.hpp"

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
#include "JSONNumber.hpp"
#include "IThreadUtils.hpp"
#include "OSMLayer.hpp"
#include "MapQuestLayer.hpp"
#include "BingMapsLayer.hpp"
#include "CartoDBLayer.hpp"
#include "MapBoxLayer.hpp"
#include "WMSLayer.hpp"
#include "LayerTilesRenderParameters.hpp"
#include "IWebSocketListener.hpp"
#include "IWebSocket.hpp"
#include "SceneLighting.hpp"

MapBoo_Scene::~MapBoo_Scene() {
  delete _baseLayer;
  delete _overlayLayer;
}

const std::string MapBoo_Scene::description() const {
  IStringBuilder *isb = IStringBuilder::newStringBuilder();

  isb->addString("[Scene name=");
  isb->addString(_name);

  isb->addString(", description=");
  isb->addString(_description);

  isb->addString(", icon=");
  isb->addString(_icon);

  isb->addString(", backgroundColor=");
  isb->addString(_backgroundColor.description());

  isb->addString(", baseLayer=");
  if (_baseLayer == NULL) {
    isb->addString("NULL");
  }
  else {
    isb->addString(_baseLayer->description());
  }

  isb->addString(", overlayLayer=");
  if (_overlayLayer == NULL) {
    isb->addString("NULL");
  }
  else {
    isb->addString(_overlayLayer->description());
  }

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

MapBooBuilder::MapBooBuilder(const URL& serverURL,
                             const URL& tubesURL,
                             bool useWebSockets,
                             const std::string& applicationId,
                             MapBooApplicationChangeListener* applicationListener) :
_serverURL(serverURL),
_tubesURL(tubesURL),
_useWebSockets(useWebSockets),
_applicationId(applicationId),
_applicationName(""),
_applicationTimestamp(-1),
_gl(NULL),
_g3mWidget(NULL),
_storage(NULL),
_threadUtils(NULL),
_layerSet( new LayerSet() ),
_downloader(NULL),
_applicationListener(applicationListener),
_gpuProgramManager(NULL),
_isApplicationTubeOpen(false),
_applicationCurrentSceneIndex(-1),
_applicationDefaultSceneIndex(0),
_context(NULL)
{

}

GPUProgramManager* MapBooBuilder::getGPUProgramManager() {
  if (_gpuProgramManager == NULL) {
    _gpuProgramManager = createGPUProgramManager();
  }
  return _gpuProgramManager;
}

IDownloader* MapBooBuilder::getDownloader() {
  if (_downloader == NULL) {
    _downloader = createDownloader();
  }
  return _downloader;
}

IThreadUtils* MapBooBuilder::getThreadUtils() {
  if (_threadUtils == NULL) {
    _threadUtils = createThreadUtils();
  }
  return _threadUtils;
}

void MapBooBuilder::setGL(GL *gl) {
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

GL* MapBooBuilder::getGL() {
  if (!_gl) {
    ILogger::instance()->logError("Logic Error: _gl not initialized");
  }

  return _gl;
}

PlanetRenderer* MapBooBuilder::createPlanetRenderer() {
  const TileTessellator* tessellator = new EllipsoidalTileTessellator(true);

  ElevationDataProvider* elevationDataProvider = NULL;
  const float verticalExaggeration = 1;
  TileTexturizer* texturizer = new MultiLayerTileTexturizer();
  TileRasterizer* tileRasterizer = NULL;

  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;

  const TilesRenderParameters* parameters = new TilesRenderParameters(renderDebug,
                                                                      useTilesSplitBudget,
                                                                      forceFirstLevelTilesRenderOnStart,
                                                                      incrementalTileQuality);

  const bool showStatistics = false;
  long long texturePriority = DownloadPriority::HIGHER;

  return new PlanetRenderer(tessellator,
                            elevationDataProvider,
                            verticalExaggeration,
                            texturizer,
                            tileRasterizer,
                            _layerSet,
                            parameters,
                            showStatistics,
                            texturePriority);
}

const Planet* MapBooBuilder::createPlanet() {
  return Planet::createEarth();
}

std::vector<ICameraConstrainer*>* MapBooBuilder::createCameraConstraints() {
  std::vector<ICameraConstrainer*>* cameraConstraints = new std::vector<ICameraConstrainer*>;
  SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  cameraConstraints->push_back(scc);

  return cameraConstraints;
}

CameraRenderer* MapBooBuilder::createCameraRenderer() {
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  cameraRenderer->addHandler(new CameraRotationHandler());
  cameraRenderer->addHandler(new CameraDoubleTapHandler());

  return cameraRenderer;
}

Renderer* MapBooBuilder::createBusyRenderer() {
  return new BusyMeshRenderer(Color::newFromRGBA(0, 0, 0, 1));
}

MapQuestLayer* MapBooBuilder::parseMapQuestLayer(const JSONObject* jsonLayer,
                                                 const TimeInterval& timeToCache) const {
  const std::string imagery = jsonLayer->getAsString("imagery", "<imagery not present>");
  if (imagery.compare("OpenAerial") == 0) {
    return MapQuestLayer::newOpenAerial(timeToCache);
  }

  // defaults to OSM
  return MapQuestLayer::newOSM(timeToCache);
}

BingMapsLayer* MapBooBuilder::parseBingMapsLayer(const JSONObject* jsonLayer,
                                                 const TimeInterval& timeToCache) const {
  const std::string key = jsonLayer->getAsString("key", "");
  const std::string imagerySet = jsonLayer->getAsString("imagerySet", "Aerial");

  return new BingMapsLayer(imagerySet, key, timeToCache);
}

CartoDBLayer* MapBooBuilder::parseCartoDBLayer(const JSONObject* jsonLayer,
                                               const TimeInterval& timeToCache) const {
  const std::string userName = jsonLayer->getAsString("userName", "");
  const std::string table    = jsonLayer->getAsString("table",    "");

  return new CartoDBLayer(userName, table, timeToCache);
}

MapBoxLayer* MapBooBuilder::parseMapBoxLayer(const JSONObject* jsonLayer,
                                             const TimeInterval& timeToCache) const {
  const std::string mapKey = jsonLayer->getAsString("mapKey", "");

  return new MapBoxLayer(mapKey, timeToCache);
}

WMSLayer* MapBooBuilder::parseWMSLayer(const JSONObject* jsonLayer) const {

  const std::string mapLayer = jsonLayer->getAsString("layerName", "");
  const URL mapServerURL = URL(jsonLayer->getAsString("server", ""), false);
  const std::string versionStr = jsonLayer->getAsString("version", "");
  WMSServerVersion mapServerVersion = WMS_1_1_0;
  if (versionStr.compare("WMS_1_3_0") == 0) {
    mapServerVersion = WMS_1_3_0;
  }
  const std::string queryLayer = jsonLayer->getAsString("queryLayer", "");
  const std::string style = jsonLayer->getAsString("style", "");
  const URL queryServerURL = URL("", false);
  const WMSServerVersion queryServerVersion = mapServerVersion;
  const double lowerLat = jsonLayer->getAsNumber("lowerLat", -90.0);
  const double lowerLon = jsonLayer->getAsNumber("lowerLon", -180.0);
  const double upperLat = jsonLayer->getAsNumber("upperLat", 90.0);
  const double upperLon = jsonLayer->getAsNumber("upperLon", 180.0);
  const Sector sector = Sector(Geodetic2D(Angle::fromDegrees(lowerLat), Angle::fromDegrees(lowerLon)),
                               Geodetic2D(Angle::fromDegrees(upperLat), Angle::fromDegrees(upperLon)));
  std::string imageFormat = jsonLayer->getAsString("imageFormat", "image/png");
  if (imageFormat.compare("JPG") == 0) {
    imageFormat = "image/jpeg";
  }
  const std::string srs = jsonLayer->getAsString("projection", "EPSG_4326");
  LayerTilesRenderParameters* layerTilesRenderParameters = NULL;
  if (srs.compare("EPSG_4326") == 0) {
    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultNonMercator(Sector::fullSphere());
  }
  else if (srs.compare("EPSG_900913") == 0) {
    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultMercator(0, 17);
  }
  const bool isTransparent = jsonLayer->getAsBoolean("transparent", false);
  const double expiration = jsonLayer->getAsNumber("expiration", 0);
  const long long milliseconds = IMathUtils::instance()->round(expiration);
  const TimeInterval timeToCache = TimeInterval::fromMilliseconds(milliseconds);
  const bool readExpired = jsonLayer->getAsBoolean("acceptExpiration", false);

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


Layer* MapBooBuilder::parseLayer(const JSONBaseObject* jsonBaseObjectLayer) const {
  if (jsonBaseObjectLayer == NULL) {
    return NULL;
  }

  if (jsonBaseObjectLayer->asNull() != NULL) {
    return NULL;
  }

  const TimeInterval defaultTimeToCache = TimeInterval::fromDays(30);

  const JSONObject* jsonLayer = jsonBaseObjectLayer->asObject();
  if (jsonLayer == NULL) {
    ILogger::instance()->logError("Layer is not a json object");
    return NULL;
  }

  const std::string layerType = jsonLayer->getAsString("layer", "<layer not present>");
  if (layerType.compare("OSM") == 0) {
    return new OSMLayer(defaultTimeToCache);
  }
  else if (layerType.compare("MapQuest") == 0) {
    return parseMapQuestLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("BingMaps") == 0) {
    return parseBingMapsLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("CartoDB") == 0) {
    return parseCartoDBLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("MapBox") == 0) {
    return parseMapBoxLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("WMS") == 0) {
    return parseWMSLayer(jsonLayer);
  }
  else {
    ILogger::instance()->logError("Unsupported layer type \"%s\"", layerType.c_str());
    return NULL;
  }
}

Color MapBooBuilder::parseColor(const JSONString* jsonColor) const {
  if (jsonColor == NULL) {
    return Color::black();
  }

  const Color* color = Color::parse(jsonColor->value());
  if (color == NULL) {
    ILogger::instance()->logError("Invalid format in attribute 'color' (%s)",
                                  jsonColor->value().c_str());
    return Color::black();
  }

  Color result(*color);
  delete color;
  return result;
}

MapBoo_Scene* MapBooBuilder::parseScene(const JSONObject* jsonObject) const {
  if (jsonObject == NULL) {
    return NULL;
  }

  std::string name            = jsonObject->getAsString("name", "");
  std::string description     = jsonObject->getAsString("description", "");
  std::string icon            = jsonObject->getAsString("icon", "");
  Color       backgroundColor = parseColor( jsonObject->getAsString("bgColor") );
  Layer*      baseLayer       = parseLayer( jsonObject->get("baseLayer") );
  Layer*      overlayLayer    = parseLayer( jsonObject->get("overlayLayer") );

  return new MapBoo_Scene(name,
                          description,
                          icon,
                          backgroundColor,
                          baseLayer,
                          overlayLayer);
}


void MapBooBuilder::parseApplicationDescription(const std::string& json,
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
        const int timestamp = (int) jsonObject->getAsNumber("timestamp", 0);

        if (getApplicationTimestamp() != timestamp) {
          const JSONString* jsonName = jsonObject->getAsString("name");
          if (jsonName != NULL) {
            setApplicationName( jsonName->value() );
          }

          // always process defaultSceneIndex before scenes
          const JSONNumber* jsonDefaultSceneIndex = jsonObject->getAsNumber("defaultSceneIndex");
          if (jsonDefaultSceneIndex != NULL) {
            const int defaultSceneIndex = (int) jsonDefaultSceneIndex->value();
            setApplicationDefaultSceneIndex(defaultSceneIndex);
          }

          const JSONArray* jsonScenes = jsonObject->getAsArray("scenes");
          if (jsonScenes != NULL) {
            std::vector<MapBoo_Scene*> scenes;

            const int scenesCount = jsonScenes->size();
            for (int i = 0; i < scenesCount; i++) {
              MapBoo_Scene* scene = parseScene( jsonScenes->getAsObject(i) );
              if (scene != NULL) {
                scenes.push_back(scene);
              }
            }

            setApplicationScenes(scenes);
          }

          // int _TODO_Application_Warnings;

          setApplicationTimestamp(timestamp);
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

void MapBooBuilder::setApplicationDefaultSceneIndex(int defaultSceneIndex) {
  _applicationDefaultSceneIndex = defaultSceneIndex;
}

class MapBooBuilder_SceneDescriptionBufferListener : public IBufferDownloadListener {
private:
  MapBooBuilder* _builder;

public:
  MapBooBuilder_SceneDescriptionBufferListener(MapBooBuilder* builder) :
  _builder(builder)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _builder->parseApplicationDescription(buffer->getAsString(), url);
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


class MapBooBuilder_PollingScenePeriodicalTask : public GTask {
private:
  MapBooBuilder* _builder;

  long long _requestId;


  URL getURL() const {
    const int applicationTimestamp = _builder->getApplicationTimestamp();

    const URL _sceneDescriptionURL = _builder->createPollingApplicationDescriptionURL();

    if (applicationTimestamp < 0) {
      return _sceneDescriptionURL;
    }

    IStringBuilder* ib = IStringBuilder::newStringBuilder();

    ib->addString(_sceneDescriptionURL.getPath());
    ib->addString("?lastTs=");
    ib->addInt(applicationTimestamp);

    const std::string path = ib->getString();

    delete ib;

    return URL(path, false);
  }


public:
  MapBooBuilder_PollingScenePeriodicalTask(MapBooBuilder* builder) :
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
                                           new MapBooBuilder_SceneDescriptionBufferListener(_builder),
                                           true);
  }
};

void MapBoo_Scene::fillLayerSet(LayerSet* layerSet) const {
  if (_baseLayer != NULL) {
    layerSet->addLayer(_baseLayer);
  }

  if (_overlayLayer != NULL) {
    layerSet->addLayer(_overlayLayer);
  }
}

void MapBooBuilder::recreateLayerSet() {
  _layerSet->removeAllLayers(false);

  const MapBoo_Scene* scene = getApplicationCurrentScene();
  if (scene != NULL) {
    scene->fillLayerSet(_layerSet);
  }
}

const URL MapBooBuilder::createApplicationTubeURL() const {
  const std::string tubesPath = _tubesURL.getPath();

  return URL(tubesPath + "/application/" + _applicationId + "/runtime", false);
}

const URL MapBooBuilder::createPollingApplicationDescriptionURL() const {
  const std::string tubesPath = _serverURL.getPath();

  return URL(tubesPath + "/application/" + _applicationId + "/runtime", false);
}


class MapBooBuilder_TubeWatchdogPeriodicalTask : public GTask {
private:
  MapBooBuilder* _builder;
  bool _firstRun;

public:
  MapBooBuilder_TubeWatchdogPeriodicalTask(MapBooBuilder* builder) :
  _builder(builder),
  _firstRun(true)
  {
  }

  void run(const G3MContext* context) {
    if (_firstRun) {
      _firstRun = false;
    }
    else {
      if (!_builder->isApplicationTubeOpen()) {
        _builder->openApplicationTube(context);
      }
    }
  }

};


std::vector<PeriodicalTask*>* MapBooBuilder::createPeriodicalTasks() {
  std::vector<PeriodicalTask*>* periodicalTasks = new std::vector<PeriodicalTask*>();

  if (_useWebSockets) {
    periodicalTasks->push_back(new PeriodicalTask(TimeInterval::fromSeconds(5),
                                                  new MapBooBuilder_TubeWatchdogPeriodicalTask(this)));
  }
  else {
    periodicalTasks->push_back(new PeriodicalTask(TimeInterval::fromSeconds(5),
                                                  new MapBooBuilder_PollingScenePeriodicalTask(this)));
  }

  return periodicalTasks;
}

IStorage* MapBooBuilder::getStorage() {
  if (_storage == NULL) {
    _storage = createStorage();
  }
  return _storage;
}

class MapBooBuilder_ApplicationTubeListener : public IWebSocketListener {
private:
  MapBooBuilder* _builder;

public:
  MapBooBuilder_ApplicationTubeListener(MapBooBuilder* builder) :
  _builder(builder)
  {
  }

  ~MapBooBuilder_ApplicationTubeListener() {
  }

  void onOpen(IWebSocket* ws) {
    ILogger::instance()->logInfo("Tube '%s' opened!",
                                 ws->getURL().getPath().c_str());
    _builder->setApplicationTubeOpened(true);
  }

  void onError(IWebSocket* ws,
               const std::string& error) {
    ILogger::instance()->logError("Error '%s' on Tube '%s'",
                                  error.c_str(),
                                  ws->getURL().getPath().c_str());
    _builder->setApplicationTubeOpened(false);
  }

  void onMesssage(IWebSocket* ws,
                  const std::string& message) {
    _builder->parseApplicationDescription(message, ws->getURL());
  }

  void onClose(IWebSocket* ws) {
    ILogger::instance()->logError("Tube '%s' closed!",
                                  ws->getURL().getPath().c_str());
    _builder->setApplicationTubeOpened(false);
  }
};

class MapBooBuilder_SceneTubeConnector : public GInitializationTask {
private:
  MapBooBuilder* _builder;

public:
  MapBooBuilder_SceneTubeConnector(MapBooBuilder* builder) :
  _builder(builder)
  {
  }

  void run(const G3MContext* context) {
    _builder->setContext(context);
    _builder->openApplicationTube(context);
  }

  bool isDone(const G3MContext* context) {
    return true;
  }
};

void MapBooBuilder::setContext(const G3MContext* context) {
  _context = context;
}

MapBooBuilder::~MapBooBuilder() {

}

void MapBooBuilder::openApplicationTube(const G3MContext* context) {
  const bool autodeleteListener  = true;
  const bool autodeleteWebSocket = true;

  const IFactory* factory = context->getFactory();
  factory->createWebSocket(createApplicationTubeURL(),
                           new MapBooBuilder_ApplicationTubeListener(this),
                           autodeleteListener,
                           autodeleteWebSocket);
}

GInitializationTask* MapBooBuilder::createInitializationTask() {
  return _useWebSockets ? new MapBooBuilder_SceneTubeConnector(this) : NULL;
}

const int MapBooBuilder::getApplicationCurrentSceneIndex() {
  if (_applicationCurrentSceneIndex < 0) {
    _applicationCurrentSceneIndex = _applicationDefaultSceneIndex;
  }
  return _applicationCurrentSceneIndex;
}

const MapBoo_Scene* MapBooBuilder::getApplicationCurrentScene() {
  const int currentSceneIndex = getApplicationCurrentSceneIndex();

  const bool validCurrentSceneIndex = ((currentSceneIndex >= 0) &&
                                       (currentSceneIndex < _applicationScenes.size()));

  return validCurrentSceneIndex ? _applicationScenes[currentSceneIndex] : NULL;
}

Color MapBooBuilder::getCurrentBackgroundColor() {
  const MapBoo_Scene* scene = getApplicationCurrentScene();
  return (scene == NULL) ? Color::black() : scene->getBackgroundColor();
}

G3MWidget* MapBooBuilder::create() {
  if (_g3mWidget != NULL) {
    ILogger::instance()->logError("The G3MWidget was already created, can't be created more than once");
    return NULL;
  }


  CompositeRenderer* mainRenderer = new CompositeRenderer();

  PlanetRenderer* planetRenderer = createPlanetRenderer();
  mainRenderer->addRenderer(planetRenderer);

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
                                 Color::black(),
                                 false,      // logFPS
                                 false,      // logDownloaderStatistics
                                 initializationTask,
                                 true,       // autoDeleteInitializationTask
                                 *periodicalTasks,
                                 getGPUProgramManager(),
                                 createSceneLighting());
  delete cameraConstraints;
  delete periodicalTasks;

  return _g3mWidget;
}

int MapBooBuilder::getApplicationTimestamp() const {
  return _applicationTimestamp;
}

void MapBooBuilder::setApplicationTimestamp(const int timestamp) {
  _applicationTimestamp = timestamp;
}

void MapBooBuilder::setApplicationName(const std::string& name) {
  if (_applicationName.compare(name) != 0) {
    _applicationName = name;

    if (_applicationListener != NULL) {
      _applicationListener->onNameChanged(_context, _applicationName);
    }
  }
}

class MapBooBuilder_ChangeSceneTask : public GTask {
private:
  MapBooBuilder* _builder;
  const int      _sceneIndex;

public:
  MapBooBuilder_ChangeSceneTask(MapBooBuilder* builder,
                                int sceneIndex) :
  _builder(builder),
  _sceneIndex(sceneIndex)
  {
  }

  void run(const G3MContext* context) {
    _builder->rawChangeScene(_sceneIndex);
  }
};

void MapBooBuilder::rawChangeScene(int sceneIndex) {
  _applicationCurrentSceneIndex = sceneIndex;

  changedCurrentScene();
}

void MapBooBuilder::changeScene(int sceneIndex) {
  const int currentSceneIndex = getApplicationCurrentSceneIndex();
  if (currentSceneIndex != sceneIndex) {
    if ((sceneIndex >= 0) &&
        (sceneIndex < _applicationScenes.size())) {
      getThreadUtils()->invokeInRendererThread(new MapBooBuilder_ChangeSceneTask(this, sceneIndex),
                                               true);
    }
  }
}

void MapBooBuilder::changeScene(const MapBoo_Scene* scene) {
  const int size = _applicationScenes.size();
  for (int i = 0; i < size; i++) {
    if (_applicationScenes[i] == scene) {
      changeScene(i);
      break;
    }
  }
}

void MapBooBuilder::changedCurrentScene() {
  recreateLayerSet();

  if (_g3mWidget != NULL) {
    _g3mWidget->setBackgroundColor(getCurrentBackgroundColor());

    // force immediate execution of PeriodicalTasks
    _g3mWidget->resetPeriodicalTasksTimeouts();
  }

  const MapBoo_Scene* currentScene = getApplicationCurrentScene();
  if (_applicationListener != NULL) {
    _applicationListener->onSceneChanged(_context,
                                         getApplicationCurrentSceneIndex(),
                                         currentScene);
  }
}

void MapBooBuilder::setApplicationScenes(const std::vector<MapBoo_Scene*>& applicationScenes) {
  const int currentScenesCount = _applicationScenes.size();
  for (int i = 0; i < currentScenesCount; i++) {
    MapBoo_Scene* scene = _applicationScenes[i];
    delete scene;
  }

  _applicationScenes.clear();

  _applicationScenes = applicationScenes;

  changedCurrentScene();
  
  if (_applicationListener != NULL) {
    _applicationListener->onScenesChanged(_context, _applicationScenes);
  }
}

void MapBooBuilder::setApplicationTubeOpened(bool open) {
  if (_isApplicationTubeOpen != open) {
    _isApplicationTubeOpen = open;
  }
}

SceneLighting* MapBooBuilder::createSceneLighting() {
  return new DefaultSceneLighting();
}
