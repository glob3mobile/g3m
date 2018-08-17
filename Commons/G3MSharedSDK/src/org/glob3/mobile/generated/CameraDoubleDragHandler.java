package org.glob3.mobile.generated;//
//  CameraDoubleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//

//
//  CameraDoubleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//





public class CameraDoubleDragHandler extends CameraEventHandler
{

  public CameraDoubleDragHandler()
  {
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
	if (touchEvent.getTouchCount()!=2)
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
  //    if (cameraContext->getCurrentGesture() == DoubleDrag) {
  //      GL* gl = rc->getGL();
  //      float vertices[] = { 0,0,0};
  //      int indices[] = {0};
  //      gl->enableVerticesPosition();
  //      gl->disableTexture2D();
  //      gl->disableTextures();
  //      gl->vertexPointer(3, 0, vertices);
  //      gl->color((float) 1, (float) 1, (float) 1, 1);
  //      gl->pointSize(10);
  //      gl->pushMatrix();
  //      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
  //      gl->multMatrixf(T);
  //      gl->drawPoints(1, indices);
  //      gl->popMatrix();
  //
  //      // draw each finger
  //      gl->pointSize(60);
  //      gl->pushMatrix();
  //      MutableMatrix44D T0 = MutableMatrix44D::createTranslationMatrix(_initialPoint0.asVector3D());
  //      gl->multMatrixf(T0);
  //      gl->drawPoints(1, indices);
  //      gl->popMatrix();
  //      gl->pushMatrix();
  //      MutableMatrix44D T1 = MutableMatrix44D::createTranslationMatrix(_initialPoint1.asVector3D());
  //      gl->multMatrixf(T1);
  //      gl->drawPoints(1, indices);
  //      gl->popMatrix();
  //
  //
  //      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
  //      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
  //    }
  //  }
  
  }

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
  
	// double dragging
	final Vector2F pixel0 = touchEvent.getTouch(0).getPos();
	final Vector2F pixel1 = touchEvent.getTouch(1).getPos();
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D& initialRay0 = camera->pixel2Ray(pixel0);
	final Vector3D initialRay0 = camera.pixel2Ray(new Vector2F(pixel0));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D& initialRay1 = camera->pixel2Ray(pixel1);
	final Vector3D initialRay1 = camera.pixel2Ray(new Vector2F(pixel1));
  
	if (initialRay0.isNan() || initialRay1.isNan())
		return;
  
	cameraContext.setCurrentGesture(Gesture.DoubleDrag);
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: eventContext->getPlanet()->beginDoubleDrag(camera->getCartesianPosition(), camera->getViewDirection(), camera->pixel2Ray(pixel0), camera->pixel2Ray(pixel1));
	eventContext.getPlanet().beginDoubleDrag(camera.getCartesianPosition(), camera.getViewDirection(), camera.pixel2Ray(new Vector2F(pixel0)), camera.pixel2Ray(new Vector2F(pixel1)));
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	if (cameraContext.getCurrentGesture() != Gesture.DoubleDrag)
		return;
  
	// compute transformation matrix
	final Planet planet = eventContext.getPlanet();
	final Vector2F pixel0 = touchEvent.getTouch(0).getPos();
	final Vector2F pixel1 = touchEvent.getTouch(1).getPos();
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D& initialRay0 = Camera::pixel2Ray(_cameraPosition, pixel0, _cameraViewPort, _cameraModelViewMatrix);
	final Vector3D initialRay0 = Camera.pixel2Ray(new MutableVector3D(_cameraPosition), new Vector2F(pixel0), new MutableVector2I(_cameraViewPort), new MutableMatrix44D(_cameraModelViewMatrix));
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: const Vector3D& initialRay1 = Camera::pixel2Ray(_cameraPosition, pixel1, _cameraViewPort, _cameraModelViewMatrix);
	final Vector3D initialRay1 = Camera.pixel2Ray(new MutableVector3D(_cameraPosition), new Vector2F(pixel1), new MutableVector2I(_cameraViewPort), new MutableMatrix44D(_cameraModelViewMatrix));
  
	 if (initialRay0.isNan() || initialRay1.isNan())
		 return;
  
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: MutableMatrix44D matrix = planet->doubleDrag(initialRay0, initialRay1);
	MutableMatrix44D matrix = planet.doubleDrag(new Vector3D(initialRay0), new Vector3D(initialRay1));
	if (!matrix.isValid())
		return;
  
	// apply transformation
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: cameraContext->getNextCamera()->setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0), _cameraCenter.transformedBy(matrix, 1.0), _cameraUp.transformedBy(matrix, 0.0));
	cameraContext.getNextCamera().setLookAtParams(_cameraPosition.transformedBy(new MutableMatrix44D(matrix), 1.0), _cameraCenter.transformedBy(new MutableMatrix44D(matrix), 1.0), _cameraUp.transformedBy(new MutableMatrix44D(matrix), 0.0));
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	cameraContext.setCurrentGesture(Gesture.None);
  }

  public MutableVector3D _cameraPosition = new MutableVector3D();
  public MutableVector3D _cameraCenter = new MutableVector3D();
  public MutableVector3D _cameraUp = new MutableVector3D();
  public MutableVector2I _cameraViewPort = new MutableVector2I();
  public MutableMatrix44D _cameraModelViewMatrix = new MutableMatrix44D();

}
