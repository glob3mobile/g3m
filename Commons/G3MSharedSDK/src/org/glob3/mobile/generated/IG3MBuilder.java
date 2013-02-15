package org.glob3.mobile.generated; 
//
//  IG3MBuilder.cpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//

//
//  IG3MBuilder.hpp
//  G3MiOSSDK
//
//  Created by Mari Luz Mateo on 20/11/12.
//
//


//class TileRendererBuilder;


public abstract class IG3MBuilder
{

  private GL _gl;
  private IDownloader _downloader;
  private IThreadUtils _threadUtils;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private java.util.ArrayList<ICameraConstrainer> _cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
  private CameraRenderer _cameraRenderer;
  private Color _backgroundColor;
  private TileRendererBuilder _tileRendererBuilder;
  private Renderer _busyRenderer;
  private java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();
  private GInitializationTask _initializationTask;
  private boolean _autoDeleteInitializationTask;
  private java.util.ArrayList<PeriodicalTask> _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
  private boolean _logFPS;
  private boolean _logDownloaderStatistics;
  private WidgetUserData _userData;


  /**
   * Returns the _gl.
   * Lazy initialization.
   *
   * @return _gl: GL*
   */
  private GL getGL()
  {
    if (_gl == null)
    {
      ILogger.instance().logError("Logic Error: _gl not initialized");
    }
  
    return _gl;
  }

  /**
   * Returns the _downloader.
   * Lazy initialization.
   *
   * @return _downloader: IDownloader*
   */
  private IDownloader getDownloader()
  {
    if (_downloader == null)
    {
      _downloader = createDownloader();
    }
  
    return _downloader;
  }

  /**
   * Returns the _threadUtils.
   * Lazy initialization.
   *
   * @return _threadUtils: IThreadUtils*
   */
  private IThreadUtils getThreadUtils()
  {
    if (_threadUtils == null)
    {
      _threadUtils = createThreadUtils();
    }
  
    return _threadUtils;
  }

  /**
   * Returns the _cameraConstraints.
   * Lazy initialization.
   *
   * @return _cameraConstraints: std::vector<ICameraConstrainer*>
   */
  private java.util.ArrayList<ICameraConstrainer> getCameraConstraints()
  {
    if (_cameraConstraints.size() == 0)
    {
      _cameraConstraints = createCameraConstraints();
    }
  
    return _cameraConstraints;
  }

  /**
   * Returns the _cameraRenderer.
   * Lazy initialization.
   *
   * @return _cameraRenderer: CameraRenderer*
   */
  private CameraRenderer getCameraRenderer()
  {
    if (_cameraRenderer == null)
    {
      _cameraRenderer = createCameraRenderer();
    }
  
    return _cameraRenderer;
  }

  /**
   * Returns the _busyRenderer.
   * Lazy initialization.
   *
   * @return _busyRenderer: Renderer*
   */
  private Renderer getBusyRenderer()
  {
    if (_busyRenderer == null)
    {
      _busyRenderer = new BusyMeshRenderer();
    }
  
    return _busyRenderer;
  }

  /**
   * Returns the _backgroundColor.
   * Lazy initialization.
   *
   * @return _backgroundColor: Color*
   */
  private Color getBackgroundColor()
  {
    if (_backgroundColor == null)
    {
      _backgroundColor = Color.newFromRGBA((float)0, (float)0.1, (float)0.2, (float)1);
    }
  
    return _backgroundColor;
  }

  /**
   * Returns the array of renderers.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _renderers: std::vector<Renderer*>
   */
  private java.util.ArrayList<Renderer> getRenderers()
  {
    return _renderers;
  }

  /**
   * Returns the value of _logFPS flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _logFPS: bool
   */
  private boolean getLogFPS()
  {
    return _logFPS;
  }

  /**
   * Returns the value of _logDownloaderStatistics flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _logDownloaderStatistics: bool
   */
  private boolean getLogDownloaderStatistics()
  {
    return _logDownloaderStatistics;
  }

  /**
   * Returns the initialization task.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _logDownloaderStatistics: GInitializationTask*
   */
  private GInitializationTask getInitializationTask()
  {
    return _initializationTask;
  }

