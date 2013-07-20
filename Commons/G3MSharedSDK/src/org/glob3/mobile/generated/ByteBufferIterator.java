package org.glob3.mobile.generated; 
//
//  ByteBufferIterator.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//

//
//  ByteBufferIterator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/2/13.
//
//


//class IByteBuffer;


public class ByteBufferIterator
{
  private final IByteBuffer _buffer;
  private int _cursor;
  private int _timestamp;

//  void checkTimestamp() const;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ByteBufferIterator(ByteBufferIterator that);

  public ByteBufferIterator(IByteBuffer buffer)
  {
     _buffer = buffer;
     _cursor = 0;
     _timestamp = buffer.timestamp();
  
  }


  //void ByteBufferIterator::checkTimestamp() const {
  //  if (_timestamp != _buffer->timestamp()) {
  //    ILogger::instance()->logError("The buffer was changed after the iteration started");
  //  }
  //}
  
  public final boolean hasNext()
  {
    //checkTimestamp();
    if (_timestamp != _buffer.timestamp())
    {
      ILogger.instance().logError("The buffer was changed after the iteration started");
    }
  
    return (_cursor < _buffer.size());
  }

  public final byte nextUInt8()
  {
    //checkTimestamp();
    if (_timestamp != _buffer.timestamp())
    {
      ILogger.instance().logError("The buffer was changed after the iteration started");
    }
  
    if (_cursor >= _buffer.size())
    {
      ILogger.instance().logError("Iteration overflow");
      return 0;
    }
  
    return _buffer.get(_cursor++);
  }
  public final short nextInt16()
  {
    // LittleEndian
    final short b1 = (short) (nextUInt8() & 0xFF);
    final short b2 = (short) (nextUInt8() & 0xFF);
  
    final int iResult = (((int) b1) | ((int)(b2 << 8)));
    final short result = (short) iResult;
  //  if (result != -9999) {
  //    printf("break point on me\n");
  //  }
  
  //  if (result > 0) {
  //    printf("break point on me\n");
  //  }
    return result;
  }
  public final int nextInt32()
  {
    // LittleEndian
    final int b1 = nextUInt8() & 0xFF;
    final int b2 = nextUInt8() & 0xFF;
    final int b3 = nextUInt8() & 0xFF;
    final int b4 = nextUInt8() & 0xFF;
  
    return (((int) b1) | ((int) b2 << 8) | ((int) b3 << 16) | ((int) b4 << 24));
  }
  public final long nextInt64()
  {
    // LittleEndian
    final int b1 = nextUInt8() & 0xFF;
    final int b2 = nextUInt8() & 0xFF;
    final int b3 = nextUInt8() & 0xFF;
    final int b4 = nextUInt8() & 0xFF;
    final int b5 = nextUInt8() & 0xFF;
    final int b6 = nextUInt8() & 0xFF;
    final int b7 = nextUInt8() & 0xFF;
    final int b8 = nextUInt8() & 0xFF;
  
    return (((long) b1) | ((long) b2 << 8) | ((long) b3 << 16) | ((long) b4 << 24) | ((long) b5 << 32) | ((long) b6 << 40) | ((long) b7 << 48) | ((long) b8 << 56));
  }

  public final IByteBuffer nextBufferUpTo(byte sentinel)
  {
    ByteBufferBuilder builder = new ByteBufferBuilder();
  
    byte c;
  
    while ((c = nextUInt8()) != sentinel)
    {
      builder.add(c);
    }
  
    return builder.create();
  }

  public final String nextZeroTerminatedString()
  {
    IByteBuffer buffer = nextBufferUpTo((byte) 0);
    final String result = buffer.getAsString();
    if (buffer != null)
       buffer.dispose();
    return result;
  }

  public final double nextDouble()
  {
    final long l = nextInt64();
    return IMathUtils.instance().rawLongBitsToDouble(l);
  }

}