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
#include "ByteBuffer.hpp"

class Response {
private:
  const URL         _url;
  const ByteBuffer* _data;

#ifdef C_CODE
  Response& operator=(const Response& that);
  
  Response(const Response& that);
#endif
  
public:
  
  Response(const URL& url,
           ByteBuffer* data):
  _url(url),
  _data(data)
  {
  }
  
  URL getURL() const {
    return _url;
  }
  
  const ByteBuffer* getByteBuffer() const {
    return _data;
  }
  
};

#endif
