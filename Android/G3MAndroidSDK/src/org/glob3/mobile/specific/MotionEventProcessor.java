

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2F;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;


public final class MotionEventProcessor {

   //Stores pointer positions, id and event type
   private static class EventProcessed {
      // LAST EVENT PROCESSED
      private ArrayList<Integer> _pointersID = new ArrayList<Integer>();
      private ArrayList<Touch>   _touchs     = new ArrayList<Touch>();
      private TouchEventType     _type       = TouchEventType.Down;


      @SuppressWarnings("unchecked")
      @Override
      protected EventProcessed clone() {
         final EventProcessed e = new EventProcessed();
         e._pointersID = (ArrayList<Integer>) _pointersID.clone();
         e._touchs = (ArrayList<Touch>) _touchs.clone();
         e._type = _type;
         return e;
      }


      public void clear() {
         _pointersID.clear();
         _touchs.clear();
      }

   }

   private EventProcessed       _prevLastEvent = new EventProcessed();
   private final EventProcessed _lastEvent     = new EventProcessed();


   public TouchEvent processEvent(final MotionEvent event) {

      // SAVING LAST EVENT TO CREATE A NEW ONE
      final EventProcessed auxEvent = _lastEvent.clone();
      _lastEvent.clear();

      for (int i = 0; i < event.getPointerCount(); i++) {

         final int pointerID = event.getPointerId(i);
         final PointerCoords pc = new PointerCoords();
         event.getPointerCoords(i, pc);
         // TOUCH EVENT
         final Vector2F pos = new Vector2F(pc.x, pc.y);

         Vector2F prevPos = null;
         if (event.getAction() != MotionEvent.ACTION_UP) {
            if (auxEvent._pointersID.contains(pointerID)) {
               final Touch lastT = auxEvent._touchs.get(auxEvent._pointersID.indexOf(pointerID));
               prevPos = new Vector2F(lastT.getPos()._x, lastT.getPos()._y);
            }
            else {
               prevPos = Vector2F.zero();
            }
         }
         else {

            if (_prevLastEvent._pointersID.contains(pointerID)) {
               final Touch lastT = _prevLastEvent._touchs.get(_prevLastEvent._pointersID.indexOf(pointerID));
               prevPos = new Vector2F(lastT.getPos()._x, lastT.getPos()._y);
            }
            else {
               prevPos = Vector2F.zero();
            }

         }


         final Touch t = new Touch(pos, prevPos);
         _lastEvent._touchs.add(t);
         _lastEvent._pointersID.add(pointerID);
      }

      // If a move event has not change the position of pointers
      // or if the first two fingers movement just moves one
      // we dismiss it
      if (event.getAction() == MotionEvent.ACTION_MOVE) {

         // Log.d("", "TE MOVE");
         double dist = 0;
         for (int i = 0; i < _lastEvent._touchs.size(); i++) {
            final double d = _lastEvent._touchs.get(i).getPos().sub(_lastEvent._touchs.get(i).getPrevPos()).squaredLength();

            if ((d == 0) && (auxEvent._type == TouchEventType.Down)) {
               return null;
            }

            dist += d;
            // Log.d("", "TE MOVE DIST " + d);
         }

         if (dist == 0) {
            return null;
         }
      }

      _lastEvent._type = TouchEventType.Down;

      switch (event.getAction()) {

         case MotionEvent.ACTION_MOVE:
            _lastEvent._type = TouchEventType.Move;
            break;
         /**
          * MotionEvent.ACTION_POINTER_1_UP and MotionEvent.ACTION_POINTER_2_UP are deprecated. MotionEvent.ACTION_POINTER_UP
          * added as fallback
          */
         //         case MotionEvent.ACTION_POINTER_1_UP:
         //         case MotionEvent.ACTION_POINTER_2_UP:
         case MotionEvent.ACTION_POINTER_UP:
         case MotionEvent.ACTION_UP:
            _lastEvent._type = TouchEventType.Up;
            break;
         /**
          * MotionEvent.ACTION_POINTER_2_DOWN is deprecated.
          */
         //         case MotionEvent.ACTION_POINTER_2_DOWN:
         case MotionEvent.ACTION_POINTER_DOWN:
         case MotionEvent.ACTION_DOWN:
            _lastEvent._type = TouchEventType.Down;
            break;
         default:
            break;
      }

      @SuppressWarnings("unchecked")
      final TouchEvent te = new TouchEvent(TouchEvent.create(_lastEvent._type, (ArrayList<Touch>) _lastEvent._touchs.clone()));

      //		Log.d("", "TE " + type.toString());
      //		for (int i = 0; i < touchs.size(); i++)
      //			Log.d("", "TE P " + touchs.get(i).getPos().x() + " "
      //					+ touchs.get(i).getPrevPos().x());

      //Saving the last event to use its position in Event Up as previous Position
      _prevLastEvent = auxEvent.clone();

      return te;
   }


   public TouchEvent processDoubleTapEvent(final MotionEvent event) {
      final PointerCoords pc = new PointerCoords();
      event.getPointerCoords(0, pc);
      final Vector2F pos = new Vector2F(pc.x, pc.y);
      final Touch t = new Touch(pos, pos, (byte) 2);

      final TouchEvent te = TouchEvent.create(TouchEventType.Down, t);

      return te;
   }

}
