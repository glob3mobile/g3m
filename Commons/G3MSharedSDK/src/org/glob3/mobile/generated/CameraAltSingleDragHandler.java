package org.glob3.mobile.generated; 
//
//  CameraAltSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//

//
//  CameraAltSingleDragHandler.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 5/12/14.
//
//





public class CameraAltSingleDragHandler extends CameraEventHandler
{

  private final float _maxHeadingMovementInDegrees;
  private final float _maxPitchMovementInDegrees;

  public CameraAltSingleDragHandler(float maxHeadingMovementInDegrees)
  {
     this(maxHeadingMovementInDegrees, 180);
  }
  public CameraAltSingleDragHandler()
  {
     this(360, 180);
  }
  public CameraAltSingleDragHandler(float maxHeadingMovementInDegrees, float maxPitchMovementInDegrees)
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
    // only one finger needed and Alt
    if (!touchEvent.isAltPressed())
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
  
    cameraContext.setCurrentGesture(Gesture.AltDrag);
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (!touchEvent.isAltPressed())
    {
      cameraContext.setCurrentGesture(Gesture.None);
      return;
    }
  
    if (cameraContext.getCurrentGesture()!=Gesture.AltDrag)
       return;
  
    Camera cam = cameraContext.getNextCamera();
    TaitBryanAngles angles = cam.getHeadingPitchRoll();
  
    final Touch t = touchEvent.getTouch(0);
  
    final Vector2I delta = t.getPos().sub(t.getPrevPos());
  
    final float heading = (((float)delta._x) / cam.getViewPortWidth()) * (_maxHeadingMovementInDegrees * 0.5f);
    final float pitch = (((float)delta._y) / cam.getViewPortHeight()) * (_maxPitchMovementInDegrees * 0.5f);
  
    double finalHeading = angles._heading._degrees + heading;
  
    double finalPitch = angles._pitch._degrees + pitch;
    if (finalPitch < -90) //Boundaries
    {
      finalPitch = -90;
    }
    else if (finalPitch > 90)
    {
      finalPitch = 90;
    }
  
    cam.setHeadingPitchRoll(Angle.fromDegrees(finalHeading), Angle.fromDegrees(finalPitch), angles._roll);
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(Gesture.None);
  }
}