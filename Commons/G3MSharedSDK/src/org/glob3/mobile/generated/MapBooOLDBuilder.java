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

  private TileLODTester _tileLODTester;
  private TileVisibilityTester _tileVisibilityTester;

  private MapBooOLD_ErrorRenderer _mbErrorRenderer;

  private LayerSet _layerSet;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  PlanetRenderer createPlanetRenderer();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  java.util.ArrayList<ICameraConstrainer> createCameraConstraints(Planet planet, PlanetRenderer planetRenderer);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  CameraRenderer createCameraRenderer();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ProtoRenderer createBusyRenderer();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ErrorRenderer createErrorRenderer();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  java.util.ArrayList<PeriodicalTask> createPeriodicalTasks();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void recreateLayerSet();

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


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Layer parseLayer(JSONBaseObject jsonBaseObjectLayer);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapQuestLayer parseMapQuestLayer(JSONObject jsonLayer, TimeInterval timeToCache);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BingMapsLayer parseBingMapsLayer(JSONObject jsonLayer, TimeInterval timeToCache);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  CartoDBLayer parseCartoDBLayer(JSONObject jsonLayer, boolean transparent, TimeInterval timeToCache);


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBoxLayer parseMapBoxLayer(JSONObject jsonLayer, TimeInterval timeToCache);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  WMSLayer parseWMSLayer(JSONObject jsonLayer, boolean transparent);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URLTemplateLayer parseURLTemplateLayer(JSONObject jsonLayer, boolean transparent);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  String getApplicationCurrentSceneId();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_Scene getApplicationCurrentScene();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Color getCurrentBackgroundColor();

//  const std::string parseSceneId(const JSONObject* jsonObject) const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_Scene parseScene(JSONObject json);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Color parseColor(JSONString jsonColor);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Sector parseSector(JSONBaseObject jsonBaseObjectLayer);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_MultiImage parseMultiImage(JSONObject jsonObject);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_MultiImage_Level parseMultiImageLevel(JSONObject jsonObject);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_CameraPosition parseCameraPosition(JSONObject jsonObject);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void changedCurrentScene();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void updateVisibleScene(boolean cameraPositionChanged);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  String getApplicationCurrentSceneCommand();

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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_Notification parseNotification(JSONObject jsonNotification);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  java.util.ArrayList<MapBooOLD_Notification> parseNotifications(JSONArray jsonArray);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void addApplicationNotification(MapBooOLD_Notification notification);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void addApplicationNotifications(java.util.ArrayList<MapBooOLD_Notification> notifications);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL parseURL(JSONString jsonString);

  private MarksRenderer _marksRenderer;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MarksRenderer getMarksRenderer();

  private boolean _hasParsedApplication;

  private boolean _initialParse;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void fireOnScenesChanged();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setCameraPosition(MapBooOLD_CameraPosition cameraPosition, boolean animated);
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setCameraPosition(MapBooOLD_CameraPosition cameraPosition);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  String getViewAsString();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL createApplicationCurrentSceneURL();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL createGetFeatureInfoRestURL(Tile tile, Vector2I size, Vector2I pixel, Geodetic3D position);
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
     _tileLODTester = null;
     _tileVisibilityTester = null;
    _featureInfoDownloadListener = new FeatureInfoDownloadListener(_applicationListener);
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  public void dispose()

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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  G3MWidget create();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Planet createPlanet();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  IStorage getStorage();

  protected abstract IStorage createStorage();

  protected abstract IDownloader createDownloader();

  protected abstract IThreadUtils createThreadUtils();

  protected abstract GPUProgramManager createGPUProgramManager();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  SceneLighting createSceneLighting();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL createApplicationPollURL();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Sector parseSector(JSONObject jsonObject, String paramName);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TileLODTester createDefaultTileLODTester();
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TileVisibilityTester createDefaultTileVisibilityTester();

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  int getApplicationEventId();

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationEventId(int eventId);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  int getApplicationTimestamp();

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationTimestamp(int timestamp);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationName(String name);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationWebsite(String website);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationEMail(String eMail);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationAbout(String about);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void addApplicationScene(MapBooOLD_Scene scene, int position);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void deleteApplicationScene(String sceneId);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationScenes(java.util.ArrayList<MapBooOLD_Scene> applicationScenes);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void saveApplicationData();

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  URL createApplicationTubeURL();

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void parseApplicationJSON(String json, URL url);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void parseApplicationJSON(JSONObject jsonBaseObjectLayer, URL url);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void parseApplicationEventsJSON(String json, URL url);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void parseSceneEventAndUpdateScene(JSONObject jsonObject);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void openApplicationTube(G3MContext context);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationCurrentSceneId(String currentSceneId);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void rawChangeScene(String sceneId);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setContext(G3MContext context);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setApplicationTubeOpened(boolean open);

  /** Private to MapbooBuilder, don't call it */
  public final boolean isApplicationTubeOpen()
  {
    return _isApplicationTubeOpen;
  }

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  boolean onTerrainTouch(G3MEventContext ec, Vector2F pixel, Camera camera, Geodetic3D position, Tile tile);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setHasParsedApplication();

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  boolean hasParsedApplication();


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MapBooOLD_Notification createNotification(Geodetic2D position, Camera camera, String message, URL iconURL);

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

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void changeScene(String sceneId);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void changeScene(MapBooOLD_Scene scene);


  public final boolean isQueryableCurrentScene()
  {
    return getApplicationCurrentScene().isQueryable();
  }

  public final URL getServerURL()
  {
    return _serverURL;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void requestGetFeatureInfo(Tile tile, Vector2I size, Vector2I pixel, Geodetic3D position);

  /** Private to MapbooBuilder, don't call it */
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void pollApplicationDataFromServer(G3MContext context);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  String getApplicationId();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void setTileLODTester(TileLODTester tlt);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TileLODTester getTileLODTester();

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  TileVisibilityTester getTileVisibilityTester();

}