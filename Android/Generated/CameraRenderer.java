package org.glob3.mobile.generated; 
//
//  CameraRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class CameraRenderer extends Renderer
{

  private Camera _camera; //Camera used at current frame
  private final Planet _planet; //Planet

  private Camera _camera0 = new Camera(); //Initial Camera saved on Down event
  private MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
  private boolean _cameraFixed; //If true the camera is being moved and has no inertia

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  void onDown(const TouchEvent& event);
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  void onMove(const TouchEvent& event);
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  void onUp(const TouchEvent& event);


  public CameraRenderer()
  {
	  _camera0 = new Camera(0,0);
	  _initialPoint = new MutableVector3D(0,0,0);
	  _cameraFixed = false;
  }

  public final void initialize(InitializationContext ic)
  {
  }

  public final int render(RenderContext rc)
  {
	_camera = rc.getCamera(); //Saving camera reference
	_planet = rc.getPlanet();
  
	if (!_cameraFixed) //AutoDragging
		_camera.applyInertia();
  
	rc.getCamera().draw(rc);
  
	//_camera->print();
	return 0;
  }

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//  boolean onTouchEvent(const TouchEvent* event);


}
//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void CameraRenderer::onDown(const TouchEvent& event)
//{
//  //Stop movement
//  _camera->stopInertia();
//
//  //Saving Camera0
//  Camera c(*_camera);
//  _camera0 = c;
//
//  Vector2D pixel = event.getTouch(0)->getPos();
//
//  Vector3D ray = _camera0.pixel2Vector(pixel);
//  _initialPoint = _planet->closestIntersection(_camera0.getPos(), ray);
//
//  _cameraFixed = true;
//}

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void CameraRenderer::onMove(const TouchEvent& event)
//{
//  int n = event.getNumTouch();
//
//  //ONE FINGER
//  if (n == 1)
//  {
//
//	if (_initialPoint.length() > 0) //VALID INITIAL POINT
//	{
//
//	  Vector2D pixel = event.getTouch(0)->getPos();
//	  Vector3D ray = _camera0.pixel2Vector(pixel);
//	  Vector3D pos = _camera0.getPos();
//
//	  Vector3D finalPoint = _planet->closestIntersection(pos, ray);
//
//	  if (finalPoint.length() <= 0.0) //INVALID FINAL POINT
//	  {
//		//We take the closest point to the sphere
//		Vector3D finalPoint2 = _planet->closestPointToSphere(pos, ray);
//		_camera->copyFrom(_camera0);
//		_camera->dragCamera(_initialPoint, finalPoint2);
//
//	  }
//	  else
//	  {
//		_camera->copyFrom(_camera0);
//		_camera->dragCamera(_initialPoint, finalPoint);
//	  }
//	}
//  }
//
//
//  //TWO FINGERS
//  if (n==2)
//  {
//	Vector2D pixel0 = event.getTouch(0)->getPos();
//	Vector2D pixel1 = event.getTouch(1)->getPos();
//	Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);
//
//	Vector3D ray = _camera0.pixel2Vector(pixelCenter);
//	_initialPoint = _planet->closestIntersection(_camera0.getPos(), ray);
//
//	//IF CENTER PIXEL INTERSECTS THE PLANET
//	if (_initialPoint.length() > 0)
//	{
//	  Vector2D prevPixel0 = event.getTouch(0)->getPrevPos();
//	  Vector2D prevPixel1 = event.getTouch(1)->getPrevPos();
//
//	  double dist = pixel0.sub(pixel1).length();
//	  double prevDist = prevPixel0.sub(prevPixel1).length();
//
//	  //ZOOM
//	  if (_planet->intersections(_camera->getPos(), _camera->getCenter()).size() > 0)
//	  {
//		//IF THE CENTER OF THE VIEW INTERSECTS THE PLANET
//		_camera->zoom(prevDist /dist);
//	  }
//
//
//	}
//
//  }
//
//}


//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//void CameraRenderer::onUp(const TouchEvent& event)
//{
//  _cameraFixed = false;
//}

//C++ TO JAVA CONVERTER TODO TASK: There are no simple equivalents to events in Java:
//boolean CameraRenderer::onTouchEvent(const TouchEvent* event)
//{
//  switch (event->getType())
//  {
//	case Down:
//	  onDown(*event);
//	  break;
//	case Move:
//	  onMove(*event);
//	  break;
//	case Up:
//	  onUp(*event);
//	default:
//	  break;
//  }
//
//  return true;
//}