

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2D;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;


public class MotionEventProcessor {

   final static String TAG        = "MotionEventProcessor";

   boolean             _mouseDown = false;
   boolean             _keyDown   = false;
   Vector2D            _prevPos   = null;


   public TouchEvent processEvent(final Event event) {
      final Vector2D pos = new Vector2D(event.getClientX(), event.getClientY());

      Touch touch = null;
      TouchEvent touchEvent = null;

      switch (DOM.eventGetType(event)) {
         case Event.ONMOUSEMOVE: {
            if (!_mouseDown) {
               return null;
            }
            //            if (_keyDown) {
            //               touch = new Touch(pos, _prevPos, (byte) 3);
            //            }
            //            else {
            touch = new Touch(pos, _prevPos);
            //            }
            touchEvent = TouchEvent.create(TouchEventType.Move, touch);
            _prevPos = pos;

            //            log(LogLevel.InfoLevel, " onMouseUp");
            break;
         }
         case Event.ONMOUSEDOWN:
            _mouseDown = true;
            _prevPos = pos;
            //            if (_keyDown) {
            //               touch = new Touch(pos, _prevPos, (byte) 3);
            //            }
            //            else {
            touch = new Touch(pos, _prevPos);
            //            }
            touchEvent = TouchEvent.create(TouchEventType.Down, touch);

            //            log(LogLevel.InfoLevel, " onMouseDown");
            break;
         case Event.ONMOUSEUP:
            if (!_mouseDown) {
               return null;
            }
            //            if (_keyDown) {
            //               touch = new Touch(pos, pos, (byte) 3);
            //            }
            //            else {
            touch = new Touch(pos, _prevPos);
            //            }
            touchEvent = TouchEvent.create(TouchEventType.Up, touch);
            _prevPos = pos;
            _mouseDown = false;

            //            log(LogLevel.InfoLevel, " onMouseUp");
            break;
         case Event.ONDBLCLICK:

            //          log(LogLevel.InfoLevel, " onDoubleClick");
            break;
         case Event.ONKEYDOWN:
            // event.getShiftKey() does not work on Chrome
            if (event.getShiftKey() || (event.getKeyCode() == 16)) {
               _keyDown = true;

               log(LogLevel.InfoLevel, " onKeyDown");
            }
            else {
               return null;
            }

            break;
         case Event.ONKEYUP:
            // event.getShiftKey() does not work on Chrome
            if (event.getShiftKey() || (event.getKeyCode() == 16)) {
               _keyDown = true;

               log(LogLevel.InfoLevel, " onKeyUp");
            }
            else {
               return null;
            }

            break;
         case Event.ONMOUSEWHEEL:

            log(LogLevel.InfoLevel, " onMousewheel " + event.getMouseWheelVelocityY());
            break;

         default:
            return null;
      }

      return touchEvent;
   }


   public void log(final LogLevel level,
                   final String msg) {
      if (ILogger.instance() != null) {
         switch (level) {
            case InfoLevel:
               ILogger.instance().logInfo("----------                   " + TAG + msg);
               break;
            case WarningLevel:
               ILogger.instance().logWarning("----------                   " + TAG + msg);
               break;
            case ErrorLevel:
               ILogger.instance().logError("----------                   " + TAG + msg);
               break;
            default:
               break;
         }
      }
      else {
         GWT.log(TAG + msg);
      }

   }
}
