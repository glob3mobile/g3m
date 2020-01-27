//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//

#ifndef G3MiOSSDK_Color
#define G3MiOSSDK_Color

#include <string>

class Angle;


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

#ifdef C_CODE
  Color(const Color* that):
  _red(that->_red),
  _green(that->_green),
  _blue(that->_blue),
  _alpha(that->_alpha) {
  }
#endif

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

  static Color interpolateColor(const Color& from,
                                const Color& middle,
                                const Color& to,
                                float d);

  static const Color TRANSPARENT;
  static const Color BLACK;
  static const Color GRAY;
  static const Color DARK_GRAY;
  static const Color LIGHT_GRAY;
  static const Color WHITE;
  static const Color YELLOW;
  static const Color CYAN;
  static const Color MAGENTA;
  static const Color RED;
  static const Color ORANGE;
  static const Color GREEN;
  static const Color BLUE;

  static Color transparent() {
    return TRANSPARENT;
  }

  static Color black() {
    return BLACK;
  }

  static Color gray() {
    return GRAY;
  }

  static Color darkGray() {
    return DARK_GRAY;
  }

  static Color lightGray() {
    return LIGHT_GRAY;
  }

  static Color white() {
    return WHITE;
  }

  static Color yellow() {
    return YELLOW;
  }

  static Color cyan() {
    return CYAN;
  }

  static Color magenta() {
    return MAGENTA;
  }

  static Color red() {
    return RED;
  }

  static Color orange() {
    return ORANGE;
  }

  static Color green() {
    return GREEN;
  }

  static Color blue() {
    return BLUE;
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

  Angle getHue() const;

  Color adjustBrightness(float brightness) const {
    const float newBrightness = getBrightness() + brightness;
    return Color::fromHueSaturationBrightness(getHueInRadians(),
                                              getSaturation(),
                                              newBrightness,
                                              _alpha);
  }

  Color adjustSaturationBrightness(float saturation,
                                   float brightness) const {
    const float newSaturation = getSaturation() + saturation;
    const float newBrightness = getBrightness() + brightness;
    return Color::fromHueSaturationBrightness(getHueInRadians(),
                                              newSaturation,
                                              newBrightness,
                                              _alpha);
  }

  Color darker() const {
    return adjustBrightness(-0.08f);
  }

  Color twiceDarker() const {
    return adjustBrightness(-0.16f);
  }

  Color muchDarker() const {
    return adjustBrightness(-0.64f);
  }

  Color lighter() const {
    return adjustSaturationBrightness(-0.03f, 0.08f);
  }

  Color twiceLighter() const {
    return adjustSaturationBrightness(-0.06f, 0.16f);
  }

  Color muchLighter() const {
    return adjustSaturationBrightness(-0.24f, 0.64f);
  }

  const std::string id() const;

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif
  
};

#endif
