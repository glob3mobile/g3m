//
//  StringBuilder_iOS.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "StringBuilder_iOS.hpp"

std::string StringBuilder_iOS::stringFormat(std::string x, ...) const {
  va_list l;
  va_start(l, x);
  
  char buffer[2048];
  vsprintf(buffer, x.c_str(), l);
  
  va_end(l);
  return buffer;
}

std::string StringBuilder_iOS::stringFromUTF8(const unsigned char data[]) const{
  return (char*) data;
}