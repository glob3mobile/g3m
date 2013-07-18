

package org.glob3.mobile.specific;

import java.nio.ShortBuffer;

import org.glob3.mobile.generated.IShortBuffer;


public class ShortBuffer_Android
         extends
            IShortBuffer {
   private final ShortBuffer _buffer;
   private int               _timestamp;


   //   private boolean           _hasGLBuffer = false;
   //   private int               _glBuffer;


   public ShortBuffer_Android(final int size) {
      _buffer = ShortBuffer.wrap(new short[size]);
   }


   public ShortBuffer_Android(final short[] array) {
      _buffer = ShortBuffer.wrap(array);
   }


   @Override
   public int size() {
      return _buffer.capacity();
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
      if (_buffer.get(i) != value) {
         _buffer.put(i, value);
         _timestamp++;
      }
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
