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

bool Color::isEqualsTo(const Color& that) const {
  return
  (_red   == that._red  ) &&
  (_green == that._green) &&
  (_blue  == that._blue ) &&
  (_alpha == that._alpha);
}
