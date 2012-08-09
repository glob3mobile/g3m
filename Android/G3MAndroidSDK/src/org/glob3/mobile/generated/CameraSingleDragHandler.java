package org.glob3.mobile.generated; 
//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//





public class CameraSingleDragHandler extends CameraEventHandler
{

  public CameraSingleDragHandler()
  {
	  _camera0 = new Camera(new Camera(null, 0, 0));
	  _initialPoint = new MutableVector3D(0,0,0);
	  _initialPixel = new MutableVector3D(0,0,0);
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
  public final int render(RenderContext rc, CameraContext cameraContext)
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
  
	return Renderer.maxTimeToRender;
  }

  public final void onDown(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	Camera camera = cameraContext.getCamera();
	_camera0 = new Camera(camera);
	cameraContext.setCurrentGesture(Gesture.Drag);
  
	// dragging
	Vector2D pixel = touchEvent.getTouch(0).getPos();
	_initialPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  
	//printf ("down 1 finger\n");
  }
  public final void onMove(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	if (cameraContext.getCurrentGesture()!=Gesture.Drag)
		return;
	if (_initialPoint.isNan())
		return;
  
	final Vector2D pixel = touchEvent.getTouch(0).getPos();
	MutableVector3D finalPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
	if (finalPoint.isNan())
	{
	  //INVALID FINAL POINT
	  Vector3D ray = _camera0.pixel2Ray(pixel);
	  Vector3D pos = _camera0.getPosition();
	  finalPoint = eventContext.getPlanet().closestPointToSphere(pos, ray).asMutableVector3D();
	}
  
	Camera camera = cameraContext.getCamera();
	camera.copyFrom(_camera0);
	camera.dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  
	//printf ("Moving 1 finger.  gesture=%d\n", _currentGesture);
  }
  public final void onUp(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	cameraContext.setCurrentGesture(Gesture.None);
	_initialPixel = Vector3D.nan().asMutableVector3D();
  
	//printf ("end 1 finger\n");
  }


  public Camera _camera0 ; //Initial Camera saved on Down event

  public MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  public MutableVector3D _initialPixel = new MutableVector3D(); //Initial pixel at start of gesture

}