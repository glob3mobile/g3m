package org.glob3.mobile.generated; 
public class G3MWidget implements ChangedRendererInfoListener
{

  public static void initSingletons(ILogger logger, IFactory factory, IStringUtils stringUtils, IStringBuilder stringBuilder, IMathUtils mathUtils, IJSONParser jsonParser, ITextUtils textUtils)
  {
    if (ILogger.instance() == null)
    {
      ILogger.setInstance(logger);
      IFactory.setInstance(factory);
      IStringUtils.setInstance(stringUtils);
      IStringBuilder.setInstance(stringBuilder);
      IMathUtils.setInstance(mathUtils);
      IJSONParser.setInstance(jsonParser);
      ITextUtils.setInstance(textUtils);
    }
    else
    {
      ILogger.instance().logWarning("Singletons already set");
    }
  }

  public static G3MWidget create(GL gl, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, ICameraActivityListener cameraActivityListener, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, ProtoRenderer busyRenderer, ErrorRenderer errorRenderer, Renderer hudRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GInitializationTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks, GPUProgramManager gpuProgramManager, SceneLighting sceneLighting, InitialCameraPositionProvider initialCameraPositionProvider, InfoDisplay infoDisplay)
  {
  
    return new G3MWidget(gl, storage, downloader, threadUtils, cameraActivityListener, planet, cameraConstrainers, cameraRenderer, mainRenderer, busyRenderer, errorRenderer, hudRenderer, backgroundColor, logFPS, logDownloaderStatistics, initializationTask, autoDeleteInitializationTask, periodicalTasks, gpuProgramManager, sceneLighting, initialCameraPositionProvider, infoDisplay);
  }

  public void dispose()
  {
    if (_rendererState != null)
       _rendererState.dispose();
    if (_renderContext != null)
       _renderContext.dispose();
  
    if (_userData != null)
       _userData.dispose();
  
    if (_planet != null)
       _planet.dispose();
    if (_cameraRenderer != null)
       _cameraRenderer.dispose();
    if (_mainRenderer != null)
       _mainRenderer.dispose();
    if (_busyRenderer != null)
       _busyRenderer.dispose();
    if (_errorRenderer != null)
       _errorRenderer.dispose();
    if (_hudRenderer != null)
       _hudRenderer.dispose();
    if (_gl != null)
       _gl.dispose();
    if (_effectsScheduler != null)
       _effectsScheduler.dispose();
    if (_currentCamera != null)
       _currentCamera.dispose();
    if (_nextCamera != null)
       _nextCamera.dispose();
    if (_texturesHandler != null)
       _texturesHandler.dispose();
    if (_timer != null)
       _timer.dispose();
  
    if (_downloader != null)
    {
      _downloader.stop();
      if (_downloader != null)
         _downloader.dispose();
    }
  
    if (_storage != null)
       _storage.dispose();
    if (_threadUtils != null)
       _threadUtils.dispose();
    if (_cameraActivityListener != null)
       _cameraActivityListener.dispose();
  
    for (int n = 0; n < _cameraConstrainers.size(); n++)
    {
      if (_cameraConstrainers.get(n) != null)
         _cameraConstrainers.get(n).dispose();
    }
    if (_frameTasksExecutor != null)
       _frameTasksExecutor.dispose();
  
    for (int i = 0; i < _periodicalTasks.size(); i++)
    {
      PeriodicalTask periodicalTask = _periodicalTasks.get(i);
      if (periodicalTask != null)
         periodicalTask.dispose();
    }
  
    if (_context != null)
       _context.dispose();
  
    if (_rootState != null)
    {
      _rootState._release();
    }
    if (_initialCameraPositionProvider != null)
       _initialCameraPositionProvider.dispose();
  
    if(_infoDisplay != null)
    {
      if (_infoDisplay != null)
         _infoDisplay.dispose();
    }
  }

