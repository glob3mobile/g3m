package org.glob3.mobile.generated; 
//
//  CameraZoomAndRotateHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 26/06/13.
//
//


//
//  CameraZoomAndRotateHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo on 26/06/13.
//
//




public class CameraZoomAndRotateHandler extends CameraEventHandler
{
  private double _fingerSep0;
  private double _lastAngle;
  private double _angle0;

  private MutableVector3D _centralGlobePoint = new MutableVector3D();
  private MutableVector3D _centralGlobeNormal = new MutableVector3D();

  private void zoom(Camera camera, Vector2F difCurrentPixels)
  {
    final double MIN_CAMERA_HEIGHT = 30;
  
    // compute angle params
    double angle = Math.atan2(difCurrentPixels._y, difCurrentPixels._x);
    while (Math.abs(_lastAngle-angle)>DefineConstants.PI/2)
    {
      if (angle<_lastAngle)
         angle+=DefineConstants.PI;
         else
            angle-=DefineConstants.PI;
    }
    _lastAngle = angle;
    angle -= _angle0;
  
    // compute zoom params
    double fingerSep = Math.sqrt((difCurrentPixels._x *difCurrentPixels._x+difCurrentPixels._y *difCurrentPixels._y));
    double factor = _fingerSep0 / fingerSep;
    double desp = 1-factor;
    Vector3D w = _centralGlobePoint.asVector3D().sub(_cameraPosition);
    double dist = w.length();
  
     // don't allow much closer
     if (dist *factor<MIN_CAMERA_HEIGHT && factor<0.999)
      return;
  
     // don't allow much further away
    double R = _centralGlobePoint.length();
     if (dist *factor>11 *R && factor>1.001)
      return;
  
     // make zoom and rotation
    camera.setLookAtParams(_cameraPosition, _cameraCenter, _cameraUp);
  
    // make rotation
    camera.rotateWithAxisAndPoint(_centralGlobeNormal.asVector3D(), _centralGlobePoint.asVector3D(), Angle.fromRadians(angle));
    //camera->rotateWithAxis(_centralGlobePoint.asVector3D(), Angle::fromRadians(angle));
  
    // make zoom
    camera.moveForward(desp *dist);
  }
  private void rotate()
  {
    System.out.print("rotating....\n");
  }


  public CameraZoomAndRotateHandler()
  {
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
    camera.getLookAtParamsInto(_cameraPosition, _cameraCenter, _cameraUp);
    cameraContext.setCurrentGesture(Gesture.DoubleDrag);
  
    // double dragging
    _initialPixel0 = new MutableVector2F(touchEvent.getTouch(0).getPos());
    _initialPixel1 = new MutableVector2F(touchEvent.getTouch(1).getPos());
    }
  public final void onMove(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  
    Vector2F pixel0 = touchEvent.getTouch(0).getPos();
    Vector2F pixel1 = touchEvent.getTouch(1).getPos();
    Vector2F difCurrentPixels = pixel1.sub(pixel0);
    final Planet planet = eventContext.getPlanet();
  
    // if it is the first move, let's decide if make zoom or rotate
    if (cameraContext.getCurrentGesture() == Gesture.DoubleDrag)
    {
      Vector2F difPixel0 = pixel0.sub(_initialPixel0.asVector2F());
      Vector2F difPixel1 = pixel1.sub(_initialPixel1.asVector2F());
      if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) || (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1))
      {
        //printf ("zoom..\n");
        cameraContext.setCurrentGesture(Gesture.Zoom);
      }
  
      // test if starting a zoom action
      if ((difPixel0._y<-1 && difPixel1._y>1) || (difPixel0._y>1 && difPixel1._y<-1) || (difPixel0._x<-1 && difPixel1._x>1) || (difPixel0._x>1 && difPixel1._x<-1))
      {
  
        // compute intersection of view direction with the globe
        Vector3D intersection = planet.closestIntersection(_cameraPosition.asVector3D(), _cameraCenter.sub(_cameraPosition).asVector3D());
        if (!intersection.isNan())
        {
  //        _centralGlobePoint = intersection.asMutableVector3D();
          _centralGlobePoint.copyFrom(intersection);
  //        _centralGlobeNormal = planet->geodeticSurfaceNormal(_centralGlobePoint).asMutableVector3D();
          _centralGlobeNormal.copyFrom(planet.geodeticSurfaceNormal(_centralGlobePoint));
          _fingerSep0 = Math.sqrt((difCurrentPixels._x *difCurrentPixels._x+difCurrentPixels._y *difCurrentPixels._y));
          _lastAngle = _angle0 = Math.atan2(difCurrentPixels._y, difCurrentPixels._x);
          cameraContext.setCurrentGesture(Gesture.Zoom);
        }
        else
          ILogger.instance().logInfo("Zoom is no possible. View direction does not intersect the globe");
      }
  
      // test if starting a rotate action
      if ((difPixel0._y<-1 && difPixel1._y<-1) || (difPixel0._y>1 && difPixel1._y>1) || (difPixel0._x<-1 && difPixel1._x<-1) || (difPixel0._x>1 && difPixel1._x>1))
      {
        //cameraContext->setCurrentGesture(Rotate);
      }
    }
  
    // call specific transformation
    switch (cameraContext.getCurrentGesture())
    {
      case Zoom:
        zoom(cameraContext.getNextCamera(), difCurrentPixels);
        break;
  
      case Rotate:
        rotate();
        break;
  
      default:
        break;
    }
  
  }
  public final void onUp(G3MEventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
    cameraContext.setCurrentGesture(Gesture.None);
    //_initialPixel0 = _initialPixel1 = Vector2I(-1,-1);
  
    //printf ("end 2 fingers.  gesture=%d\n", _currentGesture);
  }

  //MutableVector3D _initialPoint;  //Initial point at dragging
  public MutableVector2F _initialPixel0 = new MutableVector2F(); //Initial pixels at start of gesture
  public MutableVector2F _initialPixel1 = new MutableVector2F();
  //MutableVector3D _initialPoint0, _initialPoint1;
  public double _initialFingerSeparation;
  public double _initialFingerInclination;

  public MutableVector3D _cameraPosition = new MutableVector3D();
  public MutableVector3D _cameraCenter = new MutableVector3D();
  public MutableVector3D _cameraUp = new MutableVector3D();

}