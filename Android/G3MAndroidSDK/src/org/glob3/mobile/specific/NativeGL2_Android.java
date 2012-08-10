package org.glob3.mobile.specific;

import java.nio.ByteBuffer;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	  final int getBitField(GLBufferType b) {
		    switch (b) {
		      case ColorBuffer:
		        return GLES20.GL_COLOR_BUFFER_BIT;
		      case DepthBuffer:
		        return GLES20.GL_DEPTH_BUFFER_BIT;
		    }
			return 0;
		  }
		  
		int getEnum(GLFeature f) {
		    switch (f) {
		      case PolygonOffsetFill:
		        return GLES20.GL_POLYGON_OFFSET_FILL;
		      case DepthTest:
		        return GLES20.GL_DEPTH_TEST;
		      case Blend:
		        return GLES20.GL_BLEND;
		      case CullFacing:
		        return GLES20.GL_CULL_FACE;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLCullFace f) {
		    switch (f) {
		      case Front:
		        return GLES20.GL_FRONT;
		      case FrontAndBack:
		        return GLES20.GL_FRONT_AND_BACK;
		      case Back:
		        return GLES20.GL_BACK;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLType t) {
		    switch (t) {
		      case Float:
		        return GLES20.GL_FLOAT;
		      case UnsignedByte:
		        return GLES20.GL_UNSIGNED_BYTE;
		      case UnsignedInt:
		        return GLES20.GL_UNSIGNED_INT;
		      case Int:
		        return GLES20.GL_INT;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLPrimitive p) {
		    switch (p) {
		      case TriangleStrip:
		        return GLES20.GL_TRIANGLE_STRIP;
		      case Lines:
		        return GLES20.GL_LINES;
		      case LineLoop:
		        return GLES20.GL_LINE_LOOP;
		      case Points:
		        return GLES20.GL_POINTS;
		    }
			return 0;
		  }
		  
		  final GLError getError(int e)  {
		    switch (e) {
		      case GLES20.GL_NO_ERROR:
		        return GLError.NoError;
		      case GLES20.GL_INVALID_ENUM:
		        return GLError.InvalidEnum;
		      case GLES20.GL_INVALID_VALUE:
		        return GLError.InvalidValue;
		      case GLES20.GL_INVALID_OPERATION:
		        return GLError.InvalidOperation;
		      case GLES20.GL_OUT_OF_MEMORY:
		        return GLError.OutOfMemory;
		    }
		    return GLError.UnknownError; 
		  }
		  
		  final int getEnum(GLBlendFactor b) {
		    switch (b) {
		      case SrcAlpha:
		        return GLES20.GL_SRC_ALPHA;
		      case OneMinusSrcAlpha:
		        return GLES20.GL_ONE_MINUS_SRC_ALPHA;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLAlignment a) {
		    switch (a) {
		      case Unpack:
		        return GLES20.GL_UNPACK_ALIGNMENT;
		      case Pack:
		        return GLES20.GL_PACK_ALIGNMENT;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLTextureType t) {
		    switch (t) {
		      case Texture2D:
		        return GLES20.GL_TEXTURE_2D;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLTextureParameter t) {
		    switch (t) {
		      case MinFilter:
		        return GLES20.GL_TEXTURE_MIN_FILTER;
		      case MagFilter:
		        return GLES20.GL_TEXTURE_MAG_FILTER;
		      case WrapS:
		        return GLES20.GL_TEXTURE_WRAP_S;
		      case WrapT:
		        return GLES20.GL_TEXTURE_WRAP_T;
		    }
			return 0;
		  }
		  
		  final int getValue(GLTextureParameterValue t) {
		    switch (t) {
		      case Linear:
		        return GLES20.GL_LINEAR;
		      case ClampToEdge:
		        return GLES20.GL_CLAMP_TO_EDGE;
		    }
			return 0;
		  }
		  
		  final int getEnum(GLFormat f) {
		    switch (f) {
		      case RGBA:
		        return GLES20.GL_RGBA;
		    }
			return 0;
		  }

	
	// TOO SLOW (PUT ANDROID BUG)
	static public long timeConvertingFloat = 0;
	Map<float[], FloatBuffer> arrayToBufferMap = new HashMap<float[], FloatBuffer>();

	private FloatBuffer floatArrayToFloatBuffer(final float[] fv) {

		// Clear the map when is big
		final int size = arrayToBufferMap.size();
		if (size > 5000) {
			arrayToBufferMap.clear();
		}

		if (!arrayToBufferMap.containsKey(fv)) {

			// Log.d("GL", "CREANDO BUFFER");
			final ByteBuffer byteBuf = ByteBuffer.allocateDirect(fv.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			final FloatBuffer fb = byteBuf.asFloatBuffer();

			final long t1 = System.nanoTime();

			fb.put(fv); // /TOO SLOW UNTIL VERSION GINGERBEAD (BECAUSE OF THIS,
						// USE HASHMAP)

			final long t2 = System.nanoTime();
			timeConvertingFloat += (t2 - t1);

			fb.position(0);

			arrayToBufferMap.put(fv, fb);

			return fb;
		}

		return arrayToBufferMap.get(fv);
	}

	/////////////////////////////

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
		FloatBuffer fb = floatArrayToFloatBuffer(value);
		GLES20.glUniformMatrix4fv(location, count, transpose, fb);
	}

	@Override
	public void clearColor(float red, float green, float blue, float alpha) {
		GLES20.glClearColor(red, green, blue, alpha);
	}

	@Override
	public void clear(int nBuffer, GLBufferType[] buffers) {
	    int b = 0x00000000;
	    for (int i = 0; i < buffers.length; i++) {
	      b |= getBitField(buffers[i]);
	    }
	    GLES20.glClear(b);
	}

	@Override
	public void uniform4f(int location, float v0, float v1, float v2, float v3) {
		GLES20.glUniform4f(location, v0, v1, v2, v3);
	}

	@Override
	public void enable(GLFeature feature) {
		GLES20.glEnable(getEnum(feature));
	}

	@Override
	public void disable(GLFeature feature) {
		GLES20.glDisable(getEnum(feature));
	}

	@Override
	public void polygonOffset(float factor, float units) {
		GLES20.glPolygonOffset(factor, units);
	}

	@Override
	public void vertexAttribPointer(int index, int size, GLType type,
			boolean normalized, int stride, Object pointer) {
		float[] floatArray = (float[]) pointer;
		final FloatBuffer fb = floatArrayToFloatBuffer(floatArray);
		GLES20.glVertexAttribPointer(index, size, getEnum(type), normalized, stride, fb);
	}

	@Override
	public void drawElements(GLPrimitive mode, int count, GLType type,
			Object indices) {
		if (type == GLType.Int || type == GLType.UnsignedInt){
			int[] ind = (int[]) indices;
			final IntBuffer indexBuffer = IntBuffer.wrap(ind);
			GLES20.glDrawElements(getEnum(mode), count, getEnum(type), indexBuffer);
		}
	}

	@Override
	public void lineWidth(float width) {
		GLES20.glLineWidth(width);
	}

	@Override
	public GLError getError() {
		return getError(GLES20.glGetError());
	}

	@Override
	public void blendFunc(GLBlendFactor sfactor, GLBlendFactor dfactor) {
		GLES20.glBlendFunc(getEnum(sfactor), getEnum(dfactor));
	}

	@Override
	public void bindTexture(GLTextureType target, int texture) {
		GLES20.glBindTexture(getEnum(target), texture);
	}

	@Override
	public void deleteTextures(int n, int[] textures) {
		final IntBuffer tex = IntBuffer.wrap(textures);
		GLES20.glDeleteTextures(n, tex);
	}

	@Override
	public void enableVertexAttribArray(int location) {
		GLES20.glEnableVertexAttribArray(location);
	}

	@Override
	public void disableVertexAttribArray(int location) {
		GLES20.glDisableVertexAttribArray(location);
	}

	@Override
	public void pixelStorei(GLAlignment pname, int param) {
		GLES20.glPixelStorei(getEnum(pname), param);
	}

	@Override
	public ArrayList<Integer> genTextures(int n) {
		int[] tex = new int[n];
		GLES20.glGenTextures(n, tex, 0);
		ArrayList<Integer> ai = new ArrayList<Integer>();
		for(int i = 0; i < n; i++){
			ai.add(tex[i]);
		}
		return ai;
	}

	@Override
	public void texParameteri(GLTextureType target, GLTextureParameter par,
			GLTextureParameterValue v) {
		GLES20.glTexParameteri(getEnum(target), getEnum(par), getValue(v));
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
