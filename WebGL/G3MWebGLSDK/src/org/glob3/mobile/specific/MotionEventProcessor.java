

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;


public class MotionEventProcessor {

   //Stores pointer positions, id and event type
   class EventProcessed {

      public java.util.ArrayList<Integer> _pointersID = new ArrayList<Integer>();
      public java.util.ArrayList<Touch>   _touchs     = new ArrayList<Touch>();
      public TouchEventType               _type       = TouchEventType.Down;


      protected EventProcessed createCopy() {
         final EventProcessed e = new EventProcessed();
         //         e._pointersID = (ArrayList<Integer>) this._pointersID.clone();
         e._pointersID = new ArrayList<Integer>(this._pointersID);
         //         e._touchs = (ArrayList<Touch>) this._touchs.clone();
         e._touchs = new ArrayList<Touch>(this._touchs);
         e._type = this._type;
         return e;
      }


      public void clear() {
         _pointersID.clear();
         _touchs.clear();
      }

   }

   EventProcessed _prevLastEvent = new EventProcessed();
   EventProcessed _lastEvent     = new EventProcessed();


   public TouchEvent processEvent(final Event event) {
      final EventProcessed auxEvent = _lastEvent.createCopy();
      _lastEvent.clear();

      switch (DOM.eventGetType(event)) {
         case Event.ONKEYDOWN:
            log("onKeyDown");
            break;
         case Event.ONKEYUP:
            log("onKeyUp");
            break;
         case Event.ONMOUSEWHEEL:
            log("onMousewheel");
            break;

         default:
            break;
      }

      return new TouchEvent(null);
   }


   /*
   ArrayList<Touch> _touches   = new ArrayList<Touch>();
   TouchEventType   _type      = TouchEventType.Down;
   boolean          _mouseDown = false;
   boolean          _keyDown   = false;


   public TouchEvent processEvent(final Event event) {
      //      final ArrayList<Touch> lastTouches = (ArrayList<Touch>) _touches.clone();
      final ArrayList<Touch> lastTouches = new ArrayList<Touch>(_touches);
      //      final TouchEventType lastType = _type;

      _touches.clear();

      final Vector2D pos = new Vector2D(event.getClientX(), event.getClientY());

      Vector2D prevPos;
      if (lastTouches.size() != 0) {
         prevPos = new Vector2D(lastTouches.get(0).getPos().x(), lastTouches.get(0).getPos().y());
      }
      else {
         prevPos = new Vector2D(0, 0);
      }

      final Touch t = new Touch(pos, prevPos);
      _touches.add(t);

      _type = TouchEventType.Down;

      switch (DOM.eventGetType(event)) {
         case Event.ONKEYDOWN: {
            // TODO: event.getShiftKey() does not work on Chrome
            if (event.getShiftKey() || (event.getKeyCode() == 16)) {
               _keyDown = true;
               log("on shift keyDown");
            }
            else {
               return null;
            }
            break;
         }
         case Event.ONKEYUP: {
            _keyDown = false;
            if (event.getShiftKey()) {
               log("on shift keyUp");
            }
            else {
               return null;
            }
            break;
         }
         case Event.ONMOUSEWHEEL: {
            log("onMouseWheel");
            _type = TouchEventType.Move;
            break;
         }
         case Event.ONMOUSEMOVE: {
            if (!_mouseDown) {
               return null;
            }
            _type = TouchEventType.Move;
            break;
         }
         case Event.ONMOUSEUP: {
            log("onMouseUP");
            _mouseDown = false;
            _type = TouchEventType.Up;
            break;
         }
         case Event.ONMOUSEDOWN: {
            log("onMouseDOWN");
            _mouseDown = true;
            _type = TouchEventType.Down;
            break;
         }
         default:
            return null;
      }

      //      return new TouchEvent(TouchEvent.create(_type, (ArrayList<Touch>) _touches.clone()));
      return new TouchEvent(TouchEvent.create(_type, new ArrayList<Touch>(_touches)));
   }


   // test
   public void log(final String msg) {
      GWT.log(msg);
   }
   
   */

   // test
   public void log(final String msg) {
      GWT.log(msg);
   }

}
