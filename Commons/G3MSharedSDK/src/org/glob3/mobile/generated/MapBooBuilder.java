package org.glob3.mobile.generated; 
public abstract class MapBooBuilder
{

  private final URL _serverURL;
  private final URL _tubesURL;

  private MapBoo_ViewType _viewType;

  private MapBooApplicationChangeListener _applicationListener;

  private final boolean _enableNotifications;

  private String _applicationId;
  private String _applicationName;
  private String _applicationWebsite;
  private String _applicationEMail;
  private String _applicationAbout;
  private int _applicationTimestamp;
  private java.util.ArrayList<MapBoo_Scene> _applicationScenes = new java.util.ArrayList<MapBoo_Scene>();
  private String _applicationCurrentSceneId;
  private String _lastApplicationCurrentSceneId;

  private GL _gl;
  private G3MWidget _g3mWidget;
  private IStorage _storage;

  private IWebSocket _webSocket;

  private G3MContext _context;

  private boolean _isApplicationTubeOpen;

  private LayerSet _layerSet;
  private PlanetRenderer createPlanetRenderer()
  {
    final boolean skirted = true;
    TileTessellator tessellator = new PlanetTileTessellator(skirted, Sector.fullSphere());
  
    ElevationDataProvider elevationDataProvider = null;
    final float verticalExaggeration = 1F;
    TileTexturizer texturizer = new MultiLayerTileTexturizer();
    TileRasterizer tileRasterizer = null;
  
    final boolean renderDebug = false;
    final boolean useTilesSplitBudget = true;
    final boolean forceFirstLevelTilesRenderOnStart = true;
    final boolean incrementalTileQuality = false;
    final Quality quality = Quality.QUALITY_LOW;
  
    final TilesRenderParameters parameters = new TilesRenderParameters(renderDebug, useTilesSplitBudget, forceFirstLevelTilesRenderOnStart, incrementalTileQuality, quality);
  
    final boolean showStatistics = false;
    long texturePriority = DownloadPriority.HIGHER;
  
    final Sector renderedSector = Sector.fullSphere();
    final boolean renderTileMeshes = true;
  
    PlanetRenderer result = new PlanetRenderer(tessellator, elevationDataProvider, true, verticalExaggeration, texturizer, tileRasterizer, _layerSet, parameters, showStatistics, texturePriority, renderedSector, renderTileMeshes);
  
    if (_enableNotifications)
    {
      result.addTerrainTouchListener(new MapBooBuilder_TerrainTouchListener(this));
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
    cameraRenderer.addHandler(new CameraDoubleTapHandler());
  
    return cameraRenderer;
  }

  private Renderer createBusyRenderer()
  {
    return new BusyMeshRenderer(Color.newFromRGBA(0, 0, 0, 1));
  }

  private ErrorRenderer createErrorRenderer()
  {
    return new HUDErrorRenderer();
  }

  private java.util.ArrayList<PeriodicalTask> createPeriodicalTasks()
  {
    java.util.ArrayList<PeriodicalTask> periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
  
    periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(5), new MapBooBuilder_TubeWatchdogPeriodicalTask(this)));
  
