//
//  IMathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IMathUtils_hpp
#define G3MiOSSDK_IMathUtils_hpp

#include <string.h>
#include <cstdio>
#include "ILogger.hpp"

class IMathUtils{
  
  
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
  
  virtual ~IMathUtils(){}
  
  virtual double pi() const = 0;
  virtual double halfPi() const = 0;
  
  virtual bool isNan(double v) const = 0;
  virtual bool isNan(float v) const = 0;
  
  virtual double NanD() const = 0;
  virtual float NanF() const = 0;
  
  virtual double sin(double v) const = 0;
  virtual float sin(float v) const = 0;
  
  virtual double asin(double v) const = 0;
  virtual float asin(float v) const = 0;
  
  virtual double cos(double v) const = 0;
  virtual float cos(float v) const = 0;
  
  virtual double acos(double v) const = 0;
  virtual float acos(float v) const = 0;
  
  virtual double tan(double v) const = 0;
  virtual float tan(float v) const = 0;
  
  virtual double atan(double v) const = 0;
  virtual float atan(float v) const = 0;
  
  virtual double atan2(double u, double v) const = 0;
  virtual float atan2(float u, float v) const = 0;
  
  virtual double round(double v) const = 0;
  virtual float round(float v) const = 0;
  
  virtual int abs(int v) const = 0;
  virtual double abs(double v) const = 0;
  virtual float abs(float v) const = 0;
  
  virtual double sqrt(double v) const = 0;
  virtual float sqrt(float v) const = 0;
  
  virtual double pow(double v, double u) const = 0;
  virtual float pow(float v, float u) const = 0;
  
  virtual double exp(double v) const = 0;
  virtual float exp(float v) const = 0;
  
  virtual double log10(double v) const = 0;
  virtual float log10(float v) const = 0;
  
  virtual double log(double v) const = 0;
  virtual float log(float v) const = 0;
  
  virtual int maxInt32() const = 0;
  virtual int minInt32() const = 0;
  
  virtual long long maxInt64() const = 0;
  virtual long long minInt64() const = 0;

  virtual double maxDouble() const = 0;
  virtual double minDouble() const = 0;

  virtual float maxFloat() const = 0;
  virtual float minFloat() const = 0;
  
  virtual int toInt(double value) const = 0;
  virtual int toInt(float value) const = 0;

  virtual double min(double d1, double d2) const = 0;
  virtual double max(double d1, double d2) const = 0;

  virtual double lerp(double from,
                      double to,
                      double alpha) {
    return from + ((to - from) * alpha);
  }

  virtual float lerp(float from,
                     float to,
                     float alpha) {
    return from + ((to - from) * alpha);
  }

  virtual long long doubleToRawLongBits(double value) const = 0;
  virtual double rawLongBitsToDouble(long long value) const = 0;

};


#endif
