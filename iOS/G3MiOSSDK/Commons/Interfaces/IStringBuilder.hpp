//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IStringBuilder_hpp
#define G3MiOSSDK_IStringBuilder_hpp

#include <string>

class IStringBuilder{
  
  static IStringBuilder* _instance;
  
public:
  static void setInstance(IStringBuilder* isb);
  
  static IStringBuilder* instance();
  
  virtual std::string stringFromUTF8(const unsigned char data[]) const = 0;
  
  virtual std::string stringFormat(std::string x, ...) const = 0;
#ifdef C_CODE
  // a virtual destructor is needed for conversion to Java
  virtual ~IStringBuilder() {}
#endif
  
};


#endif
