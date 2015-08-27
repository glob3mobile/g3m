//
//  MapBooOLDBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/25/13.
//
//

#include "MapBooOLDBuilder.hpp"

#include "ILogger.hpp"
#include "CompositeRenderer.hpp"
#include "PlanetRenderer.hpp"
#include "PlanetTileTessellator.hpp"
#include "DefaultTileTexturizer.hpp"
#include "TilesRenderParameters.hpp"
#include "DownloadPriority.hpp"
#include "G3MWidget.hpp"
#include "SectorAndHeightCameraConstrainer.hpp"
#include "CameraRenderer.hpp"
#include "CameraSingleDragHandler.hpp"
#include "CameraDoubleDragHandler.hpp"
#include "CameraRotationHandler.hpp"
#include "CameraDoubleTapHandler.hpp"
#include "BusyMeshRenderer.hpp"
#include "GInitializationTask.hpp"
#include "PeriodicalTask.hpp"
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
#include "IDownloader.hpp"
#include "IBufferDownloadListener.hpp"
#include "HUDErrorRenderer.hpp"
#include "TerrainTouchListener.hpp"
#include "MarksRenderer.hpp"
#include "Mark.hpp"
#include "URLTemplateLayer.hpp"
#include "ErrorHandling.hpp"
#include "ICanvas.hpp"
#include "ICanvasUtils.hpp"
#include "DownloaderImageBuilder.hpp"

#include "LevelTileCondition.hpp"
#include "Info.hpp"

const std::string MapBooOLD_CameraPosition::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("[CameraPosition position=");
  isb->addString(_position.description());

  isb->addString(", heading=");
  isb->addString(_heading.description());

  isb->addString(", pitch=");
  isb->addString(_pitch.description());

  isb->addString(", animated=");
  isb->addBool(_animated);

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

MapBooOLD_Scene::~MapBooOLD_Scene() {
  delete _screenshot;
  delete _baseLayer;
  delete _overlayLayer;
  delete _cameraPosition;
  delete _sector;
}

