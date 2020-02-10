//
//  StringBuilder_iOS.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//

#ifndef G3MiOSSDK_StringBuilder_iOS
#define G3MiOSSDK_StringBuilder_iOS

#include "G3M/IStringBuilder.hpp"

#include <sstream>

class StringBuilder_iOS: public IStringBuilder {
private:
  std::ostringstream _oss;

protected:

  IStringBuilder* clone(const int floatPrecision) const {
    return new StringBuilder_iOS(floatPrecision);
  }

public:

  StringBuilder_iOS(const int floatPrecision) {
    _oss.precision(floatPrecision);
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

  IStringBuilder* clear(const int floatPrecision) {
    _oss.str(std::string());
    _oss.precision(floatPrecision);
    return this;
  }

  bool contentEqualsTo(const std::string& that) const {
    return _oss.str() == that;
  }

};

#endif
