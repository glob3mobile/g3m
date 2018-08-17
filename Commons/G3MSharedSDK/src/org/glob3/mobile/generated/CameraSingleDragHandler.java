package org.glob3.mobile.generated;//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//


//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//






public class CameraSingleDragHandler extends CameraEventHandler
{

  public CameraSingleDragHandler(boolean useInertia)
  {
	  _useInertia = useInertia;
  }

  public void dispose()
  {
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  super.dispose();
//#endif

  }


  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
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

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  //  // TEMP TO DRAW A POINT WHERE USER PRESS
  //  if (false) {
  //    if (cameraContext->getCurrentGesture() == Drag) {
  //      GL* gl = rc->getGL();
  //      float vertices[] = { 0,0,0};
  //      int indices[] = {0};
  //      gl->enableVerticesPosition();
  //      gl->disableTexture2D();
  //      gl->disableTextures();
  //      gl->vertexPointer(3, 0, vertices);
  //      gl->color((float) 0, (float) 1, (float) 0, 1);
  //      gl->pointSize(60);
  //      gl->pushMatrix();
  //      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
  //      gl->multMatrixf(T);
  //      gl->drawPoints(1, indices);
  //      gl->popMatrix();
  //
  //      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
  //      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
  //    }
  //  }
  }

  public final boolean _useInertia;
  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	Camera camera = cameraContext.getNextCamera();
	tangible.RefObject<MutableVector3D> tempRef__cameraPosition = new tangible.RefObject<MutableVector3D>(_cameraPosition);
	tangible.RefObject<MutableVector3D> tempRef__cameraCenter = new tangible.RefObject<MutableVector3D>(_cameraCenter);
	tangible.RefObject<MutableVector3D> tempRef__cameraUp = new tangible.RefObject<MutableVector3D>(_cameraUp);
	camera.getLookAtParamsInto(tempRef__cameraPosition, tempRef__cameraCenter, tempRef__cameraUp);
	_cameraPosition = tempRef__cameraPosition.argvalue;
	_cameraCenter = tempRef__cameraCenter.argvalue;
	_cameraUp = tempRef__cameraUp.argvalue;
	tangible.RefObject<MutableMatrix44D> tempRef__cameraModelViewMatrix = new tangible.RefObject<MutableMatrix44D>(_cameraModelViewMatrix);
	camera.getModelViewMatrixInto(tempRef__cameraModelViewMatrix);
	_cameraModelViewMatrix = tempRef__cameraModelViewMatrix.argvalue;
	tangible.RefObject<MutableVector2I> tempRef__cameraViewPort = new tangible.RefObject<MutableVector2I>(_cameraViewPort);
	camera.getViewPortInto(tempRef__cameraViewPort);
	_cameraViewPort = tempRef__cameraViewPort.argvalue;
  
	// dragging
	final Vector2F pixel = touchEvent.getTouch(0).getPos();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D& initialRay = camera->pixel2Ray(pixel);
	final Vector3D initialRay = camera.pixel2Ray(new Vector2F(pixel));
	if (!initialRay.isNan())
	{
	  cameraContext.setCurrentGesture(Gesture.Drag);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: eventContext->getPlanet()->beginSingleDrag(camera->getCartesianPosition(),initialRay);
	  eventContext.getPlanet().beginSingleDrag(camera.getCartesianPosition(), new Vector3D(initialRay));
	}
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
	if (cameraContext.getCurrentGesture()!=Gesture.Drag)
		return;
  
	//check finalRay
	final Vector2F pixel = touchEvent.getTouch(0).getPos();
	tangible.RefObject<MutableVector3D> tempRef__finalRay = new tangible.RefObject<MutableVector3D>(_finalRay);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: Camera::pixel2RayInto(_cameraPosition, pixel, _cameraViewPort, _cameraModelViewMatrix, _finalRay);
	Camera.pixel2RayInto(new MutableVector3D(_cameraPosition), new Vector2F(pixel), new MutableVector2I(_cameraViewPort), new MutableMatrix44D(_cameraModelViewMatrix), tempRef__finalRay);
	_finalRay = tempRef__finalRay.argvalue;
	if (_finalRay.isNan())
		return;
  
	// compute transformation matrix
	final Planet planet = eventContext.getPlanet();
	MutableMatrix44D matrix = planet.singleDrag(_finalRay.asVector3D());
	if (!matrix.isValid())
		return;
  
	// apply transformation
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: cameraContext->getNextCamera()->setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0), _cameraCenter.transformedBy(matrix, 1.0), _cameraUp.transformedBy(matrix, 0.0));
	cameraContext.getNextCamera().setLookAtParams(_cameraPosition.transformedBy(new MutableMatrix44D(matrix), 1.0), _cameraCenter.transformedBy(new MutableMatrix44D(matrix), 1.0), _cameraUp.transformedBy(new MutableMatrix44D(matrix), 0.0));
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	final Planet planet = eventContext.getPlanet();
  
	// test if animation is needed
	if (_useInertia)
	{
	  final Touch touch = touchEvent.getTouch(0);
	  final Vector2F currPixel = touch.getPos();
	  final Vector2F prevPixel = touch.getPrevPos();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const double desp = currPixel.sub(prevPixel).length();
	  final double desp = currPixel.sub(new Vector2F(prevPixel)).length();
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning method getPixelsInMM is ! working fine in iOS devices
	  final float delta = IFactory.instance().getDeviceInfo().getPixelsInMM(0.2f);
  
	  if ((cameraContext.getCurrentGesture() == Gesture.Drag) && (desp > delta))
	  {
		Effect effect = planet.createEffectFromLastSingleDrag();
		if (effect != null)
		{
		  EffectTarget target = cameraContext.getNextCamera().getEffectTarget();
		  eventContext.getEffectsScheduler().startEffect(effect, target);
		}
	  }
	}
  
	// update gesture
	cameraContext.setCurrentGesture(Gesture.None);
  }

  private MutableVector3D _cameraPosition = new MutableVector3D();
  private MutableVector3D _cameraCenter = new MutableVector3D();
  private MutableVector3D _cameraUp = new MutableVector3D();
  private MutableVector2I _cameraViewPort = new MutableVector2I();
  private MutableMatrix44D _cameraModelViewMatrix = new MutableMatrix44D();
  private MutableVector3D _finalRay = new MutableVector3D();
}