const std::string MapBooOLD_MultiImage_Level::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[Level size=");
  isb->addInt(_width);
  isb->addString("x");
  isb->addInt(_height);
  isb->addString(", url=");
  isb->addString(_url.description());
  isb->addString("]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string MapBooOLD_MultiImage::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[MultiImage averageColor=");
  isb->addString(_averageColor.description());
  isb->addString(", _levels=[");
  const int levelsSize = _levels.size();
  for (int i = 0; i < levelsSize; i++) {
    if (i > 0) {
      isb->addString(", ");
    }
    isb->addString(_levels[i]->description());
  }
  isb->addString("]]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}

MapBooOLD_MultiImage_Level* MapBooOLD_MultiImage::getBestLevel(int width) const {
  const int levelsSize = _levels.size();
  if (levelsSize == 0) {
    return NULL;
  }

  for (int i = 0; i < levelsSize; i++) {
    MapBooOLD_MultiImage_Level* level = _levels[i];
    const int levelWidth = level->getWidth();
    if (levelWidth <= width) {
      if ((levelWidth < width) && (i > 0)) {
        return _levels[i - 1];
      }
      return level;
    }
  }

  // all levels are widther than width, so select the level with the less resolution
  return _levels[levelsSize - 1];
}

const std::string MapBooOLD_Scene::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("[Scene name=");
  isb->addString(_name);

  isb->addString(", description=");
  isb->addString(_description);

  isb->addString(", screenshot=");
  isb->addString((_screenshot == NULL) ? "null" : _screenshot->description());

  isb->addString(", backgroundColor=");
  isb->addString(_backgroundColor.description());

  isb->addString(", cameraPosition=");
  if (_cameraPosition == NULL) {
    isb->addString("NULL");
  }
  else {
    isb->addString(_cameraPosition->description());
  }

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

  isb->addString(", hasWarnings=");
  isb->addBool(_hasWarnings);

  isb->addString("]");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

MapBooOLDBuilder::MapBooOLDBuilder(const URL& serverURL,
                             const URL& tubesURL,
                             const std::string& applicationId,
                             MapBooOLD_ViewType viewType,
                             MapBooOLDApplicationChangeListener* applicationListener,
                             bool enableNotifications,
                             const std::string& token) :
_serverURL(serverURL),
_tubesURL(tubesURL),
_applicationId(applicationId),
_viewType(viewType),
_applicationName(""),
_applicationWebsite(""),
_applicationEMail(""),
_applicationAbout(""),
_applicationTimestamp(-1),
_applicationEventId(-1),
_token(token),
_gl(NULL),
_g3mWidget(NULL),
_storage(NULL),
_threadUtils(NULL),
_layerSet( new LayerSet() ),
_downloader(NULL),
_applicationListener(applicationListener),
_enableNotifications(enableNotifications),
_gpuProgramManager(NULL),
_isApplicationTubeOpen(false),
_initialParse(true),
_applicationCurrentSceneId("-1"),
_lastApplicationCurrentSceneId("-1"),
_context(NULL),
_webSocket(NULL),
_marksRenderer(NULL),
_hasParsedApplication(false)
{
  _featureInfoDownloadListener = new FeatureInfoDownloadListener(_applicationListener);
}

GPUProgramManager* MapBooOLDBuilder::getGPUProgramManager() {
  if (_gpuProgramManager == NULL) {
    _gpuProgramManager = createGPUProgramManager();
  }
  return _gpuProgramManager;
}

IDownloader* MapBooOLDBuilder::getDownloader() {
  if (_downloader == NULL) {
    _downloader = createDownloader();
  }
  return _downloader;
}

IThreadUtils* MapBooOLDBuilder::getThreadUtils() {
  if (_threadUtils == NULL) {
    _threadUtils = createThreadUtils();
  }
  return _threadUtils;
}

void MapBooOLDBuilder::setGL(GL *gl) {
  if (_gl != NULL) {
    //ILogger::instance()->logError("LOGIC ERROR: _gl already initialized");
    //return;
    THROW_EXCEPTION("LOGIC ERROR: _gl already initialized");
  }
  if (gl == NULL) {
    //ILogger::instance()->logError("LOGIC ERROR: _gl cannot be NULL");
    //return;
    THROW_EXCEPTION("LOGIC ERROR: _gl cannot be NULL");
  }
  _gl = gl;
}

GL* MapBooOLDBuilder::getGL() {
  if (_gl == NULL) {
    ILogger::instance()->logError("Logic Error: _gl not initialized");
  }
  return _gl;
}


class MapBooOLDBuilder_TerrainTouchListener : public TerrainTouchListener {
private:
  MapBooOLDBuilder* _mapBooBuilder;

public:
  MapBooOLDBuilder_TerrainTouchListener(MapBooOLDBuilder* mapBooBuilder) :
  _mapBooBuilder(mapBooBuilder)
  {
  }

  ~MapBooOLDBuilder_TerrainTouchListener() {

  }

  bool onTerrainTouch(const G3MEventContext* ec,
                      const Vector2F&        pixel,
                      const Camera*          camera,
                      const Geodetic3D&      position,
                      const Tile*            tile) {
    return _mapBooBuilder->onTerrainTouch(ec,
                                          pixel,
                                          camera,
                                          position,
                                          tile);
  }
};

const std::string MapBooOLDBuilder::escapeString(const std::string& str) const {
  const IStringUtils* su = IStringUtils::instance();

  return su->replaceAll(str, "\"", "\\\"");
}

const std::string MapBooOLDBuilder::toCameraPositionJSON(const MapBooOLD_CameraPosition* cameraPosition) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("{");

  const Geodetic3D position = cameraPosition->getPosition();

  isb->addString("\"latitude\":");
  isb->addDouble(position._latitude._degrees);

  isb->addString(",\"longitude\":");
  isb->addDouble(position._longitude._degrees);

  isb->addString(",\"height\":");
  isb->addDouble( position._height );

  isb->addString(",\"heading\":");
  isb->addDouble( cameraPosition->getHeading()._degrees );

  isb->addString(",\"pitch\":");
  isb->addDouble( cameraPosition->getPitch()._degrees );

  isb->addString(",\"animated\":");
  isb->addBool( true );

  isb->addString("}");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string MapBooOLDBuilder::toCameraPositionJSON(const Camera* camera) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("{");

  const Geodetic3D position = camera->getGeodeticPosition();

  isb->addString("\"latitude\":");
  isb->addDouble(position._latitude._degrees);

  isb->addString(",\"longitude\":");
  isb->addDouble(position._longitude._degrees);

  isb->addString(",\"height\":");
  isb->addDouble( position._height );

  isb->addString(",\"heading\":");
  isb->addDouble( camera->getHeading()._degrees );

  isb->addString(",\"pitch\":");
  isb->addDouble( camera->getPitch()._degrees );

  isb->addString(",\"animated\":");
  isb->addBool( true );

  isb->addString("}");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string MapBooOLDBuilder::getSendNotificationCommand(const Geodetic2D&            position,
                                                            const MapBooOLD_CameraPosition* cameraPosition,
                                                            const std::string&           message,
                                                            const URL*                   iconURL) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("notification=");

  isb->addString("{");

  isb->addString("\"latitude\":");
  isb->addDouble(position._latitude._degrees);

  isb->addString(",\"longitude\":");
  isb->addDouble(position._longitude._degrees);

  isb->addString(",\"message\":");
  isb->addString("\"");
  isb->addString( escapeString(message) );
  isb->addString("\"");

  if (iconURL != NULL) {
    isb->addString(",\"iconURL\":");
    isb->addString("\"");
    isb->addString( escapeString(iconURL->_path) );
    isb->addString("\"");
  }

  isb->addString(",\"cameraPosition\":");
  isb->addString( toCameraPositionJSON(cameraPosition) );

  isb->addString("}");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

const std::string MapBooOLDBuilder::getSendNotificationCommand(const Geodetic2D&  position,
                                                            const Camera*      camera,
                                                            const std::string& message,
                                                            const URL*         iconURL) const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();

  isb->addString("notification=");

  isb->addString("{");

  isb->addString("\"latitude\":");
  isb->addDouble(position._latitude._degrees);

  isb->addString(",\"longitude\":");
  isb->addDouble(position._longitude._degrees);

  isb->addString(",\"message\":");
  isb->addString("\"");
  isb->addString( escapeString(message) );
  isb->addString("\"");

  if (iconURL != NULL) {
    isb->addString(",\"iconURL\":");
    isb->addString("\"");
    isb->addString( escapeString(iconURL->_path) );
    isb->addString("\"");
  }

  isb->addString(",\"cameraPosition\":");
  isb->addString( toCameraPositionJSON(camera) );

  isb->addString("}");

  const std::string s = isb->getString();
  delete isb;
  return s;
}

void MapBooOLDBuilder::sendNotification(const Geodetic2D&            position,
                                     const MapBooOLD_CameraPosition* cameraPosition,
                                     const std::string&           message,
                                     const URL*                   iconURL) const {
  if ((_webSocket != NULL) && _isApplicationTubeOpen) {
    _webSocket->send( getSendNotificationCommand(position,
                                                 cameraPosition,
                                                 message,
                                                 iconURL) );
  }
  else {
    ILogger::instance()->logError("Can't send notification, websocket disconnected");
  }
}

void MapBooOLDBuilder::sendNotification(const Geodetic2D&  position,
                                     const Camera*      camera,
                                     const std::string& message,
                                     const URL*         iconURL) const {
  if ((_webSocket != NULL) && _isApplicationTubeOpen) {
    _webSocket->send( getSendNotificationCommand(position,
                                                 camera,
                                                 message,
                                                 iconURL) );
  }
  else {
    ILogger::instance()->logError("Can't send notification, websocket disconnected");
  }
}

bool MapBooOLDBuilder::onTerrainTouch(const G3MEventContext* ec,
                                   const Vector2F&        pixel,
                                   const Camera*          camera,
                                   const Geodetic3D&      position,
                                   const Tile*            tile) {
  if (_applicationListener != NULL) {
    _applicationListener->onTerrainTouch(this,
                                         ec,
                                         pixel,
                                         camera,
                                         position,
                                         tile);
  }

  return true;
}



PlanetRenderer* MapBooOLDBuilder::createPlanetRenderer() {
  const bool skirted = true;
  TileTessellator* tessellator = new PlanetTileTessellator(skirted, Sector::fullSphere());

  ElevationDataProvider* elevationDataProvider = NULL;
  const float verticalExaggeration = 1;
  
  TileTexturizer* texturizer = new DefaultTileTexturizer(new DownloaderImageBuilder(URL("http://www.mapboo.com/web/img/tileNotFound.jpg")));

  const bool renderDebug = false;
  const bool useTilesSplitBudget = true;
  const bool forceFirstLevelTilesRenderOnStart = true;
  const bool incrementalTileQuality = false;
  const Quality quality = QUALITY_LOW;

  const TilesRenderParameters* parameters = new TilesRenderParameters(renderDebug,
                                                                      useTilesSplitBudget,
                                                                      forceFirstLevelTilesRenderOnStart,
                                                                      incrementalTileQuality,
                                                                      quality);
  

  const bool showStatistics = false;
  long long tileDownloadPriority = DownloadPriority::HIGHER;

  const Sector renderedSector = Sector::fullSphere();
  const bool renderTileMeshes = true;

  const bool logTilesPetitions = false;

  TileRenderingListener* tileRenderingListener = NULL;
  
  ChangedRendererInfoListener* changedRendererInfoListener = NULL;

  TouchEventType touchEventTypeOfTerrainTouchListener = DownUp;
  
  PlanetRenderer* result = new PlanetRenderer(tessellator,
                                              elevationDataProvider,
                                              true,
                                              verticalExaggeration,
                                              texturizer,
                                              _layerSet,
                                              parameters,
                                              showStatistics,
                                              tileDownloadPriority,
                                              renderedSector,
                                              renderTileMeshes,
                                              logTilesPetitions,
                                              tileRenderingListener,
                                              changedRendererInfoListener,
                                              touchEventTypeOfTerrainTouchListener);

  if (_enableNotifications) {
    result->addTerrainTouchListener(new MapBooOLDBuilder_TerrainTouchListener(this));
  }
  
  return result;
}

const Planet* MapBooOLDBuilder::createPlanet() {
  //return Planet::createEarth();
  return Planet::createSphericalEarth();
}

std::vector<ICameraConstrainer*>* MapBooOLDBuilder::createCameraConstraints(const Planet* planet,
                                                                         PlanetRenderer* planetRenderer) {
  std::vector<ICameraConstrainer*>* cameraConstraints = new std::vector<ICameraConstrainer*>;
  //SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();

  const Geodetic3D initialCameraPosition = planet->getDefaultCameraPosition(Sector::fullSphere());

  cameraConstraints->push_back( new RenderedSectorCameraConstrainer(planetRenderer,
                                                                    initialCameraPosition._height * 1.2) );

  return cameraConstraints;
}

CameraRenderer* MapBooOLDBuilder::createCameraRenderer() {
  CameraRenderer* cameraRenderer = new CameraRenderer();
  const bool useInertia = true;
  cameraRenderer->addHandler(new CameraSingleDragHandler(useInertia));
  cameraRenderer->addHandler(new CameraDoubleDragHandler());
  cameraRenderer->addHandler(new CameraRotationHandler());
  //cameraRenderer->addHandler(new CameraDoubleTapHandler());

  return cameraRenderer;
}

ProtoRenderer* MapBooOLDBuilder::createBusyRenderer() {
  return new BusyMeshRenderer(Color::newFromRGBA(0, 0, 0, 1));
}


class Mapboo_ErrorMessagesCustomizer : public ErrorMessagesCustomizer {
private:
  MapBooOLDBuilder* _mbBuilder;

public:
  Mapboo_ErrorMessagesCustomizer(MapBooOLDBuilder* mbBuilder) {
    _mbBuilder = mbBuilder;
  }

  ~Mapboo_ErrorMessagesCustomizer() {}

  std::vector<std::string> customize(const std::vector<std::string>& errors) {
    std::vector<std::string> customizedErrorMessages;
    const IStringUtils* stringUtils = IStringUtils::instance();
    const int errorsSize = errors.size();

    const std::string appNotFound = "Invalid request: Application #" + _mbBuilder->getApplicationId() + " not found";

    for (int i = 0; i < errorsSize; i++) {
      std::string error = errors.at(i);
      if (stringUtils->beginsWith(error, appNotFound)) {
        customizedErrorMessages.push_back("Oops, application not found!");
        break;
      }
      customizedErrorMessages.push_back(error);
    }

    return customizedErrorMessages;
  }
};

ErrorRenderer* MapBooOLDBuilder::createErrorRenderer() {
  return new HUDErrorRenderer(new Mapboo_ErrorMessagesCustomizer(this));
}

MapQuestLayer* MapBooOLDBuilder::parseMapQuestLayer(const JSONObject* jsonLayer,
                                                 const TimeInterval& timeToCache) const {
  const std::string imagery = jsonLayer->getAsString("imagery", "<imagery not present>");
  if (imagery.compare("OpenAerial") == 0) {
    return MapQuestLayer::newOpenAerial(timeToCache);
  }

  // defaults to OSM
  return MapQuestLayer::newOSM(timeToCache);
}

BingMapsLayer* MapBooOLDBuilder::parseBingMapsLayer(const JSONObject* jsonLayer,
                                                 const TimeInterval& timeToCache) const {
  const std::string key = jsonLayer->getAsString("key", "");
  const std::string imagerySet = jsonLayer->getAsString("imagerySet", "Aerial");

  return new BingMapsLayer(imagerySet,
                           key,
                           timeToCache,
                           true, // readExpired
                           2, // initialLevel
                           25, // maxLevel
                           1, // transparency
                           NULL, // condition
                           new std::vector<const Info*>()); // disclaimerInfo
}

CartoDBLayer* MapBooOLDBuilder::parseCartoDBLayer(const JSONObject* jsonLayer,
                                               const bool transparent,
                                               const TimeInterval& timeToCache) const {
  const std::string userName = jsonLayer->getAsString("userName", "");
  const std::string table    = jsonLayer->getAsString("table",    "");

  return new CartoDBLayer(userName, //
                          table, //
                          timeToCache, //
                          true, // readExpired
                          1, // transparency
                          transparent, // isTransparent
                          NULL, // condition,
                          new std::vector<const Info*>()); // disclaimerInfo
}

MapBoxLayer* MapBooOLDBuilder::parseMapBoxLayer(const JSONObject* jsonLayer,
                                             const TimeInterval& timeToCache) const {
  const std::string mapKey = jsonLayer->getAsString("mapKey", "");

  return new MapBoxLayer(mapKey,
                         timeToCache,
                         true, // readExpired
                         1, // initialLevel
                         19, // maxLevel
                         1, // transparency
                         NULL, // condition
                         new std::vector<const Info*>()); // disclaimerInfo
}

WMSLayer* MapBooOLDBuilder::parseWMSLayer(const JSONObject* jsonLayer,
                                       const bool transparent) const {

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
  const Sector sector = parseSector(jsonLayer, "validSector");
  std::string imageFormat = jsonLayer->getAsString("imageFormat", "image/png");
  const std::string srs = jsonLayer->getAsString("projection", "EPSG:4326");
  LayerTilesRenderParameters* layerTilesRenderParameters = NULL;
  if (srs.compare("EPSG:4326") == 0) {
    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultWGS84(0, 17);
  }
  else if (srs.compare("EPSG:3857") == 0) {
    layerTilesRenderParameters = LayerTilesRenderParameters::createDefaultMercator(0, 17);
  }
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
                      srs,
                      style,
                      transparent,
                      NULL,
                      timeToCache,
                      readExpired,
                      layerTilesRenderParameters);
}

