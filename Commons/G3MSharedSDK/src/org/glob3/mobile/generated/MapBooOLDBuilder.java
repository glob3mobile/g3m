package org.glob3.mobile.generated; 
public abstract class MapBooOLDBuilder
{

  private final URL _serverURL;
  private final URL _tubesURL;

  private MapBooOLD_ViewType _viewType;

  private MapBooOLDApplicationChangeListener _applicationListener;

  private FeatureInfoDownloadListener _featureInfoDownloadListener;

  private final boolean _enableNotifications;

  private String _applicationId;
  private String _applicationName;
  private String _applicationWebsite;
  private String _applicationEMail;
  private String _applicationAbout;
  private int _applicationTimestamp;
  private java.util.ArrayList<MapBooOLD_Scene> _applicationScenes = new java.util.ArrayList<MapBooOLD_Scene>();
  private String _applicationCurrentSceneId;
  private String _lastApplicationCurrentSceneId;

  private int _applicationEventId;
  private final String _token;

  private GL _gl;
  private G3MWidget _g3mWidget;
  private IStorage _storage;

  private IWebSocket _webSocket;

  private G3MContext _context;

  private boolean _isApplicationTubeOpen;

  private MapBooOLD_ErrorRenderer _mbErrorRenderer;

  private LayerSet _layerSet;
  private PlanetRenderer createPlanetRenderer()
  {
    final boolean skirted = true;
    TileTessellator tessellator = new PlanetTileTessellator(skirted, Sector.fullSphere());
  
    ElevationDataProvider elevationDataProvider = null;
    final float verticalExaggeration = 1F;
  
    TileTexturizer texturizer = new DefaultTileTexturizer(new DownloaderImageBuilder(new URL("http://www.mapboo.com/web/img/tileNotFound.jpg")));
  
    final boolean renderDebug = false;
    final boolean useTilesSplitBudget = true;
    final boolean forceFirstLevelTilesRenderOnStart = true;
    final boolean incrementalTileQuality = false;
    final Quality quality = Quality.QUALITY_LOW;
  
    final TilesRenderParameters parameters = new TilesRenderParameters(renderDebug, useTilesSplitBudget, forceFirstLevelTilesRenderOnStart, incrementalTileQuality, quality);
  
  
    final boolean showStatistics = false;
    long tileDownloadPriority = DownloadPriority.HIGHER;
  
    final Sector renderedSector = Sector.fullSphere();
    final boolean renderTileMeshes = true;
  
    final boolean logTilesPetitions = false;
  
    TileRenderingListener tileRenderingListener = null;
  
    ChangedRendererInfoListener changedRendererInfoListener = null;
  
    TouchEventType touchEventTypeOfTerrainTouchListener = TouchEventType.DownUp;
  
    PlanetRenderer result = new PlanetRenderer(tessellator, elevationDataProvider, true, verticalExaggeration, texturizer, _layerSet, parameters, showStatistics, tileDownloadPriority, renderedSector, renderTileMeshes, logTilesPetitions, tileRenderingListener, changedRendererInfoListener, touchEventTypeOfTerrainTouchListener);
  
    if (_enableNotifications)
    {
      result.addTerrainTouchListener(new MapBooOLDBuilder_TerrainTouchListener(this));
    }
  
    return result;
  }

  private java.util.ArrayList<ICameraConstrainer> createCameraConstraints(Planet planet, PlanetRenderer planetRenderer)
  {
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
    //SimpleCameraConstrainer* scc = new SimpleCameraConstrainer();
  
    final Geodetic3D initialCameraPosition = planet.getDefaultCameraPosition(Sector.fullSphere());
  
    cameraConstraints.add(new RenderedSectorCameraConstrainer(planetRenderer, initialCameraPosition._height * 1.2));
  
    return cameraConstraints;
  }

  private CameraRenderer createCameraRenderer()
  {
    CameraRenderer cameraRenderer = new CameraRenderer();
    final boolean useInertia = true;
    cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
    cameraRenderer.addHandler(new CameraDoubleDragHandler());
    cameraRenderer.addHandler(new CameraRotationHandler());
    //cameraRenderer->addHandler(new CameraDoubleTapHandler());
  
    return cameraRenderer;
  }

  private ProtoRenderer createBusyRenderer()
  {
    return new BusyMeshRenderer(Color.newFromRGBA(0, 0, 0, 1));
  }

  private ErrorRenderer createErrorRenderer()
  {
    return new HUDErrorRenderer(new Mapboo_ErrorMessagesCustomizer(this));
  }

