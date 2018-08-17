package org.glob3.mobile.generated;//
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
  private final IByteBuffer _buffer;
  private int _cursor;
  private int _bufferTimestamp;
  private int _bufferSize;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  ByteBufferIterator(ByteBufferIterator that);

  public ByteBufferIterator(IByteBuffer buffer)
  {
	  _buffer = buffer;
	  _cursor = 0;
	  _bufferTimestamp = buffer.timestamp();
	  _bufferSize = buffer.size();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean hasNext() const
  public final boolean hasNext()
  {
	if (_bufferTimestamp != _buffer.timestamp())
	{
	  THROW_EXCEPTION("The buffer was changed after the iteration started");
	  // _bufferSize = _buffer->size();
	}
  
	return (_cursor < _bufferSize);
  }

  public final byte nextUInt8()
  {
	if (_bufferTimestamp != _buffer.timestamp())
	{
	  THROW_EXCEPTION("The buffer was changed after the iteration started");
	  //_bufferSize = _buffer->size();
	}
  
	if (_cursor >= _bufferSize)
	{
	  THROW_EXCEPTION("Iteration overflow");
	  // return 0;
	}
  
	return _buffer.get(_cursor++);
  }
  public final short nextInt16()
  {
	// LittleEndian
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final byte b1 = nextUInt8();
	final byte b2 = nextUInt8();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final short b1 = (short)(nextUInt8() & 0xFF);
	final short b2 = (short)(nextUInt8() & 0xFF);
//#endif
  
	final int iResult = (((int) b1) | ((int)(b2 << 8)));
	return (short) iResult;
  }
  public final int nextInt32()
  {
	// LittleEndian
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final byte b1 = nextUInt8();
	final byte b2 = nextUInt8();
	final byte b3 = nextUInt8();
	final byte b4 = nextUInt8();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int b1 = nextUInt8() & 0xFF;
	final int b2 = nextUInt8() & 0xFF;
	final int b3 = nextUInt8() & 0xFF;
	final int b4 = nextUInt8() & 0xFF;
//#endif
  
	return (((int) b1) | ((int) b2 << 8) | ((int) b3 << 16) | ((int) b4 << 24));
  }
  public final long nextInt64()
  {
	// LittleEndian
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	final byte b1 = nextUInt8();
	final byte b2 = nextUInt8();
	final byte b3 = nextUInt8();
	final byte b4 = nextUInt8();
	final byte b5 = nextUInt8();
	final byte b6 = nextUInt8();
	final byte b7 = nextUInt8();
	final byte b8 = nextUInt8();
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	final int b1 = nextUInt8() & 0xFF;
	final int b2 = nextUInt8() & 0xFF;
	final int b3 = nextUInt8() & 0xFF;
	final int b4 = nextUInt8() & 0xFF;
	final int b5 = nextUInt8() & 0xFF;
	final int b6 = nextUInt8() & 0xFF;
	final int b7 = nextUInt8() & 0xFF;
	final int b8 = nextUInt8() & 0xFF;
//#endif
  
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
  public final float nextFloat()
  {
	final int i = nextInt32();
	return IMathUtils.instance().rawIntBitsToFloat(i);
  }

  public final void nextUInt8(int count, byte[] dst)
  {
	for (int i = 0; i < count; i++)
	{
	  dst[i] = nextUInt8();
	}
  }
  public final void nextInt16(int count, short[] dst)
  {
	for (int i = 0; i < count; i++)
	{
	  dst[i] = nextInt16();
	}
  }
  public final void nextInt32(int count, int[] dst)
  {
	for (int i = 0; i < count; i++)
	{
	  dst[i] = nextInt32();
	}
  }

}
