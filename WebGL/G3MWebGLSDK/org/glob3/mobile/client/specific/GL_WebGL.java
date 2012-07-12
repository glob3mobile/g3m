

package org.glob3.mobile.client.specific;

import java.util.List;

import org.glob3.mobile.client.generated.IGL;
import org.glob3.mobile.client.generated.IImage;
import org.glob3.mobile.client.generated.MutableMatrix44D;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.user.client.DOM;


public class GL_WebGL
         extends
            IGL {

   // stack of ModelView matrices
   MutableMatrix44D                     _modelView = new MutableMatrix44D();
   int                                  _numIndex  = 0;
   int                                  _index[];

   public static List<JavaScriptObject> _programList;


   public GL_WebGL() {
      webGLStart();
   }


   // WebGL loading function
   private void webGLStart() {
      final JavaScriptObject canvas = DOM.getElementById(G3MWidget_WebGL.canvasId);
      WebGL.jsInitGL(canvas);
      WebGL.jsLoadWebGlUtils();
      WebGL.jsLoadGlMatrix();
      WebGL.loadGlobalVars();
      CreateProgram();
      WebGL.jsEnable3D();

      WebGL.jsIdentity();
   }


   // WebGL program creation function
   private int CreateProgram() {
      final JavaScriptObject newWebGLProgram = WebGL.jsCreateNewProgram("shader-fs", "shader-vs");
      if (newWebGLProgram == null) {
         return 0;
      }

      return 1;
   }


   @Override
   public void enableVertices() {
      jsEnableVertices();
   }


   public native void jsEnableVertices() /*-{
		$wnd.gl.enableVertexAttribArray($wnd.shaderProgram.Position);
   }-*/;


   @Override
   public void enableTextures() {
      jsEnableTextures();
   }


   public static native void jsEnableTextures()/*-{
		$wnd.gl.enableVertexAttribArray($wnd.shaderProgram.textureCoord);
   }-*/;


   @Override
   public void enableTexture2D() {
      WebGL.jsEnableTexture2D();
   }


   @Override
   public void disableTexture2D() {
      WebGL.jsDisableTexture2D();
   }


   @Override
   public void disableVertices() {
      WebGL.jsDisableVertices();
   }


   @Override
   public void disableTextures() {
      WebGL.jsDisableTextures();
   }


   @Override
   public void clearScreen(final float r,
                           final float g,
                           final float b,
                           final float a) {
      WebGL.jsClearScreen(r, g, b);
   }


   @Override
   public void color(final float r,
                     final float g,
                     final float b,
                     final float a) {
      WebGL.jsColor(r, g, b);
   }


   @Override
   public void pushMatrix() {
      WebGL.jsPushMatrix();
   }


   @Override
   public void popMatrix() {
      WebGL.jsPopMatrix();
   }


   @Override
   public void loadMatrixf(final MutableMatrix44D m) {

      _modelView = m; // SAVING MODELVIEW

      final JsArrayNumber arrayJs = (JsArrayNumber) JsArrayNumber.createArray();
      for (int i = 0; i < 16; i++) {
         arrayJs.push((float) m.get(i));
      }
      WebGL.jsLoadMatrixf(arrayJs);
   }


   @Override
   public void multMatrixf(final MutableMatrix44D m) {
      final MutableMatrix44D product = _modelView.multMatrix(m);

      _modelView = new MutableMatrix44D(product);

      final JsArrayNumber arrayJs = (JsArrayNumber) JsArrayNumber.createArray();
      for (int i = 0; i < 16; i++) {
         arrayJs.push((float) _modelView.get(i));
      }
      WebGL.jsLoadMatrixf(arrayJs);
   }


   @Override
   public void vertexPointer(final int size,
                             final int stride,
                             final float[] vertex) {
      final JsArrayNumber jsArray = (JsArrayNumber) JsArrayNumber.createArray();
      for (final float element : vertex) {
         jsArray.push(element);
      }
      WebGL.jsVertexPointer(size, stride, jsArray);
   }


   @Override
   public void drawTriangleStrip(final int n,
                                 final byte[] i) {
      _numIndex = n;
      _index = new int[i.length];
      for (int j = 0; j < i.length; j++) {
         _index[j] = i[j];
      }
      final JsArrayInteger jsNumberArray = (JsArrayInteger) JsArrayInteger.createArray();
      for (int j = 0; j < _index.length; j++) {
         jsNumberArray.set(j, _index[j]);
      }
      WebGL.jsDrawIndexedMesh(_numIndex, jsNumberArray);
   }


   @Override
   public void drawLines(final int n,
                         final byte[] i) {
      _numIndex = n;
      _index = new int[i.length];
      for (int j = 0; j < i.length; j++) {
         _index[j] = i[j];
      }
      final JsArrayInteger jsNumberArray = (JsArrayInteger) JsArrayInteger.createArray();
      for (int j = 0; j < _index.length; j++) {
         jsNumberArray.set(j, _index[j]);
      }
      WebGL.jsDrawLines(_numIndex, jsNumberArray);
   }


   @Override
   public void drawLineLoop(final int n,
                            final byte[] i) {
      _numIndex = n;
      _index = new int[i.length];
      for (int j = 0; j < i.length; j++) {
         _index[j] = i[j];
      }
      final JsArrayInteger jsNumberArray = (JsArrayInteger) JsArrayInteger.createArray();
      for (int j = 0; j < _index.length; j++) {
         jsNumberArray.set(j, _index[j]);
      }
      WebGL.jsDrawLineLoop(_numIndex, jsNumberArray);
   }


   @Override
   public void setProjection(final MutableMatrix44D projection) {
      // Conversion a un array JavaScript
      final JsArrayNumber arrayJs = (JsArrayNumber) JsArrayNumber.createArray();
      for (int i = 0; i < 16; i++) {
         arrayJs.push(projection.get(i));
      }

      WebGL.jsSetProjection(arrayJs);
   }


   @Override
   public void useProgram(final int program) {
      WebGL.jsUseProgram();
   }


   @Override
   public void enablePolygonOffset(final float factor,
                                   final float units) {
      WebGL.jsEnablePolygonOffset(factor, units);
   }


   @Override
   public void disablePolygonOffset() {
      WebGL.jsDisablePolygonOffset();
   }


   @Override
   public void lineWidth(final float width) {
      WebGL.jsLineWidth(width);

   }


   @Override
   public int getError() {
      // TODO Auto-generated method stub
      return 0;
   }


   @Override
   public int uploadTexture(final IImage image,
                            final int textureWidth,
                            final int textureHeight) {
      //		final JsArray<JavaScriptObject> jsTextures = (JsArray<JavaScriptObject>) JsArray
      //				.createArray();
      //		final Image_WebGL image = (Image_WebGL) image;
      //		jsTextures.set(0, image.imgObject);
      //
      //		// Boolean parameter indicates whether its a billboard (true) or not
      //		if (false) {
      //			return WebGL.jsUploadBillboardTexture(jsTextures);
      //		}
      //
      //		// Regular texture uploading:
      //		return WebGL.jsUploadMultipleTextures(jsTextures);
      return 0;
   }


   @Override
   public void setTextureCoordinates(final int size,
                                     final int stride,
                                     final float[] texcoord) {
      final JsArrayNumber jsArray = (JsArrayNumber) JsArrayNumber.createArray();
      for (final float element : texcoord) {
         jsArray.push(element);
      }
      WebGL.jsTexCoordPointer(size, stride, jsArray);
   }


   @Override
   public void bindTexture(final int n) {
      WebGL.jsBindTexture(n);
   }


   @Override
   public void depthTest(final boolean b) {
      // TODO Auto-generated method stub

   }


   @Override
   public void blend(final boolean b) {
      // TODO Auto-generated method stub

   }


   @Override
   public void drawBillBoard(final int textureId,
                             final float x,
                             final float y,
                             final float z,
                             final float viewPortRatio) {
      WebGL.jsDrawBillBoard(textureId, x, y, z, viewPortRatio); // No rotation
   }


   @Override
   public void deleteTexture(final int glTextureId) {
      WebGL.jsDeleteTextures(glTextureId - 1);
   }

}
