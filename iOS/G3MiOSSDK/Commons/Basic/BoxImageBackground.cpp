//
//  BoxImageBackground.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#include "BoxImageBackground.hpp"

#include "Vector2I.hpp"
#include "IMathUtils.hpp"
#include "ICanvas.hpp"
#include "IStringUtils.hpp"


BoxImageBackground::BoxImageBackground(const Vector2F& margin,
                                       const float     borderWidth,
                                       const Color&    borderColor,
                                       const Vector2F& padding,
                                       const Color&    backgroundColor,
                                       const float     cornerRadius) :
_margin(margin),
_borderWidth(borderWidth),
_borderColor(borderColor),
_padding(padding),
_backgroundColor(backgroundColor),
_cornerRadius(cornerRadius)
{

}

const Vector2F BoxImageBackground::initializeCanvas(ICanvas* canvas,
                                                    const float contentWidth,
                                                    const float contentHeight) const {
  const IMathUtils* mu = IMathUtils::instance();

  const float canvasWidth  = contentWidth  + ((_margin._x + _borderWidth + _padding._x) * 2);
  const float canvasHeight = contentHeight + ((_margin._y + _borderWidth + _padding._y) * 2);

  canvas->initialize((int) mu->ceil(canvasWidth),
                     (int) mu->ceil(canvasHeight));

  const float boxWidth  = canvasWidth  - ((_margin._x + _borderWidth) * 2);
  const float boxHeight = canvasHeight - ((_margin._y + _borderWidth) * 2);

//#warning remove debug code
//  canvas->setFillColor(Color::red());
//  canvas->fillRectangle(0, 0, canvasWidth, canvasHeight);


  if (!_backgroundColor.isFullTransparent()) {
    canvas->setFillColor(_backgroundColor);
    if (_cornerRadius > 0) {
      canvas->fillRoundedRectangle(_margin._x, _margin._y,
                                   boxWidth,   boxHeight,
                                   _cornerRadius);
    }
    else {
      canvas->fillRectangle(_margin._x, _margin._y,
                            boxWidth,   boxHeight);
    }
  }

  if (_borderWidth > 0 && !_borderColor.isFullTransparent()) {
    canvas->setLineColor(_borderColor);
    canvas->setLineWidth(_borderWidth);
    if (_cornerRadius > 0) {
      canvas->strokeRoundedRectangle(_margin._x, _margin._y,
                                     boxWidth,   boxHeight,
                                     _cornerRadius);
    }
    else {
      canvas->strokeRectangle(_margin._x, _margin._y,
                              boxWidth,   boxHeight);
    }
  }

  const Vector2F contentPosition((_margin._x + _borderWidth + _padding._x),
                                 (_margin._y + _borderWidth + _padding._y));
  return contentPosition;
}

const std::string BoxImageBackground::description() const {
  const IStringUtils* su = IStringUtils::instance();
  return (_margin.description()      + "/" +
          su->toString(_borderWidth) + "/" +
          _borderColor.id()          + "/" +
          _padding.description()     + "/" +
          _backgroundColor.id()      + "/" +
          su->toString(_cornerRadius));
}
