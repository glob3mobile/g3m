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
  {
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
    camera.getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
    camera.getModelViewMatrixInto(_cameraModelViewMatrix);
    camera.getViewPortInto(_cameraViewPort);
  
    // dragging
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    final Vector3D initialRay = camera.pixel2Ray(pixel);
    if (!initialRay.isNan())
    {
      cameraContext.setCurrentGesture(Gesture.Drag);
      eventContext.getPlanet().beginSingleDrag(camera.getCartesianPosition(), initialRay);
    }
  }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (cameraContext.getCurrentGesture()!=Gesture.Drag)
       return;
  
    //check finalRay
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    Camera.pixel2RayInto(_cameraPosition, pixel, _cameraViewPort, _cameraModelViewMatrix, _finalRay);
    if (_finalRay.isNan())
       return;
  
    // compute transformation matrix
    final Planet planet = eventContext.getPlanet();
    MutableMatrix44D matrix = planet.singleDrag(_finalRay.asVector3D());
    if (!matrix.isValid())
       return;
  
    // apply transformation
    cameraContext.getNextCamera().setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0), _cameraCenter.transformedBy(matrix, 1.0), _cameraUp.transformedBy(matrix, 0.0));
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