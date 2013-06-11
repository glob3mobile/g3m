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



public class CompositeRenderer extends Renderer
{
  private java.util.ArrayList<Renderer> _renderers = new java.util.ArrayList<Renderer>();

  private G3MContext _context;

  private boolean _enable;

  public CompositeRenderer()
  {
     _context = null;
     _enable = true;
    _renderers = new java.util.ArrayList<Renderer>();
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
  
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      if (_renderers.get(i).isEnable())
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
  
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      _renderers.get(i).initialize(context);
    }
  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      Renderer renderer = _renderers.get(i);
      if (renderer.isEnable())
      {
        if (!renderer.isReadyToRender(rc))
        {
          return false;
        }
      }
    }
  
    return true;
  }

  public final void render(G3MRenderContext rc)
  {
    //rc->getLogger()->logInfo("CompositeRenderer::render()");
  
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      Renderer renderer = _renderers.get(i);
      if (renderer.isEnable())
      {
        renderer.render(rc);
      }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    // the events are processed bottom to top
    final int rendersSize = _renderers.size();
    for (int i = rendersSize - 1; i >= 0; i--)
    {
      Renderer renderer = _renderers.get(i);
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
    final int rendersSize = _renderers.size();
    for (int i = rendersSize - 1; i >= 0; i--)
    {
      _renderers.get(i).onResizeViewportEvent(ec, width, height);
    }
  }

  public final void addRenderer(Renderer renderer)
  {
    _renderers.add(renderer);
    if (_context != null)
    {
      renderer.initialize(_context);
    }
  }

  public final void start(G3MRenderContext rc)
  {
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      _renderers.get(i).start(rc);
    }
  }

  public final void stop(G3MRenderContext rc)
  {
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      _renderers.get(i).stop(rc);
    }
  }

  public final void onResume(G3MContext context)
  {
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      _renderers.get(i).onResume(context);
    }
  }

  public final void onPause(G3MContext context)
  {
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      _renderers.get(i).onPause(context);
    }
  }

  public final void onDestroy(G3MContext context)
  {
    final int rendersSize = _renderers.size();
    for (int i = 0; i < rendersSize; i++)
    {
      _renderers.get(i).onDestroy(context);
    }
  }
}