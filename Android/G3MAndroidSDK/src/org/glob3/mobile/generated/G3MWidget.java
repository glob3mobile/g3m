package org.glob3.mobile.generated; 
//
//  G3MWidget.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  G3MWidget.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Renderer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TouchEvent;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Planet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ILogger;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFactory;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class GL;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TexturesHandler;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Downloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Camera;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ITimer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class EffectsScheduler;



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class ICameraConstrainer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrameTasksExecutor;


public class G3MWidget
{

  public static G3MWidget create(FrameTasksExecutor frameTasksExecutor, IFactory factory, ILogger logger, GL gl, TexturesHandler texturesHandler, IDownloader downloader, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstraint, Renderer renderer, Renderer busyRenderer, EffectsScheduler scheduler, int width, int height, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics)
  {
	if (logger != null)
	{
	  logger.logInfo("Creating G3MWidget...");
	}
  
	ILogger.setInstance(logger);
  
	return new G3MWidget(frameTasksExecutor, factory, logger, gl, texturesHandler, downloader, planet, cameraConstraint, renderer, busyRenderer, scheduler, width, height, backgroundColor, logFPS, logDownloaderStatistics);
  }

  public void dispose()
  {
	if (_factory != null)
		_factory.dispose();
	if (_logger != null)
		_logger.dispose();
	if (_gl != null)
		_gl.dispose();
	if (_renderer != null)
		_renderer.dispose();
	if (_busyRenderer != null)
		_busyRenderer.dispose();
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
  
	_downloader = null;
  
  
	if (_frameTasksExecutor != null)
		_frameTasksExecutor.dispose();
  }

