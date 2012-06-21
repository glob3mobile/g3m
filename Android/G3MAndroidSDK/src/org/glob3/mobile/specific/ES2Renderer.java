package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

class ES2Renderer implements GLSurfaceView.Renderer {
	
	final G3MWidget _widget;

    private int _program;
    final Context _context;

    public ES2Renderer(Context context, G3MWidget widget) {
    	_context = context;
        _widget = widget;
    }
    
    public void onDrawFrame(GL10 glUnused) {
    	
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        _widget.getGL().useProgram(_program);
        
        // Enable the depth tests and Cull Face
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
        
        _widget.render();
    }
    
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
        GLES20.glViewport(0, 0, width, height);
        
        _widget.onResizeViewportEvent(width,height);
    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
    	_program =  GL2Shaders.createProgram(GL2Shaders.getVertexShader(), GL2Shaders.getFragmentShader());
    }


}