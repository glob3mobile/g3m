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

  public static G3MWidget create(IFactory factory, ILogger logger, IGL gl, Planet planet, Renderer renderer, int width, int height, Color backgroundColor)
  {
	if (logger != null)
	{
	  logger.logInfo("Creating G3MWidget...");
	}
  
	return new G3MWidget(factory, logger, gl, planet, renderer, width, height, backgroundColor);
  }

  public void dispose()
  {
	_renderer = null;
	_planet = null;
  
	_factory = null;
	_gl = null;
  }

  public final int render()
  {
	RenderContext rc = new RenderContext(_factory, _logger, _planet, _gl, _camera);
  
	// Clear the scene
	_gl.clearScreen(_backgroundColor);
  
	int ___check_with_JM_and_Agustin;
	_gl.enableVertices();
  
	int timeToRedraw = _renderer.render(rc);
  
	return timeToRedraw;
  }

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  void onTouchEvent(const TouchEvent* event);

  public final void onResizeViewportEvent(int width, int height)
  {
	_renderer.onResizeViewportEvent(width, height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IGL * getGL() const
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
  private final Color _backgroundColor ;

  private G3MWidget(IFactory factory, ILogger logger, IGL gl, Planet planet, Renderer renderer, int width, int height, Color backgroundColor)
  {
	  _factory = factory;
	  _logger = logger;
	  _gl = gl;
	  _planet = planet;
	  _renderer = renderer;
	  _camera = new Camera(width, height);
	  _backgroundColor = backgroundColor;
	InitializationContext ic = new InitializationContext(_factory, _logger, _planet);
	_renderer.initialize(ic);
  }
}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void G3MWidget::onTouchEvent(const TouchEvent* event)
//{
//  _renderer->onTouchEvent(event);
//}
