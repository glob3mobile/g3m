

package org.glob3.mobile.generated;

public class CameraRenderer
         implements
            ProtoRenderer {
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
      final int handlersSize = _handlers.size();
      for (int i = 0; i < handlersSize; i++) {
         final CameraEventHandler handler = _handlers.get(i);
         if (handler != null) {
            handler.dispose();
         }
      }
   }


   public final void addHandler(final CameraEventHandler handler) {
      _handlers.add(handler);
   }


   public final void setProcessTouchEvents(final boolean processTouchEvents) {
      _processTouchEvents = processTouchEvents;
   }


   @Override
   public final void render(final G3MRenderContext rc,
                            final GLState glState) {

      // create the CameraContext
      if (_cameraContext == null) {
         _cameraContext = new CameraContext(Gesture.None, rc.getNextCamera());
      }

      // render camera object
      //  rc->getCurrentCamera()->render(rc, parentState);

      final int handlersSize = _handlers.size();
      for (int i = 0; i < handlersSize; i++) {
         _handlers.get(i).render(rc, _cameraContext);
      }
   }


   @Override
   public final void initialize(final G3MContext context) {

   }


   public final boolean onTouchEvent(final G3MEventContext ec,
                                     final TouchEvent touchEvent) {
      if (_processTouchEvents) {
         // abort all the camera effect currently running
         if (touchEvent.getType() == TouchEventType.Down) {
            final EffectTarget target = _cameraContext.getNextCamera().getEffectTarget();
            ec.getEffectsScheduler().cancelAllEffectsFor(target);
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

   }


   public final RenderState getRenderState(final G3MRenderContext rc) {
      return RenderState.ready();
   }


   @Override
   public final void start(final G3MRenderContext rc) {

   }


   @Override
   public final void stop(final G3MRenderContext rc) {

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
