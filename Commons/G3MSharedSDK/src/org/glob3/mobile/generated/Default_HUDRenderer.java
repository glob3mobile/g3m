package org.glob3.mobile.generated; 
public class Default_HUDRenderer extends DefaultRenderer
{

  private HUDImageRenderer _hudImageRenderer;


  public Default_HUDRenderer()
  {
    _hudImageRenderer = new HUDImageRenderer(new DefaultHUDInfoRenderer_ImageFactory());
  }

  public void dispose()
  {
    if (_hudImageRenderer != null)
       _hudImageRenderer.dispose();
  }

  public final void updateInfo(java.util.ArrayList<Info> info)
  {
    DefaultHUDInfoRenderer_ImageFactory factory = (DefaultHUDInfoRenderer_ImageFactory)(_hudImageRenderer.getImageFactory());
    if (factory.setInfo(info))
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