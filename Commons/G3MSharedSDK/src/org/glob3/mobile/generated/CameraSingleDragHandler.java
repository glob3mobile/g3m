package org.glob3.mobile.generated;
//
//  CameraSingleDragHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//


//
//  CameraSingleDragHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//






public class CameraSingleDragHandler extends CameraEventHandler
{
  private final boolean _useInertia;

  private MutableVector3D _cameraPosition = new MutableVector3D();
  private MutableVector3D _cameraCenter = new MutableVector3D();
  private MutableVector3D _cameraUp = new MutableVector3D();
  private MutableVector2I _cameraViewPort = new MutableVector2I();
  private MutableMatrix44D _cameraModelViewMatrix = new MutableMatrix44D();
  private MutableVector3D _finalRay = new MutableVector3D();

  private Vector2F _previousEventPosition0;
  private Vector2F _previousEventPosition1;

  private void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final Camera camera = cameraContext.getNextCamera();
    camera.getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
    camera.getModelViewMatrixInto(_cameraModelViewMatrix);
    camera.getViewPortInto(_cameraViewPort);
  
    // dragging
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    final Vector3D initialRay = camera.pixel2Ray(pixel);
    if (!initialRay.isNan())
    {
      cameraContext.setCurrentGesture(CameraEventGesture.Drag);
      eventContext.getPlanet().beginSingleDrag(camera.getCartesianPosition(), initialRay);
    }
  }
  private void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    if (cameraContext.getCurrentGesture() != CameraEventGesture.Drag)
    {
      return;
    }
  
    //check finalRay
    final Vector2F pixel = touchEvent.getTouch(0).getPos();
    Camera.pixel2RayInto(_cameraPosition, pixel, _cameraViewPort, _cameraModelViewMatrix, _finalRay);
    if (_finalRay.isNan())
    {
      return;
    }
  
    // compute transformation matrix
    final Planet planet = eventContext.getPlanet();
    final MutableMatrix44D matrix = planet.singleDrag(_finalRay.asVector3D());
    if (!matrix.isValid())
    {
      return;
    }
  
    // apply transformation
    cameraContext.getNextCamera().setLookAtParams(_cameraPosition.transformedBy(matrix, 1.0), _cameraCenter.transformedBy(matrix, 1.0), _cameraUp.transformedBy(matrix, 0.0));
    if (_previousEventPosition1 != null)
       _previousEventPosition1.dispose();
    _previousEventPosition1 = _previousEventPosition0;
    _previousEventPosition0 = pixel;
  }
  private void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    // test if animation is needed
    if (_useInertia && (cameraContext.getCurrentGesture() == CameraEventGesture.Drag))
    {
      final Touch touch = touchEvent.getTouch(0);
      final Vector2F currentPosition = touch.getPos();
      final Vector2F previousEventPosition = getPreviousEventPosition(currentPosition);
      if (previousEventPosition != null)
      {
        final double desp = previousEventPosition.squaredDistanceTo(currentPosition);
  
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning method getPixelsInMM is ! working fine in iOS devices
        final float delta = IFactory.instance().getDeviceInfo().getPixelsInMM(0.2f);
        if (desp > delta)
        {
          final Planet planet = eventContext.getPlanet();
          Effect effect = planet.createEffectFromLastSingleDrag();
          if (effect != null)
          {
            EffectTarget target = cameraContext.getNextCamera().getEffectTarget();
            eventContext.getEffectsScheduler().startEffect(effect, target);
          }
        }
      }
    }
  
    if (_previousEventPosition0 != null)
       _previousEventPosition0.dispose();
    _previousEventPosition0 = null;
    if (_previousEventPosition1 != null)
       _previousEventPosition1.dispose();
    _previousEventPosition1 = null;
  
    cameraContext.setCurrentGesture(CameraEventGesture.None);
  }

  private Vector2F getPreviousEventPosition(Vector2F currentPosition)
  {
  
    if ((_previousEventPosition1 == null) && (_previousEventPosition0 == null))
    {
      return null;
    }
    else if (_previousEventPosition1 == null)
    {
      return _previousEventPosition0;
    }
    else if (_previousEventPosition0 == null)
    {
      return _previousEventPosition1;
    }
    else
    {
      final double desp0 = _previousEventPosition0.squaredDistanceTo(currentPosition);
      return (desp0 == 0) ? _previousEventPosition1 : _previousEventPosition0;
    }
  }

  public CameraSingleDragHandler(boolean useInertia)
  {
     _useInertia = useInertia;
     _previousEventPosition0 = null;
     _previousEventPosition1 = null;
  }

  public void dispose()
  {
    if (_previousEventPosition0 != null)
       _previousEventPosition0.dispose();
    if (_previousEventPosition1 != null)
       _previousEventPosition1.dispose();
  
    super.dispose();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    if (touchEvent.getTouchCount() != 1)
    {
      return false;
    }
    if (touchEvent.getTapCount() > 1)
    {
      return false;
    }
  
    switch (touchEvent.getType())
    {
      case Down:
        onDown(eventContext, touchEvent, cameraContext);
        return true;
  
      case Move:
        onMove(eventContext, touchEvent, cameraContext);
        return true;
  
      case Up:
        onUp(eventContext, touchEvent, cameraContext);
        return true;
  
      default:
        return false;
    }
  
  }

  public final void render(G3MRenderContext rc, CameraContext cameraContext)
  {

  }

}