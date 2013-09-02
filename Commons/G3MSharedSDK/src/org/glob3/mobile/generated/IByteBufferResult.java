package org.glob3.mobile.generated; 
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

  public final IByteBuffer getBuffer()
  {
    return _buffer;
  }

  public final boolean isExpired()
  {
    return _expired;
  }
}