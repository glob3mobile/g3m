//
//  Color.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/3/13.
//
//

#include "Color.hpp"

#include "IStringUtils.hpp"
#include "IMathUtils.hpp"
#include "IStringBuilder.hpp"

Color* Color::parse(const std::string& str) {
  const IStringUtils* su = IStringUtils::instance();

  std::string colorStr = su->trim(str);

  if ( su->beginsWith(colorStr, "#") ) {
#ifdef C_CODE
    colorStr = su->trim( su->substring(colorStr, 1) );
#endif
#ifdef JAVA_CODE
    colorStr = su.trim(su.substring(colorStr, 1));
#endif
  }

  const int strSize = colorStr.size();

  std::string rs;
  std::string gs;
  std::string bs;
  std::string as;
  if (strSize == 3) {
    // RGB
    rs = su->substring(colorStr, 0, 1);
    gs = su->substring(colorStr, 1, 2);
    bs = su->substring(colorStr, 2, 3);
    as = "ff";

    rs = rs + rs;
    gs = gs + gs;
    bs = bs + bs;
  }
  else if (strSize == 4) {
    // RGBA
    rs = su->substring(colorStr, 0, 1);
    gs = su->substring(colorStr, 1, 2);
    bs = su->substring(colorStr, 2, 3);
    as = su->substring(colorStr, 3, 4);

    rs = rs + rs;
    gs = gs + gs;
    bs = bs + bs;
    as = as + as;
  }
  else if (strSize == 6) {
    // RRGGBB
    rs = su->substring(colorStr, 0, 2);
    gs = su->substring(colorStr, 2, 4);
    bs = su->substring(colorStr, 4, 6);
    as = "ff";
  }
  else if (strSize == 8) {
    // RRGGBBAA
    rs = su->substring(colorStr, 0, 2);
    gs = su->substring(colorStr, 2, 4);
    bs = su->substring(colorStr, 4, 6);
    as = su->substring(colorStr, 6, 8);
  }
  else {
    ILogger::instance()->logError("Invalid color format: \"%s\"", str.c_str());
    return NULL;
  }

  const IMathUtils* mu = IMathUtils::instance();

  const float r = mu->clamp( (float) su->parseHexInt(rs) / 255.0f, 0, 1 );
  const float g = mu->clamp( (float) su->parseHexInt(gs) / 255.0f, 0, 1 );
  const float b = mu->clamp( (float) su->parseHexInt(bs) / 255.0f, 0, 1 );
  const float a = mu->clamp( (float) su->parseHexInt(as) / 255.0f, 0, 1 );

  return Color::newFromRGBA(r, g, b, a);
}

bool Color::isEquals(const Color& that) const {
  return ((_red   == that._red  ) &&
          (_green == that._green) &&
          (_blue  == that._blue ) &&
          (_alpha == that._alpha));
}

float Color::getSaturation() const {
  const IMathUtils* mu = IMathUtils::instance();

//  const float r = _red;
//  const float g = _green;
//  const float b = _blue;

  const float max = mu->max(_red, _green, _blue);
  const float min = mu->min(_red, _green, _blue);

  if (max == 0) {
    return 0;
  }

  return (max - min) / max;
}

float Color::getBrightness() const {
  return IMathUtils::instance()->max(_red, _green, _blue);
}

double Color::getHueInRadians() const {
  const IMathUtils* mu = IMathUtils::instance();

  const float r = _red;
  const float g = _green;
  const float b = _blue;

  const float max = mu->max(r, g, b);
  const float min = mu->min(r, g, b);

  const float span = (max - min);

  if (span == 0) {
    return 0;
  }

  const double deg60 = TO_RADIANS(60.0);

  double h;
  if (r == max) {
    h = ((g - b) / span) * deg60;
  }
  else if (g == max) {
    h = (deg60 * 2) + (((b - r) / span) * deg60);
  }
  else {
    h = (deg60 * 4) + (((r - g) / span) * deg60);
  }

  if (h < 0) {
    return (PI * 2) + h;
  }

  return h;

}

Color Color::fromHueSaturationBrightness(double hueInRadians,
                                         float saturation,
                                         float brightness,
                                         float alpha) {
  const IMathUtils* mu = IMathUtils::instance();

  const float s = mu->clamp(saturation, 0, 1);
  const float v = mu->clamp(brightness, 0, 1);

  //  zero saturation yields gray with the given brightness
  if (s == 0) {
    return fromRGBA(v, v, v, alpha);
  }

  const double deg60 = TO_RADIANS(60);

  //final float hf = (float) ((hue % GMath.DEGREES_360) / GMath.DEGREES_60);
  const float hf = (float) (mu->pseudoModule(hueInRadians, PI * 2) / deg60);

  const int i = (int) hf; // integer part of hue
  const float f = hf - i; // fractional part of hue

  const float p = (1 - s) * v;
  const float q = (1 - (s * f)) * v;
  const float t = (1 - (s * (1 - f))) * v;

  switch (i) {
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

Color Color::wheelStep(int wheelSize,
                       int step) const {
  const double stepInRadians = (PI * 2) / wheelSize;

  const double hueInRadians = getHueInRadians() + (stepInRadians * step);

  return Color::fromHueSaturationBrightness(hueInRadians,
                                            getSaturation(),
                                            getBrightness(),
                                            _alpha);
}

const std::string Color::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("[Color red=");
  isb->addFloat(_red);
  isb->addString(", green=");
  isb->addFloat(_green);
  isb->addString(", blue=");
  isb->addFloat(_blue);
  isb->addString(", alpha=");
  isb->addFloat(_alpha);
  isb->addString("]");
  const std::string s = isb->getString();
  delete isb;
  return s;
}