  private java.util.ArrayList<PeriodicalTask> createPeriodicalTasks()
  {
    java.util.ArrayList<PeriodicalTask> periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
  
    periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(5), new MapBooOLDBuilder_TubeWatchdogPeriodicalTask(this)));
  
    return periodicalTasks;
  }

  private void recreateLayerSet()
  {
    final MapBooOLD_Scene scene = getApplicationCurrentScene();
  
    if (scene == null)
    {
      _layerSet.removeAllLayers(true);
    }
    else
    {
      LayerSet newLayerSet = scene.createLayerSet();
      if (!newLayerSet.isEquals(_layerSet))
      {
        _layerSet.removeAllLayers(true);
        _layerSet.takeLayersFrom(newLayerSet);
      }
      if (newLayerSet != null)
         newLayerSet.dispose();
    }
  }

  private IThreadUtils _threadUtils;
  private IThreadUtils getThreadUtils()
  {
    if (_threadUtils == null)
    {
      _threadUtils = createThreadUtils();
    }
    return _threadUtils;
  }

  private IDownloader _downloader;
  private IDownloader getDownloader()
  {
    if (_downloader == null)
    {
      _downloader = createDownloader();
    }
    return _downloader;
  }

  private GPUProgramManager _gpuProgramManager;
  private GPUProgramManager getGPUProgramManager()
  {
    if (_gpuProgramManager == null)
    {
      _gpuProgramManager = createGPUProgramManager();
    }
    return _gpuProgramManager;
  }


  private Layer parseLayer(JSONBaseObject jsonBaseObjectLayer)
  {
    if (jsonBaseObjectLayer == null)
    {
      return null;
    }
  
    if (jsonBaseObjectLayer.asNull() != null)
    {
      return null;
    }
  
    final TimeInterval defaultTimeToCache = TimeInterval.fromDays(30);
  
    final JSONObject jsonLayer = jsonBaseObjectLayer.asObject();
    if (jsonLayer == null)
    {
      ILogger.instance().logError("Layer is not a json object");
      return null;
    }
  
    final boolean transparent = jsonLayer.getAsBoolean("transparent", false);
    final String layerType = jsonLayer.getAsString("layer", "<layer not present>");
    Layer layer;
    if (layerType.compareTo("OSM") == 0)
    {
      layer = new OSMLayer(defaultTimeToCache, true, 2, 1, null, new java.util.ArrayList<Info>()); //disclaimerInfo -  condition, -  transparency, -  initialLevel, -  readExpired,
    }
    else if (layerType.compareTo("MapQuest") == 0)
    {
      layer = parseMapQuestLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("BingMaps") == 0)
    {
      layer = parseBingMapsLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("CartoDB") == 0)
    {
      layer = parseCartoDBLayer(jsonLayer, transparent, defaultTimeToCache);
    }
    else if (layerType.compareTo("MapBox") == 0)
    {
      layer = parseMapBoxLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("WMS") == 0)
    {
      layer = parseWMSLayer(jsonLayer, transparent);
    }
    else if (layerType.compareTo("URLTemplate") == 0)
    {
      layer = parseURLTemplateLayer(jsonLayer, transparent);
    }
    else
    {
      ILogger.instance().logError("Unsupported layer type \"%s\"", layerType);
      ILogger.instance().logError("%s", jsonBaseObjectLayer.description());
      return null;
    }
  
    final String layerAttribution = jsonLayer.getAsString("attribution", "");
    if (layerAttribution.compareTo("") != 0)
    {
      layer.addInfo(new Info(layerAttribution));
    }
    return layer;
  }

  private MapQuestLayer parseMapQuestLayer(JSONObject jsonLayer, TimeInterval timeToCache)
  {
    final String imagery = jsonLayer.getAsString("imagery", "<imagery not present>");
    if (imagery.compareTo("OpenAerial") == 0)
    {
      return MapQuestLayer.newOpenAerial(timeToCache);
    }
  
    // defaults to OSM
    return MapQuestLayer.newOSM(timeToCache);
  }

  private BingMapsLayer parseBingMapsLayer(JSONObject jsonLayer, TimeInterval timeToCache)
  {
    final String key = jsonLayer.getAsString("key", "");
    final String imagerySet = jsonLayer.getAsString("imagerySet", "Aerial");
  
    return new BingMapsLayer(imagerySet, key, timeToCache, true, 2, 25, 1, null, new java.util.ArrayList<Info>()); // disclaimerInfo -  condition -  transparency -  maxLevel -  initialLevel -  readExpired
  }

  private CartoDBLayer parseCartoDBLayer(JSONObject jsonLayer, boolean transparent, TimeInterval timeToCache)
  {
    final String userName = jsonLayer.getAsString("userName", "");
    final String table = jsonLayer.getAsString("table", "");
  
    return new CartoDBLayer(userName, table, timeToCache, true, 1, transparent, null, new java.util.ArrayList<Info>()); // disclaimerInfo -  condition, -  isTransparent -  transparency -  readExpired
  }


  private MapBoxLayer parseMapBoxLayer(JSONObject jsonLayer, TimeInterval timeToCache)
  {
    final String mapKey = jsonLayer.getAsString("mapKey", "");
  
    return new MapBoxLayer(mapKey, timeToCache, true, 1, 19, 1, null, new java.util.ArrayList<Info>()); // disclaimerInfo -  condition -  transparency -  maxLevel -  initialLevel -  readExpired
  }

  private WMSLayer parseWMSLayer(JSONObject jsonLayer, boolean transparent)
  {
  
    final String mapLayer = jsonLayer.getAsString("layerName", "");
    final URL mapServerURL = new URL(jsonLayer.getAsString("server", ""), false);
    final String versionStr = jsonLayer.getAsString("version", "");
    WMSServerVersion mapServerVersion = WMSServerVersion.WMS_1_1_0;
    if (versionStr.compareTo("WMS_1_3_0") == 0)
    {
      mapServerVersion = WMSServerVersion.WMS_1_3_0;
    }
    final String queryLayer = jsonLayer.getAsString("queryLayer", "");
    final String style = jsonLayer.getAsString("style", "");
    final URL queryServerURL = new URL("", false);
    final WMSServerVersion queryServerVersion = mapServerVersion;
    final Sector sector = parseSector(jsonLayer, "validSector");
    String imageFormat = jsonLayer.getAsString("imageFormat", "image/png");
    final String srs = jsonLayer.getAsString("projection", "EPSG:4326");
    LayerTilesRenderParameters layerTilesRenderParameters = null;
    if (srs.compareTo("EPSG:4326") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultWGS84(0, 17);
    }
    else if (srs.compareTo("EPSG:3857") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultMercator(0, 17);
    }
    final double expiration = jsonLayer.getAsNumber("expiration", 0);
    final long milliseconds = IMathUtils.instance().round(expiration);
    final TimeInterval timeToCache = TimeInterval.fromMilliseconds(milliseconds);
    final boolean readExpired = jsonLayer.getAsBoolean("acceptExpiration", false);
  
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, sector, imageFormat, srs, style, transparent, null, timeToCache, readExpired, layerTilesRenderParameters);
  }

  private URLTemplateLayer parseURLTemplateLayer(JSONObject jsonLayer, boolean transparent)
  {
    final String urlTemplate = jsonLayer.getAsString("url", "");
  
    final int firstLevel = (int) jsonLayer.getAsNumber("firstLevel", 1);
    final int maxLevel = (int) jsonLayer.getAsNumber("maxLevel", 19);
  
    final String projection = jsonLayer.getAsString("projection", "EPSG:3857");
    final boolean mercator = (projection.equals("EPSG:3857"));
  
    final Sector sector = parseSector(jsonLayer, "validSector");
  
    URLTemplateLayer result;
    if (mercator)
    {
      result = URLTemplateLayer.newMercator(urlTemplate, sector, transparent, firstLevel, maxLevel, TimeInterval.fromDays(30));
    }
    else
    {
  //    result = URLTemplateLayer::newWGS84(urlTemplate,
  //                                        sector,
  //                                        transparent,
  //                                        firstLevel,
  //                                        maxLevel,
  //                                        TimeInterval::fromDays(30));
  
      result = new URLTemplateLayer(urlTemplate, sector, transparent, TimeInterval.fromDays(30), true, new LevelTileCondition(firstLevel, maxLevel), LayerTilesRenderParameters.createDefaultWGS84(sector, 1, maxLevel));
    }
  
    return result;
  }

  private String getApplicationCurrentSceneId()
  {
    return _applicationCurrentSceneId;
  }
  private MapBooOLD_Scene getApplicationCurrentScene()
  {
    final String currentSceneId = getApplicationCurrentSceneId();
  
    final int scenesCount = _applicationScenes.size();
    for (int i = 0; i < scenesCount; i++)
    {
      final String sceneId = _applicationScenes.get(i).getId();
      if (sceneId.compareTo(currentSceneId) == 0)
      {
        return _applicationScenes.get(i);
      }
    }
    return null;
  }

  private Color getCurrentBackgroundColor()
  {
    final MapBooOLD_Scene scene = getApplicationCurrentScene();
    return (scene == null) ? Color.black() : scene.getBackgroundColor();
  }

