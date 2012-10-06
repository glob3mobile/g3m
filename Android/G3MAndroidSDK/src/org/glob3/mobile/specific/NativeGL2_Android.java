

package org.glob3.mobile.specific;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.glob3.mobile.generated.IFloatBuffer;
import org.glob3.mobile.generated.IGLProgramId;
import org.glob3.mobile.generated.IGLTextureId;
import org.glob3.mobile.generated.IGLUniformID;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IIntBuffer;
import org.glob3.mobile.generated.INativeGL;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;


public class NativeGL2_Android
         extends
            INativeGL {

   //   final int getBitField(final GLBufferType b) {
   //      switch (b) {
   //         case ColorBuffer:
   //            return GLES20.GL_COLOR_BUFFER_BIT;
   //         case DepthBuffer:
   //            return GLES20.GL_DEPTH_BUFFER_BIT;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLBufferType=" + b);
   //      }
   //   }
   //
   //
   //   int getEnum(final GLFeature f) {
   //      switch (f) {
   //         case PolygonOffsetFill:
   //            return GLES20.GL_POLYGON_OFFSET_FILL;
   //         case DepthTest:
   //            return GLES20.GL_DEPTH_TEST;
   //         case Blend:
   //            return GLES20.GL_BLEND;
   //         case CullFacing:
   //            return GLES20.GL_CULL_FACE;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLFeature=" + f);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLCullFace f) {
   //      switch (f) {
   //         case Front:
   //            return GLES20.GL_FRONT;
   //         case FrontAndBack:
   //            return GLES20.GL_FRONT_AND_BACK;
   //         case Back:
   //            return GLES20.GL_BACK;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLCullFace=" + f);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLType t) {
   //      switch (t) {
   //         case Float:
   //            return GLES20.GL_FLOAT;
   //         case UnsignedByte:
   //            return GLES20.GL_UNSIGNED_BYTE;
   //         case UnsignedInt:
   //            return GLES20.GL_UNSIGNED_INT;
   //         case Int:
   //            return GLES20.GL_INT;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLType=" + t);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLPrimitive p) {
   //      switch (p) {
   //         case TriangleStrip:
   //            return GLES20.GL_TRIANGLE_STRIP;
   //         case Lines:
   //            return GLES20.GL_LINES;
   //         case LineLoop:
   //            return GLES20.GL_LINE_LOOP;
   //         case Points:
   //            return GLES20.GL_POINTS;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLPrimitive=" + p);
   //      }
   //   }
   //
   //
   //   final GLError getError(final int e) {
   //      switch (e) {
   //         case GLES20.GL_NO_ERROR:
   //            return GLError.NoError;
   //         case GLES20.GL_INVALID_ENUM:
   //            return GLError.InvalidEnum;
   //         case GLES20.GL_INVALID_VALUE:
   //            return GLError.InvalidValue;
   //         case GLES20.GL_INVALID_OPERATION:
   //            return GLError.InvalidOperation;
   //         case GLES20.GL_OUT_OF_MEMORY:
   //            return GLError.OutOfMemory;
   //         default:
   //            return GLError.UnknownError;
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLBlendFactor b) {
   //      switch (b) {
   //         case SrcAlpha:
   //            return GLES20.GL_SRC_ALPHA;
   //         case OneMinusSrcAlpha:
   //            return GLES20.GL_ONE_MINUS_SRC_ALPHA;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLBlendFactor=" + b);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLAlignment a) {
   //      switch (a) {
   //         case Unpack:
   //            return GLES20.GL_UNPACK_ALIGNMENT;
   //         case Pack:
   //            return GLES20.GL_PACK_ALIGNMENT;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLAlignment=" + a);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLTextureType t) {
   //      switch (t) {
   //         case Texture2D:
   //            return GLES20.GL_TEXTURE_2D;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLTextureType=" + t);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLTextureParameter t) {
   //      switch (t) {
   //         case MinFilter:
   //            return GLES20.GL_TEXTURE_MIN_FILTER;
   //         case MagFilter:
   //            return GLES20.GL_TEXTURE_MAG_FILTER;
   //         case WrapS:
   //            return GLES20.GL_TEXTURE_WRAP_S;
   //         case WrapT:
   //            return GLES20.GL_TEXTURE_WRAP_T;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLTextureParameter=" + t);
   //      }
   //   }
   //
   //
   //   final int getValue(final GLTextureParameterValue t) {
   //      switch (t) {
   //         case Linear:
   //            return GLES20.GL_LINEAR;
   //         case ClampToEdge:
   //            return GLES20.GL_CLAMP_TO_EDGE;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLTextureParameterValue=" + t);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLFormat f) {
   //      switch (f) {
   //         case RGBA:
   //            return GLES20.GL_RGBA;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLFormat=" + f);
   //      }
   //   }
   //
   //
   //   final int getEnum(final GLVariable v) {
   //      switch (v) {
   //         case Viewport:
   //            return GLES20.GL_VIEWPORT;
   //         default:
   //            throw new UnsupportedOperationException("Unsupported GLVariable=" + v);
   //      }
   //   }


   /////////////////////////////

   @Override
   public void useProgram(final IGLProgramId program) {
      GLES20.glUseProgram(((GLProgramId_Android) program).getProgram());
   }


   @Override
   public int getAttribLocation(final IGLProgramId program,
                                final String name) {
      return GLES20.glGetAttribLocation(((GLProgramId_Android) program).getProgram(), name);
   }


   @Override
   public IGLUniformID getUniformLocation(final IGLProgramId program,
                                          final String name) {
      final int id = GLES20.glGetUniformLocation(((GLProgramId_Android) program).getProgram(), name);
      return (new GLUniformID_Android(id));
   }


   @Override
   public void uniform2f(final IGLUniformID loc,
                         final float x,
                         final float y) {
      GLES20.glUniform2f(((GLUniformID_Android) loc).getID(), x, y);
   }


   @Override
   public void uniform1f(final IGLUniformID loc,
                         final float x) {
      GLES20.glUniform1f(((GLUniformID_Android) loc).getID(), x);
   }


   @Override
   public void uniform1i(final IGLUniformID loc,
                         final int v) {
      GLES20.glUniform1i(((GLUniformID_Android) loc).getID(), v);
   }


   //   @Override
   //   public void uniformMatrix4fv(final IGLUniformID location,
   //                                final int count,
   //                                final boolean transpose,
   //                                final float[] value) {
   //      //      final FloatBuffer fb = floatArrayToFloatBuffer(value);
   //      //
   //      //      GLES20.glUniformMatrix4fv(location, count, transpose, fb);
   //      GLES20.glUniformMatrix4fv(((GLUniformID_Android) location).getID(), count, transpose, value, 0);
   //   }

   @Override
   public void uniformMatrix4fv(final IGLUniformID location,
                                final boolean transpose,
                                final IFloatBuffer buffer) {
      //      final FloatBuffer fb = floatArrayToFloatBuffer(value);
      //
      //      GLES20.glUniformMatrix4fv(location, count, transpose, fb);
      GLES20.glUniformMatrix4fv(((GLUniformID_Android) location).getID(), 1, transpose,
               ((FloatBuffer_Android) buffer).getBuffer());
   }


   @Override
   public void clearColor(final float red,
                          final float green,
                          final float blue,
                          final float alpha) {
      GLES20.glClearColor(red, green, blue, alpha);
   }


   @Override
   public void clear(final int buffers) {
      //   public void clear(final int nBuffer,
      //                     final GLBufferType[] buffers) {
      //      int b = 0x00000000;
      //      for (final GLBufferType buffer : buffers) {
      //         b |= getBitField(buffer);
      //      }
      GLES20.glClear(buffers);
   }


   @Override
   public void uniform4f(final IGLUniformID location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      GLES20.glUniform4f(((GLUniformID_Android) location).getID(), v0, v1, v2, v3);
   }


   @Override
   public void enable(final int feature) {
      GLES20.glEnable(feature);
   }


   @Override
   public void disable(final int feature) {
      GLES20.glDisable(feature);
   }


   @Override
   public void polygonOffset(final float factor,
                             final float units) {
      GLES20.glPolygonOffset(factor, units);
   }


   @Override
   public void lineWidth(final float width) {
      GLES20.glLineWidth(width);
   }


   @Override
   public int getError() {
      return GLES20.glGetError();
   }


   @Override
   public void blendFunc(final int sfactor,
                         final int dfactor) {
      GLES20.glBlendFunc(sfactor, dfactor);
   }


   @Override
   public void bindTexture(final int target,
                           final IGLTextureId texture) {
      GLES20.glBindTexture(target, ((GLTextureId_Android) texture).getGLTextureId());
   }


   @Override
   public void deleteTextures(final int n,
                              final IGLTextureId[] textures) {
      final int[] texIds = new int[n];
      for (int i = 0; i < n; i++) {
         texIds[i] = ((GLTextureId_Android) textures[i]).getGLTextureId();
      }
      GLES20.glDeleteTextures(n, texIds, 0);
   }


   @Override
   public void enableVertexAttribArray(final int location) {
      GLES20.glEnableVertexAttribArray(location);
   }


   @Override
   public void disableVertexAttribArray(final int location) {
      GLES20.glDisableVertexAttribArray(location);
   }


   @Override
   public void pixelStorei(final int pname,
                           final int param) {
      GLES20.glPixelStorei(pname, param);
   }


   @Override
   public ArrayList<IGLTextureId> genTextures(final int n) {
      final ArrayList<IGLTextureId> ai = new ArrayList<IGLTextureId>();
      final int[] tex = new int[n];
      GLES20.glGenTextures(n, tex, 0);
      for (int i = 0; i < n; i++) {
         ai.add(new GLTextureId_Android(tex[i]));
      }
      return ai;
   }


   @Override
   public void texParameteri(final int target,
                             final int par,
                             final int v) {
      GLES20.glTexParameteri(target, par, v);
   }


   //   @Override
   //   public void texImage2D(final GLTextureType target,
   //                          final int level,
   //                          final GLFormat internalFormat,
   //                          final int width,
   //                          final int height,
   //                          final int border,
   //                          final GLFormat format,
   //                          final GLType type,
   //                          final Object data) {
   //
   //      if (type == GLType.UnsignedByte) {
   //         final byte[] array = (byte[]) data;
   //         final ByteBuffer pixels = ByteBuffer.wrap(array);
   //         GLES20.glTexImage2D(getEnum(target), level, getEnum(internalFormat), width, height, border, getEnum(format),
   //                  getEnum(type), pixels);
   //      }
   //      else {
   //         throw new UnsupportedOperationException("Invalid type=" + type);
   //      }
   //   }


   @Override
   public void drawArrays(final int mode,
                          final int first,
                          final int count) {
      GLES20.glDrawArrays(mode, first, count);

   }


   @Override
   public void cullFace(final int c) {
      GLES20.glCullFace(c);
   }


   @Override
   public void getIntegerv(final int v,
                           final int[] i) {
      GLES20.glGetIntegerv(v, i, 0);
   }


   @Override
   public void generateMipmap(final int target) {
      GLES20.glGenerateMipmap(target);
   }


   @Override
   public void vertexAttribPointer(final int index,
                                   final int size,
                                   final boolean normalized,
                                   final int stride,
                                   final IFloatBuffer buffer) {
      final FloatBuffer floatBuffer = ((FloatBuffer_Android) buffer).getBuffer();
      GLES20.glVertexAttribPointer(index, size, GLES20.GL_FLOAT, normalized, stride, floatBuffer);
   }


   @Override
   public void drawElements(final int mode,
                            final int count,
                            final IIntBuffer indices) {
      final IntBuffer indexBuffer = ((IntBuffer_Android) indices).getBuffer();
      GLES20.glDrawElements(mode, count, GLES20.GL_UNSIGNED_INT, indexBuffer);
   }


   @Override
   public void texImage2D(final IImage image,
                          final int format) {

      final Bitmap b = ((Image_Android) image).getBitmap();
      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);

      //      final ByteBuffer_Android bb = (ByteBuffer_Android) ((Image_Android) image).createByteBufferRGBA8888(image.getWidth(),
      //               image.getHeight());
      //
      //      GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, getEnum(format), image.getWidth(), image.getHeight(), 0, getEnum(format),
      //               GLES20.GL_UNSIGNED_BYTE, bb.getBuffer());

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
   public int Primitive_TriangleStrip() {
      return GLES20.GL_TRIANGLE_STRIP;
   }


   @Override
   public int Primitive_Lines() {
      return GLES20.GL_LINES;
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
}
