

package org.glob3.mobile.generated;

public class G3MWidget {

   public static void initSingletons(final ILogger logger,
                                     final IFactory factory,
                                     final IStringUtils stringUtils,
                                     final IStringBuilder stringBuilder,
                                     final IMathUtils mathUtils,
                                     final IJSONParser jsonParser,
                                     final ITextUtils textUtils) {
      if (ILogger.instance() == null) {
         ILogger.setInstance(logger);
         IFactory.setInstance(factory);
         IStringUtils.setInstance(stringUtils);
         IStringBuilder.setInstance(stringBuilder);
         IMathUtils.setInstance(mathUtils);
         IJSONParser.setInstance(jsonParser);
         ITextUtils.setInstance(textUtils);
      }
      else {
         ILogger.instance().logWarning("Singletons already set");
      }
   }


   public static G3MWidget create(final GL gl,
                                  final IStorage storage,
                                  final IDownloader downloader,
                                  final IThreadUtils threadUtils,
                                  final ICameraActivityListener cameraActivityListener,
                                  final Planet planet,
                                  final java.util.ArrayList<ICameraConstrainer> cameraConstrainers,
                                  final CameraRenderer cameraRenderer,
                                  final Renderer mainRenderer,
                                  final Renderer busyRenderer,
                                  final ErrorRenderer errorRenderer,
                                  final Color backgroundColor,
                                  final boolean logFPS,
                                  final boolean logDownloaderStatistics,
                                  final GInitializationTask initializationTask,
                                  final boolean autoDeleteInitializationTask,
                                  final java.util.ArrayList<PeriodicalTask> periodicalTasks,
                                  final GPUProgramManager gpuProgramManager,
                                  final SceneLighting sceneLighting,
                                  final InitialCameraPositionProvider initialCameraPositionProvider) {

      return new G3MWidget(gl, storage, downloader, threadUtils, cameraActivityListener, planet, cameraConstrainers,
               cameraRenderer, mainRenderer, busyRenderer, errorRenderer, backgroundColor, logFPS, logDownloaderStatistics,
               initializationTask, autoDeleteInitializationTask, periodicalTasks, gpuProgramManager, sceneLighting,
               initialCameraPositionProvider);
   }


   public void dispose() {
      if (_userData != null) {
         _userData.dispose();
      }

      if (_planet != null) {
         _planet.dispose();
      }
      if (_cameraRenderer != null) {
         _cameraRenderer.dispose();
      }
      if (_mainRenderer != null) {
         _mainRenderer.dispose();
      }
      if (_busyRenderer != null) {
         _busyRenderer.dispose();
      }
      if (_errorRenderer != null) {
         _errorRenderer.dispose();
      }
      if (_gl != null) {
         _gl.dispose();
      }
      if (_effectsScheduler != null) {
         _effectsScheduler.dispose();
      }
      if (_currentCamera != null) {
         _currentCamera.dispose();
      }
      if (_nextCamera != null) {
         _nextCamera.dispose();
      }
      if (_texturesHandler != null) {
         _texturesHandler.dispose();
      }
      if (_timer != null) {
         _timer.dispose();
      }

      if (_downloader != null) {
         _downloader.stop();
         if (_downloader != null) {
            _downloader.dispose();
         }
      }

      if (_storage != null) {
         _storage.dispose();
      }
      if (_threadUtils != null) {
         _threadUtils.dispose();
      }
      if (_cameraActivityListener != null) {
         _cameraActivityListener.dispose();
      }

      for (int n = 0; n < _cameraConstrainers.size(); n++) {
         if (_cameraConstrainers.get(n) != null) {
            _cameraConstrainers.get(n).dispose();
         }
      }
      if (_frameTasksExecutor != null) {
         _frameTasksExecutor.dispose();
      }

      for (int i = 0; i < _periodicalTasks.size(); i++) {
         final PeriodicalTask periodicalTask = _periodicalTasks.get(i);
         if (periodicalTask != null) {
            periodicalTask.dispose();
         }
      }

      if (_context != null) {
         _context.dispose();
      }

      if (_rootState != null) {
         _rootState.dispose();
      }
      if (_initialCameraPositionProvider != null) {
         _initialCameraPositionProvider.dispose();
      }
   }


