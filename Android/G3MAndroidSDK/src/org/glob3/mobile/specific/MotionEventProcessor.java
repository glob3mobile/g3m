
package org.glob3.mobile.specific;

import java.util.*;

import org.glob3.mobile.generated.*;

import android.view.*;
import android.view.MotionEvent.*;

public final class MotionEventProcessor {

   //Stores pointer positions, id and event type
   private static class EventProcessed {
      // LAST EVENT PROCESSED
      // private ArrayList<Integer> _pointersID = new ArrayList<>();
      private final ArrayList<Touch> _touchs;
      private TouchEventType         _type;

      private EventProcessed(final ArrayList<Touch> touchs,
                             final TouchEventType type) {
         _touchs = touchs;
         _type   = type;
      }

      private EventProcessed() {
         _touchs = new ArrayList<>();
         _type   = TouchEventType.Down;
      }

      @Override
      protected EventProcessed clone() {
         return new EventProcessed(new ArrayList<>(_touchs), _type);
      }

      public void clear() {
         // _pointersID.clear();
         _touchs.clear();
      }

   }

   private final EventProcessed _lastEvent = new EventProcessed();

   public TouchEvent processEvent(final MotionEvent event) {

      // SAVING LAST EVENT TO CREATE A NEW ONE
      _lastEvent.clear();

      for (int i = 0; i < event.getPointerCount(); i++) {
         //         final int pointerID = event.getPointerId(i);

         final PointerCoords pc = new PointerCoords();
         event.getPointerCoords(i, pc);
         final Vector2F pos = new Vector2F(pc.x, pc.y);

         final Touch t = new Touch(pos, (byte) 1);
         _lastEvent._touchs.add(t);
         //         _lastEvent._pointersID.add(pointerID);
      }

      //      // If a move event has not change the position of pointers
      //      // or if the first two fingers movement just moves one
      //      // we dismiss it
      //      if (event.getAction() == MotionEvent.ACTION_MOVE) {
      //
      //         // Log.d("", "TE MOVE");
      //         double dist = 0;
      //         for (final Touch element : _lastEvent._touchs) {
      //            final double d = element.getPos().sub(element.getPrevPos()).squaredLength();
      //
      //            if ((d == 0) && (auxEvent._type == TouchEventType.Down)) {
      //               return null;
      //            }
      //
      //            dist += d;
      //            // Log.d("", "TE MOVE DIST " + d);
      //         }
      //
      //         if (dist == 0) {
      //            return null;
      //         }
      //      }

      _lastEvent._type = TouchEventType.Down;

      switch (event.getAction()) {

      case MotionEvent.ACTION_MOVE:
         _lastEvent._type = TouchEventType.Move;
         break;
      /**
       * MotionEvent.ACTION_POINTER_1_UP and MotionEvent.ACTION_POINTER_2_UP are
       * deprecated. MotionEvent.ACTION_POINTER_UP added as fallback
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

      final TouchEvent te = TouchEvent.create(_lastEvent._type, new ArrayList<>(_lastEvent._touchs));

      //		Log.d("", "TE " + type.toString());
      //		for (int i = 0; i < touchs.size(); i++)
      //			Log.d("", "TE P " + touchs.get(i).getPos().x() + " "
      //					+ touchs.get(i).getPrevPos().x());

      return te;
   }

   static public TouchEvent processDoubleTapEvent(final MotionEvent event) {
      final PointerCoords pc = new PointerCoords();
      event.getPointerCoords(0, pc);

      final Vector2F pos      = new Vector2F(pc.x, pc.y);
      final byte     tapCount = (byte) 2;

      return TouchEvent.create(TouchEventType.Down, new Touch(pos, tapCount));
   }

}