  /**
   * Returns the value of _autoDeleteInitializationTask flag.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _autoDeleteInitializationTask: bool
   */
  private boolean getAutoDeleteInitializationTask()
  {
    return _autoDeleteInitializationTask;
  }

  /**
   * Returns the array of periodical tasks.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _periodicalTasks: std::vector<PeriodicalTask*>
   */
  private java.util.ArrayList<PeriodicalTask> getPeriodicalTasks()
  {
    return _periodicalTasks;
  }

  /**
   * Returns the user data.
   * Method created to keep convention. It is not needed as it does not have to create a default value.
   *
   * @return _userData: WidgetUserData*
   */
  private WidgetUserData getUserData()
  {
    return _userData;
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

  private void pvtSetInitializationTask(GInitializationTask initializationTask, boolean autoDeleteInitializationTask)
  {
    if (_initializationTask != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _initializationTask already initialized");
      return;
    }
    _initializationTask = initializationTask;
    _autoDeleteInitializationTask = autoDeleteInitializationTask;
  }

  protected IStorage _storage;


  /**
   * Returns the _storage.
   * Lazy initialization.
   *
   * @return _storage: IStorage*
   */
  protected final IStorage getStorage()
  {
    if (_storage == null)
    {
      _storage = createStorage();
    }
  
    return _storage;
  }

  protected final G3MWidget create()
  {
    Renderer mainRenderer = null;
    TileRenderer tileRenderer = getTileRendererBuilder().create();
    if (getRenderers().size() > 0)
    {
      mainRenderer = new CompositeRenderer();
      ((CompositeRenderer) mainRenderer).addRenderer(tileRenderer);
  
      for (int i = 0; i < getRenderers().size(); i++)
      {
        ((CompositeRenderer) mainRenderer).addRenderer(getRenderers().get(i));
      }
    }
    else
    {
      mainRenderer = tileRenderer;
    }
  
    Color backgroundColor = Color.fromRGBA(getBackgroundColor().getRed(), getBackgroundColor().getGreen(), getBackgroundColor().getBlue(), getBackgroundColor().getAlpha());
  
    G3MWidget g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), getPlanet(), getCameraConstraints(), getCameraRenderer(), mainRenderer, getBusyRenderer(), backgroundColor, getLogFPS(), getLogDownloaderStatistics(), getInitializationTask(), getAutoDeleteInitializationTask(), getPeriodicalTasks());
  
    g3mWidget.setUserData(getUserData());
  
    _gl = null;
    _storage = null;
    _downloader = null;
    _threadUtils = null;
    _planet = null;
    for (int i = 0; i < _cameraConstraints.size(); i++)
    {
      _cameraConstraints.set(i, null);
    }
    _cameraRenderer = null;
    for (int i = 0; i < _renderers.size(); i++)
    {
      _renderers.set(i, null);
    }
    _busyRenderer = null;
    _backgroundColor = null;
    _initializationTask = null;
    for (int i = 0; i < _periodicalTasks.size(); i++)
    {
      _periodicalTasks.set(i, null);
    }
    _userData = null;
    _tileRendererBuilder = null;
  
