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


class File{
  const std::string _path;
public:
  File(std::string f): _path(f){};
  std::string getPath() const{ return _path;}
};

class Url{
  const std::string _path;
public:  
  Url(std::string f): _path(f){};
  std::string getPath() const{ return _path;}
};

class Response{
private:
  const File _file;
  const Url _url;
  ByteBuffer _data;
  
public:
  
  Response(std::string file, std::string url, ByteBuffer data): _file(file), _url(url), _data(data){}
  
  Url getURL() const{ return _url;}
  ByteBuffer getByteBuffer() const{ return _data;}
};

#endif
