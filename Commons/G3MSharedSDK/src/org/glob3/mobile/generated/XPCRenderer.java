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


public class XPCRenderer extends DefaultRenderer
{

  private ITimer _timer;

  private java.util.ArrayList<XPCPointCloud> _clouds = new java.util.ArrayList<XPCPointCloud>();
  private int _cloudsSize;
  private java.util.ArrayList<String> _errors = new java.util.ArrayList<String>();

  private GLState _glState;



  protected final void onChangedContext()
  {
    for (int i = 0; i < _cloudsSize; i++)
    {
      XPCPointCloud cloud = _clouds.get(i);
      cloud.initialize(_context);
    }
  }




  ///#include "G3MContext.hpp"
  
  
  
  public XPCRenderer()
  {
     _cloudsSize = 0;
     _glState = new GLState();
     _timer = null;
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

  public final void render(G3MRenderContext rc, GLState glState)
  {
    if (_cloudsSize > 0)
    {
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
        cloud.render(rc, _glState, frustum, nowInMS);
      }
    }
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, deltaHeight, metadataListener, deleteMetadataListener, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener)
  {
     addPointCloud(serverURL, cloudName, downloadPriority, timeToCache, readExpired, pointColorizer, deletePointColorizer, minProjectedArea, pointSize, dynamicPointSize, verticalExaggeration, deltaHeight, metadataListener, true, false);
  }
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, double deltaHeight)
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
  public final void addPointCloud(URL serverURL, String cloudName, long downloadPriority, TimeInterval timeToCache, boolean readExpired, XPCPointColorizer pointColorizer, boolean deletePointColorizer, double minProjectedArea, float pointSize, boolean dynamicPointSize, float verticalExaggeration, double deltaHeight, XPCMetadataListener metadataListener, boolean deleteMetadataListener, boolean verbose)
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