package org.glob3.mobile.generated; 
//
//  CameraDoubleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  CameraDoubleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//





public class CameraDoubleDragHandler extends CameraEventHandler
{

  public CameraDoubleDragHandler()
  {
     _camera0 = new Camera(new Camera());
  }

  public void dispose()
  {
  super.dispose();

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
    _camera0.copyFrom(camera);
    // double dragging
    final Vector2I pixel0 = touchEvent.getTouch(0).getPos();
    final Vector2I pixel1 = touchEvent.getTouch(1).getPos();
  
    final Vector3D initialRay0 = _camera0.pixel2Ray(pixel0);
    final Vector3D initialRay1 = _camera0.pixel2Ray(pixel1);
  
    if (initialRay0.isNan() || initialRay1.isNan())
       return;
  
    cameraContext.setCurrentGesture(Gesture.DoubleDrag);
    eventContext.getPlanet().beginDoubleDrag(_camera0.getCartesianPosition(), _camera0.getViewDirection(), _camera0.pixel2Ray(pixel0), _camera0.pixel2Ray(pixel1));
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (cameraContext.getCurrentGesture() != Gesture.DoubleDrag)
       return;
  
    // compute transformation matrix
    final Planet planet = eventContext.getPlanet();
    final Vector2I pixel0 = touchEvent.getTouch(0).getPos();
    final Vector2I pixel1 = touchEvent.getTouch(1).getPos();
    final Vector3D initialRay0 = _camera0.pixel2Ray(pixel0);
    final Vector3D initialRay1 = _camera0.pixel2Ray(pixel1);
  
     if (initialRay0.isNan() || initialRay1.isNan())
        return;
  
    MutableMatrix44D matrix = planet.doubleDrag(initialRay0, initialRay1);
    if (!matrix.isValid())
       return;
  
    // apply transformation
    Camera camera = cameraContext.getNextCamera();
    camera.copyFrom(_camera0);
    camera.applyTransform(matrix);
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(Gesture.None);
  }

  public Camera _camera0 = new Camera(); //Initial Camera saved on Down event

}