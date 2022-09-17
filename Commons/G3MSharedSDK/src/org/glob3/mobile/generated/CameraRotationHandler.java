package org.glob3.mobile.generated;
//
//  CameraRotationHandler.cpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//


//
//  CameraRotationHandler.hpp
//  G3M
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//





public class CameraRotationHandler extends CameraEventHandler
{
  private MutableVector3D _pivotPoint = new MutableVector3D();
  private MutableVector2F _pivotPixel = new MutableVector2F();

  private MutableVector3D _cameraPosition = new MutableVector3D();
  private MutableVector3D _cameraCenter = new MutableVector3D();
  private MutableVector3D _cameraUp = new MutableVector3D();
  private MutableVector3D _tempCameraPosition = new MutableVector3D();
  private MutableVector3D _tempCameraCenter = new MutableVector3D();
  private MutableVector3D _tempCameraUp = new MutableVector3D();

  private void onDown(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final Camera camera = cameraContext.getNextCamera();
    camera.getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
    cameraContext.setCurrentGesture(CameraEventGesture.Rotate);
  
    // middle pixel in 2D
    Vector2F pixel0 = touchEvent.getTouch(0).getPos();
    Vector2F pixel1 = touchEvent.getTouch(1).getPos();
    Vector2F pixel2 = touchEvent.getTouch(2).getPos();
    Vector2F averagePixel = pixel0.add(pixel1).add(pixel2).div(3.0f);
    _pivotPixel.set(averagePixel._x, averagePixel._y);
    //_lastYValid = _initialPixel.y();
  
    // compute center of view
    //  _pivotPoint = camera->getXYZCenterOfView().asMutableVector3D();
    _pivotPoint.set(camera.getXYZCenterOfView());
    if (_pivotPoint.isNan())
    {
      ILogger.instance().logError("CAMERA ERROR: center point does not intersect globe!!\n");
      cameraContext.setCurrentGesture(CameraEventGesture.None);
    }
  
    //printf ("down 3 fingers\n");
  }

  private void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    //_currentGesture = getGesture(touchEvent);
    if (cameraContext.getCurrentGesture() != CameraEventGesture.Rotate)
       return;
  
    // current middle pixel in 2D
    final Vector2F c0 = touchEvent.getTouch(0).getPos();
    final Vector2F c1 = touchEvent.getTouch(1).getPos();
    final Vector2F c2 = touchEvent.getTouch(2).getPos();
    final Vector2F cm = c0.add(c1).add(c2).div(3.0f);
  
    // compute normal to Initial point
    Vector3D normal = eventContext.getPlanet().geodeticSurfaceNormal(_pivotPoint);
  
    // vertical rotation around normal vector to globe
    Camera camera = cameraContext.getNextCamera();
    camera.setLookAtParams(_cameraPosition, _cameraCenter, _cameraUp);
    Angle angle_v = Angle.fromDegrees((_pivotPixel._x-cm._x)*0.25);
    camera.rotateWithAxisAndPoint(normal, _pivotPoint.asVector3D(), angle_v);
  
    // compute angle between normal and view direction
    Vector3D view = camera.getViewDirection();
    double dot = normal.normalized().dot(view.normalized().times(-1));
    double initialAngle = mu.acos(dot) / DefineConstants.PI * 180;
  
    // rotate more than 85 degrees or less than 0 degrees is not allowed
    double delta = (cm._y - _pivotPixel._y) * 0.25;
    double finalAngle = initialAngle + delta;
    if (finalAngle > 85)
       delta = 85 - initialAngle;
    if (finalAngle < 0)
       delta = -initialAngle;
  
    // create temporal camera to test if next rotation is valid
    camera.getLookAtParamsInto(_tempCameraPosition, _tempCameraCenter, _tempCameraUp);
  
    // horizontal rotation over the original camera horizontal axix
    Vector3D u = camera.getHorizontalVector();
    camera.rotateWithAxisAndPoint(u, _pivotPoint.asVector3D(), Angle.fromDegrees(delta));
  
    // update camera only if new view intersects globe
    if (camera.getXYZCenterOfView().isNan())
    {
      camera.setLookAtParams(_tempCameraPosition, _tempCameraCenter, _tempCameraUp);
    }
  
  }

  private void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(CameraEventGesture.None);
    _pivotPixel.set(MutableVector2F.zero());
  }

  public CameraRotationHandler()
  {
     _pivotPoint = new MutableVector3D(0, 0, 0);
     _pivotPixel = new MutableVector2F(0, 0);
  }

  public void dispose()
  {
    super.dispose();
  }

  public final RenderState getRenderState(G3MRenderContext rc)
  {
    return RenderState.ready();
  }

  public final boolean onTouchEvent(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    if (touchEvent.getTouchCount() != 3)
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

}