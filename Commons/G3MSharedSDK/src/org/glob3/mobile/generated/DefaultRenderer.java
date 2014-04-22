package org.glob3.mobile.generated; 
//
//  DefaultRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

//
//  DefaultRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//


//class ChangedInfoListener;



//class GPUProgramState;

public abstract class DefaultRenderer implements Renderer
{


  private boolean _enable;


  protected ChangedRendererInfoListener _changedInfoListener = null;

  protected int _rendererIdentifier = -1;

  protected G3MContext _context;

  protected DefaultRenderer()
  {
     _enable = true;

  }

  protected DefaultRenderer(boolean enable)
  {
     _enable = enable;

  }

  public void dispose()
  {
    _context = null;
    _changedInfoListener = null;
  }


  public final boolean isEnable()
  {
    return _enable;
  }

  public void setEnable(boolean enable)
  {
    _enable = enable;
  }

  public void initialize(G3MContext context)
  {
    _context = context;
    onChangedContext();
  }

  public void onResume(G3MContext context)
  {
    _context = context;
    onChangedContext();
  }

  public void onPause(G3MContext context)
  {
    _context = null;
    onLostContext();
  }

  public void onDestroy(G3MContext context)
  {
    _context = null;
    onLostContext();
  }

  public void onChangedContext()
  {
  }

  public void onLostContext()
  {
  }


  public RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public abstract void render(G3MRenderContext rc, GLState glState);

  public boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public abstract void onResizeViewportEvent(G3MEventContext ec, int width, int height);

  public void start(G3MRenderContext rc)
  {

  }

  public void stop(G3MRenderContext rc)
  {

  }

  public SurfaceElevationProvider getSurfaceElevationProvider()
  {
    return null;
  }

  public PlanetRenderer getPlanetRenderer()
  {
    return null;
  }

  public boolean isPlanetRenderer()
  {
    return false;
  }

  public final void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererIdentifier)
  {
    if (_changedInfoListener != null)
    {
      ILogger.instance().logError("Changed Renderer Info Listener of DefaultRenderer already set");
    }
    _changedInfoListener = changedInfoListener;
    _rendererIdentifier = rendererIdentifier;
    ILogger.instance().logInfo("Changed Renderer Info Listener of DefaultRenderer set ok");
  }
}