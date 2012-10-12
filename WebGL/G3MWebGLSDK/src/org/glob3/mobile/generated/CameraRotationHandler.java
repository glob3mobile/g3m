package org.glob3.mobile.generated; 
//
//  CameraRotationHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraRotationHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if ! G3MiOSSDK_CameraRotationHandlerr_h
//#define G3MiOSSDK_CameraRotationHandler_h



public class CameraRotationHandler extends CameraEventHandler
{

  public CameraRotationHandler()
  {
	  _camera0 = new Camera(new Camera(0, 0));
	  _initialPoint = new MutableVector3D(0,0,0);
	  _initialPixel = new MutableVector3D(0,0,0);
  }

  public void dispose()
  {
  }

  public final boolean onTouchEvent(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	// three finger needed
	if (touchEvent.getTouchCount()!=3)
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
  //  // TEMP TO DRAW A POINT WHERE USER PRESS
  //  if (false) {
  //    if (cameraContext->getCurrentGesture() == Rotate) {
  //      GL *gl = rc->getGL();
  //      float vertices[] = { 0,0,0};
  //      int indices[] = {0};
  //      gl->enableVerticesPosition();
  //      gl->disableTexture2D();
  //      gl->disableTextures();
  //      gl->vertexPointer(3, 0, vertices);
  //      gl->color((float) 1, (float) 1, (float) 0, 1);
  //      gl->pointSize(10);
  //      gl->pushMatrix();
  //      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
  //      gl->multMatrixf(T);
  //      gl->drawPoints(1, indices);
  //      gl->popMatrix();
  //      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
  //      //printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
  //    }
  //  }
  }

  public final void onDown(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	Camera camera = cameraContext.getNextCamera();
	_camera0.copyFrom(camera);
	cameraContext.setCurrentGesture(Gesture.Rotate);
  
	// middle pixel in 2D
	Vector2D pixel0 = touchEvent.getTouch(0).getPos();
	Vector2D pixel1 = touchEvent.getTouch(1).getPos();
	Vector2D pixel2 = touchEvent.getTouch(2).getPos();
	Vector2D averagePixel = pixel0.add(pixel1).add(pixel2).div(3);
	_initialPixel = new MutableVector3D(averagePixel._x, averagePixel._y, 0);
	lastYValid = _initialPixel.y();
  
	// compute center of view
	_initialPoint = camera.getXYZCenterOfView().asMutableVector3D();
	if (_initialPoint.isNan())
	{
	  ILogger.instance().logError("CAMERA ERROR: center point does not intersect globe!!\n");
	  cameraContext.setCurrentGesture(Gesture.None);
	}
  
	//printf ("down 3 fingers\n");
  }
  public final void onMove(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	//_currentGesture = getGesture(touchEvent);
	if (cameraContext.getCurrentGesture() != Gesture.Rotate)
		return;
  
	// current middle pixel in 2D
	Vector2D c0 = touchEvent.getTouch(0).getPos();
	Vector2D c1 = touchEvent.getTouch(1).getPos();
	Vector2D c2 = touchEvent.getTouch(2).getPos();
	Vector2D cm = c0.add(c1).add(c2).div(3);
  
	// compute normal to Initial point
	Vector3D normal = eventContext.getPlanet().geodeticSurfaceNormal(_initialPoint.asVector3D());
  
	// vertical rotation around normal vector to globe
	Camera camera = cameraContext.getNextCamera();
	camera.copyFrom(_camera0);
	Angle angle_v = Angle.fromDegrees((_initialPixel.x()-cm._x)*0.25);
	camera.rotateWithAxisAndPoint(normal, _initialPoint.asVector3D(), angle_v);
  
	// compute angle between normal and view direction
	Vector3D view = camera.getViewDirection();
	double dot = normal.normalized().dot(view.normalized().times(-1));
	double initialAngle = IMathUtils.instance().acos(dot) / IMathUtils.instance().pi() * 180;
  
	// rotate more than 85 degrees or less than 0 degrees is not allowed
	double delta = (cm._y - _initialPixel.y()) * 0.25;
	double finalAngle = initialAngle + delta;
	if (finalAngle > 85)
		delta = 85 - initialAngle;
	if (finalAngle < 0)
		delta = -initialAngle;
  
	// create temporal camera to test if next rotation is valid
	Camera tempCamera = new Camera(camera);
  
	// horizontal rotation over the original camera horizontal axix
	Vector3D u = camera.getHorizontalVector();
	tempCamera.rotateWithAxisAndPoint(u, _initialPoint.asVector3D(), Angle.fromDegrees(delta));
  
	// update camera only if new view intersects globe
	//tempCamera.updateModelMatrix();
	if (!tempCamera.getXYZCenterOfView().isNan())
	{
	  camera.copyFrom(tempCamera);
	}
  }
  public final void onUp(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	cameraContext.setCurrentGesture(Gesture.None);
	_initialPixel = Vector3D.nan().asMutableVector3D();
  }

  public double lastYValid;
  public Camera _camera0 ; //Initial Camera saved on Down event

  public MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  public MutableVector3D _initialPixel = new MutableVector3D(); //Initial pixel at start of gesture

}
//#endif
