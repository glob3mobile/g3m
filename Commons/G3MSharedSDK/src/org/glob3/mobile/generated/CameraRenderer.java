package org.glob3.mobile.generated; 
public class CameraRenderer implements ProtoRenderer
{
  private boolean _processTouchEvents;
  private java.util.ArrayList<CameraEventHandler> _handlers = new java.util.ArrayList<CameraEventHandler>();
  private CameraContext _cameraContext;
  private MeshRenderer _meshRenderer;


  public CameraRenderer()
  {
     _cameraContext = null;
     _processTouchEvents = true;
     _meshRenderer = null;
  }

  public void dispose()
  {
    if (_cameraContext != null)
       _cameraContext.dispose();
    final int handlersSize = _handlers.size();
    for (int i = 0; i < handlersSize; i++)
    {
      CameraEventHandler handler = _handlers.get(i);
      if (handler != null)
         handler.dispose();
    }
  }

  public final void addHandler(CameraEventHandler handler)
  {
    _handlers.add(handler);
  }

  public final void setProcessTouchEvents(boolean processTouchEvents)
  {
    _processTouchEvents = processTouchEvents;
  }

  public final void render(G3MRenderContext rc, GLState glState)
  {
  
    // create the CameraContext
    if (_cameraContext == null)
    {
      _cameraContext = new CameraContext(Gesture.None, rc.getNextCamera());
    }
  
    // render camera object
  //  rc->getCurrentCamera()->render(rc, parentState);
  
    final int handlersSize = _handlers.size();
    for (int i = 0; i < handlersSize; i++)
    {
      _handlers.get(i).render(rc, _cameraContext);
    }
  }

  public final void initialize(G3MContext context)
  {

  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_processTouchEvents)
    {
      // abort all the camera effect currently running
      if (touchEvent.getType() == TouchEventType.Down)
      {
        EffectTarget target = _cameraContext.getNextCamera().getEffectTarget();
        ec.getEffectsScheduler().cancelAllEffectsFor(target);
      }
  
      Vector3D cameraPos = _cameraContext.getNextCamera().getCartesianPosition();
      MarksRenderer marksRenderer = ec.getWidget().getMarksRenderer();
      if (marksRenderer != null)
      {
        java.util.ArrayList<Mark> marks = marksRenderer.getMarks();
        for (int i = 0; i<marks.size(); i++)
        {
          Mark mark = marks.get(i);
          Vector3D posMark = mark.getCartesianPosition(ec.getPlanet());
          double distCamMark = cameraPos.distanceTo(posMark);
          if (distCamMark > mark.getMinDistanceToCamera())
             continue;
          Vector2F pixel = _cameraContext.getNextCamera().point2Pixel(posMark);
          Vector3D posZRender = ec.getWidget().getScenePositionForPixel((int)(pixel._x+0.5), (int)(pixel._y+0.5));
          /*
           printf("marca %d: \n",i);
           Geodetic3D geoMark = ec->getPlanet()->toGeodetic3D(*posMark);
           printf("   posMark en %f %f %f   Geo=%f %f %f\n", posMark->_x, posMark->_y, posMark->_z,
           geoMark._latitude._degrees, geoMark._longitude._degrees, geoMark._height);
           Geodetic3D geoZRender = ec->getPlanet()->toGeodetic3D(posZRender);
           printf("   posZrender en %f %f %f   Geo=%f %f %f\n", posZRender._x, posZRender._y, posZRender._z,
           geoZRender._latitude._degrees, geoZRender._longitude._degrees, geoZRender._height);
           double distanceLatLon = sqrt((geoMark._latitude._degrees-geoZRender._latitude._degrees)*
           (geoMark._latitude._degrees-geoZRender._latitude._degrees)+
           (geoMark._longitude._degrees-geoZRender._longitude._degrees)*
           (geoMark._longitude._degrees-geoZRender._longitude._degrees));*/
  
          double distCamTerrain = cameraPos.distanceTo(posZRender);
          //printf ("distCanMark=%f   distCamTerrain=%f   Factor=%f\n", distCamMark, distCamTerrain, distCamMark/distCamTerrain);
          if (distCamMark/distCamTerrain<1.2)
            mark.setVisible(true);
          else
            mark.setVisible(false);
        }
  
        // this call is needed at this point. I don't know why
        ec.getWidget().getScenePositionForCentralPixel();
      }
  
      // pass the event to all the handlers
      final int handlersSize = _handlers.size();
      for (int i = 0; i < handlersSize; i++)
      {
        if (_handlers.get(i).onTouchEvent(ec, touchEvent, _cameraContext))
        {
  
  
    /*
          TaitBryanAngles angles = _cameraContext->getNextCamera()->getHeadingPitchRoll();
          printf ("angulos camera: pitch=%:2f  heading=%.2f   roll=%.2f\n",
                  angles._pitch._degrees, angles._heading._degrees, angles._roll._degrees);
    */
  
          return true;
        }
      }
  
  
    }
  
    // if no handler processed the event, return not-handled
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final void start(G3MRenderContext rc)
  {

  }

  public final void stop(G3MRenderContext rc)
  {

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

  public final void zRender(G3MRenderContext rc, GLState glState)
  {
  }

  public final void setDebugMeshRenderer(MeshRenderer meshRenderer)
  {
    _meshRenderer = meshRenderer;
    for (int n = 0; n<_handlers.size(); n++)
      _handlers.get(n).setDebugMeshRenderer(meshRenderer);
  }
}