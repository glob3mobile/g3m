package org.glob3.mobile.generated; 
public class G3MWidget
{
  private G3MWidget(Planet planet, Renderer renderer, IFactory factory, IGL gl)
  {
	  _planet = planet;
	  _renderer = renderer;
	  _factory = factory;
	  _gl = gl;
	InitializationContext ic = new InitializationContext(_factory);
	_renderer.initialize(ic);
  }


  public static G3MWidget create(Planet planet, Renderer renderer)
  {
	int __TODO_create_factory_and_gl;
	IFactory factory = null;
	IGL gl = null;
	return new G3MWidget(planet, renderer, factory, gl);
  }
  public void dispose()
  {
	_factory = null;
	if (_gl != null)
		_gl.dispose();
  }

  public final boolean render()
  {
	RenderContext rc = new RenderContext(_factory, _gl);
  
	_renderer.render(rc);
  
	return true;
  }

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//	void onTapEvent(TapEvent &event);
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//	void onTouchEvent(TouchEvent &event);

  private Renderer _renderer;
  private final Planet _planet;
  private IFactory _factory;
  private IGL _gl;
}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void G3MWidget::onTapEvent(TapEvent &event)
//{
//
//}


//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void G3MWidget::onTouchEvent(TouchEvent &event)
//{
//
//}
