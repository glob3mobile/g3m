

package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Looper;


class ES2Renderer
         implements
            GLSurfaceView.Renderer {

   final G3MWidget_Android _widget;

   private int             _program;
   final Context           _context;
   boolean                 _hasRendered = false;


   public ES2Renderer(final Context context,
                      final G3MWidget_Android widget) {
      _context = context;
      _widget = widget;
   }


   @Override
   public void onDrawFrame(final GL10 glUnused) {

      if (Looper.myLooper() == null) {
         Looper.prepare();
      }

      _hasRendered = true;

      final G3MWidget widget = _widget.getWidget();

      GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
      widget.getGL().useProgram(_program);

      // Enable the depth tests and Cull Face
      GLES20.glEnable(GLES20.GL_DEPTH_TEST);
      GLES20.glEnable(GLES20.GL_CULL_FACE);

      widget.render();
   }


   @Override
   public void onSurfaceChanged(final GL10 glUnused,
                                final int width,
                                final int height) {
      // Ignore the passed-in GL10 interface, and use the GLES20
      // class's static methods instead.
      GLES20.glViewport(0, 0, width, height);

      if (_hasRendered) {
         _widget.getWidget().onResizeViewportEvent(width, height);
      }
   }


   @Override
   public void onSurfaceCreated(final GL10 glUnused,
                                final EGLConfig config) {
      _program = GL2Shaders.createProgram(GL2Shaders.getVertexShader(), GL2Shaders.getFragmentShader());
   }


}
