

package org.glob3.mobile.specific;

import java.nio.ByteBuffer;

import org.glob3.mobile.generated.IByteBuffer;


public class ByteBuffer_JavaDesktop
    extends
      IByteBuffer {

  private final ByteBuffer _buffer;
  private int _timestamp = 0;


  public ByteBuffer_JavaDesktop(final byte[] data) {
    _buffer = ByteBuffer.wrap(data);

    // _buffer = ByteBuffer.allocateDirect(data.length);
    // _buffer.put(data);
    // _buffer.rewind();
  }


  public ByteBuffer_JavaDesktop(final int size) {
    // _buffer = ByteBuffer.allocate(size);
    _buffer = ByteBuffer.wrap(new byte[size]);
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
  public byte get(final int i) {
    return _buffer.get(i);
  }


  @Override
  public void put(final int i,
                  final byte value) {
    if (_buffer.get(i) != value) {
      _buffer.put(i, value);
      _timestamp++;
    }
  }


  @Override
  public void rawPut(final int i,
                     final byte value) {
    _buffer.put(i, value);
  }


  public ByteBuffer getBuffer() {
    return _buffer;
  }


  @Override
  public String description() {
    return "ByteBuffer_Desktop (size=" + _buffer.capacity() + ")";
  }


  @Override
  public String getAsString() {
    final byte[] bytes = _buffer.array();
    return new String(bytes);
  }

}
