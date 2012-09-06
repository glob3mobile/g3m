

package org.glob3.mobile.specific;

import java.util.ArrayList;
import java.util.List;

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
import org.glob3.mobile.generated.GLType;
import org.glob3.mobile.generated.GLVariable;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.MutableMatrix44D;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;


public class GL_WebGL
         extends
            INativeGL {

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
      WebGL.jsUseProgram();  //USING SHADING PROGRAM
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
   public int getAttribLocation(int program,
                                String name) {
      // TODO this method must be implemented
      return 0;
   }


   @Override
   public int getUniformLocation(int program,
                                 String name) {
      // TODO this method must be implemented
      return 0;
   }


   @Override
   public void uniform2f(int loc,
                         float x,
                         float y) {
      // TODO this method must be implemented
      
   }


   @Override
   public void uniform1f(int loc,
                         float x) {
      // TODO this method must be implemented
      
   }


   @Override
   public void uniform1i(int loc,
                         int v) {
      // TODO this method must be implemented
      
   }


   @Override
   public void uniformMatrix4fv(int location,
                                int count,
                                boolean transpose,
                                float[] value) {
      // TODO this method must be implemented
      
   }


   @Override
   public void clearColor(float red,
                          float green,
                          float blue,
                          float alpha) {
      // TODO this method must be implemented
      
   }


   @Override
   public void clear(int nBuffer,
                     GLBufferType[] buffers) {
      // TODO this method must be implemented
      
   }


   @Override
   public void uniform4f(int location,
                         float v0,
                         float v1,
                         float v2,
                         float v3) {
      // TODO this method must be implemented
      
   }


   @Override
   public void enable(GLFeature feature) {
      // TODO this method must be implemented
      
   }


   @Override
   public void disable(GLFeature feature) {
      // TODO this method must be implemented
      
   }


   @Override
   public void polygonOffset(float factor,
                             float units) {
      // TODO this method must be implemented
      
   }


   @Override
   public void vertexAttribPointer(int index,
                                   int size,
                                   GLType type,
                                   boolean normalized,
                                   int stride,
                                   Object pointer) {
      // TODO this method must be implemented
      
   }


   @Override
   public void drawElements(GLPrimitive mode,
                            int count,
                            GLType type,
                            Object indices) {
      // TODO this method must be implemented
      
   }


   @Override
   public void blendFunc(GLBlendFactor sfactor,
                         GLBlendFactor dfactor) {
      // TODO this method must be implemented
      
   }


   @Override
   public void bindTexture(GLTextureType target,
                           int texture) {
      // TODO this method must be implemented
      
   }


   @Override
   public void deleteTextures(int n,
                              int[] textures) {
      // TODO this method must be implemented
      
   }


   @Override
   public void enableVertexAttribArray(int location) {
      // TODO this method must be implemented
      
   }


   @Override
   public void disableVertexAttribArray(int location) {
      // TODO this method must be implemented
      
   }


   @Override
   public void pixelStorei(GLAlignment pname,
                           int param) {
      // TODO this method must be implemented
      
   }


   @Override
   public ArrayList<GLTextureId> genTextures(int n) {
      // TODO this method must be implemented
      return null;
   }


   @Override
   public void texParameteri(GLTextureType target,
                             GLTextureParameter par,
                             GLTextureParameterValue v) {
      // TODO this method must be implemented
      
   }


   @Override
   public void texImage2D(GLTextureType target,
                          int level,
                          GLFormat internalFormat,
                          int width,
                          int height,
                          int border,
                          GLFormat format,
                          GLType type,
                          Object data) {
      // TODO this method must be implemented
      
   }


   @Override
   public void generateMipmap(GLTextureType target) {
      // TODO this method must be implemented
      
   }


   @Override
   public void drawArrays(GLPrimitive mode,
                          int first,
                          int count) {
      // TODO this method must be implemented
      
   }


   @Override
   public void cullFace(GLCullFace c) {
      // TODO this method must be implemented
      
   }


   @Override
   public void getIntegerv(GLVariable v,
                           int[] i) {
      // TODO this method must be implemented
      
   }


   @Override
   public void useProgram(int program) {
      // TODO this method must be implemented
      
   }


   @Override
   public void lineWidth(float width) {
      // TODO this method must be implemented
      
   }


   @Override
   public GLError getError() {
      // TODO this method must be implemented
      return null;
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
