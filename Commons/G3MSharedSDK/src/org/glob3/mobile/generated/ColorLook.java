package org.glob3.mobile.generated;
//
//  ColorLook.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//

//
//  ColorLook.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/9/26.
//



//class Matrix44D;


// The parameters of a tile color grade (PlanetRenderer::setColorMatrix).
// Interpolating the parameters, not the matrices, keeps every step of a transition a real look.
public class ColorLook
{
  // releases both
  private static Matrix44D createSequence(Matrix44D first, Matrix44D second)
  {
    Matrix44D sequence = ColorMatrix.createSequence(first, second);
    first._release();
    second._release();
    return sequence;
  }

  public static ColorLook neutral()
  {
    return new ColorLook(6500, 1, Angle.zero(), 1, 1, Color.white()); // tint -  brightness -  contrast -  hue -  saturation -  temperature, neutral white balance
  }

  public static ColorLook linearInterpolation(ColorLook from, ColorLook to, double alpha)
  {
    final IMathUtils mu = IMathUtils.instance();
    final float alphaF = (float) alpha;
    return new ColorLook(mu.linearInterpolation(from._temperature, to._temperature, alpha), mu.linearInterpolation(from._saturation, to._saturation, alpha), Angle.linearInterpolation(from._hue, to._hue, alpha), mu.linearInterpolation(from._contrast, to._contrast, alpha), mu.linearInterpolation(from._brightness, to._brightness, alpha), Color.fromRGBA(mu.linearInterpolation(from._tint._red, to._tint._red, alphaF), mu.linearInterpolation(from._tint._green, to._tint._green, alphaF), mu.linearInterpolation(from._tint._blue, to._tint._blue, alphaF), 1));
  }

  public final double _temperature; // kelvin, see ColorMatrix::createWhiteBalance
  public final double _saturation;
  public final Angle _hue ;
  public final double _contrast;
  public final double _brightness;
  public final Color _tint ;

  public ColorLook(double temperature, double saturation, Angle hue, double contrast, double brightness, Color tint)
  {
     _temperature = temperature;
     _saturation = saturation;
     _hue = new Angle(hue);
     _contrast = contrast;
     _brightness = brightness;
     _tint = tint;
  }

  public ColorLook(ColorLook that)
  {
     _temperature = that._temperature;
     _saturation = that._saturation;
     _hue = new Angle(that._hue);
     _contrast = that._contrast;
     _brightness = that._brightness;
     _tint = that._tint;
  }

  public void dispose()
  {
  }

  public final boolean isEquals(ColorLook that)
  {
    return ((_temperature == that._temperature) && (_saturation == that._saturation) && _hue.isEquals(that._hue) && (_contrast == that._contrast) && (_brightness == that._brightness) && _tint.isEquals(that._tint));
  }

  // applied in this order: white balance, saturation, hue rotation, contrast, brightness, tint
  public final Matrix44D createColorMatrix()
  {
    Matrix44D result = ColorMatrix.createWhiteBalance(_temperature);
    result = createSequence(result, ColorMatrix.createSaturation(_saturation));
    result = createSequence(result, ColorMatrix.createHueRotation(_hue));
    result = createSequence(result, ColorMatrix.createContrast(_contrast));
    result = createSequence(result, ColorMatrix.createBrightness(_brightness));
    result = createSequence(result, ColorMatrix.createTint(_tint));
    return result;
  }

}