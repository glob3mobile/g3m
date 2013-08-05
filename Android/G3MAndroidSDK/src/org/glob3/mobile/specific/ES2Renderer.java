

package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GL;
import org.glob3.mobile.generated.ShaderProgram;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Looper;
import android.util.Log;


//import org.glob3.mobile.generated.IGLProgramId;


public final class ES2Renderer
         implements
            GLSurfaceView.Renderer {

   final private G3MWidget_Android _widgetAndroid;

   private boolean                 _hasRendered = false;
   private int                     _width;
   private int                     _height;
   private final GL                _gl;
   private ShaderProgram           _shaderProgram;

   private final NativeGL2_Android _nativeGL;


   // private ShaderProgram		   _shaderProgram2;


   public ES2Renderer(final G3MWidget_Android widget) {
      _widgetAndroid = widget;
      _width = 1;
      _height = 1;
      _nativeGL = new NativeGL2_Android();
      _gl = new GL(_nativeGL, false);
   }


   void setOpenGLThread(final Thread openglThread) {
      _nativeGL.setOpenGLThread(openglThread);
   }


   @Override
   public void onDrawFrame(final GL10 glUnused) {
      if (Looper.myLooper() == null) {
         Looper.prepare();
      }

      _hasRendered = true;

      final G3MWidget widget = _widgetAndroid.getG3MWidget();

      //GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
      widget.getGL().useProgram(_shaderProgram);

      // Enable the depth tests and Cull Face
      //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
      //GLES20.glEnable(GLES20.GL_CULL_FACE);

      widget.render(_width, _height);
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
      //_program = new GLProgramId_Android(GL2Shaders.createProgram(GL2Shaders.getVertexShader(), GL2Shaders.getFragmentShader()));
      _shaderProgram = new ShaderProgram(_gl);
      if (_shaderProgram.loadShaders(GL2Shaders.getVertexShader(), GL2Shaders.getFragmentShader()) == false) {
         Log.e("GL2Shaders", "Failed to load shaders");
      }

      //_shaderProgram2 = new ShaderProgram(_gl);
   }


   public final GL getGL() {
      return _gl;
   }


}
