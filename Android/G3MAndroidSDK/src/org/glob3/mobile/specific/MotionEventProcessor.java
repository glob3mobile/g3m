package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2D;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class MotionEventProcessor {
	
	
	class EventProcessed{
		
		// LAST EVENT PROCESSED
		public java.util.ArrayList<Integer> _pointersID = new ArrayList<Integer>();
		public java.util.ArrayList<Touch> _touchs = new ArrayList<Touch>();
		public TouchEventType _type = TouchEventType.Down;
		
		protected EventProcessed clone() {
			EventProcessed e = new EventProcessed();
			e._pointersID = (ArrayList<Integer>) this._pointersID.clone();
			e._touchs = (ArrayList<Touch>) this._touchs.clone();
			e._type = this._type;
			return e;
		}
		
		public void clear() {
			_pointersID.clear();
			_touchs.clear();
		}
		
	}
	
	EventProcessed _lastEvent = new EventProcessed();

	public TouchEvent processEvent(MotionEvent event) {

		// SAVING LAST EVENT
//		ArrayList<Integer> lastPointersID = (ArrayList<Integer>) _pointersID.clone();
//		ArrayList<Touch> lastTouchs = (ArrayList<Touch>) _touchs.clone();
//		TouchEventType lastType = _type;
		
		EventProcessed auxEvent = _lastEvent.clone();
		
		_lastEvent.clear();
		

//		_pointersID.clear();
//		_touchs.clear();

		for (int i = 0; i < event.getPointerCount(); i++) {

			int pointerID = event.getPointerId(i);
			PointerCoords pc = new PointerCoords();
			event.getPointerCoords(i, pc);
			// TOUCH EVENT
			Vector2D pos = new Vector2D(pc.x, pc.y);

			Vector2D prevPos;
//			if (lastPointersID.contains(pointerID)) {
//				Touch lastT = lastTouchs.get(lastPointersID.indexOf(pointerID));
//				prevPos = new Vector2D(lastT.getPos().x(), lastT.getPos().y());
//			} else {
//				prevPos = new Vector2D(0, 0);
//			}
			if (auxEvent._pointersID.contains(pointerID)) {
				Touch lastT = auxEvent._touchs.get(auxEvent._pointersID.indexOf(pointerID));
				prevPos = new Vector2D(lastT.getPos().x(), lastT.getPos().y());
			} else {
				prevPos = new Vector2D(0, 0);
			}
			

			Touch t = new Touch(pos, prevPos);
//			_touchs.add(t);
//			_pointersID.add(pointerID);
			
			_lastEvent._touchs.add(t);
			_lastEvent._pointersID.add(pointerID);
		}

		// If a move event has not change the position of pointers
		// or if the first two fingers movement just moves one
		// we dismiss it
//		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//
//			// Log.d("", "TE MOVE");
//			double dist = 0;
//			for (int i = 0; i < _touchs.size(); i++) {
//				double d = _touchs.get(i).getPos()
//						.sub(_touchs.get(i).getPrevPos()).squaredLength();
//
//				if (d == 0 && lastType == TouchEventType.Down)
//					return null;
//
//				dist += d;
//				// Log.d("", "TE MOVE DIST " + d);
//			}
//
//			if (dist == 0)
//				return null;
//		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			// Log.d("", "TE MOVE");
			double dist = 0;
			for (int i = 0; i < _lastEvent._touchs.size(); i++) {
				double d = _lastEvent._touchs.get(i).getPos()
						.sub(_lastEvent._touchs.get(i).getPrevPos()).squaredLength();

				if (d == 0 && auxEvent._type == TouchEventType.Down)
					return null;

				dist += d;
				// Log.d("", "TE MOVE DIST " + d);
			}

			if (dist == 0)
				return null;
		}

		_lastEvent._type = TouchEventType.Down;

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE:
			_lastEvent._type = TouchEventType.Move;
			break;

		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
		case MotionEvent.ACTION_UP:
			_lastEvent._type = TouchEventType.Up;
			break;

		case MotionEvent.ACTION_POINTER_2_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_DOWN:
			_lastEvent._type = TouchEventType.Down;
			break;
		default:
			break;
		}

		final TouchEvent te = new TouchEvent(TouchEvent.create(_lastEvent._type, (ArrayList<Touch>) _lastEvent._touchs.clone()));

//		Log.d("", "TE " + type.toString());
//		for (int i = 0; i < touchs.size(); i++)
//			Log.d("", "TE P " + touchs.get(i).getPos().x() + " "
//					+ touchs.get(i).getPrevPos().x());

		return te;
	}
	

}
