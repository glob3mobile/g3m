//
//  GEOLine2DRasterStyle.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/22/13.
//
//

#include "GEOLine2DRasterStyle.hpp"


void GEOLine2DRasterStyle::apply(ICanvas* canvas) const {
  canvas->setLineColor(_color);
  canvas->setLineWidth(_width);
  canvas->setLineCap(_cap);
  canvas->setLineJoin(_join);
  canvas->setLineMiterLimit(_miterLimit);
  
  int _DGD_rasterizer;

  canvas->setLineDash(_dashLengths,
                      _dashCount,
                      _dashPhase);
}
