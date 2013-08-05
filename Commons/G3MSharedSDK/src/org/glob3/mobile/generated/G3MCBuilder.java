package org.glob3.mobile.generated; 
public abstract class G3MCBuilder
{

  private final URL _serverURL;
  private final URL _tubesURL;

  private final boolean _useWebSockets;

  private G3MCSceneChangeListener _sceneListener;

  private String _sceneId;
  private int _sceneTimestamp;
  private Layer _sceneBaseLayer;
  private Layer _sceneOverlayLayer;
  private String _sceneUser;
  private String _sceneName;
  private String _sceneDescription;
  private Color _sceneBackgroundColor;


  private GL _gl;
  private G3MWidget _g3mWidget;
  private IStorage _storage;

  private IWebSocket _sceneTubeWebSocket;
  private boolean _isSceneTubeOpen;

  private LayerSet _layerSet;
  private TileRenderer createTileRenderer()
  {
    final TileTessellator tessellator = new EllipsoidalTileTessellator(true);
  
    ElevationDataProvider elevationDataProvider = null;
    final float verticalExaggeration = 1F;
    TileTexturizer texturizer = new MultiLayerTileTexturizer();
  
    final boolean renderDebug = false;
    final boolean useTilesSplitBudget = true;
    final boolean forceFirstLevelTilesRenderOnStart = true;
    final boolean incrementalTileQuality = false;
    final boolean renderIncompletePlanet = false;
    final URL incompletePlanetTexureURL = new URL("", false);
  
    final TilesRenderParameters parameters = new TilesRenderParameters(renderDebug, useTilesSplitBudget, forceFirstLevelTilesRenderOnStart, incrementalTileQuality, renderIncompletePlanet, incompletePlanetTexureURL);
  
    final boolean showStatistics = false;
    long texturePriority = DownloadPriority.HIGHER;
  
    return new TileRenderer(tessellator, elevationDataProvider, verticalExaggeration, texturizer, _layerSet, parameters, showStatistics, texturePriority);
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
      periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(2), new G3MCBuilder_TubeWatchdogPeriodicalTask(this)));
    }
    else
    {
      periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(2), new G3MCBuilder_PollingScenePeriodicalTask(this)));
    }
  
    return periodicalTasks;
  }

  private URL createScenesDescriptionsURL()
  {
    final String serverPath = _serverURL.getPath();
  
    return new URL(serverPath + "/scenes/", false);
  }

  private void recreateLayerSet()
  {
    _layerSet.removeAllLayers(false);
  
    if (_sceneBaseLayer != null)
    {
      _layerSet.addLayer(_sceneBaseLayer);
    }
  
    if (_sceneOverlayLayer != null)
    {
      _layerSet.addLayer(_sceneOverlayLayer);
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

  private void resetScene(String sceneId)
  {
    _sceneId = sceneId;
  
    _sceneTimestamp = -1;
  
    if (_sceneBaseLayer != null)
       _sceneBaseLayer.dispose();
    _sceneBaseLayer = null;
  
    if (_sceneOverlayLayer != null)
       _sceneOverlayLayer.dispose();
    _sceneOverlayLayer = null;
  
    _sceneUser = "";
  
    _sceneName = "";
  
    _sceneDescription = "";
  
    if (_sceneBackgroundColor != null)
       _sceneBackgroundColor.dispose();
    _sceneBackgroundColor = Color.newFromRGBA(0, 0, 0, 1);
  }

  private void resetG3MWidget()
  {
    _layerSet.removeAllLayers(false);
  
    if (_g3mWidget != null)
    {
      _g3mWidget.setBackgroundColor(_sceneBackgroundColor);
  
      // force inmediate ejecution of PeriodicalTasks
      _g3mWidget.resetPeriodicalTasksTimeouts();
    }
  }

  private GInitializationTask createInitializationTask()
  {
    return _useWebSockets ? new G3MCBuilder_SceneTubeConnector(this) : null;
  }



  private Layer parseLayer(JSONBaseObject jsonBaseObjectLayer)
  {
  
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

  protected G3MCBuilder(URL serverURL, URL tubesURL, boolean useWebSockets, String sceneId, G3MCSceneChangeListener sceneListener)
  {
     _serverURL = serverURL;
     _tubesURL = tubesURL;
     _useWebSockets = useWebSockets;
     _sceneId = sceneId;
     _sceneTimestamp = -1;
     _sceneBaseLayer = null;
     _sceneOverlayLayer = null;
     _sceneUser = "";
     _sceneName = "";
     _sceneDescription = "";
     _sceneBackgroundColor = Color.newFromRGBA(0, 0, 0, 1);
     _gl = null;
     _g3mWidget = null;
     _storage = null;
     _threadUtils = null;
     _layerSet = new LayerSet();
     _downloader = null;
     _sceneListener = sceneListener;
     _isSceneTubeOpen = false;
     _sceneTubeWebSocket = null;
  
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
  
    TileRenderer tileRenderer = createTileRenderer();
    mainRenderer.addRenderer(tileRenderer);
  
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = createCameraConstraints();
  
    GInitializationTask initializationTask = createInitializationTask();
  
    java.util.ArrayList<PeriodicalTask> periodicalTasks = createPeriodicalTasks();
  
    ICameraActivityListener cameraActivityListener = null;
  
    _g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), cameraActivityListener, createPlanet(), cameraConstraints, createCameraRenderer(), mainRenderer, createBusyRenderer(), _sceneBackgroundColor, false, false, initializationTask, true, periodicalTasks); // autoDeleteInitializationTask -  logDownloaderStatistics -  logFPS
  
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

  /** Private to G3M, don't call it */
  public final int getSceneTimestamp()
  {
    return _sceneTimestamp;
  }

  /** Private to G3M, don't call it */
  public final void setSceneTimestamp(int timestamp)
  {
    _sceneTimestamp = timestamp;
  }

  /** Private to G3M, don't call it */
  public final void setSceneBaseLayer(Layer baseLayer)
  {
    if (baseLayer == null)
    {
      ILogger.instance().logError("Base Layer can't be NULL");
      return;
    }
  
    if (_sceneBaseLayer != baseLayer)
    {
      if (_sceneBaseLayer != null)
         _sceneBaseLayer.dispose();
      _sceneBaseLayer = baseLayer;
  
      recreateLayerSet();
  
      if (_sceneListener != null)
      {
        _sceneListener.onBaseLayerChanged(_sceneBaseLayer);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void setSceneOverlayLayer(Layer overlayLayer)
  {
    if (_sceneOverlayLayer != overlayLayer)
    {
      if (_sceneOverlayLayer != null)
         _sceneOverlayLayer.dispose();
      _sceneOverlayLayer = overlayLayer;
  
      recreateLayerSet();
  
      if (_sceneListener != null)
      {
        _sceneListener.onOverlayLayerChanged(_sceneOverlayLayer);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void setSceneUser(String user)
  {
    if (_sceneUser.compareTo(user) != 0)
    {
      _sceneUser = user;
  
      if (_sceneListener != null)
      {
        _sceneListener.onUserChanged(_sceneUser);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void setSceneName(String name)
  {
    if (_sceneName.compareTo(name) != 0)
    {
      _sceneName = name;
  
      if (_sceneListener != null)
      {
        _sceneListener.onNameChanged(_sceneName);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void setSceneDescription(String description)
  {
    if (_sceneDescription.compareTo(description) != 0)
    {
      _sceneDescription = description;
  
      if (_sceneListener != null)
      {
        _sceneListener.onDescriptionChanged(_sceneDescription);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void setSceneBackgroundColor(Color backgroundColor)
  {
    if (!_sceneBackgroundColor.isEqualsTo(backgroundColor))
    {
      if (_sceneBackgroundColor != null)
         _sceneBackgroundColor.dispose();
      _sceneBackgroundColor = new Color(backgroundColor);
  
      if (_g3mWidget != null)
      {
        _g3mWidget.setBackgroundColor(_sceneBackgroundColor);
      }
  
      if (_sceneListener != null)
      {
        _sceneListener.onBackgroundColorChanged(_sceneBackgroundColor);
      }
    }
  }

  /** Private to G3M, don't call it */
  public final URL createPollingSceneDescriptionURL()
  {
    final String serverPath = _serverURL.getPath();
  
    return new URL(serverPath + "/scenes/" + _sceneId, false);
  }

  /** Private to G3M, don't call it */
  public final URL createSceneTubeURL()
  {
    final String tubesPath = _tubesURL.getPath();
  
    return new URL(tubesPath + "/scene/" + _sceneId, false);
  }

  /** Private to G3M, don't call it */
  public final void rawChangeScene(String sceneId)
  {
    if (sceneId.compareTo(_sceneId) != 0)
    {
      resetScene(sceneId);
  
      resetG3MWidget();
  
      if (_sceneListener != null)
      {
        _sceneListener.onSceneChanged(sceneId);
      }
  
      if (_sceneTubeWebSocket != null)
      {
        _sceneTubeWebSocket.close();
      }
    }
  }

  /** Private to G3M, don't call it */
  public final void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener listener)
  {
     requestScenesDescriptions(listener, true);
  }
  public final void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener listener, boolean autoDelete)
  {
    getDownloader().requestBuffer(createScenesDescriptionsURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), true, new G3MCBuilder_ScenesDescriptionsBufferListener(listener, autoDelete), true);
  }

  /** Private to G3M, don't call it */
  public final void parseSceneDescription(String json, URL url)
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
          final int timestamp = (int) jsonObject.getAsNumber("ts", 0);
  
          if (getSceneTimestamp() != timestamp)
          {
            final JSONString jsonUser = jsonObject.getAsString("user");
            if (jsonUser != null)
            {
              setSceneUser(jsonUser.value());
            }
  
            //id
  
            final JSONString jsonName = jsonObject.getAsString("name");
            if (jsonName != null)
            {
              setSceneName(jsonName.value());
            }
  
            final JSONString jsonDescription = jsonObject.getAsString("description");
            if (jsonDescription != null)
            {
              setSceneDescription(jsonDescription.value());
            }
  
            final JSONString jsonBGColor = jsonObject.getAsString("bgColor");
            if (jsonBGColor != null)
            {
              final Color bgColor = Color.parse(jsonBGColor.value());
              if (bgColor == null)
              {
                ILogger.instance().logError("Invalid format in attribute 'bgColor' (%s)", jsonBGColor.value());
              }
              else
              {
                setSceneBackgroundColor(bgColor);
                if (bgColor != null)
                   bgColor.dispose();
              }
            }
  
            final JSONBaseObject jsonBaseLayer = jsonObject.get("baseLayer");
            if (jsonBaseLayer != null)
            {
              setSceneBaseLayer(parseLayer(jsonBaseLayer));
            }
  
            final JSONBaseObject jsonOverlayLayer = jsonObject.get("overlayLayer");
            if (jsonOverlayLayer != null)
            {
              setSceneOverlayLayer(parseLayer(jsonOverlayLayer));
            }
  
            //tags
  
            setSceneTimestamp(timestamp);
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
  public final void openSceneTube(G3MContext context)
  {
    final boolean autodeleteListener = true;
    final boolean autodeleteWebSocket = true;
  
    _sceneTubeWebSocket = context.getFactory().createWebSocket(createSceneTubeURL(), new G3MCBuilder_SceneTubeListener(this), autodeleteListener, autodeleteWebSocket);
  }

  public final void setSceneTubeOpened(boolean open)
  {
    if (_isSceneTubeOpen != open)
    {
      _isSceneTubeOpen = open;
      if (!_isSceneTubeOpen)
      {
        _sceneTubeWebSocket = null;
      }
    }
  }

  public final boolean isSceneTubeOpen()
  {
    return _isSceneTubeOpen;
  }

  public final void changeScene(String sceneId)
  {
    if (sceneId.compareTo(_sceneId) != 0)
    {
      getThreadUtils().invokeInRendererThread(new G3MCBuilder_ChangeSceneIdTask(this, sceneId), true);
    }
  }
}