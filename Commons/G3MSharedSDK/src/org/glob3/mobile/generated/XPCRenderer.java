package org.glob3.mobile.generated;
//
//  XPCRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class ITimer;
//class XPCPointCloud;
//class Camera;
//class XPCSelectionResult;


public class XPCRenderer extends DefaultRenderer
{

  private ITimer _timer;

  private java.util.ArrayList<XPCPointCloud> _clouds = new java.util.ArrayList<XPCPointCloud>();
  private int _cloudsSize;
  private RenderStateCollector _renderStateCollector = new RenderStateCollector();

  private GLState _glState;

  private Camera _lastCamera;
  private boolean _renderDebug;
  private XPCSelectionResult _selectionResult;

  private ITimer _lastSplitTimer;


  protected final void onChangedContext()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      cloud.initialize(_context);
    }
  }



  public XPCRenderer()
  {
     _cloudsSize = 0;
     _renderStateCollector = new RenderStateCollector();
     _glState = new GLState();
     _timer = null;
     _lastCamera = null;
     _renderDebug = false;
     _selectionResult = null;
     _lastSplitTimer = null;
  }

  public void dispose()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      cloud.cancel();
      cloud._release();
    }
  
    _glState._release();
    if (_timer != null)
       _timer.dispose();
  
    if (_selectionResult != null)
       _selectionResult.dispose();
  
    if (_lastSplitTimer != null)
       _lastSplitTimer.dispose();
  
    super.dispose();
  }


  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _renderStateCollector.reset();
  
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      _renderStateCollector.add(cloud.getRenderState(rc));
    }
  
    // busy clouds don't make the renderer busy
    final RenderState renderState = _renderStateCollector.getState();
    if (renderState._type == RenderState_Type.RENDER_BUSY)
    {
      return RenderState.ready();
    }
    return renderState;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void addPointCloud(XPCPointCloud pointCloud)
  {
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
    _cloudsSize = _clouds.size();
  }

  public final boolean removePointCloud(XPCPointCloud pointCloud)
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      if (_clouds.get(i) == pointCloud)
      {
        _clouds.remove(i);
        _cloudsSize = _clouds.size();
  
        pointCloud.cancel();
        pointCloud._release();
  
        return true;
      }
    }
    return false;
  }

  public final void removeAllPointClouds()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      cloud.cancel();
      cloud._release();
    }
    _clouds.clear();
    _cloudsSize = _clouds.size();
  
    if (_selectionResult != null)
       _selectionResult.dispose();
    _selectionResult = null;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_cloudsSize > 0)
    {
      _lastCamera = rc.getCurrentCamera();
  
  
      if (_lastSplitTimer == null)
      {
        _lastSplitTimer = rc.getFactory().createTimer();
        _lastSplitTimer.start();
      }
  
      if (_timer == null)
      {
        _timer = rc.getFactory().createTimer();
      }
      final long nowInMS = _timer.nowInMilliseconds();
  
      final Camera camera = rc.getCurrentCamera();
      ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
      if (f == null)
      {
        _glState.addGLFeature(new ModelViewGLFeature(camera.getModelViewMatrix44D()), false);
      }
      else
      {
        f.setMatrix(camera.getModelViewMatrix44D());
      }
  
      _glState.setParent(glState);
  
      final Frustum frustum = camera.getFrustumInModelCoordinates();
      for (int i = 0; i < _cloudsSize; i++)
      {
        XPCPointCloud cloud = _clouds.get(i);
        cloud.render(rc, _lastSplitTimer, _glState, frustum, nowInMS, _renderDebug);
      }
  
  //    if (_selectionResult != NULL) {
  //      _selectionResult->render(rc, _glState);
  //    }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_cloudsSize > 0)
    {
      if (_lastCamera != null)
      {
        if (touchEvent.getType() == TouchEventType.LongPress)
        {
          final Vector2F touchedPixel = touchEvent.getTouch(0).getPos();
          final Vector3D rayDirection = _lastCamera.pixel2Ray(touchedPixel);
  
          if (!rayDirection.isNan())
          {
            final Vector3D rayOrigin = _lastCamera.getCartesianPosition();
  
            XPCSelectionResult selectionResult = new XPCSelectionResult(new Ray(rayOrigin, rayDirection));
  
            boolean selectedPoints = false;
            for (int i = 0; i < _cloudsSize; i++)
            {
              XPCPointCloud cloud = _clouds.get(i);
              if (cloud.selectPoints(selectionResult))
              {
                selectedPoints = true;
              }
            }
  
            ILogger.instance().logInfo("Camera position=%s heading=%f pitch=%f", _lastCamera.getGeodeticPosition().description(), _lastCamera.getHeading()._degrees, _lastCamera.getPitch()._degrees);
  
            if (selectedPoints)
            {
              if (_selectionResult != null)
                 _selectionResult.dispose();
              _selectionResult = selectionResult;
  
              return _selectionResult.notifyPointCloud(ec.getPlanet());
            }
  
            if (selectionResult != null)
               selectionResult.dispose();
            selectionResult = null;
          }
        }
      }
    }
  
    if (touchEvent.getType() == TouchEventType.LongPress)
    {
      if (_selectionResult != null)
         _selectionResult.dispose();
      _selectionResult = null;
    }
  
    return false;
  }

}