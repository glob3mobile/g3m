//
//  Response.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 28/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Response_h
#define G3MiOSSDK_Response_h

#include "ByteBuffer.hpp"

class Url{
  const std::string _path;
public:  
  Url(std::string f): _path(f){};
  std::string getPath() const{ return _path;}
};

class Response{
private:
  const Url _url;
  ByteBuffer _data;
  
public:
  
  Response(std::string url, ByteBuffer data): _url(url), _data(data){}
  
  Url getURL() const{ return _url;}
  ByteBuffer getByteBuffer() const{ return _data;}
};

#endif
