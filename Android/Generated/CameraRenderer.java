package org.glob3.mobile.generated; 
public class CameraRenderer extends Renderer
{

  private Camera _camera; //Camera used at current frame
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE //Planet

  private Camera _camera0 ; //Initial Camera saved on Down event
  private MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  private MutableVector3D _initialPixel = new MutableVector3D(); //Initial pixel at start of gesture

  private Gesture _currentGesture; //Gesture the user is making at the moment

  private void onDown(TouchEvent touchEvent)
  {
	//Saving Camera0
	Camera c = new Camera(_camera);
	_camera0 = c;
  
	//Initial Point for Dragging
	Vector2D pixel = touchEvent.getTouch(0).getPos();
	Vector3D ray = _camera0.pixel2Vector(pixel);
	_initialPoint = _planet.closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
	_currentGesture = Gesture.Drag; //Dragging
  }
  private void onMove(TouchEvent touchEvent)
  {
	_currentGesture = getGesture(touchEvent);
  
	switch (_currentGesture)
	{
	  case Drag:
		makeDrag(touchEvent);
		break;
	  case Zoom:
		makeZoom(touchEvent);
		break;
	  case Rotate:
		makeRotate(touchEvent);
		break;
	  default:
		break;
	}
  }
  private void onUp(TouchEvent touchEvent)
  {
	_currentGesture = Gesture.None;
	_initialPixel = Vector3D.nan().asMutableVector3D();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Gesture getGesture(const TouchEvent& touchEvent) const
  private Gesture getGesture(TouchEvent touchEvent)
  {
	int n = touchEvent.getNumTouch();
	if (n == 1)
	{
	  //Dragging
	  if (_currentGesture == Gesture.Drag)
		return Gesture.Drag;
	  else
		return Gesture.None;
	}
  
	if (n == 2)
	{
  
	  //If the gesture is set we don't have to change it
	  if (_currentGesture == Gesture.Zoom)
		  return Gesture.Zoom;
	  if (_currentGesture == Gesture.Rotate)
		  return Gesture.Rotate;
  
	  //We have to fingers and the previous event was Drag
	  Vector2D pixel0 = touchEvent.getTouch(0).getPos();
	  Vector2D pixel1 = touchEvent.getTouch(1).getPos();
  
	  Vector2D prevPixel0 = touchEvent.getTouch(0).getPrevPos();
	  Vector2D prevPixel1 = touchEvent.getTouch(1).getPrevPos();
  
	  //If both fingers go in the same direction we should rotate the camera
	  if ((pixel0.y() > prevPixel0.y() && pixel1.y() > prevPixel1.y()) || (pixel0.x() > prevPixel0.x() && pixel1.x() > prevPixel1.x()) || (pixel0.y() < prevPixel0.y() && pixel1.y() < prevPixel1.y()) || (pixel0.x() < prevPixel0.x() && pixel1.x() < prevPixel1.x()))
	  {
		return Gesture.Rotate;
	  }
	  else
	  {
  
		//If fingers are diverging it is zoom
		return Gesture.Zoom;
	  }
  
	}
	return Gesture.None;
  }

  private void makeDrag(TouchEvent touchEvent)
  {
	if (!_initialPoint.isNan()) //VALID INITIAL POINT
	{
	  Vector2D pixel = touchEvent.getTouch(0).getPos();
	  Vector3D ray = _camera0.pixel2Vector(pixel);
	  Vector3D pos = _camera0.getPos();
  
	  MutableVector3D finalPoint = _planet.closestIntersection(pos, ray).asMutableVector3D();
	  if (finalPoint.isNan()) //INVALID FINAL POINT
	  {
		finalPoint = _planet.closestPointToSphere(pos, ray).asMutableVector3D();
	  }
  
	  _camera.copyFrom(_camera0);
	  _camera.dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
	}
  }
  private void makeZoom(TouchEvent touchEvent)
  {
	Vector2D pixel0 = touchEvent.getTouch(0).getPos();
	Vector2D pixel1 = touchEvent.getTouch(1).getPos();
	Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
  
	Vector3D ray = _camera0.pixel2Vector(pixelCenter);
	_initialPoint = _planet.closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
  
	Vector2D centerOfViewport = new Vector2D(_camera0.getWidth() / 2, _camera0.getHeight() / 2);
	Vector3D ray2 = _camera0.pixel2Vector(centerOfViewport);
	Vector3D pointInCenterOfView = _planet.closestIntersection(_camera0.getPos(), ray2);
  
	//IF CENTER PIXEL INTERSECTS THE PLANET
	if (!_initialPoint.isNan())
	{
  
	  //IF THE CENTER OF THE VIEW INTERSECTS THE PLANET
	  if (!pointInCenterOfView.isNan())
	  {
  
		Vector2D prevPixel0 = touchEvent.getTouch(0).getPrevPos();
		Vector2D prevPixel1 = touchEvent.getTouch(1).getPrevPos();
  
		double dist = pixel0.sub(pixel1).length();
		double prevDist = prevPixel0.sub(prevPixel1).length();
  
		Vector2D pixelDelta = pixel1.sub(pixel0);
		Vector2D prevPixelDelta = prevPixel1.sub(prevPixel0);
  
		Angle angle = pixelDelta.angle();
		Angle prevAngle = prevPixelDelta.angle();
  
		//We rotate and zoom the camera with the same gesture
		_camera.zoom(prevDist /dist);
		_camera.pivotOnCenter(angle.sub(prevAngle));
	  }
	}
  }
  private void makeRotate(TouchEvent touchEvent)
  {
	int todo_JM_working;
  
	Vector2D pixel0 = touchEvent.getTouch(0).getPos();
	Vector2D pixel1 = touchEvent.getTouch(1).getPos();
	Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
  
	//The gesture is starting
	if (_initialPixel.isNan())
	{
	  Vector3D v = new Vector3D(pixelCenter.x(), pixelCenter.y(), 0);
	  _initialPixel = v.asMutableVector3D(); //Storing starting pixel
	}
  
	Vector3D ray = _camera0.pixel2Vector(pixelCenter);
	_initialPoint = _planet.closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
  
	//Calculating the point we are going to rotate around
	Vector2D centerViewport = new Vector2D(_camera.getWidth() /2, _camera.getHeight() /2);
	Vector3D rayCV = _camera0.pixel2Vector(pixelCenter);
	Vector3D rotatingPoint = _planet.closestIntersection(_camera0.getPos(), rayCV);
  
	//We don't rotate
	if (_initialPoint.isNan() || rotatingPoint.isNan())
		return;
  
	//Rotating axis
	Vector3D camVec = _camera0.getPos().sub(_camera0.getCenter());
	Vector3D normal = _planet.geodeticSurfaceNormal(rotatingPoint);
	Vector3D horizontalAxis = normal.cross(camVec);
  
	//Calculating the angle we have to rotate the camera vertically
	double distY = pixelCenter.y() - _initialPixel.y();
	Angle horizontalAngle = Angle.fromDegrees(((double)distY / (double)_camera0.getHeight()) * 180.0);
  
	//We don't put the camera upside down
	if (horizontalAngle.degrees() < 0.0 || horizontalAngle.degrees() > 85.0)
		return;
	System.out.printf("%f\n", horizontalAngle.degrees());
  
	//Back-Up camera
	Camera cameraAux = new Camera(0,0);
	cameraAux.copyFrom(_camera);
  
	//Apply rotation
	_camera.copyFrom(_camera0);
	_camera.rotateWithAxisAndPoint(horizontalAxis, _initialPoint.asVector3D(), horizontalAngle);
  
	//If the final camera don't intersect the planet we don't apply the transformation
  
	Vector3D newCamVec = _camera.getPos().sub(_camera.getCenter());
	Angle a = Angle.fromRadians(newCamVec.angleBetween(normal));
	System.out.printf("ANGLE NCV : %f\n", a.degrees());
  
	//If the camera is too low or doesn't intersect the planet
	if ((a.degrees() > 85.0) || !cameraLooksToPlanet(_camera))
	{
	  System.out.print("MOVEMENT NOT ALLOWED\n");
	  // we don't apply the transformation
	  _camera.copyFrom(cameraAux);
	}
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean cameraLooksToPlanet(const Camera& c) const
  private boolean cameraLooksToPlanet(Camera c)
  {
	  int todo_JM_working;
  
	Vector2D centerViewport = new Vector2D(c.getWidth() /2, c.getHeight() /2);
	Vector3D rayCV = c.pixel2Vector(centerViewport);
	return ! _planet.closestIntersection(c.getPos(), rayCV).isNan();
  }


  public CameraRenderer()
  {
	  _camera0 = new Camera(0,0);
	  _initialPoint = new MutableVector3D(0,0,0);
	  _currentGesture = Gesture.None;
	  _camera = null;
	  _initialPixel = new MutableVector3D(0,0,0);
  }

  public final void initialize(InitializationContext ic)
  {
  }

  public final int render(RenderContext rc)
  {
	_camera = rc.getCamera(); //Saving camera reference
	_planet = rc.getPlanet();
  
	rc.getCamera().draw(rc); //We "draw" the camera with IGL
	return 0;
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	switch (touchEvent.getType())
	{
	  case Down:
		onDown(touchEvent);
		break;
	  case Move:
		onMove(touchEvent);
		break;
	  case Up:
		onUp(touchEvent);
	  default:
		break;
	}
  
	return true;
  }

  public final boolean onResizeViewportEvent(int width, int height)
  {
	if (_camera != null)
	{
	  _camera.resizeViewport(width, height);
	  return true;
	}
	else
	  return false;
  }


}