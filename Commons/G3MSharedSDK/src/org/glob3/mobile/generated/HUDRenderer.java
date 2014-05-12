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

public class HUDRenderer extends DefaultRenderer
{

  private java.util.ArrayList<HUDWidget> _widgets = new java.util.ArrayList<HUDWidget>();
  private int _widgetsSize;

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
    _widgetsSize = _widgets.size();
  }

  public void dispose()
  {
    for (int i = 0; i < _widgetsSize; i++)
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
    _widgetsSize = _widgets.size();
  
    if (_context != null)
    {
      widget.initialize(_context);
    }
  }

  public final void removeAllWidgets()
  {
    for (int i = 0; i < _widgetsSize; i++)
    {
      HUDWidget widget = _widgets.get(i);
      if (widget != null)
         widget.dispose();
    }
  
    _widgets.clear();
    _widgetsSize = _widgets.size();
  
  }

  public final void onChangedContext()
  {
    for (int i = 0; i < _widgetsSize; i++)
    {
      HUDWidget widget = _widgets.get(i);
      widget.initialize(_context);
    }
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    if (_widgetsSize == 0)
    {
      return RenderState.ready();
    }
  
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _widgetsSize; i++)
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

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {
  //  const int halfWidth  = width  / 2;
  //  const int halfHeight = height / 2;
  //  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(-halfWidth,  halfWidth,
  //                                                                                           -halfHeight, halfHeight,
  //                                                                                           -halfWidth,  halfWidth);
  //  double left, double right,
  //  double bottom, double top,
  //  double znear, double zfar
  //  MutableMatrix44D projectionMatrix = MutableMatrix44D::createOrthographicProjectionMatrix(0, width,
  //                                                                                           0, height,
  //                                                                                           -halfWidth, halfWidth);
    MutableMatrix44D projectionMatrix = MutableMatrix44D.createOrthographicProjectionMatrix(0, width, 0, height, -1, +1);
  
    ProjectionGLFeature pr = (ProjectionGLFeature) _glState.getGLFeature(GLFeatureID.GLF_PROJECTION);
    if (pr == null)
    {
      _glState.addGLFeature(new ProjectionGLFeature(projectionMatrix.asMatrix44D()), false);
    }
    else
    {
      pr.setMatrix(projectionMatrix.asMatrix44D());
    }
  
    for (int i = 0; i < _widgetsSize; i++)
    {
      HUDWidget widget = _widgets.get(i);
      widget.onResizeViewportEvent(ec, width, height);
    }
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_widgetsSize == 0)
    {
      return;
    }
  
    INativeGL nativeGL = rc.getGL().getNative();
  
    nativeGL.depthMask(false);
  
    for (int i = 0; i < _widgetsSize; i++)
    {
      HUDWidget widget = _widgets.get(i);
      widget.render(rc, _glState);
    }
  
    nativeGL.depthMask(true);
  }

}