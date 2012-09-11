package org.glob3.mobile.specific;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.glob3.mobile.generated.IIntBuffer;

public class IntBuffer_Android extends IIntBuffer {
   
   IntBuffer _buffer;
   int       _timestamp;

   public IntBuffer_Android(int size) {
      _buffer = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
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
   public int get(int i) {
      return _buffer.get(i);
   }


   @Override
   public void put(int i,
                   int value) {
      if (_buffer.get(i) != value) {
         _buffer.put(i, value);
         _timestamp++;
       }
   }
   
   public IntBuffer getBuffer() {
      return _buffer;
   }

}
