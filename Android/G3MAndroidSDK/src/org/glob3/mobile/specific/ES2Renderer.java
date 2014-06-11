

package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GL;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Looper;


//import org.glob3.mobile.generated.IGLProgramId;


public final class ES2Renderer
         implements
            GLSurfaceView.Renderer {

   private static final int        GOAL_MS_PER_FRAME = 33;   // 33ms => 30FPS

   final private G3MWidget_Android _widgetAndroid;

   private boolean                 _hasRendered      = false;
   private int                     _width;
   private int                     _height;
   private final GL                _gl;

   private final NativeGL2_Android _nativeGL;


   private long                    _startTime;


   public ES2Renderer(final G3MWidget_Android widget) {
      _widgetAndroid = widget;
      _width = 1;
      _height = 1;
      _nativeGL = new NativeGL2_Android();
      _gl = new GL(_nativeGL, false);

      _startTime = System.currentTimeMillis();
   }


   void setOpenGLThread(final Thread openGLThread) {
      _nativeGL.setOpenGLThread(openGLThread);
   }


   @Override
   public void onDrawFrame(final GL10 glUnused) {
      if (Looper.myLooper() == null) {
         Looper.prepare();
      }

      _hasRendered = true;

      final G3MWidget widget = _widgetAndroid.getG3MWidget();
      widget.render(_width, _height);

      // experimental FPS reduction - DGD
      final long now = System.currentTimeMillis();
      final long timeElapsedInRender = now - _startTime;
      final long timeLeftInMS = GOAL_MS_PER_FRAME - timeElapsedInRender;
      if (timeLeftInMS > 0) {
         //         System.gc();
         //
         //         timeElapsedInRender = System.currentTimeMillis() - _startTime;
         //         timeLeftInMS = GOAL_MS_PER_FRAME - timeElapsedInRender;
         //         if (timeLeftInMS > 0) {
         try {
            //ILogger.instance().logInfo("**** sleeping OpenGL thread for " + timeLeftInMS + "ms");
            Thread.sleep(timeLeftInMS);
         }
         catch (final InterruptedException e) {
         }
         //         }
         _startTime = System.currentTimeMillis();
      }
      else {
         _startTime = now;
      }

   }


   @Override
   public void onSurfaceChanged(final GL10 glUnused,
                                final int width,
                                final int height) {
      // Ignore the passed-in GL10 interface, and use the GLES20
      // class's static methods instead.
      GLES20.glViewport(0, 0, width, height);
      _width = width;
      _height = height;

      if (_hasRendered) {
         _widgetAndroid.getG3MWidget().onResizeViewportEvent(width, height);
      }
   }


   @Override
   public void onSurfaceCreated(final GL10 glUnused,
                                final EGLConfig config) {
   }


   public final GL getGL() {
      return _gl;
   }


}
