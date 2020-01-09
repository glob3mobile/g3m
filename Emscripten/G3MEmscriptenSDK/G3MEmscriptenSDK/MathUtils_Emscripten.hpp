
#ifndef MathUtils_Emscripten_hpp
#define MathUtils_Emscripten_hpp

#include "IMathUtils.hpp"

class MathUtils_Emscripten : public IMathUtils {

public:

  double sin(double v) const;
  float  sin(float v)  const;

  double sinh(double v) const;
  float  sinh(float v)  const;

  double asin(double v) const;
  float  asin(float v)  const;

  double cos(double v) const;
  float  cos(float v)  const;

  double acos(double v) const;
  float  acos(float v)  const;

  double tan(double v) const;
  float  tan(float v)  const;

  double atan(double v) const;
  float  atan(float v)  const;

  double atan2(double u, double v) const;
  float  atan2(float u, float v)   const;

  long long round(double v) const;
  int       round(float v)  const;

  int checkedRound(float v) const;

  int    abs(int v)    const;
  double abs(double v) const;
  float  abs(float v)  const;

  double sqrt(double v) const;
  float  sqrt(float v)  const;

  double pow(double v, double u) const;
  float  pow(float v, float u)   const;

  double exp(double v) const;
  float  exp(float v)  const;

  double log10(double v) const;
  float  log10(float v)  const;

  double log(double v) const;
  float  log(float v)  const;

  short maxInt16() const;
  short minInt16() const;

  int maxInt32() const;
  int minInt32() const;

  long long maxInt64() const;
  long long minInt64() const;

  double maxDouble() const;
  double minDouble() const;

  float maxFloat() const;
  float minFloat() const;

  int toInt(double value) const;
  int toInt(float value)  const;

  double min(double d1, double d2) const;
  double max(double d1, double d2) const;

  float min(float f1, float f2) const;
  float max(float f1, float f2) const;

  int max(int i1, int i2) const;

  long long max(long long l1, long long l2) const;

  long long doubleToRawLongBits(double value) const;

  float rawIntBitsToFloat(int value) const;

  double rawLongBitsToDouble(long long value) const;

  double floor(double d) const;
  float floor(float f) const;

  double ceil(double d) const;
  float ceil(float f) const;

  double mod(double d1, double d2) const;
  float mod(float f1, float f2) const;

  double nextRandomDouble() const;

  double copySign(double a, double b) const;

};

#endif
