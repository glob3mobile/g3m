package org.glob3.mobile.generated; 
//
//  CameraSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//


//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//






public class CameraSingleDragHandler extends CameraEventHandler
{

  public CameraSingleDragHandler(boolean useInertia)
//  _initialPoint(0,0,0),
//  _initialPixel(0,0),
  {
     _camera0 = new Camera(new Camera(0, 0));
     _useInertia = useInertia;
  }

  public void dispose()
  {
  super.dispose();

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
    _camera0.copyFrom(camera);
    cameraContext.setCurrentGesture(Gesture.Drag);
  
    // dragging
    final Vector2I pixel = touchEvent.getTouch(0).getPos();
    eventContext.getPlanet().beginSingleDrag(_camera0.getCartesianPosition(), _camera0.pixel2Ray(pixel));
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (cameraContext.getCurrentGesture()!=Gesture.Drag)
       return;
  
    // compute transformation matrix
    final Planet planet = eventContext.getPlanet();
    final Vector2I pixel = touchEvent.getTouch(0).getPos();
    MutableMatrix44D matrix = planet.singleDrag(_camera0.pixel2Ray(pixel));
    if (!matrix.isValid())
       return;
  
    // apply transformation
    Camera camera = cameraContext.getNextCamera();
    camera.copyFrom(_camera0);
    camera.applyTransform(matrix);
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final Planet planet = eventContext.getPlanet();
  
    // test if animation is needed
    if (_useInertia)
    {
      final Touch touch = touchEvent.getTouch(0);
      Vector2I currPixel = touch.getPos();
      Vector2I prevPixel = touch.getPrevPos();
      double desp = currPixel.sub(prevPixel).length();
  
      if (cameraContext.getCurrentGesture() == Gesture.Drag && desp>2)
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

  private Camera _camera0 ; //Initial Camera saved on Down event
}