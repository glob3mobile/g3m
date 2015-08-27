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


//class GL;
//class IStorage;
//class IDownloader;
//class IThreadUtils;
//class ICameraActivityListener;
//class CameraRenderer;
//class ICameraConstrainer;
//class Color;
//class GInitializationTask;
//class PeriodicalTask;
//class G3MWidget;
//class PlanetRendererBuilder;
//class Planet;
//class Renderer;
//class ProtoRenderer;
//class WidgetUserData;
//class GPUProgramSources;
//class GPUProgramManager;
//class SceneLighting;
//class Sector;
//class GEORenderer;
//class GEOSymbolizer;
//class MeshRenderer;
//class ShapesRenderer;
//class MarksRenderer;
//class ErrorRenderer;
//class InfoDisplay;


public abstract class IG3MBuilder
{
  private GL _gl;
  private IDownloader _downloader;
  private IThreadUtils _threadUtils;
  private ICameraActivityListener _cameraActivityListener;
  private Planet _planet;
  private java.util.ArrayList<ICameraConstrainer> _cameraConstraints;
  private CameraRenderer _cameraRenderer;
  private Color _backgroundColor;
  private PlanetRendererBuilder _planetRendererBuilder;
  private ProtoRenderer _busyRenderer;
  private ErrorRenderer _errorRenderer;
  private Renderer _hudRenderer;
  private java.util.ArrayList<Renderer> _renderers;
  private GInitializationTask _initializationTask;
  private boolean _autoDeleteInitializationTask;
  private java.util.ArrayList<PeriodicalTask> _periodicalTasks;
  private boolean _logFPS;
  private boolean _logDownloaderStatistics;
  private WidgetUserData _userData;
  private java.util.ArrayList<GPUProgramSources> _sources = new java.util.ArrayList<GPUProgramSources>();
  private SceneLighting _sceneLighting;
  private Sector _shownSector;
  private InfoDisplay _infoDisplay;


  /**
   * Returns the _gl.
   *
   * @return _gl: GL*
   */
  private GL getGL()
  {
    if (_gl == null)
    {
      ILogger.instance().logError("LOGIC ERROR: gl not initialized");
    }
  
    return _gl;
  }

  /**
   * Returns the _cameraActivityListener. If it does not exist, it will be default initializated.
   *
   * @return _threadUtils: IThreadUtils*
   */
  private ICameraActivityListener getCameraActivityListener()
  {
    return _cameraActivityListener;
  }

  /**
   * Returns the _cameraConstraints list. If it does not exist, it will be default initializated.
   * @see IG3MBuilder#createDefaultCameraConstraints()
   *
   * @return _cameraConstraints: std::vector<ICameraConstrainer*>
   */
  private java.util.ArrayList<ICameraConstrainer> getCameraConstraints()
  {
    if (_cameraConstraints == null)
    {
      _cameraConstraints = createDefaultCameraConstraints();
    }
  
    return _cameraConstraints;
  }

  /**
   * Returns the _cameraRenderer. If it does not exist, it will be default initializated.
   * @see IG3MBuilder#createDefaultCameraRenderer()
   *
   * @return _cameraRenderer: CameraRenderer*
   */
  private CameraRenderer getCameraRenderer()
  {
    if (_cameraRenderer == null)
    {
      _cameraRenderer = createDefaultCameraRenderer();
    }
  
    return _cameraRenderer;
  }

  /**
   * Returns the _busyRenderer. If it does not exist, it will be default initializated.
   *
   * @return _busyRenderer: Renderer*
   */
  private ProtoRenderer getBusyRenderer()
  {
    if (_busyRenderer == null)
    {
      _busyRenderer = new BusyMeshRenderer(Color.newFromRGBA((float)0, (float)0, (float)0, (float)1));
    }
  
    return _busyRenderer;
  }
  private ErrorRenderer getErrorRenderer()
  {
    if (_errorRenderer == null)
    {
      _errorRenderer = new HUDErrorRenderer();
    }
  
    return _errorRenderer;
  }
  private Renderer getHUDRenderer()
  {
    return _hudRenderer;
  }

