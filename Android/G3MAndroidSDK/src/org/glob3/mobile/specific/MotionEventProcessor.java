package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2D;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class MotionEventProcessor {
	
	// LAST EVENT PROCESSED
	java.util.ArrayList<Integer> _pointersID = new ArrayList<Integer>();
	java.util.ArrayList<Touch> _touchs = new ArrayList<Touch>();
	TouchEventType _type = TouchEventType.Down;

	public TouchEvent processEvent(MotionEvent event) {

		// SAVING LAST EVENT
		ArrayList<Integer> lastPointersID = (ArrayList<Integer>) _pointersID.clone();
		ArrayList<Touch> lastTouchs = (ArrayList<Touch>) _touchs.clone();
		TouchEventType lastType = _type;

		_pointersID.clear();
		_touchs.clear();

		for (int i = 0; i < event.getPointerCount(); i++) {

			int pointerID = event.getPointerId(i);
			PointerCoords pc = new PointerCoords();
			event.getPointerCoords(i, pc);
			// TOUCH EVENT
			Vector2D pos = new Vector2D(pc.x, pc.y);

			Vector2D prevPos;
			if (lastPointersID.contains(pointerID)) {
				Touch lastT = lastTouchs.get(lastPointersID.indexOf(pointerID));
				prevPos = new Vector2D(lastT.getPos().x(), lastT.getPos().y());
			} else {
				prevPos = new Vector2D(0, 0);
			}

			Touch t = new Touch(pos, prevPos);
			_touchs.add(t);
			_pointersID.add(pointerID);
		}

		// If a move event has not change the position of pointers
		// or if the first two fingers movement just moves one
		// we dismiss it
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			// Log.d("", "TE MOVE");
			double dist = 0;
			for (int i = 0; i < _touchs.size(); i++) {
				double d = _touchs.get(i).getPos()
						.sub(_touchs.get(i).getPrevPos()).squaredLength();

				if (d == 0 && lastType == TouchEventType.Down)
					return null;

				dist += d;
				// Log.d("", "TE MOVE DIST " + d);
			}

			if (dist == 0)
				return null;
		}

		_type = TouchEventType.Down;

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE:
			_type = TouchEventType.Move;
			break;

		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
		case MotionEvent.ACTION_UP:
			_type = TouchEventType.Up;
			break;

		case MotionEvent.ACTION_POINTER_2_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_DOWN:
			_type = TouchEventType.Down;
			break;
		default:
			break;
		}

		final TouchEvent te = new TouchEvent(TouchEvent.create(_type,
				(ArrayList<Touch>) _touchs.clone()));

//		Log.d("", "TE " + type.toString());
//		for (int i = 0; i < touchs.size(); i++)
//			Log.d("", "TE P " + touchs.get(i).getPos().x() + " "
//					+ touchs.get(i).getPrevPos().x());

		return te;
	}
	

}