  public final void render(int width, int height)
  {
    if (_paused)
    {
      return;
    }
  
    if (_width != width || _height != height)
    {
      _width = width;
      _height = height;
  
      onResizeViewportEvent(_width, _height);
    }
  
    if (!_initialCameraPositionHasBeenSet)
    {
      _initialCameraPositionHasBeenSet = true;
  
      final Geodetic3D position = _initialCameraPositionProvider.getCameraPosition(_planet, _mainRenderer.getPlanetRenderer());
  
      _currentCamera.setGeodeticPosition(position);
      _currentCamera.setHeading(Angle.zero());
      _currentCamera.setPitch(Angle.fromDegrees(-90));
      _currentCamera.setRoll(Angle.zero());
  
      _nextCamera.setGeodeticPosition(position);
      _nextCamera.setHeading(Angle.zero());
      _nextCamera.setPitch(Angle.fromDegrees(-90));
      _nextCamera.setRoll(Angle.zero());
    }
  
    _timer.start();
    _renderCounter++;
  
  
  
    if (_initializationTask != null)
    {
      if (!_initializationTaskWasRun)
      {
        _initializationTask.run(_context);
        _initializationTaskWasRun = true;
      }
  
      _initializationTaskReady = _initializationTask.isDone(_context);
      if (_initializationTaskReady)
      {
        if (_autoDeleteInitializationTask)
        {
          if (_initializationTask != null)
             _initializationTask.dispose();
        }
        _initializationTask = null;
      }
    }
  
    // Start periodical tasks
    final int periodicalTasksCount = _periodicalTasks.size();
    for (int i = 0; i < periodicalTasksCount; i++)
    {
      PeriodicalTask pt = _periodicalTasks.get(i);
      pt.executeIfNecessary(_context);
    }
  
    // give to the CameraContrainers the opportunity to change the nextCamera
    final int cameraConstrainersCount = _cameraConstrainers.size();
    for (int i = 0; i< cameraConstrainersCount; i++)
    {
      ICameraConstrainer constrainer = _cameraConstrainers.get(i);
      constrainer.onCameraChange(_planet, _currentCamera, _nextCamera);
    }
    _planet.applyCameraConstrainers(_currentCamera, _nextCamera);
  
    _currentCamera.copyFrom(_nextCamera);
  
    _rendererState = calculateRendererState();
    final RenderState_Type renderStateType = _rendererState._type;
  
    _renderContext.clear();
  
    _effectsScheduler.doOneCyle(_renderContext);
  
    _frameTasksExecutor.doPreRenderCycle(_renderContext);
  
    _gl.clearScreen(_backgroundColor);
  
    if (_rootState == null)
    {
      _rootState = new GLState();
    }
  
    switch (renderStateType)
    {
      case RENDER_READY:
        setSelectedRenderer(_mainRenderer);
        _cameraRenderer.render(_renderContext, _rootState);
  
        _sceneLighting.modifyGLState(_rootState, _renderContext); //Applying ilumination to rootState
  
        if (_mainRenderer.isEnable())
        {
          _mainRenderer.render(_renderContext, _rootState);
        }
  
        break;
  
      case RENDER_BUSY:
        setSelectedRenderer(_busyRenderer);
        _busyRenderer.render(_renderContext, _rootState);
        break;
  
      default:
        _errorRenderer.setErrors(_rendererState.getErrors());
        setSelectedRenderer(_errorRenderer);
        _errorRenderer.render(_renderContext, _rootState);
        break;
  
    }
  
    java.util.ArrayList<OrderedRenderable> orderedRenderables = _renderContext.getSortedOrderedRenderables();
    if (orderedRenderables != null)
    {
      final int orderedRenderablesCount = orderedRenderables.size();
      for (int i = 0; i < orderedRenderablesCount; i++)
      {
        OrderedRenderable orderedRenderable = orderedRenderables.get(i);
        orderedRenderable.render(_renderContext);
        if (orderedRenderable != null)
           orderedRenderable.dispose();
      }
    }
  
    if (_hudRenderer != null)
    {
      if (renderStateType == RenderState_Type.RENDER_READY)
      {
        if (_hudRenderer.isEnable())
        {
          _hudRenderer.render(_renderContext, _rootState);
        }
      }
    }
  
    //Removing unused programs
    if (_renderCounter % _nFramesBeetweenProgramsCleanUp == 0)
    {
      _gpuProgramManager.removeUnused();
    }
  
    final long elapsedTimeMS = _timer.elapsedTimeInMilliseconds();
    //  if (elapsedTimeMS > 100) {
    //    ILogger::instance()->logWarning("Frame took too much time: %dms", elapsedTimeMS);
    //  }
  
    if (_logFPS)
    {
      _totalRenderTime += elapsedTimeMS;
  
      if ((_renderStatisticsTimer == null) || (_renderStatisticsTimer.elapsedTimeInMilliseconds() > 2000))
      {
        final double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
        final double fps = 1000.0 / averageTimePerRender;
        ILogger.instance().logInfo("FPS=%f", fps);
  
        _renderCounter = 0;
        _totalRenderTime = 0;
  
        if (_renderStatisticsTimer == null)
        {
          _renderStatisticsTimer = IFactory.instance().createTimer();
        }
        else
        {
          _renderStatisticsTimer.start();
        }
      }
    }
  
    if (_logDownloaderStatistics)
    {
      String cacheStatistics = "";
  
      if (_downloader != null)
      {
        cacheStatistics = _downloader.statistics();
      }
  
      if (!_lastCacheStatistics.equals(cacheStatistics))
      {
        ILogger.instance().logInfo("%s", cacheStatistics);
        _lastCacheStatistics = cacheStatistics;
      }
    }
  }

