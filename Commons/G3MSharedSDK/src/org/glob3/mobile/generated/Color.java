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