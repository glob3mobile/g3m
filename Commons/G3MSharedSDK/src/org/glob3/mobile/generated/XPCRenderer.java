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
//class XPCPointSelectionListener;
//class Camera;
//class XPCSelectionResult;


public class XPCRenderer extends DefaultRenderer
{

  private ITimer _timer;

  private java.util.ArrayList<XPCPointCloud> _clouds = new java.util.ArrayList<XPCPointCloud>();
  private int _cloudsSize;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

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

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, XPCPointSelectionListener pointSelectionListener, boolean deletePointSelectionListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, pointSelectionListener, deletePointSelectionListener, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, XPCPointSelectionListener pointSelectionListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, pointSelectionListener, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, null, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, 0, null, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, 1.0f, 0, null, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, true, 1.0f, 0, null, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, true, true, 1.0f, 0, null, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, 1.0f, true, true, 1.0f, 0, null, true, null, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, boolean depthTest, float verticalExaggeration, float deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, XPCPointSelectionListener pointSelectionListener, boolean deletePointSelectionListener, boolean verbose)
  {
  
    XPCPointCloud pointCloud = new XPCPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, depthTest, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, pointSelectionListener, deletePointSelectionListener, verbose);
  
    if (_context != null)
    {
      pointCloud.initialize(_context);
    }
    _clouds.add(pointCloud);
    _cloudsSize = _clouds.size();
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
        cloud.render(rc, _lastSplitTimer, _glState, frustum, nowInMS, _renderDebug, _selectionResult);
      }
  
  //    if (_selectionResult != NULL) {
  //      _selectionResult->render(rc, _glState);
  //    }
    }
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    _renderDebug = false;
  
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
            // _renderDebug = true;
  
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
  
    //  if (!_renderDebug) {
    //    delete _ray;
    //    _ray = NULL;
    //  }
  
    return false;
  }

}