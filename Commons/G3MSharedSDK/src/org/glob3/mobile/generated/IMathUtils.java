package org.glob3.mobile.generated;import java.util.*;

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



//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE

//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define SIN(x) sin(x)
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define COS(x) cos(x)
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define TAN(x) tan(x)
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define NAND NAN
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define NANF NAN

//#else

//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define SIN(x) java.lang.Math.sin(x)
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define COS(x) java.lang.Math.cos(x)
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define TAN(x) java.lang.Math.tan(x)
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define NAND java.lang.Double.NaN
//C++ TO JAVA CONVERTER TODO TASK: Alternate #define macros with the same name cannot be converted to Java:
//#define NANF java.lang.Float.NaN

//#endif

//#define PI 3.14159265358979323846264338327950288
//#define HALF_PI 1.57079632679489661923132169163975144

//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define ISNAN(x) (x != x)

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Geodetic2D;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
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

//  virtual double NanD() const = 0;
//  virtual float  NanF() const = 0;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double sin(double v) const = 0;
  public abstract double sin(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float sin(float v) const = 0;
  public abstract float sin(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double sinh(double v) const = 0;
  public abstract double sinh(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float sinh(float v) const = 0;
  public abstract float sinh(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double asin(double v) const = 0;
  public abstract double asin(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float asin(float v) const = 0;
  public abstract float asin(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double cos(double v) const = 0;
  public abstract double cos(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float cos(float v) const = 0;
  public abstract float cos(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double acos(double v) const = 0;
  public abstract double acos(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float acos(float v) const = 0;
  public abstract float acos(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double tan(double v) const = 0;
  public abstract double tan(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float tan(float v) const = 0;
  public abstract float tan(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double atan(double v) const = 0;
  public abstract double atan(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float atan(float v) const = 0;
  public abstract float atan(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double atan2(double u, double v) const = 0;
  public abstract double atan2(double u, double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float atan2(float u, float v) const = 0;
  public abstract float atan2(float u, float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long round(double v) const = 0;
  public abstract long round(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int round(float v) const = 0;
  public abstract int round(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int abs(int v) const = 0;
  public abstract int abs(int v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double abs(double v) const = 0;
  public abstract double abs(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float abs(float v) const = 0;
  public abstract float abs(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double sqrt(double v) const = 0;
  public abstract double sqrt(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float sqrt(float v) const = 0;
  public abstract float sqrt(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double pow(double v, double u) const = 0;
  public abstract double pow(double v, double u);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float pow(float v, float u) const = 0;
  public abstract float pow(float v, float u);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double exp(double v) const = 0;
  public abstract double exp(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float exp(float v) const = 0;
  public abstract float exp(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double log10(double v) const = 0;
  public abstract double log10(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float log10(float v) const = 0;
  public abstract float log10(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double log(double v) const = 0;
  public abstract double log(double v);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float log(float v) const = 0;
  public abstract float log(float v);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual short maxInt16() const = 0;
  public abstract short maxInt16();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual short minInt16() const = 0;
  public abstract short minInt16();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int maxInt32() const = 0;
  public abstract int maxInt32();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int minInt32() const = 0;
  public abstract int minInt32();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long maxInt64() const = 0;
  public abstract long maxInt64();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long minInt64() const = 0;
  public abstract long minInt64();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double maxDouble() const = 0;
  public abstract double maxDouble();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double minDouble() const = 0;
  public abstract double minDouble();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float maxFloat() const = 0;
  public abstract float maxFloat();
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float minFloat() const = 0;
  public abstract float minFloat();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int toInt(double value) const = 0;
  public abstract int toInt(double value);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int toInt(float value) const = 0;
  public abstract int toInt(float value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double min(double d1, double d2) const = 0;
  public abstract double min(double d1, double d2);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float min(float f1, float f2) const = 0;
  public abstract float min(float f1, float f2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int min(int i1, int i2) const
  public final int min(int i1, int i2)
  {
	return (i1 < i2) ? i1 : i2;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: long min(long i1, long i2) const
  public final long min(long i1, long i2)
  {
	return (i1 < i2) ? i1 : i2;
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double max(double d1, double d2) const = 0;
  public abstract double max(double d1, double d2);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float max(float f1, float f2) const = 0;
  public abstract float max(float f1, float f2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int max(int i1, int i2) const = 0;
  public abstract int max(int i1, int i2);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long max(long l1, long l2) const = 0;
  public abstract long max(long l1, long l2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double max(double d1, double d2, double d3) const
  public double max(double d1, double d2, double d3)
  {
	return max(max(d1, d2), d3);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float max(float f1, float f2, float f3) const
  public float max(float f1, float f2, float f3)
  {
	return max(max(f1, f2), f3);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float min(float f1, float f2, float f3) const
  public float min(float f1, float f2, float f3)
  {
	return min(min(f1, f2), f3);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double floor(double d) const = 0;
  public abstract double floor(double d);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float floor(float f) const = 0;
  public abstract float floor(float f);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double ceil(double d) const = 0;
  public abstract double ceil(double d);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float ceil(float f) const = 0;
  public abstract float ceil(float f);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double fmod(double d1, double d2) const = 0;
  public abstract double fmod(double d1, double d2);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float fmod(float f1, float f2) const = 0;
  public abstract float fmod(float f1, float f2);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double linearInterpolation(double from, double to, double alpha) const
  public double linearInterpolation(double from, double to, double alpha)
  {
	return from + ((to - from) * alpha);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double cosineInterpolation(double from, double to, double alpha) const
  public double cosineInterpolation(double from, double to, double alpha)
  {
	final double alpha2 = (1.0 - Math.cos(alpha *DefineConstants.PI)) / 2.0;
	return (from * (1.0 - alpha2) + to * alpha2);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float linearInterpolation(float from, float to, float alpha) const
  public float linearInterpolation(float from, float to, float alpha)
  {
	return from + ((to - from) * alpha);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double quadraticBezierInterpolation(double from, double middle, double to, double alpha) const
  public double quadraticBezierInterpolation(double from, double middle, double to, double alpha)
  {
	final double oneMinusAlpha = 1.0 - alpha;
	return (oneMinusAlpha *oneMinusAlpha * from) + (2.0 *oneMinusAlpha *alpha * middle) + (alpha *alpha * to);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float quadraticBezierInterpolation(float from, float middle, float to, float alpha) const
  public float quadraticBezierInterpolation(float from, float middle, float to, float alpha)
  {
	final float oneMinusAlpha = 1.0f - alpha;
	return (oneMinusAlpha *oneMinusAlpha * from) + (2.0f *oneMinusAlpha *alpha * middle) + (alpha *alpha * to);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual long doubleToRawLongBits(double value) const = 0;
  public abstract long doubleToRawLongBits(double value);
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double rawLongBitsToDouble(long value) const = 0;
  public abstract double rawLongBitsToDouble(long value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float rawIntBitsToFloat(int value) const = 0;
  public abstract float rawIntBitsToFloat(int value);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double clamp(double value, double min, double max) const
  public double clamp(double value, double min, double max)
  {
	if (value < min)
		return min;
	if (value > max)
		return max;
	return value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual float clamp(float value, float min, float max) const
  public float clamp(float value, float min, float max)
  {
	if (value < min)
		return min;
	if (value > max)
		return max;
	return value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEquals(double x, double y) const
  public boolean isEquals(double x, double y)
  {
	if (x == y)
	{
	  return true;
	}
	final double epsilon = 1e-8;
	return Math.abs(x - y) <= epsilon * max(Math.abs(x), Math.abs(y), 1.0);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isEquals(float x, float y) const
  public boolean isEquals(float x, float y)
  {
	if (x == y)
	{
	  return true;
	}
	final float epsilon = 1e-8f;
	return Math.abs(x - y) <= epsilon * max(Math.abs(x), Math.abs(y), 1.0f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean isBetween(float value, float min, float max) const
  public boolean isBetween(float value, float min, float max)
  {
	return (value >= min) && (value <= max);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double pseudoModule(double numerator, double denominator) const
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
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual double nextRandomDouble() const = 0;
  public abstract double nextRandomDouble();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D greatCircleIntermediatePoint(const Angle& fromLat, const Angle& fromLon, const Angle& toLat, const Angle& toLon, const double alpha) const
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

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int gcd(int a, int b) const
  public int gcd(int a, int b)
  {
	return (b == 0) ? a : gcd(b, a % b);
  }

}
