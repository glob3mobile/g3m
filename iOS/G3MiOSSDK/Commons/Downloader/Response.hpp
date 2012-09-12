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
//#include "ByteArrayWrapper.hpp"
#include "IByteBuffer.hpp"

class OLDResponse {
private:
#ifdef C_CODE
  const URL         _url;
#endif
#ifdef JAVA_CODE
  final private URL _url;     //Converter puts "Url"
#endif
  const IByteBuffer* _data;
  
#ifdef C_CODE
  OLDResponse& operator=(const OLDResponse& that);
  
  OLDResponse(const OLDResponse& that);
#endif
  
public:
  
  OLDResponse(const URL& url,
              const IByteBuffer* data):
  _url(url),
  _data(data)
  {
    int _____REMOVE_OLDResponse;
  }
  
  URL getURL() const {
    return _url;
  }
  
  const IByteBuffer* getByteArrayWrapper() const {
    return _data;
  }
  
};

#endif
