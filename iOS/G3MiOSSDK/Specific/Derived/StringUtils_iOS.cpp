//
//  StringUtils_iOS.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

#include "StringUtils_iOS.hpp"

std::string StringUtils_iOS::createString(unsigned char* data,
                                          int            length) const {
  
#ifdef C_CODE
  unsigned char* cStr = new unsigned char[length + 1];
  memcpy(cStr, data, length * sizeof(unsigned char));
  cStr[length] = 0;
  
  return (char*) cStr;
#endif
  
#ifdef JAVA_CODE
  __TODO_create_an_string;
#endif
  
}
