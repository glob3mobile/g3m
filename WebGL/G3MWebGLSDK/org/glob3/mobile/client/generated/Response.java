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
  private URL _url = new URL();     //Converter puts "Url"
  private final ByteArrayWrapper _data;



  public Response(URL url, ByteArrayWrapper data)
  {
	  _url = url;
	  _data = data;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: URL getURL() const
  public final URL getURL()
  {
	return _url;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ByteArrayWrapper* getByteArrayWrapper() const
  public final ByteArrayWrapper getByteArrayWrapper()
  {
	return _data;
  }

}