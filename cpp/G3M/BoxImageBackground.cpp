//
//  BoxImageBackground.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/19/19.
//

#include "BoxImageBackground.hpp"

#include "Vector2I.hpp"
#include "IMathUtils.hpp"
#include "ICanvas.hpp"
#include "IStringBuilder.hpp"


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

// IStringBuilder keeps every float digit; IStringUtils::toString(float) keeps only 6 on iOS
const std::string BoxImageBackground::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("Box/");
  isb->addString(_margin.description());
  isb->addString("/");
  isb->addFloat(_borderWidth);
  isb->addString("/");
  isb->addString(_borderColor.id());
  isb->addString("/");
  isb->addString(_padding.description());
  isb->addString("/");
  isb->addString(_backgroundColor.id());
  isb->addString("/");
  isb->addFloat(_cornerRadius);
  const std::string s = isb->getString();
  delete isb;
  return s;
}

BoxImageBackground* BoxImageBackground::copy() const {
  return new BoxImageBackground(_margin,
                                _borderWidth,
                                _borderColor,
                                _padding,
                                _backgroundColor,
                                _cornerRadius);
}
