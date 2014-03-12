//
//  StringBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_StringBuilder_iOS
#define G3MiOSSDK_StringBuilder_iOS

#include "IStringBuilder.hpp"

#include <sstream>

class StringBuilder_iOS: public IStringBuilder {
private:
  std::ostringstream _oss;

protected:

  IStringBuilder* getNewInstance() const {
    return new StringBuilder_iOS();
  }

public:
  
  StringBuilder_iOS() {
    _oss.precision(20);
  }

  IStringBuilder* addBool(bool b) {
    _oss << (b ? "true" : "false");
    return this;
  }

  IStringBuilder* addDouble(double d) {
    _oss << d;
    return this;
  }

  IStringBuilder* addFloat(float f) {
    _oss << f;
    return this;
  }

  IStringBuilder* addInt(int i) {
    _oss << i;
    return this;
  }

  IStringBuilder* addLong(long long l) {
    _oss << l;
    return this;
  }

  IStringBuilder* addString(const std::string& s) {
    _oss << s;
    return this;
  }

  const std::string getString() const {
    return _oss.str();
  }

};

#endif
