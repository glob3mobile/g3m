

#include "MathUtils_Emscripten.hpp"

#include <math.h>
#include <limits>

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
};


double MathUtils_Emscripten::sin(double v) const { return MathAux::sin_(v); }
float  MathUtils_Emscripten::sin(float v)  const { return sinf(v); }

double MathUtils_Emscripten::sinh(double v) const { return MathAux::sinh_(v); }
float  MathUtils_Emscripten::sinh(float v)  const { return sinhf(v); }

double MathUtils_Emscripten::asin(double v) const { return MathAux::asin_(v); }
float  MathUtils_Emscripten::asin(float v)  const { return asinf(v); }

double MathUtils_Emscripten::cos(double v) const { return MathAux::cos_(v); }
float  MathUtils_Emscripten::cos(float v)  const { return cosf(v); }

double MathUtils_Emscripten::acos(double v) const { return MathAux::acos_(v); }
float  MathUtils_Emscripten::acos(float v)  const { return acosf(v); }

double MathUtils_Emscripten::tan(double v) const { return MathAux::tan_(v); }
float  MathUtils_Emscripten::tan(float v)  const { return tanf(v); }

double MathUtils_Emscripten::atan(double v) const { return MathAux::atan_(v); }
float  MathUtils_Emscripten::atan(float v)  const { return atanf(v); }

double MathUtils_Emscripten::atan2(double u, double v) const { return MathAux::atan2_(u,v); }
float  MathUtils_Emscripten::atan2(float u, float v)   const { return atan2f(u,v);  }

long long MathUtils_Emscripten::round(double v) const { return (long long) MathAux::round_(v); }
int       MathUtils_Emscripten::round(float v)  const { return (int) roundf(v); }

int MathUtils_Emscripten::checkedRound(float v) const {
  const float roundedF = roundf(v);
  if (roundedF > maxInt32()) {
    return maxInt32();
  }
  else if (roundedF < minInt16()) {
    return minInt16();
  }
  else {
    return (int) roundedF;
  }
}

int    MathUtils_Emscripten::abs(int v)    const { return MathAux::abs_(v); }
double MathUtils_Emscripten::abs(double v) const { return fabs(v); }
float  MathUtils_Emscripten::abs(float v)  const { return fabsf(v); }

double MathUtils_Emscripten::sqrt(double v) const { return MathAux::sqrt_(v); }
float  MathUtils_Emscripten::sqrt(float v)  const { return sqrtf(v); }

double MathUtils_Emscripten::pow(double v, double u) const { return MathAux::pow_(v,u); }
float  MathUtils_Emscripten::pow(float v, float u)   const { return powf(v,u); }

double MathUtils_Emscripten::exp(double v) const { return MathAux::exp_(v); }
float  MathUtils_Emscripten::exp(float v)  const { return expf(v); }

double MathUtils_Emscripten::log10(double v) const { return MathAux::log10_(v);}
float  MathUtils_Emscripten::log10(float v)  const { return log10f(v); }

double MathUtils_Emscripten::log(double v) const { return MathAux::log_(v); }
float  MathUtils_Emscripten::log(float v)  const { return (float) log( (double) v); }

short MathUtils_Emscripten::maxInt16() const { return std::numeric_limits<short>::max(); }
short MathUtils_Emscripten::minInt16() const { return -(std::numeric_limits<short>::max()); }

int MathUtils_Emscripten::maxInt32() const { return std::numeric_limits<int>::max(); }
int MathUtils_Emscripten::minInt32() const { return -(std::numeric_limits<int>::max()); }

long long MathUtils_Emscripten::maxInt64() const { return std::numeric_limits<long long>::max(); }
long long MathUtils_Emscripten::minInt64() const { return -(std::numeric_limits<long long>::max()); }

double MathUtils_Emscripten::maxDouble() const { return std::numeric_limits<double>::max(); }
double MathUtils_Emscripten::minDouble() const { return -(std::numeric_limits<double>::max()); }

float MathUtils_Emscripten::maxFloat() const { return std::numeric_limits<float>::max(); }
float MathUtils_Emscripten::minFloat() const { return -(std::numeric_limits<float>::max()); }

int MathUtils_Emscripten::toInt(double value) const { return (int) value; }
int MathUtils_Emscripten::toInt(float value)  const { return (int) value; }

double MathUtils_Emscripten::min(double d1, double d2) const {
  return (d1 < d2) ? d1 : d2;
}

double MathUtils_Emscripten::max(double d1, double d2) const {
  return (d1 > d2) ? d1 : d2;
}

float MathUtils_Emscripten::min(float f1, float f2) const {
  return (f1 < f2) ? f1 : f2;
}

float MathUtils_Emscripten::max(float f1, float f2) const {
  return (f1 > f2) ? f1 : f2;
}

int MathUtils_Emscripten::max(int i1, int i2) const {
  return (i1 > i2) ? i1 : i2;
}

long long MathUtils_Emscripten::max(long long l1, long long l2) const {
  return (l1 > l2) ? l1 : l2;
}

long long MathUtils_Emscripten::doubleToRawLongBits(double value) const {
  union DoubleAndLong {
    double    _d;
    long long _l;
  } dal;
  
  dal._d = value;
  return dal._l;
}

float MathUtils_Emscripten::rawIntBitsToFloat(int value) const {
  union FloatAndInt {
    float _f;
    int   _i;
  } dal;
  
  dal._i = value;
  return dal._f;
}

double MathUtils_Emscripten::rawLongBitsToDouble(long long value) const {
  union DoubleAndLong {
    double    _d;
    long long _l;
  } dal;
  
  dal._l = value;
  return dal._d;
}

double MathUtils_Emscripten::floor(double d) const {
  return MathAux::floor_(d);
}

float MathUtils_Emscripten::floor(float f) const {
  return floorf(f);
}

double MathUtils_Emscripten::ceil(double d) const {
  return MathAux::ceil_(d);
}

float MathUtils_Emscripten::ceil(float f) const {
  return ceilf(f);
}

double MathUtils_Emscripten::mod(double d1, double d2) const {
  return fmod(d1, d2);
}

float MathUtils_Emscripten::mod(float f1, float f2) const {
  return fmodf(f1, f2);
}

double MathUtils_Emscripten::nextRandomDouble() const {
  return (double)rand() / RAND_MAX;
}

double MathUtils_Emscripten::copySign(double a, double b) const {
  return copysign(a, b);
}
