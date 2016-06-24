//
//  DefaultChessCanvasImageBuilder.cpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 21/08/14.
//
//

#include "DefaultChessCanvasImageBuilder.hpp"
#include "G3MContext.hpp"
#include "ICanvas.hpp"
#include "IStringUtils.hpp"


DefaultChessCanvasImageBuilder::DefaultChessCanvasImageBuilder(int width,
                                                               int height,
                                                               const Color& backgroundColor,
                                                               const Color& boxColor,
                                                               int splits) :
<<<<<<< HEAD
<<<<<<< HEAD
  CanvasImageBuilder(width, height, false),
  _backgroundColor(backgroundColor),
  _boxColor(boxColor),
  _splits(splits)
=======
CanvasImageBuilder(width, height),
=======
CanvasImageBuilder(width, height, false),
>>>>>>> 05b769f... -
_backgroundColor(backgroundColor),
_boxColor(boxColor),
_splits(splits)
>>>>>>> 882166c33bdf9946c54ea507ad5e1c47fb3e83e0
{
}


void DefaultChessCanvasImageBuilder::buildOnCanvas(const G3MContext* context,
                                                   ICanvas* canvas) {
  const float width  = canvas->getWidth();
  const float height = canvas->getHeight();


  canvas->setFillColor(_backgroundColor);
  canvas->fillRectangle(0, 0, width, height);

  canvas->setFillColor(_boxColor);

  const float xInterval = (float) width  / _splits;
  const float yInterval = (float) height / _splits;

  for (int col = 0; col < _splits; col += 2) {
    const float x  = col * xInterval;
    const float x2 = (col + 1) * xInterval;
    for (int row = 0; row < _splits; row += 2) {
      const float y  = row * yInterval;
      const float y2 = (row + 1) * yInterval;

      canvas->fillRoundedRectangle(x + 2,
                                   y + 2,
                                   xInterval - 4,
                                   yInterval - 4,
                                   4);
      canvas->fillRoundedRectangle(x2 + 2,
                                   y2 + 2,
                                   xInterval - 4,
                                   yInterval - 4,
                                   4);
    }
  }
}

std::string DefaultChessCanvasImageBuilder::getImageName(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();

  return "_DefaultChessCanvasImage_" +
          su->toString(_width) +
          "_" + su->toString(_height) +
          "_" + _backgroundColor.toID() +
          "_" + _boxColor.toID() +
          "_" + su->toString(_splits);
}
