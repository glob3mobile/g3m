package org.glob3.mobile.generated; 
//
//  Response.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class Response
{
  private Url _url = new Url();
  private final ByteBuffer _data;



  public Response(Url url, ByteBuffer data)
  {
	  _url = new Url(url);
	  _data = data;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Url getURL() const
  public final Url getURL()
  {
	return _url;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ByteBuffer* getByteBuffer() const
  public final ByteBuffer getByteBuffer()
  {
	return _data;
  }

}