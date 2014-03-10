//
//  GEOLabelRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/10/14.
//
//

#include "GEOLabelRasterSymbol.hpp"

#include "ICanvas.hpp"
#include "GFont.hpp"
#include "GEORasterProjection.hpp"

const Sector* GEOLabelRasterSymbol::calculateSectorFromPosition(const Geodetic2D& position) {
  const double delta = 1;
  return new Sector(Geodetic2D::fromDegrees(position._latitude._degrees  - delta,
                                            position._longitude._degrees - delta),
                    Geodetic2D::fromDegrees(position._latitude._degrees  + delta,
                                            position._longitude._degrees + delta));
}

GEOLabelRasterSymbol::GEOLabelRasterSymbol(const std::string& label,
                                           const Geodetic2D& position,
                                           const int minTileLevel,
                                           const int maxTileLevel) :
GEORasterSymbol(calculateSectorFromPosition(position),
                minTileLevel,
                maxTileLevel),
_position(position),
_label(label)
{

}

void GEOLabelRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                        const GEORasterProjection* projection) const {
#warning Diego at work!

  canvas->setFont(GFont::serif());
  const Vector2F extent = canvas->textExtent(_label);

  const Vector2F pixelPosition = projection->project(&_position);

  const float left = (pixelPosition._x + extent._x) / 2;
  const float top  = (pixelPosition._y + extent._y) / 2;

//  drawAt((canvas->getWidth()  - extent._x) / 2,
//         (canvas->getHeight() - extent._y) / 2,
//         canvas);


  canvas->fillText(_label,
                   left, top);
}
