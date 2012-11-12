

package org.glob3.mobile.generated;

public class G3MWidget {

   public static void initSingletons(final ILogger logger,
                                     final IFactory factory,
                                     final IStringUtils stringUtils,
                                     final IThreadUtils threadUtils,
                                     final IStringBuilder stringBuilder,
                                     final IMathUtils mathUtils,
                                     final IJSONParser jsonParser,
                                     final IStorage storage,
                                     final IDownloader downloader) {
      if (ILogger.instance() == null) {
         ILogger.setInstance(logger);
         IFactory.setInstance(factory);
         IStringUtils.setInstance(stringUtils);
         IThreadUtils.setInstance(threadUtils);
         IStringBuilder.setInstance(stringBuilder);
         IMathUtils.setInstance(mathUtils);
         IJSONParser.setInstance(jsonParser);
         IStorage.setInstance(storage);
         IDownloader.setInstance(downloader);
      }
      else {
         ILogger.instance().logWarning("Singletons already set");
      }
   }


   public static G3MWidget create(final INativeGL nativeGL,
                                  final Planet planet,
                                  final java.util.ArrayList<ICameraConstrainer> cameraConstrainers,
                                  final CameraRenderer cameraRenderer,
                                  final Renderer mainRenderer,
                                  final Renderer busyRenderer,
                                  final int width,
                                  final int height,
                                  final Color backgroundColor,
                                  final boolean logFPS,
                                  final boolean logDownloaderStatistics,
                                  final GTask initializationTask,
                                  final boolean autoDeleteInitializationTask,
                                  final java.util.ArrayList<PeriodicalTask> periodicalTasks) {

      return new G3MWidget(nativeGL, planet, cameraConstrainers, cameraRenderer, mainRenderer, busyRenderer, width, height,
               backgroundColor, logFPS, logDownloaderStatistics, initializationTask, autoDeleteInitializationTask,
               periodicalTasks);
   }


