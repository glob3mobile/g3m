//
//  MathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_MathUtils_hpp
#define G3MiOSSDK_MathUtils_hpp

#include <Math.h>
#include <limits>

#include "IMathUtils.hpp"

class MathUtils_iOS: public IMathUtils{
  
public:
  
  bool isNan(double v) const{ return isNan(v);}
  bool isNan(float v) const{ return isNan(v);}
  
  double NanD() const{ return NAN;}
  float NanF() const{ return NAN; }
  
  double sin(double v) const{ return sin(v); }
  float sin(float v) const{ return sin(v); }
  
  double cos(double v) const{ return cos(v); }
  float cos(float v) const{ return cos(v); }
  
  double tan(double v) const{ return tan(v); }
  float tan(float v) const{ return tan(v); }
  
  double atan(double v) const{ return atan(v); }
  float atan(float v) const{ return atan(v); }
  
  double atan2(double v) const{ return atan2(v); }
  float atan2(float v) const{ return atan2(v); }
  
  int round(double v) const{ return round(v); }
  int round(float v) const{ return round(v); }
  
  int abs(int v) const{ return abs(v); }
  double abs(double v) const { return abs(v); }
  float abs(float v) const { return abs(v); }
  
  double sqrt(double v) const{ return sqrt(v); }
  float sqrt(float v) const{ return sqrt(v); }
  
  double pow(double v, double u) const{ return pow(v,u); }
  float pow(float v, float u) const{ return pow(v,u); }
  
  double exp(double v) const{ return exp(v); }
  float exp(float v) const{ return exp(v); }
  
  int maxInt() const{ return std::numeric_limits<int>::max(); }
  int minInt() const{ return std::numeric_limits<int>::min(); }
  double maxDouble() const{ return std::numeric_limits<double>::max(); }
  double minDouble() const{ return std::numeric_limits<double>::min(); }
  float maxFloat() const{ return std::numeric_limits<float>::max(); }
  float minFloat() const{ return std::numeric_limits<float>::min(); }
};

#endif
