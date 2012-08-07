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



public class G3MWidget
{

  public static G3MWidget create(IFactory factory, ILogger logger, GL gl, TexturesHandler texturesHandler, Downloader downloaderOLD, IDownloader downloader, Planet planet, Renderer renderer, Renderer busyRenderer, EffectsScheduler scheduler, int width, int height, Color backgroundColor, boolean logFPS)
  {
	if (logger != null)
	{
	  logger.logInfo("Creating G3MWidget...");
	}
  
	ILogger.setInstance(logger);
  
	return new G3MWidget(factory, logger, gl, texturesHandler, downloaderOLD, downloader, planet, renderer, busyRenderer, scheduler, width, height, backgroundColor, logFPS);
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
	if (_scheduler != null)
		_scheduler.dispose();
	if (_camera != null)
		_camera.dispose();
	if (_texturesHandler != null)
		_texturesHandler.dispose();
	if (_timer != null)
		_timer.dispose();
	if (_downloader != null)
		_downloader.dispose();
  }

  public final int render()
  {
	_timer.start();
	_renderCounter++;
  
	RenderContext rc = new RenderContext(_factory, _logger, _planet, _gl, _camera, _texturesHandler, _downloaderOLD, _downloader, _scheduler);
  
	_scheduler.doOneCyle(rc);
  
	_rendererReady = _renderer.isReadyToRender(rc);
	Renderer selectedRenderer = _rendererReady ? _renderer : _busyRenderer;
  
	// Clear the scene
	_gl.clearScreen(_backgroundColor);
  
	final int timeToRedraw = selectedRenderer.render(rc);
  
	final TimeInterval elapsedTime = _timer.elapsedTime();
	if (elapsedTime.milliseconds() > 100)
	{
	  _logger.logWarning("Frame took too much time: %dms", elapsedTime.milliseconds());
	}
	_totalRenderTime += elapsedTime.milliseconds();
  
	if ((_renderCounter % 60) == 0)
	{
	  if (_logFPS)
	  {
		final double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
		final double fps = 1000.0 / averageTimePerRender;
		_logger.logInfo("FPS=%f", fps);
	  }
  
	  _renderCounter = 0;
	  _totalRenderTime = 0;
	}
  
	return timeToRedraw;
  
  }

  public final void onTouchEvent(TouchEvent myEvent)
  {
	if (_rendererReady)
	{
	  _renderer.onTouchEvent(myEvent);
	}
  }

  public final void onResizeViewportEvent(int width, int height)
  {
	if (_rendererReady)
	{
	  _renderer.onResizeViewportEvent(width, height);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GL* getGL() const
  public final GL getGL()
  {
	return _gl;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Camera* getCamera() const
  public final Camera getCamera()
  {
	return _camera;
  }


  private IFactory _factory;
  private ILogger _logger;
  private GL _gl;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private Renderer _renderer;
  private Renderer _busyRenderer;
  private EffectsScheduler _scheduler;

  private Camera _camera;
  private Downloader _downloaderOLD;
  private IDownloader _downloader;
  private TexturesHandler _texturesHandler;
  private final Color _backgroundColor ;

  private ITimer _timer;
  private int _renderCounter;
  private int _totalRenderTime;
  private final boolean _logFPS;

  private boolean _rendererReady;

  private void initializeGL()
  {
	_gl.enableDepthTest();
	_gl.enableCullFace(CullFace.Back);
  }

  private G3MWidget(IFactory factory, ILogger logger, GL gl, TexturesHandler texturesHandler, Downloader downloaderOLD, IDownloader downloader, Planet planet, Renderer renderer, Renderer busyRenderer, EffectsScheduler scheduler, int width, int height, Color backgroundColor, boolean logFPS)
  {
	  _factory = factory;
	  _logger = logger;
	  _gl = gl;
	  _texturesHandler = texturesHandler;
	  _planet = planet;
	  _renderer = renderer;
	  _busyRenderer = busyRenderer;
	  _scheduler = scheduler;
	  _camera = new Camera(planet, width, height);
	  _backgroundColor = backgroundColor;
	  _timer = factory.createTimer();
	  _renderCounter = 0;
	  _totalRenderTime = 0;
	  _logFPS = logFPS;
	  _downloaderOLD = downloaderOLD;
	  _downloader = downloader;
	  _rendererReady = false;
	initializeGL();
  
	InitializationContext ic = new InitializationContext(_factory, _logger, _planet, _downloaderOLD, _downloader, _scheduler);
	_scheduler.initialize(ic);
	_renderer.initialize(ic);
	_busyRenderer.initialize(ic);
  }

  private void initializeDefault()
  {

  }

}