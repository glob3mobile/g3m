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
CanvasImageBuilder(width, height, false),
_backgroundColor(backgroundColor),
_boxColor(boxColor),
_splits(splits)
{
}


void DefaultChessCanvasImageBuilder::buildOnCanvas(const G3MContext* context,
                                                   ICanvas* canvas) {
  const float width  = canvas->getWidth();
  const float height = canvas->getHeight();


  canvas->setFillColor(_backgroundColor);
  canvas->fillRectangle(0, 0, width, height);

  if (!_boxColor.isFullTransparent()) {
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
}

const std::string DefaultChessCanvasImageBuilder::getImageName(const G3MContext* context) const {
  const IStringUtils* su = context->getStringUtils();

  return ("_DefaultChessCanvasImage_" +
          getResolutionID(context) +
          "_" + _backgroundColor.id() +
          "_" + _boxColor.id() +
          "_" + su->toString(_splits));
}
