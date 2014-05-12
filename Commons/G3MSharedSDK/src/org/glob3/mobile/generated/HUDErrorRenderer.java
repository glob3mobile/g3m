package org.glob3.mobile.generated; 
public class HUDErrorRenderer implements ErrorRenderer
{
  private HUDImageRenderer _hudImageRenderer;
  private ErrorMessagesCustomizer _errorMessageCustomizer;


  public HUDErrorRenderer()
  {
     this(null);
  }
  public HUDErrorRenderer(ErrorMessagesCustomizer errorMessageCustomizer)
  {
    _hudImageRenderer = new HUDImageRenderer(new HUDErrorRenderer_ImageFactory());
    _errorMessageCustomizer = errorMessageCustomizer;
  }

  public void dispose()
  {

  }

  public final void setErrors(java.util.ArrayList<String> errors)
  {
    HUDErrorRenderer_ImageFactory factory = (HUDErrorRenderer_ImageFactory)(_hudImageRenderer.getImageFactory());
    final java.util.ArrayList<String> customizedErrors = (_errorMessageCustomizer != null) ? _errorMessageCustomizer.customize(errors) : errors;
    if (factory.setErrors(customizedErrors))
    {
      _hudImageRenderer.recreateImage();
    }
  }

  public final void initialize(G3MContext context)
  {
    _hudImageRenderer.initialize(context);
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    _hudImageRenderer.render(rc, glState);
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    _hudImageRenderer.onResizeViewportEvent(ec, width, height);
  }

  public final void start(G3MRenderContext rc)
  {
    _hudImageRenderer.start(rc);
  }

  public final void stop(G3MRenderContext rc)
  {
    _hudImageRenderer.stop(rc);
  }


  public final void onResume(G3MContext context)
  {
    _hudImageRenderer.onResume(context);
  }

  public final void onPause(G3MContext context)
  {
    _hudImageRenderer.onPause(context);
  }

  public final void onDestroy(G3MContext context)
  {
    _hudImageRenderer.onDestroy(context);
  }

}