

package org.glob3.mobile.specific;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLTextureId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IShortBuffer;
import org.glob3.mobile.generated.MutableMatrix44D;
import org.glob3.mobile.generated.ShaderProgram;
import org.glob3.mobile.generated.ShaderType;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;


public final class NativeGL2_Android
         extends
            INativeGL {

   private Thread _openglThread = null;


   void setOpenGLThread(final Thread openglThread) {
      _openglThread = openglThread;
   }


   private void checkOpenGLThread() {
      if (_openglThread != null) {
         final Thread currentThread = Thread.currentThread();
         if (currentThread != _openglThread) {
            throw new RuntimeException("OpenGL code executed from a Non-OpenGL thread.  (OpenGLThread=" + _openglThread
                                       + ", CurrentThread=" + currentThread + ")");
         }
      }
   }


   @Override
   public void uniform2f(final IGLUniformID loc,
                         final float x,
                         final float y) {
      checkOpenGLThread();
      GLES20.glUniform2f(((GLUniformID_Android) loc).getID(), x, y);
   }


   @Override
   public void uniform1f(final IGLUniformID loc,
                         final float x) {
      checkOpenGLThread();
      GLES20.glUniform1f(((GLUniformID_Android) loc).getID(), x);
   }


   @Override
   public void uniform1i(final IGLUniformID loc,
                         final int v) {
      checkOpenGLThread();
      GLES20.glUniform1i(((GLUniformID_Android) loc).getID(), v);
   }


   //   @Override
   //   public void uniformMatrix4fv(final IGLUniformID location,
   //                                final boolean transpose,
   //                                final IFloatBuffer buffer) {
   // checkOpenGLThread(); 
   //      GLES20.glUniformMatrix4fv(((GLUniformID_Android) location).getID(), 1, transpose,
   //               ((FloatBuffer_Android) buffer).getBuffer());
   //   }


   @Override
   public void uniformMatrix4fv(final IGLUniformID location,
                                final boolean transpose,
                                final MutableMatrix44D matrix) {
      checkOpenGLThread();
      GLES20.glUniformMatrix4fv( //
               ((GLUniformID_Android) location).getID(), //
               1, //
               transpose, //
               matrix.getColumnMajorFloatArray(), //
               0 //
      );
   }


   @Override
   public void clearColor(final float red,
                          final float green,
                          final float blue,
                          final float alpha) {
      checkOpenGLThread();
      GLES20.glClearColor(red, green, blue, alpha);
   }


   @Override
   public void clear(final int buffers) {
      checkOpenGLThread();
      GLES20.glClear(buffers);
   }


   @Override
   public void uniform4f(final IGLUniformID location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      checkOpenGLThread();
      GLES20.glUniform4f(((GLUniformID_Android) location).getID(), v0, v1, v2, v3);
   }


   @Override
   public void enable(final int feature) {
      checkOpenGLThread();
      GLES20.glEnable(feature);
   }


   @Override
   public void disable(final int feature) {
      checkOpenGLThread();
      GLES20.glDisable(feature);
   }


   @Override
   public void polygonOffset(final float factor,
                             final float units) {
      checkOpenGLThread();
      GLES20.glPolygonOffset(factor, units);
   }


   @Override
   public void lineWidth(final float width) {
      checkOpenGLThread();
      GLES20.glLineWidth(width);
   }


   @Override
   public int getError() {
      checkOpenGLThread();
      return GLES20.glGetError();
   }


   @Override
   public void blendFunc(final int sfactor,
                         final int dfactor) {
      checkOpenGLThread();
      GLES20.glBlendFunc(sfactor, dfactor);
   }


   @Override
   public void bindTexture(final int target,
                           final IGLTextureId texture) {
      checkOpenGLThread();
      GLES20.glBindTexture(target, ((GLTextureId_Android) texture).getGLTextureId());
   }


   @Override
   public boolean deleteTexture(final IGLTextureId texture) {
      checkOpenGLThread();
      GLES20.glDeleteTextures(1, new int[] { ((GLTextureId_Android) texture).getGLTextureId() }, 0);
      return false;
   }


   @Override
   public void enableVertexAttribArray(final int location) {
      checkOpenGLThread();
      GLES20.glEnableVertexAttribArray(location);
   }


   @Override
   public void disableVertexAttribArray(final int location) {
      checkOpenGLThread();
      GLES20.glDisableVertexAttribArray(location);
   }


   @Override
   public void pixelStorei(final int pname,
                           final int param) {
      checkOpenGLThread();
      GLES20.glPixelStorei(pname, param);
   }


   @Override
   public ArrayList<IGLTextureId> genTextures(final int count) {
      checkOpenGLThread();
      final ArrayList<IGLTextureId> result = new ArrayList<IGLTextureId>(count);
      final int[] textureIds = new int[count];
      GLES20.glGenTextures(count, textureIds, 0);
      for (int i = 0; i < count; i++) {
         final int textureId = textureIds[i];
         if (textureId == 0) {
            ILogger.instance().logError("Can't create a textureId");
         }
         else {
            result.add(new GLTextureId_Android(textureId));
         }
      }
      return result;
   }


   @Override
   public void texParameteri(final int target,
                             final int par,
                             final int v) {
      checkOpenGLThread();
      GLES20.glTexParameteri(target, par, v);
   }


   @Override
   public void drawArrays(final int mode,
                          final int first,
                          final int count) {
      checkOpenGLThread();
      GLES20.glDrawArrays(mode, first, count);
   }


   @Override
   public void cullFace(final int c) {
      checkOpenGLThread();
      GLES20.glCullFace(c);
   }


   @Override
   public void getIntegerv(final int v,
                           final int[] i) {
      checkOpenGLThread();
      GLES20.glGetIntegerv(v, i, 0);
   }


   @Override
   public void generateMipmap(final int target) {
      checkOpenGLThread();
      GLES20.glGenerateMipmap(target);
   }


   @Override
   public void vertexAttribPointer(final int index,
                                   final int size,
                                   final boolean normalized,
                                   final int stride,
                                   final IFloatBuffer buffer) {
      checkOpenGLThread();

      final FloatBuffer floatBuffer = ((FloatBuffer_Android) buffer).getBuffer();

      //      System.err.println("vertexAttribPointer(index=" + index + //
      //                         ", size=" + size + //
      //                         ", normalized=" + normalized + //
      //                         ", stride=" + stride + //
      //                         ", floatBuffer=" + floatBuffer + ")");

      GLES20.glVertexAttribPointer(index, size, GLES20.GL_FLOAT, normalized, stride, floatBuffer);


//            final FloatBuffer_Android bufferAndroid = (FloatBuffer_Android) buffer;
//            final int webGLBuffer = bufferAndroid.getGLBuffer();
//            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, webGLBuffer);
//      
//      
//            final FloatBuffer array = bufferAndroid.getBuffer();
//            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, size, array, GLES20.GL_STATIC_DRAW);
//      
//            GLES20.glVertexAttribPointer(index, size, GLES20.GL_FLOAT, normalized, stride, 0);
   }


   @Override
   public void drawElements(final int mode,
                            final int count,
                            final IShortBuffer indices) {
      checkOpenGLThread();

      final ShortBuffer indexBuffer = ((ShortBuffer_Android) indices).getBuffer();

      //      System.err.println("drawElements(mode=" + mode + //
      //                         ", count=" + count + //
      //                         ", indexBuffer=" + indexBuffer + ")");

      GLES20.glDrawElements(mode, count, GLES20.GL_UNSIGNED_SHORT, indexBuffer);


      //      final ShortBuffer_Android bufferAndroid = (ShortBuffer_Android) indices;
      //      final int webGLBuffer = bufferAndroid.getGLBuffer();
      //      GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, webGLBuffer);
      //
      //      final Buffer array = bufferAndroid.getBuffer();
      //      GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, count, array, GLES20.GL_STATIC_DRAW);
      //
      //      GLES20.glDrawElements(mode, count, GLES20.GL_UNSIGNED_SHORT, 0);
   }


   @Override
   public void texImage2D(final IImage image,
                          final int format) {
      checkOpenGLThread();
      final Bitmap b = ((Image_Android) image).getBitmap();
      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
   }


   @Override
   public int CullFace_Front() {
      return GLES20.GL_FRONT;
   }


   @Override
   public int CullFace_Back() {
      return GLES20.GL_BACK;
   }


   @Override
   public int CullFace_FrontAndBack() {
      return GLES20.GL_FRONT_AND_BACK;
   }


   @Override
   public int BufferType_ColorBuffer() {
      return GLES20.GL_COLOR_BUFFER_BIT;
   }


   @Override
   public int BufferType_DepthBuffer() {
      return GLES20.GL_DEPTH_BUFFER_BIT;
   }


   @Override
   public int Feature_PolygonOffsetFill() {
      return GLES20.GL_POLYGON_OFFSET_FILL;
   }


   @Override
   public int Feature_DepthTest() {
      return GLES20.GL_DEPTH_TEST;
   }


   @Override
   public int Feature_Blend() {
      return GLES20.GL_BLEND;
   }


   @Override
   public int Feature_CullFace() {
      return GLES20.GL_CULL_FACE;
   }


   @Override
   public int Type_Float() {
      return GLES20.GL_FLOAT;
   }


   @Override
   public int Type_UnsignedByte() {
      return GLES20.GL_UNSIGNED_BYTE;
   }


   @Override
   public int Type_UnsignedInt() {
      return GLES20.GL_UNSIGNED_INT;
   }


   @Override
   public int Type_Int() {
      return GLES20.GL_INT;
   }


   @Override
   public int Primitive_Triangles() {
      return GLES20.GL_TRIANGLES;
   }


   @Override
   public int Primitive_TriangleStrip() {
      return GLES20.GL_TRIANGLE_STRIP;
   }


   @Override
   public int Primitive_TriangleFan() {
      return GLES20.GL_TRIANGLE_FAN;
   }


   @Override
   public int Primitive_Lines() {
      return GLES20.GL_LINES;
   }


   @Override
   public int Primitive_LineStrip() {
      return GLES20.GL_LINE_STRIP;
   }


   @Override
   public int Primitive_LineLoop() {
      return GLES20.GL_LINE_LOOP;
   }


   @Override
   public int Primitive_Points() {
      return GLES20.GL_POINTS;
   }


   @Override
   public int BlendFactor_SrcAlpha() {
      return GLES20.GL_SRC_ALPHA;
   }


   @Override
   public int BlendFactor_OneMinusSrcAlpha() {
      return GLES20.GL_ONE_MINUS_SRC_ALPHA;
   }


   @Override
   public int TextureType_Texture2D() {
      return GLES20.GL_TEXTURE_2D;
   }


   @Override
   public int TextureParameter_MinFilter() {
      return GLES20.GL_TEXTURE_MIN_FILTER;
   }


   @Override
   public int TextureParameter_MagFilter() {
      return GLES20.GL_TEXTURE_MAG_FILTER;
   }


   @Override
   public int TextureParameter_WrapS() {
      return GLES20.GL_TEXTURE_WRAP_S;
   }


   @Override
   public int TextureParameter_WrapT() {
      return GLES20.GL_TEXTURE_WRAP_T;
   }


   @Override
   public int TextureParameterValue_Linear() {
      return GLES20.GL_LINEAR;
   }


   @Override
   public int TextureParameterValue_ClampToEdge() {
      return GLES20.GL_CLAMP_TO_EDGE;
   }


   @Override
   public int Alignment_Pack() {
      return GLES20.GL_PACK_ALIGNMENT;
   }


   @Override
   public int Alignment_Unpack() {
      return GLES20.GL_UNPACK_ALIGNMENT;
   }


   @Override
   public int Format_RGBA() {
      return GLES20.GL_RGBA;
   }


   @Override
   public int Variable_Viewport() {
      return GLES20.GL_VIEWPORT;
   }


   @Override
   public int Error_NoError() {
      return GLES20.GL_NO_ERROR;
   }


   @Override
   public void useProgram(final ShaderProgram program) {
      checkOpenGLThread();
      GLES20.glUseProgram(program.getProgram());
   }


   @Override
   public int getAttribLocation(final ShaderProgram program,
                                final String name) {
      checkOpenGLThread();
      return GLES20.glGetAttribLocation(program.getProgram(), name);
   }


   @Override
   public IGLUniformID getUniformLocation(final ShaderProgram program,
                                          final String name) {
      checkOpenGLThread();
      final int id = GLES20.glGetUniformLocation(program.getProgram(), name);
      return new GLUniformID_Android(id);
   }


   @Override
   public int createProgram() {
      checkOpenGLThread();
      return GLES20.glCreateProgram();
   }


   @Override
   public void deleteProgram(final int program) {
      checkOpenGLThread();
      GLES20.glDeleteProgram(program);
   }


   @Override
   public void attachShader(final int program,
                            final int shader) {
      checkOpenGLThread();
      GLES20.glAttachShader(program, shader);
   }


   @Override
   public int createShader(final ShaderType type) {
      checkOpenGLThread();
      switch (type) {
         case VERTEX_SHADER:
            return GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
         case FRAGMENT_SHADER:
            return GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
         default:
            throw new RuntimeException("Invalid ShaderType=" + type);
      }
   }


   @Override
   public boolean compileShader(final int shader,
                                final String source) {
      checkOpenGLThread();
      GLES20.glShaderSource(shader, source);
      GLES20.glCompileShader(shader);
      final int[] compiled = new int[1];
      GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
      return (compiled[0] != 0);
   }


   @Override
   public void deleteShader(final int shader) {
      checkOpenGLThread();
      GLES20.glDeleteShader(shader);
   }


   @Override
   public void printShaderInfoLog(final int shader) {
      checkOpenGLThread();
      final int[] compiled = new int[1];
      GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
      if (compiled[0] == 0) {
         Log.e("GL2Shaders", "Could not compile shader " + shader + ":");
         Log.e("GL2Shaders", GLES20.glGetShaderInfoLog(shader));
      }
   }


   @Override
   public boolean linkProgram(final int program) {
      checkOpenGLThread();
      GLES20.glLinkProgram(program);
      final int[] linkStatus = new int[1];
      GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
      return (linkStatus[0] == GLES20.GL_TRUE);
   }


   @Override
   public void printProgramInfoLog(final int program) {
      checkOpenGLThread();
      final int[] linkStatus = new int[1];
      GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
      if (linkStatus[0] == GLES20.GL_TRUE) {
         Log.e("GL2Shaders", "Could not link program: ");
         Log.e("GL2Shaders", GLES20.glGetProgramInfoLog(program));
      }
   }


}
