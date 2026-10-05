//
//  CircleImageFactory.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

#include "CircleImageFactory.hpp"
#include "ICanvas.hpp"
#include "G3MContext.hpp"
#include "IStringUtils.hpp"

CircleImageFactory::CircleImageFactory(const Color& color,
                                       int radius) :
CanvasImageFactory(radius*2 + 2, radius*2 + 2, true),
_color(color),
_radius(radius)
{
  
}

void CircleImageFactory::buildOnCanvas(const G3MContext* context,
                                       ICanvas* canvas) {
  canvas->setFillColor(_color);
  canvas->fillEllipse(1, 1, _radius*2, _radius*2);
}

const std::string CircleImageFactory::getImageName(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();
  
  return ("_CircleImage_" +
          getResolutionID(context) +
          "_" + _color.id() +
          "_" + su->toString(_radius));
}
