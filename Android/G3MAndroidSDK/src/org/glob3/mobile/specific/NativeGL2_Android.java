package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.GLAlignment;
import org.glob3.mobile.generated.GLBlendFactor;
import org.glob3.mobile.generated.GLBufferType;
import org.glob3.mobile.generated.GLCullFace;
import org.glob3.mobile.generated.GLError;
import org.glob3.mobile.generated.GLFeature;
import org.glob3.mobile.generated.GLFormat;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.GLTextureParameter;
import org.glob3.mobile.generated.GLTextureParameterValue;
import org.glob3.mobile.generated.GLTextureType;
import org.glob3.mobile.generated.GLType;
import org.glob3.mobile.generated.INativeGL;

import android.opengl.GLES20;

public class NativeGL2_Android extends INativeGL {

	@Override
	public void useProgram(int program) {
		GLES20.glUseProgram(program);
	}

	@Override
	public int getAttribLocation(int program, String name) {
		return GLES20.glGetAttribLocation(program, name);
	}

	@Override
	public int getUniformLocation(int program, String name) {
		return GLES20.glGetUniformLocation(program, name);
	}

	@Override
	public void uniform2f(int loc, float x, float y) {
		GLES20.glUniform2f(loc, x, y);
	}

	@Override
	public void uniform1f(int loc, float x) {
		GLES20.glUniform1f(loc, x);
	}

	@Override
	public void uniform1i(int loc, int v) {
		GLES20.glUniform1i(loc, v);
	}

	@Override
	public void uniformMatrix4fv(int location, int count, boolean transpose,
			float[] value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearColor(float red, float green, float blue, float alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear(int nBuffer, GLBufferType[] buffers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uniform4f(int location, float v0, float v1, float v2, float v3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable(GLFeature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disable(GLFeature feature) {
		// TODO Auto-generated method stub

	}

	@Override
	public void polygonOffset(float factor, float units) {
		// TODO Auto-generated method stub

	}

	@Override
	public void vertexAttribPointer(int index, int size, GLType type,
			boolean normalized, int stride, Object pointer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawElements(GLPrimitive mode, int count, GLType type,
			Object indices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lineWidth(float width) {
		// TODO Auto-generated method stub

	}

	@Override
	public GLError getError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindTexture(GLTextureType target, int texture) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTextures(int n, int[] textures) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableVertexAttribArray(int location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableVertexAttribArray(int location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pixelStorei(GLAlignment pname, int param) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Integer> genTextures(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void texParameteri(GLTextureType target, GLTextureParameter par,
			GLTextureParameterValue v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void texImage2D(GLTextureType target, int level,
			GLFormat internalFormat, int width, int height, int border,
			GLFormat format, GLType type, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawArrays(GLPrimitive mode, int first, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cullFace(GLCullFace c) {
		// TODO Auto-generated method stub

	}

}
