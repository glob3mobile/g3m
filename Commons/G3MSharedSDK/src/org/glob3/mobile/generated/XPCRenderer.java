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
//class URL;
//class TimeInterval;
//class XPCPointColorizer;
//class XPCMetadataListener;
//class Camera;
//class Ray;


public class XPCRenderer extends DefaultRenderer
{

  private ITimer _timer;

  private java.util.ArrayList<XPCPointCloud> _clouds = new java.util.ArrayList<XPCPointCloud>();
  private int _cloudsSize;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private GLState _glState;

  private Camera _lastCamera;
  private boolean _renderDebug;
  private Ray _ray;


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
     _glState = new GLState();
     _timer = null;
     _lastCamera = null;
     _renderDebug = false;
     _ray = null;
  }

  public void dispose()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      cloud._release();
    }
  
    _glState._release();
    if (_timer != null)
       _timer.dispose();
  
    if (_ray != null)
       _ray.dispose();
  
    super.dispose();
  }

  public final void removeAllPointClouds()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      cloud._release();
    }
    _clouds.clear();
    _cloudsSize = _clouds.size();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    _errors.clear();
    boolean busyFlag = false;
    boolean errorFlag = false;
  
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      final RenderState childRenderState = cloud.getRenderState(rc);
  
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
    if (_cloudsSize > 0)
    {
      _lastCamera = rc.getCurrentCamera();
  
      if (_timer == null)
      {
        _timer = rc.getFactory().createTimer();
      }
      final long nowInMS = _timer.elapsedTimeInMilliseconds();
  
      final Camera camera = rc.getCurrentCamera();
      ModelViewGLFeature f = (ModelViewGLFeature) _glState.getGLFeature(GLFeatureID.GLF_MODEL_VIEW);
      if (f == null)
      {
        _glState.addGLFeature(new ModelViewGLFeature(camera), true);
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
        cloud.render(rc, _glState, frustum, nowInMS, _renderDebug, _ray);
      }
  
      if (_ray != null)
      {
        _ray.render(rc, _glState, Color.YELLOW);
      }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    _renderDebug = false;
  
    if (_cloudsSize > 0)
    {
      if (_lastCamera != null)
      {
        if ((touchEvent.getTouchCount() == 1) && (touchEvent.getTapCount() == 1) && (touchEvent.getType() == TouchEventType.LongPress))
        {
  
          final Vector2F touchedPixel = touchEvent.getTouch(0).getPos();
          final Vector3D rayDirection = _lastCamera.pixel2Ray(touchedPixel);
  
          if (!rayDirection.isNan())
          {
            // _renderDebug = true;
  
            final Vector3D rayOrigin = _lastCamera.getCartesianPosition();
  
            Ray ray = new Ray(rayOrigin, rayDirection);
  
            //const Planet* planet = ec->getPlanet();
  
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning SELECTION (pointCloud, tree, node, pointID)
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning currentXYZ, minimumSquaredDistance
  
            for (int i = 0; i < _cloudsSize; i++)
            {
              XPCPointCloud cloud = _clouds.get(i);
              if (cloud.touchesRay(ray))
              {
                if (_ray != null)
                   _ray.dispose();
                _ray = ray;
  //              _renderDebug = true;
                return true;
              }
            }
  
            if (ray != null)
               ray.dispose();
          }
  
        }
      }
    }
  
    if (touchEvent.getType() == TouchEventType.LongPress)
    {
      if (_ray != null)
         _ray.dispose();
      _ray = null;
    }
  
  //  if (!_renderDebug) {
  //    delete _ray;
  //    _ray = NULL;
  //  }
  
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, deltaHeight, metadataListener, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, float deltaHeight)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, deltaHeight, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, true, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, 1.0f, true, 1.0f, 0, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, boolean verbose)
  {
  
    XPCPointCloud pointCloud = new XPCPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, verbose);
  
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
    _cloudsSize = _clouds.size();
  }

}