    return g3mWidget;
  }

  protected abstract IThreadUtils createThreadUtils();
  protected abstract IStorage createStorage();
  protected abstract IDownloader createDownloader();

  public IG3MBuilder()
  {
     _gl = null;
     _storage = null;
     _downloader = null;
     _threadUtils = null;
     _planet = null;
     _cameraRenderer = null;
     _backgroundColor = null;
     _tileRendererBuilder = null;
     _busyRenderer = null;
     _initializationTask = null;
     _autoDeleteInitializationTask = true;
     _logFPS = false;
     _logDownloaderStatistics = false;
     _userData = null;
  }
  public void dispose()
  {
    if (_gl != null)
       _gl.dispose();
    if (_storage != null)
       _storage.dispose();
    if (_downloader != null)
       _downloader.dispose();
    if (_threadUtils != null)
       _threadUtils.dispose();
    if (_planet != null)
       _planet.dispose();
    for (int i = 0; i < _cameraConstraints.size(); i++)
    {
      if (_cameraConstraints.get(i) != null)
         _cameraConstraints.get(i).dispose();
    }
    if (_cameraRenderer != null)
       _cameraRenderer.dispose();
    for (int i = 0; i < _renderers.size(); i++)
    {
      if (_renderers.get(i) != null)
         _renderers.get(i).dispose();
    }
    if (_busyRenderer != null)
       _busyRenderer.dispose();
    if (_backgroundColor != null)
       _backgroundColor.dispose();
    if (_initializationTask != null)
       _initializationTask.dispose();
    for (int i = 0; i < _periodicalTasks.size(); i++)
    {
      if (_periodicalTasks.get(i) != null)
         _periodicalTasks.get(i).dispose();
    }
    if (_userData != null)
       _userData.dispose();
    if (_tileRendererBuilder != null)
       _tileRendererBuilder.dispose();
  }
  public final void setGL(GL gl)
  {
    if (_gl != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _gl already initialized");
      return;
    }
    _gl = gl;
  }
  public final void setStorage(IStorage storage)
  {
    if (_storage != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _storage already initialized");
      return;
    }
    _storage = storage;
  }
  public final void setDownloader(IDownloader downloader)
  {
    if (_downloader != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _downloader already initialized");
      return;
    }
    _downloader = downloader;
  }
  public final void setThreadUtils(IThreadUtils threadUtils)
  {
    if (_threadUtils != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _threadUtils already initialized");
      return;
    }
    _threadUtils = threadUtils;
  }
  public final void setPlanet(Planet planet)
  {
    if (_planet != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _planet already initialized");
      return;
    }
    _planet = planet;
  }
  public final void addCameraConstraint(ICameraConstrainer cameraConstraint)
  {
    _cameraConstraints.add(cameraConstraint);
  }
  public final void setCameraRenderer(CameraRenderer cameraRenderer)
  {
    if (_cameraRenderer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _cameraRenderer already initialized");
      return;
    }
    _cameraRenderer = cameraRenderer;
  }
  public final void setBackgroundColor(Color backgroundColor)
  {
    if (_backgroundColor != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _backgroundColor already initialized");
      return;
    }
    _backgroundColor = backgroundColor;
  }
  public final void setBusyRenderer(Renderer busyRenderer)
  {
    if (_busyRenderer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _busyRenderer already initialized");
      return;
    }
    _busyRenderer = busyRenderer;
  }
  public final void addRenderer(Renderer renderer)
  {
    if (!renderer.isTileRenderer())
    {
      _renderers.add(renderer);
    }
    else
    {
      ILogger.instance().logError("LOGIC ERROR: a new TileRenderer is not expected to be added");
    }
  }
  public final void addPeriodicalTask(PeriodicalTask periodicalTask)
  {
    _periodicalTasks.add(periodicalTask);
  }
  public final void setLogFPS(boolean logFPS)
  {
    _logFPS = logFPS;
  }
  public final void setLogDownloaderStatistics(boolean logDownloaderStatistics)
  {
    _logDownloaderStatistics = logDownloaderStatistics;
  }
  public final void setUserData(WidgetUserData userData)
  {
    if (_userData != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _userData already initialized");
      return;
    }
    _userData = userData;
  }
  public final void setInitializationTask(GInitializationTask initializationTask) {
    pvtSetInitializationTask(initializationTask,
                             true // parameter ignored in Java code 
);
  }


  /**
   * Returns the _planet.
   * Lazy initialization.
   *
   * @return _planet: const Planet*
   */
  public final Planet getPlanet()
  {
    if (_planet == null)
    {
      _planet = Planet.createEarth();
    }
    return _planet;
  }

  /**
   * Returns the _tileRendererBuilder.
   * Lazy initialization.
   *
   * @return _tileRendererBuilder: TileRendererBuilder*
   */
  public final TileRendererBuilder getTileRendererBuilder()
  {
    if (_tileRendererBuilder == null)
    {
      _tileRendererBuilder = new TileRendererBuilder();
    }
  
    return _tileRendererBuilder;
  }
}