package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class ES2Renderer implements GLSurfaceView.Renderer {

	int _mProgram = 0; // OPENGL PROGRAM
	final G3MWidget _widget;
	int _width, _height;

	public ES2Renderer(G3MWidget widget) {
		_widget = widget;

	}

	@Override
	public void onDrawFrame(GL10 arg0) {

		// Check frame buffer status
		if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
			Log.d("GL", "PROBLEM");
		}
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Enable the depth tests and Cull Face
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		GLES20.glViewport(0, 0, _width, _height);

		_widget.getGL().useProgram(_mProgram);

		_widget.render();
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {

		_width = width;
		_height = height;

		GLES20.glViewport(0, 0, width, height);

		_widget.onResizeViewportEvent(width, height);

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		//GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		//GLES20.glCullFace(GLES20.GL_BACK);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		//GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		//GLES20.glCullFace(GLES20.GL_BACK);

		// Creating OpenGL program
		final String mFragmentShader = GL2Shaders.getFragmentShader();
		final String mVertexShader = GL2Shaders.getVertexShader();
		_mProgram = GL2Shaders.createProgram(mVertexShader, mFragmentShader); // CREATING PROGRAM
	}

}
