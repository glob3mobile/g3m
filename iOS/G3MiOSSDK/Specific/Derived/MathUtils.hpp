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

#include "IMathUtils.hpp"

class MathUtils{
  
  bool isNan(double v) const{ return isNan(v);}
  bool isNan(float v) const{ return isNan(v);}
  
  double NanD() const{ return NAN;}
  float NanF() const{ return NAN; }
  
  double sin(double v) const{ return sin(v); }
  float sin(float v) const{ return sin(v); }
  
  double cos(double v) const = 0;
  float cos(float v) const = 0;
  
  double tan(double v) const = 0;
  float tan(float v) const = 0;
  
  double atan(double v) const = 0;
  float atan(float v) const = 0;
  
  double atan2(double v) const = 0;
  float atan2(float v) const = 0;
  
  int round(double v) const = 0;
  int round(float v) const = 0;
  
  int abs(int v) const = 0;
  double abs(double v) const = 0;
  float abs(float v) const = 0;
  
  double sqrt(double v) const = 0;
  float sqrt(float v) const = 0;
  
  double pow(double v, double u) const = 0;
  float pow(float v, float u) const = 0;
  
  double exp(double v) const = 0;
  float exp(float v) const = 0;
  
  double maxDouble() const = 0;
  double minDouble() const = 0;
  float maxFloat() const = 0;
  float minFloat() const = 0;
};

#endif
