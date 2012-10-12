

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLProgramId;
import org.glob3.mobile.generated.IGLTextureId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.MutableMatrix44D;

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

   private final JavaScriptObject _gl;


   public NativeGL_WebGL(final JavaScriptObject webGLContext) {
      _gl = webGLContext;
   }


   @Override
   public void useProgram(final IGLProgramId program) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      jsUseProgram(p);
      //  printGLError();
   }


   private native void jsUseProgram(JavaScriptObject program) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.useProgram(program);
   }-*/;


   @Override
   public int getAttribLocation(final IGLProgramId program,
                                final String name) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      final int result = jsGetAttribLocation(p, name);
      //  printGLError();
      return result;
   }


   //   private void printGLError() {
   //      println("" + jsGetError());
   //   }


   private native int jsGetAttribLocation(final JavaScriptObject program,
                                          final String name) /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.getAttribLocation(program, name);
   }-*/;


   @Override
   public IGLUniformID getUniformLocation(final IGLProgramId program,
                                          final String name) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      final JavaScriptObject u = jsGetUniformLocation(p, name);
      final IGLUniformID result = new GLUniformID_WebGL(u);
      //  printGLError();
      return result;
   }


   private native JavaScriptObject jsGetUniformLocation(final JavaScriptObject program,
                                                        final String name) /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.getUniformLocation(program, name);
   }-*/;


   @Override
   public void uniform2f(final IGLUniformID loc,
                         final float x,
                         final float y) {
      final JavaScriptObject l = ((GLUniformID_WebGL) loc).getId();
      jsUniform2f(l, x, y);
      //  printGLError();
   }


   private native void jsUniform2f(final JavaScriptObject loc,
                                   final float x,
                                   final float y) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.uniform2f(loc, x, y);
   }-*/;


   @Override
   public void uniform1f(final IGLUniformID loc,
                         final float x) {
      final JavaScriptObject l = ((GLUniformID_WebGL) loc).getId();
      jsUniform1f(l, x);
      //  printGLError();
   }


   private native void jsUniform1f(final JavaScriptObject loc,
                                   final float x) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform1f(loc, x);
   }-*/;


   @Override
   public void uniform1i(final IGLUniformID loc,
                         final int v) {
      final JavaScriptObject l = ((GLUniformID_WebGL) loc).getId();
      jsUniform1i(l, v);
      //  printGLError();
   }


   private native void jsUniform1i(final JavaScriptObject loc,
                                   final int x) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform1i(loc, x);
   }-*/;


   //   @Override
   //   public native void uniformMatrix4fv(final IGLUniformID location,
   //                                       final boolean transpose,
   //                                       final IFloatBuffer buffer) /*-{
   //		var id = location.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();
   //		var value = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getBuffer()();
   //		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniformMatrix4fv(
   //				id, transpose, value);
   //   }-*/;

   @Override
   public native void uniformMatrix4fv(final IGLUniformID location,
                                       final boolean transpose,
                                       final MutableMatrix44D matrix) /*-{
		var id = location.@org.glob3.mobile.specific.GLUniformID_WebGL::getId()();

		var buffer = matrix.@org.glob3.mobile.generated.MutableMatrix44D::getColumnMajorFloatBuffer()();

		var value = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getBuffer()();

		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniformMatrix4fv(
				id, transpose, value);
   }-*/;


   @Override
   public native void clearColor(final float red,
                                 final float green,
                                 final float blue,
                                 final float alpha) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.clearColor(red,
				green, blue, alpha);
   }-*/;


   @Override
   public native void clear(final int buffers) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.clear(buffers);
   }-*/;


   @Override
   public void uniform4f(final IGLUniformID location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      final JavaScriptObject l = ((GLUniformID_WebGL) location).getId();
      jsUniform4f(l, v0, v1, v2, v3);
      //  printGLError();
   }


   private native void jsUniform4f(final JavaScriptObject loc,
                                   final float v0,
                                   final float v1,
                                   final float v2,
                                   final float v3) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform4f(loc, v0,
				v1, v2, v3);
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
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.polygonOffset(
				factor, units);
   }-*/;


   @Override
   public native void vertexAttribPointer(final int index,
                                          final int size,
                                          final boolean normalized,
                                          final int stride,
                                          final IFloatBuffer buffer) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		// var webGLBuffer = gl.createBuffer();
		var webGLBuffer = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getWebGLBuffer(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);
		gl.bindBuffer(gl.ARRAY_BUFFER, webGLBuffer);

		var array = buffer.@org.glob3.mobile.specific.FloatBuffer_WebGL::getBuffer()();
		gl.bufferData(gl.ARRAY_BUFFER, array, gl.STATIC_DRAW);

		gl.vertexAttribPointer(index, size, gl.FLOAT, normalized, stride, 0);
   }-*/;


   @Override
   public native void drawElements(final int mode,
                                   final int count,
                                   final IIntBuffer indices) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		// var buffer = gl.createBuffer();
		var webGLBuffer = indices.@org.glob3.mobile.specific.IntBuffer_WebGL::getWebGLBuffer(Lcom/google/gwt/core/client/JavaScriptObject;)(gl);
		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, webGLBuffer);

		var array = indices.@org.glob3.mobile.specific.IntBuffer_WebGL::getBuffer()();
		gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, array, gl.STATIC_DRAW);

		//TODO CHECK UNSIGNED SHORT
		gl.drawElements(mode, count, gl.UNSIGNED_SHORT, 0);

		//		gl.deleteBuffer(buffer);
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
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.blendFunc(sfactor,
				dfactor);
   }-*/;


   @Override
   public native void bindTexture(final int target,
                                  final IGLTextureId texture) /*-{
		var id = texture.@org.glob3.mobile.specific.GLTextureId_WebGL::getWebGLTexture()();
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bindTexture(target,
				id);
   }-*/;


   @Override
   public boolean deleteTexture(final IGLTextureId texture) {
      //      String error = "";
      //      for (int i = 0; i < n; i++) {
      //         final JavaScriptObject id = ((GLTextureId_WebGL) textures[i]).getWebGLTexture();
      //         jsDeleteTexture(id);
      //         //         error += jsGetError();
      //      }
      //      println(error);

      jsDeleteTexture(((GLTextureId_WebGL) texture).getWebGLTexture());
      return false;
   }


   private native void jsDeleteTexture(final JavaScriptObject id) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.deleteTexture(id);
   }-*/;


   @Override
   public native void enableVertexAttribArray(final int location) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.enableVertexAttribArray(location);
   }-*/;


   @Override
   public native void disableVertexAttribArray(final int location) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.disableVertexAttribArray(location);
   }-*/;


   @Override
   public native void pixelStorei(final int pname,
                                  final int param) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.pixelStorei(pname,
				param);
   }-*/;


   @Override
   public ArrayList<IGLTextureId> genTextures(final int n) {
      final ArrayList<IGLTextureId> array = new ArrayList<IGLTextureId>();

      //      String error = "";
      for (int i = 0; i < n; i++) {
         final JavaScriptObject webGLTexture = jsCreateTexture(i); //WebGLTexture
         //         error += jsGetError();
         array.add(new GLTextureId_WebGL(webGLTexture));
      }
      //      println(error);
      return array;
   }


   private native JavaScriptObject jsCreateTexture(final int i) /*-{
		var texture = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.createTexture();
		texture.id = i;
		return texture;
   }-*/;


   @Override
   public native void texParameteri(final int target,
                                    final int par,
                                    final int v) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.texParameteri(
				target, par, v);
   }-*/;


   @Override
   public void texImage2D(final IImage image,
                          final int format) {
      final JavaScriptObject im = ((Image_WebGL) image).getImage(); //IMAGE JS
      jsTexImage2D(im, format);
      //  printGLError();
   }


   private native void jsTexImage2D(final JavaScriptObject image,
                                    final int format) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		var TEXTURE_2D = gl.TEXTURE_2D;
		var UNSIGNED_BYTE = gl.UNSIGNED_BYTE;

		gl.texImage2D(TEXTURE_2D, 0, format, format, UNSIGNED_BYTE, image);
   }-*/;


   @Override
   public native void generateMipmap(final int target) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.generateMipmap(target);
   }-*/;


   @Override
   public native void drawArrays(final int mode,
                                 final int first,
                                 final int count) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.drawArrays(mode,
				first, count);
   }-*/;


   @Override
   public native void cullFace(final int c) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.cullFace(c);
   }-*/;


   @Override
   public void getIntegerv(final int v,
                           final int[] i) {
      // TODO getIntegerv is not implemented in WebGL. Check with v!=viewport

      final JsArrayInteger aux = jsGetIntegerv(v);
      for (int j = 0; j < aux.length(); j++) {
         i[j] = aux.get(j);
      }
      //  printGLError();
   }


   private native JsArrayInteger jsGetIntegerv(final int v) /*-{
		var result = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.getParameter(v);
		var intArray = new Array();
		for (i = 0; i < result.length; i++) {
			intArray.push(result[i]);
		}
		return intArray;
   }-*/;


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
   public native int Primitive_TriangleStrip() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TRIANGLE_STRIP;
   }-*/;


   @Override
   public native int Primitive_Lines() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINES;
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
   public native int TextureParameterValue_Linear() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINEAR;
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


   private void println(final String msg) {
      if (!msg.equals("0")) {
         //         System.out.println(msg);
      }
   }

}
