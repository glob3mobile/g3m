

package org.glob3.mobile.specific;


import java.nio.charset.*;

import org.glob3.mobile.generated.*;


public final class ByteBuffer_Android
                                      extends
                                         IByteBuffer {

   private static final Charset UTF_8 = Charset.forName("UTF-8");

   private byte[] _buffer;
   private int    _timestamp = 0;


   ByteBuffer_Android(final byte[] data) {
      _buffer = data;
   }


   public ByteBuffer_Android(final int size) {
      _buffer = new byte[size];
   }


   @Override
   public void dispose() {
      _buffer = null;
      super.dispose();
   }


   @Override
   public int size() {
      return _buffer.length;
   }


   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public byte get(final int i) {
      return _buffer[i];
   }


   @Override
   public void put(final int i,
                   final byte value) {
      if (_buffer[i] != value) {
         _buffer[i] = value;
         _timestamp++;
      }
   }


   @Override
   public void rawPut(final int i,
                      final byte value) {
      _buffer[i] = value;
   }


   public byte[] getBuffer() {
      return _buffer;
   }


   @Override
   public String description() {
      return "ByteBuffer_Android (size=" + _buffer.length + ")";
   }


   @Override
   public String getAsString() {
      return new String(_buffer, UTF_8);
   }


   @Override
   public ByteBuffer_Android copy(final int from,
                                  final int length) {
      final byte[] newBuffer = new byte[length];
      System.arraycopy(_buffer, from, newBuffer, 0, length);
      return new ByteBuffer_Android(newBuffer);
   }


}
