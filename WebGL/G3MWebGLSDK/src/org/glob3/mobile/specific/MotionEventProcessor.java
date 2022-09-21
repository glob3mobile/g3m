

package org.glob3.mobile.specific;


import java.util.*;

import org.glob3.mobile.generated.*;
import org.glob3.mobile.generated.Touch;

import com.google.gwt.core.client.*;
import com.google.gwt.core.client.Scheduler.*;
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.*;


final class MotionEventProcessor {


   private final class ScheduledCommandImplementation
                                                      implements
                                                         ScheduledCommand {
      private final TouchEvent _event;


      private ScheduledCommandImplementation(final TouchEvent event) {
         _event = event;
      }


      @Override
      public void execute() {
         _widget.onTouchEvent(_event);
      }
   }


   private static final Vector2F DELTA = new Vector2F(10, 0);


   private final G3MWidget_WebGL _widget;
   private final CanvasElement   _canvasElement;

   private boolean _mouseDown = false;


   MotionEventProcessor(final G3MWidget_WebGL widget,
                        final CanvasElement canvasElement) {
      _widget        = widget;
      _canvasElement = canvasElement;
   }


   void processEvent(final Event event) {

      TouchEvent touchEvent = null;

      switch (DOM.eventGetType(event)) {
      case Event.ONTOUCHSTART:
         event.preventDefault();
         touchEvent = processTouchStart(event);
         break;
      case Event.ONTOUCHEND:
         event.preventDefault();
         touchEvent = processTouchEnd(event);
         break;
      case Event.ONTOUCHMOVE:
         event.preventDefault();
         touchEvent = processTouchMove(event);
         break;
      case Event.ONTOUCHCANCEL:
         event.preventDefault();
         touchEvent = processTouchCancel(event);
         break;

      case Event.ONMOUSEMOVE:
         touchEvent = processMouseMove(event);
         break;
      case Event.ONMOUSEDOWN:
         touchEvent = processMouseDown(event);
         break;
      case Event.ONMOUSEUP:
         touchEvent = processMouseUp(event);
         break;

      case Event.ONDBLCLICK:
         touchEvent = processDoubleClick(event);
         break;

      case Event.ONCONTEXTMENU:
         event.preventDefault();
         touchEvent = processContextMenu(event);
         break;

      case Event.ONMOUSEWHEEL:
         event.preventDefault();
         touchEvent = processMouseWheel(event);
         break;

      default:
         return;
      }

      if (touchEvent != null) {
         dispatchEvents(touchEvent);
      }
   }


   private ArrayList<Touch> createTouches(final JsArray<com.google.gwt.dom.client.Touch> jsTouches) {
      final float devicePixelRatio = _widget.getDevicePixelRatio();

      // final Map<Integer, Vector2F> currentTouchesPositions = new HashMap<>();

      final int              jsTouchesSize = jsTouches.length();
      final ArrayList<Touch> touches       = new ArrayList<>(jsTouchesSize);
      for (int i = 0; i < jsTouchesSize; i++) {
         final com.google.gwt.dom.client.Touch jsTouch = jsTouches.get(i);

         final Vector2F position = new Vector2F( //
               jsTouch.getRelativeX(_canvasElement) * devicePixelRatio, //
               jsTouch.getRelativeY(_canvasElement) * devicePixelRatio //
         );

         // final Integer touchId = Integer.valueOf(jsTouch.getIdentifier());
         // currentTouchesPositions.put(touchId, currentTouchPosition);

         touches.add(new Touch(position, (byte) 1));
      }

      return touches;
   }


   private Vector2F createPosition(final Event event) {
      final float devicePixelRatio = _widget.getDevicePixelRatio();
      return new Vector2F( //
            (event.getClientX() - _canvasElement.getAbsoluteLeft()) * devicePixelRatio, //
            (event.getClientY() - _canvasElement.getAbsoluteTop()) * devicePixelRatio //
      );
      //      return new Vector2F( //
      //            getRelativeX(event, _canvasElement) * devicePixelRatio, //
      //            getRelativeY(event, _canvasElement) * devicePixelRatio //
      //      );
   }


   //   private static int getRelativeX(final Event event,
   //                                   final Element target) {
   //      return (event.getClientX() - target.getAbsoluteLeft()) + target.getScrollLeft() + target.getOwnerDocument().getScrollLeft();
   //   }
   //
   //
   //   private static int getRelativeY(final Event event,
   //                                   final Element target) {
   //      return (event.getClientY() - target.getAbsoluteTop()) + target.getScrollTop() + target.getOwnerDocument().getScrollTop();
   //   }


   private TouchEvent processTouchStart(final Event event) {
      return createTouchEvent(TouchEventType.Down, createTouches(event.getTouches()), 0 /* wheelDelta */);
   }


   private TouchEvent processTouchMove(final Event event) {
      return createTouchEvent(TouchEventType.Move, createTouches(event.getTouches()), 0 /* wheelDelta */);
   }


