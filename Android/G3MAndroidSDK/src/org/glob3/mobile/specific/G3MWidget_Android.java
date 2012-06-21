package org.glob3.mobile.specific;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IGL;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.SimplePlanetRenderer;
import org.glob3.mobile.generated.TouchEvent;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class G3MWidget_Android extends GLSurfaceView implements
		OnGestureListener {

	G3MWidget _widget;
	ES2Renderer _es2renderer;

	final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();

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

		// marks renderer
		MarksRenderer marks = new MarksRenderer();
		comp.addRenderer(marks);

		Mark m1 = new Mark("Fuerteventura", "", "plane.png", new Geodetic3D(
				Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
		marks.addMark(m1);

		// simple planet renderer, with a basic world image
		IImage im = ((Factory_Android) factory)
				.createImageFromFileName("world.jpg");
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
	
	public boolean onTouchEvent(MotionEvent event) {
		
		final TouchEvent te = _motionEventProcessor.processEvent(event);
		
		if (te != null){
			// SEND MESSAGE TO RENDER THREAD
			queueEvent(new Runnable() {
				@Override
				public void run() {
					_widget.onTouchEvent(te);
				}
			});
			return true;
		} else{
			return false;
		}
	}
}
