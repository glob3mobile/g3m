//
//  CircleImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/2/15.
//
//

#include "CircleImageBuilder.hpp"
#include "ICanvas.hpp"


CircleImageBuilder::CircleImageBuilder(const Color& color,
                                       int radius) :
CanvasImageBuilder(radius*2 + 2, radius*2 + 2),
_color(color),
_radius(radius)
{

}

void CircleImageBuilder::buildOnCanvas(const G3MContext* context,
                                       ICanvas* canvas) {
  const float width  = canvas->getWidth();
  const float height = canvas->getHeight();

  canvas->setFillColor(_color);
  canvas->fillEllipse(1, 1, width-2, height-2);
}
