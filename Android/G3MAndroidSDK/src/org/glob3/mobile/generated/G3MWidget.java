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



public class G3MWidget
{

  public static G3MWidget create(IFactory factory, ILogger logger, IGL gl, TexturesHandler texturesHandler, Planet planet, Renderer renderer, int width, int height, Color backgroundColor, boolean logFPS)
  {
	if (logger != null)
	{
	  logger.logInfo("Creating G3MWidget...");
	}
  
	return new G3MWidget(factory, logger, gl, texturesHandler, planet, renderer, width, height, backgroundColor, logFPS);
  }

  public void dispose()
  {
	_factory = null;
	_logger = null;
	_gl = null;
	_planet = null;
	_renderer = null;
	if (_camera != null)
		_camera.dispose();
	if (_texturesHandler != null)
		_texturesHandler.dispose();
	_timer = null;
  }

  public final int render()
  {
	_timer.start();
	_renderCounter++;
  
	RenderContext rc = new RenderContext(_factory, _logger, _planet, _gl, _camera, _texturesHandler);
  
	// Clear the scene
	_gl.clearScreen(_backgroundColor);
  
	int timeToRedraw = _renderer.render(rc);
  
  
	final TimeInterval elapsedTime = _timer.elapsedTime();
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
	_renderer.onTouchEvent(myEvent);
  }

  public final void onResizeViewportEvent(int width, int height)
  {
	_renderer.onResizeViewportEvent(width, height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IGL* getGL() const
  public final IGL getGL()
  {
	return _gl;
  }


  private IFactory _factory;
  private ILogger _logger;
  private IGL _gl;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private Renderer _renderer;
  private Camera _camera;
  private TexturesHandler _texturesHandler;
  private final Color _backgroundColor ;

  private ITimer _timer;
  private int _renderCounter;
  private int _totalRenderTime;
  private final boolean _logFPS;

  private void initializeGL()
  {
	_gl.depth(true);
	_gl.cullFace(true, CullFace.BACK);
  }

  private G3MWidget(IFactory factory, ILogger logger, IGL gl, TexturesHandler texturesHandler, Planet planet, Renderer renderer, int width, int height, Color backgroundColor, boolean logFPS)
  {
	  _factory = factory;
	  _logger = logger;
	  _gl = gl;
	  _texturesHandler = texturesHandler;
	  _planet = planet;
	  _renderer = renderer;
	  _camera = new Camera(width, height);
	  _backgroundColor = backgroundColor;
	  _timer = factory.createTimer();
	  _renderCounter = 0;
	  _totalRenderTime = 0;
	  _logFPS = logFPS;
	initializeGL();

	InitializationContext ic = new InitializationContext(_factory, _logger, _planet);
	_renderer.initialize(ic);
  }

}