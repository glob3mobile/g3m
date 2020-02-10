

#include "StringBuilder_Emscripten.hpp"

IStringBuilder* StringBuilder_Emscripten::clone(const int floatPrecision) const {
  return new StringBuilder_Emscripten(floatPrecision);
}

StringBuilder_Emscripten::StringBuilder_Emscripten(const int floatPrecision) {
  _oss.precision(floatPrecision);
}

IStringBuilder* StringBuilder_Emscripten::addBool(bool b) {
  _oss << (b ? "true" : "false");
  return this;
}

IStringBuilder* StringBuilder_Emscripten::addDouble(double d) {
  _oss << d;
  return this;
}

IStringBuilder* StringBuilder_Emscripten::addFloat(float f) {
  _oss << f;
  return this;
}

IStringBuilder* StringBuilder_Emscripten::addInt(int i) {
  _oss << i;
  return this;
}

IStringBuilder* StringBuilder_Emscripten::addLong(long long l) {
  _oss << l;
  return this;
}

IStringBuilder* StringBuilder_Emscripten::addString(const std::string& s) {
  _oss << s;
  return this;
}

const std::string StringBuilder_Emscripten::getString() const {
  return _oss.str();
}

IStringBuilder* StringBuilder_Emscripten::clear(const int floatPrecision) {
  _oss.str(std::string());
  _oss.precision(floatPrecision);
  return this;
}

bool StringBuilder_Emscripten::contentEqualsTo(const std::string& that) const {
  return _oss.str() == that;
}
