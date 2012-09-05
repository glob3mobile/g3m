package org.glob3.mobile.generated; 
//***************************************************************


public class CameraSingleDragHandler extends CameraEventHandler
{

  public CameraSingleDragHandler(boolean useInertia)
  {
	  _camera0 = new Camera(new Camera(0, 0));
	  _initialPoint = new MutableVector3D(0,0,0);
	  _initialPixel = new MutableVector2D(0,0);
	  _useInertia = useInertia;
  }

  public void dispose()
  {
  }


  public final boolean onTouchEvent(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	// only one finger needed
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

  public final void render(RenderContext rc, CameraContext cameraContext)
  {
	// TEMP TO DRAW A POINT WHERE USER PRESS
	if (false)
	{
	  if (cameraContext.getCurrentGesture() == Gesture.Drag)
	  {
		GL gl = rc.getGL();
		float[] vertices = { 0,0,0};
		int[] indices = {0};
		gl.enableVerticesPosition();
		gl.disableTexture2D();
		gl.disableTextures();
		gl.vertexPointer(3, 0, vertices);
		gl.color((float) 0, (float) 1, (float) 0, 1);
		gl.pointSize(60);
		gl.pushMatrix();
		MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(_initialPoint.asVector3D());
		gl.multMatrixf(T);
		gl.drawPoints(1, indices);
		gl.popMatrix();
  
		//Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
		//printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
	  }
	}
  }

  public final boolean _useInertia;
  public final void onDown(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	Camera camera = cameraContext.getNextCamera();
	_camera0.copyFrom(camera);
	cameraContext.setCurrentGesture(Gesture.Drag);
	_axis = MutableVector3D.nan();
	_lastRadians = _radiansStep = 0.0;
  
	// dragging
	final Vector2D pixel = touchEvent.getTouch(0).getPos();
	_initialPixel = pixel.asMutableVector2D();
	_initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  
	//printf ("down 1 finger. Initial point = %f %f %f\n", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());
  }
  public final void onMove(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
	if (cameraContext.getCurrentGesture()!=Gesture.Drag)
	{
	  return;
	}
  
	if (_initialPoint.isNan())
	{
	  return;
	}
  
	final Vector2D pixel = touchEvent.getTouch(0).getPos();
  
  //  const Vector2D pixel = Vector2D(touchEvent.getTouch(0)->getPos().x(), _initialPixel.y());
  
	MutableVector3D finalPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
	if (finalPoint.isNan())
	{
	  //INVALID FINAL POINT
	  //printf ("--invalid final point in drag!!\n");
	  Vector3D ray = _camera0.pixel2Ray(pixel);
	  Vector3D pos = _camera0.getCartesianPosition();
	  finalPoint = eventContext.getPlanet().closestPointToSphere(pos, ray).asMutableVector3D();
	}
  
	// make drag
	Camera camera = cameraContext.getNextCamera();
	camera.copyFrom(_camera0);
	camera.dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  
  
	// save drag parameters
	_axis = _initialPoint.cross(finalPoint);
  
	final double radians = - IMathUtils.instance().asin(_axis.length()/_initialPoint.length()/finalPoint.length());
	_radiansStep = radians - _lastRadians;
	_lastRadians = radians;
  }
  public final void onUp(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	if (_useInertia)
	{
	  // test if animation is needed
	  final Touch touch = touchEvent.getTouch(0);
	  Vector2D currPixel = touch.getPos();
	  Vector2D prevPixel = touch.getPrevPos();
	  double desp = currPixel.sub(prevPixel).length();
  
	  if (cameraContext.getCurrentGesture() == Gesture.Drag && !_axis.isNan() && desp>2)
	  {
		// start inertial effect
		Effect effect = new SingleDragEffect(_axis.asVector3D(), Angle.fromRadians(_radiansStep));
		eventContext.getEffectsScheduler().startEffect(effect, cameraContext);
	  }
	}
  
	// update gesture
	cameraContext.setCurrentGesture(Gesture.None);
	_initialPixel = MutableVector2D.nan();
  }

  private Camera _camera0 ; //Initial Camera saved on Down event

  private MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  private MutableVector2D _initialPixel = new MutableVector2D(); //Initial pixel at start of gesture

  private MutableVector3D _axis = new MutableVector3D();
  private double _lastRadians;
  private double _radiansStep;

}