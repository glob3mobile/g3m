//
//  UnderlinedBoxImageBackground.cpp
//  G3M
//

#include "UnderlinedBoxImageBackground.hpp"

#include "IMathUtils.hpp"
#include "ICanvas.hpp"
#include "IStringBuilder.hpp"


UnderlinedBoxImageBackground::UnderlinedBoxImageBackground(const Vector2F& margin,
                                                           const Vector2F& padding,
                                                           const Color&    backgroundColor,
                                                           const float     cornerRadius,
                                                           const Color&    lineColor,
                                                           const float     lineWidth) :
_margin(margin),
_padding(padding),
_backgroundColor(backgroundColor),
_cornerRadius(cornerRadius),
_lineColor(lineColor),
_lineWidth(lineWidth)
{

}

const Vector2F UnderlinedBoxImageBackground::initializeCanvas(ICanvas* canvas,
                                                              const float contentWidth,
                                                              const float contentHeight) const {
  const IMathUtils* mu = IMathUtils::instance();

  const float canvasWidth  = contentWidth  + ((_margin._x + _padding._x) * 2);
  const float canvasHeight = contentHeight + ((_margin._y + _padding._y) * 2);

  canvas->initialize((int) mu->ceil(canvasWidth),
                     (int) mu->ceil(canvasHeight));

  const float boxWidth  = canvasWidth  - (_margin._x * 2);
  const float boxHeight = canvasHeight - (_margin._y * 2);
  const float boxBottom = _margin._y + boxHeight;

  if (!_backgroundColor.isFullTransparent()) {
    canvas->setFillColor(_backgroundColor);
    if (_cornerRadius > 0) {
      canvas->fillRoundedRectangle(_margin._x, _margin._y,
                                   boxWidth,   boxHeight,
                                   _cornerRadius);
      // squares the bottom corners
      canvas->fillRectangle(_margin._x, boxBottom - _cornerRadius,
                            boxWidth,   _cornerRadius);
    }
    else {
      canvas->fillRectangle(_margin._x, _margin._y,
                            boxWidth,   boxHeight);
    }
  }

  if ((_lineWidth > 0) && !_lineColor.isFullTransparent()) {
    canvas->setFillColor(_lineColor);
    canvas->fillRectangle(_margin._x, boxBottom - _lineWidth,
                          boxWidth,   _lineWidth);
  }

  return Vector2F(_margin._x + _padding._x,
                  _margin._y + _padding._y);
}

// IStringBuilder keeps every float digit; IStringUtils::toString(float) keeps only 6 on iOS
const std::string UnderlinedBoxImageBackground::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString("UnderlinedBox/");
  isb->addString(_margin.description());
  isb->addString("/");
  isb->addString(_padding.description());
  isb->addString("/");
  isb->addString(_backgroundColor.id());
  isb->addString("/");
  isb->addFloat(_cornerRadius);
  isb->addString("/");
  isb->addString(_lineColor.id());
  isb->addString("/");
  isb->addFloat(_lineWidth);
  const std::string s = isb->getString();
  delete isb;
  return s;
}

UnderlinedBoxImageBackground* UnderlinedBoxImageBackground::copy() const {
  return new UnderlinedBoxImageBackground(_margin,
                                          _padding,
                                          _backgroundColor,
                                          _cornerRadius,
                                          _lineColor,
                                          _lineWidth);
}
