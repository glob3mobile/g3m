//
//  LabelStyle.cpp
//  G3M
//

#include "LabelStyle.hpp"

#include "ImageBackground.hpp"
#include "NullImageBackground.hpp"
#include "BoxImageBackground.hpp"
#include "IStringBuilder.hpp"


const ImageBackground* LabelStyle::backgroundOrNull(const ImageBackground* background) {
  return (background == NULL) ? new NullImageBackground() : background;
}

LabelStyle::LabelStyle(const GFont&           font,
                       const Color&           color,
                       const Color&           shadowColor,
                       const float            shadowBlur,
                       const Vector2F&        shadowOffset,
                       const ImageBackground* background) :
_font(font),
_color(color),
_shadowColor(shadowColor),
_shadowBlur(shadowBlur),
_shadowOffset(shadowOffset),
_background(backgroundOrNull(background))
{
}

LabelStyle::LabelStyle(const LabelStyle& that) :
_font(that._font),
_color(that._color),
_shadowColor(that._shadowColor),
_shadowBlur(that._shadowBlur),
_shadowOffset(that._shadowOffset),
_background(that._background->copy())
{
}

LabelStyle::~LabelStyle() {
  delete _background;
}

LabelStyle LabelStyle::plain(const GFont& font,
                             const Color& color) {
  return LabelStyle(font, color, Color::TRANSPARENT, 0, Vector2F::ZERO, NULL);
}

LabelStyle LabelStyle::shadowed(const GFont&    font,
                                const Color&    color,
                                const Color&    shadowColor,
                                const float     shadowBlur,
                                const Vector2F& shadowOffset) {
  return LabelStyle(font, color, shadowColor, shadowBlur, shadowOffset, NULL);
}

LabelStyle LabelStyle::boxed(const GFont&    font,
                             const Color&    color,
                             const Vector2F& padding,
                             const Color&    backgroundColor,
                             const float     cornerRadius) {
  return LabelStyle(font,
                    color,
                    Color::TRANSPARENT,
                    0,
                    Vector2F::ZERO,
                    new BoxImageBackground(Vector2F::ZERO,     /* margin      */
                                           0,                  /* borderWidth */
                                           Color::TRANSPARENT, /* borderColor */
                                           padding,
                                           backgroundColor,
                                           cornerRadius));
}

LabelStyle LabelStyle::copyWithFont(const GFont& font) const {
  return LabelStyle(font, _color, _shadowColor, _shadowBlur, _shadowOffset, _background->copy());
}

LabelStyle LabelStyle::copyWithoutBackground() const {
  return LabelStyle(_font, _color, _shadowColor, _shadowBlur, _shadowOffset, NULL);
}

const Vector2F LabelStyle::initializeCanvas(ICanvas* canvas,
                                            const Vector2F& textExtent) const {
  return _background->initializeCanvas(canvas, textExtent);
}

ImageBackground* LabelStyle::copyBackground() const {
  return _background->copy();
}

// IStringBuilder keeps every float digit; IStringUtils::toString(float) keeps only 6 on iOS
const std::string LabelStyle::description() const {
  IStringBuilder* isb = IStringBuilder::newStringBuilder();
  isb->addString(_font.description());
  isb->addString("/");
  isb->addString(_color.id());
  isb->addString("/");
  isb->addString(_shadowColor.id());
  isb->addString("/");
  isb->addFloat(_shadowBlur);
  isb->addString("/");
  isb->addString(_shadowOffset.description());
  isb->addString("/");
  isb->addString(_background->description());
  const std::string s = isb->getString();
  delete isb;
  return s;
}
