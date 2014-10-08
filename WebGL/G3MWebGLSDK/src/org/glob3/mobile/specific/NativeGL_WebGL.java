

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.GPUAttribute;
import org.glob3.mobile.generated.GPUProgram;
import org.glob3.mobile.generated.GPUUniform;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLTextureId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IShortBuffer;
import org.glob3.mobile.generated.Matrix44D;
import org.glob3.mobile.generated.ShaderType;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;


public final class NativeGL_WebGL
         extends
            INativeGL {
   /*      
   Warning: Error: WebGL: Drawing without vertex attrib 0 array enabled forces the browser to do expensive emulation
   work when running on desktop OpenGL platforms, for example on Mac. It is preferable to always draw with vertex 
   attrib 0 array enabled, by using bindAttribLocation to bind some always-used attribute to location 0.
   */

   private final JavaScriptObject                      _gl;

   private final java.util.ArrayList<JavaScriptObject> _shaderList = new java.util.ArrayList<JavaScriptObject>();


   public NativeGL_WebGL(final JavaScriptObject webGLContext) {
      _gl = webGLContext;
   }


   @Override
   public native void uniform2f(final IGLUniformID loc,
                                final float x,
                                final float y) /*-{
		var locId = loc.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform2f(locId, x, y);
   }-*/;


   @Override
   public native void uniform1f(final IGLUniformID loc,
                                final float x) /*-{
		var locId = loc.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform1f(locId, x);
   }-*/;


   @Override
   public native void uniform1i(final IGLUniformID loc,
                                final int v)/*-{
		var locId = loc.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform1i(locId, v);
   }-*/;


   @Override
   public native void uniform4f(final IGLUniformID location,
                                final float v0,
                                final float v1,
                                final float v2,
                                final float v3) /*-{
		var locId = location.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform4f(locId, v0, v1, v2, v3);
   }-*/;


   @Override
   public native void uniformMatrix4fv(final IGLUniformID location,
                                       final boolean transpose,
                                       final Matrix44D matrix) /*-{
		var id = location.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
		var buffer = matrix.@org.glob3.mobile.generated.Matrix44D::getColumnMajorFloatBuffer()();
		var value = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getBuffer()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniformMatrix4fv(id, transpose, value);
   }-*/;


   @Override
   public native void clearColor(final float red,
                                 final float green,
                                 final float blue,
                                 final float alpha) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.clearColor(red, green, blue, alpha);
   }-*/;


   @Override
   public native void clear(final int buffers) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.clear(buffers);
   }-*/;


   @Override
   public native void enable(final int feature) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.enable(feature);
   }-*/;


   @Override
   public native void disable(final int feature) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.disable(feature);
   }-*/;


   @Override
   public native void polygonOffset(final float factor,
                                    final float units) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.polygonOffset(factor, units);
   }-*/;


   @Override
   public native void vertexAttribPointer(final int index,
                                          final int size,
                                          final boolean normalized,
                                          final int stride,
                                          final IFloatBuffer buffer) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		//		// var webGLBuffer = gl.createBuffer();
		//		var webGLBuffer = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getWebGLBuffer(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);
		//		gl.bindBuffer(gl.ARRAY_BUFFER, webGLBuffer);
		//
		//		var array = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getBuffer()();
		//		gl.bufferData(gl.ARRAY_BUFFER, array, gl.STATIC_DRAW);

		var webGLBuffer = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::bindVBO(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);

		gl.vertexAttribPointer(index, size, gl.FLOAT, normalized, stride, 0);
   }-*/;


   @Override
   public native void drawElements(final int mode,
                                   final int count,
                                   final IShortBuffer indices) /*-{
		//debugger;

		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		var webGLBuffer = indices.@org.glob3.mobile.specific.ShortBuffer_WebGL::getWebGLBuffer(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);
		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, webGLBuffer);

		var array = indices.@org.glob3.mobile.specific.ShortBuffer_WebGL::getBuffer()();
		gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, array, gl.STATIC_DRAW);

		gl.drawElements(mode, count, gl.UNSIGNED_SHORT, 0);
   }-*/;


   @Override
   public native void lineWidth(final float width) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.lineWidth(width);
   }-*/;


   @Override
   public native int getError() /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var e = gl.getError();

		if (e == gl.INVALID_ENUM) {
			//                      debugger;
			console.error("NativeGL_WebGL: INVALID_ENUM");
		}

		if (e == gl.INVALID_VALUE) {
			//                   debugger;
			console.error("NativeGL_WebGL: INVALID_VALUE");
		}

		if (e == gl.INVALID_OPERATION) {
			//                   debugger;
			console.error("NativeGL_WebGL: INVALID_OPERATION");
		}

		if (e == gl.OUT_OF_MEMORY) {
			//                   debugger;
			console.error("NativeGL_WebGL: INVALID_OPERATION");
		}

		if (e == gl.CONTEXT_LOST_WEBGL) {
			console.error("NativeGL_WebGL: CONTEXT_LOST_WEBGL");
		}

		return Number(e);
   }-*/;


   @Override
   public native void blendFunc(final int sfactor,
                                final int dfactor) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.blendFunc(sfactor, dfactor);
   }-*/;


   @Override
   public native void bindTexture(final int target,
                                  final IGLTextureId texture) /*-{
		var id = texture.@org.glob3.mobile.specific.GLTextureId_WebGL::getWebGLTexture()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bindTexture(target, id);
   }-*/;


   @Override
   public native boolean deleteTexture(final IGLTextureId texture) /*-{
		var textureID = texture.@org.glob3.mobile.specific.GLTextureId_WebGL::getWebGLTexture()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.deleteTexture(textureID);
		return false;
   }-*/;


   @Override
   public native void enableVertexAttribArray(final int location) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.enableVertexAttribArray(location);
   }-*/;


   @Override
   public native void disableVertexAttribArray(final int location) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.disableVertexAttribArray(location);
   }-*/;


   @Override
   public native void pixelStorei(final int pname,
                                  final int param) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.pixelStorei(pname, param);
   }-*/;


   @Override
   public native ArrayList<IGLTextureId> genTextures(final int n) /*-{
		var array = @java.util.ArrayList::new()();
		for (i = 0; i < n; i++) {
			var texture = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.createTexture();

			var textureID = @org.glob3.mobile.specific.GLTextureId_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(texture);
			array.@java.util.ArrayList::add(Ljava/lang/Object;)(textureID);
		}

		return array;
   }-*/;


   @Override
   public native void texParameteri(final int target,
                                    final int par,
                                    final int v) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.texParameteri(target, par, v);
   }-*/;


   @Override
   public native void texImage2D(final IImage image,
                                 final int format) /*-{
		var img = image.@org.glob3.mobile.specific.Image_WebGL::getImage()();
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var TEXTURE_2D = gl.TEXTURE_2D;
		var UNSIGNED_BYTE = gl.UNSIGNED_BYTE;

		gl.texImage2D(TEXTURE_2D, 0, format, format, UNSIGNED_BYTE, img);
   }-*/;


   @Override
   public native void generateMipmap(final int target) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.generateMipmap(target);
   }-*/;


   @Override
   public native void drawArrays(final int mode,
                                 final int first,
                                 final int count) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.drawArrays(mode, first, count);
   }-*/;


   @Override
   public native void cullFace(final int c) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.cullFace(c);
   }-*/;


   @Override
   public native void getIntegerv(final int v,
                                  final int[] i) /*-{
		// TODO Warning: getIntegerv is not implemented in WebGL.
		var result = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.getParameter(v);

		this.@org.glob3.mobile.specific.NativeGL_WebGL::convertJsArrayIngeterToJavaArrayInt(Lcom/google/gwt/core/client/JsArrayInteger;[I)(result, i);
   }-*/;


   private void convertJsArrayIngeterToJavaArrayInt(final JsArrayInteger jsArray,
                                                    final int[] javaArray) {
      for (int i = 0; i < jsArray.length(); i++) {
         javaArray[i] = jsArray.get(i);
      }
   }


   @Override
   public native int CullFace_Front() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FRONT;
   }-*/;


   @Override
   public native int CullFace_Back() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.BACK;
   }-*/;


   @Override
   public native int CullFace_FrontAndBack() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FRONT_AND_BACK;
   }-*/;


   @Override
   public native int BufferType_ColorBuffer() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.COLOR_BUFFER_BIT;
   }-*/;


   @Override
   public native int BufferType_DepthBuffer() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.DEPTH_BUFFER_BIT;
   }-*/;


   @Override
   public native int Feature_PolygonOffsetFill() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.POLYGON_OFFSET_FILL;
   }-*/;


   @Override
   public native int Feature_DepthTest() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.DEPTH_TEST;
   }-*/;


   @Override
   public native int Feature_Blend() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.BLEND;
   }-*/;


   @Override
   public native int Feature_CullFace() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.CULL_FACE;
   }-*/;


   @Override
   public native int Type_Float() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT;
   }-*/;


   @Override
   public native int Type_UnsignedByte() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNSIGNED_BYTE;
   }-*/;


   @Override
   public native int Type_UnsignedInt() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNSIGNED_INT;
   }-*/;


   @Override
   public native int Type_Int() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.INT;
   }-*/;


   @Override
   public native int Primitive_Triangles() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TRIANGLES;
   }-*/;


   @Override
   public native int Primitive_TriangleStrip() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TRIANGLE_STRIP;
   }-*/;


   @Override
   public native int Primitive_TriangleFan() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TRIANGLE_FAN;
   }-*/;


   @Override
   public native int Primitive_Lines() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINES;
   }-*/;


   @Override
   public native int Primitive_LineStrip() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINE_STRIP;
   }-*/;


   @Override
   public native int Primitive_LineLoop() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINE_LOOP;
   }-*/;


   @Override
   public native int Primitive_Points() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.POINTS;
   }-*/;


   @Override
   public native int BlendFactor_SrcAlpha() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.SRC_ALPHA;
   }-*/;


   @Override
   public native int BlendFactor_OneMinusSrcAlpha() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ONE_MINUS_SRC_ALPHA;
   }-*/;


   @Override
   public native int TextureType_Texture2D() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_2D;
   }-*/;


   @Override
   public native int TextureParameter_MinFilter() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_MIN_FILTER;
   }-*/;


   @Override
   public native int TextureParameter_MagFilter() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_MAG_FILTER;
   }-*/;


   @Override
   public native int TextureParameter_WrapS() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_WRAP_S;
   }-*/;


   @Override
   public native int TextureParameter_WrapT() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_WRAP_T;
   }-*/;


   @Override
   public native int TextureParameterValue_ClampToEdge() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.CLAMP_TO_EDGE;
   }-*/;


   @Override
   public native int Alignment_Pack() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.PACK_ALIGNMENT;
   }-*/;


   @Override
   public native int Alignment_Unpack() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNPACK_ALIGNMENT;
   }-*/;


   @Override
   public native int Format_RGBA() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.RGBA;
   }-*/;


   @Override
   public native int Variable_Viewport() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.VIEWPORT;
   }-*/;


   @Override
   public native int Error_NoError() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.NO_ERROR;
   }-*/;


   @Override
   public native int createProgram() /*-{
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.createProgram();
		shaderList.@java.util.ArrayList::add(Ljava/lang/Object;)(jsoProgram);
		var id = shaderList.@java.util.ArrayList::size()() - 1;

		return id;
   }-*/;


   @Override
   public native boolean deleteProgram(final int program) /*-{
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(program);

		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.deleteProgram(jsoProgram);
   }-*/;


   @Override
   public native void attachShader(final int program,
                                   final int shader) /*-{
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(program);
		var jsoShader = shaderList.@java.util.ArrayList::get(I)(shader);

		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.attachShader(jsoProgram, jsoShader);
   }-*/;


   @Override
   public native int createShader(final ShaderType type) /*-{
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var shaderType;

		switch (type) {
		case @org.glob3.mobile.generated.ShaderType::VERTEX_SHADER:
			shaderType = gl.VERTEX_SHADER;
			break;
		case @org.glob3.mobile.generated.ShaderType::FRAGMENT_SHADER:
			shaderType = gl.FRAGMENT_SHADER;
			break;
		default:
			$wnd.alert("Unknown shader type");
			return 0;
		}
		var shader = gl.createShader(shaderType);
		shaderList.@java.util.ArrayList::add(Ljava/lang/Object;)(shader);
		var id = shaderList.@java.util.ArrayList::size()() - 1;

		return id;
   }-*/;


   @Override
   public native boolean compileShader(final int shader,
                                       final String source) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoShader = shaderList.@java.util.ArrayList::get(I)(shader);

		gl.shaderSource(jsoShader, source);
		gl.compileShader(jsoShader);

		return gl.getShaderParameter(jsoShader, gl.COMPILE_STATUS);
   }-*/;


   @Override
   public native boolean deleteShader(final int shader) /*-{
		//TODO: IMPLEMENTATION FAILS
		//debugger;
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoShader = shaderList.@java.util.ArrayList::get(I)(shader);
		return true;
		//return gl.deleteShader(jsoShader);
   }-*/;


   @Override
   public native void printShaderInfoLog(final int shader) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoShader = shaderList.@java.util.ArrayList::get(I)(shader);

		if (!gl.getShaderParameter(jsoShader, gl.COMPILE_STATUS)) {
			$wnd.alert("Error compiling shaders: " + gl.getShaderInfoLog(jsoShader));
		}
   }-*/;


   @Override
   public native boolean linkProgram(final int program) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(program);

		gl.linkProgram(jsoProgram);

		return gl.getProgramParameter(jsoProgram, gl.LINK_STATUS);
   }-*/;


   @Override
   public native void printProgramInfoLog(final int program) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(program);

		if (!gl.getProgramParameter(jsoProgram, gl.LINK_STATUS)) {
			$wnd.alert("Error linking program: " + gl.getProgramInfoLog(jsoProgram));
		}
   }-*/;


   @Override
   public native int BlendFactor_One() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ONE;
   }-*/;


   @Override
   public native int BlendFactor_Zero() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ZERO;
   }-*/;


   @Override
   public native void useProgram(GPUProgram program) /*-{
		var progInt = program.@org.glob3.mobile.generated.GPUProgram::getProgramID()();
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(progInt);
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.useProgram(jsoProgram);
   }-*/;


   @Override
   public native int Type_Vec2Float() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT_VEC2;
   }-*/;


   @Override
   public native int Type_Vec4Float() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT_VEC4;
   }-*/;


   @Override
   public native int Type_Bool() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.BOOL;
   }-*/;


   @Override
   public native int Type_Matrix4Float() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT_MAT4;
   }-*/;


   @Override
   public native int Variable_ActiveAttributes() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ACTIVE_ATTRIBUTES;
   }-*/;


   @Override
   public native int Variable_ActiveUniforms() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ACTIVE_UNIFORMS;
   }-*/;


   @Override
   public native void bindAttribLocation(GPUProgram program,
                                         int loc,
                                         String name) /*-{
		var progInt = program.@org.glob3.mobile.generated.GPUProgram::getProgramID()();
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(progInt);
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		gl.bindAttribLocation(jsoProgram, loc, name);
   }-*/;


   @Override
   public native int getProgramiv(GPUProgram program,
                                  int param) /*-{
		var progInt = program.@org.glob3.mobile.generated.GPUProgram::getProgramID()();
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(progInt);
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		//Return the value for the passed pname given the passed program. The type returned is the natural type for the requested pname
		return gl.getProgramParameter(jsoProgram, param);

   }-*/;


   @Override
   public native GPUUniform getActiveUniform(final GPUProgram program,
                                             final int i) /*-{
		var progInt = program.@org.glob3.mobile.generated.GPUProgram::getProgramID()();
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(progInt);

		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		var info = gl.getActiveUniform(jsoProgram, i);
		var id = gl.getUniformLocation(jsoProgram, info.name);

		var glUniformID = @org.glob3.mobile.specific.GLUniformID_WebGL::new(Lcom/google/gwt/core/client/JavaScriptObject;)(id);

		switch (info.type) {
		case gl.FLOAT_MAT4:
			return @org.glob3.mobile.generated.GPUUniformMatrix4Float::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		case gl.FLOAT_VEC4:
			return @org.glob3.mobile.generated.GPUUniformVec4Float::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		case gl.FLOAT:
			return @org.glob3.mobile.generated.GPUUniformFloat::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		case gl.FLOAT_VEC2:
			return @org.glob3.mobile.generated.GPUUniformVec2Float::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		case gl.FLOAT_VEC3:
			return @org.glob3.mobile.generated.GPUUniformVec3Float::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		case gl.BOOL:
			return @org.glob3.mobile.generated.GPUUniformBool::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		case gl.SAMPLER_2D:
			return @org.glob3.mobile.generated.GPUUniformSampler2D::new(Ljava/lang/String;Lorg/glob3/mobile/generated/IGLUniformID;)(info.name, glUniformID);
		default:
			return null;
			break;
		}
   }-*/;


   @Override
   public native GPUAttribute getActiveAttribute(final GPUProgram program,
                                                 final int i) /*-{
		var progInt = program.@org.glob3.mobile.generated.GPUProgram::getProgramID()();
		var shaderList = this.@org.glob3.mobile.specific.NativeGL_WebGL::_shaderList;
		var jsoProgram = shaderList.@java.util.ArrayList::get(I)(progInt);

		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		var info = gl.getActiveAttrib(jsoProgram, i);
		var id = gl.getAttribLocation(jsoProgram, info.name);

		switch (info.type) {
		case gl.FLOAT_VEC3:
			return @org.glob3.mobile.generated.GPUAttributeVec3Float::new(Ljava/lang/String;I)(info.name, id);
		case gl.FLOAT_VEC4:
			return @org.glob3.mobile.generated.GPUAttributeVec4Float::new(Ljava/lang/String;I)(info.name, id);
		case gl.FLOAT_VEC2:
			return @org.glob3.mobile.generated.GPUAttributeVec2Float::new(Ljava/lang/String;I)(info.name, id);
		default:
			return null;
			break;
		}
   }-*/;


   @Override
   public native void uniform3f(IGLUniformID location,
                                float x,
                                float y,
                                float z) /*-{
		var locId = location.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform3f(locId, x, y, z);
   }-*/;


   @Override
   public native int Type_Vec3Float() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT_VEC3;
   }-*/;


   @Override
   public native void depthMask(final boolean depthMask) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.depthMask(depthMask);
   }-*/;


   @Override
   public native int TextureParameterValue_Linear() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINEAR;
   }-*/;


   @Override
   public native int TextureParameterValue_Nearest() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.NEAREST;
   }-*/;


   @Override
   public native int TextureParameterValue_NearestMipmapNearest() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.NEAREST_MIPMAP_NEAREST;
   }-*/;


   @Override
   public native int TextureParameterValue_NearestMipmapLinear() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.NEAREST_MIPMAP_LINEAR;
   }-*/;


   @Override
   public native int TextureParameterValue_LinearMipmapNearest() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINEAR_MIPMAP_NEAREST;
   }-*/;


   @Override
   public native int TextureParameterValue_LinearMipmapLinear() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINEAR_MIPMAP_LINEAR;
   }-*/;


   @Override
   public native void setActiveTexture(final int i) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.activeTexture(gl.TEXTURE0 + i);
   }-*/;


@Override
public native void vertexAttribPointer(int index, int size, boolean normalized,
		int stride, IByteBuffer buffer) /*-{
//	debugger;
	var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
	var webGLBuffer = buffer.@org.glob3.mobile.specific.ByteBuffer_WebGL::getWebGLBuffer(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);
	gl.bindBuffer(gl.ARRAY_BUFFER, webGLBuffer);
	var array = buffer.@org.glob3.mobile.specific.ByteBuffer_WebGL::getBuffer()();
	gl.bufferData(gl.ARRAY_BUFFER, array, gl.STATIC_DRAW);
	gl.vertexAttribPointer(index, size, gl.UNSIGNED_BYTE, normalized, stride, 0);
	gl.getError();
}-*/;


@Override
public void deleteVBO(int x) {
	// TODO METHOD UNUSED IN WEBGL (for the time being)
	
}


@Override
public int getBoundVBO() {
	// TODO METHOD UNUSED IN WEBGL (for the time being)
	return 0;
}


@Override
public int genBuffer() {
	// TODO METHOD UNUSED IN WEBGL (for the time being)
	return 0;
}


@Override
public void bindVBO(int vbo) {
	// TODO Auto-generated method stub
	
}


}