   private TouchEvent processTouchEnd(final Event event) {
      return createTouchEvent(TouchEventType.Up, createTouches(event.getChangedTouches()), 0 /* wheelDelta */);
   }


   /**
    * @param event
    */
   private TouchEvent processTouchCancel(final Event event) {
      return null;
   }


   private void dispatchEvents(final TouchEvent... events) {
      if (events.length > 0) {
         final Scheduler scheduler = Scheduler.get();
         for (final TouchEvent event : events) {
            scheduler.scheduleDeferred(new ScheduledCommandImplementation(event));
         }
      }
   }


   private TouchEvent processMouseMove(final Event event) {
      if (!_mouseDown) {
         return null;
      }

      final Vector2F         currentMousePosition = createPosition(event);
      final ArrayList<Touch> touches              = new ArrayList<>();

      final byte tapCount = (byte) 1;
      if (event.getShiftKey()) {
         touches.add(new Touch(currentMousePosition.sub(DELTA), tapCount));
         touches.add(new Touch(currentMousePosition, tapCount));
         touches.add(new Touch(currentMousePosition.add(DELTA), tapCount));
      }
      else {
         touches.add(new Touch(currentMousePosition, tapCount));
      }

      return createTouchEvent(TouchEventType.Move, touches, 0 /* wheelDelta */);
   }


   private TouchEvent processMouseDown(final Event event) {
      final Vector2F         currentMousePosition = createPosition(event);
      final ArrayList<Touch> touches              = new ArrayList<>();

      _mouseDown = true;

      final byte tapCount = (byte) 1;
      if (event.getShiftKey()) {
         touches.add(new Touch(currentMousePosition.sub(DELTA), tapCount));
         touches.add(new Touch(currentMousePosition, tapCount));
         touches.add(new Touch(currentMousePosition.add(DELTA), tapCount));
      }
      else {
         touches.add(new Touch(currentMousePosition, tapCount));
      }

      return createTouchEvent(TouchEventType.Down, touches, 0 /* wheelDelta */);
   }


   private TouchEvent processMouseUp(final Event event) {
      final Vector2F         currentMousePosition = createPosition(event);
      final ArrayList<Touch> touches              = new ArrayList<>();
      final TouchEventType   touchType;

      _mouseDown = false;

      final byte tapCount = (byte) 1;
      if (event.getShiftKey()) {
         touches.add(new Touch(currentMousePosition.sub(DELTA), tapCount));
         touches.add(new Touch(currentMousePosition, tapCount));
         touches.add(new Touch(currentMousePosition.add(DELTA), tapCount));

         touchType = TouchEventType.Up;
      }
      else {
         touches.add(new Touch(currentMousePosition, tapCount));
         final boolean isSecondaryButton = event.getCtrlKey() && (event.getButton() == NativeEvent.BUTTON_LEFT);
         touchType = isSecondaryButton ? TouchEventType.LongPress : TouchEventType.Up;
      }

      return createTouchEvent(touchType, touches, 0 /* wheelDelta */);
   }


   private TouchEvent processDoubleClick(final Event event) {
      final Vector2F currentMousePosition = createPosition(event);
      final Touch    touch                = new Touch(currentMousePosition, (byte) 2);
      return createTouchEvent(TouchEventType.Down, touch, 0 /* wheelDelta */);
   }


   private TouchEvent processContextMenu(final Event event) {
      _mouseDown = false;

      final Vector2F currentMousePosition = createPosition(event);
      final byte     tapCount             = (byte) 1;

      final Touch touch = new Touch(currentMousePosition, tapCount);

      return createTouchEvent(TouchEventType.LongPress, touch, 0 /* wheelDelta */);
   }


   private TouchEvent processMouseWheel(final Event event) {
      final Vector2F position = createPosition(event);
      final int      delta    = jsGetMouseWheelDelta(event);

      final ArrayList<Touch> touches = new ArrayList<>(2);
      touches.add(new Touch(position, (byte) 0));
      return createTouchEvent(TouchEventType.MouseWheel, touches, delta);
   }


   private native int jsGetMouseWheelDelta(final Event event) /*-{
		var e = event;
		var delta = (Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail))));
		return delta;
   }-*/;


   private static TouchEvent createTouchEvent(final TouchEventType type,
                                              final java.util.ArrayList<Touch> touchs,
                                              final double wheelDelta) {

      if ((touchs == null) || touchs.isEmpty()) {
         throw new RuntimeException("Invalid touches for " + type);
      }

      final TouchEvent result = TouchEvent.create(type, touchs, wheelDelta);
      // ILogger.instance().logInfo("createTouchEvent(1): " + result.description());
      return result;
   }


   private static TouchEvent createTouchEvent(final TouchEventType type,
                                              final Touch touch,
                                              final double wheelDelta) {
      final TouchEvent result = TouchEvent.create(type, touch, wheelDelta);
      // ILogger.instance().logInfo("createTouchEvent(2): " + result.description());
      return result;
   }


}
