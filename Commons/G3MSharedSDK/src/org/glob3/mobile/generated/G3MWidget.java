package org.glob3.mobile.generated;
public class G3MWidget implements ChangedRendererInfoListener, FrustumPolicyHandler
{

  public static void initSingletons(ILogger logger, IFactory factory, IStringUtils stringUtils, IStringBuilder stringBuilder, IMathUtils mathUtils, IJSONParser jsonParser, ITextUtils textUtils, IDeviceAttitude devAttitude, IDeviceLocation devLocation)
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
      IDeviceAttitude.setInstance(devAttitude);
      IDeviceLocation.setInstance(devLocation);
    }
    else
    {
      ILogger.instance().logWarning("Singletons already set");
    }
  }

  public static G3MWidget create(GL gl, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, ICameraActivityListener cameraActivityListener, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, ProtoRenderer busyRenderer, ErrorRenderer errorRenderer, Renderer hudRenderer, NearFrustumRenderer nearFrustumRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GInitializationTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks, GPUProgramManager gpuProgramManager, SceneLighting sceneLighting, InitialCameraPositionProvider initialCameraPositionProvider, InfoDisplay infoDisplay, ViewMode viewMode, FrustumPolicy frustumPolicy)
  {
  
    return new G3MWidget(gl, storage, downloader, threadUtils, cameraActivityListener, planet, cameraConstrainers, cameraRenderer, mainRenderer, busyRenderer, errorRenderer, hudRenderer, nearFrustumRenderer, backgroundColor, logFPS, logDownloaderStatistics, initializationTask, autoDeleteInitializationTask, periodicalTasks, gpuProgramManager, sceneLighting, initialCameraPositionProvider, infoDisplay, viewMode, frustumPolicy);
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
    if (_nearFrustumRenderer != null)
       _nearFrustumRenderer.dispose();
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
  
    if (_rightEyeCam != null)
       _rightEyeCam.dispose();
    if (_leftEyeCam != null)
       _leftEyeCam.dispose();
    if (_auxCam != null)
       _auxCam.dispose();
  
    if (_frustumPolicy != null)
       _frustumPolicy.dispose();
  
    if (_previousTouchEvent != null)
       _previousTouchEvent.dispose();
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
      _currentCamera.setHeading(Angle._ZERO);
      _currentCamera.setPitch(Angle._MINUS_HALF_PI);
      _currentCamera.setRoll(Angle._ZERO);
  
      _nextCamera.setGeodeticPosition(position);
      _nextCamera.setHeading(Angle._ZERO);
      _nextCamera.setPitch(Angle._MINUS_HALF_PI);
      _nextCamera.setRoll(Angle._ZERO);
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
      final boolean validNextCamera = constrainer.onCameraChange(_planet, _currentCamera, _nextCamera);
      if (!validNextCamera)
      {
        _nextCamera.copyFrom(_currentCamera, true);
      }
    }
    _planet.applyCameraConstrains(_currentCamera, _nextCamera);
  
    _currentCamera.copyFrom(_nextCamera, false);
  
    _rendererState = calculateRendererState();
    final RenderState_Type renderStateType = _rendererState._type;
  
    _renderContext.clear();
  
    _effectsScheduler.doOneCyle(_renderContext);
  
    _frameTasksExecutor.doPreRenderCycle(_renderContext);
  
    switch (_viewMode)
    {
      case MONO:
        rawRenderMono(renderStateType);
        break;
      case STEREO:
        rawRenderStereoParallelAxis(renderStateType);
        break;
      default:
        throw new RuntimeException("WRONG VIEW MODE.");
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
  
    G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage, _surfaceElevationProvider, _viewMode, getCurrentCamera());
  
    if (_previousTouchEvent != null)
    {
      if (isDuplicatedTouchEvent(touchEvent, _previousTouchEvent))
      {
        //ILogger::instance()->logInfo("** Discarded duplicated event %s", touchEvent->description().c_str());
        return;
      }
    }
  
    // notify the original event
    notifyTouchEvent(ec, touchEvent);
    if (_previousTouchEvent != null)
       _previousTouchEvent.dispose();
    _previousTouchEvent = touchEvent.clone();
  
  
    if (touchEvent.getTouchCount() != 1)
    {
      _touchDownUpOnProcess = false;
      return;
    }
  
  
    // creates DownUp event when a Down is immediately followed by an Up
    final Touch touch = touchEvent.getTouch(0);
    final TouchEventType eventType = touchEvent.getType();
    if (eventType == TouchEventType.Down)
    {
      _touchDownUpOnProcess = true;
      final Vector2F pos = touch.getPos();
      _touchDownPositionX = pos._x;
      _touchDownPositionY = pos._y;
      return;
    }
  
  
    if (!_touchDownUpOnProcess)
    {
      return;
    }
  
  
    if (eventType == TouchEventType.Up)
    {
      final TouchEvent downUpEvent = TouchEvent.create(TouchEventType.DownUp, touch.clone());
      notifyTouchEvent(ec, downUpEvent);
      if (_previousTouchEvent != null)
         _previousTouchEvent.dispose();
      _previousTouchEvent = downUpEvent.clone();
  
      if (downUpEvent != null)
         downUpEvent.dispose();
      _touchDownUpOnProcess = false;
    }
    else if (eventType == TouchEventType.Move)
    {
      final Vector2F pos = touch.getPos();
      final double sd = pos.squaredDistanceTo(_touchDownPositionX, _touchDownPositionY);
      final float thresholdInPixels = _context.getFactory().getDeviceInfo().getPixelsInMM(1);
      if (sd > (thresholdInPixels * thresholdInPixels))
      {
        _touchDownUpOnProcess = false;
      }
    }
    else
    {
      _touchDownUpOnProcess = false;
    }
  
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage, _surfaceElevationProvider, _viewMode, getCurrentCamera());
  
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
    if (_nearFrustumRenderer != null)
    {
      _nearFrustumRenderer.onResizeViewportEvent(ec, width, height);
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
    if (_nearFrustumRenderer != null)
    {
      _nearFrustumRenderer.onPause(_context);
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
    if (_nearFrustumRenderer != null)
    {
      _nearFrustumRenderer.onResume(_context);
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
    if (_nearFrustumRenderer != null)
    {
      _nearFrustumRenderer.onDestroy(_context);
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
     setAnimatedCameraPosition(position, heading, Angle.minusHalfPi());
  }
  public final void setAnimatedCameraPosition(Geodetic3D position)
  {
     setAnimatedCameraPosition(position, Angle.zero(), Angle.minusHalfPi());
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
     setAnimatedCameraPosition(interval, position, heading, Angle.minusHalfPi(), false, false);
  }
  public final void setAnimatedCameraPosition(TimeInterval interval, Geodetic3D position)
  {
     setAnimatedCameraPosition(interval, position, Angle.zero(), Angle.minusHalfPi(), false, false);
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
      finalLatInDegrees -= 180;
    }
    while (finalLatInDegrees < -90)
    {
      finalLatInDegrees += 180;
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
  
    _backgroundColor = backgroundColor;
  }
  public final Color getBackgroundColor()
  {
    return _backgroundColor;
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
  //  if(_hudRenderer != NULL) {
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
  
  public final void changedRendererInfo(int rendererID, java.util.ArrayList<Info> info)
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

  public final void setViewMode(ViewMode viewMode)
  {
    if (_viewMode != viewMode)
    {
      _viewMode = viewMode;
  
      if (_viewMode != ViewMode.STEREO)
      {
        if (_auxCam != null)
           _auxCam.dispose();
        _auxCam = null;
        if (_leftEyeCam != null)
           _leftEyeCam.dispose();
        _leftEyeCam = null;
        if (_rightEyeCam != null)
           _rightEyeCam.dispose();
        _rightEyeCam = null;
      }
  
      _context.setViewMode(_viewMode);
      _renderContext.setViewMode(_viewMode);
  
      onResizeViewportEvent(_width, _height);
    }
  }

  public final void changeToFixedFrustum(double zNear, double zFar)
  {
    switch (_viewMode)
    {
      case MONO:
        _currentCamera.setFixedFrustum(zNear, zFar);
        break;
      case STEREO:
        _leftEyeCam.setFixedFrustum(zNear, zFar);
        _rightEyeCam.setFixedFrustum(zNear, zFar);
        _currentCamera.setFixedFrustum(zNear, zFar);
        break;
      default:
        throw new RuntimeException("WRONG VIEW MODE :: changeToFixedFrustum");
    }
  }

  public final void resetFrustumPolicy()
  {
    switch (_viewMode)
    {
      case MONO:
        _currentCamera.resetFrustumPolicy();
        break;
      case STEREO:
        _leftEyeCam.resetFrustumPolicy();
        _rightEyeCam.resetFrustumPolicy();
        _currentCamera.resetFrustumPolicy();
        break;
      default:
        throw new RuntimeException("WRONG VIEW MODE :: resetFrustumPolicy");
    }
  }

  public final void addCameraConstrainer(ICameraConstrainer constrainer)
  {
    _cameraConstrainers.add(constrainer);
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
  private NearFrustumRenderer _nearFrustumRenderer;
  private RenderState _rendererState;
  private ProtoRenderer _selectedRenderer;

  private EffectsScheduler _effectsScheduler;

  private java.util.ArrayList<ICameraConstrainer> _cameraConstrainers = new java.util.ArrayList<ICameraConstrainer>();

  private final FrustumPolicy _frustumPolicy;
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

  private G3MContext _context;
  private G3MRenderContext _renderContext;

  private boolean _paused;
  private boolean _initializationTaskWasRun;
  private boolean _initializationTaskReady;

  private boolean _touchDownUpOnProcess;

  private GPUProgramManager _gpuProgramManager;

  private SurfaceElevationProvider _surfaceElevationProvider;

  private SceneLighting _sceneLighting;
  private GLState _rootState;

  private final InitialCameraPositionProvider _initialCameraPositionProvider;
  private boolean _initialCameraPositionHasBeenSet;


  private boolean _forceBusyRenderer;

  private InfoDisplay _infoDisplay;


  private float _touchDownPositionX;
  private float _touchDownPositionY;

  private ViewMode _viewMode;

  //For stereo vision
  private Camera _auxCam;
  private Camera _leftEyeCam;
  private Camera _rightEyeCam;


  private TouchEvent _previousTouchEvent;

  private G3MWidget(GL gl, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, ICameraActivityListener cameraActivityListener, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, ProtoRenderer busyRenderer, ErrorRenderer errorRenderer, Renderer hudRenderer, NearFrustumRenderer nearFrustumRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GInitializationTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks, GPUProgramManager gpuProgramManager, SceneLighting sceneLighting, InitialCameraPositionProvider initialCameraPositionProvider, InfoDisplay infoDisplay, ViewMode viewMode, FrustumPolicy frustumPolicy)
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
     _nearFrustumRenderer = nearFrustumRenderer;
     _width = 1;
     _height = 1;
     _frustumPolicy = frustumPolicy;
     _currentCamera = new Camera(1, frustumPolicy.copy());
     _nextCamera = new Camera(2, frustumPolicy.copy());
     _backgroundColor = backgroundColor;
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
     _context = new G3MContext(IFactory.instance(), IStringUtils.instance(), threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, downloader, _effectsScheduler, storage, mainRenderer.getSurfaceElevationProvider(), viewMode);
     _paused = false;
     _initializationTaskWasRun = false;
     _initializationTaskReady = true;
     _touchDownUpOnProcess = false;
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
     _viewMode = viewMode;
     _leftEyeCam = null;
     _rightEyeCam = null;
     _auxCam = null;
     _previousTouchEvent = null;
    _effectsScheduler.initialize(_context);
    _cameraRenderer.initialize(_context);
    _mainRenderer.initialize(_context);
    _busyRenderer.initialize(_context);
    _errorRenderer.initialize(_context);
    if (_hudRenderer != null)
    {
      _hudRenderer.initialize(_context);
    }
    if (_nearFrustumRenderer != null)
    {
      _nearFrustumRenderer.initialize(_context);
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
  
  
    _renderContext = new G3MRenderContext(_frameTasksExecutor, IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _gl, _currentCamera, _nextCamera, _texturesHandler, _downloader, _effectsScheduler, IFactory.instance().createTimer(), _storage, _gpuProgramManager, _surfaceElevationProvider, _viewMode, this);
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
  
        if (!handled && (_nearFrustumRenderer != null))
        {
          if (_nearFrustumRenderer.isEnable())
          {
            handled = _nearFrustumRenderer.onTouchEvent(ec, touchEvent);
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
  
    if (_nearFrustumRenderer != null)
    {
      RenderState nearFrustumRendererRenderState = _nearFrustumRenderer.getRenderState(_renderContext);
      if (nearFrustumRendererRenderState._type == RenderState_Type.RENDER_ERROR)
      {
        return nearFrustumRendererRenderState;
      }
      else if (nearFrustumRendererRenderState._type == RenderState_Type.RENDER_BUSY)
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

  private void rawRender(RenderState_Type renderStateType)
  {
  
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
  
      orderedRenderables.clear();
    }
  
    if (renderStateType == RenderState_Type.RENDER_READY)
    {
      if (_nearFrustumRenderer != null)
      {
        if (_nearFrustumRenderer.isEnable())
        {
          _nearFrustumRenderer.render(_currentCamera.getFrustumData(), this, _renderContext, _rootState);
        }
      }
  
      if (_hudRenderer != null)
      {
        if (_hudRenderer.isEnable())
        {
          _hudRenderer.render(_renderContext, _rootState);
        }
      }
    }
  
  }

  private void rawRenderMono(RenderState_Type renderStateType)
  {
  
    _gl.clearScreen(_backgroundColor);
    _gl.viewport(0, 0, _width, _height);
    rawRender(renderStateType);
  }

  private void rawRenderStereoParallelAxis(RenderState_Type renderStateType)
  {
  
    if (_auxCam == null)
    {
      _auxCam = new Camera(-1, _frustumPolicy.copy());
    }
  
    final boolean eyesUpdated = _auxCam.getTimestamp() != _currentCamera.getTimestamp();
    if (eyesUpdated)
    {
  
      //Saving central camera
      if (_rightEyeCam == null)
      {
        _rightEyeCam = new Camera(-1, _frustumPolicy.copy());
      }
      if (_leftEyeCam == null)
      {
        _leftEyeCam = new Camera(-1, _frustumPolicy.copy());
      }
      _auxCam.copyFrom(_currentCamera, true);
      _leftEyeCam.copyFrom(_auxCam, true);
      _rightEyeCam.copyFrom(_auxCam, true);
  
      //For 3D scenes we create the "eyes" cameras
      if (renderStateType == RenderState_Type.RENDER_READY)
      {
        final Vector3D camPos = _currentCamera.getCartesianPosition();
        final Vector3D camCenter = _currentCamera.getCenter();
        final Vector3D eyesDirection = _currentCamera.getUp().cross(_currentCamera.getViewDirection()).normalized();
  //      const double halfEyesSeparation = 0.07 / 2.0;
        final double halfEyesSeparation = 0.001;
        final Vector3D up = _currentCamera.getUp();
  
        final Angle hFOV_2 = _currentCamera.getHorizontalFOV().times(0.5);
        final Angle vFOV = _currentCamera.getVerticalFOV();
  
        final Vector3D leftEyePosition = camPos.add(eyesDirection.times(-halfEyesSeparation));
        final Vector3D leftEyeCenter = camCenter.add(eyesDirection.times(-halfEyesSeparation));
        _leftEyeCam.setLookAtParams(leftEyePosition.asMutableVector3D(), leftEyeCenter.asMutableVector3D(), up.asMutableVector3D());
        _leftEyeCam.setFOV(vFOV, hFOV_2);
  
        final Vector3D rightEyePosition = camPos.add(eyesDirection.times(halfEyesSeparation));
        final Vector3D rightEyeCenter = camCenter.add(eyesDirection.times(halfEyesSeparation));
  
        _rightEyeCam.setLookAtParams(rightEyePosition.asMutableVector3D(), rightEyeCenter.asMutableVector3D(), up.asMutableVector3D());
        _rightEyeCam.setFOV(vFOV, hFOV_2);
      }
  
    }
  
    final int halfWidth = _width / 2;
  
    _gl.clearScreen(_backgroundColor);
    //Left
    _gl.viewport(0, 0, halfWidth, _height);
    _currentCamera.copyFrom(_leftEyeCam, true);
    rawRender(renderStateType);
  
    //Right
    _gl.viewport(halfWidth, 0, halfWidth, _height);
    _currentCamera.copyFrom(_rightEyeCam, true);
    rawRender(renderStateType);
  
    //Restoring central camera
    _currentCamera.copyFrom(_auxCam, true);
  }

  private boolean isDuplicatedTouchEvent(TouchEvent touchEvent, TouchEvent previousTouchEvent)
  {
    if (previousTouchEvent == null)
    {
      return false;
    }
  
    if (touchEvent.getType() != TouchEventType.Move)
    {
      // only Move events will be removed
      return false;
    }
  
    return touchEvent.isEquals(previousTouchEvent);
  }

}