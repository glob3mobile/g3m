

package org.glob3.mobile.specific;

import java.io.UnsupportedEncodingException;

import org.glob3.mobile.generated.IByteBuffer;


public final class ByteBuffer_Android
extends
IByteBuffer {

   //private final ByteBuffer _buffer;
   private final byte[] _buffer;
   private int          _timestamp = 0;


   ByteBuffer_Android(final byte[] data) {
      //      _buffer = ByteBuffer.wrap(data);
      _buffer = data;

      //_buffer = ByteBuffer.allocateDirect(data.length);
      //_buffer.put(data);
      //_buffer.rewind();
   }


   public ByteBuffer_Android(final int size) {
      //_buffer = ByteBuffer.allocate(size);
      //      _buffer = ByteBuffer.wrap(new byte[size]);
      _buffer = new byte[size];
   }


   @Override
   public int size() {
      //      return _buffer.capacity();
      return _buffer.length;
   }


   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public byte get(final int i) {
      //      return _buffer.get(i);
      return _buffer[i];
   }


   @Override
   public void put(final int i,
                   final byte value) {
      //      if (_buffer.get(i) != value) {
      //         _buffer.put(i, value);
      //         _timestamp++;
      //      }
      if (_buffer[i] != value) {
         _buffer[i] = value;
         _timestamp++;
      }
   }


   @Override
   public void rawPut(final int i,
                      final byte value) {
      //      _buffer.put(i, value);
      _buffer[i] = value;
   }


   //   public ByteBuffer getBuffer() {
   //      return _buffer;
   //   }


   public byte[] getBuffer() {
      return _buffer;
   }


   @Override
   public String description() {
      //      return "ByteBuffer_iOS (size=" + _buffer.capacity() + ")";
      return "ByteBuffer_iOS (size=" + _buffer.length + ")";
   }


   @Override
   public String getAsString() {
      //      final byte[] bytes = _buffer.array();
      //      return new String(bytes);
      try {
         return new String(_buffer, "UTF-8");
      }
      catch (final UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
   }


}
