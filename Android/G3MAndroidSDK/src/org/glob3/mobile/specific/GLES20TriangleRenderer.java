package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

class GLES20TriangleRenderer implements GLSurfaceView.Renderer {
	
	final G3MWidget _widget;

    private int _program;
    final Context _context;

    public GLES20TriangleRenderer(Context context, G3MWidget widget) {
    	_context = context;
        _widget = widget;
    }
    
    public void onDrawFrame(GL10 glUnused) {
    	
    	
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        _widget.getGL().useProgram(_program);
        
        _widget.getGL().clearScreen(0.0f, 0.0f, 1.0f, 1.0f);
        _widget.getGL().color(1.0f, 0.0f, 0.0f, 1.0f);
        
        _widget.render();
        
        _widget.getGL().enableVertices();
        _widget.getGL().disableTextures();
        _widget.getGL().vertexPointer(3, 0, mTriangleVerticesData2);
      
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        checkGlError("glDrawArrays");
    }
    
    float znear = (float) 636500;

    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
        GLES20.glViewport(0, 0, width, height);
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
    	_program =  GL2Shaders.createProgram(GL2Shaders.getVertexShader(), GL2Shaders.getFragmentShader());
    	
    	for(int i = 0;  i < mTriangleVerticesData2.length; i++) mTriangleVerticesData2[i] *= 6e5;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("Renderer", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    private final float[] mTriangleVerticesData2 = {
            // X, Y, Z, U, V
            0, -1.0f, -0.5f,
            0, 1.0f, -0.5f,
            0, 0.0f,  1.11803399f};

}