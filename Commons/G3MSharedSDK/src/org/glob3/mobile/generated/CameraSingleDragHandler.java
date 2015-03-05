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
     _camera0 = new Camera(new Camera());
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
    if (touchEvent.getType() == TouchEventType.MouseWheelChanged)
    {
      return false;
    }
  
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
<<<<<<< HEAD
    final Vector2I pixel = touchEvent.getTouch(0).getPos();
  
    Vector3D touchedPosition = eventContext.getWidget().getScenePositionForPixel(pixel._x, pixel._y);
  
    //Geodetic3D geoPos = eventContext->getPlanet()->toGeodetic3D(v);
    //printf("ZBUFFER EN %f, %f, %f\n ", geoPos._latitude._degrees, geoPos._longitude._degrees, geoPos._height);
  /*
    eventContext->getPlanet()->beginSingleDrag(_camera0.getCartesianPosition(),
                                               _camera0.pixel2Ray(pixel));*/
      eventContext.getPlanet().beginSingleDrag(_camera0.getCartesianPosition(), touchedPosition);
  
  /*
  =======
    const Vector3D& initialRay = _camera0.pixel2Ray(pixel);
    if (!initialRay.isNan()) {
      cameraContext->setCurrentGesture(Drag);
      eventContext->getPlanet()->beginSingleDrag(_camera0.getCartesianPosition(),initialRay);
=======
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    final Vector3D initialRay = _camera0.pixel2Ray(pixel);
    if (!initialRay.isNan())
    {
      cameraContext.setCurrentGesture(Gesture.Drag);
      eventContext.getPlanet().beginSingleDrag(_camera0.getCartesianPosition(), initialRay);
>>>>>>> purgatory
    }
  >>>>>>> origin/purgatory
   */
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (cameraContext.getCurrentGesture()!=Gesture.Drag)
       return;
  
    //check finalRay
    final Vector3D finalRay = _camera0.pixel2Ray(touchEvent.getTouch(0).getPos());
    if (finalRay.isNan())
       return;
  
    // compute transformation matrix
    final Planet planet = eventContext.getPlanet();
    MutableMatrix44D matrix = planet.singleDrag(finalRay);
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
      final Vector2F currPixel = touch.getPos();
      final Vector2F prevPixel = touch.getPrevPos();
      final double desp = currPixel.sub(prevPixel).length();
  
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

  private Camera _camera0 = new Camera(); //Initial Camera saved on Down event
}