URLTemplateLayer* MapBooOLDBuilder::parseURLTemplateLayer(const JSONObject* jsonLayer,
                                                       const bool transparent) const {
  const std::string urlTemplate = jsonLayer->getAsString("url", "");

  const int firstLevel = (int) jsonLayer->getAsNumber("firstLevel", 1);
  const int maxLevel   = (int) jsonLayer->getAsNumber("maxLevel", 19);

  const std::string projection = jsonLayer->getAsString("projection", "EPSG:3857");
  const bool mercator = (projection == "EPSG:3857");

  const Sector sector = parseSector(jsonLayer, "validSector");

  URLTemplateLayer* result;
  if (mercator) {
    result = URLTemplateLayer::newMercator(urlTemplate,
                                           sector,
                                           transparent,
                                           firstLevel,
                                           maxLevel,
                                           TimeInterval::fromDays(30));
  }
  else {
//    result = URLTemplateLayer::newWGS84(urlTemplate,
//                                        sector,
//                                        transparent,
//                                        firstLevel,
//                                        maxLevel,
//                                        TimeInterval::fromDays(30));
    
    result = new URLTemplateLayer(urlTemplate,
                                  sector,
                                  transparent,
                                  TimeInterval::fromDays(30),
                                  true,
                                  new LevelTileCondition(firstLevel, maxLevel),
                                  LayerTilesRenderParameters::createDefaultWGS84(sector, 1, maxLevel));
  }

  return result;
}

Layer* MapBooOLDBuilder::parseLayer(const JSONBaseObject* jsonBaseObjectLayer) const {
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

  const bool transparent = jsonLayer->getAsBoolean("transparent", false);
  const std::string layerType = jsonLayer->getAsString("layer", "<layer not present>");
  Layer* layer;
  if (layerType.compare("OSM") == 0) {
    layer = new OSMLayer(defaultTimeToCache,
                         true, // readExpired,
                         2, // initialLevel,
                         1, // transparency,
                         NULL, // condition,
                         new std::vector<const Info*>()); //disclaimerInfo
  }
  else if (layerType.compare("MapQuest") == 0) {
    layer = parseMapQuestLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("BingMaps") == 0) {
    layer = parseBingMapsLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("CartoDB") == 0) {
    layer = parseCartoDBLayer(jsonLayer, transparent, defaultTimeToCache);
  }
  else if (layerType.compare("MapBox") == 0) {
    layer = parseMapBoxLayer(jsonLayer, defaultTimeToCache);
  }
  else if (layerType.compare("WMS") == 0) {
    layer = parseWMSLayer(jsonLayer, transparent);
  }
  else if (layerType.compare("URLTemplate") == 0) {
    layer = parseURLTemplateLayer(jsonLayer, transparent);
  }
  else {
    ILogger::instance()->logError("Unsupported layer type \"%s\"", layerType.c_str());
    ILogger::instance()->logError("%s", jsonBaseObjectLayer->description().c_str());
    return NULL;
  }
  
  const std::string layerAttribution = jsonLayer->getAsString("attribution", "");
  if (layerAttribution.compare("") != 0) {
    layer->addInfo(new Info(layerAttribution));
  }
  return layer;
}

