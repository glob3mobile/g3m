package org.glob3.mobile.generated; 
//
//  CameraCtrlSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//

//
//  CameraCtrlSingleDragHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//





public class CameraCtrlSingleDragHandler extends CameraEventHandler
{

  private final float _maxHeadingMovementInDegrees;
  private final float _maxPitchMovementInDegrees;

  public CameraCtrlSingleDragHandler(float maxHeadingMovementInDegrees)
  {
     this(maxHeadingMovementInDegrees, 180);
  }
  public CameraCtrlSingleDragHandler()
  {
     this(360, 180);
  }
  public CameraCtrlSingleDragHandler(float maxHeadingMovementInDegrees, float maxPitchMovementInDegrees)
  {
     _maxHeadingMovementInDegrees = maxHeadingMovementInDegrees;
     _maxPitchMovementInDegrees = maxPitchMovementInDegrees;
  }

  public void dispose()
  {
    super.dispose();

  }


  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    // only one finger needed and Ctrl
    if (!touchEvent.isCtrlPressed())
    {
       return false;
    }
    if (touchEvent.getTouchCount()!=1)
       return false;
    if (touchEvent.getTapCount()>1)
       return false;
  
    switch (touchEvent.getType())
    {
      case Down:
        onDown(eventContext, touchEvent, cameraContext);
        break;
      case Move:
        onMove(eventContext, touchEvent, cameraContext);
        break;
      case Up:
        onUp(eventContext, touchEvent, cameraContext);
      default:
        break;
    }
  
    return true;
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    cameraContext.setCurrentGesture(Gesture.CtrlDrag);
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (!touchEvent.isCtrlPressed())
    {
      cameraContext.setCurrentGesture(Gesture.None);
      return;
    }
  
    if (cameraContext.getCurrentGesture()!=Gesture.CtrlDrag)
       return;
  
    Camera cam = cameraContext.getNextCamera();
    TaitBryanAngles angles = cam.getHeadingPitchRoll();
  
    final Touch t = touchEvent.getTouch(0);
  
    final Vector2I delta = t.getPos().sub(t.getPrevPos());
  
    final float heading = (((float)delta._x) / cam.getViewPortWidth()) * (_maxHeadingMovementInDegrees * 0.5f);
    final float pitch = (((float)delta._y) / cam.getViewPortHeight()) * (_maxPitchMovementInDegrees * 0.5f);
  
    cam.setHeadingPitchRoll(angles._heading.add(Angle.fromDegrees(heading)), angles._pitch.add(Angle.fromDegrees(pitch)), angles._roll);
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(Gesture.None);
  }
}