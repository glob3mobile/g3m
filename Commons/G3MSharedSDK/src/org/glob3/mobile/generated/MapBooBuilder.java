package org.glob3.mobile.generated; 
public abstract class MapBooBuilder
{

  private final URL _serverURL;
  private final URL _tubesURL;

  private MapBoo_ViewType _viewType;

  private MapBooApplicationChangeListener _applicationListener;

  private String _applicationId;
  private String _applicationName;
  private String _applicationWebsite;
  private String _applicationEMail;
  private String _applicationAbout;
  private int _applicationTimestamp;

  private java.util.ArrayList<MapBoo_Scene> _applicationScenes = new java.util.ArrayList<MapBoo_Scene>();
  private int _applicationCurrentSceneIndex;
  private int _lastApplicationCurrentSceneIndex;

  private GL _gl;
  private G3MWidget _g3mWidget;
  private IStorage _storage;

  private IWebSocket _webSocket;

  private G3MContext _context;

  private boolean _isApplicationTubeOpen;

  private LayerSet _layerSet;
  private PlanetRenderer createPlanetRenderer()
  {
    TileTessellator tessellator = new PlanetTileTessellator(true, Sector.fullSphere());
  
    ElevationDataProvider elevationDataProvider = null;
    final float verticalExaggeration = 1F;
    TileTexturizer texturizer = new MultiLayerTileTexturizer();
    TileRasterizer tileRasterizer = null;
  
    final boolean renderDebug = false;
    final boolean useTilesSplitBudget = true;
    final boolean forceFirstLevelTilesRenderOnStart = true;
    final boolean incrementalTileQuality = false;
  
    final TilesRenderParameters parameters = new TilesRenderParameters(renderDebug, useTilesSplitBudget, forceFirstLevelTilesRenderOnStart, incrementalTileQuality);
  
    final boolean showStatistics = false;
    long texturePriority = DownloadPriority.HIGHER;
  
    int TODO_CHECK_MAPBOO_FULLSPHERE;
    final Sector renderedSector = Sector.fullSphere();
  
    return new PlanetRenderer(tessellator, elevationDataProvider, verticalExaggeration, texturizer, tileRasterizer, _layerSet, parameters, showStatistics, texturePriority, renderedSector);
  }

  private java.util.ArrayList<ICameraConstrainer> createCameraConstraints()
  {
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
    SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
    cameraConstraints.add(scc);
  
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
    else
    {
      ILogger.instance().logError("Unsupported layer type \"%s\"", layerType);
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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BingMapsLayer parseBingMapsLayer(JSONObject jsonLayer, TimeInterval timeToCache);

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
    if (imageFormat.compareTo("JPG") == 0)
    {
      imageFormat = "image/jpeg";
    }
    final String srs = jsonLayer.getAsString("projection", "EPSG_4326");
    LayerTilesRenderParameters layerTilesRenderParameters = null;
    if (srs.compareTo("EPSG_4326") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultNonMercator(Sector.fullSphere());
    }
    else if (srs.compareTo("EPSG_900913") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultMercator(0, 17);
    }
    final boolean isTransparent = jsonLayer.getAsBoolean("transparent", false);
    final double expiration = jsonLayer.getAsNumber("expiration", 0);
    final long milliseconds = IMathUtils.instance().round(expiration);
    final TimeInterval timeToCache = TimeInterval.fromMilliseconds(milliseconds);
    final boolean readExpired = jsonLayer.getAsBoolean("acceptExpiration", false);
  
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, sector, imageFormat, (srs.compareTo("EPSG_4326") == 0) ? "EPSG:4326" : "EPSG:900913", style, isTransparent, null, timeToCache, readExpired, layerTilesRenderParameters);
  }


  private int getApplicationCurrentSceneIndex()
  {
    if (_applicationCurrentSceneIndex < 0)
    {
      _applicationCurrentSceneIndex = 0;
    }
    return _applicationCurrentSceneIndex;
  }
  private MapBoo_Scene getApplicationCurrentScene()
  {
    final int sceneIndex = getApplicationCurrentSceneIndex();
  
    final boolean validSceneIndex = ((sceneIndex >= 0) && (sceneIndex < _applicationScenes.size()));
  
    return validSceneIndex ? _applicationScenes.get(sceneIndex) : null;
  }

  private Color getCurrentBackgroundColor()
  {
    final MapBoo_Scene scene = getApplicationCurrentScene();
    return (scene == null) ? Color.black() : scene.getBackgroundColor();
  }

