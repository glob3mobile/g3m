package org.glob3.mobile.generated; 
//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//





public class Color
{
  private final float _red;
  private final float _green;
  private final float _blue;
  private final float _alpha;

  private Color(float red, float green, float blue, float alpha)
  {
	  _red = red;
	  _green = green;
	  _blue = blue;
	  _alpha = alpha;

  }

  private static boolean isValidHex(String hex)
  {
	  if (hex.charAt(0) == '#')
	  {
		  if (hex.length() != 7)
		  {
			  return false;
		  }
	  }
	  else
	  {
		  if (hex.length() != 6)
		  {
			  return false;
		  }
	  }

	  if(!hex.matches("^#?([a-f]|[A-F]|[0-9]){3}(([a-f]|[A-F]|[0-9]){3})?$")){ return false;}

	  return true;
  }

  private static Color hexToRGB(String hex)
  {
	  if (!isValidHex(hex))
	  {
		  ILogger.instance().logError("The value received is not avalid hex string!");
	  }

	  if (hex.charAt(0) == '#')
	  {
		  hex = hex.substring(0, 0) + hex.substring(0 + 1);
	  }

	  String R = hex.substring(0, 2);
	  String G = hex.substring(2, 4);
	  String B = hex.substring(4, 6);

	  return new Color((float)IMathUtils.instance().parseIntHex(R)/255, (float)IMathUtils.instance().parseIntHex(G)/255, (float)IMathUtils.instance().parseIntHex(B)/255, 1);
  }

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

  public static Color fromRGBA(float red, float green, float blue, float alpha)
  {
	return new Color(red, green, blue, alpha);
  }

  public static Color newFromRGBA(float red, float green, float blue, float alpha)
  {
	return new Color(red, green, blue, alpha);
  }

  public static Color newFromHEX(String hex)
  {
	  return hexToRGB(hex);
  }

  public static Color black()
  {
	return Color.fromRGBA(0, 0, 0, 1);
  }

  public static Color white()
  {
	return Color.fromRGBA(1, 1, 1, 1);
  }


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getRed() const
  public final float getRed()
  {
	return _red;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getGreen() const
  public final float getGreen()
  {
	return _green;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getBlue() const
  public final float getBlue()
  {
	return _blue;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: float getAlpha() const
  public final float getAlpha()
  {
	return _alpha;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Color mixedWith(const Color& that, float factor) const
  public final Color mixedWith(Color that, float factor)
  {
	float frac1 = factor;
	if (factor < 0)
		factor = 0F;
	if (factor > 1)
		factor = 1F;

	final float frac2 = 1 - frac1;

	final float newRed = (getRed() * frac2) + (that.getRed() * frac1);
	final float newGreen = (getGreen() * frac2) + (that.getGreen() * frac1);
	final float newBlue = (getBlue() * frac2) + (that.getBlue() * frac1);
	final float newAlpha = (getAlpha() * frac2) + (that.getAlpha() * frac1);

	return Color.fromRGBA(newRed, newGreen, newBlue, newAlpha);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent() const
  public final boolean isTransparent()
  {
	return (_alpha < 1);
  }

}