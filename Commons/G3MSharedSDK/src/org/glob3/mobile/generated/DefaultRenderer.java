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

  private java.util.ArrayList<String> _info = new java.util.ArrayList<String>();

  private void notifyChangedInfo(java.util.ArrayList<String> info)
  {
    if(_changedInfoListener!= null)
    {
      if(isEnable())
      {
        _changedInfoListener.changedRendererInfo(_rendererIdentifier, info);
      }
    }
  }


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

  protected void onChangedContext()
  {
  }

  protected void onLostContext()
  {
  }


  public final boolean isEnable()
  {
    return _enable;
  }

  public void setEnable(boolean enable)
  {
    if(enable != _enable)
    {
      _enable = enable;
      if(_changedInfoListener!= null)
      {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning vtp ask dgd: empty vector?
        if(isEnable())
        {
          notifyChangedInfo(_info);
        }
        else
        {
          final java.util.ArrayList<String> info = new java.util.ArrayList<String>();
          _changedInfoListener.changedRendererInfo(_rendererIdentifier, info);
        }
      }
    }
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

  public final void setInfo(java.util.ArrayList<String> info)
  {
    _info.clear();
    _info.addAll(info);
    notifyChangedInfo(_info);
  }

  public final void addInfo(java.util.ArrayList<String> info)
  {
    _info.addAll(info);
    notifyChangedInfo(_info);
  }

  public final void addInfo(String info)
  {
    _info.add(info);
    notifyChangedInfo(_info);
  }




  public final void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererIdentifier)
  {
    if (_changedInfoListener != null)
    {
      ILogger.instance().logError("Changed Renderer Info Listener of DefaultRenderer already set");
    }
    else
    {
      _changedInfoListener = changedInfoListener;
      _rendererIdentifier = rendererIdentifier;
      notifyChangedInfo(_info);
    }
  }
}