//
//  Response.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Response_h
#define G3MiOSSDK_Response_h

#include "URL.hpp"
#include "ByteArrayWrapper.hpp"

class Response {
private:
#ifdef C_CODE
  const URL         _url;
#endif
#ifdef JAVA_CODE
  final private URL _url;     //Converter puts "Url"
#endif
  const ByteArrayWrapper* _data;

#ifdef C_CODE
  Response& operator=(const Response& that);
  
  Response(const Response& that);
#endif
  
public:
  
  Response(const URL& url,
           const ByteArrayWrapper* data):
  _url(url),
  _data(data)
  {
  }
  
  URL getURL() const {
    return _url;
  }
  
  const ByteArrayWrapper* getByteArrayWrapper() const {
    return _data;
  }
  
};

#endif
