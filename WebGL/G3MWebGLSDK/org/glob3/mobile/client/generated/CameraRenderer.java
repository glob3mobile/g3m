

package org.glob3.mobile.client.generated;

public class CameraRenderer
         extends
            Renderer {

   private Camera          _camera;                              //Camera used at current frame
   private Planet          _planet;                              // REMOVED FINAL WORD BY CONVERSOR RULE //Planet

   private Camera          _camera0;                             //Initial Camera saved on Down event
   private MutableVector3D _initialPoint = new MutableVector3D(); //Initial point at dragging
   private MutableVector3D _initialPixel = new MutableVector3D(); //Initial pixel at start of gesture

   private Gesture         _currentGesture;                      //Gesture the user is making at the moment


   private void onDown(final TouchEvent touchEvent) {
      //Saving Camera0
      final Camera c = new Camera(_camera);
      _camera0 = c;

      //Initial Point for Dragging
      final Vector2D pixel = touchEvent.getTouch(0).getPos();
      final Vector3D ray = _camera0.pixel2Vector(pixel);
      _initialPoint = _planet.closestIntersection(_camera0.getPos(), ray).asMutableVector3D();
      _currentGesture = Gesture.Drag; //Dragging
   }


   private void onMove(final TouchEvent touchEvent) {
      _currentGesture = getGesture(touchEvent);

      switch (_currentGesture) {
         case Drag:
            makeDrag(touchEvent);
            break;
         case Zoom:
            makeZoom(touchEvent);
            break;
         case Rotate:
            makeRotate(touchEvent);
            break;
         default:
            break;
      }
   }


   private void onUp(final TouchEvent touchEvent) {
      _currentGesture = Gesture.None;
      _initialPixel = Vector3D.nan().asMutableVector3D();
   }


   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: Gesture getGesture(const TouchEvent& touchEvent) const
   private Gesture getGesture(final TouchEvent touchEvent) {
      final int n = touchEvent.getNumTouch();
      if (n == 1) {
         //Dragging
         if (_currentGesture == Gesture.Drag) {
            return Gesture.Drag;
         }
         else {
            return Gesture.None;
         }
      }

      if (n == 2) {

         //If the gesture is set we don't have to change it
         if (_currentGesture == Gesture.Zoom) {
            return Gesture.Zoom;
         }
         if (_currentGesture == Gesture.Rotate) {
            return Gesture.Rotate;
         }

         //We have to fingers and the previous event was Drag
         final Vector2D pixel0 = touchEvent.getTouch(0).getPos();
         final Vector2D pixel1 = touchEvent.getTouch(1).getPos();

         final Vector2D prevPixel0 = touchEvent.getTouch(0).getPrevPos();
         final Vector2D prevPixel1 = touchEvent.getTouch(1).getPrevPos();

         //If both fingers go in the same direction we should rotate the camera
         if (((pixel0.y() > prevPixel0.y()) && (pixel1.y() > prevPixel1.y()))
             || ((pixel0.x() > prevPixel0.x()) && (pixel1.x() > prevPixel1.x()))
             || ((pixel0.y() < prevPixel0.y()) && (pixel1.y() < prevPixel1.y()))
             || ((pixel0.x() < prevPixel0.x()) && (pixel1.x() < prevPixel1.x()))) {
            return Gesture.Rotate;
         }
         else {

            //If fingers are diverging it is zoom
            return Gesture.Zoom;
         }

      }
      return Gesture.None;
   }


   private void makeDrag(final TouchEvent touchEvent) {
      if (!_initialPoint.isNan()) //VALID INITIAL POINT
      {
         final Vector2D pixel = touchEvent.getTouch(0).getPos();
         final Vector3D ray = _camera0.pixel2Vector(pixel);
         final Vector3D pos = _camera0.getPos();

         MutableVector3D finalPoint = _planet.closestIntersection(pos, ray).asMutableVector3D();
         if (finalPoint.isNan()) //INVALID FINAL POINT
         {
            finalPoint = _planet.closestPointToSphere(pos, ray).asMutableVector3D();
         }

         _camera.copyFrom(_camera0);
         _camera.dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
      }
   }


   private void makeZoom(final TouchEvent touchEvent) {
      final Vector2D pixel0 = touchEvent.getTouch(0).getPos();
      final Vector2D pixel1 = touchEvent.getTouch(1).getPos();
      final Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);

      final Vector3D ray = _camera0.pixel2Vector(pixelCenter);
      _initialPoint = _planet.closestIntersection(_camera0.getPos(), ray).asMutableVector3D();

      final Vector2D centerOfViewport = new Vector2D(_camera0.getWidth() / 2, _camera0.getHeight() / 2);
      final Vector3D ray2 = _camera0.pixel2Vector(centerOfViewport);
      final Vector3D pointInCenterOfView = _planet.closestIntersection(_camera0.getPos(), ray2);

      //IF CENTER PIXEL INTERSECTS THE PLANET
      if (!_initialPoint.isNan()) {

         //IF THE CENTER OF THE VIEW INTERSECTS THE PLANET
         if (!pointInCenterOfView.isNan()) {

            final Vector2D prevPixel0 = touchEvent.getTouch(0).getPrevPos();
            final Vector2D prevPixel1 = touchEvent.getTouch(1).getPrevPos();

            final double dist = pixel0.sub(pixel1).length();
            final double prevDist = prevPixel0.sub(prevPixel1).length();

            final Vector2D pixelDelta = pixel1.sub(pixel0);
            final Vector2D prevPixelDelta = prevPixel1.sub(prevPixel0);

            final Angle angle = pixelDelta.angle();
            final Angle prevAngle = prevPixelDelta.angle();

            //We rotate and zoom the camera with the same gesture
            _camera.zoom(prevDist / dist);
            _camera.pivotOnCenter(angle.sub(prevAngle));
         }
      }
   }


   private void makeRotate(final TouchEvent touchEvent) {
      final int todo_JM_there_is_a_bug;

      final Vector2D pixel0 = touchEvent.getTouch(0).getPos();
      final Vector2D pixel1 = touchEvent.getTouch(1).getPos();
      final Vector2D pixelCenter = pixel0.add(pixel1).div(2.0);

      //The gesture is starting
      if (_initialPixel.isNan()) {
         final Vector3D v = new Vector3D(pixelCenter.x(), pixelCenter.y(), 0);
         _initialPixel = v.asMutableVector3D(); //Storing starting pixel
      }

      //Calculating the point we are going to rotate around
      final Vector3D rotatingPoint = centerOfViewOnPlanet(_camera0);
      if (rotatingPoint.isNan()) {
         return;
      }

      //Rotating axis
      final Vector3D camVec = _camera0.getPos().sub(_camera0.getCenter());
      final Vector3D normal = _planet.geodeticSurfaceNormal(rotatingPoint);
      final Vector3D horizontalAxis = normal.cross(camVec);

      //Calculating the angle we have to rotate the camera vertically
      final double distY = pixelCenter.y() - _initialPixel.y();
      final double distX = pixelCenter.x() - _initialPixel.x();
      final Angle verticalAngle = Angle.fromDegrees((distY / _camera0.getHeight()) * 180.0);
      final Angle horizontalAngle = Angle.fromDegrees((distX / _camera0.getWidth()) * 360.0);

      //	System.out.printf("ROTATING V=%f H=%f\n", verticalAngle.degrees(), horizontalAngle.degrees());

      //Back-Up camera0
      final Camera cameraAux = new Camera(0, 0);
      cameraAux.copyFrom(_camera0);

      //Rotating vertically
      cameraAux.rotateWithAxisAndPoint(horizontalAxis, rotatingPoint, verticalAngle); //Up and down

      //Check if the view isn't too low
      final Vector3D vCamAux = cameraAux.getPos().sub(cameraAux.getCenter());
      final Angle alpha = vCamAux.angleBetween(normal);
      final Vector3D center = centerOfViewOnPlanet(_camera);

      if ((alpha.degrees() > 85.0) || center.isNan()) {
         cameraAux.copyFrom(_camera0); //We trash the vertical rotation
      }

      //Rotating horizontally
      cameraAux.rotateWithAxisAndPoint(normal, rotatingPoint, horizontalAngle); //Horizontally

      //Finally we copy the new camera
      _camera.copyFrom(cameraAux);

   }


   //C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
   //ORIGINAL LINE: Vector3D centerOfViewOnPlanet(const Camera& c) const
   private Vector3D centerOfViewOnPlanet(final Camera c) {
      final Vector2D centerViewport = new Vector2D(c.getWidth() / 2, c.getHeight() / 2);
      final Vector3D rayCV = c.pixel2Vector(centerViewport);
      final Vector3D center = _planet.closestIntersection(c.getPos(), rayCV);

      return center;
   }


   public CameraRenderer() {
      _camera0 = new Camera(0, 0);
      _initialPoint = new MutableVector3D(0, 0, 0);
      _currentGesture = Gesture.None;
      _camera = null;
      _initialPixel = new MutableVector3D(0, 0, 0);
   }


   @Override
   public final void initialize(final InitializationContext ic) {
   }


   @Override
   public final int render(final RenderContext rc) {
      _camera = rc.getCamera(); //Saving camera reference
      _planet = rc.getPlanet();

      rc.getCamera().draw(rc); //We "draw" the camera with IGL
      return 0;
   }


   @Override
   public final boolean onTouchEvent(final TouchEvent touchEvent) {
      switch (touchEvent.getType()) {
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


   @Override
   public final void onResizeViewportEvent(final int width,
                                           final int height) {
      if (_camera != null) {
         _camera.resizeViewport(width, height);
      }
   }


}
