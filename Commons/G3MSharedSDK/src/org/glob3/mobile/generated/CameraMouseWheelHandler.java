package org.glob3.mobile.generated; 
//
//  CameraMouseWheelHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//

//
//  CameraMouseWheelHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 10/10/14.
//
//



public class CameraMouseWheelHandler extends CameraEventHandler
{
  private final double _factor;
  public CameraMouseWheelHandler()
  {
     this(0.1);
  }
  public CameraMouseWheelHandler(double factor)
  {
     _factor = factor;
  }

  public void dispose()
  {
    super.dispose();

  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (touchEvent.getType() == TouchEventType.MouseWheelChanged)
    {
      onMouseWheel(eventContext, touchEvent, cameraContext);
      return true;
    }
    return false;
  
  /*
    /**** THIS CODE IS TO TEST MOUSEWHEELHANDLER
    // only one finger needed
    if (touchEvent->getTouchCount()!=1) return false;
    if (touchEvent->getTapCount()>1) return false;
    if (touchEvent->getType() == MouseWheelChanged){
      return false;
    }
    switch (touchEvent->getType()) {
      case Down:
        onMouseWheel(eventContext, *touchEvent, cameraContext);
        break;
      default:
        break;
    }
    return true;
  */
  
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

  public final void onMouseWheel(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    MutableVector3D cameraPosition = new MutableVector3D();
    MutableVector3D cameraCenter = new MutableVector3D();
    MutableVector3D cameraUp = new MutableVector3D();
    MutableVector2I cameraViewPort = new MutableVector2I();
    MutableMatrix44D cameraModelViewMatrix = new MutableMatrix44D();
  
    // save params
    Camera camera = cameraContext.getNextCamera();
    camera.getLookAtParamsInto(cameraPosition, cameraCenter, cameraUp);
    camera.getModelViewMatrixInto(cameraModelViewMatrix);
    camera.getViewPortInto(cameraViewPort);
  
    final double delta = touchEvent.getMouseWheelDelta();
  
    double factor = delta< 0? - _factor : _factor;
  
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    Vector3D touchedPosition = camera.getScenePositionForPixel(pixel._x, pixel._y);
    if (touchedPosition.isNan())
       return;
  
    final Vector3D initialRay = Camera.pixel2Ray(cameraPosition, pixel, cameraViewPort, cameraModelViewMatrix);
    if (initialRay.isNan())
       return;
  
    final Planet planet = eventContext.getPlanet();
    MutableMatrix44D matrix = planet.zoomUsingMouseWheel(factor, camera.getCartesianPosition(), camera.getViewDirection(), camera.getScenePositionForCentralPixel(), touchedPosition, initialRay);
    if (!matrix.isValid())
       return;
  
    // apply transformation
    cameraContext.getNextCamera().setLookAtParams(cameraPosition.transformedBy(matrix, 1.0), cameraCenter.transformedBy(matrix, 1.0), cameraUp.transformedBy(matrix, 0.0));
  
  }

}