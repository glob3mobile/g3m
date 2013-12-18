package org.glob3.mobile.generated; 
//
//  HUDRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

//
//  HUDRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//




//class HUDWidget;

public class HUDRenderer extends LeafRenderer
{
  private G3MContext _context;

  private java.util.ArrayList<HUDWidget> _widgets = new java.util.ArrayList<HUDWidget>();

  private GLState _glState;

  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();
  private final boolean _readyWhenWidgetsReady;

  public HUDRenderer()
  {
     this(true);
  }
  public HUDRenderer(boolean readyWhenWidgetsReady)
  {
     _glState = new GLState();
     _readyWhenWidgetsReady = readyWhenWidgetsReady;
     _context = null;
  }

  public void dispose()
  {
    final int size = _widgets.size();
    for (int i = 0; i < size; i++)
    {
      HUDWidget widget = _widgets.get(i);
      if (widget != null)
         widget.dispose();
    }
  
    _glState._release();
  
    super.dispose();
  }

  public final void addWidget(HUDWidget widget)
  {
    _widgets.add(widget);
  
    if (_context != null)
    {
      widget.initialize(_context);
    }
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    final int size = _widgets.size();
    for (int i = 0; i < size; i++)
    {
      HUDWidget widget = _widgets.get(i);
      widget.initialize(context);
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    final int size = _widgets.size();
    for (int i = 0; i < size; i++)
    {
      HUDWidget widget = _widgets.get(i);
      if (widget.isEnable())
      {
        final RenderState childRenderState = widget.getRenderState(rc);
  
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
    else if (busyFlag && _readyWhenWidgetsReady)
    {
      return RenderState.busy();
    }
    else
    {
      return RenderState.ready();
    }
  }

  public final void onResume(G3MContext context)
  {
  }

  public final void onPause(G3MContext context)
  {
  }

  public final void onDestroy(G3MContext context)
  {
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void start(G3MRenderContext rc)
  {
  }

  public final void stop(G3MRenderContext rc)
  {
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
    final int halfWidth = width / 2;
    final int halfHeight = height / 2;
    MutableMatrix44D projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(-halfWidth, halfWidth, -halfHeight, halfHeight, -halfWidth, halfWidth);
  
    ProjectionGLFeature pr = (ProjectionGLFeature) _glState.getGLFeature(GLFeatureID.GLF_PROJECTION);
    if (pr == null)
    {
      _glState.addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()), false);
    }
    else
    {
      pr.setMatrix(projectionMatrix.asMatrix44D());
    }
  
    final int size = _widgets.size();
    for (int i = 0; i < size; i++)
    {
      HUDWidget widget = _widgets.get(i);
      widget.onResizeViewportEvent(ec, width, height);
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    rc.getGL().getNative().depthMask(false);
  
    final int size = _widgets.size();
    for (int i = 0; i < size; i++)
    {
      HUDWidget widget = _widgets.get(i);
      if (widget.isEnable())
      {
        widget.render(rc, _glState);
      }
    }
  
    rc.getGL().getNative().depthMask(true);
  }

}