  /**
   * Returns the _backgroundColor. If it does not exist, it will be default initializated.
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
   * Returns the renderers list. If it does not exist, it will be default initializated.
   * @see IG3MBuilder#createDefaultRenderers()
   *
   * @return _renderers: std::vector<Renderer*>
   */
  private java.util.ArrayList<Renderer> getRenderers()
  {
    if (_renderers == null)
    {
      _renderers = createDefaultRenderers();
    }
    return _renderers;
  }

  /**
   * Returns the value of _logFPS flag.
   *
   * @return _logFPS: bool
   */
  private boolean getLogFPS()
  {
    return _logFPS;
  }

  /**
   * Returns the value of _logDownloaderStatistics flag.
   *
   * @return _logDownloaderStatistics: bool
   */
  private boolean getLogDownloaderStatistics()
  {
    return _logDownloaderStatistics;
  }

  /**
   * Returns the initialization task.
   *
   * @return _logDownloaderStatistics: GInitializationTask*
   */
  private GInitializationTask getInitializationTask()
  {
    return _initializationTask;
  }

  /**
   * Returns the value of _autoDeleteInitializationTask flag.
   *
   * @return _autoDeleteInitializationTask: bool
   */
  private boolean getAutoDeleteInitializationTask()
  {
    return _autoDeleteInitializationTask;
  }

  /**
   * Returns the array of periodical tasks. If it does not exist, it will be default initializated.
   * @see IG3MBuilder#createDefaultPeriodicalTasks()
   *
   * @return _periodicalTasks: std::vector<PeriodicalTask*>
   */
  private java.util.ArrayList<PeriodicalTask> getPeriodicalTasks()
  {
    if (_periodicalTasks == null)
    {
      _periodicalTasks = createDefaultPeriodicalTasks();
    }
    return _periodicalTasks;
  }

  /**
   * Returns the user data.
   *
   * @return _userData: WidgetUserData*
   */
  private WidgetUserData getUserData()
  {
    return _userData;
  }
  private GPUProgramManager getGPUProgramManager()
  {
    //GPU Program Manager
    GPUProgramFactory gpuProgramFactory = new GPUProgramFactory();
    for(int i = 0; i < _sources.size(); i++)
    {
      gpuProgramFactory.add(_sources.get(i));
    }
    GPUProgramManager gpuProgramManager = new GPUProgramManager(gpuProgramFactory);
    return gpuProgramManager;
  }
  private java.util.ArrayList<ICameraConstrainer> createDefaultCameraConstraints()
  {
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
    SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
    cameraConstraints.add(scc);
  
    return cameraConstraints;
  }
  private CameraRenderer createDefaultCameraRenderer()
  {
    CameraRenderer cameraRenderer = new CameraRenderer();
    final boolean useInertia = true;
    cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
    cameraRenderer.addHandler(new CameraDoubleDragHandler());
    //cameraRenderer->addHandler(new CameraZoomAndRotateHandler());
    cameraRenderer.addHandler(new CameraRotationHandler());
    cameraRenderer.addHandler(new CameraDoubleTapHandler());
  
    return cameraRenderer;
  }
  private java.util.ArrayList<Renderer> createDefaultRenderers()
  {
    java.util.ArrayList<Renderer> renderers = new java.util.ArrayList<Renderer>();
  
    return renderers;
  }
  private java.util.ArrayList<PeriodicalTask> createDefaultPeriodicalTasks()
  {
    java.util.ArrayList<PeriodicalTask> periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
  
    return periodicalTasks;
  }
  private Sector getShownSector()
  {
    if (_shownSector == null)
    {
      return Sector.fullSphere();
    }
    return _shownSector;
  }
  private InfoDisplay getInfoDisplay()
  {
    return _infoDisplay;
  }

  private void pvtSetInitializationTask(GInitializationTask initializationTask, boolean autoDeleteInitializationTask)
  {
    if (_initializationTask != null)
    {
      ILogger.instance().logError("LOGIC ERROR: initializationTask already initialized");
      return;
    }
    if (initializationTask == null)
    {
      ILogger.instance().logError("LOGIC ERROR: initializationTask cannot be NULL");
      return;
    }
    _initializationTask = initializationTask;
    _autoDeleteInitializationTask = autoDeleteInitializationTask;
  }


