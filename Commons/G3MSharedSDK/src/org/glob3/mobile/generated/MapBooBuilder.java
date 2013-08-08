package org.glob3.mobile.generated; 
public abstract class MapBooBuilder
{

  private final URL _serverURL;
  private final URL _tubesURL;

  private final boolean _useWebSockets;

  private MapBooApplicationChangeListener _applicationListener;

  private String _applicationId;
  private String _applicationName;
  private String _applicationDescription;
  private int _applicationTimestamp;

  private java.util.ArrayList<MapBoo_Scene> _applicationScenes = new java.util.ArrayList<MapBoo_Scene>();
  private int _applicationCurrentSceneIndex;
  private int _applicationDefaultSceneIndex;

  private GL _gl;
  private G3MWidget _g3mWidget;
  private IStorage _storage;

//  IWebSocket* _applicationTubeWebSocket;
  private boolean _isApplicationTubeOpen;

  private LayerSet _layerSet;
  private PlanetRenderer createPlanetRenderer()
  {
    final TileTessellator tessellator = new EllipsoidalTileTessellator(true);
  
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
  
    return new PlanetRenderer(tessellator, elevationDataProvider, verticalExaggeration, texturizer, tileRasterizer, _layerSet, parameters, showStatistics, texturePriority);
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
    final boolean processRotation = true;
    final boolean processZoom = true;
    cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
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
  
    if (_useWebSockets)
    {
      periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(2), new MapBooBuilder_TubeWatchdogPeriodicalTask(this)));
    }
    else
    {
      periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(2), new MapBooBuilder_PollingScenePeriodicalTask(this)));
    }
  
    return periodicalTasks;
  }

  private void recreateLayerSet()
  {
    _layerSet.removeAllLayers(false);
  
    final MapBoo_Scene scene = getApplicationCurrentScene();
    if (scene != null)
    {
      scene.recreateLayerSet(_layerSet);
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

//  void resetApplication(const std::string& applicationId);

//  void resetG3MWidget();

  private GInitializationTask createInitializationTask()
  {
    return _useWebSockets ? new MapBooBuilder_SceneTubeConnector(this) : null;
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
  
    final JSONObject jsonBaseLayer = jsonBaseObjectLayer.asObject();
    if (jsonBaseLayer == null)
    {
      ILogger.instance().logError("Layer is not a json object");
      return null;
    }
  
    final String layerType = jsonBaseLayer.getAsString("layer", "<layer not present>");
    if (layerType.compareTo("OSM") == 0)
    {
      return new OSMLayer(defaultTimeToCache);
    }
    else if (layerType.compareTo("MapQuest") == 0)
    {
      return parseMapQuestLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("BingMaps") == 0)
    {
      return parseBingMapsLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("CartoDB") == 0)
    {
      return parseCartoDBLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("MapBox") == 0)
    {
      return parseMapBoxLayer(jsonBaseLayer, defaultTimeToCache);
    }
    else if (layerType.compareTo("WMS") == 0)
    {
      return parseWMSLayer(jsonBaseLayer);
    }
    else
    {
      ILogger.instance().logError("Unsupported layer type \"%s\"", layerType);
      return null;
    }
  }

  private MapQuestLayer parseMapQuestLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String imagery = jsonBaseLayer.getAsString("imagery", "<imagery not present>");
    if (imagery.compareTo("OpenAerial") == 0)
    {
      return MapQuestLayer.newOpenAerial(timeToCache);
    }
  
    // defaults to OSM
    return MapQuestLayer.newOSM(timeToCache);
  }

  private BingMapsLayer parseBingMapsLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String key = jsonBaseLayer.getAsString("key", "");
    final String imagerySet = jsonBaseLayer.getAsString("imagerySet", "Aerial");
  
    return new BingMapsLayer(imagerySet, key, timeToCache);
  }

  private CartoDBLayer parseCartoDBLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String userName = jsonBaseLayer.getAsString("userName", "");
    final String table = jsonBaseLayer.getAsString("table", "");
  
    return new CartoDBLayer(userName, table, timeToCache);
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BingMapsLayer parseBingMapsLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache);

  private MapBoxLayer parseMapBoxLayer(JSONObject jsonBaseLayer, TimeInterval timeToCache)
  {
    final String mapKey = jsonBaseLayer.getAsString("mapKey", "");
  
    return new MapBoxLayer(mapKey, timeToCache);
  }

  private WMSLayer parseWMSLayer(JSONObject jsonBaseLayer)
  {
  
    final String mapLayer = jsonBaseLayer.getAsString("layerName", "");
    final URL mapServerURL = new URL(jsonBaseLayer.getAsString("server", ""), false);
    final String versionStr = jsonBaseLayer.getAsString("version", "");
    WMSServerVersion mapServerVersion = WMSServerVersion.WMS_1_1_0;
    if (versionStr.compareTo("WMS_1_3_0") == 0)
    {
      mapServerVersion = WMSServerVersion.WMS_1_3_0;
    }
    final String queryLayer = jsonBaseLayer.getAsString("queryLayer", "");
    final String style = jsonBaseLayer.getAsString("style", "");
    final URL queryServerURL = new URL("", false);
    final WMSServerVersion queryServerVersion = mapServerVersion;
    final double lowerLat = jsonBaseLayer.getAsNumber("lowerLat", -90.0);
    final double lowerLon = jsonBaseLayer.getAsNumber("lowerLon", -180.0);
    final double upperLat = jsonBaseLayer.getAsNumber("upperLat", 90.0);
    final double upperLon = jsonBaseLayer.getAsNumber("upperLon", 180.0);
    final Sector sector = new Sector(new Geodetic2D(Angle.fromDegrees(lowerLat), Angle.fromDegrees(lowerLon)), new Geodetic2D(Angle.fromDegrees(upperLat), Angle.fromDegrees(upperLon)));
    String imageFormat = jsonBaseLayer.getAsString("imageFormat", "image/png");
    if (imageFormat.compareTo("JPG") == 0)
    {
      imageFormat = "image/jpeg";
    }
    final String srs = jsonBaseLayer.getAsString("projection", "EPSG_4326");
    LayerTilesRenderParameters layerTilesRenderParameters = null;
    if (srs.compareTo("EPSG_4326") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultNonMercator(Sector.fullSphere());
    }
    else if (srs.compareTo("EPSG_900913") == 0)
    {
      layerTilesRenderParameters = LayerTilesRenderParameters.createDefaultMercator(0, 17);
    }
    final boolean isTransparent = jsonBaseLayer.getAsBoolean("transparent", false);
    final double expiration = jsonBaseLayer.getAsNumber("expiration", 0);
    final long milliseconds = IMathUtils.instance().round(expiration);
    final TimeInterval timeToCache = TimeInterval.fromMilliseconds(milliseconds);
    final boolean readExpired = jsonBaseLayer.getAsBoolean("acceptExpiration", false);
  
    return new WMSLayer(mapLayer, mapServerURL, mapServerVersion, queryLayer, queryServerURL, queryServerVersion, sector, imageFormat, (srs.compareTo("EPSG_4326") == 0) ? "EPSG:4326" : "EPSG:900913", style, isTransparent, null, timeToCache, readExpired, layerTilesRenderParameters);
  }


  private int getApplicationCurrentSceneIndex()
  {
    if (_applicationCurrentSceneIndex < 0)
    {
      _applicationCurrentSceneIndex = _applicationDefaultSceneIndex;
    }
    return _applicationCurrentSceneIndex;
  }
  private MapBoo_Scene getApplicationCurrentScene()
  {
    final int currentSceneIndex = getApplicationCurrentSceneIndex();
    final int applicationScenesSize = _applicationScenes.size();
    if ((applicationScenesSize == 0) || (currentSceneIndex < 0) || (currentSceneIndex >= applicationScenesSize))
    {
      return null;
    }
  
    return _applicationScenes.get(currentSceneIndex);
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
  
    String name = jsonObject.getAsString("name", "");
    String description = jsonObject.getAsString("description", "");
    String icon = jsonObject.getAsString("icon", "");
    Color backgroundColor = parseColor(jsonObject.getAsString("bgColor"));
    Layer baseLayer = parseLayer(jsonObject.get("baseLayer"));
    Layer overlayLayer = parseLayer(jsonObject.get("overlayLayer"));
  
    return new MapBoo_Scene(name, description, icon, backgroundColor, baseLayer, overlayLayer);
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

  protected MapBooBuilder(URL serverURL, URL tubesURL, boolean useWebSockets, String applicationId, MapBooApplicationChangeListener applicationListener)
  //_applicationTubeWebSocket(NULL),
  {
     _serverURL = serverURL;
     _tubesURL = tubesURL;
     _useWebSockets = useWebSockets;
     _applicationId = applicationId;
     _applicationName = "";
     _applicationDescription = "";
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
     _applicationDefaultSceneIndex = 0;
  
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
  
    GInitializationTask initializationTask = createInitializationTask();
  
    java.util.ArrayList<PeriodicalTask> periodicalTasks = createPeriodicalTasks();
  
    ICameraActivityListener cameraActivityListener = null;
  
    _g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), cameraActivityListener, createPlanet(), cameraConstraints, createCameraRenderer(), mainRenderer, createBusyRenderer(), Color.black(), false, false, initializationTask, true, periodicalTasks, getGPUProgramManager()); // autoDeleteInitializationTask -  logDownloaderStatistics -  logFPS
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

  /** Private to G3M, don't call it */
  public final int getApplicationTimestamp()
  {
    return _applicationTimestamp;
  }

  /** Private to G3M, don't call it */
  public final void setApplicationTimestamp(int timestamp)
  {
    _applicationTimestamp = timestamp;
  }

  /** Private to G3M, don't call it */
  public final void setApplicationName(String name)
  {
    if (_applicationName.compareTo(name) != 0)
    {
      _applicationName = name;
  
      if (_applicationListener != null)
      {
        _applicationListener.onNameChanged(_applicationName);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void setApplicationDescription(String description)
  {
    if (_applicationDescription.compareTo(description) != 0)
    {
      _applicationDescription = description;
  
      if (_applicationListener != null)
      {
        _applicationListener.onDescriptionChanged(_applicationDescription);
      }
    }
  }

  /** Private to G3M, don't call it */
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
  
    recreateLayerSet();
  
    if (_g3mWidget != null)
    {
      _g3mWidget.setBackgroundColor(getCurrentBackgroundColor());
  
  //    // force inmediate ejecution of PeriodicalTasks
  //    _g3mWidget->resetPeriodicalTasksTimeouts();
    }
  
    if (_applicationListener != null)
    {
      _applicationListener.onScenesChanged(_applicationScenes);
    }
  }

  /** Private to G3M, don't call it */
  public final URL createPollingApplicationDescriptionURL()
  {
    final String tubesPath = _serverURL.getPath();
  
    return new URL(tubesPath + "/application/" + _applicationId + "/runtime", false);
  }

  /** Private to G3M, don't call it */
  public final URL createApplicationTubeURL()
  {
    final String tubesPath = _tubesURL.getPath();
  
    return new URL(tubesPath + "/application/" + _applicationId + "/runtime", false);
  }

//  /** Private to G3M, don't call it */
//  void rawChangeApplication(const std::string& applicationId);

  /** Private to G3M, don't call it */
  public final void parseApplicationDescription(String json, URL url)
  {
    final JSONBaseObject jsonBaseObject = IJSONParser.instance().parse(json, true);
  
    if (jsonBaseObject == null)
    {
      ILogger.instance().logError("Can't parse SceneJSON from %s", url.getPath());
    }
    else
    {
      final JSONObject jsonObject = jsonBaseObject.asObject();
      if (jsonObject == null)
      {
        ILogger.instance().logError("Invalid SceneJSON (1)");
      }
      else
      {
        final JSONString error = jsonObject.getAsString("error");
        if (error == null)
        {
          final int timestamp = (int) jsonObject.getAsNumber("timestamp", 0);
  
          if (getApplicationTimestamp() != timestamp)
          {
            final JSONString jsonName = jsonObject.getAsString("name");
            if (jsonName != null)
            {
              setApplicationName(jsonName.value());
            }
  
            final JSONString jsonDescription = jsonObject.getAsString("description");
            if (jsonDescription != null)
            {
              setApplicationDescription(jsonDescription.value());
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
  
  //          const JSONNumber* jsonDefaultScene = jsonObject->getAsNumber("defaultScene");
  //          if (jsonDefaultScene != NULL) {
  //            const int defaultScene = (int) jsonDefaultScene->value();
  //            setApplication
  //          }
  
            int _TODO_Application_Warnings;
  
            setApplicationTimestamp(timestamp);
          }
        }
        else
        {
          ILogger.instance().logError("Server Error: %s", error.value());
        }
      }
  
      if (jsonBaseObject != null)
         jsonBaseObject.dispose();
    }
  
  }

  /** Private to G3M, don't call it */
  public final void openApplicationTube(G3MContext context)
  {
    final boolean autodeleteListener = true;
    final boolean autodeleteWebSocket = true;
  
  //  _applicationTubeWebSocket = context->getFactory()->createWebSocket(createApplicationTubeURL(),
  //                                                                     new MapBooBuilder_ApplicationTubeListener(this),
  //                                                                     autodeleteListener,
  //                                                                     autodeleteWebSocket);
  
    context.getFactory().createWebSocket(createApplicationTubeURL(), new MapBooBuilder_ApplicationTubeListener(this), autodeleteListener, autodeleteWebSocket);
  }


  //class MapBooBuilder_ChangeSceneIdTask : public GTask {
  //private:
  //  MapBooBuilder*    _builder;
  //  const std::string _applicationId;
  //
  //public:
  //  MapBooBuilder_ChangeSceneIdTask(MapBooBuilder* builder,
  //                                  const std::string& applicationId) :
  //  _builder(builder),
  //  _applicationId(applicationId)
  //  {
  //  }
  //
  //  void run(const G3MContext* context) {
  //    _builder->rawChangeApplication(_applicationId);
  //  }
  //};
  //
  //void MapBooBuilder::changeApplication(const std::string& applicationId) {
  //  if (applicationId.compare(_applicationId) != 0) {
  //    getThreadUtils()->invokeInRendererThread(new MapBooBuilder_ChangeSceneIdTask(this, applicationId),
  //                                             true);
  //  }
  //}
  
  //void MapBooBuilder::resetApplication(const std::string& applicationId) {
  //  _applicationId = applicationId;
  //
  //  _applicationTimestamp = -1;
  //
  ////  delete _sceneBaseLayer;
  ////  _sceneBaseLayer = NULL;
  ////
  ////  delete _sceneOverlayLayer;
  ////  _sceneOverlayLayer = NULL;
  //
  ////  _sceneUser = "";
  //
  //  _applicationName = "";
  //
  //  _applicationDescription = "";
  //
  ////  delete _sceneBackgroundColor;
  ////  _sceneBackgroundColor = Color::newFromRGBA(0, 0, 0, 1);
  //}
  
  //void MapBooBuilder::resetG3MWidget() {
  //  _layerSet->removeAllLayers(false);
  //
  //  if (_g3mWidget != NULL) {
  //    _g3mWidget->setBackgroundColor(*_sceneBackgroundColor);
  //
  //    // force inmediate ejecution of PeriodicalTasks
  //    _g3mWidget->resetPeriodicalTasksTimeouts();
  //  }
  //}
  
  public final void setApplicationTubeOpened(boolean open)
  {
    if (_isApplicationTubeOpen != open)
    {
      _isApplicationTubeOpen = open;
  //    if (!_isApplicationTubeOpen) {
  //      _applicationTubeWebSocket = NULL;
  //    }
    }
  }

  public final boolean isApplicationTubeOpen()
  {
    return _isApplicationTubeOpen;
  }

//  void changeApplication(const std::string& applicationId);
}