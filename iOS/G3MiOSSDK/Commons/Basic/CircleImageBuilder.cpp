//
//  CircleImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

#include "CircleImageBuilder.hpp"
#include "ICanvas.hpp"
#include "G3MContext.hpp"
#include "IStringUtils.hpp"

CircleImageBuilder::CircleImageBuilder(const Color& color,
                                       int radius) :
CanvasImageBuilder(radius*2 + 2, radius*2 + 2, true),
_color(color),
_radius(radius)
{

}

void CircleImageBuilder::buildOnCanvas(const G3MContext* context,
                                       ICanvas* canvas) {
  canvas->setFillColor(_color);
  canvas->fillEllipse(1, 1, _radius*2, _radius*2);
}

const std::string CircleImageBuilder::getImageName(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();

  return "_CircleImage_" + _color.id() + "_" + su->toString(_radius);
}
