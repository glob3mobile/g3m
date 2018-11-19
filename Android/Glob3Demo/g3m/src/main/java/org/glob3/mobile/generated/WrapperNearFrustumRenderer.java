package org.glob3.mobile.generated;
//
//  WrapperNearFrustumRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/17.
//
//

//
//  WrapperNearFrustumRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/17.
//
//




public class WrapperNearFrustumRenderer extends NearFrustumRenderer
{
  private final double _zNear;
  private Renderer _renderer;



  ///#include "Camera.hpp"
  
  
  public WrapperNearFrustumRenderer(double zNear, Renderer renderer)
  {
     _zNear = zNear;
     _renderer = renderer;
  }

  public void dispose()
  {
    if (_renderer != null)
       _renderer.dispose();
  }

  public final void initialize(G3MContext context)
  {
    _renderer.initialize(context);
  }

  public final void start(G3MRenderContext rc)
  {
    _renderer.start(rc);
  }

  public final void stop(G3MRenderContext rc)
  {
    _renderer.stop(rc);
  }

  public final void onResume(G3MContext context)
  {
    _renderer.onResume(context);
  }

  public final void onPause(G3MContext context)
  {
    _renderer.onPause(context);
  }

  public final void onDestroy(G3MContext context)
  {
    _renderer.onDestroy(context);
  }

  public final boolean isEnable()
  {
    return _renderer.isEnable();
  }

  public final void setEnable(boolean enable)
  {
    _renderer.setEnable(enable);
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return _renderer.getRenderState(rc);
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    _renderer.onResizeViewportEvent(ec, width, height);
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return _renderer.onTouchEvent(ec, touchEvent);
  }

  public final void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererID)
  {
    _renderer.setChangedRendererInfoListener(changedInfoListener, rendererID);
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    _renderer.render(rc, glState);
  }

  public final void render(FrustumData currentFrustumData, FrustumPolicyHandler handler, G3MRenderContext rc, GLState glState)
  {
    handler.changeToFixedFrustum(_zNear, currentFrustumData._zNear);
    rc.getGL().clearDepthBuffer();
    render(rc, glState);
    handler.resetFrustumPolicy();
  }

}
