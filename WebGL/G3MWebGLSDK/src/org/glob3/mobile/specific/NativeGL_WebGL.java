

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
import org.glob3.mobile.generated.GLTextureId;
import org.glob3.mobile.generated.GLTextureParameter;
import org.glob3.mobile.generated.GLTextureParameterValue;
import org.glob3.mobile.generated.GLTextureType;
import org.glob3.mobile.generated.GLVariable;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.INativeGL;

import com.google.gwt.core.client.JavaScriptObject;


public class NativeGL_WebGL
         extends
            INativeGL {

   /////////////////////////////


   JavaScriptObject _gl; //WebGL context


   public NativeGL_WebGL(final JavaScriptObject canvas) {
      _gl = jsCreateContext(canvas);
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


   @Override
   public int getAttribLocation(final int program,
                                final String name) {

      return jsGetAttribLocation(_gl, program, name);

   }


   private native int jsGetAttribLocation(JavaScriptObject gl,
                                          final int program,
                                          final String name)/*- {
                                                            return gl.getAttribLocation(program, name);
                                                            }-*/;


   @Override
   public int getUniformLocation(final int program,
                                 final String name) {
      return jsGetUniformLocation(_gl, program, name);
   }


   private native int jsGetUniformLocation(JavaScriptObject gl,
                                           final int program,
                                           final String name) /*-{
		return gl.getUniformLocation(program, name);
   }-*/;


   @Override
   public void uniform2f(final int loc,
                         final float x,
                         final float y) {
      jsUniform2f(_gl, loc, x, y);
   }


   private native void jsUniform2f(JavaScriptObject gl,
                                   final int loc,
                                   final float x,
                                   final float y) /*-{
		gl.uniform2f(loc, x, y);
   }-*/;


   @Override
   public void uniform1f(final int loc,
                         final float x) {
      jsUniform1f(_gl, loc, x);
   }


   private native void jsUniform1f(JavaScriptObject gl,
                                   final int loc,
                                   final float x) /*-{
		gl.uniform1f(loc, x);
   }-*/;


   @Override
   public void uniform1i(final int loc,
                         final int v) {
      jsUniform1i(_gl, loc, v);
   }


   private native void jsUniform1i(JavaScriptObject gl,
                                   final int loc,
                                   final float x) /*-{
		gl.uniform1i(loc, x);
   }-*/;


   @Override
   public void uniformMatrix4fv(final int location,
                                final int count,
                                final boolean transpose,
                                final float[] value) {
      jsUniformMatrix4fv(_gl, location, count, transpose, value);
   }

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


   @Override
   public void clear(final int nBuffer,
                     final GLBufferType[] buffers) {
      jsClear(_gl, 2, buffers);
   }


   private native void jsClear(JavaScriptObject gl,
                               final int nBuffer,
                               final GLBufferType[] buffers) /*-{
		gl.clear($wnd.gl.COLOR_BUFFER_BIT | $wnd.gl.DEPTH_BUFFER_BIT);
   }-*/;


   @Override
   public void uniform4f(final int location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      jsUniform4f(_gl, location, v0, v1, v2, v3);
   }


   private native void jsUniform4f(JavaScriptObject gl,
                                   final int location,
                                   final float v0,
                                   final float v1,
                                   final float v2,
                                   final float v3) /*-{
		gl.uniform4f(location, v0, v1, v2, v3);
   }-*/;


   @Override
   public void enable(final GLFeature feature) {
      //TODO
      //    jsEnable(_gl, jsGetEnum(_gl, feature));
   }


   private native void jsEnable(JavaScriptObject gl,
                                int feature) /*-{
		gl.enable(feature);
   }-*/;


   @Override
   public void disable(final GLFeature feature) {
      //TODO
      //    jsDisable(_gl, jsGetEnum(_gl, feature));
   }


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


   @Override
   public void blendFunc(final GLBlendFactor sfactor,
                         final GLBlendFactor dfactor) {
      //TODO
      //     jsBlendFunc(_gl, TODO_ignoring_count, TODO_ignoring_count)
   }


   private native void jsBlendFunc(JavaScriptObject gl,
                                   int sfactor,
                                   int dfactor) /*-{
		gl.blendFunc(sfactor, dfactor);
   }-*/;


   @Override
   public void bindTexture(final GLTextureType target,
                           final int texture) {
      // TODO this method must be implemented

   }


   @Override
   public void deleteTextures(final int n,
                              final int[] textures) {
      // TODO this method must be implemented

   }


   @Override
   public void enableVertexAttribArray(final int location) {
      // TODO this method must be implemented

   }


   @Override
   public void disableVertexAttribArray(final int location) {
      // TODO this method must be implemented

   }


   @Override
   public void pixelStorei(final GLAlignment pname,
                           final int param) {
      // TODO this method must be implemented

   }


   @Override
   public ArrayList<GLTextureId> genTextures(final int n) {
      // TODO this method must be implemented
      return null;
   }


   @Override
   public void texParameteri(final GLTextureType target,
                             final GLTextureParameter par,
                             final GLTextureParameterValue v) {
      // TODO this method must be implemented

   }


   @Override
   public void generateMipmap(final GLTextureType target) {
      // TODO this method must be implemented

   }


   @Override
   public void drawArrays(final GLPrimitive mode,
                          final int first,
                          final int count) {
      // TODO this method must be implemented

   }


   @Override
   public void cullFace(final GLCullFace c) {
      // TODO this method must be implemented

   }


   @Override
   public void getIntegerv(final GLVariable v,
                           final int[] i) {
      // TODO this method must be implemented

   }


   @Override
   public void useProgram(final int program) {
      // TODO this method must be implemented

   }


   @Override
   public void lineWidth(final float width) {
      // TODO this method must be implemented

   }


   @Override
   public GLError getError() {
      // TODO this method must be implemented
      return null;
   }


   @Override
   public void vertexAttribPointer(final int index,
                                   final int size,
                                   final boolean normalized,
                                   final int stride,
                                   final IFloatBuffer buffer) {
      // TODO Auto-generated method stub

   }


   @Override
   public void drawElements(final GLPrimitive mode,
                            final int count,
                            final IIntBuffer indices) {
      // TODO Auto-generated method stub

   }


   @Override
   public void texImage2D(final IImage image,
                          final GLFormat format) {
      // TODO Auto-generated method stub

   }


   //////////////////////////////////

   //   @Override
   //   public void enableVertices() {
   //      jsEnableVertices();
   //   }


   //   public native void jsEnableVertices() /*-{
   //		$wnd.gl.enableVertexAttribArray($wnd.shaderProgram.Position);
   //   }-*/;


   //   @Override
   //   public void enableTextures() {
   //      jsEnableTextures();
   //   }


   //   public static native void jsEnableTextures()/*-{
   //		$wnd.gl.enableVertexAttribArray($wnd.shaderProgram.textureCoord);
   //   }-*/;


   //   @Override
   //   public void enableTexture2D() {
   //      WebGL.jsEnableTexture2D();
   //   }


   //   @Override
   //   public void disableTexture2D() {
   //      WebGL.jsDisableTexture2D();
   //   }


   //   @Override
   //   public void disableVertices() {
   //      WebGL.jsDisableVertices();
   //   }


   //   @Override
   //   public void disableTextures() {
   //      WebGL.jsDisableTextures();
   //   }


   //   @Override
   //   public void clearScreen(final float r,
   //                           final float g,
   //                           final float b,
   //                           final float a) {
   //      WebGL.jsClearScreen(r, g, b);
   //   }


   //   @Override
   //   public void color(final float r,
   //                     final float g,
   //                     final float b,
   //                     final float a) {
   //      WebGL.jsColor(r, g, b);
   //   }


   //   @Override
   //   public void pushMatrix() {
   //      WebGL.jsPushMatrix();
   //   }


   //   @Override
   //   public void popMatrix() {
   //      WebGL.jsPopMatrix();
   //   }


   //   @Override
   //   public void loadMatrixf(final MutableMatrix44D m) {
   //      _modelView = m; // SAVING MODELVIEW
   //
   //      final JsArrayNumber arrayJs = (JsArrayNumber) JsArrayNumber.createArray();
   //      for (int i = 0; i < 16; i++) {
   //         arrayJs.push((float) m.get(i));
   //      }
   //      WebGL.jsLoadMatrixf(arrayJs);
   //   }


   //   @Override
   //   public void multMatrixf(final MutableMatrix44D m) {
   //	   final JsArrayNumber arrayJs = (JsArrayNumber) JsArrayNumber.createArray();
   //	      for (int i = 0; i < 16; i++) {
   //	         arrayJs.push((float) m.get(i));
   //	      }
   //      WebGL.jsMultiplyModelViewMatrix(arrayJs);
   //   }


   //   @Override
   //   public void vertexPointer(final int size,
   //                             final int stride,
   //                             final float[] vertex) {
   //      final JsArrayNumber jsArray = (JsArrayNumber) JsArrayNumber.createArray();
   //      for (final float element : vertex) {
   //         jsArray.push(element);
   //      }
   //      WebGL.jsVertexPointer(size, stride, jsArray);
   //   }


   //   @Override
   //   public void drawTriangleStrip(final int n,
   //                                 final byte[] i) {
   //      _numIndex = n;
   //      _index = new int[i.length];
   //      for (int j = 0; j < i.length; j++) {
   //         _index[j] = i[j];
   //      }
   //      final JsArrayInteger jsNumberArray = (JsArrayInteger) JsArrayInteger.createArray();
   //      for (int j = 0; j < _index.length; j++) {
   //         jsNumberArray.set(j, _index[j]);
   //      }
   //      WebGL.jsDrawIndexedMesh(_numIndex, jsNumberArray);
   //   }


   //   @Override
   //   public void drawLines(final int n,
   //                         final byte[] i) {
   //      _numIndex = n;
   //      _index = new int[i.length];
   //      for (int j = 0; j < i.length; j++) {
   //         _index[j] = i[j];
   //      }
   //      final JsArrayInteger jsNumberArray = (JsArrayInteger) JsArrayInteger.createArray();
   //      for (int j = 0; j < _index.length; j++) {
   //         jsNumberArray.set(j, _index[j]);
   //      }
   //      WebGL.jsDrawLines(_numIndex, jsNumberArray);
   //   }


   //   @Override
   //   public void drawLineLoop(final int n,
   //                            final byte[] i) {
   //      _numIndex = n;
   //      _index = new int[i.length];
   //      for (int j = 0; j < i.length; j++) {
   //         _index[j] = i[j];
   //      }
   //      final JsArrayInteger jsNumberArray = (JsArrayInteger) JsArrayInteger.createArray();
   //      for (int j = 0; j < _index.length; j++) {
   //         jsNumberArray.set(j, _index[j]);
   //      }
   //      WebGL.jsDrawLineLoop(_numIndex, jsNumberArray);
   //   }


   //   @Override
   //   public void setProjection(final MutableMatrix44D projection) {
   //      // Conversion a un array JavaScript
   //      final JsArrayNumber arrayJs = (JsArrayNumber) JsArrayNumber.createArray();
   //      for (int i = 0; i < 16; i++) {
   //         arrayJs.push(projection.get(i));
   //      }
   //
   //      WebGL.jsSetProjection(arrayJs);
   //   }


   //   @Override
   //   public void useProgram(final int program) {
   //      WebGL.jsUseProgram();
   //   }


   //   @Override
   //   public void enablePolygonOffset(final float factor,
   //                                   final float units) {
   //      WebGL.jsEnablePolygonOffset(factor, units);
   //   }


   //   @Override
   //   public void disablePolygonOffset() {
   //      WebGL.jsDisablePolygonOffset();
   //   }


   //   @Override
   //   public void lineWidth(final float width) {
   //      WebGL.jsLineWidth(width);
   //
   //   }


   //   @Override
   //   public int uploadTexture(final IImage image,
   //                            final int textureWidth,
   //                            final int textureHeight) {
   //      //		final JsArray<JavaScriptObject> jsTextures = (JsArray<JavaScriptObject>) JsArray
   //      //				.createArray();
   //      //		final Image_WebGL image = (Image_WebGL) image;
   //      //		jsTextures.set(0, image.imgObject);
   //      //
   //      //		// Boolean parameter indicates whether its a billboard (true) or not
   //      //		if (false) {
   //      //			return WebGL.jsUploadBillboardTexture(jsTextures);
   //      //		}
   //      //
   //      //		// Regular texture uploading:
   //      //		return WebGL.jsUploadMultipleTextures(jsTextures);
   //      return 0;
   //   }


   //   @Override
   //   public void setTextureCoordinates(final int size,
   //                                     final int stride,
   //                                     final float[] texcoord) {
   //      final JsArrayNumber jsArray = (JsArrayNumber) JsArrayNumber.createArray();
   //      for (final float element : texcoord) {
   //         jsArray.push(element);
   //      }
   //      WebGL.jsTexCoordPointer(size, stride, jsArray);
   //   }


   //   @Override
   //   public void bindTexture(final int n) {
   //      WebGL.jsBindTexture(n);
   //   }


   //   @Override
   //   public void drawBillBoard(final int textureId,
   //                             final float x,
   //                             final float y,
   //                             final float z,
   //                             final float viewPortRatio) {
   //      WebGL.jsDrawBillBoard(textureId, x, y, z, viewPortRatio); // No rotation
   //   }


   //   @Override
   //   public void deleteTexture(final int glTextureId) {
   //      WebGL.jsDeleteTextures(glTextureId - 1);
   //   }

}
