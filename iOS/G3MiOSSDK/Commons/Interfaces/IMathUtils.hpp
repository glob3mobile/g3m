//
//  IMathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IMathUtils
#define G3MiOSSDK_IMathUtils

#include <string>
#include <cstdio>
#include "ILogger.hpp"

#ifdef C_CODE

#include <math.h>
#define SIN(x) sin(x)
#define COS(x) cos(x)
#define TAN(x) tan(x)
#define NAND   NAN
#define NANF   NAN

#else

#define SIN(x) java.lang.Math.sin(x)
#define COS(x) java.lang.Math.cos(x)
#define TAN(x) java.lang.Math.tan(x)
#define NAND   java.lang.Double.NaN
#define NANF   java.lang.Float.NaN

#endif

#define PI       3.14159265358979323846264338327950288
#define HALF_PI  1.57079632679489661923132169163975144

#define ISNAN(x) (x != x)

class IMathUtils {
private:
  static IMathUtils* _instance;

public:
  static void setInstance(IMathUtils* math) {
    if (_instance != NULL) {
      ILogger::instance()->logWarning("IMathUtils instance already set!");
      delete _instance;
    }
    _instance = math;
  }

  static IMathUtils* instance() {
    return _instance;
  }

  virtual ~IMathUtils() {
  }

//  virtual double NanD() const = 0;
//  virtual float  NanF() const = 0;

  virtual double sin(double v) const = 0;
  virtual float  sin(float v)  const = 0;

  virtual double sinh(double v) const = 0;
  virtual float  sinh(float v)  const = 0;

  virtual double asin(double v) const = 0;
  virtual float  asin(float v)  const = 0;

  virtual double cos(double v) const = 0;
  virtual float  cos(float v)  const = 0;

  virtual double acos(double v) const = 0;
  virtual float  acos(float v)  const = 0;

  virtual double tan(double v) const = 0;
  virtual float  tan(float v)  const = 0;

  virtual double atan(double v) const = 0;
  virtual float  atan(float v)  const = 0;

  virtual double atan2(double u, double v) const = 0;
  virtual float  atan2(float u,  float v)  const = 0;

  virtual long long round(double v) const = 0;
  virtual int       round(float v)  const = 0;

  virtual int    abs(int v)    const = 0;
  virtual double abs(double v) const = 0;
  virtual float  abs(float v)  const = 0;

  virtual double sqrt(double v) const = 0;
  virtual float  sqrt(float v)  const = 0;

  virtual double pow(double v, double u) const = 0;
  virtual float  pow(float v, float u)  const = 0;

  virtual double exp(double v) const = 0;
  virtual float  exp(float v)  const = 0;

  virtual double log10(double v) const = 0;
  virtual float  log10(float v)  const = 0;

  virtual double log(double v) const = 0;
  virtual float  log(float v)  const = 0;

  virtual short maxInt16() const = 0;
  virtual short minInt16() const = 0;

  virtual int maxInt32() const = 0;
  virtual int minInt32() const = 0;

  virtual long long maxInt64() const = 0;
  virtual long long minInt64() const = 0;

  virtual double maxDouble() const = 0;
  virtual double minDouble() const = 0;

  virtual float maxFloat() const = 0;
  virtual float minFloat() const = 0;

  virtual int toInt(double value) const = 0;
  virtual int toInt(float value)  const = 0;

  virtual double min(double d1, double d2) const = 0;
  virtual float  min(float f1,  float f2)  const = 0;

  virtual double max(double d1, double d2) const = 0;
  virtual float  max(float f1,  float f2)  const = 0;

  virtual int max(int i1, int i2) const = 0;
  virtual long long max(long long l1, long long l2) const = 0;

  virtual double max(double d1, double d2, double d3) const {
    return max(max(d1, d2), d3);
  }

  virtual float max(float f1, float f2, float f3)  const {
    return max(max(f1, f2), f3);
  }

  virtual float min(float f1, float f2, float f3)  const {
    return min(min(f1, f2), f3);
  }

  virtual double floor(double d) const = 0;
  virtual float  floor(float f)  const = 0;

  virtual double ceil(double d) const = 0;
  virtual float  ceil(float f)  const = 0;
  
  virtual double fmod(double d1, double d2) const = 0;
  virtual float  fmod(float f1, float f2)  const = 0;

  virtual double linearInterpolation(double from,
                                     double to,
                                     double alpha) const {
    return from + ((to - from) * alpha);
  }

  virtual float linearInterpolation(float from,
                                    float to,
                                    float alpha) const {
    return from + ((to - from) * alpha);
  }


  virtual double quadraticBezierInterpolation(double from,
                                              double middle,
                                              double to,
                                              double alpha) const {
    const double oneMinusAlpha = 1.0 - alpha;
    return ( oneMinusAlpha*oneMinusAlpha * from ) + ( 2.0*oneMinusAlpha*alpha * middle ) + ( alpha*alpha * to );
  }

  virtual float quadraticBezierInterpolation(float from,
                                             float middle,
                                             float to,
                                             float alpha) const {
    const float oneMinusAlpha = 1.0f - alpha;
    return ( oneMinusAlpha*oneMinusAlpha * from ) + ( 2.0f*oneMinusAlpha*alpha * middle ) + ( alpha*alpha * to );
  }


  virtual long long doubleToRawLongBits(double value) const = 0;
  virtual double rawLongBitsToDouble(long long value) const = 0;

  virtual double clamp(double value,
                       double min,
                       double max) const {
    if (value < min) return min;
    if (value > max) return max;
    return value;
  }

  virtual float clamp(float value,
                      float min,
                      float max) const {
    if (value < min) return min;
    if (value > max) return max;
    return value;
  }

  virtual bool isEquals(double x, double y) const {
    if (x == y) {
      return true;
    }
    const double epsilon = 1e-8;
    return abs(x - y) <= epsilon * max(abs(x), abs(y), 1.0);
  }

  virtual bool isEquals(float x, float y) const {
    if (x == y) {
      return true;
    }
    const float epsilon = 1e-8f;
    return abs(x - y) <= epsilon * max(abs(x), abs(y), 1.0f);
  }

  virtual bool isBetween(float value,
                         float min,
                         float max) const {
    return (value >= min) && (value <= max);
  }

  virtual double pseudoModule(double numerator,
                              double denominator) const {

    const double result = numerator / denominator;
    const long long intPart = (long long) result; // integer part
    const double fracPart = result - intPart;     // fractional part

//    if (closeTo(fracPart, 1.0)) {
    if (fracPart == 1.0) {
      return 0;
    }

    return fracPart * denominator;
  }


};

#endif
