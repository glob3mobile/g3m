

package org.glob3.mobile.generated;

public class MarksRenderer
         extends
            LeafRenderer {
   private static final double             DISTANCE_THRESHOLD = 50 * 50;

   private final boolean                   _readyWhenMarksReady;
   private final java.util.ArrayList<Mark> _marks             = new java.util.ArrayList<Mark>();

   private InitializationContext           _initializationContext;
   private Camera                          _lastCamera;

   private MarkTouchListener               _markTouchListener;
   private boolean                         _autoDeleteMarkTouchListener;


   public MarksRenderer(final boolean readyWhenMarksReady) {
      _readyWhenMarksReady = readyWhenMarksReady;
      _initializationContext = null;
      _lastCamera = null;
      _markTouchListener = null;
      _autoDeleteMarkTouchListener = false;
   }


   public final void setMarkTouchListener(final MarkTouchListener markTouchListener,
                                          final boolean autoDelete) {
      if ((_markTouchListener != null) && _autoDeleteMarkTouchListener) {
         if (_markTouchListener != null) {
            _markTouchListener.dispose();
         }
      }

      _markTouchListener = markTouchListener;
      _autoDeleteMarkTouchListener = autoDelete;
   }


   @Override
   public void dispose() {
      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
         if (_marks.get(i) != null) {
            _marks.get(i).dispose();
         }
      }

      if ((_markTouchListener != null) && _autoDeleteMarkTouchListener) {
         if (_markTouchListener != null) {
            _markTouchListener.dispose();
         }
      }
      _markTouchListener = null;
   }


   @Override
   public void initialize(final InitializationContext ic) {
      _initializationContext = ic;

      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
         final Mark mark = _marks.get(i);
         mark.initialize(_initializationContext);
      }
   }


   @Override
   public void render(final RenderContext rc) {
      //  rc.getLogger()->logInfo("MarksRenderer::render()");

      // Saving camera for use in onTouchEvent
      _lastCamera = rc.getCurrentCamera();


      final GL gl = rc.getGL();

      gl.enableVerticesPosition();
      gl.enableTextures();

      gl.disableDepthTest();
      gl.enableBlend();

      final Vector3D radius = rc.getPlanet().getRadii();
      final double minDistanceToCamera = ((radius._x + radius._y + radius._z) / 3) * 0.75;

      final int marksSize = _marks.size();
      for (int i = 0; i < marksSize; i++) {
         final Mark mark = _marks.get(i);
         //rc->getLogger()->logInfo("Rendering Mark: \"%s\"", mark->getName().c_str());

         if (mark.isReady()) {
            mark.render(rc, minDistanceToCamera);
         }
      }

      gl.enableDepthTest();
      gl.disableBlend();

      gl.disableTextures();
      gl.disableVerticesPosition();
      gl.disableTexture2D();
   }


   public final void addMark(final Mark mark) {
      _marks.add(mark);
      if (_initializationContext != null) {
         mark.initialize(_initializationContext);
      }
   }


   @Override
   public boolean onTouchEvent(final EventContext ec,
                               final TouchEvent touchEvent) {
      if (_markTouchListener == null) {
         return false;
      }

      boolean handled = false;

      // if (touchEvent->getType() == LongPress) {
      if (touchEvent.getType() == TouchEventType.Down) {

         if (_lastCamera != null) {
            final Vector2D touchedPixel = touchEvent.getTouch(0).getPos();
            final Vector3D ray = _lastCamera.pixel2Ray(touchedPixel);
            final Vector3D origin = _lastCamera.getCartesianPosition();

            final Planet planet = ec.getPlanet();

            final Vector3D positionCartesian = planet.closestIntersection(origin, ray);
            if (positionCartesian.isNan()) {
               return false;
            }

            // const Geodetic3D position = planet->toGeodetic3D(positionCartesian);

            double minDistance = IMathUtils.instance().maxDouble();
            Mark nearestMark = null;

            final int marksSize = _marks.size();
            for (int i = 0; i < marksSize; i++) {
               final Mark mark = _marks.get(i);

               if (mark.isReady()) {
                  if (mark.isRendered()) {
                     final Vector3D cartesianMarkPosition = planet.toCartesian(mark.getPosition());
                     final Vector2D markPixel = _lastCamera.point2Pixel(cartesianMarkPosition);

                     final double distance = markPixel.sub(touchedPixel).squaredLength();
                     if (distance < minDistance) {
                        nearestMark = mark;
                        minDistance = distance;
                     }
                  }
               }
            }

            if (nearestMark != null) {
               if (minDistance <= DISTANCE_THRESHOLD) {
                  handled = _markTouchListener.touchedMark(nearestMark);
               }
            }

         }

      }

      return handled;


   }


   @Override
   public final void onResizeViewportEvent(final EventContext ec,
                                           final int width,
                                           final int height) {

   }


   @Override
   public final boolean isReadyToRender(final RenderContext rc) {
      if (_readyWhenMarksReady) {
         final int marksSize = _marks.size();
         for (int i = 0; i < marksSize; i++) {
            if (!_marks.get(i).isReady()) {
               return false;
            }
         }
      }

      return true;
   }


   @Override
   public final void start() {

   }


   @Override
   public final void stop() {

   }


   @Override
   public final void onResume(final InitializationContext ic) {

   }


   @Override
   public final void onPause(final InitializationContext ic) {

   }

}
