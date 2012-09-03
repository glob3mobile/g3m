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


  public Color(Color c)
  {
	  _red = c._red;
	  _green = c._green;
	  _blue = c._blue;
	  _alpha = c._alpha;
  }

  public static Color fromRGBA(float red, float green, float blue, float alpha)
  {
	return new Color(red, green, blue, alpha);
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
}