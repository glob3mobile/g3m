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

#include <sstream>

class StringBuilder_iOS: public IStringBuilder {
  
  std::ostringstream _oss;
  
protected:
  
  
  IStringBuilder* getNewInstance() const{
    return new StringBuilder_iOS();
  }
  
public:
  
  IStringBuilder* addBool(bool b){
    _oss << b;
    return this;
  }
  
  IStringBuilder* add(double d){
    _oss << d;
    return this;
  }
  
  IStringBuilder* add(const std::string& s){
    _oss << s;
    return this;
  }
  
  std::string getString() const{
    return _oss.str();
  }
};

#endif
