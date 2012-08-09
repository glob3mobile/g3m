package org.glob3.mobile.generated; 
//***************************************************************


public class CameraDoubleTapHandler extends CameraEventHandler
{


  public void dispose()
  {
  }


  public final boolean onTouchEvent(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
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
  public final int render(RenderContext rc, CameraContext cameraContext)
  {
	return Renderer.maxTimeToRender;
  }


  private void onDown(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	// compute globe point where user tapped
	final Vector2D pixel = touchEvent.getTouch(0).getPos();
	Camera camera = cameraContext.getCamera();
	final Vector3D initialPoint = camera.pixel2PlanetPoint(pixel);
	if (initialPoint.isNan())
		return;
  
	// compute central point of view
	final Vector3D centerPoint = camera.centerOfViewOnPlanet();
  
	// compute drag parameters
	final Vector3D axis = initialPoint.cross(centerPoint);
	final Angle angle = Angle.fromRadians(-Math.asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
	// compute zoom factor
	final double height = eventContext.getPlanet().toGeodetic3D(camera.getPosition()).height();
	final double distance = height * 0.5;
  
	Effect effect = new DoubleTapEffect(TimeInterval.fromSeconds(0.75), axis, angle, distance);
  
	int __check_with_agustin;
	// the EffectTarget has to be the Camera or the Planet.
	// use inheritance for EffectTarget
	eventContext.getEffectsScheduler().startEffect(effect, cameraContext);
  }
  private void onMove(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }
  private void onUp(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

}