    return periodicalTasks;
  }

  private void recreateLayerSet()
  {
    final MapBoo_Scene scene = getApplicationCurrentScene();
  
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
  
    final String layerType = jsonLayer.getAsString("layer", "<layer not present>");
    if (layerType.compareTo("OSM") == 0)
    {
      return new OSMLayer(defaultTimeToCache);
    }
    else if (layerType.compareTo("MapQuest") == 0)
    {
      return parseMapQuestLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("BingMaps") == 0)
    {
      return parseBingMapsLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("CartoDB") == 0)
    {
      return parseCartoDBLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("MapBox") == 0)
    {
      return parseMapBoxLayer(jsonLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("WMS") == 0)
    {
      return parseWMSLayer(jsonLayer);
    }
    else if (layerType.compareTo("URLTemplate") == 0)
    {
      return parseURLTemplateLayer(jsonLayer);
    }
    else
    {
      ILogger.instance().logError("Unsupported layer type \"%s\"", layerType);
      ILogger.instance().logError("%s", jsonBaseObjectLayer.description());
      return null;
    }
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
  
    return new BingMapsLayer(imagerySet, key, timeToCache);
  }

  private CartoDBLayer parseCartoDBLayer(JSONObject jsonLayer, TimeInterval timeToCache)
  {
    final String userName = jsonLayer.getAsString("userName", "");
    final String table = jsonLayer.getAsString("table", "");
  
    return new CartoDBLayer(userName, table, timeToCache);
  }


  private MapBoxLayer parseMapBoxLayer(JSONObject jsonLayer, TimeInterval timeToCache)
  {
    final String mapKey = jsonLayer.getAsString("mapKey", "");
  
    return new MapBoxLayer(mapKey, timeToCache);
  }

  private WMSLayer parseWMSLayer(JSONObject jsonLayer)
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
    final double lowerLat = jsonLayer.getAsNumber("lowerLat", -90.0);
    final double lowerLon = jsonLayer.getAsNumber("lowerLon", -180.0);
    final double upperLat = jsonLayer.getAsNumber("upperLat", 90.0);
    final double upperLon = jsonLayer.getAsNumber("upperLon", 180.0);
    final Sector sector = new Sector(new Geodetic2D(Angle.fromDegrees(lowerLat), Angle.fromDegrees(lowerLon)), new Geodetic2D(Angle.fromDegrees(upperLat), Angle.fromDegrees(upperLon)));
    String imageFormat = jsonLayer.getAsString("imageFormat", "image/png");
    final String srs = jsonLayer.getAsString("projection", "EPSG:4326");
    LayerTilesRenderParameters layerTilesRenderParameters = null;
    if (srs.compareTo("EPSG:4326") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultWGS84(Sector.fullSphere());
    }
    else if (srs.compareTo("EPSG:3857") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultMercator(0, 17);
    }
    final boolean isTransparent = jsonLayer.getAsBoolean("transparent", false);
    final double expiration = jsonLayer.getAsNumber("expiration", 0);
    final long milliseconds = IMathUtils.instance().round(expiration);
    final TimeInterval timeToCache = TimeInterval.fromMilliseconds(milliseconds);
    final boolean readExpired = jsonLayer.getAsBoolean("acceptExpiration", false);
  
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, sector, imageFormat, srs, style, isTransparent, null, timeToCache, readExpired, layerTilesRenderParameters);
  }

  private URLTemplateLayer parseURLTemplateLayer(JSONObject jsonLayer)
  {
    final String urlTemplate = jsonLayer.getAsString("url", "");
  
    final boolean transparent = jsonLayer.getAsBoolean("transparent", true);
  
    final int firstLevel = (int) jsonLayer.getAsNumber("firstLevel", 1);
    final int maxLevel = (int) jsonLayer.getAsNumber("maxLevel", 19);
  
    final String projection = jsonLayer.getAsString("projection", "EPSG:3857");
    final boolean mercator = (projection.equals("EPSG:3857"));
  
    final double lowerLat = jsonLayer.getAsNumber("lowerLat", -90.0);
    final double lowerLon = jsonLayer.getAsNumber("lowerLon", -180.0);
    final double upperLat = jsonLayer.getAsNumber("upperLat", 90.0);
    final double upperLon = jsonLayer.getAsNumber("upperLon", 180.0);
  
    final Sector sector = Sector.fromDegrees(lowerLat, lowerLon, upperLat, upperLon);
  
    URLTemplateLayer result;
    if (mercator)
    {
      result = URLTemplateLayer.newMercator(urlTemplate, sector, transparent, firstLevel, maxLevel, TimeInterval.fromDays(30));
    }
    else
    {
      result = URLTemplateLayer.newWGS84(urlTemplate, sector, transparent, firstLevel, maxLevel, TimeInterval.fromDays(30));
    }
  
    return result;
  }

  private String getApplicationCurrentSceneId()
  {
  //  if (_applicationCurrentSceneId.compare("-1") == 0) {
  //    _applicationCurrentSceneId = _applicationScenes.at(0)->getId() ;
  //  }
    return _applicationCurrentSceneId;
  }
  private MapBoo_Scene getApplicationCurrentScene()
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
    final MapBoo_Scene scene = getApplicationCurrentScene();
    return (scene == null) ? Color.black() : scene.getBackgroundColor();
  }

