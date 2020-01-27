
#ifndef StringBuilder_Emscripten_hpp
#define StringBuilder_Emscripten_hpp

#include "G3M/IStringBuilder.hpp"

#include <sstream>


class StringBuilder_Emscripten : public IStringBuilder {
private:
  std::ostringstream _oss;

protected:

  IStringBuilder* clone(const int floatPrecision) const;

public:

  StringBuilder_Emscripten(const int floatPrecision);

  IStringBuilder* addBool(bool b);

  IStringBuilder* addDouble(double d);

  IStringBuilder* addFloat(float f);

  IStringBuilder* addInt(int i);

  IStringBuilder* addLong(long long l);

  IStringBuilder* addString(const std::string& s);

  const std::string getString() const;

  IStringBuilder* clear(const int floatPrecision);

  bool contentEqualsTo(const std::string& that) const;
};

#endif
