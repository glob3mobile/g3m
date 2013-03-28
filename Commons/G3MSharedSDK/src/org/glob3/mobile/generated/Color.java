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

  public static Color black()
  {
    return Color.fromRGBA(0, 0, 0, 1);
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

  public static Color green()
  {
    return Color.fromRGBA(0, 1, 0, 1);
  }

  public final float getRed()
  {
    return _red;
  }

  public final float getGreen()
  {
    return _green;
  }

  public final float getBlue()
  {
    return _blue;
  }

  public final float getAlpha()
  {
    return _alpha;
  }

  public final Color mixedWith(Color that, float factor)
  {
    float frac1 = factor;
    if (frac1 < 0)
       frac1 = 0F;
    if (frac1 > 1)
       frac1 = 1F;

    final float frac2 = 1 - frac1;

    final float newRed = (getRed() * frac2) + (that.getRed() * frac1);
    final float newGreen = (getGreen() * frac2) + (that.getGreen() * frac1);
    final float newBlue = (getBlue() * frac2) + (that.getBlue() * frac1);
    final float newAlpha = (getAlpha() * frac2) + (that.getAlpha() * frac1);

    return Color.fromRGBA(newRed, newGreen, newBlue, newAlpha);
  }

  public final boolean isTransparent()
  {
    return (_alpha < 1);
  }

}