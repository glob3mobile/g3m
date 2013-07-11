//
//  GEORasterLineSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#ifndef __G3MiOSSDK__GEORasterLineSymbol__
#define __G3MiOSSDK__GEORasterLineSymbol__

#include "GEORasterSymbol.hpp"

#include "Color.hpp"
#include "GEOLine2DStyle.hpp"
class Geodetic2D;
class GEOLine2DStyle;


class GEORasterLineSymbol : public GEORasterSymbol {
private:
  mutable const std::vector<Geodetic2D*>* _coordinates;

  const Color _lineColor;
  const float _lineWidth;

  GEORasterLineSymbol(const std::vector<Geodetic2D*>* coordinates,
                      const Sector* sector,
                      const Color&  lineColor,
                      const float   lineWidth) :
  GEORasterSymbol(sector),
  _coordinates(coordinates),
  _lineColor(lineColor),
  _lineWidth(lineWidth)
  {
  }

public:
  GEORasterLineSymbol(const std::vector<Geodetic2D*>* coordinates,
                      const GEOLine2DStyle& style):
  GEORasterSymbol( calculateSector(coordinates) ),
  _coordinates( copy(coordinates) ),
  _lineColor( style.getColor() ),
  _lineWidth( style.getWidth() )

  {
  }

  ~GEORasterLineSymbol();

  GEORasterLineSymbol* createSymbol() const;


  void rasterize(ICanvas*                   canvas,
                 const GEORasterProjection* projection) const;


};

#endif