Color MapBooOLDBuilder::parseColor(const JSONString* jsonColor) const {
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

MapBooOLD_MultiImage_Level* MapBooOLDBuilder::parseMultiImageLevel(const JSONObject* jsonObject) const {
  const JSONString* jsURL = jsonObject->getAsString("url");
  if (jsURL == NULL) {
    return NULL;
  }

  const JSONNumber* jsWidth = jsonObject->getAsNumber("width");
  if (jsWidth == NULL) {
    return NULL;
  }

  const JSONNumber* jsHeight = jsonObject->getAsNumber("height");
  if (jsHeight == NULL) {
    return NULL;
  }

  return new MapBooOLD_MultiImage_Level(URL(_serverURL, "/images/" + jsURL->value()),
                                     (int) jsWidth->value(),
                                     (int) jsHeight->value());
}

MapBooOLD_MultiImage* MapBooOLDBuilder::parseMultiImage(const JSONObject* jsonObject) const {
  if (jsonObject == NULL) {
    return NULL;
  }

  Color averageColor = parseColor( jsonObject->getAsString("averageColor") );

  std::vector<MapBooOLD_MultiImage_Level*> levels;

  const JSONArray* jsLevels = jsonObject->getAsArray("levels");
  if (jsLevels != NULL) {
    const int levelsCount = jsLevels->size();
    for (int i = 0; i < levelsCount; i++) {
      MapBooOLD_MultiImage_Level* level = parseMultiImageLevel( jsLevels->getAsObject(i) );
      if (level != NULL) {
        levels.push_back(level);
      }
    }
  }

  return new MapBooOLD_MultiImage(averageColor, levels);
}

const MapBooOLD_CameraPosition* MapBooOLDBuilder::parseCameraPosition(const JSONObject* jsonObject) const {
  if (jsonObject == NULL) {
    return NULL;
  }

  const double latitudeInDegress  = jsonObject->getAsNumber("latitude", 0);
  const double longitudeInDegress = jsonObject->getAsNumber("longitude", 0);
  const double height             = jsonObject->getAsNumber("height", 0);

  const double headingInDegrees = jsonObject->getAsNumber("heading", 0);
  const double pitchInDegrees   = jsonObject->getAsNumber("pitch", 0);

  const bool animated = jsonObject->getAsBoolean("animated", true);

  return new MapBooOLD_CameraPosition(Geodetic3D::fromDegrees(latitudeInDegress, longitudeInDegress, height),
                                   Angle::fromDegrees(headingInDegrees),
                                   Angle::fromDegrees(pitchInDegrees),
                                   animated);
}

//const std::string MapBooOLDBuilder::parseSceneId(const JSONObject* jsonObject) const {
//  if (jsonObject == NULL) {
//    ILogger::instance()->logError("Missing Scene ID");
//    return "";
//  }
//
//  return jsonObject->getAsString("$oid", "");
//}

Sector* MapBooOLDBuilder::parseSector(const JSONBaseObject* jsonBaseObjectLayer) const {
  if (jsonBaseObjectLayer == NULL) {
    return NULL;
  }

  if (jsonBaseObjectLayer->asNull() != NULL) {
    return NULL;
  }

  const JSONObject* jsonObject = jsonBaseObjectLayer->asObject();
  if (jsonObject == NULL) {
    return NULL;
  }

  const double lowerLat = jsonObject->getAsNumber("lowerLat",  -90.0);
  const double lowerLon = jsonObject->getAsNumber("lowerLon", -180.0);
  const double upperLat = jsonObject->getAsNumber("upperLat",   90.0);
  const double upperLon = jsonObject->getAsNumber("upperLon",  180.0);
  
  return new Sector(Geodetic2D::fromDegrees(lowerLat, lowerLon),
                    Geodetic2D::fromDegrees(upperLat, upperLon));
}

const Sector MapBooOLDBuilder::parseSector(const JSONObject* jsonObject, const std::string& paramName) const {
 
  const JSONObject* sector = jsonObject->getAsObject(paramName);
  
  if (sector == NULL) {
    return Sector::fullSphere();
  }
  
  if (sector->asNull() != NULL) {
    return Sector::fullSphere();
  }
  
  const double lowerLat = sector->getAsNumber("lowerLat",  -90.0);
  const double lowerLon = sector->getAsNumber("lowerLon", -180.0);
  const double upperLat = sector->getAsNumber("upperLat",   90.0);
  const double upperLon = sector->getAsNumber("upperLon",  180.0);
  
  return Sector(Geodetic2D::fromDegrees(lowerLat, lowerLon),
                Geodetic2D::fromDegrees(upperLat, upperLon));
}

MapBooOLD_Scene* MapBooOLDBuilder::parseScene(const JSONObject* jsonObject) const {
  if (jsonObject == NULL) {
    return NULL;
  }

  const bool hasWarnings = jsonObject->getAsBoolean("hasWarnings", false);
  
  const bool queryable = jsonObject->getAsBoolean("queryable", false);


  //  if (hasWarnings && (_viewType != VIEW_PRESENTATION)) {
  //    return NULL;
  //  }

  return new MapBooOLD_Scene(jsonObject->getAsString("id", ""),
                          jsonObject->getAsString("name", ""),
                          jsonObject->getAsString("description", ""),
                          parseMultiImage( jsonObject->getAsObject("screenshot") ),
                          parseColor( jsonObject->getAsString("backgroundColor") ),
                          parseCameraPosition( jsonObject->getAsObject("cameraPosition") ),
                          parseSector( jsonObject->get("sector") ),
                          parseLayer( jsonObject->get("baseLayer") ),
                          parseLayer( jsonObject->get("overlayLayer") ),
                          queryable,
                          hasWarnings);
}

const URL* MapBooOLDBuilder::parseURL(const JSONString* jsonString) const {
  if (jsonString == NULL) {
    return NULL;
  }
  return new URL(jsonString->value());
}

MapBooOLD_Notification* MapBooOLDBuilder::parseNotification(const JSONObject* jsonObject) const {
  if (jsonObject == NULL) {
    return NULL;
  }

  return new MapBooOLD_Notification(Geodetic2D::fromDegrees(jsonObject->getAsNumber("latitude",  0),
                                                         jsonObject->getAsNumber("longitude", 0)),
                                 parseCameraPosition( jsonObject->getAsObject("cameraPosition") ),
                                 jsonObject->getAsString("message", ""),
                                 parseURL( jsonObject->getAsString("iconURL") )
                                 );
}

std::vector<MapBooOLD_Notification*>* MapBooOLDBuilder::parseNotifications(const JSONArray* jsonArray) const {
  std::vector<MapBooOLD_Notification*>* result = new std::vector<MapBooOLD_Notification*>();

  if (jsonArray != NULL) {
    const int size = jsonArray->size();
    for (int i = 0; i < size; i++) {
      MapBooOLD_Notification* notification = parseNotification( jsonArray->getAsObject(i) );
      if (notification != NULL) {
        result->push_back(notification);
      }
    }
  }

  return result;
}

void MapBooOLDBuilder::parseApplicationEventsJSON(const std::string& json,
                                              const URL& url) {
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(json, true);
  if (jsonBaseObject == NULL) {
    ILogger::instance()->logError("Can't parse ApplicationJSON from %s",
                                  url._path.c_str());
  }
  else {
    const JSONArray* jsonArray = jsonBaseObject->asArray();
    if (jsonArray != NULL) {
      const int size = jsonArray->size();
      for (int i = 0; i < size; i++) {
        const JSONObject* jsonObject = jsonArray->getAsObject(i);
        parseApplicationJSON(jsonObject, url);
      }
    }
    else {
      parseApplicationJSON(json, url);
    }
  }
  delete jsonBaseObject;
}


void MapBooOLDBuilder::parseApplicationJSON(const JSONObject* jsonObject,
                                         const URL& url) {
  std::vector<std::string> errors;

  if (jsonObject == NULL) {
    ILogger::instance()->logError("Invalid ApplicationJSON");
  }
  else {
    const JSONString* jsonError = jsonObject->getAsString("error");
    if (jsonError == NULL) {
      const int eventId = (int) jsonObject->getAsNumber("eventId", 0);
      const int timestamp = (int) jsonObject->getAsNumber("timestamp", 0);
      
      if (getApplicationEventId() != eventId) {
        const JSONString* jsonName = jsonObject->getAsString("name");
        if (jsonName != NULL) {
          setApplicationName( jsonName->value() );
        }
        
        const JSONString* jsonWebsite = jsonObject->getAsString("website");
        if (jsonWebsite != NULL) {
          setApplicationWebsite( jsonWebsite->value() );
        }
        
        const JSONString* jsonEMail = jsonObject->getAsString("email");
        if (jsonEMail != NULL) {
          setApplicationEMail( jsonEMail->value() );
        }
        
        const JSONString* jsonAbout = jsonObject->getAsString("about");
        if (jsonAbout != NULL) {
          setApplicationAbout( jsonAbout->value() );
        }
        
        const JSONObject* jsonScene = jsonObject->getAsObject("scene");
        if (jsonScene != NULL) {
          parseSceneEventAndUpdateScene(jsonScene);
        }
        
        const JSONArray* jsonAllScenes = jsonObject->getAsArray("scenes");
        if (jsonAllScenes != NULL) {
          std::vector<MapBooOLD_Scene*> scenes;
          
          const int scenesCount = jsonAllScenes->size();
          for (int i = 0; i < scenesCount; i++) {
            MapBooOLD_Scene* scene = parseScene( jsonAllScenes->getAsObject(i) );
            if (scene != NULL) {
              scenes.push_back(scene);
            }
          }
          
          setApplicationScenes(scenes);
        }
        
        const JSONObject* jsonScenes = jsonObject->getAsObject("scenes");
        if (jsonScenes != NULL) {
          const JSONObject* jsonPutScene = jsonScenes->getAsObject("putScene");
          if (jsonPutScene != NULL) {
            const JSONNumber* jsonPosition = jsonPutScene->getAsNumber("position");
            int position = (jsonPosition != NULL) ? (int) jsonPosition->value() : 0;
            const JSONObject* jsonNewScene = jsonPutScene->getAsObject("scene");
            if (jsonNewScene != NULL) {
              MapBooOLD_Scene* scene = parseScene(jsonNewScene);
              if (scene != NULL) {
                addApplicationScene(scene, position);
              }
            }
          }
          else {
            const JSONObject* jsonDeleteScene = jsonScenes->getAsObject("deleteScene");
            if (jsonDeleteScene != NULL) {
              const JSONString* jsonSceneId = jsonDeleteScene->getAsString("sceneId");
              if (jsonSceneId != NULL) {
                deleteApplicationScene(jsonSceneId->value());
              }
            }
          }
        }
        
        setApplicationEventId(eventId);
        setApplicationTimestamp(timestamp);
        saveApplicationData();
        setHasParsedApplication();
      }
      
      const JSONString* jsonCurrentSceneId = jsonObject->getAsString("currentSceneId");
      if (jsonCurrentSceneId != NULL) {
        setApplicationCurrentSceneId( jsonCurrentSceneId->value() );
      }
      
      if (_enableNotifications) {
        const JSONArray* jsonNotifications = jsonObject->getAsArray("notifications");
        if (jsonNotifications != NULL) {
          addApplicationNotifications( parseNotifications(jsonNotifications) );
        }
        
        const JSONObject* jsonNotification = jsonObject->getAsObject("notification");
        if (jsonNotification != NULL) {
          addApplicationNotification( parseNotification(jsonNotification) );
        }
      }
      
      if (_initialParse) {
        _initialParse = false;
        if (_applicationCurrentSceneId.compare("-1") == 0) {
          if (_applicationScenes.size() > 0) {
            setApplicationCurrentSceneId(_applicationScenes.at(0)->getId());
          }
        }
      }
    }
    else {
      errors.push_back(jsonError->value().c_str());
      ILogger::instance()->logError("Server Error: %s",
                                    jsonError->value().c_str());
      if (_initialParse) {
        _initialParse = false;
        setHasParsedApplication();
      }
    }
  }
  _mbErrorRenderer->setErrors(errors);
}


void MapBooOLDBuilder::parseApplicationJSON(const std::string& json,
                                         const URL& url) {
  const JSONBaseObject* jsonBaseObject = IJSONParser::instance()->parse(json, true);

  if (jsonBaseObject == NULL) {
    ILogger::instance()->logError("Can't parse ApplicationJSON from %s",
                                  url._path.c_str());
  }
  else {
    const JSONObject* jsonObject = jsonBaseObject->asObject();
    parseApplicationJSON(jsonObject, url);
  }
  delete jsonBaseObject;
}


void MapBooOLDBuilder::parseSceneEventAndUpdateScene(const JSONObject* jsonObject) {
  if (jsonObject == NULL) {
    return;
  }
  
  const JSONString* jsonSceneToBeUpdatedID = jsonObject->getAsString("id");
  if (jsonSceneToBeUpdatedID == NULL) {
    return;
  }
  const std::string sceneToBeUpdatedID = jsonSceneToBeUpdatedID->value();
  const int scenesCount = _applicationScenes.size();
  for (int i = 0; i < scenesCount; i++) {
    const std::string sceneID = _applicationScenes[i]->getId();
    if (sceneID.compare(sceneToBeUpdatedID) == 0) {
      MapBooOLD_Scene* oldScene = _applicationScenes[i];
      
      const std::string name = jsonObject->getAsString("name", oldScene->getName());
      const std::string description = jsonObject->getAsString("description", oldScene->getDescription());
      const JSONBaseObject* jboScreenshot = jsonObject->get("screenshot");
      const MapBooOLD_MultiImage* screenshot;
      if (jboScreenshot != NULL) {
        screenshot = parseMultiImage(jboScreenshot->asObject());
      }
      else {
        const MapBooOLD_MultiImage* oldScreenshot = oldScene->getScreenshot();
        screenshot = (oldScreenshot != NULL) ? oldScreenshot->deepCopy() : NULL;
      }
      const JSONBaseObject* jboBackgroundColor = jsonObject->get("backgroundColor");
      const Color backgroundColor = (jboBackgroundColor != NULL) ? parseColor(jboBackgroundColor->asString()) : oldScene->getBackgroundColor();
      const JSONBaseObject* jboCameraPosition = jsonObject->get("cameraPosition");
      const MapBooOLD_CameraPosition* cameraPosition;
      if (jboCameraPosition != NULL) {
        cameraPosition = parseCameraPosition(jboCameraPosition->asObject());
      }
      else {
        const MapBooOLD_CameraPosition* oldCameraPosition = oldScene->getCameraPosition();
        cameraPosition = (oldCameraPosition != NULL) ? new MapBooOLD_CameraPosition(oldCameraPosition->getPosition(),
                                                                                 oldCameraPosition->getHeading(),
                                                                                 oldCameraPosition->getPitch(),
                                                                                 oldCameraPosition->isAnimated()) : NULL ;
      }
      const JSONBaseObject* jboSector = jsonObject->get("sector");
      const Sector* sector;
      if (jboSector != NULL) {
        sector = parseSector(jboSector->asObject());
      }
      else {
        const Sector* oldSector = oldScene->getSector();
        sector = (oldSector != NULL) ? new Sector(oldSector->_lower, oldSector->_upper) : NULL;
      }
      const JSONBaseObject* jboBaseLayer = jsonObject->get("baseLayer");
      Layer* baseLayer = (jboBaseLayer != NULL) ? parseLayer(jboBaseLayer->asObject()) : oldScene->getBaseLayer()->copy();
      const JSONBaseObject* jboOverlayLayer = jsonObject->get("overlayLayer");
      Layer* oldOverlayLayer = (oldScene->getOverlayLayer() != NULL) ? oldScene->getOverlayLayer()->copy() : NULL;
      Layer* overlayLayer = (jboOverlayLayer != NULL) ? parseLayer(jboOverlayLayer->asObject()) : oldOverlayLayer;
      
      const bool hasWarnings = jsonObject->getAsBoolean("hasWarnings", false);
      const bool queryable = jsonObject->getAsBoolean("queryable", oldScene->isQueryable());
      const bool cameraPositionChaged = (jboCameraPosition != NULL);
      
      MapBooOLD_Scene* newScene = new MapBooOLD_Scene(sceneToBeUpdatedID, //
                                                name, //
                                                description, //
                                                screenshot, //
                                                backgroundColor, //
                                                cameraPosition, //
                                                sector, //
                                                baseLayer, //
                                                overlayLayer, //
                                                queryable, //
                                                hasWarnings);
      
      _applicationScenes[i] = newScene;
      
      if (sceneID.compare(_applicationCurrentSceneId) == 0) {
        updateVisibleScene(cameraPositionChaged);
      }
      
      if (_applicationListener != NULL) {
        _applicationListener->onSceneChanged(_context, newScene);
      }
      fireOnScenesChanged();
      
      delete oldScene;
      
      break;
    }
  }
}

void MapBooOLDBuilder::addApplicationNotifications(const std::vector<MapBooOLD_Notification*>* notifications) {
  if (notifications == NULL) {
    return;
  }

  const int size = notifications->size();
  for (int i = 0; i < size; i++) {
    MapBooOLD_Notification* notification = notifications->at(i);
    if (notification != NULL) {
      addApplicationNotification(notification);
    }
  }

  delete notifications;
}

void MapBooOLDBuilder::addApplicationNotification(MapBooOLD_Notification* notification) {
  if (_marksRenderer != NULL) {
    const std::string message = notification->getMessage();

    const bool hasMessage = (message.size() > 0);
    const URL* iconURL = notification->getIconURL();

    const Geodetic2D position = notification->getPosition();

    bool newMark = false;

    if (hasMessage) {
      if (iconURL == NULL) {
        _marksRenderer->addMark( new Mark(message,
                                          Geodetic3D(position, 0),
                                          ABSOLUTE,
                                          0) );
      }
      else {
        _marksRenderer->addMark( new Mark(message,
                                          *iconURL,
                                          Geodetic3D(position, 0),
                                          ABSOLUTE,
                                          0) );
      }
      newMark = true;
    }
    else {
      if (iconURL != NULL) {
        _marksRenderer->addMark( new Mark(*iconURL,
                                          Geodetic3D(position, 0),
                                          ABSOLUTE,
                                          0) );
        newMark = true;
      }
    }

    if (newMark) {
      const MapBooOLD_CameraPosition* cameraPosition = notification->getCameraPosition();
      if (cameraPosition != NULL) {
        setCameraPosition(cameraPosition, true);
      }
    }
  }

  delete notification;
}

void MapBooOLDBuilder::setApplicationCurrentSceneId(const std::string& currentSceneId) {
  if (_applicationCurrentSceneId.compare(currentSceneId) != 0) {
    const int scenesCount = _applicationScenes.size();
    for (int i = 0; i < scenesCount; i++) {
      const std::string sceneId = _applicationScenes[i]->getId();
      if (sceneId.compare(currentSceneId) == 0) {
        _applicationCurrentSceneId = currentSceneId;
        changedCurrentScene();
        
        break;
      }
    }
  }
}

LayerSet* MapBooOLD_Scene::createLayerSet() const {
  LayerSet* layerSet = new LayerSet();
  if (_baseLayer != NULL) {
    layerSet->addLayer(_baseLayer->copy());
  }
  if (_overlayLayer != NULL) {
    layerSet->addLayer(_overlayLayer->copy());
  }
  return layerSet;
}

void MapBooOLDBuilder::recreateLayerSet() {
  const MapBooOLD_Scene* scene = getApplicationCurrentScene();

  if (scene == NULL) {
    _layerSet->removeAllLayers(true);
  }
  else {
    LayerSet* newLayerSet = scene->createLayerSet();
    if (!newLayerSet->isEquals(_layerSet)) {
      _layerSet->removeAllLayers(true);
      _layerSet->takeLayersFrom(newLayerSet);
    }
    delete newLayerSet;
  }
}

const URL MapBooOLDBuilder::createApplicationTubeURL() const {
  const std::string tubesPath = _tubesURL._path;

  std::string view;
  switch (_viewType) {
    case VIEW_PRESENTATION:
      view = "presentation";
      break;
    case VIEW_EDITION_PREVIEW:
      view = "edition-preview";
      break;
    default:
      view = "runtime";
  }

  return URL(tubesPath + "/application/" + _applicationId + "/" + view, false);
}


class MapBooOLDBuilder_TubeWatchdogPeriodicalTask : public GTask {
private:
  MapBooOLDBuilder* _builder;
  bool _firstRun;

public:
  MapBooOLDBuilder_TubeWatchdogPeriodicalTask(MapBooOLDBuilder* builder) :
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
        _builder->pollApplicationDataFromServer(context);
        
        _builder->openApplicationTube(context);
      }
    }
  }

};


