//
//  GEO2DLineRasterStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//

#include "GEO2DLineRasterStyle.hpp"


bool GEO2DLineRasterStyle::apply(ICanvas* canvas) const {
  const bool applied = (_width > 0) && (!_color.isFullTransparent());

  if (applied) {
    canvas->setLineColor(_color);
    canvas->setLineWidth(_width);
    canvas->setLineCap(_cap);
    canvas->setLineJoin(_join);
    canvas->setLineMiterLimit(_miterLimit);

    canvas->setLineDash(_dashLengths,
                        _dashCount,
                        _dashPhase);
  }
  
  return applied;
}
