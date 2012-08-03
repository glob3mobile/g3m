package org.glob3.mobile.generated; 
//
//  CameraSimpleDragRenderer.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraSimpleDragRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//





public class CameraSimpleDragRenderer extends CameraRenderer
{

  public CameraSimpleDragRenderer()
  {
	  _camera0 = new Camera(new Camera(null, 0, 0));
  }

  public final boolean onTouchEvent(TouchEvent touchEvent)
  {
	// only one finger needed
	if (touchEvent.getTouchCount()!=1)
		return false;
  
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
  public final int render(RenderContext rc)
  {
	_planet = rc.getPlanet();
	_camera = rc.getCamera();
	_gl = rc.getGL();
  
	// TEMP TO DRAW A POINT WHERE USER PRESS
	if (false)
	{
	  if (_currentGesture == Gesture.Drag)
	  {
		float[] vertices = { 0,0,0};
		int[] indices = {0};
		_gl.enableVerticesPosition();
		_gl.disableTexture2D();
		_gl.disableTextures();
		_gl.vertexPointer(3, 0, vertices);
		_gl.color((float) 0, (float) 1, (float) 0, 1);
		_gl.pointSize(60);
		_gl.pushMatrix();
  
		double height = _planet.toGeodetic3D(_camera.getPosition()).height();
		System.out.printf ("altura camara = %f\n", height);
  
  
		MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(_initialPoint.asVector3D());
		_gl.multMatrixf(T);
		_gl.drawPoints(1, indices);
		_gl.popMatrix();
  
		//Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
		//printf ("zoom with initial point = (%f, %f)\n", g.latitude().degrees(), g.longitude().degrees());
	  }
	}
  
	return Renderer.maxTimeToRender;
  }
  public final void initialize(InitializationContext ic)
  {
  }
  public final void onResizeViewportEvent(int width, int height)
  {
  }


  private void onDown(TouchEvent touchEvent)
  {
	_camera0 = new Camera(_camera);
	_currentGesture = Gesture.Drag;
  
	// dragging
	MutableVector2D pixel = touchEvent.getTouch(0).getPos().asMutableVector2D();
	final Vector3D ray = _camera0.pixel2Ray(pixel.asVector2D());
	_initialPoint = _planet.closestIntersection(_camera0.getPosition(), ray).asMutableVector3D();
  
	//printf ("down 1 finger\n");
  }
  private void onMove(TouchEvent touchEvent)
  {
	//_currentGesture = getGesture(touchEvent);
	if (_currentGesture!=Gesture.Drag)
		return;
	if (_initialPoint.isNan())
		return;
  
	final Vector2D pixel = touchEvent.getTouch(0).getPos();
	final Vector3D ray = _camera0.pixel2Ray(pixel);
	final Vector3D pos = _camera0.getPosition();
  
	MutableVector3D finalPoint = _planet.closestIntersection(pos, ray).asMutableVector3D();
	if (finalPoint.isNan())
	{
	  //INVALID FINAL POINT
	  finalPoint = _planet.closestPointToSphere(pos, ray).asMutableVector3D();
	}
  
	_camera.copyFrom(_camera0);
	_camera.dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  
	//printf ("Moving 1 finger.  gesture=%d\n", _currentGesture);
  }
  private void onUp(TouchEvent touchEvent)
  {
	_currentGesture = Gesture.None;
	_initialPixel = Vector3D.nan().asMutableVector3D();
  
	//printf ("end 1 finger\n");
  }

  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE
  private IGL _gl;
  private Camera _camera0 ; //Initial Camera saved on Down event
  private Camera _camera; // Camera used at current frame

}