std::vector<PeriodicalTask*>* MapBooOLDBuilder::createPeriodicalTasks() {
  std::vector<PeriodicalTask*>* periodicalTasks = new std::vector<PeriodicalTask*>();

  periodicalTasks->push_back(new PeriodicalTask(TimeInterval::fromSeconds(5),
                                                new MapBooOLDBuilder_TubeWatchdogPeriodicalTask(this)));

  return periodicalTasks;
}

IStorage* MapBooOLDBuilder::getStorage() {
  if (_storage == NULL) {
    _storage = createStorage();
  }
  return _storage;
}

class MapBooOLDBuilder_ApplicationTubeListener : public IWebSocketListener {
private:
  MapBooOLDBuilder* _builder;

public:
  MapBooOLDBuilder_ApplicationTubeListener(MapBooOLDBuilder* builder) :
  _builder(builder)
  {
  }

  ~MapBooOLDBuilder_ApplicationTubeListener() {
  }

  void onOpen(IWebSocket* ws) {
    ILogger::instance()->logInfo("Tube '%s' opened!",
                                 ws->getURL()._path.c_str());
    _builder->setApplicationTubeOpened(true);
  }

  void onError(IWebSocket* ws,
               const std::string& error) {
    ILogger::instance()->logError("Error '%s' on Tube '%s'",
                                  error.c_str(),
                                  ws->getURL()._path.c_str());
    _builder->setApplicationTubeOpened(false);
  }

