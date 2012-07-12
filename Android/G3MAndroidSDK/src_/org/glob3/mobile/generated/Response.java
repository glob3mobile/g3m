package org.glob3.mobile.generated; 
public class Response
{
  private Url _url = new Url();
  private ByteBuffer _data = new ByteBuffer();


  public Response(String url, ByteBuffer data)
  {
	  _url = new Url(url);
	  _data = new ByteBuffer(data);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Url getURL() const
  public final Url getURL()
  {
	  return _url;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: ByteBuffer getByteBuffer() const
  public final ByteBuffer getByteBuffer()
  {
	  return _data;
  }
}