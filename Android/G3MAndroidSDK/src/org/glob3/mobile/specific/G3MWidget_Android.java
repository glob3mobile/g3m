package org.glob3.mobile.specific;

import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DummyRenderer;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IGL;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.Planet;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class G3MWidget_Android extends GLSurfaceView implements OnGestureListener {

	final G3MWidget _widget;
	final ES2Renderer _es2renderer;
	
	public G3MWidget_Android(Context context) {
		super(context);
		
		int width = getWidth();
		int height = getHeight();
		
		//RENDERERS
	    CompositeRenderer comp = new CompositeRenderer();
	    DummyRenderer dummy = new DummyRenderer();
	    comp.addRenderer(dummy);
	    CameraRenderer cr = new CameraRenderer();
	    comp.addRenderer(cr);
		
	    IFactory factory = new Factory_Android();
	    ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
	    IGL gl  = new GL2();
		
	    _widget = G3MWidget.create(factory,
                logger,
                gl,
                Planet.createEarth(),
                comp,
                width, height,
                Color.fromRGB((float)0.0, (float)0.1, (float)0.2, (float)1.0)); 
	 
	      setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
	      setEGLConfigChooser(true); //IT GIVES US A RGB DEPTH OF 8 BITS PER CHANNEL, HAVING TO FORCE PROPER BUFFER ALLOCATION

	      //DETECT LONG PRESS
	      setLongClickable(true);

	      //SETTING RENDERER
	      _es2renderer = new ES2Renderer(_widget);
	      setRenderer(_es2renderer);
	      setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
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
	
	


}
