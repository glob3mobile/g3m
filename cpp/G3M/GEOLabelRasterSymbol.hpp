//
//  GEOLabelRasterSymbol.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 3/10/14.
//
//

#ifndef __G3M__GEOLabelRasterSymbol__
#define __G3M__GEOLabelRasterSymbol__

#include "GEORasterSymbol.hpp"

#include <string>

#include "GFont.hpp"
#include "Color.hpp"


class GEOLabelRasterSymbol : public GEORasterSymbol {
private:
  const std::string _label;
  const Geodetic2D  _position;
  const GFont       _font;
  const Color       _color;

  mutable Sector* _sector;
  static Sector* calculateSectorFromPosition(const Geodetic2D& position);

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

  ~GEOLabelRasterSymbol();

  const Sector* getSector() const;

};

#endif
