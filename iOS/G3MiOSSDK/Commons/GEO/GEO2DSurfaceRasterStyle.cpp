//
//  GEO2DSurfaceRasterStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#include "GEO2DSurfaceRasterStyle.hpp"

#include "ICanvas.hpp"

bool GEO2DSurfaceRasterStyle::apply(ICanvas* canvas) const {
  const bool applied = !_color.isFullTransparent();
  if (applied) {
    canvas->setFillColor(_color);
  }
  return applied;
}
