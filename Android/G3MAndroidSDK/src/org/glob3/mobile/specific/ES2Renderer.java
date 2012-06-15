package org.glob3.mobile.specific;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.glob3.mobile.generated.G3MWidget;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class ES2Renderer implements GLSurfaceView.Renderer {
	
	int _mProgram; //OPENGL PROGRAM
	G3MWidget _widget;
	
	public ES2Renderer(G3MWidget widget)
	{
		 _widget = widget;
		
		 //LOADING SHADERS CODE
		 final String mFragmentShader = GL2Shaders.getFragmentShader();
		 final String mVertexShader = GL2Shaders.getVertexShader();
		
		 //CREATING PROGRAM
		 _mProgram = GL2Shaders.createProgram(mVertexShader, mFragmentShader);
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		
		 // Enable the depth tests and Cull Face
	     GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	     GLES20.glEnable(GLES20.GL_CULL_FACE);
	     
	     _widget.render();
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		// TODO Auto-generated method stub
		
	}

}
