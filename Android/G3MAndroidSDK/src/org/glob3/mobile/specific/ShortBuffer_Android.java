

package org.glob3.mobile.specific;

import android.opengl.GLES20;

import java.nio.ShortBuffer;

import org.glob3.mobile.generated.IShortBuffer;


public class ShortBuffer_Android
         extends
            IShortBuffer {
   private final ShortBuffer _buffer;
   private int               _timestamp;
   
   //ID
   private static long _nextID = 0;
   private final long _id = _nextID++;
   private int _glBuffer;
   private boolean _vertexBufferCreated = false;
   private int _boundVertexBuffer = -1;
   private int _vertexBufferTimeStamp = -1;


   //   private boolean           _hasGLBuffer = false;
   //   private int               _glBuffer;


   ShortBuffer_Android(final int size) {
      _buffer = ShortBuffer.wrap(new short[size]);
   }


   ShortBuffer_Android(final short[] array) {
      _buffer = ShortBuffer.wrap(array);
   }


   ShortBuffer_Android(final short[] array,
                       final int length) {
      final short[] result = new short[length];
      System.arraycopy(array, 0, result, 0, length);
      _buffer = ShortBuffer.wrap(result);

      //      //_buffer = ByteBuffer.allocate(length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
      //      _buffer = ByteBuffer.allocate(length * 2).asShortBuffer();
      //      _buffer.put(array, 0, length);
      //      _buffer.rewind();

      //      _buffer = ShortBuffer.wrap(array, 0, length);
      //      _buffer.rewind();
   }


   @Override
   public int size() {
      return _buffer.capacity();
   }


   @Override
   public void rewind() {
      _buffer.rewind();
   }

   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public short get(final int i) {
      return _buffer.get(i);
   }


   @Override
   public void put(final int i,
                   final short value) {
      /*if (_buffer.get(i) != value) */{
         _buffer.put(i, value);
         _timestamp++;
      }
   }

   @Override
   public void put(short[] value) {
      _buffer.put(value);
      _timestamp++;
   }




   @Override
   public void rawPut(final int i,
                      final short value) {
      _buffer.put(i, value);
   }


   public ShortBuffer getBuffer() {
      return _buffer;
   }


   @Override
   public String description() {
      return "ShortBuffer_Android(timestamp=" + _timestamp + ", buffer=" + _buffer + ")";
   }


   @Override
   public long getID() {
	  return _id;
   }




   public void bindAsElementVBOToGPU() {

      if (!_vertexBufferCreated) {
         final java.nio.IntBuffer ib = java.nio.IntBuffer.allocate(1);
         GLES20.glGenBuffers(1, ib); //COULD RETURN GL_INVALID_VALUE EVEN WITH NO ERROR
         _glBuffer = ib.get(0);
         _vertexBufferCreated = true;
      }

      if (_glBuffer != _boundVertexBuffer) {
         GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, _glBuffer);
         _boundVertexBuffer = _glBuffer;
      }

      if (_vertexBufferTimeStamp != _timestamp) {
         _vertexBufferTimeStamp = _timestamp;

         final ShortBuffer buffer = getBuffer();
         final int vboSize = 2 * size(); // sizeof short = 2
         GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboSize, buffer, GLES20.GL_STATIC_DRAW);
      }

   }


   public void bindAsVBOToGPU() {

      if (!_vertexBufferCreated) {
         final java.nio.IntBuffer ib = java.nio.IntBuffer.allocate(1);
         GLES20.glGenBuffers(1, ib); //COULD RETURN GL_INVALID_VALUE EVEN WITH NO ERROR
         _glBuffer = ib.get(0);
         _vertexBufferCreated = true;
      }

      if (_glBuffer != _boundVertexBuffer) {
         GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _glBuffer);
         _boundVertexBuffer = _glBuffer;
      }

      if (_vertexBufferTimeStamp != _timestamp) {
         _vertexBufferTimeStamp = _timestamp;

         final ShortBuffer buffer = getBuffer();
         final int vboSize = 2 * size(); // sizeof short = 2
         GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vboSize, buffer, GLES20.GL_STATIC_DRAW);
      }
   }


   @Override
   public void dispose() {
      if (_vertexBufferCreated) {
         final int[] buffers = new int[] {_glBuffer};
         GLES20.glDeleteBuffers(1, buffers, 0);
         _vertexBufferCreated = false;

         if (_glBuffer == _boundVertexBuffer) {
            _boundVertexBuffer = -1;
         }
      }
   }


   //   @Override
   //   public void dispose() {
   //      super.dispose();
   //
   //      if (_hasGLBuffer) {
   //         final int[] buffers = new int[] { _glBuffer };
   //         GLES20.glDeleteBuffers(1, buffers, 0);
   //         _hasGLBuffer = false;
   //      }
   //   }
   //
   //
   //   public int getGLBuffer() {
   //      if (!_hasGLBuffer) {
   //         final int[] buffers = new int[1];
   //         GLES20.glGenBuffers(1, buffers, 0);
   //         _glBuffer = buffers[0];
   //         _hasGLBuffer = true;
   //      }
   //
   //      return _glBuffer;
   //   }

}
