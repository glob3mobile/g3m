package org.glob3.mobile.generated; 
//
//  IMathUtils.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 29/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  IMathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public abstract class IMathUtils
{


  private static IMathUtils _instance = null;

  public static void setInstance(IMathUtils math)
  {
    if (_instance != null)
    {
      ILogger.instance().logWarning("IMathUtils instance already set!");
      if (_instance != null)
         _instance.dispose();
    }
    _instance = math;
  }

  public static IMathUtils instance()
  {
    return _instance;
  }

  public void dispose()
  {
  }

  public abstract double pi();
  public abstract double halfPi();

  public abstract boolean isNan(double v);
  public abstract boolean isNan(float v);

  public abstract double NanD();
  public abstract float NanF();

  public abstract double sin(double v);
  public abstract float sin(float v);

  public abstract double asin(double v);
  public abstract float asin(float v);

  public abstract double cos(double v);
  public abstract float cos(float v);

  public abstract double acos(double v);
  public abstract float acos(float v);

  public abstract double tan(double v);
  public abstract float tan(float v);

  public abstract double atan(double v);
  public abstract float atan(float v);

  public abstract double atan2(double u, double v);
  public abstract float atan2(float u, float v);

  public abstract double round(double v);
  public abstract float round(float v);

  public abstract int abs(int v);
  public abstract double abs(double v);
  public abstract float abs(float v);

  public abstract double sqrt(double v);
  public abstract float sqrt(float v);

  public abstract double pow(double v, double u);
  public abstract float pow(float v, float u);

  public abstract double exp(double v);
  public abstract float exp(float v);

  public abstract double log10(double v);
  public abstract float log10(float v);

  public abstract double log(double v);
  public abstract float log(float v);

  public abstract int maxInt32();
  public abstract int minInt32();

  public abstract long maxInt64();
  public abstract long minInt64();

  public abstract double maxDouble();
  public abstract double minDouble();

  public abstract float maxFloat();
  public abstract float minFloat();

  public abstract int toInt(double value);
  public abstract int toInt(float value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int parseIntHex(String hex) const = 0;
  public abstract int parseIntHex(String hex);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double min(double d1, double d2) const = 0;

  public abstract double min(double d1, double d2);
  public abstract double max(double d1, double d2);

  public double lerp(double from, double to, double alpha)
  {
    return from + ((to - from) * alpha);
  }

  public float lerp(float from, float to, float alpha)
  {
    return from + ((to - from) * alpha);
  }

  public abstract long doubleToRawLongBits(double value);
  public abstract double rawLongBitsToDouble(long value);

}