//  const std::string parseSceneId(const JSONObject* jsonObject) const;
  private MapBooOLD_Scene parseScene(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    final boolean hasWarnings = jsonObject.getAsBoolean("hasWarnings", false);
  
    final boolean queryable = jsonObject.getAsBoolean("queryable", false);
  
  
    //  if (hasWarnings && (_viewType != VIEW_PRESENTATION)) {
    //    return NULL;
    //  }
  
    return new MapBooOLD_Scene(jsonObject.getAsString("id", ""), jsonObject.getAsString("name", ""), jsonObject.getAsString("description", ""), parseMultiImage(jsonObject.getAsObject("screenshot")), parseColor(jsonObject.getAsString("backgroundColor")), parseCameraPosition(jsonObject.getAsObject("cameraPosition")), parseSector(jsonObject.get("sector")), parseLayer(jsonObject.get("baseLayer")), parseLayer(jsonObject.get("overlayLayer")), queryable, hasWarnings);
  }

  private Color parseColor(JSONString jsonColor)
  {
    if (jsonColor == null)
    {
      return Color.black();
    }
  
    final Color color = Color.parse(jsonColor.value());
    if (color == null)
    {
      ILogger.instance().logError("Invalid format in attribute 'color' (%s)", jsonColor.value());
      return Color.black();
    }
  
    Color result = new Color(color);
    if (color != null)
       color.dispose();
    return result;
  }

  //const std::string MapBooOLDBuilder::parseSceneId(const JSONObject* jsonObject) const {
  //  if (jsonObject == NULL) {
  //    ILogger::instance()->logError("Missing Scene ID");
  //    return "";
  //  }
  //
  //  return jsonObject->getAsString("$oid", "");
  //}
  
  private Sector parseSector(JSONBaseObject jsonBaseObjectLayer)
  {
    if (jsonBaseObjectLayer == null)
    {
      return null;
    }
  
    if (jsonBaseObjectLayer.asNull() != null)
    {
      return null;
    }
  
    final JSONObject jsonObject = jsonBaseObjectLayer.asObject();
    if (jsonObject == null)
    {
      return null;
    }
  
    final double lowerLat = jsonObject.getAsNumber("lowerLat", -90.0);
    final double lowerLon = jsonObject.getAsNumber("lowerLon", -180.0);
    final double upperLat = jsonObject.getAsNumber("upperLat", 90.0);
    final double upperLon = jsonObject.getAsNumber("upperLon", 180.0);
  
    return new Sector(Geodetic2D.fromDegrees(lowerLat, lowerLon), Geodetic2D.fromDegrees(upperLat, upperLon));
  }

  private MapBooOLD_MultiImage parseMultiImage(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    Color averageColor = parseColor(jsonObject.getAsString("averageColor"));
  
    java.util.ArrayList<MapBooOLD_MultiImage_Level> levels = new java.util.ArrayList<MapBooOLD_MultiImage_Level>();
  
    final JSONArray jsLevels = jsonObject.getAsArray("levels");
    if (jsLevels != null)
    {
      final int levelsCount = jsLevels.size();
      for (int i = 0; i < levelsCount; i++)
      {
        MapBooOLD_MultiImage_Level level = parseMultiImageLevel(jsLevels.getAsObject(i));
        if (level != null)
        {
          levels.add(level);
        }
      }
    }
  
    return new MapBooOLD_MultiImage(averageColor, levels);
  }
  private MapBooOLD_MultiImage_Level parseMultiImageLevel(JSONObject jsonObject)
  {
    final JSONString jsURL = jsonObject.getAsString("url");
    if (jsURL == null)
    {
      return null;
    }
  
    final JSONNumber jsWidth = jsonObject.getAsNumber("width");
    if (jsWidth == null)
    {
      return null;
    }
  
    final JSONNumber jsHeight = jsonObject.getAsNumber("height");
    if (jsHeight == null)
    {
      return null;
    }
  
    return new MapBooOLD_MultiImage_Level(new URL(_serverURL, "/images/" + jsURL.value()), (int) jsWidth.value(), (int) jsHeight.value());
  }
  private MapBooOLD_CameraPosition parseCameraPosition(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    final double latitudeInDegress = jsonObject.getAsNumber("latitude", 0);
    final double longitudeInDegress = jsonObject.getAsNumber("longitude", 0);
    final double height = jsonObject.getAsNumber("height", 0);
  
    final double headingInDegrees = jsonObject.getAsNumber("heading", 0);
    final double pitchInDegrees = jsonObject.getAsNumber("pitch", 0);
  
    final boolean animated = jsonObject.getAsBoolean("animated", true);
  
    return new MapBooOLD_CameraPosition(Geodetic3D.fromDegrees(latitudeInDegress, longitudeInDegress, height), Angle.fromDegrees(headingInDegrees), Angle.fromDegrees(pitchInDegrees), animated);
  }

  private void changedCurrentScene()
  {
    recreateLayerSet();
  
    final MapBooOLD_Scene currentScene = getApplicationCurrentScene();
  
    if (_g3mWidget != null)
    {
      _g3mWidget.setBackgroundColor(getCurrentBackgroundColor());
  
      // force immediate execution of PeriodicalTasks
      _g3mWidget.resetPeriodicalTasksTimeouts();
  
      if (currentScene != null)
      {
        final Sector sector = currentScene.getSector();
        if (sector == null)
        {
          _g3mWidget.setRenderedSector(Sector.fullSphere());
        }
        else
        {
          _g3mWidget.setRenderedSector(sector);
        }
  
        setCameraPosition(currentScene.getCameraPosition());
      }
    }
  
    if (_applicationListener != null)
    {
      _applicationListener.onCurrentSceneChanged(_context, getApplicationCurrentSceneId(), currentScene);
    }
  
    if (_viewType == MapBooOLD_ViewType.VIEW_EDITION_PREVIEW)
    {
      if (_applicationCurrentSceneId.compareTo(_lastApplicationCurrentSceneId) != 0)
      {
        if (_lastApplicationCurrentSceneId.compareTo("-1") != 0)
        {
          if (_webSocket != null && _isApplicationTubeOpen)
          {
            _webSocket.send(getApplicationCurrentSceneCommand());
          }
          else if (_token.length() > 0)
          {
              _g3mWidget.getG3MContext().getDownloader().requestBuffer(createApplicationCurrentSceneURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapBooOLDBuilder_DummyListener(), false); // readExpired
          }
          else
          {
              ILogger.instance().logError("VIEW_PRESENTATION: can't fire the event of changed scene");
          }
        }
        _lastApplicationCurrentSceneId = _applicationCurrentSceneId;
      }
    }
  }

  private void updateVisibleScene(boolean cameraPositionChanged)
  {
    recreateLayerSet();
    final MapBooOLD_Scene currentScene = getApplicationCurrentScene();
  
    if (_g3mWidget != null)
    {
      _g3mWidget.setBackgroundColor(getCurrentBackgroundColor());
  
      // force immediate execution of PeriodicalTasks
      _g3mWidget.resetPeriodicalTasksTimeouts();
  
      if (currentScene != null)
      {
        final Sector sector = currentScene.getSector();
        if (sector == null)
        {
          _g3mWidget.setRenderedSector(Sector.fullSphere());
        }
        else
        {
          _g3mWidget.setRenderedSector(sector);
        }
  
        if (cameraPositionChanged)
        {
          setCameraPosition(currentScene.getCameraPosition());
        }
      }
    }
  }

  private String getApplicationCurrentSceneCommand()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("currentSceneId=");
    isb.addString(_applicationCurrentSceneId);
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  private String getSendNotificationCommand(Geodetic2D position, MapBooOLD_CameraPosition cameraPosition, String message, URL iconURL)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("notification=");
  
    isb.addString("{");
  
    isb.addString("\"latitude\":");
    isb.addDouble(position._latitude._degrees);
  
    isb.addString(",\"longitude\":");
    isb.addDouble(position._longitude._degrees);
  
    isb.addString(",\"message\":");
    isb.addString("\"");
    isb.addString(escapeString(message));
    isb.addString("\"");
  
    if (iconURL != null)
    {
      isb.addString(",\"iconURL\":");
      isb.addString("\"");
      isb.addString(escapeString(iconURL._path));
      isb.addString("\"");
    }
  
    isb.addString(",\"cameraPosition\":");
    isb.addString(toCameraPositionJSON(cameraPosition));
  
    isb.addString("}");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  private String getSendNotificationCommand(Geodetic2D position, Camera camera, String message, URL iconURL)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("notification=");
  
    isb.addString("{");
  
    isb.addString("\"latitude\":");
    isb.addDouble(position._latitude._degrees);
  
    isb.addString(",\"longitude\":");
    isb.addDouble(position._longitude._degrees);
  
    isb.addString(",\"message\":");
    isb.addString("\"");
    isb.addString(escapeString(message));
    isb.addString("\"");
  
    if (iconURL != null)
    {
      isb.addString(",\"iconURL\":");
      isb.addString("\"");
      isb.addString(escapeString(iconURL._path));
      isb.addString("\"");
    }
  
    isb.addString(",\"cameraPosition\":");
    isb.addString(toCameraPositionJSON(camera));
  
    isb.addString("}");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  private String escapeString(String str)
  {
    final IStringUtils su = IStringUtils.instance();
  
    return su.replaceAll(str, "\"", "\\\"");
  }

  private String toCameraPositionJSON(Camera camera)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("{");
  
    final Geodetic3D position = camera.getGeodeticPosition();
  
    isb.addString("\"latitude\":");
    isb.addDouble(position._latitude._degrees);
  
    isb.addString(",\"longitude\":");
    isb.addDouble(position._longitude._degrees);
  
    isb.addString(",\"height\":");
    isb.addDouble(position._height);
  
    isb.addString(",\"heading\":");
    isb.addDouble(camera.getHeading()._degrees);
  
    isb.addString(",\"pitch\":");
    isb.addDouble(camera.getPitch()._degrees);
  
    isb.addString(",\"animated\":");
    isb.addBool(true);
  
    isb.addString("}");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }
  private String toCameraPositionJSON(MapBooOLD_CameraPosition cameraPosition)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    isb.addString("{");
  
    final Geodetic3D position = cameraPosition.getPosition();
  
    isb.addString("\"latitude\":");
    isb.addDouble(position._latitude._degrees);
  
    isb.addString(",\"longitude\":");
    isb.addDouble(position._longitude._degrees);
  
    isb.addString(",\"height\":");
    isb.addDouble(position._height);
  
    isb.addString(",\"heading\":");
    isb.addDouble(cameraPosition.getHeading()._degrees);
  
    isb.addString(",\"pitch\":");
    isb.addDouble(cameraPosition.getPitch()._degrees);
  
    isb.addString(",\"animated\":");
    isb.addBool(true);
  
    isb.addString("}");
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  private MapBooOLD_Notification parseNotification(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    return new MapBooOLD_Notification(Geodetic2D.fromDegrees(jsonObject.getAsNumber("latitude", 0), jsonObject.getAsNumber("longitude", 0)), parseCameraPosition(jsonObject.getAsObject("cameraPosition")), jsonObject.getAsString("message", ""), parseURL(jsonObject.getAsString("iconURL")));
  }
  private java.util.ArrayList<MapBooOLD_Notification> parseNotifications(JSONArray jsonArray)
  {
    java.util.ArrayList<MapBooOLD_Notification> result = new java.util.ArrayList<MapBooOLD_Notification>();
  
    if (jsonArray != null)
    {
      final int size = jsonArray.size();
      for (int i = 0; i < size; i++)
      {
        MapBooOLD_Notification notification = parseNotification(jsonArray.getAsObject(i));
        if (notification != null)
        {
          result.add(notification);
        }
      }
    }
  
    return result;
  }

  private void addApplicationNotification(MapBooOLD_Notification notification)
  {
    if (_marksRenderer != null)
    {
      final String message = notification.getMessage();
  
      final boolean hasMessage = (message.length() > 0);
      final URL iconURL = notification.getIconURL();
  
      final Geodetic2D position = notification.getPosition();
  
      boolean newMark = false;
  
      if (hasMessage)
      {
        if (iconURL == null)
        {
          _marksRenderer.addMark(new Mark(message, new Geodetic3D(position, 0), AltitudeMode.ABSOLUTE, 0));
        }
        else
        {
          _marksRenderer.addMark(new Mark(message, iconURL, new Geodetic3D(position, 0), AltitudeMode.ABSOLUTE, 0));
        }
        newMark = true;
      }
      else
      {
        if (iconURL != null)
        {
          _marksRenderer.addMark(new Mark(iconURL, new Geodetic3D(position, 0), AltitudeMode.ABSOLUTE, 0));
          newMark = true;
        }
      }
  
      if (newMark)
      {
        final MapBooOLD_CameraPosition cameraPosition = notification.getCameraPosition();
        if (cameraPosition != null)
        {
          setCameraPosition(cameraPosition, true);
        }
      }
    }
  
    if (notification != null)
       notification.dispose();
  }
  private void addApplicationNotifications(java.util.ArrayList<MapBooOLD_Notification> notifications)
  {
    if (notifications == null)
    {
      return;
    }
  
    final int size = notifications.size();
    for (int i = 0; i < size; i++)
    {
      MapBooOLD_Notification notification = notifications.get(i);
      if (notification != null)
      {
        addApplicationNotification(notification);
      }
    }
  
    notifications = null;
  }

  private URL parseURL(JSONString jsonString)
  {
    if (jsonString == null)
    {
      return null;
    }
    return new URL(jsonString.value());
  }

  private MarksRenderer _marksRenderer;
  private MarksRenderer getMarksRenderer()
  {
    if (_marksRenderer == null)
    {
      _marksRenderer = new MarksRenderer(false);
    }
    return _marksRenderer;
  }

  private boolean _hasParsedApplication;

  private boolean _initialParse;

  private void fireOnScenesChanged()
  {
    if (_applicationListener != null)
    {
      _applicationListener.onScenesChanged(_context,
                                           new java.util.ArrayList<MapBooOLD_Scene>(_applicationScenes));
    }
  }

  private void setCameraPosition(MapBooOLD_CameraPosition cameraPosition, boolean animated)
  {
    if (cameraPosition != null)
    {
      if (animated)
      {
        _g3mWidget.setAnimatedCameraPosition(TimeInterval.fromSeconds(3), cameraPosition.getPosition(), cameraPosition.getHeading(), cameraPosition.getPitch());
      }
      else
      {
        _g3mWidget.setCameraPosition(cameraPosition.getPosition());
        _g3mWidget.setCameraHeading(cameraPosition.getHeading());
        _g3mWidget.setCameraPitch(cameraPosition.getPitch());
      }
    }
  }
  private void setCameraPosition(MapBooOLD_CameraPosition cameraPosition)
  {
    if (cameraPosition != null)
    {
      final boolean animated = cameraPosition.isAnimated();
      setCameraPosition(cameraPosition, animated);
    }
  }

  private String getViewAsString()
  {
    switch (_viewType)
    {
      case VIEW_EDITION_PREVIEW:
        return "edition-preview";
      case VIEW_PRESENTATION:
        return "presentation";
      case VIEW_RUNTIME:
      default:
        return "runtime";
    }
  }

  private URL createApplicationCurrentSceneURL()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_serverURL._path);
    isb.addString("/REST/1/applications/");
    isb.addString(_applicationId);
    isb.addString("/_POST_?");
    isb.addString("currentSceneId=");
    isb.addString(_applicationCurrentSceneId);
    isb.addString("&token=");
    isb.addString(_token);
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
    return new URL(path, false);
  }

  private URL createGetFeatureInfoRestURL(Tile tile, Vector2I tileDimension, Vector2I pixelPosition, Geodetic3D position)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_serverURL._path);
  
    isb.addString("/Public/applications/");
    isb.addString(_applicationId);
    isb.addString("/scenes/");
  
    final MapBooOLD_Scene scene = getApplicationCurrentScene();
    isb.addString(scene.getId());
  
    isb.addString("/getinfo?");
  
    isb.addString("tileX=");
    isb.addInt(tile._column);
  
    isb.addString("&tileY=");
    isb.addInt(tile._row);
  
    isb.addString("&tileLevel=");
    isb.addInt(tile._level);
  
  
    //Sector
    isb.addString("&upperLat=");
    isb.addDouble(tile._sector._upper._latitude._degrees);
    isb.addString("&lowerLat=");
    isb.addDouble(tile._sector._lower._latitude._degrees);
    isb.addString("&upperLon=");
    isb.addDouble(tile._sector._upper._longitude._degrees);
    isb.addString("&lowerLon=");
    isb.addDouble(tile._sector._lower._longitude._degrees);
  
  
    isb.addString("&tileBBox=");
    isb.addString("TODO");
  
    isb.addString("&tileWidth=");
    isb.addInt(tileDimension._x);
  
    isb.addString("&tileHeight=");
    isb.addInt(tileDimension._y);
  
    isb.addString("&pixelX=");
    isb.addInt(pixelPosition._x);
  
    isb.addString("&pixelY=");
    isb.addInt(pixelPosition._y);
  
    isb.addString("&lat=");
    isb.addDouble(position._latitude._degrees);
  
    isb.addString("&lon=");
    isb.addDouble(position._longitude._degrees);
  
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
    return new URL(path, false);
  
  }
  protected MapBooOLDBuilder(URL serverURL, URL tubesURL, String applicationId, MapBooOLD_ViewType viewType, MapBooOLDApplicationChangeListener applicationListener, boolean enableNotifications, String token)
  {
     _serverURL = serverURL;
     _tubesURL = tubesURL;
     _applicationId = applicationId;
     _viewType = viewType;
     _applicationName = "";
     _applicationWebsite = "";
     _applicationEMail = "";
     _applicationAbout = "";
     _applicationTimestamp = -1;
     _applicationEventId = -1;
     _token = token;
     _gl = null;
     _g3mWidget = null;
     _storage = null;
     _threadUtils = null;
     _layerSet = new LayerSet();
     _downloader = null;
     _applicationListener = applicationListener;
     _enableNotifications = enableNotifications;
     _gpuProgramManager = null;
     _isApplicationTubeOpen = false;
     _initialParse = true;
     _applicationCurrentSceneId = "-1";
     _lastApplicationCurrentSceneId = "-1";
     _context = null;
     _webSocket = null;
     _marksRenderer = null;
     _hasParsedApplication = false;
    _featureInfoDownloadListener = new FeatureInfoDownloadListener(_applicationListener);
  }

  public void dispose()
  {
  
  }

  protected final void setGL(GL gl)
  {
    if (_gl != null)
    {
      //ILogger::instance()->logError("LOGIC ERROR: _gl already initialized");
      //return;
      throw new RuntimeException("LOGIC ERROR: _gl already initialized");
    }
    if (gl == null)
    {
      //ILogger::instance()->logError("LOGIC ERROR: _gl cannot be NULL");
      //return;
      throw new RuntimeException("LOGIC ERROR: _gl cannot be NULL");
    }
    _gl = gl;
  }

  protected final GL getGL()
  {
    if (_gl == null)
    {
      ILogger.instance().logError("Logic Error: _gl not initialized");
    }
    return _gl;
  }

  protected final G3MWidget create()
  {
    if (_g3mWidget != null)
    {
      ILogger.instance().logError("The G3MWidget was already created, can't be created more than once");
      return null;
    }
  
  
    CompositeRenderer mainRenderer = new CompositeRenderer();
  
    _mbErrorRenderer = new MapBooOLD_ErrorRenderer();
    mainRenderer.addRenderer(_mbErrorRenderer);
  
    final Planet planet = createPlanet();
  
    PlanetRenderer planetRenderer = createPlanetRenderer();
    mainRenderer.addRenderer(planetRenderer);
  
    mainRenderer.addRenderer(getMarksRenderer());
  
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = createCameraConstraints(planet, planetRenderer);
  
    GInitializationTask initializationTask = new MapBooOLDBuilder_ApplicationTubeConnector(this);
  
    java.util.ArrayList<PeriodicalTask> periodicalTasks = createPeriodicalTasks();
  
    ICameraActivityListener cameraActivityListener = null;
  
  
    InitialCameraPositionProvider icpp = new SimpleInitialCameraPositionProvider();
  
    MapBooOLD_HUDRenderer hudRenderer = new MapBooOLD_HUDRenderer();
    InfoDisplay infoDisplay = new MapBooOLD_HUDRendererInfoDisplay(hudRenderer);
    infoDisplay.showDisplay();
  
    _g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), cameraActivityListener, planet, cameraConstraints, createCameraRenderer(), mainRenderer, createBusyRenderer(), createErrorRenderer(), hudRenderer, Color.black(), false, false, initializationTask, true, periodicalTasks, getGPUProgramManager(), createSceneLighting(), icpp, infoDisplay); // autoDeleteInitializationTask -  logDownloaderStatistics -  logFPS
    cameraConstraints = null;
    periodicalTasks = null;
  
    return _g3mWidget;
  }

  protected final Planet createPlanet()
  {
    //return Planet::createEarth();
    return Planet.createSphericalEarth();
  }

  protected final IStorage getStorage()
  {
    if (_storage == null)
    {
      _storage = createStorage();
    }
    return _storage;
  }

  protected abstract IStorage createStorage();

  protected abstract IDownloader createDownloader();

  protected abstract IThreadUtils createThreadUtils();

  protected abstract GPUProgramManager createGPUProgramManager();

  protected final SceneLighting createSceneLighting()
  {
    return new CameraFocusSceneLighting(Color.fromRGBA((float)0.3, (float)0.3, (float)0.3, (float)1.0), Color.yellow());
  }

  protected final URL createApplicationPollURL()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_serverURL._path);
    isb.addString("/poll/");
    isb.addString(_applicationId);
    isb.addString("?view=");
    isb.addString(getViewAsString());
    isb.addString("&eventId=");
    isb.addInt(_applicationEventId);
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
    return new URL(path, false);
  }

  protected final Sector parseSector(JSONObject jsonObject, String paramName)
  {
  
    final JSONObject sector = jsonObject.getAsObject(paramName);
  
    if (sector == null)
    {
      return Sector.fullSphere();
    }
  
    if (sector.asNull() != null)
    {
      return Sector.fullSphere();
    }
  
    final double lowerLat = sector.getAsNumber("lowerLat", -90.0);
    final double lowerLon = sector.getAsNumber("lowerLon", -180.0);
    final double upperLat = sector.getAsNumber("upperLat", 90.0);
    final double upperLon = sector.getAsNumber("upperLon", 180.0);
  
    return new Sector(Geodetic2D.fromDegrees(lowerLat, lowerLon), Geodetic2D.fromDegrees(upperLat, upperLon));
  }

  /** Private to MapbooBuilder, don't call it */
  public final int getApplicationEventId()
  {
    return _applicationEventId;
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationEventId(int eventId)
  {
    _applicationEventId = eventId;
  }

  /** Private to MapbooBuilder, don't call it */
  public final int getApplicationTimestamp()
  {
    return _applicationTimestamp;
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationTimestamp(int timestamp)
  {
    _applicationTimestamp = timestamp;
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationName(String name)
  {
    if (_applicationName.compareTo(name) != 0)
    {
      _applicationName = name;
  
      if (_applicationListener != null)
      {
        _applicationListener.onNameChanged(_context, _applicationName);
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationWebsite(String website)
  {
    if (_applicationWebsite.compareTo(website) != 0)
    {
      _applicationWebsite = website;
  
      if (_applicationListener != null)
      {
        _applicationListener.onWebsiteChanged(_context, _applicationWebsite);
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationEMail(String eMail)
  {
    if (_applicationEMail.compareTo(eMail) != 0)
    {
      _applicationEMail = eMail;
  
      if (_applicationListener != null)
      {
        _applicationListener.onEMailChanged(_context, _applicationEMail);
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationAbout(String about)
  {
    if (_applicationAbout.compareTo(about) != 0)
    {
      _applicationAbout = about;
  
      if (_applicationListener != null)
      {
        _applicationListener.onAboutChanged(_context, _applicationAbout);
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void addApplicationScene(MapBooOLD_Scene scene, int position)
  {
    _applicationScenes.add(position, scene);
  
    fireOnScenesChanged();
  }

  /** Private to MapbooBuilder, don't call it */
  public final void deleteApplicationScene(String sceneId)
  {
    final int scenesCount = _applicationScenes.size();
    int sceneIndex = -1;
    for (int i = 0; i < scenesCount; i++)
    {
      final String iSceneId = _applicationScenes.get(i).getId();
      if (iSceneId.compareTo(sceneId) == 0)
      {
        sceneIndex = i;
        break;
      }
    }
    if (sceneIndex != -1)
    {
      MapBooOLD_Scene scene = _applicationScenes.get(sceneIndex);
      _applicationScenes.remove(sceneIndex);
      if (scene != null)
         scene.dispose();
  
      if (_viewType == MapBooOLD_ViewType.VIEW_RUNTIME)
      {
        if (_applicationCurrentSceneId.compareTo(sceneId) == 0)
        {
          setApplicationCurrentSceneId(_applicationScenes.get(0).getId());
        }
      }
  
      fireOnScenesChanged();
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationScenes(java.util.ArrayList<MapBooOLD_Scene> applicationScenes)
  {
    final int currentScenesCount = _applicationScenes.size();
    for (int i = 0; i < currentScenesCount; i++)
    {
      MapBooOLD_Scene scene = _applicationScenes.get(i);
      if (scene != null)
         scene.dispose();
    }
  
    _applicationScenes.clear();
  
    _applicationScenes = new java.util.ArrayList<MapBooOLD_Scene>(applicationScenes);
  
    fireOnScenesChanged();
  }

  /** Private to MapbooBuilder, don't call it */
  public final void saveApplicationData()
  {
    //  std::string                _applicationId;
    //  std::string                _applicationName;
    //  std::string                _applicationWebsite;
    //  std::string                _applicationEMail;
    //  std::string                _applicationAbout;
    //  int                        _applicationTimestamp;
    //  std::vector<MapBooOLD_Scene*> _applicationScenes;
    //  int                        _applicationCurrentSceneIndex;
    //  int                        _lastApplicationCurrentSceneIndex;
  ///#warning Diego at work!
  }

  /** Private to MapbooBuilder, don't call it */
  public final URL createApplicationTubeURL()
  {
    final String tubesPath = _tubesURL._path;
  
    String view;
    switch (_viewType)
    {
      case VIEW_PRESENTATION:
        view = "presentation";
        break;
      case VIEW_EDITION_PREVIEW:
        view = "edition-preview";
        break;
      default:
        view = "runtime";
    }
  
    return new URL(tubesPath + "/application/" + _applicationId + "/" + view, false);
  }

  /** Private to MapbooBuilder, don't call it */
  public final void parseApplicationJSON(String json, URL url)
  {
    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(json, true);
  
    if (jsonBaseObject == null)
    {
      ILogger.instance().logError("Can't parse ApplicationJSON from %s", url._path);
    }
    else
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      parseApplicationJSON(jsonObject, url);
    }
    if (jsonBaseObject != null)
       jsonBaseObject.dispose();
  }

  /** Private to MapbooBuilder, don't call it */
  public final void parseApplicationJSON(JSONObject jsonObject, URL url)
  {
    java.util.ArrayList<String> errors = new java.util.ArrayList<String>();
  
    if (jsonObject == null)
    {
      ILogger.instance().logError("Invalid ApplicationJSON");
    }
    else
    {
      final JSONString jsonError = jsonObject.getAsString("error");
      if (jsonError == null)
      {
        final int eventId = (int) jsonObject.getAsNumber("eventId", 0);
        final int timestamp = (int) jsonObject.getAsNumber("timestamp", 0);
  
        if (getApplicationEventId() != eventId)
        {
          final JSONString jsonName = jsonObject.getAsString("name");
          if (jsonName != null)
          {
            setApplicationName(jsonName.value());
          }
  
          final JSONString jsonWebsite = jsonObject.getAsString("website");
          if (jsonWebsite != null)
          {
            setApplicationWebsite(jsonWebsite.value());
          }
  
          final JSONString jsonEMail = jsonObject.getAsString("email");
          if (jsonEMail != null)
          {
            setApplicationEMail(jsonEMail.value());
          }
  
          final JSONString jsonAbout = jsonObject.getAsString("about");
          if (jsonAbout != null)
          {
            setApplicationAbout(jsonAbout.value());
          }
  
          final JSONObject jsonScene = jsonObject.getAsObject("scene");
          if (jsonScene != null)
          {
            parseSceneEventAndUpdateScene(jsonScene);
          }
  
          final JSONArray jsonAllScenes = jsonObject.getAsArray("scenes");
          if (jsonAllScenes != null)
          {
            java.util.ArrayList<MapBooOLD_Scene> scenes = new java.util.ArrayList<MapBooOLD_Scene>();
  
            final int scenesCount = jsonAllScenes.size();
            for (int i = 0; i < scenesCount; i++)
            {
              MapBooOLD_Scene scene = parseScene(jsonAllScenes.getAsObject(i));
              if (scene != null)
              {
                scenes.add(scene);
              }
            }
  
            setApplicationScenes(scenes);
          }
  
          final JSONObject jsonScenes = jsonObject.getAsObject("scenes");
          if (jsonScenes != null)
          {
            final JSONObject jsonPutScene = jsonScenes.getAsObject("putScene");
            if (jsonPutScene != null)
            {
              final JSONNumber jsonPosition = jsonPutScene.getAsNumber("position");
              int position = (jsonPosition != null) ? (int) jsonPosition.value() : 0;
              final JSONObject jsonNewScene = jsonPutScene.getAsObject("scene");
              if (jsonNewScene != null)
              {
                MapBooOLD_Scene scene = parseScene(jsonNewScene);
                if (scene != null)
                {
                  addApplicationScene(scene, position);
                }
              }
            }
            else
            {
              final JSONObject jsonDeleteScene = jsonScenes.getAsObject("deleteScene");
              if (jsonDeleteScene != null)
              {
                final JSONString jsonSceneId = jsonDeleteScene.getAsString("sceneId");
                if (jsonSceneId != null)
                {
                  deleteApplicationScene(jsonSceneId.value());
                }
              }
            }
          }
  
          setApplicationEventId(eventId);
          setApplicationTimestamp(timestamp);
          saveApplicationData();
          setHasParsedApplication();
        }
  
        final JSONString jsonCurrentSceneId = jsonObject.getAsString("currentSceneId");
        if (jsonCurrentSceneId != null)
        {
          setApplicationCurrentSceneId(jsonCurrentSceneId.value());
        }
  
        if (_enableNotifications)
        {
          final JSONArray jsonNotifications = jsonObject.getAsArray("notifications");
          if (jsonNotifications != null)
          {
            addApplicationNotifications(parseNotifications(jsonNotifications));
          }
  
          final JSONObject jsonNotification = jsonObject.getAsObject("notification");
          if (jsonNotification != null)
          {
            addApplicationNotification(parseNotification(jsonNotification));
          }
        }
  
        if (_initialParse)
        {
          _initialParse = false;
          if (_applicationCurrentSceneId.compareTo("-1") == 0)
          {
            if (_applicationScenes.size() > 0)
            {
              setApplicationCurrentSceneId(_applicationScenes.get(0).getId());
            }
          }
        }
      }
      else
      {
        errors.add(jsonError.value());
        ILogger.instance().logError("Server Error: %s", jsonError.value());
        if (_initialParse)
        {
          _initialParse = false;
          setHasParsedApplication();
        }
      }
    }
    _mbErrorRenderer.setErrors(errors);
  }

  /** Private to MapbooBuilder, don't call it */
  public final void parseApplicationEventsJSON(String json, URL url)
  {
    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(json, true);
    if (jsonBaseObject == null)
    {
      ILogger.instance().logError("Can't parse ApplicationJSON from %s", url._path);
    }
    else
    {
      final JSONArray jsonArray = jsonBaseObject.asArray();
      if (jsonArray != null)
      {
        final int size = jsonArray.size();
        for (int i = 0; i < size; i++)
        {
          final JSONObject jsonObject = jsonArray.getAsObject(i);
          parseApplicationJSON(jsonObject, url);
        }
      }
      else
      {
        parseApplicationJSON(json, url);
      }
    }
    if (jsonBaseObject != null)
       jsonBaseObject.dispose();
  }

  /** Private to MapbooBuilder, don't call it */
  public final void parseSceneEventAndUpdateScene(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return;
    }
  
    final JSONString jsonSceneToBeUpdatedID = jsonObject.getAsString("id");
    if (jsonSceneToBeUpdatedID == null)
    {
      return;
    }
    final String sceneToBeUpdatedID = jsonSceneToBeUpdatedID.value();
    final int scenesCount = _applicationScenes.size();
    for (int i = 0; i < scenesCount; i++)
    {
      final String sceneID = _applicationScenes.get(i).getId();
      if (sceneID.compareTo(sceneToBeUpdatedID) == 0)
      {
        MapBooOLD_Scene oldScene = _applicationScenes.get(i);
  
        final String name = jsonObject.getAsString("name", oldScene.getName());
        final String description = jsonObject.getAsString("description", oldScene.getDescription());
        final JSONBaseObject jboScreenshot = jsonObject.get("screenshot");
        final MapBooOLD_MultiImage screenshot;
        if (jboScreenshot != null)
        {
          screenshot = parseMultiImage(jboScreenshot.asObject());
        }
        else
        {
          final MapBooOLD_MultiImage oldScreenshot = oldScene.getScreenshot();
          screenshot = (oldScreenshot != null) ? oldScreenshot.deepCopy() : null;
        }
        final JSONBaseObject jboBackgroundColor = jsonObject.get("backgroundColor");
        final Color backgroundColor = (jboBackgroundColor != null) ? parseColor(jboBackgroundColor.asString()) : oldScene.getBackgroundColor();
        final JSONBaseObject jboCameraPosition = jsonObject.get("cameraPosition");
        final MapBooOLD_CameraPosition cameraPosition;
        if (jboCameraPosition != null)
        {
          cameraPosition = parseCameraPosition(jboCameraPosition.asObject());
        }
        else
        {
          final MapBooOLD_CameraPosition oldCameraPosition = oldScene.getCameraPosition();
          cameraPosition = (oldCameraPosition != null) ? new MapBooOLD_CameraPosition(oldCameraPosition.getPosition(), oldCameraPosition.getHeading(), oldCameraPosition.getPitch(), oldCameraPosition.isAnimated()) : null;
        }
        final JSONBaseObject jboSector = jsonObject.get("sector");
        final Sector sector;
        if (jboSector != null)
        {
          sector = parseSector(jboSector.asObject());
        }
        else
        {
          final Sector oldSector = oldScene.getSector();
          sector = (oldSector != null) ? new Sector(oldSector._lower, oldSector._upper) : null;
        }
        final JSONBaseObject jboBaseLayer = jsonObject.get("baseLayer");
        Layer baseLayer = (jboBaseLayer != null) ? parseLayer(jboBaseLayer.asObject()) : oldScene.getBaseLayer().copy();
        final JSONBaseObject jboOverlayLayer = jsonObject.get("overlayLayer");
        Layer oldOverlayLayer = (oldScene.getOverlayLayer() != null) ? oldScene.getOverlayLayer().copy() : null;
        Layer overlayLayer = (jboOverlayLayer != null) ? parseLayer(jboOverlayLayer.asObject()) : oldOverlayLayer;
  
        final boolean hasWarnings = jsonObject.getAsBoolean("hasWarnings", false);
        final boolean queryable = jsonObject.getAsBoolean("queryable", oldScene.isQueryable());
        final boolean cameraPositionChaged = (jboCameraPosition != null);
  
        MapBooOLD_Scene newScene = new MapBooOLD_Scene(sceneToBeUpdatedID, name, description, screenshot, backgroundColor, cameraPosition, sector, baseLayer, overlayLayer, queryable, hasWarnings);
  
        _applicationScenes.set(i, newScene);
  
        if (sceneID.compareTo(_applicationCurrentSceneId) == 0)
        {
          updateVisibleScene(cameraPositionChaged);
        }
  
        if (_applicationListener != null)
        {
          _applicationListener.onSceneChanged(_context, newScene);
        }
        fireOnScenesChanged();
  
        if (oldScene != null)
           oldScene.dispose();
  
        break;
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void openApplicationTube(G3MContext context)
  {
  
  //  IDownloader* downloader = context->getDownloader();
  //  downloader->requestBuffer(createApplicationRestURL(),
  //                            DownloadPriority::HIGHEST,
  //                            TimeInterval::zero(),
  //                            false, // readExpired
  //                            new MapBooOLDBuilder_RestJSON(this),
  //                            true);
  
    final IFactory factory = context.getFactory();
    _webSocket = factory.createWebSocket(createApplicationTubeURL(), new MapBooOLDBuilder_ApplicationTubeListener(this), true, true); // autodeleteWebSocket -  autodeleteListener
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationCurrentSceneId(String currentSceneId)
  {
    if (_applicationCurrentSceneId.compareTo(currentSceneId) != 0)
    {
      final int scenesCount = _applicationScenes.size();
      for (int i = 0; i < scenesCount; i++)
      {
        final String sceneId = _applicationScenes.get(i).getId();
        if (sceneId.compareTo(currentSceneId) == 0)
        {
          _applicationCurrentSceneId = currentSceneId;
          changedCurrentScene();
  
          break;
        }
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void rawChangeScene(String sceneId)
  {
    _applicationCurrentSceneId = sceneId;
  
    changedCurrentScene();
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setContext(G3MContext context)
  {
    _context = context;
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationTubeOpened(boolean open)
  {
    if (_isApplicationTubeOpen != open)
    {
      _isApplicationTubeOpen = open;
      if (!_isApplicationTubeOpen)
      {
        _webSocket = null;
      }
  
      if (_isApplicationTubeOpen)
      {
        if (_applicationListener != null)
        {
          _applicationListener.onWebSocketOpen(_context);
        }
      }
      else
      {
        if (_applicationListener != null)
        {
          _applicationListener.onWebSocketClose(_context);
        }
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final boolean isApplicationTubeOpen()
  {
    return _isApplicationTubeOpen;
  }

  /** Private to MapbooBuilder, don't call it */
  public final boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile)
  {
    if (_applicationListener != null)
    {
      _applicationListener.onTerrainTouch(this, ec, pixel, camera, position, tile);
    }
  
    return true;
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setHasParsedApplication()
  {
    _hasParsedApplication = true;
  }

  /** Private to MapbooBuilder, don't call it */
  public final boolean hasParsedApplication()
  {
    return _hasParsedApplication;
  }


  public final MapBooOLD_Notification createNotification(Geodetic2D position, Camera camera, String message, URL iconURL)
  {
    MapBooOLD_CameraPosition cameraPosition = new MapBooOLD_CameraPosition(camera.getGeodeticPosition(), camera.getHeading(), camera.getPitch(), true); // animated
    return new MapBooOLD_Notification(position, cameraPosition, message, iconURL);
  }

  public final void sendNotification(Geodetic2D position, MapBooOLD_CameraPosition cameraPosition, String message, URL iconURL)
  {
    if ((_webSocket != null) && _isApplicationTubeOpen)
    {
      _webSocket.send(getSendNotificationCommand(position, cameraPosition, message, iconURL));
    }
    else
    {
      ILogger.instance().logError("Can't send notification, websocket disconnected");
    }
  }

  public final void sendNotification(Geodetic2D position, Camera camera, String message, URL iconURL)
  {
    if ((_webSocket != null) && _isApplicationTubeOpen)
    {
      _webSocket.send(getSendNotificationCommand(position, camera, message, iconURL));
    }
    else
    {
      ILogger.instance().logError("Can't send notification, websocket disconnected");
    }
  }

  public final void changeScene(String sceneId)
  {
    final String currentSceneId = getApplicationCurrentSceneId();
    if (currentSceneId.compareTo(sceneId) != 0)
    {
      final int scenesCount = _applicationScenes.size();
      for (int i = 0; i < scenesCount; i++)
      {
        final String iSceneId = _applicationScenes.get(i).getId();
        if (sceneId.compareTo(iSceneId) == 0)
        {
          getThreadUtils().invokeInRendererThread(new MapBooOLDBuilder_ChangeSceneTask(this, sceneId), true);
          break;
        }
      }
    }
  }

  public final void changeScene(MapBooOLD_Scene scene)
  {
    final int size = _applicationScenes.size();
    for (int i = 0; i < size; i++)
    {
      if (_applicationScenes.get(i) == scene)
      {
        changeScene(scene.getId());
        break;
      }
    }
  }


  public final boolean isQueryableCurrentScene()
  {
    return getApplicationCurrentScene().isQueryable();
  }

  public final URL getServerURL()
  {
    return _serverURL;
  }

  public final void requestGetFeatureInfo(Tile tile, Vector2I size, Vector2I pixel, Geodetic3D position)
  {
    _g3mWidget.getG3MContext().getDownloader().requestBuffer(createGetFeatureInfoRestURL(tile, size, pixel, position), DownloadPriority.HIGHER, TimeInterval.zero(), false, _featureInfoDownloadListener, false);
  }

  /** Private to MapbooBuilder, don't call it */
  public final void pollApplicationDataFromServer(G3MContext context)
  {
    IDownloader downloader = context.getDownloader();
    downloader.requestBuffer(createApplicationPollURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), false, new MapBooOLDBuilder_RestJSON(this), true); // readExpired
  }

  public final String getApplicationId()
  {
    return _applicationId;
  }
}