  void onMesssage(IWebSocket* ws,
                  const std::string& message) {
    //ILogger::instance()->logInfo(message);
    _builder->parseApplicationJSON(message, ws->getURL());
  }

  void onClose(IWebSocket* ws) {
    ILogger::instance()->logError("Tube '%s' closed!",
                                  ws->getURL()._path.c_str());
    _builder->setApplicationTubeOpened(false);
  }
};

class MapBooOLDBuilder_ApplicationTubeConnector : public GInitializationTask {
private:
  MapBooOLDBuilder* _builder;

public:
  MapBooOLDBuilder_ApplicationTubeConnector(MapBooOLDBuilder* builder) :
  _builder(builder)
  {
  }

  void run(const G3MContext* context) {
    _builder->setContext(context);
    _builder->openApplicationTube(context);
  }

  bool isDone(const G3MContext* context) {
    return _builder->hasParsedApplication();
  }
};

void MapBooOLDBuilder::setContext(const G3MContext* context) {
  _context = context;
}

MapBooOLDBuilder::~MapBooOLDBuilder() {

}

class MapBooOLDBuilder_RestJSON : public IBufferDownloadListener {
private:
  MapBooOLDBuilder* _builder;

public:
  MapBooOLDBuilder_RestJSON(MapBooOLDBuilder* builder) :
  _builder(builder)
  {
  }

  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    _builder->parseApplicationEventsJSON(buffer->getAsString(), url);
    delete buffer;
  }

  void onError(const URL& url) {
    ILogger::instance()->logError("Can't download %s", url._path.c_str());
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

const URL MapBooOLDBuilder::createApplicationPollURL() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_serverURL._path);
  isb->addString("/poll/");
  isb->addString(_applicationId);
  isb->addString("?view=");
  isb->addString(getViewAsString());
  isb->addString("&eventId=");
  isb->addInt(_applicationEventId);
  const std::string path = isb->getString();
  delete isb;

  return URL(path, false);
}

void MapBooOLDBuilder::openApplicationTube(const G3MContext* context) {

//  IDownloader* downloader = context->getDownloader();
//  downloader->requestBuffer(createApplicationRestURL(),
//                            DownloadPriority::HIGHEST,
//                            TimeInterval::zero(),
//                            false, // readExpired
//                            new MapBooOLDBuilder_RestJSON(this),
//                            true);

  const IFactory* factory = context->getFactory();
  _webSocket = factory->createWebSocket(createApplicationTubeURL(),
                                        new MapBooOLDBuilder_ApplicationTubeListener(this),
                                        true /* autodeleteListener  */,
                                        true /* autodeleteWebSocket */);
}

const std::string MapBooOLDBuilder::getApplicationCurrentSceneId() {
  return _applicationCurrentSceneId;
}

const MapBooOLD_Scene* MapBooOLDBuilder::getApplicationCurrentScene() {
  const std::string currentSceneId = getApplicationCurrentSceneId();
  
  const int scenesCount = _applicationScenes.size();
  for (int i = 0; i < scenesCount; i++) {
    const std::string sceneId = _applicationScenes[i]->getId();
    if (sceneId.compare(currentSceneId) == 0) {
      return _applicationScenes[i];
    }
  }
  return NULL;
}

Color MapBooOLDBuilder::getCurrentBackgroundColor() {
  const MapBooOLD_Scene* scene = getApplicationCurrentScene();
  return (scene == NULL) ? Color::black() : scene->getBackgroundColor();
}

MarksRenderer* MapBooOLDBuilder::getMarksRenderer() {
  if (_marksRenderer == NULL) {
    _marksRenderer = new MarksRenderer(false);
  }
  return _marksRenderer;
}

G3MWidget* MapBooOLDBuilder::create() {
  if (_g3mWidget != NULL) {
    ILogger::instance()->logError("The G3MWidget was already created, can't be created more than once");
    return NULL;
  }


  CompositeRenderer* mainRenderer = new CompositeRenderer();
  
  _mbErrorRenderer = new MapBooOLD_ErrorRenderer();
  mainRenderer->addRenderer(_mbErrorRenderer);
  
  const Planet* planet = createPlanet();

  PlanetRenderer* planetRenderer = createPlanetRenderer();
  mainRenderer->addRenderer(planetRenderer);

  mainRenderer->addRenderer(getMarksRenderer());

  std::vector<ICameraConstrainer*>* cameraConstraints = createCameraConstraints(planet, planetRenderer);

  GInitializationTask* initializationTask = new MapBooOLDBuilder_ApplicationTubeConnector(this);

  std::vector<PeriodicalTask*>* periodicalTasks = createPeriodicalTasks();

  ICameraActivityListener* cameraActivityListener = NULL;


  InitialCameraPositionProvider* icpp = new SimpleInitialCameraPositionProvider();

  MapBooOLD_HUDRenderer* hudRenderer = new MapBooOLD_HUDRenderer();
  InfoDisplay* infoDisplay = new MapBooOLD_HUDRendererInfoDisplay(hudRenderer);
  infoDisplay->showDisplay();
  
  _g3mWidget = G3MWidget::create(getGL(),
                                 getStorage(),
                                 getDownloader(),
                                 getThreadUtils(),
                                 cameraActivityListener,
                                 planet,
                                 *cameraConstraints,
                                 createCameraRenderer(),
                                 mainRenderer,
                                 createBusyRenderer(),
                                 createErrorRenderer(),
                                 hudRenderer,
                                 Color::black(),
                                 false,      // logFPS
                                 false,      // logDownloaderStatistics
                                 initializationTask,
                                 true,       // autoDeleteInitializationTask
                                 *periodicalTasks,
                                 getGPUProgramManager(),
                                 createSceneLighting(),
                                 icpp,
                                 infoDisplay);
  delete cameraConstraints;
  delete periodicalTasks;

  return _g3mWidget;
}


int MapBooOLDBuilder::getApplicationEventId() const {
  return _applicationEventId;
}

void MapBooOLDBuilder::setApplicationEventId(const int eventId) {
  _applicationEventId = eventId;
}

int MapBooOLDBuilder::getApplicationTimestamp() const {
  return _applicationTimestamp;
}

const std::string MapBooOLDBuilder::getApplicationId() {
  return _applicationId;
}

void MapBooOLDBuilder::saveApplicationData() const {
  //  std::string                _applicationId;
  //  std::string                _applicationName;
  //  std::string                _applicationWebsite;
  //  std::string                _applicationEMail;
  //  std::string                _applicationAbout;
  //  int                        _applicationTimestamp;
  //  std::vector<MapBooOLD_Scene*> _applicationScenes;
  //  int                        _applicationCurrentSceneIndex;
  //  int                        _lastApplicationCurrentSceneIndex;
//#warning Diego at work!
}

void MapBooOLDBuilder::setHasParsedApplication() {
  _hasParsedApplication = true;
}

bool MapBooOLDBuilder::hasParsedApplication() const {
  return _hasParsedApplication;
}

void MapBooOLDBuilder::setApplicationTimestamp(const int timestamp) {
  _applicationTimestamp = timestamp;
}

void MapBooOLDBuilder::setApplicationName(const std::string& name) {
  if (_applicationName.compare(name) != 0) {
    _applicationName = name;

    if (_applicationListener != NULL) {
      _applicationListener->onNameChanged(_context, _applicationName);
    }
  }
}

