package org.glob3.mobile.generated;//
//  Color.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/3/13.
//
//

//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//



public class Color
{

  private Color(float red, float green, float blue, float alpha)
  {
	  _red = red;
	  _green = green;
	  _blue = blue;
	  _alpha = alpha;

  }

  public final float _red;
  public final float _green;
  public final float _blue;
  public final float _alpha;

  public Color(Color that)
  {
	  _red = that._red;
	  _green = that._green;
	  _blue = that._blue;
	  _alpha = that._alpha;
  }

  public void dispose()
  {

  }

  public static Color parse(String str)
  {
	final IStringUtils su = IStringUtils.instance();
  
	String colorStr = su.trim(str);
  
	if (su.beginsWith(colorStr, "#"))
	{
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if C_CODE
	  colorStr = su.trim(su.substring(colorStr, colorStr + 1));
//#endif
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
	  colorStr = su.trim(su.substring(colorStr, colorStr + 1));
//#endif
	}
  
	final int strSize = colorStr.length();
  
	String rs;
	String gs;
	String bs;
	String as;
	if (strSize == 3)
	{
	  // RGB
	  rs = su.substring(colorStr, 0, 1);
	  gs = su.substring(colorStr, 1, 2);
	  bs = su.substring(colorStr, 2, 3);
	  as = "ff";
  
	  rs = rs + rs;
	  gs = gs + gs;
	  bs = bs + bs;
	}
	else if (strSize == 4)
	{
	  // RGBA
	  rs = su.substring(colorStr, 0, 1);
	  gs = su.substring(colorStr, 1, 2);
	  bs = su.substring(colorStr, 2, 3);
	  as = su.substring(colorStr, 3, 4);
  
	  rs = rs + rs;
	  gs = gs + gs;
	  bs = bs + bs;
	  as = as + as;
	}
	else if (strSize == 6)
	{
	  // RRGGBB
	  rs = su.substring(colorStr, 0, 2);
	  gs = su.substring(colorStr, 2, 4);
	  bs = su.substring(colorStr, 4, 6);
	  as = "ff";
	}
	else if (strSize == 8)
	{
	  // RRGGBBAA
	  rs = su.substring(colorStr, 0, 2);
	  gs = su.substring(colorStr, 2, 4);
	  bs = su.substring(colorStr, 4, 6);
	  as = su.substring(colorStr, 6, 8);
	}
	else
	{
	  ILogger.instance().logError("Invalid color format: \"%s\"", str.c_str());
	  return null;
	}
  
	final IMathUtils mu = IMathUtils.instance();
  
	final float r = mu.clamp((float) su.parseHexInt(rs) / 255.0f, 0, 1);
	final float g = mu.clamp((float) su.parseHexInt(gs) / 255.0f, 0, 1);
	final float b = mu.clamp((float) su.parseHexInt(bs) / 255.0f, 0, 1);
	final float a = mu.clamp((float) su.parseHexInt(as) / 255.0f, 0, 1);
  
	return Color.newFromRGBA(r, g, b, a);
  }


  public static Color fromRGBA255(int red, int green, int blue, int alpha)
  {
	return new Color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
  }

  public static Color fromRGBA(float red, float green, float blue, float alpha)
  {
	return new Color(red, green, blue, alpha);
  }

  public static Color newFromRGBA(float red, float green, float blue, float alpha)
  {
	return new Color(red, green, blue, alpha);
  }

  public static Color fromHueSaturationBrightness(double hueInRadians, float saturation, float brightness, float alpha)
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final float s = mu.clamp(saturation, 0, 1);
	final float v = mu.clamp(brightness, 0, 1);
  
	//  zero saturation yields gray with the given brightness
	if (s == 0)
	{
	  return fromRGBA(v, v, v, alpha);
	}
  
	final double deg60 = ((60) / 180.0 * 3.14159265358979323846264338327950288);
  
	//final float hf = (float) ((hue % GMath.DEGREES_360) / GMath.DEGREES_60);
	final float hf = (float)(mu.pseudoModule(hueInRadians, DefineConstants.PI * 2) / deg60);
  