   public void dispose() {
      if (_userData != null) {
         _userData.dispose();
      }

      if (_gl != null) {
         _gl.dispose();
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

      if (IDownloader.instance() != null) {
         IDownloader.instance().stop();
      }

      if (_frameTasksExecutor != null) {
         _frameTasksExecutor.dispose();
      }


      if (_initializationContext != null) {
         _initializationContext.dispose();
      }
   }


   public final void render() {
      _timer.start();
      _renderCounter++;

      //Start periodical task
      for (int i = 0; i < _periodicalTasks.size(); i++) {
         final PeriodicalTask pt = _periodicalTasks.get(i);
         pt.executeIfNecessary();
      }

      // give to the CameraContrainers the opportunity to change the nextCamera
      for (int i = 0; i < _cameraConstrainers.size(); i++) {
         final ICameraConstrainer constrainer = _cameraConstrainers.get(i);
         constrainer.onCameraChange(_planet, _currentCamera, _nextCamera);
      }
      _currentCamera.copyFrom(_nextCamera);


      if (_initializationTask != null) {
         _initializationTask.run();
         if (_autoDeleteInitializationTask) {
            if (_initializationTask != null) {
               _initializationTask.dispose();
            }
         }
         _initializationTask = null;
      }

      final RenderContext rc = new RenderContext(_frameTasksExecutor, IFactory.instance(), IStringUtils.instance(),
               IThreadUtils.instance(), ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _gl,
               _currentCamera, _nextCamera, _texturesHandler, _textureBuilder, IDownloader.instance(), _effectsScheduler,
               IFactory.instance().createTimer(), IStorage.instance());

      _effectsScheduler.doOneCyle(rc);

      _frameTasksExecutor.doPreRenderCycle(rc);

      _mainRendererReady = _mainRenderer.isReadyToRender(rc);

      final Renderer selectedRenderer = _mainRendererReady ? _mainRenderer : _busyRenderer;
      if (selectedRenderer != _selectedRenderer) {
         if (_selectedRenderer != null) {
            _selectedRenderer.stop();
         }
         _selectedRenderer = selectedRenderer;
         _selectedRenderer.start();
      }

      _gl.clearScreen(_backgroundColor);

      if (_mainRendererReady) {
         _cameraRenderer.render(rc);
      }

      if (_selectedRenderer.isEnable()) {
         _selectedRenderer.render(rc);
      }

      //  _frameTasksExecutor->doPostRenderCycle(&rc);

      final TimeInterval elapsedTime = _timer.elapsedTime();
      if (elapsedTime.milliseconds() > 100) {
         ILogger.instance().logWarning("Frame took too much time: %dms", elapsedTime.milliseconds());
      }

      if (_logFPS) {
         _totalRenderTime += elapsedTime.milliseconds();

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

         if (IDownloader.instance() != null) {
            cacheStatistics = IDownloader.instance().statistics();
         }

         if (!_lastCacheStatistics.equals(cacheStatistics)) {
            ILogger.instance().logInfo("%s", cacheStatistics);
            _lastCacheStatistics = cacheStatistics;
         }
      }

   }


   public final void onTouchEvent(final TouchEvent touchEvent) {
      if (_mainRendererReady) {
         final EventContext ec = new EventContext(IFactory.instance(), IStringUtils.instance(), IThreadUtils.instance(),
                  ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, IDownloader.instance(),
                  _effectsScheduler, IStorage.instance());

         boolean handled = false;
         if (_mainRenderer.isEnable()) {
            handled = _mainRenderer.onTouchEvent(ec, touchEvent);
         }

         if (!handled) {
            _cameraRenderer.onTouchEvent(ec, touchEvent);
         }
      }
   }


   public final void onResizeViewportEvent(final int width,
                                           final int height) {
      if (_mainRendererReady) {
         final EventContext ec = new EventContext(IFactory.instance(), IStringUtils.instance(), IThreadUtils.instance(),
                  ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, IDownloader.instance(),
                  _effectsScheduler, IStorage.instance());

         _cameraRenderer.onResizeViewportEvent(ec, width, height);

         if (_mainRenderer.isEnable()) {
            _mainRenderer.onResizeViewportEvent(ec, width, height);
         }
      }
   }


   public final void onPause() {
      _mainRenderer.onPause(_initializationContext);
      _busyRenderer.onPause(_initializationContext);

      _effectsScheduler.onPause(_initializationContext);

      if (IDownloader.instance() != null) {
         IDownloader.instance().onPause(_initializationContext);
      }
   }


   public final void onResume() {
      _mainRenderer.onResume(_initializationContext);
      _busyRenderer.onResume(_initializationContext);

      _effectsScheduler.onResume(_initializationContext);

      if (IDownloader.instance() != null) {
         IDownloader.instance().onResume(_initializationContext);
      }
   }


   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: GL* getGL() const
   public final GL getGL() {
      return _gl;
   }


   //  const Camera* getCurrentCamera() const {
   //    return _currentCamera;
   //  }

   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: Camera* getNextCamera() const
   public final Camera getNextCamera() {
      return _nextCamera;
   }


   public final void setUserData(final UserData userData) {
      if (_userData != null) {
         _userData.dispose();
      }

      _userData = userData;
      if (_userData != null) {
         _userData.setWidget(this);
      }
   }


   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: UserData* getUserData() const
   public final UserData getUserData() {
      return _userData;
   }


   public final void addPeriodicalTask(final PeriodicalTask periodicalTask) {
      _periodicalTasks.add(periodicalTask);
   }


   public final void addPeriodicalTask(final TimeInterval interval,
                                       final GTask task) {
      addPeriodicalTask(new PeriodicalTask(interval, task));
   }


   public final void setCameraPosition(final Geodetic3D position) {
      getNextCamera().setPosition(position);
   }


   public final void setCameraHeading(final Angle angle) {
      getNextCamera().setHeading(angle);
   }


   public final void setCameraPitch(final Angle angle) {
      getNextCamera().setPitch(angle);
   }


   public final void setAnimatedCameraPosition(final Geodetic3D position) {
      setAnimatedCameraPosition(position, TimeInterval.fromSeconds(3));
   }


   public final void setAnimatedCameraPosition(final Geodetic3D position,
                                               final TimeInterval interval) {

      final Geodetic3D startPosition = _planet.toGeodetic3D(_currentCamera.getCartesianPosition());

      double finalLat = position.latitude()._degrees;
      double finalLon = position.longitude()._degrees;

      //Fixing final latitude
      while (finalLat > 90) {
         finalLat -= 360;
      }
      while (finalLat < -90) {
         finalLat += 360;
      }

      //Fixing final longitude
      while (finalLon > 360) {
         finalLon -= 360;
      }
      while (finalLon < 0) {
         finalLon += 360;
      }
      if (Math.abs(finalLon - startPosition.longitude()._degrees) > 180) {
         finalLon -= 360;
      }

      final Geodetic3D endPosition = Geodetic3D.fromDegrees(finalLat, finalLon, position.height());

      final EffectTarget target = _nextCamera.getEffectTarget();
      _effectsScheduler.cancellAllEffectsFor(target);

      _effectsScheduler.startEffect(new GoToPositionEffect(interval, startPosition, endPosition), target);
   }


   public final void resetCameraPosition() {
      getNextCamera().resetPosition();
   }


   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: CameraRenderer* getCameraRenderer() const
   public final CameraRenderer getCameraRenderer() {
      return _cameraRenderer;
   }

   private final FrameTasksExecutor                  _frameTasksExecutor;
   private final GL                                  _gl;
   private final Planet                              _planet;                                                            // REMOVED FINAL WORD BY CONVERSOR RULE

   private final CameraRenderer                      _cameraRenderer;
   private final Renderer                            _mainRenderer;
   private final Renderer                            _busyRenderer;
   private boolean                                   _mainRendererReady;
   private Renderer                                  _selectedRenderer;

   private final EffectsScheduler                    _effectsScheduler;

   private java.util.ArrayList<ICameraConstrainer>   _cameraConstrainers = new java.util.ArrayList<ICameraConstrainer>();

   private final Camera                              _currentCamera;
   private final Camera                              _nextCamera;
   private final TexturesHandler                     _texturesHandler;
   private final TextureBuilder                      _textureBuilder;
   private final Color                               _backgroundColor;

   private final ITimer                              _timer;
   private int                                       _renderCounter;
   private int                                       _totalRenderTime;
   private final boolean                             _logFPS;
   private final boolean                             _logDownloaderStatistics;
   private String                                    _lastCacheStatistics;

   private ITimer                                    _renderStatisticsTimer;

   private UserData                                  _userData;

   private GTask                                     _initializationTask;
   private final boolean                             _autoDeleteInitializationTask;

   private final java.util.ArrayList<PeriodicalTask> _periodicalTasks    = new java.util.ArrayList<PeriodicalTask>();


   private void initializeGL() {
      _gl.enableDepthTest();

      _gl.enableCullFace(GLCullFace.back());
   }

   private final InitializationContext _initializationContext;


   private G3MWidget(final INativeGL nativeGL,
                     final Planet planet,
                     final java.util.ArrayList<ICameraConstrainer> cameraConstrainers,
                     final CameraRenderer cameraRenderer,
                     final Renderer mainRenderer,
                     final Renderer busyRenderer,
                     final int width,
                     final int height,
                     final Color backgroundColor,
                     final boolean logFPS,
                     final boolean logDownloaderStatistics,
                     final GTask initializationTask,
                     final boolean autoDeleteInitializationTask,
                     final java.util.ArrayList<PeriodicalTask> periodicalTasks) {
      _frameTasksExecutor = new FrameTasksExecutor();
      _effectsScheduler = new EffectsScheduler();
      _gl = new GL(nativeGL);
      _texturesHandler = new TexturesHandler(_gl, false);
      _textureBuilder = new CPUTextureBuilder();
      _planet = planet;
      _cameraConstrainers = cameraConstrainers;
      _cameraRenderer = cameraRenderer;
      _mainRenderer = mainRenderer;
      _busyRenderer = busyRenderer;
      _currentCamera = new Camera(width, height);
      _nextCamera = new Camera(width, height);
      _backgroundColor = backgroundColor;
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
      _initializationContext = new InitializationContext(IFactory.instance(), IStringUtils.instance(), IThreadUtils.instance(),
               ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, IDownloader.instance(),
               _effectsScheduler, IStorage.instance());
      initializeGL();

      _effectsScheduler.initialize(_initializationContext);
      _cameraRenderer.initialize(_initializationContext);
      _mainRenderer.initialize(_initializationContext);
      _busyRenderer.initialize(_initializationContext);
      _currentCamera.initialize(_initializationContext);
      _nextCamera.initialize(_initializationContext);

      if (IDownloader.instance() != null) {
         IDownloader.instance().start();
      }

      for (int i = 0; i < periodicalTasks.size(); i++) {
         addPeriodicalTask(periodicalTasks.get(i));
      }
   }
}