void MapBooOLDBuilder::setApplicationWebsite(const std::string& website) {
  if (_applicationWebsite.compare(website) != 0) {
    _applicationWebsite = website;

    if (_applicationListener != NULL) {
      _applicationListener->onWebsiteChanged(_context, _applicationWebsite);
    }
  }
}

void MapBooOLDBuilder::setApplicationEMail(const std::string& eMail) {
  if (_applicationEMail.compare(eMail) != 0) {
    _applicationEMail = eMail;

    if (_applicationListener != NULL) {
      _applicationListener->onEMailChanged(_context, _applicationEMail);
    }
  }
}

void MapBooOLDBuilder::setApplicationAbout(const std::string& about) {
  if (_applicationAbout.compare(about) != 0) {
    _applicationAbout = about;

    if (_applicationListener != NULL) {
      _applicationListener->onAboutChanged(_context, _applicationAbout);
    }
  }
}


class MapBooOLDBuilder_ChangeSceneTask : public GTask {
private:
  MapBooOLDBuilder* _builder;
  const std::string _sceneId;

public:
  MapBooOLDBuilder_ChangeSceneTask(MapBooOLDBuilder* builder,
                                const std::string& sceneId) :
  _builder(builder),
  _sceneId(sceneId)
  {
  }

  void run(const G3MContext* context) {
    _builder->rawChangeScene(_sceneId);
  }
};

void MapBooOLDBuilder::rawChangeScene(const std::string& sceneId) {
  _applicationCurrentSceneId = sceneId;

  changedCurrentScene();
}

void MapBooOLDBuilder::changeScene(const std::string& sceneId) {
  const std::string currentSceneId = getApplicationCurrentSceneId();
  if (currentSceneId.compare(sceneId) != 0) {
    const int scenesCount = _applicationScenes.size();
    for (int i = 0; i < scenesCount; i++) {
      const std::string iSceneId = _applicationScenes[i]->getId();
      if (sceneId.compare(iSceneId) == 0) {
        getThreadUtils()->invokeInRendererThread(new MapBooOLDBuilder_ChangeSceneTask(this, sceneId),
                                                 true);
        break;
      }
    }
  }
}

void MapBooOLDBuilder::changeScene(const MapBooOLD_Scene* scene) {
  const int size = _applicationScenes.size();
  for (int i = 0; i < size; i++) {
    if (_applicationScenes[i] == scene) {
      changeScene(scene->getId());
      break;
    }
  }
}

class MapBooOLDBuilder_DummyListener : public IBufferDownloadListener {
public:
  MapBooOLDBuilder_DummyListener()
  {
  }
  
  void onDownload(const URL& url,
                  IByteBuffer* buffer,
                  bool expired) {
    // do nothing
    delete buffer;
  }
  
  void onError(const URL& url) {
    ILogger::instance()->logError("Can't download %s", url._path.c_str());
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

void MapBooOLDBuilder::changedCurrentScene() {
  recreateLayerSet();

  const MapBooOLD_Scene* currentScene = getApplicationCurrentScene();

  if (_g3mWidget != NULL) {
    _g3mWidget->setBackgroundColor(getCurrentBackgroundColor());

    // force immediate execution of PeriodicalTasks
    _g3mWidget->resetPeriodicalTasksTimeouts();

    if (currentScene != NULL) {
      const Sector* sector = currentScene->getSector();
      if (sector == NULL) {
        _g3mWidget->setRenderedSector( Sector::fullSphere() );
      }
      else {
        _g3mWidget->setRenderedSector( *sector );
      }

      setCameraPosition(currentScene->getCameraPosition());
    }
  }

  if (_applicationListener != NULL) {
    _applicationListener->onCurrentSceneChanged(_context,
                                         getApplicationCurrentSceneId(),
                                         currentScene);
  }

  if (_viewType == VIEW_EDITION_PREVIEW) {
    if (_applicationCurrentSceneId.compare(_lastApplicationCurrentSceneId) != 0) {
      if (_lastApplicationCurrentSceneId.compare("-1") != 0) {
        if (_webSocket != NULL && _isApplicationTubeOpen) {
          _webSocket->send( getApplicationCurrentSceneCommand() );
        }
        else if (_token.length() > 0) {
            _g3mWidget->getG3MContext()->getDownloader()->requestBuffer(createApplicationCurrentSceneURL(), //
                                                                        DownloadPriority::HIGHEST, //
                                                                        TimeInterval::zero(), //
                                                                        false, // readExpired
                                                                        new MapBooOLDBuilder_DummyListener(), //
                                                                        false);
        }
        else {
            ILogger::instance()->logError("VIEW_PRESENTATION: can't fire the event of changed scene");
        }
      }
      _lastApplicationCurrentSceneId = _applicationCurrentSceneId;
    }
  }
}

const std::string MapBooOLDBuilder::getApplicationCurrentSceneCommand() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("currentSceneId=");
  isb->addString(_applicationCurrentSceneId);
  const std::string s = isb->getString();
  delete isb;
  return s;
}

const URL MapBooOLDBuilder::createApplicationCurrentSceneURL() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_serverURL._path);
  isb->addString("/REST/1/applications/");
  isb->addString(_applicationId);
  isb->addString("/_POST_?");
  isb->addString("currentSceneId=");
  isb->addString(_applicationCurrentSceneId);
  isb->addString("&token=");
  isb->addString(_token);
  const std::string path = isb->getString();
  delete isb;

  return URL(path, false);
}

void MapBooOLDBuilder::updateVisibleScene(const bool cameraPositionChanged) {
  recreateLayerSet();
  const MapBooOLD_Scene* currentScene = getApplicationCurrentScene();
  
  if (_g3mWidget != NULL) {
    _g3mWidget->setBackgroundColor(getCurrentBackgroundColor());
    
    // force immediate execution of PeriodicalTasks
    _g3mWidget->resetPeriodicalTasksTimeouts();
    
    if (currentScene != NULL) {
      const Sector* sector = currentScene->getSector();
      if (sector == NULL) {
        _g3mWidget->setRenderedSector( Sector::fullSphere() );
      }
      else {
        _g3mWidget->setRenderedSector( *sector );
      }
      
      if (cameraPositionChanged) {
        setCameraPosition(currentScene->getCameraPosition());
      }
    }
  }
}

void MapBooOLDBuilder::setCameraPosition(const MapBooOLD_CameraPosition* cameraPosition, const bool animated) {
  if (cameraPosition != NULL) {
    if (animated) {
      _g3mWidget->setAnimatedCameraPosition(TimeInterval::fromSeconds(3),
                                            cameraPosition->getPosition(),
                                            cameraPosition->getHeading(),
                                            cameraPosition->getPitch());
    }
    else {
      _g3mWidget->setCameraPosition( cameraPosition->getPosition() );
      _g3mWidget->setCameraHeading( cameraPosition->getHeading() );
      _g3mWidget->setCameraPitch( cameraPosition->getPitch() );
    }
  }
}

void MapBooOLDBuilder::setCameraPosition(const MapBooOLD_CameraPosition* cameraPosition) {
  if (cameraPosition != NULL) {
    const bool animated = cameraPosition->isAnimated();
    setCameraPosition(cameraPosition, animated);
  }
}

void MapBooOLDBuilder::fireOnScenesChanged() {
  if (_applicationListener != NULL) {
#ifdef C_CODE
    _applicationListener->onScenesChanged(_context, _applicationScenes);
#endif
#ifdef JAVA_CODE
    _applicationListener.onScenesChanged(_context,
                                         new java.util.ArrayList<MapBooOLD_Scene>(_applicationScenes));
#endif
  }
}

void MapBooOLDBuilder::addApplicationScene(MapBooOLD_Scene* scene, const int position) {
#ifdef C_CODE
  _applicationScenes.insert(_applicationScenes.begin() + position, scene);
#endif
#ifdef JAVA_CODE
  _applicationScenes.add(position, scene);
#endif
  
  fireOnScenesChanged();
}

void MapBooOLDBuilder::deleteApplicationScene(const std::string &sceneId) {
  const int scenesCount = _applicationScenes.size();
  int sceneIndex = -1;
  for (int i = 0; i < scenesCount; i++) {
    const std::string iSceneId = _applicationScenes[i]->getId();
    if (iSceneId.compare(sceneId) == 0) {
      sceneIndex = i;
      break;
    }
  }
  if (sceneIndex != -1) {
    MapBooOLD_Scene* scene = _applicationScenes[sceneIndex];
#ifdef C_CODE
    _applicationScenes.erase(_applicationScenes.begin() + sceneIndex);
#endif
#ifdef JAVA_CODE
    _applicationScenes.remove(sceneIndex);
#endif
    delete scene;
    
    if (_viewType == VIEW_RUNTIME) {
      if (_applicationCurrentSceneId.compare(sceneId) == 0) {
        setApplicationCurrentSceneId(_applicationScenes[0]->getId());
      }
    }
    
    fireOnScenesChanged();
  }
}

