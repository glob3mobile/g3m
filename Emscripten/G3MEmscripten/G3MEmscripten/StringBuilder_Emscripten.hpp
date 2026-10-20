
#ifndef StringBuilder_Emscripten_hpp
#define StringBuilder_Emscripten_hpp

#include "G3M/IStringBuilder.hpp"

#include <sstream>
#include <string>


class StringBuilder_Emscripten : public IStringBuilder {
private:
  std::ostringstream _oss;
  int                _floatPrecision;

  static std::string format(double d, int maxDecimals);

  static void toPrecision20(double d, std::string& digits, int& scale);

  static std::string mantissaDigits(const char* scientific);

  static bool propagateCarry(std::string& digits, int i);

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