//  const std::string parseSceneId(const JSONObject* jsonObject) const;
  private MapBoo_Scene parseScene(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    final boolean hasWarnings = jsonObject.getAsBoolean("hasWarnings", false);
  
    //  if (hasWarnings && (_viewType != VIEW_PRESENTATION)) {
    //    return NULL;
    //  }
  
    return new MapBoo_Scene(jsonObject.getAsString("id", ""), jsonObject.getAsString("name", ""), jsonObject.getAsString("description", ""), parseMultiImage(jsonObject.getAsObject("screenshot")), parseColor(jsonObject.getAsString("backgroundColor")), parseCameraPosition(jsonObject.getAsObject("cameraPosition")), parseSector(jsonObject.get("sector")), parseLayer(jsonObject.get("baseLayer")), parseLayer(jsonObject.get("overlayLayer")), hasWarnings);
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

  //const std::string MapBooBuilder::parseSceneId(const JSONObject* jsonObject) const {
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

  private MapBoo_MultiImage parseMultiImage(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    Color averageColor = parseColor(jsonObject.getAsString("averageColor"));
  
    java.util.ArrayList<MapBoo_MultiImage_Level> levels = new java.util.ArrayList<MapBoo_MultiImage_Level>();
  
    final JSONArray jsLevels = jsonObject.getAsArray("levels");
    if (jsLevels != null)
    {
      final int levelsCount = jsLevels.size();
      for (int i = 0; i < levelsCount; i++)
      {
        MapBoo_MultiImage_Level level = parseMultiImageLevel(jsLevels.getAsObject(i));
        if (level != null)
        {
          levels.add(level);
        }
      }
    }
  
    return new MapBoo_MultiImage(averageColor, levels);
  }
  private MapBoo_MultiImage_Level parseMultiImageLevel(JSONObject jsonObject)
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
  
    return new MapBoo_MultiImage_Level(new URL(_serverURL, "/images/" + jsURL.value()), (int) jsWidth.value(), (int) jsHeight.value());
  }
  private MapBoo_CameraPosition parseCameraPosition(JSONObject jsonObject)
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
  
    return new MapBoo_CameraPosition(Geodetic3D.fromDegrees(latitudeInDegress, longitudeInDegress, height), Angle.fromDegrees(headingInDegrees), Angle.fromDegrees(pitchInDegrees), animated);
  }

  private void changedCurrentScene()
  {
    recreateLayerSet();
  
    final MapBoo_Scene currentScene = getApplicationCurrentScene();
  
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
          _g3mWidget.setShownSector(Sector.fullSphere());
        }
        else
        {
          _g3mWidget.setShownSector(sector);
        }
  
        final MapBoo_CameraPosition cameraPosition = currentScene.getCameraPosition();
        if (cameraPosition != null)
        {
          if (cameraPosition.isAnimated())
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
    }
  
    if (_applicationListener != null)
    {
      _applicationListener.onCurrentSceneChanged(_context, getApplicationCurrentSceneId(), currentScene);
    }
  
    if (_viewType == MapBoo_ViewType.VIEW_EDITION_PREVIEW)
    {
      if ((_webSocket != null) && _isApplicationTubeOpen)
      {
        if (_applicationCurrentSceneId.compareTo(_lastApplicationCurrentSceneId) != 0)
        {
          if (_lastApplicationCurrentSceneId.compareTo("-1") != 0)
          {
            _webSocket.send(getApplicationCurrentSceneCommand());
          }
          _lastApplicationCurrentSceneId = _applicationCurrentSceneId;
        }
      }
      else
      {
        ILogger.instance().logError("VIEW_PRESENTATION: can't fire the event of changed scene");
      }
    }
  
  }

  private void updateVisibleScene()
  {
    recreateLayerSet();
    final MapBoo_Scene currentScene = getApplicationCurrentScene();
  
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
          _g3mWidget.setShownSector(Sector.fullSphere());
        }
        else
        {
          _g3mWidget.setShownSector(sector);
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

  private String getSendNotificationCommand(Geodetic2D position, MapBoo_CameraPosition cameraPosition, String message, URL iconURL)
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
      isb.addString(escapeString(iconURL.getPath()));
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
      isb.addString(escapeString(iconURL.getPath()));
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
  
    return su.replaceSubstring(str, "\"", "\\\"");
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
  private String toCameraPositionJSON(MapBoo_CameraPosition cameraPosition)
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

  private MapBoo_Notification parseNotification(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    return new MapBoo_Notification(Geodetic2D.fromDegrees(jsonObject.getAsNumber("latitude", 0), jsonObject.getAsNumber("longitude", 0)), parseCameraPosition(jsonObject.getAsObject("cameraPosition")), jsonObject.getAsString("message", ""), parseURL(jsonObject.getAsString("iconURL")));
  }
  private java.util.ArrayList<MapBoo_Notification> parseNotifications(JSONArray jsonArray)
  {
    java.util.ArrayList<MapBoo_Notification> result = new java.util.ArrayList<MapBoo_Notification>();
  
    if (jsonArray != null)
    {
      final int size = jsonArray.size();
      for (int i = 0; i < size; i++)
      {
        MapBoo_Notification notification = parseNotification(jsonArray.getAsObject(i));
        if (notification != null)
        {
          result.add(notification);
        }
      }
    }
  
    return result;
  }

  private void addApplicationNotification(MapBoo_Notification notification)
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
        final MapBoo_CameraPosition cameraPosition = notification.getCameraPosition();
        if (cameraPosition != null)
        {
          _g3mWidget.setAnimatedCameraPosition(TimeInterval.fromSeconds(3), cameraPosition.getPosition(), cameraPosition.getHeading(), cameraPosition.getPitch());
        }
      }
    }
  
    if (notification != null)
       notification.dispose();
  }
  private void addApplicationNotifications(java.util.ArrayList<MapBoo_Notification> notifications)
  {
    if (notifications == null)
    {
      return;
    }
  
    final int size = notifications.size();
    for (int i = 0; i < size; i++)
    {
      MapBoo_Notification notification = notifications.get(i);
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
                                           new java.util.ArrayList<MapBoo_Scene>(_applicationScenes));
    }
  }

  protected MapBooBuilder(URL serverURL, URL tubesURL, String applicationId, MapBoo_ViewType viewType, MapBooApplicationChangeListener applicationListener, boolean enableNotifications)
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
  
  }

  public void dispose()
  {
  
  }

  protected final void setGL(GL gl)
  {
    if (_gl != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _gl already initialized");
      return;
      //ERROR("LOGIC ERROR: _gl already initialized");
    }
    if (gl == null)
    {
      ILogger.instance().logError("LOGIC ERROR: _gl cannot be NULL");
      return;
      //ERROR("LOGIC ERROR: _gl cannot be NULL");
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
    final Planet planet = createPlanet();
  
    PlanetRenderer planetRenderer = createPlanetRenderer();
    mainRenderer.addRenderer(planetRenderer);
  
    mainRenderer.addRenderer(getMarksRenderer());
  
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = createCameraConstraints(planet, planetRenderer);
  
    GInitializationTask initializationTask = new MapBooBuilder_ApplicationTubeConnector(this);
  
    java.util.ArrayList<PeriodicalTask> periodicalTasks = createPeriodicalTasks();
  
    ICameraActivityListener cameraActivityListener = null;
  
  
    InitialCameraPositionProvider icpp = new SimpleInitialCameraPositionProvider();
  
    Renderer hudRenderer = null;
  
    _g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), cameraActivityListener, planet, cameraConstraints, createCameraRenderer(), mainRenderer, createBusyRenderer(), createErrorRenderer(), hudRenderer, Color.black(), false, false, initializationTask, true, periodicalTasks, getGPUProgramManager(), createSceneLighting(), icpp); // autoDeleteInitializationTask -  logDownloaderStatistics -  logFPS
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

  protected final URL createApplicationRestURL()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString(_serverURL.getPath());
    isb.addString("/applications/");
    isb.addString(_applicationId);
    isb.addString("?view=runtime&lastTs=");
    isb.addInt(_applicationTimestamp);
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
    return new URL(path, false);
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
  public final void addApplicationScene(MapBoo_Scene scene, int position)
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
      MapBoo_Scene scene = _applicationScenes.get(sceneIndex);
      _applicationScenes.remove(sceneIndex);
      if (scene != null)
         scene.dispose();
  
      if (_viewType == MapBoo_ViewType.VIEW_RUNTIME)
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
  public final void setApplicationScene(MapBoo_Scene scene)
  {
    final int scenesCount = _applicationScenes.size();
    final String sceneToBeUpdatedID = scene.getId();
    for (int i = 0; i < scenesCount; i++)
    {
      final String sceneID = _applicationScenes.get(i).getId();
      if (sceneID.compareTo(sceneToBeUpdatedID) == 0)
      {
        MapBoo_Scene oldScene = _applicationScenes.get(i);
        _applicationScenes.set(i, scene);
  
        if (sceneID.compareTo(_applicationCurrentSceneId) == 0)
        {
          updateVisibleScene();
        }
  
        if (_applicationListener != null)
        {
          _applicationListener.onSceneChanged(_context, scene);
        }
        fireOnScenesChanged();
  
        if (oldScene != null)
           oldScene.dispose();
  
        break;
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationScenes(java.util.ArrayList<MapBoo_Scene> applicationScenes)
  {
    final int currentScenesCount = _applicationScenes.size();
    for (int i = 0; i < currentScenesCount; i++)
    {
      MapBoo_Scene scene = _applicationScenes.get(i);
      if (scene != null)
         scene.dispose();
    }
  
    _applicationScenes.clear();
  
    _applicationScenes = new java.util.ArrayList<MapBoo_Scene>(applicationScenes);
  
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
    //  std::vector<MapBoo_Scene*> _applicationScenes;
    //  int                        _applicationCurrentSceneIndex;
    //  int                        _lastApplicationCurrentSceneIndex;
  ///#warning Diego at work!
  }

  /** Private to MapbooBuilder, don't call it */
  public final URL createApplicationTubeURL()
  {
    final String tubesPath = _tubesURL.getPath();
  
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
  
    //ILogger::instance()->logInfo(json);
  
    if (jsonBaseObject == null)
    {
      ILogger.instance().logError("Can't parse ApplicationJSON from %s", url.getPath());
    }
    else
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject == null)
      {
        ILogger.instance().logError("Invalid ApplicationJSON");
      }
      else
      {
        final JSONString jsonError = jsonObject.getAsString("error");
        if (jsonError == null)
        {
          final int timestamp = (int) jsonObject.getAsNumber("timestamp", 0);
  
          if (getApplicationTimestamp() != timestamp)
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
              MapBoo_Scene scene = parseScene(jsonScene);
              if (scene != null)
              {
                setApplicationScene(scene);
              }
            }
  
            final JSONArray jsonAllScenes = jsonObject.getAsArray("scenes");
            if (jsonAllScenes != null)
            {
              java.util.ArrayList<MapBoo_Scene> scenes = new java.util.ArrayList<MapBoo_Scene>();
  
              final int scenesCount = jsonAllScenes.size();
              for (int i = 0; i < scenesCount; i++)
              {
                MapBoo_Scene scene = parseScene(jsonAllScenes.getAsObject(i));
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
                  MapBoo_Scene scene = parseScene(jsonNewScene);
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
          ILogger.instance().logError("Server Error: %s", jsonError.value());
        }
      }
  
      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
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
  //                            new MapBooBuilder_RestJSON(this),
  //                            true);
  
    final IFactory factory = context.getFactory();
    _webSocket = factory.createWebSocket(createApplicationTubeURL(), new MapBooBuilder_ApplicationTubeListener(this), true, true); // autodeleteWebSocket -  autodeleteListener
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
  public final boolean onTerrainTouch(G3MEventContext ec, Vector2I pixel, Camera camera, Geodetic3D position, Tile tile)
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


  public final MapBoo_Notification createNotification(Geodetic2D position, Camera camera, String message, URL iconURL)
  {
    MapBoo_CameraPosition cameraPosition = new MapBoo_CameraPosition(camera.getGeodeticPosition(), camera.getHeading(), camera.getPitch(), true); // animated
    return new MapBoo_Notification(position, cameraPosition, message, iconURL);
  }

  public final void sendNotification(Geodetic2D position, MapBoo_CameraPosition cameraPosition, String message, URL iconURL)
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
          getThreadUtils().invokeInRendererThread(new MapBooBuilder_ChangeSceneTask(this, sceneId), true);
          break;
        }
      }
    }
  }

  public final void changeScene(MapBoo_Scene scene)
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


  public final URL getServerURL()
  {
    return _serverURL;
  }

}