

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.GLBufferType;
import org.glob3.mobile.generated.GLTextureId;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLProgramId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.INativeGL;

import com.google.gwt.core.client.JavaScriptObject;


public class NativeGL_WebGL
         extends
            INativeGL {

   /////////////////////////////


   JavaScriptObject _gl; //WebGL context


   public NativeGL_WebGL(final JavaScriptObject webGLContext) {
      _gl = webGLContext;
   }


   public static native JavaScriptObject jsCreateContext(JavaScriptObject canvas) /*-{
		gl = canvas.getContext("experimental-webgl");
		gl.viewportWidth = canvas.width;
		gl.viewportHeight = canvas.height;
		if (!gl) {
			alert("Could not initialise WebGL");
		}
		return gl;
   }-*/;

   int TODO_ignoring_count;


   private native void jsUniformMatrix4fv(JavaScriptObject gl,
                                          final int location,
                                          final int count,
                                          final boolean transpose,
                                          final float[] value) /*-{
		gl.uniformMatrix4fv(location, transpose, matrix);
   }-*/;


   @Override
   public void clearColor(final float red,
                          final float green,
                          final float blue,
                          final float alpha) {
      jsClearColor(_gl, red, green, blue, alpha);
   }


   private native void jsClearColor(JavaScriptObject gl,
                                    final float red,
                                    final float green,
                                    final float blue,
                                    final float alpha)/*-{
		gl.clearColor(red, green, blue, alpha);
   }-*/;


   private native void jsClear(JavaScriptObject gl,
                               final int nBuffer,
                               final GLBufferType[] buffers) /*-{
		gl.clear($wnd.gl.COLOR_BUFFER_BIT | $wnd.gl.DEPTH_BUFFER_BIT);
   }-*/;


   private native void jsUniform4f(JavaScriptObject gl,
                                   final int location,
                                   final float v0,
                                   final float v1,
                                   final float v2,
                                   final float v3) /*-{
		gl.uniform4f(location, v0, v1, v2, v3);
   }-*/;


   private native void jsEnable(JavaScriptObject gl,
                                int feature) /*-{
		gl.enable(feature);
   }-*/;


   private native void jsDisable(JavaScriptObject gl,
                                 int feature) /*-{
		gl.disable(feature);
   }-*/;


   @Override
   public void polygonOffset(final float factor,
                             final float units) {
      jsPolygonOffset(_gl, factor, units);
   }


   private native void jsPolygonOffset(JavaScriptObject gl,
                                       final float factor,
                                       final float units) /*-{
		gl.polygonOffset(factor, units);
   }-*/;


   private native void jsBlendFunc(JavaScriptObject gl,
                                   int sfactor,
                                   int dfactor) /*-{
		gl.blendFunc(sfactor, dfactor);
   }-*/;


   @Override
   public void deleteTextures(final int n,
                              final int[] textures) {
      // TODO this method must be implemented

   }


   @Override
   public native void enableVertexAttribArray(final int location) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.enableVertexAttribArray(location);
   }-*/;


   @Override
   public native void disableVertexAttribArray(final int location) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.disableVertexAttribArray(location);
   }-*/;


   @Override
   public ArrayList<GLTextureId> genTextures(final int n) {
      // TODO this method must be implemented
      return null;
   }


   @Override
   public native void lineWidth(final float width) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.lineWidth(width);
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
   }


   private native void jsVertexAttribPointer(final int index,
                                             final int size,
                                             final boolean normalized,
                                             final int stride,
                                             final JavaScriptObject array) /*-{

		debugger;
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		var buffer = gl.createBuffer();
		gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
		gl.bufferData(gl.ARRAY_BUFFER, array, gl.STATIC_DRAW);

		gl.vertexAttribPointer(index, size, gl.FLOAT, normalized, stride, 0);

   }-*/;


   @Override
   public native void clear(final int buffers) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.clear(buffers);
   }-*/;


   @Override
   public native void enable(final int feature)/*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.enable(feature);
   }-*/;


   @Override
   public native void disable(final int feature) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.disable(feature);
   }-*/;


   @Override
   public void drawElements(final int mode,
                            final int count,
                            final IIntBuffer indices) {
      //TODO CHECK
      jsDrawElements(mode, count, ((IntBuffer_WebGL) indices).getBuffer());
   }


   private native void jsDrawElements(final int mode,
                                      final int count,
                                      final JavaScriptObject array) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;

		var buffer = gl.createBuffer();
		gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, buffer);
		gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, array, gl.STATIC_DRAW);

		gl.drawElements(mode, count, gl.INT, 0);
   }-*/;


   @Override
   public native void blendFunc(final int sfactor,
                                final int dfactor)/*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.blendFunc(sfactor, dfactor);
   }-*/;


   @Override
   public void bindTexture(final int target,
                           final int texture) {
      // TODO Auto-generated method stub

   }


   @Override
   public native void pixelStorei(final int pname,
                                  final int param) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.pixelStorei(pname, param);
   }-*/;


   @Override
   public native void texParameteri(final int target,
                                    final int par,
                                    final int v) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.texParameteri(target, par, v);
   }-*/;


   @Override
   public void texImage2D(final IImage image,
                          final int format) {
      // TODO Auto-generated method stub

   }


   @Override
   public native void generateMipmap(final int target) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.generateMipmap(target);
   }-*/;


   @Override
   public native void drawArrays(final int mode,
                                 final int first,
                                 final int count) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.drawArrays(mode, first, count);
   }-*/;


   @Override
   public native void cullFace(final int c)/*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.cullFace(c);
   }-*/;


   @Override
   public void getIntegerv(final int v,
                           final int[] i) {
      // TODO Auto-generated method stub

      //NO implemented in webgl???

   }


   private native void jsGetIntegerv(final int v,
                                     final int[] i) /*-{
      // TODO Auto-generated method stub
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
   public native int Type_UnsignedByte()/*-{
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
   public native int Alignment_Unpack()/*-{
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
   public native int getError() /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.getError();
   }-*/;


   @Override
   public void useProgram(final IGLProgramId program) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      jsUseProgram(p);
   }


   public native void jsUseProgram(JavaScriptObject program) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.useProgram(program);
   }-*/;


   @Override
   public int getAttribLocation(final IGLProgramId program,
                                final String name) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      return jsGetAttribLocation(p, name);
   }


   public native int jsGetAttribLocation(final JavaScriptObject program,
                                         final String name) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		return gl.getAttribLocation(program, name);
   }-*/;


   @Override
   public void uniform2f(final IGLUniformID loc,
                         final float x,
                         final float y) {
      final JavaScriptObject l = ((GLUniformID_WebGL) loc).getId();
      jsUniform2f(l, x, y);
   }


   public native void jsUniform2f(final JavaScriptObject loc,
                                  final float x,
                                  final float y) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.uniform2f(loc, x, y);
   }-*/;


   @Override
   public void uniform1f(final IGLUniformID loc,
                         final float x) {
      final JavaScriptObject l = ((GLUniformID_WebGL) loc).getId();
      jsUniform1f(l, x);
   }


   public native void jsUniform1f(final JavaScriptObject loc,
                                  final float x) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.uniform1f(loc, x);
   }-*/;


   @Override
   public void uniform1i(final IGLUniformID loc,
                         final int v) {
      final JavaScriptObject l = ((GLUniformID_WebGL) loc).getId();
      jsUniform1f(l, v);
   }


   public native void jsUniform1i(final JavaScriptObject loc,
                                  final int x) /*-{
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.uniform1i(loc, x);
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

   }


   public native void jsUniformMatrix4fv(final JavaScriptObject loc,
                                         final int count,
                                         final boolean transpose,
                                         final JavaScriptObject value) /*-{
		//TODO IGNORING COUNT
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.uniformMatrix4fv(loc, transpose, value);
   }-*/;


   @Override
   public void uniform4f(final IGLUniformID location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      final JavaScriptObject l = ((GLUniformID_WebGL) location).getId();
      jsUniform4f(l, v0, v1, v2, v3);
   }


   private native void jsUniform4f(final JavaScriptObject loc,
                                   final float v0,
                                   final float v1,
                                   final float v2,
                                   final float v3) /*-{
		//TODO IGNORING COUNT
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		gl.uniform4f(loc, v0, v1, v2, v3);
   }-*/;


   @Override
   public IGLUniformID getUniformLocation(final IGLProgramId program,
                                          final String name) {
      final JavaScriptObject p = ((GLProgramId_WebGL) program).getProgram();
      final JavaScriptObject u = jsGetUniformLocation(p, name);
      return new GLUniformID_WebGL(u);
   }


   private native JavaScriptObject jsGetUniformLocation(final JavaScriptObject program,
                                                        final String name)
   /*-{
		//TODO IGNORING COUNT
		var gl = this.@org.glob3.mobile.specific.NativeGL_WebGL::_gl;
		return gl.getUniformLocation(program, name);
   }-*/;
}