  public final int render()
  {
	_timer.start();
	_renderCounter++;
  
  
	// copy next camera to current camera
	boolean acceptedCamera = true;
	for (int n = 0; n < _cameraConstraint.size(); n++)
	{
	  if (!_cameraConstraint.get(n).acceptsCamera(_nextCamera, _planet))
	  {
		acceptedCamera = false;
		break;
	  }
	}
	if (acceptedCamera)
	{
	  _currentCamera.copyFrom(_nextCamera);
	}
	else
	{
	  _nextCamera.copyFrom(_currentCamera);
	}
  
  
	// create RenderContext
	RenderContext rc = new RenderContext(_frameTasksExecutor, _factory, _logger, _planet, _gl, _currentCamera, _nextCamera, _texturesHandler, _downloader, _effectsScheduler, _factory.createTimer());
  
	_effectsScheduler.doOneCyle(rc);
  
	_frameTasksExecutor.doPreRenderCycle(rc);
  
	_rendererReady = _renderer.isReadyToRender(rc);
  
	Renderer selectedRenderer = _rendererReady ? _renderer : _busyRenderer;
	if (selectedRenderer != _selectedRenderer)
	{
	  if (_selectedRenderer != null)
	  {
		_selectedRenderer.stop();
	  }
	  _selectedRenderer = selectedRenderer;
	  _selectedRenderer.start();
	}
  
  
	// Clear the scene
	_gl.clearScreen(_backgroundColor);
  
	final int timeToRedraw = _selectedRenderer.render(rc);
  
  //  _frameTasksExecutor->doPostRenderCycle(&rc);
  
	final TimeInterval elapsedTime = _timer.elapsedTime();
	if (elapsedTime.milliseconds() > 100)
	{
	  _logger.logWarning("Frame took too much time: %dms", elapsedTime.milliseconds());
	}
  
	if (_logFPS)
	{
	  _totalRenderTime += elapsedTime.milliseconds();
  
	  if ((_renderStatisticsTimer == null) || (_renderStatisticsTimer.elapsedTime().seconds() > 2))
	  {
		final double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
		final double fps = 1000.0 / averageTimePerRender;
		_logger.logInfo("FPS=%f", fps);
  
		_renderCounter = 0;
		_totalRenderTime = 0;
  
		if (_renderStatisticsTimer == null)
		{
		  _renderStatisticsTimer = _factory.createTimer();
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
		_logger.logInfo("%s", cacheStatistics);
		_lastCacheStatistics = cacheStatistics;
	  }
	}
  
	return timeToRedraw;
  
  }

  public final void onTouchEvent(TouchEvent myEvent)
  {
	if (_rendererReady)
	{
	  EventContext ec = new EventContext(_factory, _logger, _planet, _downloader, _effectsScheduler);
  
	  _renderer.onTouchEvent(ec, myEvent);
	}
  }

  public final void onResizeViewportEvent(int width, int height)
  {
	if (_rendererReady)
	{
	  EventContext ec = new EventContext(_factory, _logger, _planet, _downloader, _effectsScheduler);
  
	  _renderer.onResizeViewportEvent(ec, width, height);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GL* getGL() const
  public final GL getGL()
  {
	return _gl;
  }

/*  const Camera* getCurrentCamera() const {
	return _currentCamera;
  }*/

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Camera* getNextCamera() const
  public final Camera getNextCamera()
  {
	return _nextCamera;
  }


  private FrameTasksExecutor _frameTasksExecutor;
  private IFactory _factory;
  private ILogger _logger;
  private GL _gl;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private Renderer _renderer;
  private Renderer _busyRenderer;
  private EffectsScheduler _effectsScheduler;

  private java.util.ArrayList<ICameraConstrainer > _cameraConstraint = new java.util.ArrayList<ICameraConstrainer >();

  private Camera _currentCamera;
  private Camera _nextCamera;
  private IDownloader _downloader;
  private TexturesHandler _texturesHandler;
  private final Color _backgroundColor ;

  private ITimer _timer;
  private int _renderCounter;
  private int _totalRenderTime;
  private final boolean _logFPS;
  private final boolean _logDownloaderStatistics;
  private String _lastCacheStatistics;

  private boolean _rendererReady;
  private Renderer _selectedRenderer;

  private ITimer _renderStatisticsTimer;

  private void initializeGL()
  {
	_gl.enableDepthTest();
	_gl.enableCullFace(GLCullFace.Back);
  }

  private G3MWidget(FrameTasksExecutor frameTasksExecutor, IFactory factory, ILogger logger, GL gl, TexturesHandler texturesHandler, IDownloader downloader, Planet planet, java.util.ArrayList<ICameraConstrainer > cameraConstraint, Renderer renderer, Renderer busyRenderer, EffectsScheduler effectsScheduler, int width, int height, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics)
  {
	  _frameTasksExecutor = frameTasksExecutor;
	  _factory = factory;
	  _logger = logger;
	  _gl = gl;
	  _texturesHandler = texturesHandler;
	  _planet = planet;
	  _cameraConstraint = cameraConstraint;
	  _renderer = renderer;
	  _busyRenderer = busyRenderer;
	  _effectsScheduler = effectsScheduler;
	  _currentCamera = new Camera(width, height);
	  _nextCamera = new Camera(width, height);
	  _backgroundColor = backgroundColor;
	  _timer = factory.createTimer();
	  _renderCounter = 0;
	  _totalRenderTime = 0;
	  _logFPS = logFPS;
	  _downloader = downloader;
	  _rendererReady = false;
	  _selectedRenderer = null;
	  _renderStatisticsTimer = null;
	  _logDownloaderStatistics = logDownloaderStatistics;
	initializeGL();
  
	InitializationContext ic = new InitializationContext(_factory, _logger, _planet, _downloader, _effectsScheduler);
	_effectsScheduler.initialize(ic);
	_renderer.initialize(ic);
	_busyRenderer.initialize(ic);
	_currentCamera.initialize(ic);
	_nextCamera.initialize(ic);
  
	if (_downloader != null)
	{
	  _downloader.start();
	}
  }

}