  private MapBoo_Scene parseScene(JSONObject jsonObject)
  {
    if (jsonObject == null)
    {
      return null;
    }
  
    final boolean hasWarnings = jsonObject.getAsBoolean("hasWarnings", false);
  
    if (hasWarnings && (_viewType != MapBoo_ViewType.VIEW_PRESENTATION))
    {
      return null;
    }
  
    return new MapBoo_Scene(jsonObject.getAsString("name", ""), jsonObject.getAsString("description", ""), parseMultiImage(jsonObject.getAsObject("screenshot")), parseColor(jsonObject.getAsString("backgroundColor")), parseCameraPosition(jsonObject.getAsObject("cameraPosition")), parseLayer(jsonObject.get("baseLayer")), parseLayer(jsonObject.get("overlayLayer")), hasWarnings);
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
        final MapBoo_CameraPosition cameraPosition = currentScene.getCameraPosition();
        if (cameraPosition != null)
        {
          _g3mWidget.setAnimatedCameraPosition(TimeInterval.fromSeconds(3), cameraPosition.getPosition(), cameraPosition.getHeading(), cameraPosition.getPitch());
        }
      }
    }
  
    if (_applicationListener != null)
    {
      _applicationListener.onSceneChanged(_context, getApplicationCurrentSceneIndex(), currentScene);
    }
  
    if (_viewType == MapBoo_ViewType.VIEW_PRESENTATION)
    {
      if (_webSocket == null)
      {
        ILogger.instance().logError("VIEW_PRESENTATION: can't fire the event of changed scene");
      }
      else
      {
        if (_applicationCurrentSceneIndex != _lastApplicationCurrentSceneIndex)
        {
          if (_lastApplicationCurrentSceneIndex >= 0)
          {
            _webSocket.send(getApplicationCurrentSceneCommand());
          }
          _lastApplicationCurrentSceneIndex = _applicationCurrentSceneIndex;
        }
      }
    }
  }

  private String getApplicationCurrentSceneCommand()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("currentSceneIndex=");
    isb.addInt(_applicationCurrentSceneIndex);
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }


  protected MapBooBuilder(URL serverURL, URL tubesURL, String applicationId, MapBoo_ViewType viewType, MapBooApplicationChangeListener applicationListener)
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
     _gpuProgramManager = null;
     _isApplicationTubeOpen = false;
     _applicationCurrentSceneIndex = -1;
     _lastApplicationCurrentSceneIndex = -1;
     _context = null;
     _webSocket = null;
  
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
    }
    if (gl == null)
    {
      ILogger.instance().logError("LOGIC ERROR: _gl cannot be NULL");
      return;
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
  
    PlanetRenderer planetRenderer = createPlanetRenderer();
    mainRenderer.addRenderer(planetRenderer);
  
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = createCameraConstraints();
  
    GInitializationTask initializationTask = new MapBooBuilder_ApplicationTubeConnector(this);
  
    java.util.ArrayList<PeriodicalTask> periodicalTasks = createPeriodicalTasks();
  
    ICameraActivityListener cameraActivityListener = null;
  
    int TODO_VIEWPORT;
    final Planet planet = createPlanet();
    //  Geodetic3D initialCameraPosition = planet->getDefaultCameraPosition(Vector2I(1,1), Sector::fullSphere());
  
    InitialCameraPositionProvider icpp = new SimpleInitialCameraPositionProvider();
  
    _g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), cameraActivityListener, planet, cameraConstraints, createCameraRenderer(), mainRenderer, createBusyRenderer(), Color.black(), false, false, initializationTask, true, periodicalTasks, getGPUProgramManager(), createSceneLighting(), icpp); // autoDeleteInitializationTask -  logDownloaderStatistics -  logFPS
    cameraConstraints = null;
    periodicalTasks = null;
  
    return _g3mWidget;
  }

  protected final Planet createPlanet()
  {
    return Planet.createEarth();
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
    return new DefaultSceneLighting();
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
  
    _applicationScenes = applicationScenes;
  
    if (_applicationListener != null)
    {
      _applicationListener.onScenesChanged(_context, _applicationScenes);
    }
  
    changedCurrentScene();
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
        //    case VIEW_RUNTIME:
        //      view = "runtime";
        //      break;
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
  
            final JSONArray jsonScenes = jsonObject.getAsArray("scenes");
            if (jsonScenes != null)
            {
              java.util.ArrayList<MapBoo_Scene> scenes = new java.util.ArrayList<MapBoo_Scene>();
  
              final int scenesCount = jsonScenes.size();
              for (int i = 0; i < scenesCount; i++)
              {
                MapBoo_Scene scene = parseScene(jsonScenes.getAsObject(i));
                if (scene != null)
                {
                  scenes.add(scene);
                }
              }
  
              setApplicationScenes(scenes);
            }
  
            setApplicationTimestamp(timestamp);
          }
  
          final JSONNumber jsonCurrentSceneIndex = jsonObject.getAsNumber("currentSceneIndex");
          if (jsonCurrentSceneIndex != null)
          {
            setApplicationCurrentSceneIndex((int) jsonCurrentSceneIndex.value());
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
    if (_webSocket != null)
       _webSocket.dispose();
  
    final IFactory factory = context.getFactory();
    _webSocket = factory.createWebSocket(createApplicationTubeURL(), new MapBooBuilder_ApplicationTubeListener(this), true, true); // autodeleteWebSocket -  autodeleteListener
  }

  /** Private to MapbooBuilder, don't call it */
  public final void setApplicationCurrentSceneIndex(int sceneIndex)
  {
    if (sceneIndex != _applicationCurrentSceneIndex)
    {
      final boolean validSceneIndex = ((sceneIndex >= 0) && (sceneIndex < _applicationScenes.size()));
  
      if (validSceneIndex)
      {
        _applicationCurrentSceneIndex = sceneIndex;
        changedCurrentScene();
      }
    }
  }

  /** Private to MapbooBuilder, don't call it */
  public final void rawChangeScene(int sceneIndex)
  {
    _applicationCurrentSceneIndex = sceneIndex;
  
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

  public final void changeScene(int sceneIndex)
  {
    final int currentSceneIndex = getApplicationCurrentSceneIndex();
    if (currentSceneIndex != sceneIndex)
    {
      final boolean validSceneIndex = ((sceneIndex >= 0) && (sceneIndex < _applicationScenes.size()));
  
      if (validSceneIndex)
      {
        getThreadUtils().invokeInRendererThread(new MapBooBuilder_ChangeSceneTask(this, sceneIndex), true);
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
        changeScene(i);
        break;
      }
    }
  }

}