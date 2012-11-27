package org.glob3.mobile.generated; 
public class G3MWidget
{

  public static void initSingletons(final ILogger logger, final IFactory factory, final IStringUtils stringUtils, final IStringBuilder stringBuilder, final IMathUtils mathUtils, final IJSONParser jsonParser)
  {
	if (ILogger.instance() == null)
	{
	  ILogger.setInstance(logger);
	  IFactory.setInstance(factory);
	  IStringUtils.setInstance(stringUtils);
	  IStringBuilder.setInstance(stringBuilder);
	  IMathUtils.setInstance(mathUtils);
	  IJSONParser.setInstance(jsonParser);
	}
	else
	{
	  ILogger.instance().logWarning("Singletons already set");
	}
  }

  public static G3MWidget create(INativeGL nativeGL, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, Renderer busyRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks)
  {
	return new G3MWidget(nativeGL, storage, downloader, threadUtils, planet, cameraConstrainers, cameraRenderer, mainRenderer, busyRenderer, backgroundColor, logFPS, logDownloaderStatistics, initializationTask, autoDeleteInitializationTask, periodicalTasks);
  }

  public void dispose()
  {
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
  
	if (_downloader != null)
	{
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
  
	if (_frameTasksExecutor != null) {
      _frameTasksExecutor.dispose();
   }
  
  
	if (_context != null) {
      _context.dispose();
   }
  }

  public final void render(final int width, final int height)
  {
     if (_paused)
     {
       return;
     }
	  if (((_width != width) || (_height != height)) && _mainRendererReady)
	  {
		  _width = width;
		  _height = height;
  
		  onResizeViewportEvent(_width, _height);
	  }
  
	_timer.start();
	_renderCounter++;
  
	//Start periodical task
	final int periodicalTasksCount = _periodicalTasks.size();
	for (int i = 0; i < periodicalTasksCount; i++)
	{
	  final PeriodicalTask pt = _periodicalTasks.get(i);
	  pt.executeIfNecessary(_context);
	}
  
	// give to the CameraContrainers the opportunity to change the nextCamera
	final int cameraConstrainersCount = _cameraConstrainers.size();
	for (int i = 0; i< cameraConstrainersCount; i++)
	{
	  final ICameraConstrainer constrainer = _cameraConstrainers.get(i);
	  constrainer.onCameraChange(_planet, _currentCamera, _nextCamera);
	}
	_currentCamera.copyFrom(_nextCamera);
  
	final G3MRenderContext rc = new G3MRenderContext(_frameTasksExecutor, IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _gl, _currentCamera, _nextCamera, _texturesHandler, _textureBuilder, _downloader, _effectsScheduler, IFactory.instance().createTimer(), _storage);
  
	_mainRendererReady = _mainRenderer.isReadyToRender(rc);
  
	if (_mainRendererReady)
	{
	  if (_initializationTask != null)
	  {
		_initializationTask.run(_context);
		if (_autoDeleteInitializationTask)
		{
		  if (_initializationTask != null) {
         _initializationTask.dispose();
      }
		}
		_initializationTask = null;
	  }
	}
  
	_effectsScheduler.doOneCyle(rc);
  
	_frameTasksExecutor.doPreRenderCycle(rc);
  
  
	final Renderer selectedRenderer = _mainRendererReady ? _mainRenderer : _busyRenderer;
	if (selectedRenderer != _selectedRenderer)
	{
	  if (_selectedRenderer != null)
	  {
		_selectedRenderer.stop();
	  }
	  _selectedRenderer = selectedRenderer;
	  _selectedRenderer.start();
	}
  
	_gl.clearScreen(_backgroundColor);
  
	if (_mainRendererReady)
	{
	  _cameraRenderer.render(rc);
	}
  
	if (_selectedRenderer.isEnable())
	{
	  _selectedRenderer.render(rc);
	}
  
  
	final java.util.ArrayList<OrderedRenderable> orderedRenderables = rc.getSortedOrderedRenderables();
	if (orderedRenderables != null)
	{
	  final int orderedRenderablesCount = orderedRenderables.size();
	  for (int i = 0; i < orderedRenderablesCount; i++)
	  {
		final OrderedRenderable orderedRenderable = orderedRenderables.get(i);
		orderedRenderable.render(rc);
		if (orderedRenderable != null) {
         orderedRenderable.dispose();
      }
	  }
	}
  
	//  _frameTasksExecutor->doPostRenderCycle(&rc);
  
	final TimeInterval elapsedTime = _timer.elapsedTime();
	if (elapsedTime.milliseconds() > 100)
	{
	  ILogger.instance().logWarning("Frame took too much time: %dms", elapsedTime.milliseconds());
	}
  
	if (_logFPS)
	{
	  _totalRenderTime += elapsedTime.milliseconds();
  
	  if ((_renderStatisticsTimer == null) || (_renderStatisticsTimer.elapsedTime().seconds() > 2))
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

  public final void onTouchEvent(final TouchEvent touchEvent)
  {
	if (_mainRendererReady)
	{
	  final G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage);
  
	  boolean handled = false;
	  if (_mainRenderer.isEnable())
	  {
		handled = _mainRenderer.onTouchEvent(ec, touchEvent);
	  }
  
	  if (!handled)
	  {
		_cameraRenderer.onTouchEvent(ec, touchEvent);
	  }
	}
  }

  public final void onResizeViewportEvent(final int width, final int height)
  {
	if (_mainRendererReady)
	{
	  final G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage);
  
	  _nextCamera.resizeViewport(width, height);
  
		 _nextCamera.resizeViewport(width, height);
  
	  _cameraRenderer.onResizeViewportEvent(ec, width, height);
  
	  if (_mainRenderer.isEnable())
	  {
		_mainRenderer.onResizeViewportEvent(ec, width, height);
	  }
	}
  }

  public final void onPause()
  {
	_paused = true;
  
	_threadUtils.onPause(_context);
  
	_effectsScheduler.onPause(_context);
  
	_mainRenderer.onPause(_context);
	_busyRenderer.onPause(_context);
  
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
  
	_effectsScheduler.onResume(_context);
  
	_threadUtils.onResume(_context);
  }

  public final void onDestroy()
  {
	_threadUtils.onDestroy(_context);
  
	_effectsScheduler.onDestroy(_context);
  
	_mainRenderer.onDestroy(_context);
	_busyRenderer.onDestroy(_context);
  
	_downloader.onDestroy(_context);
	_storage.onDestroy(_context);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GL* getGL() const
  public final GL getGL()
  {
	return _gl;
  }

  //  const Camera* getCurrentCamera() const {
  //    return _currentCamera;
  //  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Camera* getNextCamera() const
  public final Camera getNextCamera()
  {
	return _nextCamera;
  }

  public final void setUserData(final UserData userData)
  {
	  if (_userData != null) {
      _userData.dispose();
   }

	_userData = userData;
	if (_userData != null)
	{
	  _userData.setWidget(this);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: UserData* getUserData() const
  public final UserData getUserData()
  {
	return _userData;
  }

  public final void addPeriodicalTask(final PeriodicalTask periodicalTask)
  {
	_periodicalTasks.add(periodicalTask);
  }

  public final void addPeriodicalTask(final TimeInterval interval, final GTask task)
  {
	addPeriodicalTask(new PeriodicalTask(interval, task));
  }


  public final void setCameraPosition(final Geodetic3D position)
  {
	getNextCamera().setPosition(position);
  }

  public final void setCameraHeading(final Angle angle)
  {
	getNextCamera().setHeading(angle);
  }

  public final void setCameraPitch(final Angle angle)
  {
	getNextCamera().setPitch(angle);
  }

  public final void setAnimatedCameraPosition(final Geodetic3D position)
  {
	setAnimatedCameraPosition(position, TimeInterval.fromSeconds(3));
  }

  public final void setAnimatedCameraPosition(final Geodetic3D position, final TimeInterval interval)
  {
  
	final Geodetic3D startPosition = _planet.toGeodetic3D(_currentCamera.getCartesianPosition());
  
	double finalLat = position.latitude()._degrees;
	double finalLon = position.longitude()._degrees;
  
	//Fixing final latitude
	while (finalLat > 90)
	{
	  finalLat -= 360;
	}
	while (finalLat < -90)
	{
	  finalLat += 360;
	}
  
	//Fixing final longitude
	while (finalLon > 360)
	{
	  finalLon -= 360;
	}
	while (finalLon < 0)
	{
	  finalLon += 360;
	}
	if (Math.abs(finalLon - startPosition.longitude()._degrees) > 180)
	{
	  finalLon -= 360;
	}
  
	final Geodetic3D endPosition = Geodetic3D.fromDegrees(finalLat, finalLon, position.height());
  
	final EffectTarget target = _nextCamera.getEffectTarget();
	_effectsScheduler.cancellAllEffectsFor(target);
  
	_effectsScheduler.startEffect(new GoToPositionEffect(interval, startPosition, endPosition), target);
  }

  public final void resetCameraPosition()
  {
	getNextCamera().resetPosition();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: CameraRenderer* getCameraRenderer() const
  public final CameraRenderer getCameraRenderer()
  {
	return _cameraRenderer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const G3MContext* getG3MContext() const
  public final G3MContext getG3MContext()
  {
	return _context;
  }

  private IStorage _storage;
  private IDownloader _downloader;
  private IThreadUtils _threadUtils;

  private FrameTasksExecutor _frameTasksExecutor;
  private GL _gl;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE

  private CameraRenderer _cameraRenderer;
  private Renderer _mainRenderer;
  private Renderer _busyRenderer;
  private boolean _mainRendererReady;
  private Renderer _selectedRenderer;

  private EffectsScheduler _effectsScheduler;

  private java.util.ArrayList<ICameraConstrainer> _cameraConstrainers = new java.util.ArrayList<ICameraConstrainer>();

  private Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;
  private TextureBuilder _textureBuilder;
  private final Color _backgroundColor ;

  private ITimer _timer;
  private int _renderCounter;
  private int _totalRenderTime;
  private final boolean _logFPS;
  private final boolean _logDownloaderStatistics;
  private String _lastCacheStatistics;

  private ITimer _renderStatisticsTimer;

  private UserData _userData;

  private GTask _initializationTask;
  private boolean _autoDeleteInitializationTask;

  private final java.util.ArrayList<PeriodicalTask> _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();

	private int _width;
	private int _height;

  private void initializeGL()
  {
	_gl.enableDepthTest();
  
	_gl.enableCullFace(GLCullFace.back());
  }

  private final G3MContext _context;

  private boolean _paused;

  private G3MWidget(final INativeGL nativeGL, final IStorage storage, final IDownloader downloader, final IThreadUtils threadUtils, final Planet planet, final java.util.ArrayList<ICameraConstrainer> cameraConstrainers, final CameraRenderer cameraRenderer, final Renderer mainRenderer, final Renderer busyRenderer, final Color backgroundColor, final boolean logFPS, final boolean logDownloaderStatistics, final GTask initializationTask, final boolean autoDeleteInitializationTask, final java.util.ArrayList<PeriodicalTask> periodicalTasks)
  {
	  _frameTasksExecutor = new FrameTasksExecutor();
	  _effectsScheduler = new EffectsScheduler();
	  _gl = new GL(nativeGL);
	  _downloader = downloader;
	  _storage = storage;
	  _threadUtils = threadUtils;
	  _texturesHandler = new TexturesHandler(_gl, false);
	  _textureBuilder = new CPUTextureBuilder();
	  _planet = planet;
	  _cameraConstrainers = cameraConstrainers;
	  _cameraRenderer = cameraRenderer;
	  _mainRenderer = mainRenderer;
	  _busyRenderer = busyRenderer;
	  _width = 1;
	  _height = 1;
	  _currentCamera = new Camera(_width, _height);
	  _nextCamera = new Camera(_width, _height);
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
	  _context = new G3MContext(IFactory.instance(), IStringUtils.instance(), threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, downloader, _effectsScheduler, storage);
	  _paused = false;
	initializeGL();
  
	_effectsScheduler.initialize(_context);
	_cameraRenderer.initialize(_context);
	_mainRenderer.initialize(_context);
	_busyRenderer.initialize(_context);
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
	  _downloader.initialize(_context);
	  _downloader.start();
	}
  
	for (int i = 0; i < periodicalTasks.size(); i++)
	{
	  addPeriodicalTask(periodicalTasks.get(i));
	}
  }

}