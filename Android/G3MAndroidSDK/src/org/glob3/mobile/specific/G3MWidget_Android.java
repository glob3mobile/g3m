package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IGL;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SimplePlanetRenderer;
import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.Vector2D;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class G3MWidget_Android extends GLSurfaceView implements
		OnGestureListener {

	G3MWidget _widget;
	ES2Renderer _es2renderer;

	public G3MWidget_Android(Context context) {
		super(context);

		setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
		setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
									// CHANNEL, HAVING TO FORCE PROPER BUFFER
									// ALLOCATION

		// Detect Long-Press events
		setLongClickable(true);

		// Debug flags
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
	}

	// The initialization of _widget occurs when the android widget is resized
	// to the screen size
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (_widget != null)
			return; // No further initialization needed

		int width = getWidth();
		int height = getHeight();

		// Creating factory
		IFactory factory = new Factory_Android(this.getContext());

		// RENDERERS
		CompositeRenderer comp = new CompositeRenderer();

		// Camera must be first
		CameraRenderer cr = new CameraRenderer();
		comp.addRenderer(cr);

		// Dummy cube
		DummyRenderer dummy = new DummyRenderer();
		comp.addRenderer(dummy);
		
    	IImage im =  ((Factory_Android)factory).createImageFromFileName("world.jpg");
    	
        // simple planet renderer, with a basic world image
        SimplePlanetRenderer spr = new SimplePlanetRenderer(im);
        comp.addRenderer(spr);


		ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
		IGL gl = new GL2();

		_widget = G3MWidget.create(factory, logger, gl, Planet.createEarth(),
				comp, width, height, Color.fromRGB((float) 0.0, (float) 0.1,
						(float) 0.2, (float) 1.0));

		// SETTING RENDERER
		_es2renderer = new ES2Renderer(this.getContext(), _widget);
		setRenderer(_es2renderer);
	}

	@Override
	public boolean onDown(MotionEvent event) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	// EVENTS

	java.util.ArrayList<Integer> pointersID = new ArrayList<Integer>();
	java.util.ArrayList<Touch> touchs = new ArrayList<Touch>();

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// SAVING LAST EVENT
		ArrayList<Integer> lastPointersID = (ArrayList<Integer>) pointersID.clone();
		ArrayList<Touch> lastTouchs = (ArrayList<Touch>) touchs.clone();

		pointersID.clear();
		touchs.clear();

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
			touchs.add(t);
			pointersID.add(pointerID);
		}
		
		//If a move event has not change the position of pointers we dismiss it
		if (event.getAction() == MotionEvent.ACTION_MOVE){
			double dist = 0;
			for (int i = 0; i < touchs.size();i++){
				Vector2D v= touchs.get(i).getPos().sub(touchs.get(i).getPrevPos());
				dist += v.squaredLength();
			}
			if (dist == 0) 
				return false;
		}

		TouchEventType type = TouchEventType.Down;

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE:
			type = TouchEventType.Move;
			break;

		case MotionEvent.ACTION_POINTER_1_UP:
		case MotionEvent.ACTION_POINTER_2_UP:
		case MotionEvent.ACTION_UP:
			type = TouchEventType.Up;
			break;

		case MotionEvent.ACTION_POINTER_2_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_DOWN:
			type = TouchEventType.Down;
			break;
		default:
			break;
		}

		final TouchEvent te = new TouchEvent(TouchEvent.create(type,
				(ArrayList<Touch>) touchs.clone()));

		Log.d("", "TE " + type.toString());
		for (int i = 0; i < touchs.size(); i++)
			Log.d("", "TE P " + touchs.get(i).getPos().x() + " "
					+ touchs.get(i).getPrevPos().x());

		// SEND MESSAGE TO RENDER THREAD
		queueEvent(new Runnable() {
			@Override
			public void run() {
				_widget.onTouchEvent(te);
			}
		});
		return true;
	}

}
