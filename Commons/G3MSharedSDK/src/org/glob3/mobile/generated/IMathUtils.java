package org.glob3.mobile.generated;
//
//  IMathUtils.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 29/08/12.
//

//
//  IMathUtils.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/08/12.
//




//#define SIN(x) Math.sin(x)
//#define COS(x) Math.cos(x)
//#define TAN(x) Math.tan(x)
//#define NAND Double.NaN
//#define NANF Float.NaN


//#define PI 3.14159265358979323846264338327950288
//#define HALF_PI 1.57079632679489661923132169163975144

//#define ISNAN(x) (x != x)

//#define TO_RADIANS(degrees) ((degrees) / 180.0 * 3.14159265358979323846264338327950288)
//#define TO_DEGREES(radians) ((radians) * (180.0 / 3.14159265358979323846264338327950288))

//class Geodetic2D;
//class Angle;

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

  public abstract double sin(double v);
  public abstract float sin(float v);

  public abstract double sinh(double v);
  public abstract float sinh(float v);

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

  public abstract long round(double v);
  public abstract int round(float v);

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

  public abstract short maxInt16();
  public abstract short minInt16();

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

  public abstract double min(double d1, double d2);
  public abstract float min(float f1, float f2);

  public final int min(int i1, int i2)
  {
    return (i1 < i2) ? i1 : i2;
  }


  public final long min(long i1, long i2)
  {
    return (i1 < i2) ? i1 : i2;
  }


  public abstract double max(double d1, double d2);
  public abstract float max(float f1, float f2);

  public abstract int max(int i1, int i2);
  public abstract long max(long l1, long l2);

  public double max(double d1, double d2, double d3)
  {
    return max(max(d1, d2), d3);
  }

  public float max(float f1, float f2, float f3)
  {
    return max(max(f1, f2), f3);
  }

  public float min(float f1, float f2, float f3)
  {
    return min(min(f1, f2), f3);
  }

  public abstract double floor(double d);
  public abstract float floor(float f);

  public abstract double ceil(double d);
  public abstract float ceil(float f);

  public abstract double mod(double d1, double d2);
  public abstract float mod(float f1, float f2);

  public double linearInterpolation(double from, double to, double alpha)
  {
    return from + ((to - from) * alpha);
  }

  public double cosineInterpolation(double from, double to, double alpha)
  {
    final double alpha2 = (1.0 - Math.cos(alpha *DefineConstants.PI)) / 2.0;
    return (from * (1.0 - alpha2) + to * alpha2);
  }

  public float linearInterpolation(float from, float to, float alpha)
  {
    return from + ((to - from) * alpha);
  }


  public double quadraticBezierInterpolation(double from, double middle, double to, double alpha)
  {
    final double oneMinusAlpha = 1.0 - alpha;
    return (oneMinusAlpha *oneMinusAlpha * from) + (2.0 *oneMinusAlpha *alpha * middle) + (alpha *alpha * to);
  }

  public float quadraticBezierInterpolation(float from, float middle, float to, float alpha)
  {
    final float oneMinusAlpha = 1.0f - alpha;
    return (oneMinusAlpha *oneMinusAlpha * from) + (2.0f *oneMinusAlpha *alpha * middle) + (alpha *alpha * to);
  }


  public abstract long doubleToRawLongBits(double value);
  public abstract double rawLongBitsToDouble(long value);

  public abstract float rawIntBitsToFloat(int value);

  public double clamp(double value, double min, double max)
  {
    if (value < min)
       return min;
    if (value > max)
       return max;
    return value;
  }

  public float clamp(float value, float min, float max)
  {
    if (value < min)
       return min;
    if (value > max)
       return max;
    return value;
  }

  public boolean isEquals(double x, double y)
  {
    if (x == y)
    {
      return true;
    }
    final double epsilon = 1e-8;
    return Math.abs(x - y) <= epsilon * max(Math.abs(x), Math.abs(y), 1.0);
  }

  public boolean isEquals(float x, float y)
  {
    if (x == y)
    {
      return true;
    }
    final float epsilon = 1e-8f;
    return Math.abs(x - y) <= epsilon * max(Math.abs(x), Math.abs(y), 1.0f);
  }

  public boolean isBetween(float value, float min, float max)
  {
    return (value >= min) && (value <= max);
  }

  public double pseudoModule(double numerator, double denominator)
  {

    final double result = numerator / denominator;
    final long intPart = (long) result; // integer part
    final double fracPart = result - intPart; // fractional part

//    if (closeTo(fracPart, 1.0)) {
    if (fracPart == 1.0)
    {
      return 0;
    }

    return fracPart * denominator;
  }

  /** answer a double value in the range 0.0 (inclusive) and 1.0 (exclusive) */
  public abstract double nextRandomDouble();

  public final Geodetic2D greatCircleIntermediatePoint(Angle fromLat, Angle fromLon, Angle toLat, Angle toLon, double alpha)
  {
  
    final double fromLatRad = fromLat._radians;
    final double toLatRad = toLat._radians;
    final double fromLonRad = fromLon._radians;
    final double toLonRad = toLon._radians;
  
    final double cosFromLat = Math.cos(fromLatRad);
    final double cosToLat = Math.cos(toLatRad);
  
    final double d = 2 * Math.asin(Math.sqrt(Math.pow((Math.sin((fromLatRad - toLatRad) / 2)), 2) + (cosFromLat * cosToLat * Math.pow(Math.sin((fromLonRad - toLonRad) / 2), 2))));
  
    final double A = Math.sin((1 - alpha) * d) / Math.sin(d);
    final double B = Math.sin(alpha * d) / Math.sin(d);
    final double x = (A * cosFromLat * Math.cos(fromLonRad)) + (B * cosToLat * Math.cos(toLonRad));
    final double y = (A * cosFromLat * Math.sin(fromLonRad)) + (B * cosToLat * Math.sin(toLonRad));
    final double z = (A * Math.sin(fromLatRad)) + (B * Math.sin(toLatRad));
    final double latRad = Math.atan2(z, Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    final double lngRad = Math.atan2(y, x);
  
    return new Geodetic2D(Angle.fromRadians(latRad), Angle.fromRadians(lngRad));
  }

  public int gcd(int a, int b)
  {
    return (b == 0) ? a : gcd(b, a % b);
  }

  public abstract double copySign(double a, double b);

}
