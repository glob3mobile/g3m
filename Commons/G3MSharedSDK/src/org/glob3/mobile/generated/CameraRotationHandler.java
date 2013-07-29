package org.glob3.mobile.generated; 
//
//  CameraRotationHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraRotationHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if ! G3MiOSSDK_CameraRotationHandlerr_h
//#define G3MiOSSDK_CameraRotationHandler_h


public class CameraRotationHandler extends CameraEventHandler
{
  private MutableVector3D _pivotPoint = new MutableVector3D(); //Initial point at dragging
  private MutableVector2I _pivotPixel = new MutableVector2I(); //Initial pixel at start of gesture

//  int _lastYValid;
  private Camera _camera0 ; //Initial Camera saved on Down event

  public CameraRotationHandler()
  {
     _camera0 = new Camera(new Camera(0, 0));
     _pivotPoint = new MutableVector3D(0, 0, 0);
     _pivotPixel = new MutableVector2I(0, 0);
  }

  public void dispose()
  {
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
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

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {
  //  // TEMP TO DRAW A POINT WHERE USER PRESS
  //  if (false) {
  //    if (cameraContext->getCurrentGesture() == Rotate) {
  //      GL* gl = rc->getGL();
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
  //      //printf ("zoom with initial point = (%f, %f)\n", g._latitude._degrees, g._longitude._degrees);
  //    }
  //  }
  }

  public final void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    Camera camera = cameraContext.getNextCamera();
    _camera0.copyFrom(camera);
    cameraContext.setCurrentGesture(Gesture.Rotate);
  
    // middle pixel in 2D
    Vector2I pixel0 = touchEvent.getTouch(0).getPos();
    Vector2I pixel1 = touchEvent.getTouch(1).getPos();
    Vector2I pixel2 = touchEvent.getTouch(2).getPos();
    Vector2I averagePixel = pixel0.add(pixel1).add(pixel2).div(3);
    _pivotPixel = new MutableVector2I(averagePixel._x, averagePixel._y);
    //_lastYValid = _initialPixel.y();
  
    // compute center of view
    _pivotPoint = camera.getXYZCenterOfView().asMutableVector3D();
    if (_pivotPoint.isNan())
    {
      ILogger.instance().logError("CAMERA ERROR: center point does not intersect globe!!\n");
      cameraContext.setCurrentGesture(Gesture.None);
    }
  
    //printf ("down 3 fingers\n");
  }

  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    //_currentGesture = getGesture(touchEvent);
    if (cameraContext.getCurrentGesture() != Gesture.Rotate)
       return;
  
    // current middle pixel in 2D
    final Vector2I c0 = touchEvent.getTouch(0).getPos();
    final Vector2I c1 = touchEvent.getTouch(1).getPos();
    final Vector2I c2 = touchEvent.getTouch(2).getPos();
    final Vector2I cm = c0.add(c1).add(c2).div(3);
  
    // compute normal to Initial point
    Vector3D normal = eventContext.getPlanet().geodeticSurfaceNormal(_pivotPoint);
  
    // vertical rotation around normal vector to globe
    Camera camera = cameraContext.getNextCamera();
    camera.copyFrom(_camera0);
    Angle angle_v = Angle.fromDegrees((_pivotPixel.x()-cm._x)*0.25);
    camera.rotateWithAxisAndPoint(normal, _pivotPoint.asVector3D(), angle_v);
  
    // compute angle between normal and view direction
    Vector3D view = camera.getViewDirection();
    double dot = normal.normalized().dot(view.normalized().times(-1));
    double initialAngle = mu.acos(dot) / DefineConstants.PI * 180;
  
    // rotate more than 85 degrees or less than 0 degrees is not allowed
    double delta = (cm._y - _pivotPixel.y()) * 0.25;
    double finalAngle = initialAngle + delta;
    if (finalAngle > 85)
       delta = 85 - initialAngle;
    if (finalAngle < 0)
       delta = -initialAngle;
  
    // create temporal camera to test if next rotation is valid
    Camera tempCamera = new Camera(camera);
  
    // horizontal rotation over the original camera horizontal axix
    Vector3D u = camera.getHorizontalVector();
    tempCamera.rotateWithAxisAndPoint(u, _pivotPoint.asVector3D(), Angle.fromDegrees(delta));
  
    // update camera only if new view intersects globe
    //tempCamera.updateModelMatrix();
    if (!tempCamera.getXYZCenterOfView().isNan())
    {
      camera.copyFrom(tempCamera);
    }
  }

  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(Gesture.None);
    _pivotPixel = MutableVector2I.zero();
  }



}
//#endif
