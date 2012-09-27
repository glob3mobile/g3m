

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLProgramId;
import org.glob3.mobile.generated.IGLTextureId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.INativeGL;

import com.google.gwt.core.client.JavaScriptObject;


public class NativeGL_WebGL
         extends
            INativeGL {

   JavaScriptObject _gl;


   public NativeGL_WebGL(final JavaScriptObject webGLContext) {
      _gl = webGLContext;
   }


   @Override
   public void useProgram(final IGLProgramId program) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      jsUseProgram(p);
      println("" + jsGetError());
   }


   private native void jsUseProgram(JavaScriptObject program) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.useProgram(program);
   }-*/;


   @Override
   public int getAttribLocation(final IGLProgramId program,
                                final String name) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      final int result = jsGetAttribLocation(p, name);
      println("" + jsGetError());
      return result;
   }


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
      println("" + jsGetError());
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
      println("" + jsGetError());
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
      println("" + jsGetError());
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
      println("" + jsGetError());
   }


   private native void jsUniform1i(final JavaScriptObject loc,
                                   final int x) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform1i(loc, x);
   }-*/;


   @Override
   public void uniformMatrix4fv(final IGLUniformID location,
                                final int count,
                                final boolean transpose,
                                final float[] value) {
      final JavaScriptObject l = ((GLUniformID_WebGL) location).getId();

      final FloatBuffer_WebGL array = new FloatBuffer_WebGL(value);
      final JavaScriptObject buffer = array.getBuffer(); //Float32Array

      jsUniformMatrix4fv(l, count, transpose, buffer);
      println("" + jsGetError());
   }


   private native void jsUniformMatrix4fv(final JavaScriptObject loc,
                                          final int count,
                                          final boolean transpose,
                                          final JavaScriptObject value) /*-{
		//TODO IGNORING COUNT
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniformMatrix4fv(
				loc, transpose, value);
   }-*/;


   @Override
   public void clearColor(final float red,
                          final float green,
                          final float blue,
                          final float alpha) {
      jsClearColor(red, green, blue, alpha);
      println("" + jsGetError());
   }


   private native void jsClearColor(final float red,
                                    final float green,
                                    final float blue,
                                    final float alpha)/*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.clearColor(red,
				green, blue, alpha);
   }-*/;


   @Override
   public void clear(final int buffers) {
      jsClear(buffers);
      println("" + jsGetError());
   }


   private native void jsClear(final int buffers) /*-{
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
      println("" + jsGetError());
   }


   private native void jsUniform4f(final JavaScriptObject loc,
                                   final float v0,
                                   final float v1,
                                   final float v2,
                                   final float v3) /*-{
		//TODO IGNORING COUNT
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.uniform4f(loc, v0,
				v1, v2, v3);
   }-*/;


   @Override
   public void enable(final int feature) {
      jsEnable(feature);
      println("" + jsGetError());
   }


   private native void jsEnable(final int feature) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.enable(feature);
   }-*/;


   @Override
   public void disable(final int feature) {
      jsDisable(feature);
      println("" + jsGetError());
   }


   private native void jsDisable(final int feature) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.disable(feature);
   }-*/;


   @Override
   public void polygonOffset(final float factor,
                             final float units) {
      jsPolygonOffset(factor, units);
      println("" + jsGetError());
   }


   private native void jsPolygonOffset(final float factor,
                                       final float units) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.polygonOffset(
				factor, units);
   }-*/;


   @Override
   public void vertexAttribPointer(final int index,
                                   final int size,
                                   final boolean normalized,
                                   final int stride,
                                   final IFloatBuffer buffer) {
      //TODO CHECK NO CLIENT SIDE ARRAYS
      final JavaScriptObject jsbuffer = ((FloatBuffer_WebGL) buffer).getBuffer();
      jsVertexAttribPointer(index, size, normalized, stride, jsbuffer);
      println("" + jsGetError());
   }


   private native void jsVertexAttribPointer(final int index,
                                             final int size,
                                             final boolean normalized,
                                             final int stride,
                                             final JavaScriptObject array) /*-{
		var that = this;

		var ARRAY_BUFFER = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ARRAY_BUFFER;
		var STATIC_DRAW = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.STATIC_DRAW;
		var FLOAT = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT;

		var buffer = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.createBuffer();
		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bindBuffer(
				ARRAY_BUFFER, buffer);
		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bufferData(
				ARRAY_BUFFER, array, STATIC_DRAW);

		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.vertexAttribPointer(index, size, FLOAT, normalized, stride, 0);

   }-*/;


   @Override
   public void drawElements(final int mode,
                            final int count,
                            final IIntBuffer indices) {
      jsDrawElements(mode, count, ((IntBuffer_WebGL) indices).getBuffer());
      println("" + jsGetError());
   }


   //TODO CHECK UNSIGNED SHORT
   private native void jsDrawElements(final int mode,
                                      final int count,
                                      final JavaScriptObject array) /*-{
		var that = this;

		var ELEMENT_ARRAY_BUFFER = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ELEMENT_ARRAY_BUFFER;
		var STATIC_DRAW = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.STATIC_DRAW;
		var UNSIGNED_SHORT = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNSIGNED_SHORT;

		var buffer = that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.createBuffer();
		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bindBuffer(
				ELEMENT_ARRAY_BUFFER, buffer);
		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bufferData(
				ELEMENT_ARRAY_BUFFER, array, STATIC_DRAW);
		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.drawElements(mode,
				count, UNSIGNED_SHORT, 0);
   }-*/;


   @Override
   public void lineWidth(final float width) {
      jsLineWidth(width);
      println("" + jsGetError());
   }


   private native void jsLineWidth(final float width) /*-{
		that.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.lineWidth(width);
   }-*/;


   @Override
   public int getError() {
      return jsGetError();
   }


   private native int jsGetError() /*-{
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

		return Number(e);
   }-*/;


   @Override
   public void blendFunc(final int sfactor,
                         final int dfactor) {
      jsBlendFunc(sfactor, dfactor);
      println("" + jsGetError());
   }


   private native void jsBlendFunc(final int sfactor,
                                   final int dfactor) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.blendFunc(sfactor,
				dfactor);
   }-*/;


   @Override
   public void bindTexture(final int target,
                           final IGLTextureId texture) {
      final JavaScriptObject id = ((GLTextureId_WebGL) texture).getWebGLTexture();
      jsBindTexture(target, id);
      println("" + jsGetError());
   }


   private native void jsBindTexture(final int target,
                                     final JavaScriptObject id) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.bindTexture(target,
				id);
   }-*/;


   @Override
   public void deleteTextures(final int n,
                              final IGLTextureId[] textures) {
      String error = "";
      for (int i = 0; i < n; i++) {
         final JavaScriptObject id = ((GLTextureId_WebGL) textures[i]).getWebGLTexture();
         jsDeleteTexture(id);
         error += jsGetError();
      }
      println(error);
   }


   private native void jsDeleteTexture(final JavaScriptObject id) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.deleteTexture(id);
   }-*/;


   @Override
   public void enableVertexAttribArray(final int location) {
      jsEnableVertexAttribArray(location);
      println("" + jsGetError());
   }


   private native void jsEnableVertexAttribArray(final int location) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.enableVertexAttribArray(location);
   }-*/;


   @Override
   public void disableVertexAttribArray(final int location) {
      jsDisableVertexAttribArray(location);
      println("" + jsGetError());
   }


   private native void jsDisableVertexAttribArray(final int location) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.disableVertexAttribArray(location);
   }-*/;


   @Override
   public void pixelStorei(final int pname,
                           final int param) {
      jsPixelStorei(pname, param);
      println("" + jsGetError());
   }


   private native void jsPixelStorei(final int pname,
                                     final int param) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.pixelStorei(pname,
				param);
   }-*/;


   @Override
   public ArrayList<IGLTextureId> genTextures(final int n) {
      final ArrayList<IGLTextureId> array = new ArrayList<IGLTextureId>();

      String error = "";
      for (int i = 0; i < n; i++) {
         final JavaScriptObject id = jsCreateTexture(); //WebGLTextureID
         error += jsGetError();
         array.add(new GLTextureId_WebGL(id));
      }
      println(error);
      return array;
   }


   private native JavaScriptObject jsCreateTexture() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.createTexture();
   }-*/;


   @Override
   public void texParameteri(final int target,
                             final int par,
                             final int v) {
      jsTextParameteri(target, par, v);
      println("" + jsGetError());
   }


   private native void jsTextParameteri(final int target,
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
      println("" + jsGetError());
   }


   private native void jsTexImage2D(final JavaScriptObject image,
                                    final int format) /*-{
		var TEXTURE_2D = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_2D;
		var UNSIGNED_BYTE = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNSIGNED_BYTE;

		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.texImage2D(
				TEXTURE_2D, 0, format, format, UNSIGNED_BYTE, image);
   }-*/;


   @Override
   public void generateMipmap(final int target) {
      jsGenerateMipmap(target);
      println("" + jsGetError());
   }


   private native void jsGenerateMipmap(final int target) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl
				.generateMipmap(target);
   }-*/;


   @Override
   public void drawArrays(final int mode,
                          final int first,
                          final int count) {
      jsDrawArrays(mode, first, count);
      println("" + jsGetError());
   }


   private native void jsDrawArrays(final int mode,
                                    final int first,
                                    final int count) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.drawArrays(mode,
				first, count);
   }-*/;


   @Override
   public void cullFace(final int c) {
      jsCullFace(c);
      println("" + jsGetError());
   }


   private native void jsCullFace(final int c) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.cullFace(c);
   }-*/;


   @Override
   public void getIntegerv(final int v,
                           final int[] i) {
      // TODO Auto-generated method stub

      //NO implemented in webgl???
      //      throw new RuntimeException("NativeGL_WebGL::getIntegerv IS NOT IMPLEMENTED");
      jsGetIntegerv(v, i);
      println("" + jsGetError());
   }


   private native void jsGetIntegerv(final int v,
                                     final int[] i) /*-{
		this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.getParameter(v, i);
   }-*/;


   @Override
   public int CullFace_Front() {
      final int result = jsCullFace_Front();
      println("" + jsGetError());
      return result;
   }


   private native int jsCullFace_Front() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FRONT;
   }-*/;


   @Override
   public int CullFace_Back() {
      final int result = jsCullFace_Back();
      println("" + jsGetError());
      return result;
   }


   private native int jsCullFace_Back() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.BACK;
   }-*/;


   @Override
   public int CullFace_FrontAndBack() {
      final int result = jsCullFace_FrontAndBack();
      println("" + jsGetError());
      return result;
   }


   private native int jsCullFace_FrontAndBack() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FRONT_AND_BACK;
   }-*/;


   @Override
   public int BufferType_ColorBuffer() {
      final int result = jsBufferType_ColorBuffer();
      println("" + jsGetError());
      return result;
   }


   private native int jsBufferType_ColorBuffer() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.COLOR_BUFFER_BIT;
   }-*/;


   @Override
   public int BufferType_DepthBuffer() {
      final int result = jsBufferType_DepthBuffer();
      println("" + jsGetError());
      return result;
   }


   private native int jsBufferType_DepthBuffer() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.DEPTH_BUFFER_BIT;
   }-*/;


   @Override
   public int Feature_PolygonOffsetFill() {
      final int result = jsFeature_PolygonOffsetFill();
      println("" + jsGetError());
      return result;
   }


   private native int jsFeature_PolygonOffsetFill() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.POLYGON_OFFSET_FILL;
   }-*/;


   @Override
   public int Feature_DepthTest() {
      //      return jsFeature_DepthTest();
      final int result = jsFeature_DepthTest();
      println("" + jsGetError());
      return result;
   }


   private native int jsFeature_DepthTest() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.DEPTH_TEST;
   }-*/;


   @Override
   public int Feature_Blend() {
      //      return jsFeature_Blend();
      final int result = jsFeature_Blend();
      println("" + jsGetError());
      return result;
   }


   private native int jsFeature_Blend() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.BLEND;
   }-*/;


   @Override
   public int Feature_CullFace() {
      //      return jsFeature_CullFace();
      final int result = jsFeature_CullFace();
      println("" + jsGetError());
      return result;
   }


   private native int jsFeature_CullFace() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.CULL_FACE;
   }-*/;


   @Override
   public int Type_Float() {
      //      return jsType_Float();
      final int result = jsType_Float();
      println("" + jsGetError());
      return result;
   }


   private native int jsType_Float() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.FLOAT;
   }-*/;


   @Override
   public int Type_UnsignedByte() {
      //      return jsType_UnsignedByte();
      final int result = jsType_UnsignedByte();
      println("" + jsGetError());
      return result;
   }


   private native int jsType_UnsignedByte()/*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNSIGNED_BYTE;
   }-*/;


   @Override
   public int Type_UnsignedInt() {
      //      return jsType_UnsignedInt();
      final int result = jsType_UnsignedInt();
      println("" + jsGetError());
      return result;
   }


   private native int jsType_UnsignedInt() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNSIGNED_INT;
   }-*/;


   @Override
   public int Type_Int() {
      //      return jsType_Int();
      final int result = jsType_Int();
      println("" + jsGetError());
      return result;
   }


   private native int jsType_Int() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.INT;
   }-*/;


   @Override
   public int Primitive_TriangleStrip() {
      //      return jsPrimitive_TriangleStrip();
      final int result = jsPrimitive_TriangleStrip();
      println("" + jsGetError());
      return result;
   }


   private native int jsPrimitive_TriangleStrip() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TRIANGLE_STRIP;
   }-*/;


   @Override
   public int Primitive_Lines() {
      //      return jsPrimitive_Lines();
      final int result = jsPrimitive_Lines();
      println("" + jsGetError());
      return result;
   }


   private native int jsPrimitive_Lines() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINES;
   }-*/;


   @Override
   public int Primitive_LineLoop() {
      //      return jsPrimitive_LineLoop();
      final int result = jsPrimitive_LineLoop();
      println("" + jsGetError());
      return result;
   }


   private native int jsPrimitive_LineLoop() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINE_LOOP;
   }-*/;


   @Override
   public int Primitive_Points() {
      //      return jsPrimitive_Points();
      final int result = jsPrimitive_Points();
      println("" + jsGetError());
      return result;
   }


   private native int jsPrimitive_Points() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.POINTS;
   }-*/;


   @Override
   public int BlendFactor_SrcAlpha() {
      //      return jsBlendFactor_SrcAlpha();
      final int result = jsBlendFactor_SrcAlpha();
      println("" + jsGetError());
      return result;
   }


   private native int jsBlendFactor_SrcAlpha() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.SRC_ALPHA;
   }-*/;


   @Override
   public int BlendFactor_OneMinusSrcAlpha() {
      //      return jsBlendFactor_OneMinusSrcAlpha();
      final int result = jsBlendFactor_OneMinusSrcAlpha();
      println("" + jsGetError());
      return result;
   }


   private native int jsBlendFactor_OneMinusSrcAlpha() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.ONE_MINUS_SRC_ALPHA;
   }-*/;


   @Override
   public int TextureType_Texture2D() {
      //      return jsTextureType_Texture2D();
      final int result = jsTextureType_Texture2D();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureType_Texture2D() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_2D;
   }-*/;


   @Override
   public int TextureParameter_MinFilter() {
      //      return jsTextureParameter_MinFilter();
      final int result = jsTextureParameter_MinFilter();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureParameter_MinFilter() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_MIN_FILTER;
   }-*/;


   @Override
   public int TextureParameter_MagFilter() {
      //      return jsTextureParameter_MagFilter();
      final int result = jsTextureParameter_MagFilter();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureParameter_MagFilter() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_MAG_FILTER;
   }-*/;


   @Override
   public int TextureParameter_WrapS() {
      //      return jsTextureParameter_WrapS();
      final int result = jsTextureParameter_WrapS();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureParameter_WrapS() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_WRAP_S;
   }-*/;


   @Override
   public int TextureParameter_WrapT() {
      //      return jsTextureParameter_WrapT();
      final int result = jsTextureParameter_WrapT();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureParameter_WrapT() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.TEXTURE_WRAP_T;
   }-*/;


   @Override
   public int TextureParameterValue_Linear() {
      //      return jsTextureParameterValue_Linear();
      final int result = jsTextureParameterValue_Linear();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureParameterValue_Linear() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.LINEAR;
   }-*/;


   @Override
   public int TextureParameterValue_ClampToEdge() {
      final int result = jsTextureParameterValue_ClampToEdge();
      println("" + jsGetError());
      return result;
   }


   private native int jsTextureParameterValue_ClampToEdge() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.CLAMP_TO_EDGE;
   }-*/;


   @Override
   public int Alignment_Pack() {
      //      return jsAlignment_Pack();
      final int result = jsAlignment_Pack();
      println("" + jsGetError());
      return result;
   }


   private native int jsAlignment_Pack() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.PACK_ALIGNMENT;
   }-*/;


   @Override
   public int Alignment_Unpack() {
      //      return jsAlignment_Unpack();
      final int result = jsAlignment_Unpack();
      println("" + jsGetError());
      return result;
   }


   private native int jsAlignment_Unpack()/*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.UNPACK_ALIGNMENT;
   }-*/;


   @Override
   public int Format_RGBA() {
      //      return jsFormat_RGBA();
      final int result = jsFormat_RGBA();
      println("" + jsGetError());
      return result;
   }


   private native int jsFormat_RGBA() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.RGBA;
   }-*/;


   @Override
   public int Variable_Viewport() {
      //      return jsVariable_Viewport();
      final int result = jsVariable_Viewport();
      println("" + jsGetError());
      return result;
   }


   private native int jsVariable_Viewport() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.VIEWPORT;
   }-*/;


   @Override
   public int Error_NoError() {
      //      return jsError_NoError();
      final int result = jsError_NoError();
      println("" + jsGetError());
      return result;
   }


   private native int jsError_NoError() /*-{
		return this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl.NO_ERROR;
   }-*/;


   private void println(final String msg) {
      //      System.out.println(msg);
   }

}
