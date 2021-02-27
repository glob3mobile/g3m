

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
   private boolean               _mouseDown             = false;
   private Vector2F              _previousMousePosition = null;


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


   private Map<Integer, Vector2F> _previousTouchesPositions = new HashMap<>();


   private ArrayList<Touch> createTouches(final JsArray<com.google.gwt.dom.client.Touch> jsTouches) {
      final Map<Integer, Vector2F> currentTouchesPositions = new HashMap<>();

      final int              jsTouchesSize = jsTouches.length();
      final ArrayList<Touch> touches       = new ArrayList<>(jsTouchesSize);
      for (int i = 0; i < jsTouchesSize; i++) {
         final com.google.gwt.dom.client.Touch jsTouch = jsTouches.get(i);

         final Vector2F currentTouchPosition = new Vector2F( //
               jsTouch.getRelativeX(_canvasElement) * _widget.getDevicePixelRatio(), //
               jsTouch.getRelativeY(_canvasElement) * _widget.getDevicePixelRatio() //
         );

         final Integer touchId = Integer.valueOf(jsTouch.getIdentifier());

         currentTouchesPositions.put(touchId, currentTouchPosition);

         Vector2F previousTouchPosition = _previousTouchesPositions.get(touchId);
         if (previousTouchPosition == null) {
            previousTouchPosition = currentTouchPosition;
         }

         touches.add(new Touch(currentTouchPosition, previousTouchPosition));
      }

      _previousTouchesPositions = currentTouchesPositions;

      return touches;
   }


   private Vector2F createPosition(final Event event) {
      final float devicePixelRatio = _widget.getDevicePixelRatio();
      return new Vector2F( //
            (event.getClientX() - _canvasElement.getAbsoluteLeft()) * devicePixelRatio, //
            (event.getClientY() - _canvasElement.getAbsoluteTop()) * devicePixelRatio);
   }


   private TouchEvent processTouchStart(final Event event) {
      return TouchEvent.create(TouchEventType.Down, createTouches(event.getTouches()));
   }


   private TouchEvent processTouchMove(final Event event) {
      return TouchEvent.create(TouchEventType.Move, createTouches(event.getTouches()));
   }


   private TouchEvent processTouchEnd(final Event event) {
      return TouchEvent.create(TouchEventType.Up, createTouches(event.getChangedTouches()));
   }


   /**
    * @param event
    */
   private TouchEvent processTouchCancel(final Event event) {
      _previousTouchesPositions.clear();
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

      if (event.getShiftKey()) {
         touches.add(new Touch(currentMousePosition.sub(DELTA), _previousMousePosition.sub(DELTA)));
         touches.add(new Touch(currentMousePosition, _previousMousePosition));
         touches.add(new Touch(currentMousePosition.add(DELTA), _previousMousePosition.add(DELTA)));
      }
      else {
         touches.add(new Touch(currentMousePosition, _previousMousePosition));
      }

      _previousMousePosition = currentMousePosition;

      return TouchEvent.create(TouchEventType.Move, touches);
   }


   private TouchEvent processMouseDown(final Event event) {
      final Vector2F         currentMousePosition = createPosition(event);
      final ArrayList<Touch> touches              = new ArrayList<>();

      _mouseDown = true;
      if (event.getShiftKey()) {
         touches.add(new Touch(currentMousePosition.sub(DELTA), _previousMousePosition.sub(DELTA)));
         touches.add(new Touch(currentMousePosition, _previousMousePosition));
         touches.add(new Touch(currentMousePosition.add(DELTA), _previousMousePosition.add(DELTA)));
      }
      else {
         touches.add(new Touch(currentMousePosition, _previousMousePosition));
      }

      _previousMousePosition = currentMousePosition;

      return TouchEvent.create(TouchEventType.Down, touches);
   }


   private TouchEvent processMouseUp(final Event event) {
      final Vector2F         currentMousePosition = createPosition(event);
      final ArrayList<Touch> touches              = new ArrayList<>();
      final TouchEventType   touchType;
      _mouseDown = false;
      if (event.getShiftKey()) {
         touches.add(new Touch(currentMousePosition.sub(DELTA), _previousMousePosition.sub(DELTA)));
         touches.add(new Touch(currentMousePosition, _previousMousePosition));
         touches.add(new Touch(currentMousePosition.add(DELTA), _previousMousePosition.add(DELTA)));

         touchType = TouchEventType.Up;
      }
      else {
         touches.add(new Touch(currentMousePosition, _previousMousePosition));
         final boolean isSecondaryButton = event.getCtrlKey() && (event.getButton() == NativeEvent.BUTTON_LEFT);
         touchType = isSecondaryButton ? TouchEventType.LongPress : TouchEventType.Up;
      }
      _previousMousePosition = currentMousePosition;

      return TouchEvent.create(touchType, touches);
   }


   private TouchEvent processDoubleClick(final Event event) {
      final Vector2F currentMousePosition = createPosition(event);
      final Touch    touch                = new Touch(currentMousePosition, currentMousePosition, (byte) 2);
      return TouchEvent.create(TouchEventType.Down, touch);
   }


   private TouchEvent processContextMenu(final Event event) {
      _mouseDown = false;

      final Vector2F currentMousePosition = createPosition(event);
      final Touch    touch                = new Touch(currentMousePosition, _previousMousePosition);
      _previousMousePosition = currentMousePosition;

      return TouchEvent.create(TouchEventType.LongPress, touch);
   }


   private TouchEvent processMouseWheel(final Event event) {
      final Vector2F position = createPosition(event);
      final int      delta    = jsGetMouseWheelDelta(event);

      final ArrayList<Touch> touches = new ArrayList<>(2);
      touches.add(new Touch(position, position, (byte) 0, delta));
      _previousMousePosition = new Vector2F(position);
      return TouchEvent.create(TouchEventType.Down, touches);
   }


   private native int jsGetMouseWheelDelta(Event event) /*-{
		var e = event;
		var delta = (Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail))));
		return delta;
   }-*/;

}
