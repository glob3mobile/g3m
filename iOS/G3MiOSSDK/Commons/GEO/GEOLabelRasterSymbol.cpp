//
//  GEOLabelRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/14.
//
//

#include "GEOLabelRasterSymbol.hpp"

#include "ICanvas.hpp"
#include "GEORasterProjection.hpp"

const Sector* GEOLabelRasterSymbol::calculateSectorFromPosition(const Geodetic2D& position) {
  const double delta = 2;
  return new Sector(Geodetic2D::fromDegrees(position._latitude._degrees  - delta,
                                            position._longitude._degrees - delta),
                    Geodetic2D::fromDegrees(position._latitude._degrees  + delta,
                                            position._longitude._degrees + delta));
}

GEOLabelRasterSymbol::GEOLabelRasterSymbol(const std::string& label,
                                           const Geodetic2D& position,
                                           const GFont& font,
                                           const Color& color,
                                           const int minTileLevel,
                                           const int maxTileLevel) :
GEORasterSymbol(calculateSectorFromPosition(position),
                minTileLevel,
                maxTileLevel),
_position(position),
_label(label),
_font(font),
_color(color)
{

}

void GEOLabelRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                        const GEORasterProjection* projection) const {
  canvas->setFont(_font);

  const Vector2F textExtent = canvas->textExtent(_label);

  const Vector2F pixelPosition = projection->project(&_position);

  const float left = pixelPosition._x - textExtent._x/2;
  const float top  = pixelPosition._y - textExtent._y/2;

  //  canvas->setFillColor(Color::fromRGBA(0, 1, 0, 0.8f));
  //  canvas->fillRectangle(left, top, textExtent._x, textExtent._y);

  canvas->setFillColor(_color);
  canvas->fillText(_label, left, top);
}
