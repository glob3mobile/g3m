

package org.glob3.mobile.specific;

import java.nio.ShortBuffer;

import org.glob3.mobile.generated.IShortBuffer;


public class ShortBuffer_Android
         extends
            IShortBuffer {
   private final ShortBuffer _buffer;
   private int               _timestamp;


   public ShortBuffer_Android(final int size) {
      // _buffer = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      //_buffer = ByteBuffer.allocate(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      _buffer = ShortBuffer.wrap(new short[size]);
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

}
