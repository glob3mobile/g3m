package org.glob3.mobile.generated;
//
//  CameraDoubleDragHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//

//
//  CameraDoubleDragHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//






public class CameraDoubleDragHandler extends CameraEventHandler
{
  private MutableVector3D _cameraPosition = new MutableVector3D();
  private MutableVector3D _cameraCenter = new MutableVector3D();
  private MutableVector3D _cameraUp = new MutableVector3D();
  private MutableVector2I _cameraViewPort = new MutableVector2I();
  private MutableMatrix44D _cameraModelViewMatrix = new MutableMatrix44D();

  private boolean onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final Camera camera = cameraContext.getNextCamera();
    camera.getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
    camera.getModelViewMatrixInto(_cameraModelViewMatrix);
    camera.getViewPortInto(_cameraViewPort);
  
    // double dragging
    final Vector2F pixel0 = touchEvent.getTouch(0).getPos();
    final Vector2F pixel1 = touchEvent.getTouch(1).getPos();
  
    final Vector3D initialRay0 = camera.pixel2Ray(pixel0);
    final Vector3D initialRay1 = camera.pixel2Ray(pixel1);
  
    if (initialRay0.isNan() || initialRay1.isNan())
    {
      return false;
    }
  
    cameraContext.setCurrentGesture(CameraEventGesture.DoubleDrag);
    eventContext.getPlanet().beginDoubleDrag(camera.getCartesianPosition(), camera.getViewDirection(), camera.pixel2Ray(pixel0), camera.pixel2Ray(pixel1));
  
    return true;
  }
  private boolean onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    if (cameraContext.getCurrentGesture() != CameraEventGesture.DoubleDrag)
    {
      //ILogger::instance()->logError("** getCurrentGesture is not DoubleDrag");
      return false;
    }
  
    // compute transformation matrix
    final Planet planet = eventContext.getPlanet();
    final Vector2F pixel0 = touchEvent.getTouch(0).getPos();
    final Vector2F pixel1 = touchEvent.getTouch(1).getPos();
    final Vector3D initialRay0 = Camera.pixel2Ray(_cameraPosition, pixel0, _cameraViewPort, _cameraModelViewMatrix);
    final Vector3D initialRay1 = Camera.pixel2Ray(_cameraPosition, pixel1, _cameraViewPort, _cameraModelViewMatrix);
  
    if (initialRay0.isNan() || initialRay1.isNan())
    {
      //ILogger::instance()->logError("** Invalid rays");
      return false;
    }
  
    MutableMatrix44D matrix = planet.doubleDrag(initialRay0, initialRay1);
    if (!matrix.isValid())
    {
      //ILogger::instance()->logError("** Invalid matrix");
      return false;
    }
  
    // apply transformation
    cameraContext.getNextCamera().setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0), _cameraCenter.transformedBy(matrix, 1.0), _cameraUp.transformedBy(matrix, 0.0));
  
    return true;
  }
  private boolean onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(CameraEventGesture.None);
    return true;
  }

  public CameraDoubleDragHandler()
  {
     super("DoubleDrag");
  }

  public void dispose()
  {
    super.dispose();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    // only one finger needed
    if (touchEvent.getTouchCount() != 2)
    {
      return false;
    }
  
    switch (touchEvent.getType())
    {
      case Down:
        return onDown(eventContext, touchEvent, cameraContext);
  
      case Move:
        return onMove(eventContext, touchEvent, cameraContext);
  
      case Up:
        return onUp(eventContext, touchEvent, cameraContext);
  
      case LongPress:
      case DownUp:
      case MouseWheel:
      default:
        return false;
    }
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {

  }

}