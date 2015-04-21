package org.glob3.mobile.generated; 
//
//  CompositeRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class CompositeRenderer implements Renderer, ChangedRendererInfoListener
{
  private final java.util.ArrayList<Info> _info = new java.util.ArrayList<Info>();
  private java.util.ArrayList<ChildRenderer> _renderers = new java.util.ArrayList<ChildRenderer>();
  private int _renderersSize;

  private G3MContext _context;

  private boolean _enable;

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private ChangedRendererInfoListener _changedInfoListener;

  private java.util.ArrayList<Info> getInfo()
  {
    _info.clear();
  
    for (int i = 0; i < _renderersSize; i++)
    {
      ChildRenderer child = _renderers.get(i);
      final java.util.ArrayList<Info> childInfo = child.getInfo();
      _info.addAll(childInfo);
  
    }
  
    return _info;
  }

  public CompositeRenderer()
  {
     _context = null;
     _enable = true;
     _renderersSize = 0;
     _changedInfoListener = null;
    //    _renderers = std::vector<Renderer*>();
  }

  public void dispose()
  {

  }

  public final boolean isEnable()
  {
    if (!_enable)
    {
      return false;
    }
  
    for (int i = 0; i < _renderersSize; i++)
    {
      if (_renderers.get(i).getRenderer().isEnable())
      {
        return true;
      }
    }
    return false;
  }

  public final void setEnable(boolean enable)
  {
    _enable = enable;
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    for (int i = 0; i < _renderersSize; i++)
    {
      _renderers.get(i).getRenderer().initialize(context);
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _renderersSize; i++)
    {
      Renderer child = _renderers.get(i).getRenderer();
      if (child.isEnable())
      {
        final RenderState childRenderState = child.getRenderState(rc);
  
        final RenderState_Type childRenderStateType = childRenderState._type;
  
        if (childRenderStateType == RenderState_Type.RENDER_ERROR)
        {
          errorFlag = true;
  
          final java.util.ArrayList<String> childErrors = childRenderState.getErrors();
          _errors.addAll(childErrors);
        }
        else if (childRenderStateType == RenderState_Type.RENDER_BUSY)
        {
          busyFlag = true;
        }
      }
    }
  
    if (errorFlag)
    {
      return RenderState.error(_errors);
    }
    else if (busyFlag)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    //rc->getLogger()->logInfo("CompositeRenderer::render()");
  
    for (int i = 0; i < _renderersSize; i++)
    {
      Renderer renderer = _renderers.get(i).getRenderer();
      if (renderer.isEnable())
      {
        renderer.render(rc, glState);
      }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    // the events are processed bottom to top
    for (int i = _renderersSize - 1; i >= 0; i--)
    {
      Renderer renderer = _renderers.get(i).getRenderer();
      if (renderer.isEnable())
      {
        if (renderer.onTouchEvent(ec, touchEvent))
        {
          return true;
        }
      }
    }
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    // the events are processed bottom to top
    for (int i = _renderersSize - 1; i >= 0; i--)
    {
      _renderers.get(i).getRenderer().onResizeViewportEvent(ec, width, height);
    }
  }

  public final void addRenderer(Renderer renderer)
  {
    addChildRenderer(new ChildRenderer(renderer));
  }

  public final void addRenderer(Renderer renderer, java.util.ArrayList<Info> info)
  {
    addChildRenderer(new ChildRenderer(renderer, info));
  }

  public final void addChildRenderer(ChildRenderer renderer)
  {
    _renderers.add(renderer);
    _renderersSize = _renderers.size();
  
    if (_context != null)
    {
      renderer.getRenderer().initialize(_context);
    }
  
    renderer.getRenderer().setChangedRendererInfoListener(this, (_renderers.size() - 1));
  }

  public final void start(G3MRenderContext rc)
  {
    for (int i = 0; i < _renderersSize; i++)
    {
      _renderers.get(i).getRenderer().start(rc);
    }
  }

  public final void stop(G3MRenderContext rc)
  {
    for (int i = 0; i < _renderersSize; i++)
    {
      _renderers.get(i).getRenderer().stop(rc);
    }
  }

  public final void onResume(G3MContext context)
  {
    for (int i = 0; i < _renderersSize; i++)
    {
      _renderers.get(i).getRenderer().onResume(context);
    }
  }

  public final void onPause(G3MContext context)
  {
    for (int i = 0; i < _renderersSize; i++)
    {
      _renderers.get(i).getRenderer().onPause(context);
    }
  }

  public final void onDestroy(G3MContext context)
  {
    for (int i = 0; i < _renderersSize; i++)
    {
      _renderers.get(i).getRenderer().onDestroy(context);
    }
  }

  public final SurfaceElevationProvider getSurfaceElevationProvider()
  {
    SurfaceElevationProvider result = null;
  
    for (int i = 0; i < _renderersSize; i++)
    {
      Renderer renderer = _renderers.get(i).getRenderer();
      SurfaceElevationProvider childSurfaceElevationProvider = renderer.getSurfaceElevationProvider();
      if (childSurfaceElevationProvider != null)
      {
        if (result == null)
        {
          result = childSurfaceElevationProvider;
        }
        else
        {
          ILogger.instance().logError("Inconsistency in Renderers: more than one SurfaceElevationProvider");
        }
      }
    }
  
    return result;
  }

  public final PlanetRenderer getPlanetRenderer()
  {
    PlanetRenderer result = null;
  
    for (int i = 0; i < _renderersSize; i++)
    {
      Renderer renderer = _renderers.get(i).getRenderer();
      PlanetRenderer planetRenderer = renderer.getPlanetRenderer();
      if (planetRenderer != null)
      {
        if (result == null)
        {
          result = planetRenderer;
        }
        else
        {
          ILogger.instance().logError("Inconsistency in Renderers: more than one PlanetRenderer");
        }
      }
    }
  
    return result;
  }

  public boolean isPlanetRenderer()
  {
    return false;
  }

  public final void setChangedRendererInfoListener(ChangedRendererInfoListener changedInfoListener, int rendererIdentifier)
  {
    if (_changedInfoListener != null)
    {
      ILogger.instance().logError("Changed Renderer Info Listener of CompositeRenderer already set");
    }
    _changedInfoListener = changedInfoListener;
  
    if(_changedInfoListener != null)
    {
      _changedInfoListener.changedRendererInfo(-1, getInfo());
    }
  }

  public final void changedRendererInfo(int rendererIdentifier, java.util.ArrayList<Info> info)
  {
    if(rendererIdentifier >= 0 && rendererIdentifier < _renderersSize)
    {
      _renderers.get(rendererIdentifier).setInfo(info);
    }
    else
    {
      ILogger.instance().logWarning("Child Render not found: %d", rendererIdentifier);
    }
  
    if (_changedInfoListener != null)
    {
      _changedInfoListener.changedRendererInfo(-1, getInfo());
    }
  }

}