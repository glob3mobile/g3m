//
//  IStringBuilder.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/08/12.
//

#ifndef G3MiOSSDK_IStringBuilder
#define G3MiOSSDK_IStringBuilder

#include <string>

class IStringBuilder {
private:

  static IStringBuilder* _exemplar;

protected:

  virtual IStringBuilder* clone(const int floatPrecision) const = 0;

public:
  static const int DEFAULT_FLOAT_PRECISION;

  static void setInstance(IStringBuilder* exemplar);

  static IStringBuilder* newStringBuilder(const int floatPrecision = DEFAULT_FLOAT_PRECISION);

  virtual IStringBuilder* addDouble(double d) = 0;
  virtual IStringBuilder* addFloat(float f) = 0;

  virtual IStringBuilder* addInt(int i) = 0;
  virtual IStringBuilder* addLong(long long l) = 0;

  virtual IStringBuilder* addString(const std::string& s) = 0;
  virtual IStringBuilder* addBool(bool b) = 0;

  virtual IStringBuilder* clear() {
    return clear(DEFAULT_FLOAT_PRECISION);
  }

  virtual IStringBuilder* clear(const int floatPrecision) = 0;

  virtual const std::string getString() const = 0;

  // a virtual destructor is needed for conversion to Java
  virtual ~IStringBuilder() {
  }

  virtual bool contentEqualsTo(const std::string& that) const = 0;
  
};

#endif
