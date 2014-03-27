//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Color
#define G3MiOSSDK_Color

#include <string>
#include "Angle.hpp"

class Color {
private:

  Color(const float red,
        const float green,
        const float blue,
        const float alpha) :
  _red(red),
  _green(green),
  _blue(blue),
  _alpha(alpha)
  {

  }

public:
  const float _red;
  const float _green;
  const float _blue;
  const float _alpha;

  Color(const Color& that):
  _red(that._red),
  _green(that._green),
  _blue(that._blue),
  _alpha(that._alpha) {
  }

  ~Color() {

  }

  static Color* parse(const std::string& str);


  static Color fromRGBA255(const int red,
                           const int green,
                           const int blue,
                           const int alpha) {
    return Color(red   / 255.0f,
                 green / 255.0f,
                 blue  / 255.0f,
                 alpha / 255.0f);
  }

  static Color fromRGBA(const float red,
                        const float green,
                        const float blue,
                        const float alpha) {
    return Color(red, green, blue, alpha);
  }

  static Color* newFromRGBA(const float red,
                            const float green,
                            const float blue,
                            const float alpha) {
    return new Color(red, green, blue, alpha);
  }

  static Color fromHueSaturationBrightness(double hueInRadians,
                                           float saturation,
                                           float brightness,
                                           float alpha);

  static Color transparent() {
    return Color::fromRGBA(0, 0, 0, 0);
  }

  static Color black() {
    return Color::fromRGBA(0, 0, 0, 1);
  }

  static Color gray() {
    return Color::fromRGBA(0.5f, 0.5f, 0.5f, 1);
  }

  static Color darkGray() {
    const float oneThird = 1.0f / 3.0f;
    return Color::fromRGBA(oneThird, oneThird, oneThird, 1);
  }

  static Color lightGray() {
    const float twoThirds = 2.0f / 3.0f;
    return Color::fromRGBA(twoThirds, twoThirds, twoThirds, 1);
  }

  static Color white() {
    return Color::fromRGBA(1, 1, 1, 1);
  }

  static Color yellow() {
    return Color::fromRGBA(1, 1, 0, 1);
  }
  
  static Color cyan() {
    return Color::fromRGBA(0, 1, 1, 1);
  }

  static Color magenta() {
    return Color::fromRGBA(1, 0, 1, 1);
  }
  
  static Color red() {
    return Color::fromRGBA(1, 0, 0, 1);
  }

  static Color green() {
    return Color::fromRGBA(0, 1, 0, 1);
  }

  static Color blue() {
    return Color::fromRGBA(0, 0, 1, 1);
  }

  Color mixedWith(const Color& that,
                  float factor) const {
    float frac1 = factor;
    if (frac1 < 0) frac1 = 0;
    if (frac1 > 1) frac1 = 1;

    const float frac2 = 1 - frac1;

    const float newRed   = (_red   * frac2) + (that._red   * frac1);
    const float newGreen = (_green * frac2) + (that._green * frac1);
    const float newBlue  = (_blue  * frac2) + (that._blue  * frac1);
    const float newAlpha = (_alpha * frac2) + (that._alpha * frac1);

    return Color::fromRGBA(newRed, newGreen, newBlue, newAlpha);
  }

  bool isTransparent() const {
    return (_alpha < 1);
  }

  bool isFullTransparent() const {
    return (_alpha < 0.01);
  }

  bool isEquals(const Color& that) const;

  Color wheelStep(int wheelSize,
                  int step) const;

  float getSaturation() const;

  float getBrightness() const;

  double getHueInRadians() const;

  Angle getHue() const {
    return Angle::fromRadians( getHueInRadians() );
  }

  Color adjustBrightness(float brightness) {
    const float newBrightness = getBrightness() + brightness;
    return Color::fromHueSaturationBrightness(getHueInRadians(),
                                              getSaturation(),
                                              newBrightness,
                                              _alpha);
  }

  Color adjustSaturationBrightness(float saturation,
                                   float brightness) {
    const float newSaturation = getSaturation() + saturation;
    const float newBrightness = getBrightness() + brightness;
    return Color::fromHueSaturationBrightness(getHueInRadians(),
                                              newSaturation,
                                              newBrightness,
                                              _alpha);
  }

  Color darker() {
    return adjustBrightness(-0.08f);
  }

  Color twiceDarker() {
    return adjustBrightness(-0.16f);
  }

  Color muchDarker() {
    return adjustBrightness(-0.64f);
  }

  Color lighter() {
    return adjustSaturationBrightness(-0.03f, 0.08f);
  }

  Color twiceLighter() {
    return adjustSaturationBrightness(-0.06f, 0.16f);
  }
  
  Color muchLighter() {
    return adjustSaturationBrightness(-0.24f, 0.64f);
  }

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
