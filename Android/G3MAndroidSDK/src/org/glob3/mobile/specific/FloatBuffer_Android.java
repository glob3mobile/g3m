package org.glob3.mobile.specific;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.glob3.mobile.generated.IFloatBuffer;

public class FloatBuffer_Android extends IFloatBuffer {
   
   FloatBuffer _buffer;
   int       _timestamp;
   
   public FloatBuffer_Android(int size) {
      _buffer = ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
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
   public float get(int i) {
      return _buffer.get(i);
   }


   @Override
   public void put(int i,
                   float value) {
      if (_buffer.get(i) != value) {
         _buffer.put(i, value);
         _timestamp++;
       }
   }
   
   public FloatBuffer getBuffer() {
      return _buffer;
   }

}
