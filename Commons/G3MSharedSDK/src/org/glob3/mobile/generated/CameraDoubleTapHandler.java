package org.glob3.mobile.generated; 
//***************************************************************


public class CameraDoubleTapHandler extends CameraEventHandler
{


  public void dispose()
  {
  super.dispose();

  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    // only one finger needed
    if (touchEvent.getTouchCount()!=1)
       return false;
    if (touchEvent.getTapCount()!=2)
       return false;
    if (touchEvent.getType()!=TouchEventType.Down)
       return false;
  
    onDown(eventContext, touchEvent, cameraContext);
    return true;
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {

  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    // compute globe point where user tapped
    final Vector2I pixel = touchEvent.getTouch(0).getPos();
    Camera camera = cameraContext.getNextCamera();
    final Vector3D initialPoint = camera.pixel2PlanetPoint(pixel);
    if (initialPoint.isNan())
       return;
  
    // compute central point of view
    final Vector3D centerPoint = camera.getXYZCenterOfView();
  
    // compute drag parameters
    final Vector3D axis = initialPoint.cross(centerPoint);
    final Angle angle = Angle.fromRadians(- IMathUtils.instance().asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
    // compute zoom factor
    final double height = camera.getGeodeticPosition()._height;
    final double distance = height * 0.6;
  
    // create effect
    Effect effect = new DoubleTapEffect(TimeInterval.fromSeconds(0.75), axis, angle, distance);
  
    EffectTarget target = cameraContext.getNextCamera().getEffectTarget();
    eventContext.getEffectsScheduler().startEffect(effect, target);
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

}