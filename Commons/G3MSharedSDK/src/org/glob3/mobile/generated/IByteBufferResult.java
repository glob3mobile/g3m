package org.glob3.mobile.generated;import java.util.*;

public class IByteBufferResult
{
  private IByteBuffer _buffer;
  private final boolean _expired;

  public IByteBufferResult(IByteBuffer buffer, boolean expired)
  {
	  _buffer = buffer;
	  _expired = expired;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IByteBuffer* getBuffer() const
  public final IByteBuffer getBuffer()
  {
	return _buffer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isExpired() const
  public final boolean isExpired()
  {
	return _expired;
  }
}
