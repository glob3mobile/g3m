

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


public class NativeGL_WebGL
         extends
            INativeGL {

   // stack of ModelView matrices
   //   MutableMatrix44D                     _modelView = new MutableMatrix44D();
   //   int                                  _numIndex  = 0;
   //   int                                  _index[];
   //
   //   public static List<JavaScriptObject> _programList;


   //   public NativeGL_WebGL() {
   //      webGLStart();
   //   }


   //   // WebGL loading function
   //   private void webGLStart() {
   //      final JavaScriptObject canvas = DOM.getElementById(G3MWidget_WebGL.canvasId);
   //      WebGL.jsInitGL(canvas);
   //      WebGL.jsLoadWebGlUtils();
   //      WebGL.jsLoadGlMatrix();
   //      WebGL.loadGlobalVars();
   //      CreateProgram();
   //      WebGL.jsEnable3D();
   //      WebGL.jsUseProgram(); //USING SHADING PROGRAM
   //      WebGL.jsIdentity();
   //   }


   //   // WebGL program creation function
   //   private int CreateProgram() {
   //      final JavaScriptObject newWebGLProgram = WebGL.jsCreateNewProgram("shader-fs", "shader-vs");
   //      if (newWebGLProgram == null) {
   //         return 0;
   //      }
   //
   //      return 1;
   //   }


   @Override
   public native int getAttribLocation(final int program,
                                       final String name)/*-{
		return $wnd.gl.getAttribLocation(program, name);
   }-*/;


   @Override
   public native int getUniformLocation(final int program,
                                        final String name) /*-{
		return $wnd.gl.getUniformLocation(program, name);
   }-*/;


   @Override
   public native void uniform2f(final int loc,
                                final float x,
                                final float y) /*-{
		$wnd.gl.uniform2f(loc, x, y);
   }-*/;


   @Override
   public native void uniform1f(final int loc,
                                final float x)/*-{
		$wnd.gl.uniform1f(loc, x);
   }-*/;


   @Override
   public native void uniform1i(final int loc,
                                final int v) /*-{
		$wnd.gl.uniform1i(loc, v);
   }-*/;


   int TODO_ignoring_count;


   @Override
   public native void uniformMatrix4fv(final int location,
                                       final int count,
                                       final boolean transpose,
                                       final float[] value) /*-{
		$wnd.gl.uniformMatrix4fv(location, transpose, matrix);
   }-*/;


   @Override
   public native void clearColor(final float red,
                                 final float green,
                                 final float blue,
                                 final float alpha)/*-{
		$wnd.gl.clearColor(red, green, blue, alpha);
   }-*/;


   @Override
   public native void clear(final int nBuffer,
                            final GLBufferType[] buffers) /*-{
		$wnd.gl.clear($wnd.gl.COLOR_BUFFER_BIT | $wnd.gl.DEPTH_BUFFER_BIT);
   }-*/;


   @Override
   public void uniform4f(final int location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      // TODO this method must be implemented

   }


   @Override
   public void enable(final GLFeature feature) {
      // TODO this method must be implemented

   }


   @Override
   public void disable(final GLFeature feature) {
      // TODO this method must be implemented

   }


   @Override
   public void polygonOffset(final float factor,
                             final float units) {
      // TODO this method must be implemented

   }


   @Override
   public void blendFunc(final GLBlendFactor sfactor,
                         final GLBlendFactor dfactor) {
      // TODO this method must be implemented

   }


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
