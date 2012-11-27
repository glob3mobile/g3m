

package org.glob3.mobile.generated;

public class CameraRenderer
         extends
            LeafRenderer {
   private boolean                                       _processTouchEvents;
   private final java.util.ArrayList<CameraEventHandler> _handlers = new java.util.ArrayList<CameraEventHandler>();
   private CameraContext                                 _cameraContext;


   public CameraRenderer() {
      _cameraContext = null;
      _processTouchEvents = true;
   }


   @Override
   public void dispose() {
      if (_cameraContext != null) {
         _cameraContext.dispose();
      }
   }


   public final void addHandler(final CameraEventHandler handler) {
      _handlers.add(handler);
   }


   public final void setProcessTouchEvents(final boolean processTouchEvents) {
      _processTouchEvents = processTouchEvents;
   }


   @Override
   public final void render(final G3MRenderContext rc) {
      // create the CameraContext
      if (_cameraContext == null) {
         _cameraContext = new CameraContext(Gesture.None, rc.getNextCamera());
      }

      // render camera object
      rc.getCurrentCamera().render(rc);

      final int handlersSize = _handlers.size();
      for (int i = 0; i < handlersSize; i++) {
         _handlers.get(i).render(rc, _cameraContext);
      }
   }


   @Override
   public final void initialize(final G3MContext context) {
      //_logger = ic->getLogger();
      //cameraContext = new CameraContext(
   }


   @Override
   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {
      if (_processTouchEvents) {
         // abort all the camera effect currently running
         if (touchEvent.getType() == TouchEventType.Down) {
            final EffectTarget target = _cameraContext.getNextCamera().getEffectTarget();
            ec.getEffectsScheduler().cancellAllEffectsFor(target);
         }

         // pass the event to all the handlers
         final int handlersSize = _handlers.size();
         for (int i = 0; i < handlersSize; i++) {
            if (_handlers.get(i).onTouchEvent(ec, touchEvent, _cameraContext)) {
               return true;
            }
         }
      }

      // if no handler processed the event, return not-handled
      return false;
   }


   @Override
   public final void onResizeViewportEvent(final G3MEventContext ec,
                                           final int width,
                                           final int height) {
      //  moved to G3MWidget::onResizeViewportEvent
      //  if (_cameraContext != NULL) {
      //    _cameraContext->getNextCamera()->resizeViewport(width, height);
      //  }
   }


   @Override
   public final boolean isReadyToRender(final G3MRenderContext rc) {
      return true;
   }


   @Override
   public final void start() {

   }


   @Override
   public final void stop() {

   }


   @Override
   public final void onResume(final G3MContext context) {

   }


   @Override
   public final void onPause(final G3MContext context) {

   }


   @Override
   public final void onDestroy(final G3MContext context) {

   }

}
