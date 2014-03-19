

package org.glob3.mobile.specific;

import java.io.UnsupportedEncodingException;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.glob3.mobile.generated.GPUAttribute;
import org.glob3.mobile.generated.GPUAttributeVec2Float;
import org.glob3.mobile.generated.GPUAttributeVec3Float;
import org.glob3.mobile.generated.GPUAttributeVec4Float;
import org.glob3.mobile.generated.GPUProgram;
import org.glob3.mobile.generated.GPUUniform;
import org.glob3.mobile.generated.GPUUniformBool;
import org.glob3.mobile.generated.GPUUniformFloat;
import org.glob3.mobile.generated.GPUUniformMatrix4Float;
import org.glob3.mobile.generated.GPUUniformSampler2D;
import org.glob3.mobile.generated.GPUUniformVec2Float;
import org.glob3.mobile.generated.GPUUniformVec3Float;
import org.glob3.mobile.generated.GPUUniformVec4Float;
import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLTextureId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.INativeGL;
import org.glob3.mobile.generated.IShortBuffer;
import org.glob3.mobile.generated.Matrix44D;
import org.glob3.mobile.generated.ShaderType;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;


public final class NativeGL2_Android
         extends
            INativeGL {

   private Thread _openGLThread = null;


   void setOpenGLThread(final Thread openGLThread) {
      _openGLThread = openGLThread;
   }


   private final void checkOpenGLThread() {
      if (_openGLThread != null) {
         final Thread currentThread = Thread.currentThread();
         if (currentThread != _openGLThread) {
            throw new RuntimeException("OpenGL code executed from a Non-OpenGL thread.  (OpenGLThread=" + _openGLThread
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

      //      ILogger.instance().logInfo("UNIFORM " + ((GLUniformID_Android) loc).getID() + " " + x + " " + y);
   }


   @Override
   public void uniform1f(final IGLUniformID loc,
                         final float x) {
      checkOpenGLThread();
      GLES20.glUniform1f(((GLUniformID_Android) loc).getID(), x);

      //      ILogger.instance().logInfo("UNIFORM " + ((GLUniformID_Android) loc).getID() + " " + x);
   }


   @Override
   public void uniform1i(final IGLUniformID loc,
                         final int v) {
      checkOpenGLThread();
      GLES20.glUniform1i(((GLUniformID_Android) loc).getID(), v);

      //      ILogger.instance().logInfo("UNIFORM " + ((GLUniformID_Android) loc).getID() + " " + v);
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
                                final Matrix44D matrix) {
      checkOpenGLThread();
      GLES20.glUniformMatrix4fv( //
               ((GLUniformID_Android) location).getID(), //
               1, //
               transpose, //
               matrix.getColumnMajorFloatArray(), //
               0 //
      );

      //      ILogger.instance().logInfo("UNIFORM MATRIX " + ((GLUniformID_Android) location).getID() + " " + matrix.description() );
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

      //      ILogger.instance().logInfo("UNIFORM " + ((GLUniformID_Android) location).getID() + " " + v0 + " " + v1 + " " + v2 + " " + v3);
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
      //      ILogger.instance().logInfo("Attrib Enabled " + location);
   }


   @Override
   public void disableVertexAttribArray(final int location) {
      checkOpenGLThread();
      GLES20.glDisableVertexAttribArray(location);
      //      ILogger.instance().logInfo("Attrib Disabled " + location);
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

      //      ILogger.instance().logInfo("vertexAttribPointer(index=" + index + //
      //                               ", size=" + size + //
      //                               ", normalized=" + normalized + //
      //                               ", stride=" + stride + //
      //                               ", floatBuffer=" + floatBuffer + ")");


      //      final FloatBuffer floatBuffer = ((FloatBuffer_Android) buffer).getBuffer();
      //      GLES20.glVertexAttribPointer(index, size, GLES20.GL_FLOAT, normalized, stride, floatBuffer);


      final FloatBuffer_Android buffer_Android = (FloatBuffer_Android) buffer;
      buffer_Android.bindAsVBOToGPU();
      GLES20.glVertexAttribPointer(index, size, GLES20.GL_FLOAT, normalized, stride, 0);
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
      final Bitmap bitmap = ((Image_Android) image).getBitmap();
      if (bitmap == null) {
         ILogger.instance().logError("texImage2D(): bitmap is null");
      }
      else {
         GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
      }
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
   public int createProgram() {
      checkOpenGLThread();
      return GLES20.glCreateProgram();
   }


   @Override
   public boolean deleteProgram(final int program) {
      checkOpenGLThread();
      GLES20.glDeleteProgram(program);
      return true;
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
   public boolean deleteShader(final int shader) {
      checkOpenGLThread();
      GLES20.glDeleteShader(shader);
      return true;
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


   @Override
   public int BlendFactor_One() {
      return GLES20.GL_ONE;
   }


   @Override
   public int BlendFactor_Zero() {
      return GLES20.GL_ZERO;
   }


   @Override
   public void useProgram(final GPUProgram program) {
      checkOpenGLThread();
      GLES20.glUseProgram(program.getProgramID());
   }


   @Override
   public int Type_Vec2Float() {
      return GLES20.GL_FLOAT_VEC2;
   }


   @Override
   public int Type_Vec4Float() {
      return GLES20.GL_FLOAT_VEC4;
   }


   @Override
   public int Type_Bool() {
      return GLES20.GL_BOOL;
   }


   @Override
   public int Type_Matrix4Float() {
      return GLES20.GL_FLOAT_MAT4;
   }


   @Override
   public int Variable_ActiveAttributes() {
      return GLES20.GL_ACTIVE_ATTRIBUTES;
   }


   @Override
   public int Variable_ActiveUniforms() {
      return GLES20.GL_ACTIVE_UNIFORMS;
   }


   @Override
   public void bindAttribLocation(final GPUProgram program,
                                  final int loc,
                                  final String name) {
      checkOpenGLThread();
      GLES20.glBindAttribLocation(program.getProgramID(), loc, name);
   }


   @Override
   public int getProgramiv(final GPUProgram program,
                           final int param) {
      checkOpenGLThread();
      final int[] i = new int[1];
      GLES20.glGetProgramiv(program.getProgramID(), param, i, 0);
      return i[0];
   }


   @Override
   public GPUUniform getActiveUniform(final GPUProgram program,
                                      final int i) {
      checkOpenGLThread();
      final int[] maxLength = new int[1];
      GLES20.glGetProgramiv(program.getProgramID(), GLES20.GL_ACTIVE_UNIFORM_MAX_LENGTH, maxLength, 0);

      final int bufsize = maxLength[0] + 1;

      final byte[] name = new byte[bufsize];
      final int[] length = new int[1];
      final int[] size = new int[1];
      final int[] type = new int[1];

      GLES20.glGetActiveUniform(program.getProgramID(), i, bufsize, length, 0, size, 0, type, 0, name, 0);

      String nameStr = new String(name);
      nameStr = nameStr.substring(0, length[0]);
      final int id = GLES20.glGetUniformLocation(program.getProgramID(), nameStr);

      //ILogger.instance().logInfo("Uniform Name: %s - %d", nameStr, id);
      switch (type[0]) {
         case GLES20.GL_FLOAT_MAT4:
            return new GPUUniformMatrix4Float(nameStr, new GLUniformID_Android(id));
         case GLES20.GL_FLOAT_VEC4:
            return new GPUUniformVec4Float(nameStr, new GLUniformID_Android(id));
         case GLES20.GL_FLOAT:
            return new GPUUniformFloat(nameStr, new GLUniformID_Android(id));
         case GLES20.GL_FLOAT_VEC2:
            return new GPUUniformVec2Float(nameStr, new GLUniformID_Android(id));
         case GLES20.GL_FLOAT_VEC3:
            return new GPUUniformVec3Float(nameStr, new GLUniformID_Android(id));
         case GLES20.GL_BOOL:
            return new GPUUniformBool(nameStr, new GLUniformID_Android(id));
         case GLES20.GL_SAMPLER_2D:
            return new GPUUniformSampler2D(nameStr, new GLUniformID_Android(id));
         default:
            return null;
      }
   }


   @Override
   public GPUAttribute getActiveAttribute(final GPUProgram program,
                                          final int i) {
      checkOpenGLThread();
      final int[] maxLength = new int[1];
      GLES20.glGetProgramiv(program.getProgramID(), GLES20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, maxLength, 0);

      final int bufsize = maxLength[0];

      final byte[] name = new byte[maxLength[0]];
      final int[] length = new int[1];
      final int[] size = new int[1];
      final int[] type = new int[1];

      GLES20.glGetActiveAttrib(program.getProgramID(), i, bufsize, length, 0, size, 0, type, 0, name, 0);

      try {
         String nameStr = new String(name, "UTF-8");
         nameStr = nameStr.substring(0, length[0]);
         final int id = GLES20.glGetAttribLocation(program.getProgramID(), nameStr);

         //ILogger.instance().logInfo("Attribute Name: %s - %d", nameStr, id);

         switch (type[0]) {
            case GLES20.GL_FLOAT_VEC3:
               return new GPUAttributeVec3Float(nameStr, id);
            case GLES20.GL_FLOAT_VEC4:
               return new GPUAttributeVec4Float(nameStr, id);
            case GLES20.GL_FLOAT_VEC2:
               return new GPUAttributeVec2Float(nameStr, id);
            default:
               return null;
         }

      }
      catch (final UnsupportedEncodingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return null;
      }
   }


   @Override
   public void uniform3f(final IGLUniformID location,
                         final float v0,
                         final float v1,
                         final float v2) {
      final int loc = ((GLUniformID_Android) location).getID();
      GLES20.glUniform3f(loc, v0, v1, v2);
   }


   @Override
   public int Type_Vec3Float() {
      return GLES20.GL_FLOAT_VEC3;
   }


   @Override
   public void depthMask(final boolean depthMask) {
      GLES20.glDepthMask(depthMask);
   }


   @Override
   public int TextureParameterValue_Linear() {
      return GLES20.GL_LINEAR;
   }


   @Override
   public int TextureParameterValue_Nearest() {
      return GLES20.GL_NEAREST;
   }


   @Override
   public int TextureParameterValue_NearestMipmapNearest() {
      return GLES20.GL_NEAREST_MIPMAP_NEAREST;
   }


   @Override
   public int TextureParameterValue_NearestMipmapLinear() {
      return GLES20.GL_NEAREST_MIPMAP_LINEAR;
   }


   @Override
   public int TextureParameterValue_LinearMipmapNearest() {
      return GLES20.GL_LINEAR_MIPMAP_NEAREST;
   }


   @Override
   public int TextureParameterValue_LinearMipmapLinear() {
      return GLES20.GL_LINEAR_MIPMAP_LINEAR;
   }


   @Override
   public void setActiveTexture(final int i) {
      GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
   }


}