   public final void render(final int width,
                            final int height) {
      if (_paused) {
         return;
      }

      if ((_width != width) || (_height != height)) {
         _width = width;
         _height = height;

         onResizeViewportEvent(_width, _height);
      }

      if (!_initialCameraPositionHasBeenSet) {
         _initialCameraPositionHasBeenSet = true;

         final Geodetic3D g = _initialCameraPositionProvider.getCameraPosition(_planet, _mainRenderer.getPlanetRenderer(),
                  new Vector2I(_width, _height));

         _currentCamera.setGeodeticPosition(g);
         _currentCamera.setHeading(Angle.zero());
         _currentCamera.setPitch(Angle.zero());

         _nextCamera.setGeodeticPosition(g);
         _nextCamera.setHeading(Angle.zero());
         _nextCamera.setPitch(Angle.zero());
      }

      _timer.start();
      _renderCounter++;


      if (_initializationTask != null) {
         if (!_initializationTaskWasRun) {
            _initializationTask.run(_context);
            _initializationTaskWasRun = true;
         }

         _initializationTaskReady = _initializationTask.isDone(_context);
         if (_initializationTaskReady) {
            if (_autoDeleteInitializationTask) {
               if (_initializationTask != null) {
                  _initializationTask.dispose();
               }
            }
            _initializationTask = null;
         }
      }

      // Start periodical tasks
      final int periodicalTasksCount = _periodicalTasks.size();
      for (int i = 0; i < periodicalTasksCount; i++) {
         final PeriodicalTask pt = _periodicalTasks.get(i);
         pt.executeIfNecessary(_context);
      }

      // give to the CameraContrainers the opportunity to change the nextCamera
      final int cameraConstrainersCount = _cameraConstrainers.size();
      for (int i = 0; i < cameraConstrainersCount; i++) {
         final ICameraConstrainer constrainer = _cameraConstrainers.get(i);
         constrainer.onCameraChange(_planet, _currentCamera, _nextCamera);
      }
      _planet.applyCameraConstrainers(_currentCamera, _nextCamera);

      _currentCamera.copyFromForcingMatrixCreation(_nextCamera);

      final G3MRenderContext rc = new G3MRenderContext(_frameTasksExecutor, IFactory.instance(), IStringUtils.instance(),
               _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _gl, _currentCamera,
               _nextCamera, _texturesHandler, _downloader, _effectsScheduler, IFactory.instance().createTimer(), _storage,
               _gpuProgramManager, _surfaceElevationProvider);

      final int __rendererState;
      _mainRendererReady = _initializationTaskReady && _mainRenderer.isReadyToRender(rc);

      _effectsScheduler.doOneCyle(rc);

      _frameTasksExecutor.doPreRenderCycle(rc);

      final Renderer selectedRenderer = _mainRendererReady ? _mainRenderer : _busyRenderer;
      if (selectedRenderer != _selectedRenderer) {
         if (_selectedRenderer != null) {
            _selectedRenderer.stop(rc);
         }
         _selectedRenderer = selectedRenderer;
         _selectedRenderer.start(rc);
      }

      _gl.clearScreen(_backgroundColor);

      if (_rootState == null) {
         _rootState = new GLState();
      }


      if (_mainRendererReady) {
         _cameraRenderer.render(rc, _rootState);

         _sceneLighting.modifyGLState(_rootState); //Applying ilumination to rootState
      }

      if (_selectedRenderer.isEnable()) {
         _selectedRenderer.render(rc, _rootState);
      }

      final java.util.ArrayList<OrderedRenderable> orderedRenderables = rc.getSortedOrderedRenderables();
      if (orderedRenderables != null) {
         final int orderedRenderablesCount = orderedRenderables.size();
         for (int i = 0; i < orderedRenderablesCount; i++) {
            final OrderedRenderable orderedRenderable = orderedRenderables.get(i);
            orderedRenderable.render(rc);
            if (orderedRenderable != null) {
               orderedRenderable.dispose();
            }
         }
      }

      final long elapsedTimeMS = _timer.elapsedTimeInMilliseconds();
      //  if (elapsedTimeMS > 100) {
      //    ILogger::instance()->logWarning("Frame took too much time: %dms", elapsedTimeMS);
      //  }

      if (_logFPS) {
         _totalRenderTime += elapsedTimeMS;

         if ((_renderStatisticsTimer == null) || (_renderStatisticsTimer.elapsedTime().seconds() > 2)) {
            final double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
            final double fps = 1000.0 / averageTimePerRender;
            ILogger.instance().logInfo("FPS=%f", fps);

            _renderCounter = 0;
            _totalRenderTime = 0;

            if (_renderStatisticsTimer == null) {
               _renderStatisticsTimer = IFactory.instance().createTimer();
            }
            else {
               _renderStatisticsTimer.start();
            }
         }
      }

      if (_logDownloaderStatistics) {
         String cacheStatistics = "";

         if (_downloader != null) {
            cacheStatistics = _downloader.statistics();
         }

         if (!_lastCacheStatistics.equals(cacheStatistics)) {
            ILogger.instance().logInfo("%s", cacheStatistics);
            _lastCacheStatistics = cacheStatistics;
         }
      }

   }


