package org.glob3.mobile.generated; 
public class G3MWidget
{

  public static G3MWidget create(IFactory factory, ILogger logger, IGL gl, Planet planet, Renderer renderer, int width, int height)
  {
	if (logger != null)
	{
	  logger.logInfo("Creating G3MWidget...");
	}
  
	return new G3MWidget(factory, logger, gl, planet, renderer, width, height);
  }

  public void dispose()
  {
	_renderer = null;
	_planet = null;
  
	_factory = null;
	_gl = null;
  }

  public final boolean render()
  {
	RenderContext rc = new RenderContext(_factory, _logger, _planet, _gl, _camera);
  
	// Clear the scene
	//glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	//glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
	_gl.ClearScreen(1, 1, 0);
	_gl.EnableVertices();
  
  
	_renderer.render(rc);
  
	return true;
  }

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  void onTouchEvent(const TouchEvent* event);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IGL * getGL() const
  public final IGL getGL()
  {
	  return _gl;
  }


  private IFactory _factory;
  private ILogger _logger;
  private IGL _gl;
  private final Planet _planet;
  private Renderer _renderer;
  private Camera _camera;


  private G3MWidget(IFactory factory, ILogger logger, IGL gl, Planet planet, Renderer renderer, int width, int height)
  {
	  _factory = factory;
	  _logger = logger;
	  _gl = gl;
	  _planet = planet;
	  _renderer = renderer;
	  _camera = new Camera(width, height);
	InitializationContext ic = new InitializationContext(_factory, _logger, _planet);
	_renderer.initialize(ic);
  }
}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void G3MWidget::onTouchEvent(const TouchEvent* event)
//{
//  _renderer->onTouchEvent(event);
//}
