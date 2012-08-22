//
//  StringBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_StringBuilder_iOS_hpp
#define G3MiOSSDK_StringBuilder_iOS_hpp

#include "IStringBuilder.hpp"

class StringBuilder_iOS: public IStringBuilder{
  
  std::string stringFromUTF8(const unsigned char data[]) const;
  
  std::string stringFormat(std::string x, ...) const;
  
};

#endif
