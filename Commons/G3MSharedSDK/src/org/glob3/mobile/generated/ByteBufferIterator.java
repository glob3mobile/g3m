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


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IByteBuffer;


public class ByteBufferIterator
{
  private IByteBuffer _buffer;
  private int _cursor;
  private int _timestamp;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: void checkTimestamp() const
  private void checkTimestamp()
  {
	if (_timestamp != _buffer.timestamp())
	{
	  ILogger.instance().logError("The buffer was changed after the iteration started");
	}
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ByteBufferIterator(ByteBufferIterator that);

  public ByteBufferIterator(IByteBuffer buffer)
  {
	  _buffer = buffer;
	  _cursor = 0;
	  _timestamp = buffer.timestamp();
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasNext() const
  public final boolean hasNext()
  {
	checkTimestamp();
  
	return (_cursor < _buffer.size());
  }

  public final byte nextUInt8()
  {
	checkTimestamp();
  
	if (_cursor >= _buffer.size())
	{
	  ILogger.instance().logError("Iteration overflow");
	  return 0;
	}
  
	return _buffer.get(_cursor++);
  }
  public final int nextInt32()
  {
	// LittleEndian
	int b1 = nextUInt8() & 0xFF;
	int b2 = nextUInt8() & 0xFF;
	int b3 = nextUInt8() & 0xFF;
	int b4 = nextUInt8() & 0xFF;

	return ((int) b1) | ((int) b2 << 8) | ((int) b3 << 16) | ((int) b4 << 24);
  }
  public final long nextInt64()
  {
	// LittleEndian
	int b1 = nextUInt8() & 0xFF;
	int b2 = nextUInt8() & 0xFF;
	int b3 = nextUInt8() & 0xFF;
	int b4 = nextUInt8() & 0xFF;
	int b5 = nextUInt8() & 0xFF;
	int b6 = nextUInt8() & 0xFF;
	int b7 = nextUInt8() & 0xFF;
	int b8 = nextUInt8() & 0xFF;
  
	return ((long) b1) | ((long) b2 << 8) | ((long) b3 << 16) | ((long) b4 << 24) | ((long) b5 << 32) | ((long) b6 << 40) | ((long) b7 << 48) | ((long) b8 << 56);
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