   public final void onTouchEvent(final TouchEvent touchEvent) {

      final G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils,
               ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler,
               _storage, _surfaceElevationProvider);


      // notify the original event
      notifyTouchEvent(ec, touchEvent);


      // creates DownUp event when a Down is immediately followed by an Up
      if (touchEvent.getTouchCount() == 1) {
         final TouchEventType eventType = touchEvent.getType();
         if (eventType == TouchEventType.Down) {
            _clickOnProcess = true;
         }
         else {
            if (eventType == TouchEventType.Up) {
               if (_clickOnProcess) {

                  final Touch touch = touchEvent.getTouch(0);
                  final TouchEvent downUpEvent = TouchEvent.create(TouchEventType.DownUp, new Touch(touch));

                  notifyTouchEvent(ec, downUpEvent);

                  if (downUpEvent != null) {
                     downUpEvent.dispose();
                  }
               }
            }
            _clickOnProcess = false;
         }
      }
      else {
         _clickOnProcess = false;
      }

   }


   public final void onResizeViewportEvent(final int width,
                                           final int height) {
      final G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils,
               ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler,
               _storage, _surfaceElevationProvider);

      _nextCamera.resizeViewport(width, height);
      _currentCamera.resizeViewport(width, height);
      _cameraRenderer.onResizeViewportEvent(ec, width, height);
      _mainRenderer.onResizeViewportEvent(ec, width, height);
      _busyRenderer.onResizeViewportEvent(ec, width, height);
      _errorRenderer.onResizeViewportEvent(ec, width, height);
   }


   public final void onPause() {
      _paused = true;

      _threadUtils.onPause(_context);

      _effectsScheduler.onPause(_context);

      _mainRenderer.onPause(_context);
      _busyRenderer.onPause(_context);
      _errorRenderer.onPause(_context);

      _downloader.onPause(_context);
      _storage.onPause(_context);
   }


   public final void onResume() {
      _paused = false;

      _storage.onResume(_context);

      _downloader.onResume(_context);

      _mainRenderer.onResume(_context);
      _busyRenderer.onResume(_context);
      _errorRenderer.onResume(_context);

      _effectsScheduler.onResume(_context);

      _threadUtils.onResume(_context);
   }


   public final void onDestroy() {
      _threadUtils.onDestroy(_context);

      _effectsScheduler.onDestroy(_context);

      _mainRenderer.onDestroy(_context);
      _busyRenderer.onDestroy(_context);
      _errorRenderer.onDestroy(_context);

      _downloader.onDestroy(_context);
      _storage.onDestroy(_context);
   }


   public final GL getGL() {
      return _gl;
   }


   public final Camera getCurrentCamera() {
      return _currentCamera;
   }


   public final Camera getNextCamera() {
      return _nextCamera;
   }


   public final void setUserData(final WidgetUserData userData) {
      if (_userData != null) {
         _userData.dispose();
      }

      _userData = userData;
      if (_userData != null) {
         _userData.setWidget(this);
      }
   }


   public final WidgetUserData getUserData() {
      return _userData;
   }


   public final void addPeriodicalTask(final PeriodicalTask periodicalTask) {
      _periodicalTasks.add(periodicalTask);
   }


   public final void addPeriodicalTask(final TimeInterval interval,
                                       final GTask task) {
      addPeriodicalTask(new PeriodicalTask(interval, task));
   }


   public final void resetPeriodicalTasksTimeouts() {
      final int periodicalTasksCount = _periodicalTasks.size();
      for (int i = 0; i < periodicalTasksCount; i++) {
         final PeriodicalTask pt = _periodicalTasks.get(i);
         pt.resetTimeout();
      }
   }


   public final void setCameraPosition(final Geodetic3D position) {
      getNextCamera().setGeodeticPosition(position);
      _initialCameraPositionHasBeenSet = true;
   }


   public final void setCameraHeading(final Angle angle) {
      getNextCamera().setHeading(angle);
   }


   public final void setCameraPitch(final Angle angle) {
      getNextCamera().setPitch(angle);
   }


   public final void setAnimatedCameraPosition(final Geodetic3D position,
                                               final Angle heading) {
      setAnimatedCameraPosition(position, heading, Angle.zero());
   }


   public final void setAnimatedCameraPosition(final Geodetic3D position) {
      setAnimatedCameraPosition(position, Angle.zero(), Angle.zero());
   }


   public final void setAnimatedCameraPosition(final Geodetic3D position,
                                               final Angle heading,
                                               final Angle pitch) {
      setAnimatedCameraPosition(TimeInterval.fromSeconds(3), position, heading, pitch);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D position,
                                               final Angle heading,
                                               final Angle pitch,
                                               final boolean linearTiming) {
      setAnimatedCameraPosition(interval, position, heading, pitch, linearTiming, false);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D position,
                                               final Angle heading,
                                               final Angle pitch) {
      setAnimatedCameraPosition(interval, position, heading, pitch, false, false);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D position,
                                               final Angle heading) {
      setAnimatedCameraPosition(interval, position, heading, Angle.zero(), false, false);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D position) {
      setAnimatedCameraPosition(interval, position, Angle.zero(), Angle.zero(), false, false);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D position,
                                               final Angle heading,
                                               final Angle pitch,
                                               final boolean linearTiming,
                                               final boolean linearHeight) {
      final Geodetic3D fromPosition = _nextCamera.getGeodeticPosition();
      final Angle fromHeading = _nextCamera.getHeading();
      final Angle fromPitch = _nextCamera.getPitch();

      setAnimatedCameraPosition(interval, fromPosition, position, fromHeading, heading, fromPitch, pitch, linearTiming,
               linearHeight);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D fromPosition,
                                               final Geodetic3D toPosition,
                                               final Angle fromHeading,
                                               final Angle toHeading,
                                               final Angle fromPitch,
                                               final Angle toPitch,
                                               final boolean linearTiming) {
      setAnimatedCameraPosition(interval, fromPosition, toPosition, fromHeading, toHeading, fromPitch, toPitch, linearTiming,
               false);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D fromPosition,
                                               final Geodetic3D toPosition,
                                               final Angle fromHeading,
                                               final Angle toHeading,
                                               final Angle fromPitch,
                                               final Angle toPitch) {
      setAnimatedCameraPosition(interval, fromPosition, toPosition, fromHeading, toHeading, fromPitch, toPitch, false, false);
   }


   public final void setAnimatedCameraPosition(final TimeInterval interval,
                                               final Geodetic3D fromPosition,
                                               final Geodetic3D toPosition,
                                               final Angle fromHeading,
                                               final Angle toHeading,
                                               final Angle fromPitch,
                                               final Angle toPitch,
                                               final boolean linearTiming,
                                               final boolean linearHeight) {
      double finalLatInDegrees = toPosition._latitude._degrees;
      double finalLonInDegrees = toPosition._longitude._degrees;

      //Fixing final latitude
      while (finalLatInDegrees > 90) {
         finalLatInDegrees -= 360;
      }
      while (finalLatInDegrees < -90) {
         finalLatInDegrees += 360;
      }

      //Fixing final longitude
      while (finalLonInDegrees > 360) {
         finalLonInDegrees -= 360;
      }
      while (finalLonInDegrees < 0) {
         finalLonInDegrees += 360;
      }
      if (Math.abs(finalLonInDegrees - fromPosition._longitude._degrees) > 180) {
         finalLonInDegrees -= 360;
      }

      final Geodetic3D finalToPosition = Geodetic3D.fromDegrees(finalLatInDegrees, finalLonInDegrees, toPosition._height);

      stopCameraAnimation();

      _effectsScheduler.startEffect(new CameraGoToPositionEffect(interval, fromPosition, finalToPosition, fromHeading, toHeading,
               fromPitch, toPitch, linearTiming, linearHeight), _nextCamera.getEffectTarget());
   }


   public final void stopCameraAnimation() {
      final EffectTarget target = _nextCamera.getEffectTarget();
      _effectsScheduler.cancelAllEffectsFor(target);
   }


   //  void resetCameraPosition();

   public final CameraRenderer getCameraRenderer() {
      return _cameraRenderer;
   }


   public final G3MContext getG3MContext() {
      return _context;
   }


   //void G3MWidget::resetCameraPosition() {
   //  getNextCamera()->resetPosition();
   //}

   public final void setBackgroundColor(final Color backgroundColor) {
      if (_backgroundColor != null) {
         _backgroundColor.dispose();
      }

      _backgroundColor = new Color(backgroundColor);
   }


   public final PlanetRenderer getPlanetRenderer() {
      return _mainRenderer.getPlanetRenderer();
   }


   public final void setShownSector(final Sector sector) {
      getPlanetRenderer().setRenderedSector(sector);
      _initialCameraPositionHasBeenSet = false;
   }

   private final IStorage                            _storage;
   private final IDownloader                         _downloader;
   private final IThreadUtils                        _threadUtils;
   private final ICameraActivityListener             _cameraActivityListener;

   private final FrameTasksExecutor                  _frameTasksExecutor;
   private final GL                                  _gl;
   private final Planet                              _planet;                                                            // REMOVED FINAL WORD BY CONVERSOR RULE

   private final CameraRenderer                      _cameraRenderer;
   private final Renderer                            _mainRenderer;
   private final Renderer                            _busyRenderer;
   private final ErrorRenderer                       _errorRenderer;
   private boolean                                   _mainRendererReady;
   private Renderer                                  _selectedRenderer;

   private final EffectsScheduler                    _effectsScheduler;

   private java.util.ArrayList<ICameraConstrainer>   _cameraConstrainers = new java.util.ArrayList<ICameraConstrainer>();

   private final Camera                              _currentCamera;
   private final Camera                              _nextCamera;
   private final TexturesHandler                     _texturesHandler;

   private Color                                     _backgroundColor;

   private final ITimer                              _timer;
   private int                                       _renderCounter;
   private int                                       _totalRenderTime;
   private final boolean                             _logFPS;
   private final boolean                             _logDownloaderStatistics;
   private String                                    _lastCacheStatistics;

   private ITimer                                    _renderStatisticsTimer;

   private WidgetUserData                            _userData;

   private GInitializationTask                       _initializationTask;
   private final boolean                             _autoDeleteInitializationTask;

   private final java.util.ArrayList<PeriodicalTask> _periodicalTasks    = new java.util.ArrayList<PeriodicalTask>();

   private int                                       _width;
   private int                                       _height;

   private final G3MContext                          _context;

   private boolean                                   _paused;
   private boolean                                   _initializationTaskWasRun;
   private boolean                                   _initializationTaskReady;

   private boolean                                   _clickOnProcess;

   private final GPUProgramManager                   _gpuProgramManager;

   private final SurfaceElevationProvider            _surfaceElevationProvider;

   private final SceneLighting                       _sceneLighting;
   private GLState                                   _rootState;

   private final InitialCameraPositionProvider       _initialCameraPositionProvider;
   private boolean                                   _initialCameraPositionHasBeenSet;


   private G3MWidget(final GL gl,
                     final IStorage storage,
                     final IDownloader downloader,
                     final IThreadUtils threadUtils,
                     final ICameraActivityListener cameraActivityListener,
                     final Planet planet,
                     final java.util.ArrayList<ICameraConstrainer> cameraConstrainers,
                     final CameraRenderer cameraRenderer,
                     final Renderer mainRenderer,
                     final Renderer busyRenderer,
                     final ErrorRenderer errorRenderer,
                     final Color backgroundColor,
                     final boolean logFPS,
                     final boolean logDownloaderStatistics,
                     final GInitializationTask initializationTask,
                     final boolean autoDeleteInitializationTask,
                     final java.util.ArrayList<PeriodicalTask> periodicalTasks,
                     final GPUProgramManager gpuProgramManager,
                     final SceneLighting sceneLighting,
                     final InitialCameraPositionProvider initialCameraPositionProvider) {
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
      _width = 1;
      _height = 1;
      _currentCamera = new Camera(_width, _height);
      _nextCamera = new Camera(_width, _height);
      _backgroundColor = new Color(backgroundColor);
      _timer = IFactory.instance().createTimer();
      _renderCounter = 0;
      _totalRenderTime = 0;
      _logFPS = logFPS;
      _mainRendererReady = false;
      _selectedRenderer = null;
      _renderStatisticsTimer = null;
      _logDownloaderStatistics = logDownloaderStatistics;
      _userData = null;
      _initializationTask = initializationTask;
      _autoDeleteInitializationTask = autoDeleteInitializationTask;
      _surfaceElevationProvider = mainRenderer.getSurfaceElevationProvider();
      _context = new G3MContext(IFactory.instance(), IStringUtils.instance(), threadUtils, ILogger.instance(),
               IMathUtils.instance(), IJSONParser.instance(), _planet, downloader, _effectsScheduler, storage,
               mainRenderer.getSurfaceElevationProvider());
      _paused = false;
      _initializationTaskWasRun = false;
      _initializationTaskReady = true;
      _clickOnProcess = false;
      _gpuProgramManager = gpuProgramManager;
      _sceneLighting = sceneLighting;
      _rootState = null;
      _initialCameraPositionProvider = initialCameraPositionProvider;
      _initialCameraPositionHasBeenSet = false;
      _effectsScheduler.initialize(_context);
      _cameraRenderer.initialize(_context);
      _mainRenderer.initialize(_context);
      _busyRenderer.initialize(_context);
      _errorRenderer.initialize(_context);
      _currentCamera.initialize(_context);
      _nextCamera.initialize(_context);

      if (_threadUtils != null) {
         _threadUtils.initialize(_context);
      }

      if (_storage != null) {
         _storage.initialize(_context);
      }

      if (_downloader != null) {
         _downloader.initialize(_context, _frameTasksExecutor);
         _downloader.start();
      }

      for (int i = 0; i < periodicalTasks.size(); i++) {
         addPeriodicalTask(periodicalTasks.get(i));
      }
   }


   private void notifyTouchEvent(final G3MEventContext ec,
                                 final TouchEvent touchEvent) {
      if (_mainRendererReady) {
         boolean handled = false;
         if (_mainRenderer.isEnable()) {
            handled = _mainRenderer.onTouchEvent(ec, touchEvent);
         }

         if (!handled) {
            handled = _cameraRenderer.onTouchEvent(ec, touchEvent);
            if (handled) {
               if (_cameraActivityListener != null) {
                  _cameraActivityListener.touchEventHandled();
               }
            }
         }
      }
      else {
         _busyRenderer.onTouchEvent(ec, touchEvent);
      }
   }

}
