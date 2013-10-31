

package org.glob3.mobile.specific;

import java.nio.IntBuffer;

import org.glob3.mobile.generated.IIntBuffer;


public final class IntBuffer_Android
         extends
            IIntBuffer {

   private final IntBuffer _buffer;
   private int             _timestamp;
   
   //ID
   private static long _nextID = 0;
   private final long _id = _nextID++;


   IntBuffer_Android(final int size) {
      // _buffer = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      //_buffer = ByteBuffer.allocate(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
      _buffer = IntBuffer.wrap(new int[size]);
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
   public int get(final int i) {
      return _buffer.get(i);
   }


   @Override
   public void put(final int i,
                   final int value) {
      if (_buffer.get(i) != value) {
         _buffer.put(i, value);
         _timestamp++;
      }
   }


   @Override
   public void rawPut(final int i,
                      final int value) {
      _buffer.put(i, value);
   }


   public IntBuffer getBuffer() {
      return _buffer;
   }


   @Override
   public String description() {
      return "IntBuffer_Android(timestamp=" + _timestamp + ", buffer=" + _buffer + ")";
   }


@Override
public long getID() {
	return _id;
}

}