  public final void onTouchEvent(TouchEvent touchEvent)
  {
  
    G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage, _surfaceElevationProvider);
  
    // notify the original event
    notifyTouchEvent(ec, touchEvent);
  
    // creates DownUp event when a Down is immediately followed by an Up
    if (touchEvent.getTouchCount() == 1)
    {
      final TouchEventType eventType = touchEvent.getType();
      if (eventType == TouchEventType.Down)
      {
        _clickOnProcess = true;
        final Vector2F pos = touchEvent.getTouch(0).getPos();
        _touchDownPositionX = pos._x;
        _touchDownPositionY = pos._y;
      }
      else
      {
        if (eventType == TouchEventType.Up)
        {
          if (_clickOnProcess)
          {
            final Touch touch = touchEvent.getTouch(0);
            final TouchEvent downUpEvent = TouchEvent.create(TouchEventType.DownUp, new Touch(touch));
            notifyTouchEvent(ec, downUpEvent);
            if (downUpEvent != null)
               downUpEvent.dispose();
          }
        }
        if (_clickOnProcess)
        {
          if (eventType == TouchEventType.Move)
          {
            final Vector2F movePosition = touchEvent.getTouch(0).getPos();
            final double sd = movePosition.squaredDistanceTo(_touchDownPositionX, _touchDownPositionY);
            final float thresholdInPixels = _context.getFactory().getDeviceInfo().getPixelsInMM(1);
            if (sd > (thresholdInPixels * thresholdInPixels))
            {
              _clickOnProcess = false;
            }
          }
          else
          {
            _clickOnProcess = false;
          }
        }
      }
    }
    else
    {
      _clickOnProcess = false;
    }
  
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage, _surfaceElevationProvider);
  
    _nextCamera.resizeViewport(width, height);
    _currentCamera.resizeViewport(width, height);
    _cameraRenderer.onResizeViewportEvent(ec, width, height);
    _mainRenderer.onResizeViewportEvent(ec, width, height);
    _busyRenderer.onResizeViewportEvent(ec, width, height);
    _errorRenderer.onResizeViewportEvent(ec, width, height);
    if (_hudRenderer != null)
    {
      _hudRenderer.onResizeViewportEvent(ec, width, height);
    }
  }

  public final void onPause()
  {
    _paused = true;
  
    _threadUtils.onPause(_context);
  
    _effectsScheduler.onPause(_context);
  
    _mainRenderer.onPause(_context);
    _busyRenderer.onPause(_context);
    _errorRenderer.onPause(_context);
    if (_hudRenderer != null)
    {
      _hudRenderer.onPause(_context);
    }
  
    _downloader.onPause(_context);
    _storage.onPause(_context);
  }

  public final void onResume()
  {
    _paused = false;
  
    _storage.onResume(_context);
  
    _downloader.onResume(_context);
  
    _mainRenderer.onResume(_context);
    _busyRenderer.onResume(_context);
    _errorRenderer.onResume(_context);
    if (_hudRenderer != null)
    {
      _hudRenderer.onResume(_context);
    }
  
    _effectsScheduler.onResume(_context);
  
    _threadUtils.onResume(_context);
  }

  public final void onDestroy()
  {
    _threadUtils.onDestroy(_context);
  
    _effectsScheduler.onDestroy(_context);
  
    _mainRenderer.onDestroy(_context);
    _busyRenderer.onDestroy(_context);
    _errorRenderer.onDestroy(_context);
    if (_hudRenderer != null)
    {
      _hudRenderer.onDestroy(_context);
    }
  
    _downloader.onDestroy(_context);
    _storage.onDestroy(_context);
  }

  public final GL getGL()
  {
    return _gl;
  }

  public final EffectsScheduler getEffectsScheduler()
  {
    return _effectsScheduler;
  }

  public final Camera getCurrentCamera()
  {
    return _currentCamera;
  }

  public final Camera getNextCamera()
  {
    return _nextCamera;
  }

  public final void setUserData(WidgetUserData userData)
  {
    if (_userData != null)
       _userData.dispose();

    _userData = userData;
    if (_userData != null)
    {
      _userData.setWidget(this);
    }
  }

  public final WidgetUserData getUserData()
  {
    return _userData;
  }

  public final void addPeriodicalTask(PeriodicalTask periodicalTask)
  {
    _periodicalTasks.add(periodicalTask);
  }

  public final void addPeriodicalTask(TimeInterval interval, GTask task)
  {
    addPeriodicalTask(new PeriodicalTask(interval, task));
  }

  public final void resetPeriodicalTasksTimeouts()
  {
    final int periodicalTasksCount = _periodicalTasks.size();
    for (int i = 0; i < periodicalTasksCount; i++)
    {
      PeriodicalTask pt = _periodicalTasks.get(i);
      pt.resetTimeout();
    }
  }

  public final void setCameraPosition(Geodetic3D position)
  {
    getNextCamera().setGeodeticPosition(position);
    _initialCameraPositionHasBeenSet = true;
  }

  public final void setCameraHeading(Angle heading)
  {
    getNextCamera().setHeading(heading);
  }

  public final void setCameraPitch(Angle pitch)
  {
    getNextCamera().setPitch(pitch);
  }

  public final void setCameraRoll(Angle roll)
  {
    getNextCamera().setRoll(roll);
  }

  public final void setCameraHeadingPitchRoll(Angle heading, Angle pitch, Angle roll)
  {
    getNextCamera().setHeadingPitchRoll(heading, pitch, roll);
  }

  public final void setAnimatedCameraPosition(Geodetic3D position, Angle heading)
  {
     setAnimatedCameraPosition(position, heading, Angle.fromDegrees(-90));
  }
  public final void setAnimatedCameraPosition(Geodetic3D position)
  {
     setAnimatedCameraPosition(position, Angle.zero(), Angle.fromDegrees(-90));
  }
  public final void setAnimatedCameraPosition(Geodetic3D position, Angle heading, Angle pitch)
  {
    setAnimatedCameraPosition(TimeInterval.fromSeconds(3), position, heading, pitch);
  }

  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D position, Angle heading, Angle pitch, boolean linearTiming)
  {
     setAnimatedCameraPosition(interval, position, heading, pitch, linearTiming, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D position, Angle heading, Angle pitch)
  {
     setAnimatedCameraPosition(interval, position, heading, pitch, false, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D position, Angle heading)
  {
     setAnimatedCameraPosition(interval, position, heading, Angle.fromDegrees(-90), false, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D position)
  {
     setAnimatedCameraPosition(interval, position, Angle.zero(), Angle.fromDegrees(-90), false, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D position, Angle heading, Angle pitch, boolean linearTiming, boolean linearHeight)
  {
    final Geodetic3D fromPosition = _nextCamera.getGeodeticPosition();
    final Angle fromHeading = _nextCamera.getHeading();
    final Angle fromPitch = _nextCamera.getPitch();
  
    setAnimatedCameraPosition(interval, fromPosition, position, fromHeading, heading, fromPitch, pitch, linearTiming, linearHeight);
  }

  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromHeading, Angle toHeading, Angle fromPitch, Angle toPitch, boolean linearTiming)
  {
     setAnimatedCameraPosition(interval, fromPosition, toPosition, fromHeading, toHeading, fromPitch, toPitch, linearTiming, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromHeading, Angle toHeading, Angle fromPitch, Angle toPitch)
  {
     setAnimatedCameraPosition(interval, fromPosition, toPosition, fromHeading, toHeading, fromPitch, toPitch, false, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D fromPosition, Geodetic3D toPosition, Angle fromHeading, Angle toHeading, Angle fromPitch, Angle toPitch, boolean linearTiming, boolean linearHeight)
  {
  
    if (fromPosition.isEquals(toPosition) && fromHeading.isEquals(toHeading) && fromPitch.isEquals(toPitch))
    {
      return;
    }
  
    double finalLatInDegrees = toPosition._latitude._degrees;
    double finalLonInDegrees = toPosition._longitude._degrees;
  
    //Fixing final latitude
    while (finalLatInDegrees > 90)
    {
      finalLatInDegrees -= 360;
    }
    while (finalLatInDegrees < -90)
    {
      finalLatInDegrees += 360;
    }
  
    //Fixing final longitude
    while (finalLonInDegrees > 360)
    {
      finalLonInDegrees -= 360;
    }
    while (finalLonInDegrees < 0)
    {
      finalLonInDegrees += 360;
    }
    if (Math.abs(finalLonInDegrees - fromPosition._longitude._degrees) > 180)
    {
      finalLonInDegrees -= 360;
    }
  
    final Geodetic3D finalToPosition = Geodetic3D.fromDegrees(finalLatInDegrees, finalLonInDegrees, toPosition._height);
  
    cancelCameraAnimation();
  
    _effectsScheduler.startEffect(new CameraGoToPositionEffect(interval, fromPosition, finalToPosition, fromHeading, toHeading, fromPitch, toPitch, linearTiming, linearHeight), _nextCamera.getEffectTarget());
  }

  public final void cancelCameraAnimation()
  {
    EffectTarget target = _nextCamera.getEffectTarget();
    _effectsScheduler.cancelAllEffectsFor(target);
  }

  public final void cancelAllEffects()
  {
    _effectsScheduler.cancelAllEffects();
  }

  public final CameraRenderer getCameraRenderer()
  {
    return _cameraRenderer;
  }

  public final Renderer getHUDRenderer()
  {
    return _hudRenderer;
  }

  public final G3MContext getG3MContext()
  {
    return _context;
  }

  public final void setBackgroundColor(Color backgroundColor)
  {
    if (_backgroundColor != null)
       _backgroundColor.dispose();
  
    _backgroundColor = new Color(backgroundColor);
  }

  public final PlanetRenderer getPlanetRenderer()
  {
    return _mainRenderer.getPlanetRenderer();
  }

  public final boolean setRenderedSector(Sector sector)
  {
    final boolean changed = getPlanetRenderer().setRenderedSector(sector);
    if (changed)
    {
      _initialCameraPositionHasBeenSet = false;
    }
    return changed;
  }

  public final void setForceBusyRenderer(boolean forceBusyRenderer)
  {
    _forceBusyRenderer = forceBusyRenderer;
  }

  //void notifyChangedInfo() const;

  public final void setInfoDisplay(InfoDisplay infoDisplay)
  {
    _infoDisplay = infoDisplay;
  }

  public final InfoDisplay getInfoDisplay()
  {
    return _infoDisplay;
  }


  //void G3MWidget::notifyChangedInfo() const {
  //  if(_hudRenderer != NULL){
  //    const RenderState_Type renderStateType = _rendererState->_type;
  //    switch (renderStateType) {
  //      case RENDER_READY:
  //      //_hudRenderer->setInfo(_mainRenderer->getInfo());
  //      break;
  //
  //      case RENDER_BUSY:
  //      break;
  //
  //      default:
  //      break;
  //
  //    }
  //  }
  //}
  
  public final void changedRendererInfo(int rendererIdentifier, java.util.ArrayList<Info> info)
  {
    if(_infoDisplay != null)
    {
      _infoDisplay.changedInfo(info);
    }
    //  else {
    //    ILogger::instance()->logWarning("Render Infos are changing and InfoDisplay is NULL");
    //  }
  }

  public final void removeAllPeriodicalTasks()
  {
    for (int i = 0; i < _periodicalTasks.size(); i++)
    {
      PeriodicalTask periodicalTask = _periodicalTasks.get(i);
      if (periodicalTask != null)
         periodicalTask.dispose();
    }
    _periodicalTasks.clear();
  }


  private IStorage _storage;
  private IDownloader _downloader;
  private IThreadUtils _threadUtils;
  private ICameraActivityListener _cameraActivityListener;

  private FrameTasksExecutor _frameTasksExecutor;
  private GL _gl;
  private final Planet _planet;

  private CameraRenderer _cameraRenderer;
  private Renderer _mainRenderer;
  private ProtoRenderer _busyRenderer;
  private ErrorRenderer _errorRenderer;
  private Renderer _hudRenderer;
  private RenderState _rendererState;
  private ProtoRenderer _selectedRenderer;

  private EffectsScheduler _effectsScheduler;

  private java.util.ArrayList<ICameraConstrainer> _cameraConstrainers = new java.util.ArrayList<ICameraConstrainer>();

  private Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;

  private Color _backgroundColor;

  private ITimer _timer;
  private int _renderCounter;
  private int _totalRenderTime;
  private final boolean _logFPS;
  private final boolean _logDownloaderStatistics;
  private String _lastCacheStatistics;
  private final int _nFramesBeetweenProgramsCleanUp;

  private ITimer _renderStatisticsTimer;

  private WidgetUserData _userData;

  private GInitializationTask _initializationTask;
  private boolean _autoDeleteInitializationTask;

  private java.util.ArrayList<PeriodicalTask> _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();

  private int _width;
  private int _height;

  private final G3MContext _context;

  private boolean _paused;
  private boolean _initializationTaskWasRun;
  private boolean _initializationTaskReady;

  private boolean _clickOnProcess;

  private GPUProgramManager _gpuProgramManager;

  private SurfaceElevationProvider _surfaceElevationProvider;

  private SceneLighting _sceneLighting;
  private GLState _rootState;

  private final InitialCameraPositionProvider _initialCameraPositionProvider;
  private boolean _initialCameraPositionHasBeenSet;

  private G3MRenderContext _renderContext;

  private boolean _forceBusyRenderer;

  private InfoDisplay _infoDisplay;


  private float _touchDownPositionX;
  private float _touchDownPositionY;


  private G3MWidget(GL gl, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, ICameraActivityListener cameraActivityListener, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, ProtoRenderer busyRenderer, ErrorRenderer errorRenderer, Renderer hudRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GInitializationTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks, GPUProgramManager gpuProgramManager, SceneLighting sceneLighting, InitialCameraPositionProvider initialCameraPositionProvider, InfoDisplay infoDisplay)
  {
     _frameTasksExecutor = new FrameTasksExecutor();
     _effectsScheduler = new EffectsScheduler();
     _gl = gl;
     _downloader = downloader;
     _storage = storage;
     _threadUtils = threadUtils;
     _cameraActivityListener = cameraActivityListener;
     _texturesHandler = new TexturesHandler(_gl, false);
     _planet = planet;
     _cameraConstrainers = cameraConstrainers;
     _cameraRenderer = cameraRenderer;
     _mainRenderer = mainRenderer;
     _busyRenderer = busyRenderer;
     _errorRenderer = errorRenderer;
     _hudRenderer = hudRenderer;
     _width = 1;
     _height = 1;
     _currentCamera = new Camera(1);
     _nextCamera = new Camera(2);
     _backgroundColor = new Color(backgroundColor);
     _timer = IFactory.instance().createTimer();
     _renderCounter = 0;
     _totalRenderTime = 0;
     _logFPS = logFPS;
     _rendererState = new RenderState(RenderState.busy());
     _selectedRenderer = null;
     _renderStatisticsTimer = null;
     _logDownloaderStatistics = logDownloaderStatistics;
     _userData = null;
     _initializationTask = initializationTask;
     _autoDeleteInitializationTask = autoDeleteInitializationTask;
     _surfaceElevationProvider = mainRenderer.getSurfaceElevationProvider();
     _context = new G3MContext(IFactory.instance(), IStringUtils.instance(), threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, downloader, _effectsScheduler, storage, mainRenderer.getSurfaceElevationProvider());
     _paused = false;
     _initializationTaskWasRun = false;
     _initializationTaskReady = true;
     _clickOnProcess = false;
     _gpuProgramManager = gpuProgramManager;
     _sceneLighting = sceneLighting;
     _rootState = null;
     _initialCameraPositionProvider = initialCameraPositionProvider;
     _initialCameraPositionHasBeenSet = false;
     _forceBusyRenderer = false;
     _nFramesBeetweenProgramsCleanUp = 500;
     _infoDisplay = infoDisplay;
     _touchDownPositionX = 0F;
     _touchDownPositionY = 0F;
    _effectsScheduler.initialize(_context);
    _cameraRenderer.initialize(_context);
    _mainRenderer.initialize(_context);
    _busyRenderer.initialize(_context);
    _errorRenderer.initialize(_context);
    if (_hudRenderer != null)
    {
      _hudRenderer.initialize(_context);
    }
    _currentCamera.initialize(_context);
    _nextCamera.initialize(_context);
  
    if (_threadUtils != null)
    {
      _threadUtils.initialize(_context);
    }
  
    if (_storage != null)
    {
      _storage.initialize(_context);
    }
  
    if (_downloader != null)
    {
      _downloader.initialize(_context, _frameTasksExecutor);
      _downloader.start();
    }
  
    for (int i = 0; i < periodicalTasks.size(); i++)
    {
      addPeriodicalTask(periodicalTasks.get(i));
    }
    _mainRenderer.setChangedRendererInfoListener((ChangedRendererInfoListener)this, -1);
  
  
    _renderContext = new G3MRenderContext(_frameTasksExecutor, IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _gl, _currentCamera, _nextCamera, _texturesHandler, _downloader, _effectsScheduler, IFactory.instance().createTimer(), _storage, _gpuProgramManager, _surfaceElevationProvider);
  
  
    ///#ifdef C_CODE
    //  delete _rendererState;
    //  _rendererState = new RenderState( calculateRendererState() );
    ///#endif
    ///#ifdef JAVA_CODE
    //  _rendererState = calculateRendererState();
    ///#endif
  }

  private void notifyTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    final RenderState_Type renderStateType = _rendererState._type;
    switch (renderStateType)
    {
      case RENDER_READY:
      {
        boolean handled = false;
  
        if (_hudRenderer != null)
        {
          if (_hudRenderer.isEnable())
          {
            handled = _hudRenderer.onTouchEvent(ec, touchEvent);
          }
        }
  
        if (!handled && _mainRenderer.isEnable())
        {
          handled = _mainRenderer.onTouchEvent(ec, touchEvent);
        }
  
        if (!handled)
        {
          handled = _cameraRenderer.onTouchEvent(ec, touchEvent);
          if (handled)
          {
            if (_cameraActivityListener != null)
            {
              _cameraActivityListener.touchEventHandled();
            }
          }
        }
        break;
      }
      case RENDER_BUSY:
      {
        break;
      }
      default:
      {
        break;
      }
    }
  }

  private RenderState calculateRendererState()
  {
    if (_forceBusyRenderer)
    {
      return RenderState.busy();
    }
  
    if (!_initializationTaskReady)
    {
      return RenderState.busy();
    }
  
    boolean busyFlag = false;
  
    RenderState cameraRendererRenderState = _cameraRenderer.getRenderState(_renderContext);
    if (cameraRendererRenderState._type == RenderState_Type.RENDER_ERROR)
    {
      return cameraRendererRenderState;
    }
    else if (cameraRendererRenderState._type == RenderState_Type.RENDER_BUSY)
    {
      busyFlag = true;
    }
  
    if (_hudRenderer != null)
    {
      RenderState hudRendererRenderState = _hudRenderer.getRenderState(_renderContext);
      if (hudRendererRenderState._type == RenderState_Type.RENDER_ERROR)
      {
        return hudRendererRenderState;
      }
      else if (hudRendererRenderState._type == RenderState_Type.RENDER_BUSY)
      {
        busyFlag = true;
      }
    }
  
    RenderState mainRendererRenderState = _mainRenderer.getRenderState(_renderContext);
    if (mainRendererRenderState._type == RenderState_Type.RENDER_ERROR)
    {
      return mainRendererRenderState;
    }
    else if (mainRendererRenderState._type == RenderState_Type.RENDER_BUSY)
    {
      busyFlag = true;
    }
  
    return busyFlag ? RenderState.busy() : RenderState.ready();
  }

  private void setSelectedRenderer(ProtoRenderer selectedRenderer)
  {
    if (selectedRenderer != _selectedRenderer)
    {
      if (_selectedRenderer != null)
      {
        _selectedRenderer.stop(_renderContext);
      }
      _selectedRenderer = selectedRenderer;
      _selectedRenderer.start(_renderContext);
    }
  }

}