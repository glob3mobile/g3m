package org.glob3.mobile.generated; 
public class IByteBufferResult extends Disposable
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
    JAVA_POST_DISPOSE
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