void MapBooOLDBuilder::setApplicationScenes(const std::vector<MapBooOLD_Scene*>& applicationScenes) {
  const int currentScenesCount = _applicationScenes.size();
  for (int i = 0; i < currentScenesCount; i++) {
    MapBooOLD_Scene* scene = _applicationScenes[i];
    delete scene;
  }

  _applicationScenes.clear();

#ifdef C_CODE
  _applicationScenes = applicationScenes;
#endif
#ifdef JAVA_CODE
  _applicationScenes = new java.util.ArrayList<MapBooOLD_Scene>(applicationScenes);
#endif

  fireOnScenesChanged();
}

SceneLighting* MapBooOLDBuilder::createSceneLighting() {
  return new CameraFocusSceneLighting(Color::fromRGBA((float)0.3, (float)0.3, (float)0.3, (float)1.0),
                                      Color::yellow());
}

void MapBooOLDBuilder::setApplicationTubeOpened(bool open) {
  if (_isApplicationTubeOpen != open) {
    _isApplicationTubeOpen = open;
    if (!_isApplicationTubeOpen) {
      _webSocket = NULL;
    }

    if (_isApplicationTubeOpen) {
      if (_applicationListener != NULL) {
        _applicationListener->onWebSocketOpen(_context);
      }
    }
    else {
      if (_applicationListener != NULL) {
        _applicationListener->onWebSocketClose(_context);
      }
    }
  }
}

const MapBooOLD_Notification* MapBooOLDBuilder::createNotification(const Geodetic2D&  position,
                                                             const Camera*      camera,
                                                             const std::string& message,
                                                             const URL*         iconURL) const {
  MapBooOLD_CameraPosition* cameraPosition = new MapBooOLD_CameraPosition(camera->getGeodeticPosition(),
                                                                    camera->getHeading(),
                                                                    camera->getPitch(),
                                                                    true /* animated */);
  return new MapBooOLD_Notification(position, cameraPosition, message, iconURL);
}

void MapBooOLDBuilder::pollApplicationDataFromServer(const G3MContext *context) {
  IDownloader* downloader = context->getDownloader();
  downloader->requestBuffer(createApplicationPollURL(),
                            DownloadPriority::HIGHEST,
                            TimeInterval::zero(),
                            false, // readExpired
                            new MapBooOLDBuilder_RestJSON(this),
                            true);
}


const std::string MapBooOLDBuilder::getViewAsString() const {
  switch (_viewType) {
    case VIEW_EDITION_PREVIEW:
      return "edition-preview";
    case VIEW_PRESENTATION:
      return "presentation";
    case VIEW_RUNTIME:
    default:
      return "runtime";
  }
}

const URL MapBooOLDBuilder::createGetFeatureInfoRestURL(const Tile* tile,
                                                     const Vector2I& tileDimension,
                                                     const Vector2I& pixelPosition,
                                                     const Geodetic3D& position) {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_serverURL._path);

  isb->addString("/Public/applications/");
  isb->addString(_applicationId);
  isb->addString("/scenes/");
  
  const MapBooOLD_Scene* scene = getApplicationCurrentScene();
  isb->addString(scene->getId());
  
  isb->addString("/getinfo?");
  
  isb->addString("tileX=");
  isb->addInt(tile->_column);
  
  isb->addString("&tileY=");
  isb->addInt(tile->_row);
  
  isb->addString("&tileLevel=");
  isb->addInt(tile->_level);
  
  
  //Sector
  isb->addString("&upperLat=");
  isb->addDouble(tile->_sector._upper._latitude._degrees);
  isb->addString("&lowerLat=");
  isb->addDouble(tile->_sector._lower._latitude._degrees);
  isb->addString("&upperLon=");
  isb->addDouble(tile->_sector._upper._longitude._degrees);
  isb->addString("&lowerLon=");
  isb->addDouble(tile->_sector._lower._longitude._degrees);
  
  
  isb->addString("&tileBBox=");
  isb->addString("TODO");
  
  isb->addString("&tileWidth=");
  isb->addInt(tileDimension._x);
  
  isb->addString("&tileHeight=");
  isb->addInt(tileDimension._y);
  
  isb->addString("&pixelX=");
  isb->addInt(pixelPosition._x);
  
  isb->addString("&pixelY=");
  isb->addInt(pixelPosition._y);
  
  isb->addString("&lat=");
  isb->addDouble(position._latitude._degrees);
  
  isb->addString("&lon=");
  isb->addDouble(position._longitude._degrees);
  
  const std::string path = isb->getString();
  delete isb;
  
  return URL(path, false);
  
}

const void MapBooOLDBuilder::requestGetFeatureInfo(const Tile* tile,
                                 const Vector2I& size,
                                 const Vector2I& pixel,
                                 const Geodetic3D& position) {
  _g3mWidget->getG3MContext()->getDownloader()->requestBuffer(createGetFeatureInfoRestURL(tile, size, pixel, position), DownloadPriority::HIGHER, TimeInterval::zero(), false, _featureInfoDownloadListener, false);
}


void HUDInfoRenderer_ImageFactory::drawOn(ICanvas* canvas,
                                          int width,
                                          int height) {
  
  std::vector<std::string> strings;
  
  const int size = _info.size();
  for (int i = 0; i < size; i++)
  {
    strings.push_back(_info.at(i)->getText());
  }
  ICanvasUtils::drawStringsOn(strings,
                              canvas,
                              width,
                              height,
                              Left,
                              Bottom,
                              Left,
                              Color::white(),
                              11,
                              2,
                              Color::transparent(),
                              Color::black(),
                              5);
}

bool HUDInfoRenderer_ImageFactory::isEquals(const std::vector<const Info*>& v1,
                                            const std::vector<const Info*>& v2) const {
  const int size1 = v1.size();
  const int size2 = v2.size();
  if (size1 != size2) {
    return false;
  }
  
  for (int i = 0; i < size1; i++) {
    const Info* str1 = v1[i];
    const Info* str2 = v2[i];
    if (str1 != str2) {
      return false;
    }
  }
  return true;
}

bool HUDInfoRenderer_ImageFactory::setInfo(const std::vector<const Info*>& info) {
  if ( isEquals(_info, info) ) {
    return false;
  }
  
  _info.clear();
#ifdef C_CODE
  _info.insert(_info.end(),
                 info.begin(),
                 info.end());
#endif
#ifdef JAVA_CODE
  _info.addAll(info);
#endif

  return true;
}

MapBooOLD_HUDRenderer::MapBooOLD_HUDRenderer() {
  _hudImageRenderer = new HUDImageRenderer(new HUDInfoRenderer_ImageFactory());
}

MapBooOLD_HUDRenderer::~MapBooOLD_HUDRenderer() {
  delete _hudImageRenderer;
}

void MapBooOLD_HUDRenderer::updateInfo(const std::vector<const Info*>& info) {
  HUDInfoRenderer_ImageFactory* factory = (HUDInfoRenderer_ImageFactory*) (_hudImageRenderer->getImageFactory());
  if (factory->setInfo(info)) {
    _hudImageRenderer->recreateImage();
  }
}

void MapBooOLD_HUDRenderer::initialize(const G3MContext* context) {
  _hudImageRenderer->initialize(context);
}

void MapBooOLD_HUDRenderer::render(const G3MRenderContext* rc,
                              GLState* glState) {
  _hudImageRenderer->render(rc, glState);
}

void MapBooOLD_HUDRenderer::onResizeViewportEvent(const G3MEventContext* ec,
                                             int width, int height) {
  _hudImageRenderer->onResizeViewportEvent(ec,
                                           width, height);
}

void MapBooOLD_HUDRenderer::start(const G3MRenderContext* rc) {
  _hudImageRenderer->start(rc);
}

void MapBooOLD_HUDRenderer::stop(const G3MRenderContext* rc) {
  _hudImageRenderer->stop(rc);
}

void MapBooOLD_HUDRenderer::onResume(const G3MContext* context) {
  _hudImageRenderer->onResume(context);
}

void MapBooOLD_HUDRenderer::onPause(const G3MContext* context) {
  _hudImageRenderer->onPause(context);
}

void MapBooOLD_HUDRenderer::onDestroy(const G3MContext* context) {
  _hudImageRenderer->onDestroy(context);
}

void MapBooOLD_ErrorRenderer::setErrors(const std::vector<std::string>& errors) {
  _errors = errors;
}

RenderState MapBooOLD_ErrorRenderer::getRenderState(const G3MRenderContext* rc) {
  if (_errors.size() > 0) {
    return RenderState::error(_errors);
  }
  return RenderState::ready();
}
