//
//  GEO2DSurfaceRasterStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#ifndef __G3MiOSSDK__GEO2DSurfaceRasterStyle__
#define __G3MiOSSDK__GEO2DSurfaceRasterStyle__

#include "Color.hpp"
class ICanvas;

class GEO2DSurfaceRasterStyle {
private:
  const Color _color;

public:
  explicit GEO2DSurfaceRasterStyle(const Color& color) :
  _color(color)
  {
  }

  GEO2DSurfaceRasterStyle(const GEO2DSurfaceRasterStyle& that) :
  _color(that._color)
  {
  }

  bool apply(ICanvas* canvas) const;


};

#endif
