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

public class G3MWidget_Android extends GLSurfaceView implements OnGestureListener {

	G3MWidget _widget;
	ES2Renderer _es2renderer;
	
	public G3MWidget_Android(Context context) {
		super(context);
			
		setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
		setEGLConfigChooser(true); //IT GIVES US A RGB DEPTH OF 8 BITS PER CHANNEL, HAVING TO FORCE PROPER BUFFER ALLOCATION
		
		//Detect Long-Press events
		setLongClickable(true);
		  
		//Debug flags
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
	}
	
	//The initialization of _widget occurs when the android widget is resized to the screen size
	protected void onSizeChanged (int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (_widget != null) return; //No further initialization needed
		
		int width = getWidth();
		int height = getHeight();
		
		//RENDERERS
	    CompositeRenderer comp = new CompositeRenderer();
	    
	    //Camera must be first
	    CameraRenderer cr = new CameraRenderer();
	    comp.addRenderer(cr);
	    
	    //Dummy cube
	    DummyRenderer dummy = new DummyRenderer();
	    comp.addRenderer(dummy);
		
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

	    //SETTING RENDERER
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
	
	//EVENTS
	
	/*
	
	class Pointer{
		public float x,y,prevX, prevY;
		public int id;
	}
	ArrayList<Pointer> activePointers;
	
	private static final int INVALID_POINTER_ID = -1;
	// The Ôactive pointerÕ is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;
	
	float mLastTouchX, mLastTouchY, mPosX, mPosY;

	// Existing code ...

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    final int action = ev.getAction();
	    switch (action & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN: {
	        final float x = ev.getX();
	        final float y = ev.getY();
	        
	        mLastTouchX = x;
	        mLastTouchY = y;

	        // Save the ID of this pointer
	        mActivePointerId = ev.getPointerId(0);
	        break;
	    }
	        
	    case MotionEvent.ACTION_MOVE: {
	        // Find the index of the active pointer and fetch its position
	        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
	        final float x = ev.getX(pointerIndex);
	        final float y = ev.getY(pointerIndex);
	        
	        final float dx = x - mLastTouchX;
	        final float dy = y - mLastTouchY;
	        
	        mPosX += dx;
	        mPosY += dy;
	        
	        mLastTouchX = x;
	        mLastTouchY = y;
	        
	        invalidate();
	        break;
	    }
	        
	    case MotionEvent.ACTION_UP: {
	        mActivePointerId = INVALID_POINTER_ID;
	        break;
	    }
	        
	    case MotionEvent.ACTION_CANCEL: {
	        mActivePointerId = INVALID_POINTER_ID;
	        break;
	    }
	    
	    case MotionEvent.ACTION_POINTER_UP: {
	        // Extract the index of the pointer that left the touch sensor
	        final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) 
	                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	        final int pointerId = ev.getPointerId(pointerIndex);
	        if (pointerId == mActivePointerId) {
	            // This was our active pointer going up. Choose a new
	            // active pointer and adjust accordingly.
	            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
	            mLastTouchX = ev.getX(newPointerIndex);
	            mLastTouchY = ev.getY(newPointerIndex);
	            mActivePointerId = ev.getPointerId(newPointerIndex);
	        }
	        break;
	    }
	    }
	    
	    return true;
	}
	*/
	
	
		java.util.ArrayList<Integer> pointersID = new ArrayList<Integer>();
		java.util.ArrayList<Touch> touchs = new ArrayList<Touch>();
		
	   @Override
	   public boolean onTouchEvent(MotionEvent event) {
		   
		   //SAVING LAST EVENT
		   java.util.ArrayList<Integer> lastPointersID = (ArrayList<Integer>) pointersID.clone();
		   java.util.ArrayList<Touch> lastTouchs = (ArrayList<Touch>) touchs.clone();
		   
		   pointersID.clear();
		   touchs.clear();
		   
		   for(int i = 0; i < event.getPointerCount(); i++){
			   
			   int pointerID = event.getPointerId(i);
			  PointerCoords pc = new PointerCoords();
			  event.getPointerCoords(i, pc);
			  //TOUCH EVENT
			  Vector2D pos = new Vector2D(pc.x, pc.y);
			  
			  Vector2D prevPos;
			  if (lastPointersID.contains(pointerID)){
				  Touch lastT = lastTouchs.get( lastPointersID.indexOf(pointerID));
				  prevPos = new Vector2D(lastT.getPos().x() , lastT.getPos().y());
			  } else{
				  prevPos = new Vector2D(0,0);
			  }
			  
			  Touch t = new Touch(pos, prevPos);
			  touchs.add(t);
			  pointersID.add(pointerID);
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
		   
		   final TouchEvent te = new TouchEvent(TouchEvent.create(type, (ArrayList<Touch>) touchs.clone()));
		   
		   Log.d("", "TE "  + type.toString());
		   for(int i = 0; i < touchs.size(); i++) 
			   Log.d("", "TE P " + touchs.get(i).getPos().x() + " " +  touchs.get(i).getPrevPos().x() ); 
			  

		 //SEND MESSAGE TO RENDER THREAD
	      queueEvent(new Runnable() { 
	         @Override
	         public void run() {
	        	 _widget.onTouchEvent(te);
	         }
	      });
		return true;
	   }
	


}
