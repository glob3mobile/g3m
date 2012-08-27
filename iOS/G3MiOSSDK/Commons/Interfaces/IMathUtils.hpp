//
//  IMathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IMathUtils_hpp
#define G3MiOSSDK_IMathUtils_hpp

class IMathUtils{
  
public:
  
  virtual ~IMathUtils(){}
  
  virtual bool isNan(double v) const = 0;
  virtual bool isNan(float v) const = 0;
  
  virtual double NanD() const = 0;
  virtual float NanF() const = 0;
  
  virtual double sin(double v) const = 0;
  virtual float sin(float v) const = 0;
  
  virtual double cos(double v) const = 0;
  virtual float cos(float v) const = 0;
  
  virtual double tan(double v) const = 0;
  virtual float tan(float v) const = 0;
  
  virtual double atan(double v) const = 0;
  virtual float atan(float v) const = 0;
  
  virtual double atan2(double v) const = 0;
  virtual float atan2(float v) const = 0;
  
  virtual int round(double v) const = 0;
  virtual int round(float v) const = 0;
  
  virtual int abs(int v) const = 0;
  virtual double abs(double v) const = 0;
  virtual float abs(float v) const = 0;
  
  virtual double sqrt(double v) const = 0;
  virtual float sqrt(float v) const = 0;
  
  virtual double pow(double v, double u) const = 0;
  virtual float pow(float v, float u) const = 0;
  
  virtual double exp(double v) const = 0;
  virtual float exp(float v) const = 0;
  
  virtual int maxInt() const = 0;
  virtual int minInt() const = 0;
  virtual double maxDouble() const = 0;
  virtual double minDouble() const = 0;
  virtual float maxFloat() const = 0;
  virtual float minFloat() const = 0;
};

#endif
