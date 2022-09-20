

package org.glob3.mobile.specific;


import org.glob3.mobile.generated.*;

import android.annotation.*;
import android.opengl.*;
import android.util.*;
import android.view.*;
import android.view.GestureDetector.*;


public final class G3MWidget_Android
                                     extends
                                        GLSurfaceView
                                     implements
                                        OnGestureListener {

   private G3MWidget         _g3mWidget;
   private final ES2Renderer _es2renderer;

   private final MotionEventProcessor _motionEventProcessor = new MotionEventProcessor();
   private final GestureDetector      _gestureDetector;
   private Thread                     _openGLThread         = null;


   public G3MWidget_Android(final android.content.Context context) {
      this(context, null);
   }


   private void setOpenGLThread(final Thread openGLThread) {
      _openGLThread = openGLThread;
   }


   public void checkOpenGLThread() {
      if (_openGLThread != null) {
         final Thread currentThread = Thread.currentThread();
         if (currentThread != _openGLThread) {
            final String message = "OpenGL code executed from a Non-OpenGL thread.  (OpenGLThread=" + _openGLThread + ", CurrentThread=" + currentThread + ")";
            throw new RuntimeException(message);
         }
      }
   }


   // Needed to create widget from XML layout
   public G3MWidget_Android(final android.content.Context context,
                            final AttributeSet attrs) {
      super(context, attrs);

      initSingletons();

      setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
      setEGLConfigChooser(8, 8, 8, 8, 16, 0);
      //     setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
      // CHANNEL, HAVING TO FORCE PROPER BUFFER
      // ALLOCATION

      _es2renderer = new ES2Renderer(this);
      setRenderer(_es2renderer);

      queueEvent(new Runnable() {
         @Override
         public void run() {
            final Thread openGLThread = Thread.currentThread();
            ILogger.instance().logInfo("== OpenGL-Thread=%s", openGLThread.toString());
            _es2renderer.setOpenGLThread(openGLThread);
            setOpenGLThread(openGLThread);
         }
      });

      setLongClickable(true);

      // setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
      // setDebugFlags(DEBUG_CHECK_GL_ERROR);

      final OnDoubleTapListener doubleTapListener;
      if (!isInEditMode()) { // needed to allow visual edition of this widget
         //Double Tap Listener
         _gestureDetector  = new GestureDetector(context, this);
         doubleTapListener = new OnDoubleTapListener() {
                              @Override
                              public boolean onSingleTapConfirmed(final MotionEvent e) {
                                 return false;
                              }


                              @Override
                              public boolean onDoubleTapEvent(final MotionEvent event) {
                                 final TouchEvent te = MotionEventProcessor.processDoubleTapEvent(event);

                                 queueEvent(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                         _g3mWidget.onTouchEvent(te);
                                                      }
                                                   });

                                 return true;
                              }


                              @Override
                              public boolean onDoubleTap(final MotionEvent event) {
                                 final TouchEvent te = MotionEventProcessor.processDoubleTapEvent(event);

                                 queueEvent(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                         _g3mWidget.onTouchEvent(te);
                                                      }
                                                   });

                                 return true;
                              }
                           };
         _gestureDetector.setOnDoubleTapListener(doubleTapListener);
      }
      else {
         _gestureDetector  = null;
         doubleTapListener = null;
      }
   }


   private void initSingletons() {
      final ILogger         logger        = new Logger_Android(LogLevel.ErrorLevel);
      final IFactory        factory       = new Factory_Android(getContext());
      final IStringUtils    stringUtils   = new StringUtils_Android();
      final IStringBuilder  stringBuilder = new StringBuilder_Android(IStringBuilder.DEFAULT_FLOAT_PRECISION);
      final IMathUtils      mathUtils     = new MathUtils_Android();
      final IJSONParser     jsonParser    = new JSONParser_Android();
      final ITextUtils      textUtils     = new TextUtils_Android();
      final IDeviceAttitude devAttitude   = new DeviceAttitude_Android(getContext());
      final IDeviceLocation devLoc        = new DeviceLocation_Android(getContext(), (long) 500.0, 0.0f);

      G3MWidget.initSingletons(logger, factory, stringUtils, stringBuilder, mathUtils, jsonParser, textUtils, devAttitude, devLoc);
   }


   @Override
   protected void onSizeChanged(final int w,
                                final int h,
                                final int oldw,
                                final int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);

      //      if (_es2renderer == null) {
      //         _es2renderer = new ES2Renderer(this);
      //         setRenderer(_es2renderer);
      //      }
   }


   @SuppressLint("ClickableViewAccessibility")
   @Override
   public boolean onTouchEvent(final MotionEvent event) {

      //Notifying gestureDetector for DoubleTap recognition
      _gestureDetector.onTouchEvent(event);

      final TouchEvent te = _motionEventProcessor.processEvent(event);
      if (te == null) {
         return false;
      }

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _g3mWidget.onTouchEvent(te);
         }
      });
      return true;
   }


   @Override
   public boolean onDown(final MotionEvent arg0) {
      return false;
   }


   @Override
   public boolean onFling(final MotionEvent e1,
                          final MotionEvent e2,
                          final float velocityX,
                          final float velocityY) {
      return false;
   }


   @Override
   public void onLongPress(final MotionEvent e) {
      final MotionEvent.PointerCoords pc = new MotionEvent.PointerCoords();
      e.getPointerCoords(0, pc);

      final Touch      t  = new Touch(new Vector2F(pc.x, pc.y), (byte) 1);
      final TouchEvent te = TouchEvent.create(TouchEventType.LongPress, t);

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _g3mWidget.onTouchEvent(te);
         }
      });
   }


   @Override
   public boolean onScroll(final MotionEvent e1,
                           final MotionEvent e2,
                           final float distanceX,
                           final float distanceY) {
      return false;
   }


   @Override
   public void onShowPress(final MotionEvent e) {

   }


   @Override
   public boolean onSingleTapUp(final MotionEvent e) {
      return false;
   }


   public G3MWidget getG3MWidget() {
      if (_g3mWidget == null) {
         throw new RuntimeException("LAZY INITIALIZATION NEEDED");
         //         initWidget();
         //         _g3mWidget.onResume();
      }
      return _g3mWidget;
   }


   @Override
   public void onPause() {
      if (_es2renderer != null) {
         _g3mWidget.onPause();
         super.onPause();
      }
   }


   @Override
   public void onResume() {
      if (_es2renderer != null) {
         super.onResume();
         _g3mWidget.onResume();
      }
   }


   public void onDestroy() {
      getG3MWidget().onDestroy();
   }


   //   public void closeStorage() {
   //      if (IDownloader.instance() != null) {
   //         IDownloader.instance().stop();
   //      }
   //      if (IStorage.instance() != null) {
   //         // _storage.onPause(null);
   //         ((SQLiteStorage_Android) IStorage.instance()).close();
   //      }
   //   }


   public Camera getNextCamera() {
      return getG3MWidget().getNextCamera();
   }


   public WidgetUserData getUserData() {
      return getG3MWidget().getUserData();
   }


   public void setAnimatedCameraPosition(final Geodetic3D position,
                                         final TimeInterval interval) {
      getG3MWidget().setAnimatedCameraPosition(interval, position);
   }


   public void setAnimatedCameraPosition(final Geodetic3D position) {
      getG3MWidget().setAnimatedCameraPosition(position);
   }


   public void setCameraPosition(final Geodetic3D position) {
      getG3MWidget().setCameraPosition(position);
   }


   public CameraRenderer getCameraRenderer() {
      return getG3MWidget().getCameraRenderer();
   }


   public void cancelCameraAnimation() {
      getG3MWidget().cancelCameraAnimation();
   }


   public void setCameraHeading(final Angle heading) {
      getG3MWidget().setCameraHeading(heading);
   }


   public void setCameraPitch(final Angle pitch) {
      getG3MWidget().setCameraPitch(pitch);
   }


   public void setCameraRoll(final Angle roll) {
      getG3MWidget().setCameraRoll(roll);
   }


   public void setCameraHeadingPitchRoll(final Angle heading,
                                         final Angle pitch,
                                         final Angle roll) {
      getG3MWidget().setCameraHeadingPitchRoll(heading, pitch, roll);
   }


   public void setWidget(final G3MWidget widget) {
      _g3mWidget = widget;
   }


   public GL getGL() {
      return _es2renderer.getGL();
   }


   public G3MContext getG3MContext() {
      return getG3MWidget().getG3MContext();
   }


}
