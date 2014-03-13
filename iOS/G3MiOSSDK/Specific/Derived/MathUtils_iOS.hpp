//
//  MathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_MathUtils
#define G3MiOSSDK_MathUtils

#include <math.h>
#include <limits>

#include "IMathUtils.hpp"

//Class used to avoid method names collision with Math.h
class MathAux {
public:
  static inline double sin_  (double v)           { return sin(v);     }
  static inline double sinh_ (double v)           { return sinh(v);    }
  static inline double asin_ (double v)           { return asin(v);    }
  static inline double cos_  (double v)           { return cos(v);     }
  static inline double acos_ (double v)           { return acos(v);    }
  static inline double tan_  (double v)           { return tan(v);     }
  static inline double atan_ (double v)           { return atan(v);    }
  static inline double atan2_(double u, double v) { return atan2(u,v); }
  static inline double round_(double v)           { return round(v);   }
  static inline int    abs_  (int v)              { return abs(v);     }
  static inline double round_(int v)              { return round(v);   }
  static inline double sqrt_ (double v)           { return sqrt(v);    }
  static inline double pow_  (double v, double u) { return pow(v,u);   }
  static inline double exp_  (double v)           { return exp(v);     }
  static inline double log10_(double v)           { return log10(v);   }
  static inline double log_  (double v)           { return log(v);     }
  static inline double floor_(double d)           { return floor(d);   }
  static inline double ceil_ (double d)           { return ceil(d);    }
  static inline double fmod_ (double d1, double d2) { return fmod(d1, d2);}
};

class MathUtils_iOS: public IMathUtils {

public:

//  double NanD() const { return NAN;}
//  float NanF() const { return NAN; }

  double sin(double v) const { return MathAux::sin_(v); }
  float  sin(float v)  const { return sinf(v); }

  double sinh(double v) const { return MathAux::sinh_(v); }
  float  sinh(float v)  const { return sinhf(v); }

  double asin(double v) const { return MathAux::asin_(v); }
  float  asin(float v)  const { return asinf(v); }

  double cos(double v) const { return MathAux::cos_(v); }
  float  cos(float v)  const { return cosf(v); }

  double acos(double v) const { return MathAux::acos_(v); }
  float  acos(float v)  const { return acosf(v); }

  double tan(double v) const { return MathAux::tan_(v); }
  float  tan(float v)  const { return tanf(v); }

  double atan(double v) const { return MathAux::atan_(v); }
  float  atan(float v)  const { return atanf(v); }

  double atan2(double u, double v) const { return MathAux::atan2_(u,v); }
  float  atan2(float u, float v)   const { return atan2f(u,v);  }

  long long round(double v) const { return (long long) MathAux::round_(v); }
  int       round(float v)  const { return (int) roundf(v); }

  int    abs(int v)    const { return MathAux::abs_(v); }
  double abs(double v) const { return fabs(v); }
  float  abs(float v)  const { return fabsf(v); }

  double sqrt(double v) const { return MathAux::sqrt_(v); }
  float  sqrt(float v)  const { return sqrtf(v); }

  double pow(double v, double u) const { return MathAux::pow_(v,u); }
  float  pow(float v, float u)   const { return powf(v,u); }

  double exp(double v) const { return MathAux::exp_(v); }
  float  exp(float v)  const { return expf(v); }

  double log10(double v) const { return MathAux::log10_(v);}
  float  log10(float v)  const { return log10f(v); }

  double log(double v) const { return MathAux::log_(v); }
  float  log(float v)  const { return log(v); }

  short maxInt16() const { return std::numeric_limits<short>::max(); }
  short minInt16() const { return -(std::numeric_limits<short>::max()); }

  int maxInt32() const { return std::numeric_limits<int>::max(); }
  int minInt32() const { return -(std::numeric_limits<int>::max()); }

  long long maxInt64() const { return std::numeric_limits<long long>::max(); }
  long long minInt64() const { return -(std::numeric_limits<long long>::max()); }

  double maxDouble() const { return std::numeric_limits<double>::max(); }
  double minDouble() const { return -(std::numeric_limits<double>::max()); }

  float maxFloat() const { return std::numeric_limits<float>::max(); }
  float minFloat() const { return -(std::numeric_limits<float>::max()); }

  int toInt(double value) const { return (int) value; }
  int toInt(float value) const { return (int) value; }


  double min(double d1, double d2) const {
    return (d1 < d2) ? d1 : d2;
  }

  double max(double d1, double d2) const {
    return (d1 > d2) ? d1 : d2;
  }

  float min(float f1, float f2) const {
    return (f1 < f2) ? f1 : f2;
  }

  float max(float f1, float f2) const {
    return (f1 > f2) ? f1 : f2;
  }

  int max(int i1, int i2) const {
    return (i1 > i2) ? i1 : i2;
  }

  long long max(long long l1, long long l2) const {
    return (l1 > l2) ? l1 : l2;
  }

  long long doubleToRawLongBits(double value) const {
    union DoubleAndLong {
      double    _d;
      long long _l;
    } dal;

    dal._d = value;
    return dal._l;
  }

  double rawLongBitsToDouble(long long value) const {
    union DoubleAndLong {
      double    _d;
      long long _l;
    } dal;

    dal._l = value;
    return dal._d;
  }

  double floor(double d) const {
    return MathAux::floor_(d);
  }

  float floor(float f) const {
    return floorf(f);
  }

  double ceil(double d) const {
    return MathAux::ceil_(d);
  }

  float ceil(float f) const {
    return ceilf(f);
  }
  
  double fmod(double d1, double d2) const {
    return MathAux::fmod_(d1, d2);
  }
  
  float fmod(float f1, float f2) const {
    return fmodf(f1, f2);
  }
  
};

#endif