  /**
   * Returns TRUE if the given renderer list contains, at least, an instance of
   * the PlanetRenderer class. Returns FALSE if not.
   *
   * @return bool
   */
  private boolean containsPlanetRenderer(java.util.ArrayList<Renderer> renderers)
  {
    for (int i = 0; i < renderers.size(); i++)
    {
      if (renderers.get(i).isPlanetRenderer())
      {
        return true;
      }
    }
    return false;
  }


  protected IStorage _storage;


  /**
   * Returns the _storage. If it does not exist, it will be default initializated.
   *
   * @return _storage: IStorage*
   */
  protected final IStorage getStorage()
  {
    if (_storage == null)
    {
      _storage = createDefaultStorage();
    }
  
    return _storage;
  }


  /**
   * Creates the generic widget using all the parameters previously set.
   *
   * @return G3MWidget*
   */
  protected final G3MWidget create()
  {
  
  
    Sector shownSector = getShownSector();
    getPlanetRendererBuilder().setRenderedSector(shownSector); //Shown sector
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning HUDRenderer doesn't work when this code is uncommented
    InfoDisplay infoDisplay = null;
  //  InfoDisplay* infoDisplay = getInfoDisplay();
  //  if (infoDisplay == NULL) {
  //    Default_HUDRenderer* hud = new Default_HUDRenderer();
  //
  //    infoDisplay = new DefaultInfoDisplay(hud);
  //
  //    addRenderer(hud);
  //  }
  
    /*
     * If any renderers were set or added, the main renderer will be a composite renderer.
     *    If the renderers list does not contain a planetRenderer, it will be created and added.
     *    The renderers contained in the list, will be added to the main renderer.
     * If not, the main renderer will be made up of an only renderer (planetRenderer).
     */
    Renderer mainRenderer = null;
    if (getRenderers().size() > 0)
    {
      mainRenderer = new CompositeRenderer();
      if (!containsPlanetRenderer(getRenderers()))
      {
        ((CompositeRenderer) mainRenderer).addRenderer(getPlanetRendererBuilder().create());
      }
      for (int i = 0; i < getRenderers().size(); i++)
      {
        ((CompositeRenderer) mainRenderer).addRenderer(getRenderers().get(i));
      }
    }
    else
    {
      mainRenderer = getPlanetRendererBuilder().create();
    }
  
    final Geodetic3D initialCameraPosition = getPlanet().getDefaultCameraPosition(shownSector);
    addCameraConstraint(new RenderedSectorCameraConstrainer(mainRenderer.getPlanetRenderer(), initialCameraPosition._height * 1.2));
  
    InitialCameraPositionProvider icpp = new SimpleInitialCameraPositionProvider();
  
    G3MWidget g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), getCameraActivityListener(), getPlanet(), getCameraConstraints(), getCameraRenderer(), mainRenderer, getBusyRenderer(), getErrorRenderer(), getHUDRenderer(), getBackgroundColor(), getLogFPS(), getLogDownloaderStatistics(), getInitializationTask(), getAutoDeleteInitializationTask(), getPeriodicalTasks(), getGPUProgramManager(), getSceneLighting(), icpp, infoDisplay);
  
    g3mWidget.setUserData(getUserData());
  
  
    //mainRenderer->getPlanetRenderer()->initializeChangedInfoListener(g3mWidget);
  
    _gl = null;
    _storage = null;
    _downloader = null;
    _threadUtils = null;
    _cameraActivityListener = null;
    _planet = null;
    _cameraConstraints = null;
    _cameraConstraints = null;
    _cameraRenderer = null;
    _renderers = null;
    _renderers = null;
    _busyRenderer = null;
    _errorRenderer = null;
    _hudRenderer = null;
    _initializationTask = null;
    _periodicalTasks = null;
    _periodicalTasks = null;
    _userData = null;
  
    if (_shownSector != null)
       _shownSector.dispose();
    _shownSector = null;
  
    return g3mWidget;
  }

  protected abstract IThreadUtils createDefaultThreadUtils();
  protected abstract IStorage createDefaultStorage();
  protected abstract IDownloader createDefaultDownloader();


  public IG3MBuilder()
  {
     _gl = null;
     _storage = null;
     _downloader = null;
     _threadUtils = null;
     _cameraActivityListener = null;
     _planet = null;
     _cameraConstraints = null;
     _cameraRenderer = null;
     _backgroundColor = null;
     _planetRendererBuilder = null;
     _busyRenderer = null;
     _errorRenderer = null;
     _hudRenderer = null;
     _renderers = null;
     _initializationTask = null;
     _autoDeleteInitializationTask = true;
     _periodicalTasks = null;
     _logFPS = false;
     _logDownloaderStatistics = false;
     _userData = null;
     _sceneLighting = null;
     _shownSector = null;
     _infoDisplay = null;
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
    if (_cameraActivityListener != null)
       _cameraActivityListener.dispose();
    if (_planet != null)
       _planet.dispose();
    if (_cameraConstraints != null)
    {
      for (int i = 0; i < _cameraConstraints.size(); i++)
      {
        if (_cameraConstraints.get(i) != null)
           _cameraConstraints.get(i).dispose();
      }
      _cameraConstraints = null;
    }
    if (_cameraRenderer != null)
       _cameraRenderer.dispose();
    if (_renderers != null)
    {
      for (int i = 0; i < _renderers.size(); i++)
      {
        if (_renderers.get(i) != null)
           _renderers.get(i).dispose();
      }
      _renderers = null;
    }
    if (_busyRenderer != null)
       _busyRenderer.dispose();
    if (_errorRenderer != null)
       _errorRenderer.dispose();
    if (_hudRenderer != null)
       _hudRenderer.dispose();
    if (_backgroundColor != null)
       _backgroundColor.dispose();
    if (_initializationTask != null)
       _initializationTask.dispose();
    if (_periodicalTasks != null)
    {
      for (int i = 0; i < _periodicalTasks.size(); i++)
      {
        if (_periodicalTasks.get(i) != null)
           _periodicalTasks.get(i).dispose();
      }
      _periodicalTasks = null;
    }
    if (_userData != null)
       _userData.dispose();
    if (_planetRendererBuilder != null)
       _planetRendererBuilder.dispose();
    if (_shownSector != null)
       _shownSector.dispose();
  }


  /**
   * Returns the _downloader. If it does not exist, it will be default initializated.
   *
   * @return _downloader: IDownloader*
   */
  public final IDownloader getDownloader()
  {
    if (_downloader == null)
    {
      _downloader = createDefaultDownloader();
    }
  
    return _downloader;
  }

  /**
   * Returns the _threadUtils. If it does not exist, it will be default initializated.
   *
   * @return _threadUtils: IThreadUtils*
   */
  public final IThreadUtils getThreadUtils()
  {
    if (_threadUtils == null)
    {
      _threadUtils = createDefaultThreadUtils();
    }
  
    return _threadUtils;
  }


  /**
   * Sets the _gl.
   *
   * @param gl - cannot be NULL.
   */
  public final void setGL(GL gl)
  {
    if (_gl != null)
    {
      ILogger.instance().logError("LOGIC ERROR: gl already initialized");
      return;
    }
    if (gl == null)
    {
      ILogger.instance().logError("LOGIC ERROR: gl cannot be NULL");
      return;
    }
    _gl = gl;
  }


  /**
   * Sets the _storage.
   *
   * @param storage
   */
  public final void setStorage(IStorage storage)
  {
    if (_storage != null)
    {
      ILogger.instance().logError("LOGIC ERROR: storage already initialized");
      return;
    }
    _storage = storage;
  }


  /**
   * Sets the _downloader
   *
   * @param downloader - cannot be NULL.
   */
  public final void setDownloader(IDownloader downloader)
  {
    if (_downloader != null)
    {
      ILogger.instance().logError("LOGIC ERROR: downloader already initialized");
      return;
    }
    if (downloader == null)
    {
      ILogger.instance().logError("LOGIC ERROR: downloader cannot be NULL");
      return;
    }
    _downloader = downloader;
  }


  /**
   * Sets the _threadUtils
   *
   * @param threadUtils - cannot be NULL.
   */
  public final void setThreadUtils(IThreadUtils threadUtils)
  {
    if (_threadUtils != null)
    {
      ILogger.instance().logError("LOGIC ERROR: threadUtils already initialized");
      return;
    }
    if (threadUtils == null)
    {
      ILogger.instance().logError("LOGIC ERROR: threadUtils cannot be NULL");
      return;
    }
    _threadUtils = threadUtils;
  }


  /**
   * Sets the _cameraActivityListener
   *
   * @param cameraActivityListener - cannot be NULL.
   */
  public final void setCameraActivityListener(ICameraActivityListener cameraActivityListener)
  {
    if (_cameraActivityListener != null)
    {
      ILogger.instance().logError("LOGIC ERROR: cameraActivityListener already initialized");
      return;
    }
    if (cameraActivityListener == null)
    {
      ILogger.instance().logError("LOGIC ERROR: cameraActivityListener cannot be NULL");
      return;
    }
    _cameraActivityListener = cameraActivityListener;
  }


  /**
   * Sets the _planet
   *
   * @param planet - cannot be NULL.
   */
  public final void setPlanet(Planet planet)
  {
    if (_planet != null)
    {
      ILogger.instance().logError("LOGIC ERROR: planet already initialized");
      return;
    }
    if (planet == null)
    {
      ILogger.instance().logError("LOGIC ERROR: planet cannot be NULL");
      return;
    }
    _planet = planet;
  }


  /**
   * Adds a new camera constraint to the constraints list.
   * The camera constraint list will be initializated with a default constraints set.
   * @see IG3MBuilder#createDefaultCameraConstraints()
   *
   * @param cameraConstraint - cannot be NULL.
   */
  public final void addCameraConstraint(ICameraConstrainer cameraConstraint)
  {
    if (cameraConstraint == null)
    {
      ILogger.instance().logError("LOGIC ERROR: trying to add a NULL cameraConstraint object");
      return;
    }
    getCameraConstraints().add(cameraConstraint);
  }


  /**
   * Sets the camera constraints list, ignoring the default camera constraints list
   * and the camera constraints previously added, if added.
   *
   * @param cameraConstraints - std::vector<ICameraConstrainer*>
   */
  public final void setCameraConstrainsts(java.util.ArrayList<ICameraConstrainer> cameraConstraints)
  {
    if (_cameraConstraints != null)
    {
      ILogger.instance().logWarning("LOGIC WARNING: camera contraints previously set will be ignored and deleted");
      for (int i = 0; i < _cameraConstraints.size(); i++)
      {
        if (_cameraConstraints.get(i) != null)
           _cameraConstraints.get(i).dispose();
      }
      _cameraConstraints.clear();
    }
    else
    {
      _cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
    }
    for (int i = 0; i < cameraConstraints.size(); i++)
    {
      _cameraConstraints.add(cameraConstraints.get(i));
    }
  }


  /**
   * Sets the _cameraRenderer
   *
   * @param cameraRenderer - cannot be NULL.
   */
  public final void setCameraRenderer(CameraRenderer cameraRenderer)
  {
    if (_cameraRenderer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: cameraRenderer already initialized");
      return;
    }
    if (cameraRenderer == null)
    {
      ILogger.instance().logError("LOGIC ERROR: cameraRenderer cannot be NULL");
      return;
    }
    _cameraRenderer = cameraRenderer;
  }


  /**
   * Sets the _backgroundColor
   *
   * @param backgroundColor - cannot be NULL.
   */
  public final void setBackgroundColor(Color backgroundColor)
  {
    if (_backgroundColor != null)
    {
      ILogger.instance().logError("LOGIC ERROR: backgroundColor already initialized");
      return;
    }
    if (backgroundColor == null)
    {
      ILogger.instance().logError("LOGIC ERROR: backgroundColor cannot be NULL");
      return;
    }
    _backgroundColor = backgroundColor;
  }


  /**
   * Sets the _busyRenderer
   *
   * @param busyRenderer - cannot be NULL.
   */
  public final void setBusyRenderer(ProtoRenderer busyRenderer)
  {
    if (_busyRenderer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: busyRenderer already initialized");
      return;
    }
    if (busyRenderer == null)
    {
      ILogger.instance().logError("LOGIC ERROR: busyRenderer cannot be NULL");
      return;
    }
    _busyRenderer = busyRenderer;
  }

  public final void setErrorRenderer(ErrorRenderer errorRenderer)
  {
    if (_errorRenderer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: errorRenderer already initialized");
      return;
    }
    if (errorRenderer == null)
    {
      ILogger.instance().logError("LOGIC ERROR: errorRenderer cannot be NULL");
      return;
    }
    _errorRenderer = errorRenderer;
  }

  public final void setHUDRenderer(Renderer hudRenderer)
  {
    if (_hudRenderer != null)
    {
      ILogger.instance().logError("LOGIC ERROR: hudRenderer already initialized");
      return;
    }
    if (hudRenderer == null)
    {
      ILogger.instance().logError("LOGIC ERROR: hudRenderer cannot be NULL");
      return;
    }
    _hudRenderer = hudRenderer;
  }


  /**
   * Adds a new renderer to the renderers list.
   * The renderers list will be initializated with a default renderers set (empty set at the moment).
   *
   * @param renderer - cannot be either NULL or an instance of PlanetRenderer
   */
  public final void addRenderer(Renderer renderer)
  {
    if (renderer == null)
    {
      ILogger.instance().logError("LOGIC ERROR: trying to add a NULL renderer object");
      return;
    }
    if (renderer.isPlanetRenderer())
    {
      ILogger.instance().logError("LOGIC ERROR: a new PlanetRenderer is not expected to be added");
      return;
    }
    getRenderers().add(renderer);
  }


  /**
   * Sets the renderers list, ignoring the default renderers list and the renderers
   * previously added, if added.
   * The renderers list must contain at least an instance of the PlanetRenderer class.
   *
   * @param renderers - std::vector<Renderer*>
   */
  public final void setRenderers(java.util.ArrayList<Renderer> renderers)
  {
    if (!containsPlanetRenderer(renderers))
    {
      ILogger.instance().logError("LOGIC ERROR: renderers list must contain at least an instance of the PlanetRenderer class");
      return;
    }
    if (_renderers != null)
    {
      ILogger.instance().logWarning("LOGIC WARNING: renderers previously set will be ignored and deleted");
      for (int i = 0; i < _renderers.size(); i++)
      {
        if (_renderers.get(i) != null)
           _renderers.get(i).dispose();
      }
      _renderers.clear();
    }
    else
    {
      _renderers = new java.util.ArrayList<Renderer>();
    }
    for (int i = 0; i < renderers.size(); i++)
    {
      _renderers.add(renderers.get(i));
    }
  }


  /**
   * Adds a new periodical task to the periodical tasks list.
   * The periodical tasks list will be initializated with a default periodical task set (empty set at the moment).
   *
   * @param periodicalTasks - cannot be NULL
   */
  public final void addPeriodicalTask(PeriodicalTask periodicalTask)
  {
    if (periodicalTask == null)
    {
      ILogger.instance().logError("LOGIC ERROR: trying to add a NULL periodicalTask object");
      return;
    }
    getPeriodicalTasks().add(periodicalTask);
  }


  /**
   * Sets the periodical tasks list, ignoring the default periodical tasks list and the
   * periodical tasks previously added, if added.
   *
   * @param periodicalTasks - std::vector<PeriodicalTask*>
   */
  public final void setPeriodicalTasks(java.util.ArrayList<PeriodicalTask> periodicalTasks)
  {
    if (_periodicalTasks != null)
    {
      ILogger.instance().logWarning("LOGIC WARNING: periodical tasks previously set will be ignored and deleted");
      for (int i = 0; i < _periodicalTasks.size(); i++)
      {
        if (_periodicalTasks.get(i) != null)
           _periodicalTasks.get(i).dispose();
      }
      _periodicalTasks.clear();
    }
    else
    {
      _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
    }
    for (int i = 0; i < periodicalTasks.size(); i++)
    {
      _periodicalTasks.add(periodicalTasks.get(i));
    }
  }


  /**
   * Sets the _logFPS
   *
   * @param logFPS
   */
  public final void setLogFPS(boolean logFPS)
  {
    _logFPS = logFPS;
  }


  /**
   * Sets the _logDownloaderStatistics
   *
   * @param logDownloaderStatistics
   */
  public final void setLogDownloaderStatistics(boolean logDownloaderStatistics)
  {
    _logDownloaderStatistics = logDownloaderStatistics;
  }


  /**
   * Sets the _userData
   *
   * @param userData - cannot be NULL.
   */
  public final void setUserData(WidgetUserData userData)
  {
    if (_userData != null)
    {
      ILogger.instance().logError("LOGIC ERROR: userData already initialized");
      return;
    }
    if (userData == null)
    {
      ILogger.instance().logError("LOGIC ERROR: userData cannot be NULL");
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
   * Returns the _planet. If it does not exist, it will be default initializated.
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
   * Returns the _planetRendererBuilder. If it does not exist, it will be default initializated.
   *
   * @return _planetRendererBuilder: PlanetRendererBuilder*
   */
  public final PlanetRendererBuilder getPlanetRendererBuilder()
  {
    if (_planetRendererBuilder == null)
    {
      _planetRendererBuilder = new PlanetRendererBuilder();
    }
  
    return _planetRendererBuilder;
  }

  public final void addGPUProgramSources(GPUProgramSources s)
  {
    _sources.add(s);
  }

  public final void setSceneLighting(SceneLighting sceneLighting)
  {
    _sceneLighting = sceneLighting;
  }
  public final SceneLighting getSceneLighting()
  {
    if (_sceneLighting == null)
    {
      //_sceneLighting = new DefaultSceneLighting();
      _sceneLighting = new CameraFocusSceneLighting(Color.fromRGBA((float)0.3, (float)0.3, (float)0.3, (float)1.0), Color.yellow());
    }
    return _sceneLighting;
  }

  public final void setShownSector(Sector sector)
  {
    if (_shownSector != null)
    {
      ILogger.instance().logError("LOGIC ERROR: shownSector already initialized");
      return;
    }
    _shownSector = new Sector(sector);
  }

  public final GEORenderer createGEORenderer(GEOSymbolizer symbolizer)
  {
    final boolean createMeshRenderer = true;
    final boolean createShapesRenderer = true;
    final boolean createMarksRenderer = true;
    final boolean createGEOVectorLayer = true;

    return createGEORenderer(symbolizer, createMeshRenderer, createShapesRenderer, createMarksRenderer, createGEOVectorLayer);
  }

  public final GEORenderer createGEORenderer(GEOSymbolizer symbolizer, boolean createMeshRenderer, boolean createShapesRenderer, boolean createMarksRenderer, boolean createGEOVectorLayer)
  {
  
    MeshRenderer meshRenderer = createMeshRenderer ? this.createMeshRenderer() : null;
    ShapesRenderer shapesRenderer = createShapesRenderer ? this.createShapesRenderer() : null;
    MarksRenderer marksRenderer = createMarksRenderer ? this.createMarksRenderer() : null;
    GEOVectorLayer geoVectorLayer = createGEOVectorLayer ? getPlanetRendererBuilder().createGEOVectorLayer() : null;
  
    GEORenderer geoRenderer = new GEORenderer(symbolizer, meshRenderer, shapesRenderer, marksRenderer, geoVectorLayer);
    addRenderer(geoRenderer);
  
    return geoRenderer;
  }

  public final MeshRenderer createMeshRenderer()
  {
    MeshRenderer meshRenderer = new MeshRenderer();
    addRenderer(meshRenderer);
    return meshRenderer;
  }

  public final ShapesRenderer createShapesRenderer()
  {
    ShapesRenderer shapesRenderer = new ShapesRenderer();
    addRenderer(shapesRenderer);
    return shapesRenderer;
  }

  public final MarksRenderer createMarksRenderer()
  {
    MarksRenderer marksRenderer = new MarksRenderer(false);
    addRenderer(marksRenderer);
    return marksRenderer;
  }

  public final void setInfoDisplay(InfoDisplay infoDisplay)
  {
    if (_infoDisplay != null)
    {
      ILogger.instance().logError("LOGIC ERROR: infoDisplay already initialized");
      return;
    }
    _infoDisplay = infoDisplay;
  }
}