	final int i = (int) hf; // integer part of hue
	final float f = hf - i; // fractional part of hue
  
	final float p = (1 - s) * v;
	final float q = (1 - (s * f)) * v;
	final float t = (1 - (s * (1 - f))) * v;
  
	switch (i)
	{
	  case 0:
		return fromRGBA(v, t, p, alpha);
	  case 1:
		return fromRGBA(q, v, p, alpha);
	  case 2:
		return fromRGBA(p, v, t, alpha);
	  case 3:
		return fromRGBA(p, q, v, alpha);
	  case 4:
		return fromRGBA(t, p, v, alpha);
  //    case 5:
	  default:
		return fromRGBA(v, p, q, alpha);
	}
  
  }

  public static Color interpolateColor(Color from, Color middle, Color to, float d)
  {
	if (d <= 0)
	{
	  return from;
	}
	if (d >= 1)
	{
	  return to;
	}
	if (d <= 0.5)
	{
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return from.mixedWith(middle, d * 2);
	  return from.mixedWith(new Color(middle), d * 2);
	}
//C++ TO JAVA CONVERTER WARNING: The following line was determined to be a copy constructor call - this should be verified and a copy constructor should be created if it does not yet exist:
//ORIGINAL LINE: return middle.mixedWith(to, (d - 0.5f) * 2);
	return middle.mixedWith(new Color(to), (d - 0.5f) * 2);
  }

  public static Color interpolateColor(Color from, Color to, float d)
  {
	final IMathUtils mu = IMathUtils.instance();
	return Color.fromRGBA(mu.linearInterpolation(from._red, to._red, d), mu.linearInterpolation(from._green, to._green, d), mu.linearInterpolation(from._blue, to._blue, d), mu.linearInterpolation(from._alpha, to._alpha, d));

  }

  public static Color transparent()
  {
	return Color.fromRGBA(0, 0, 0, 0);
  }

  public static Color black()
  {
	return Color.fromRGBA(0, 0, 0, 1);
  }

  public static Color gray()
  {
	return Color.fromRGBA(0.5f, 0.5f, 0.5f, 1);
  }

  public static Color darkGray()
  {
	final float oneThird = 1.0f / 3.0f;
	return Color.fromRGBA(oneThird, oneThird, oneThird, 1);
  }

  public static Color lightGray()
  {
	final float twoThirds = 2.0f / 3.0f;
	return Color.fromRGBA(twoThirds, twoThirds, twoThirds, 1);
  }

  public static Color white()
  {
	return Color.fromRGBA(1, 1, 1, 1);
  }

  public static Color yellow()
  {
	return Color.fromRGBA(1, 1, 0, 1);
  }

  public static Color cyan()
  {
	return Color.fromRGBA(0, 1, 1, 1);
  }

  public static Color magenta()
  {
	return Color.fromRGBA(1, 0, 1, 1);
  }

  public static Color red()
  {
	return Color.fromRGBA(1, 0, 0, 1);
  }

  public static Color green()
  {
	return Color.fromRGBA(0, 1, 0, 1);
  }

  public static Color blue()
  {
	return Color.fromRGBA(0, 0, 1, 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color mixedWith(const Color& that, float factor) const
  public final Color mixedWith(Color that, float factor)
  {
	float frac1 = factor;
	if (frac1 < 0)
		frac1 = 0F;
	if (frac1 > 1)
		frac1 = 1F;

	final float frac2 = 1 - frac1;

	final float newRed = (_red * frac2) + (that._red * frac1);
	final float newGreen = (_green * frac2) + (that._green * frac1);
	final float newBlue = (_blue * frac2) + (that._blue * frac1);
	final float newAlpha = (_alpha * frac2) + (that._alpha * frac1);

	return Color.fromRGBA(newRed, newGreen, newBlue, newAlpha);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return (_alpha < 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isFullTransparent() const
  public final boolean isFullTransparent()
  {
	return (_alpha < 0.01);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isEquals(const Color& that) const
  public final boolean isEquals(Color that)
  {
	return ((_red == that._red) && (_green == that._green) && (_blue == that._blue) && (_alpha == that._alpha));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color wheelStep(int wheelSize, int step) const
  public final Color wheelStep(int wheelSize, int step)
  {
	final double stepInRadians = (DefineConstants.PI * 2) / wheelSize;
  
	final double hueInRadians = getHueInRadians() + (stepInRadians * step);
  
	return Color.fromHueSaturationBrightness(hueInRadians, getSaturation(), getBrightness(), _alpha);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getSaturation() const
  public final float getSaturation()
  {
	final IMathUtils mu = IMathUtils.instance();
  
  //  const float r = _red;
  //  const float g = _green;
  //  const float b = _blue;
  
	final float max = mu.max(_red, _green, _blue);
	final float min = mu.min(_red, _green, _blue);
  
	if (max == 0)
	{
	  return 0;
	}
  
	return (max - min) / max;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getBrightness() const
  public final float getBrightness()
  {
	return IMathUtils.instance().max(_red, _green, _blue);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double getHueInRadians() const
  public final double getHueInRadians()
  {
	final IMathUtils mu = IMathUtils.instance();
  
	final float r = _red;
	final float g = _green;
	final float b = _blue;
  
	final float max = mu.max(r, g, b);
	final float min = mu.min(r, g, b);
  
	final float span = (max - min);
  
	if (span == 0)
	{
	  return 0;
	}
  
	final double deg60 = ((60.0) / 180.0 * 3.14159265358979323846264338327950288);
  
	double h;
	if (r == max)
	{
	  h = ((g - b) / span) * deg60;
	}
	else if (g == max)
	{
	  h = (deg60 * 2) + (((b - r) / span) * deg60);
	}
	else
	{
	  h = (deg60 * 4) + (((r - g) / span) * deg60);
	}
  
	if (h < 0)
	{
	  return (DefineConstants.PI * 2) + h;
	}
  
	return h;
  
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle getHue() const
  public final Angle getHue()
  {
	return Angle.fromRadians(getHueInRadians());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color adjustBrightness(float brightness) const
  public final Color adjustBrightness(float brightness)
  {
	final float newBrightness = getBrightness() + brightness;
	return Color.fromHueSaturationBrightness(getHueInRadians(), getSaturation(), newBrightness, _alpha);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color adjustSaturationBrightness(float saturation, float brightness) const
  public final Color adjustSaturationBrightness(float saturation, float brightness)
  {
	final float newSaturation = getSaturation() + saturation;
	final float newBrightness = getBrightness() + brightness;
	return Color.fromHueSaturationBrightness(getHueInRadians(), newSaturation, newBrightness, _alpha);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color darker() const
  public final Color darker()
  {
	return adjustBrightness(-0.08f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color twiceDarker() const
  public final Color twiceDarker()
  {
	return adjustBrightness(-0.16f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color muchDarker() const
  public final Color muchDarker()
  {
	return adjustBrightness(-0.64f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color lighter() const
  public final Color lighter()
  {
	return adjustSaturationBrightness(-0.03f, 0.08f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color twiceLighter() const
  public final Color twiceLighter()
  {
	return adjustSaturationBrightness(-0.06f, 0.16f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color muchLighter() const
  public final Color muchLighter()
  {
	return adjustSaturationBrightness(-0.24f, 0.64f);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String toID() const
  public final String toID()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addFloat(_red);
	isb.addString("/");
	isb.addFloat(_green);
	isb.addString("/");
	isb.addFloat(_blue);
	isb.addString("/");
	isb.addFloat(_alpha);
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.addString("[Color red=");
	isb.addFloat(_red);
	isb.addString(", green=");
	isb.addFloat(_green);
	isb.addString(", blue=");
	isb.addFloat(_blue);
	isb.addString(", alpha=");
	isb.addFloat(_alpha);
	isb.addString("]");
	final String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if JAVA_CODE
  public final Override public String toString()
  {
	return description();
  }
//#endif

}
