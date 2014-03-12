//
//  GEOLabelRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/14.
//
//

#ifndef __G3MiOSSDK__GEOLabelRasterSymbol__
#define __G3MiOSSDK__GEOLabelRasterSymbol__

#include "GEORasterSymbol.hpp"

#include <string>

#include "GFont.hpp"


class GEOLabelRasterSymbol : public GEORasterSymbol {
private:
  const std::string  _label;
  const Geodetic2D   _position;
#ifdef C_CODE
  const GFont        _font;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
#endif
  const Color        _color;


  static const Sector* calculateSectorFromPosition(const Geodetic2D& position);

protected:
  void rawRasterize(ICanvas*                   canvas,
                    const GEORasterProjection* projection) const;

public:

  GEOLabelRasterSymbol(const std::string& label,
                       const Geodetic2D& position,
                       const GFont& font,
                       const Color& color,
                       const int minTileLevel = -1,
                       const int maxTileLevel = -1);

};

#endif
