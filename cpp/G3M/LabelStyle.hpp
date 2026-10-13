//
//  LabelStyle.hpp
//  G3M
//

#ifndef __G3M__LabelStyle__
#define __G3M__LabelStyle__

#include <string>

#include "GFont.hpp"
#include "Color.hpp"
#include "Vector2F.hpp"

class ImageBackground;
class ICanvas;


/**
 How a label's text is rendered: font, color, shadow and the background behind
 the text. A value: copies are independent (the background is copied too).
 */
class LabelStyle {
private:
  const GFont            _font;
  const Color            _color;
  const Color            _shadowColor;
  const float            _shadowBlur;
  const Vector2F         _shadowOffset;
  const ImageBackground* _background;

  static const ImageBackground* backgroundOrNull(const ImageBackground* background);

public:

  /** takes ownership of background; NULL means no background */
  LabelStyle(const GFont&           font,
             const Color&           color,
             const Color&           shadowColor,
             const float            shadowBlur,
             const Vector2F&        shadowOffset,
             const ImageBackground* background);

  LabelStyle(const LabelStyle& that);

  ~LabelStyle();

  static LabelStyle plain(const GFont& font,
                          const Color& color);

  static LabelStyle shadowed(const GFont&    font,
                             const Color&    color,
                             const Color&    shadowColor,
                             const float     shadowBlur,
                             const Vector2F& shadowOffset);

  /** text over a filled box with rounded corners, with padding between the text and the box */
  static LabelStyle boxed(const GFont&    font,
                          const Color&    color,
                          const Vector2F& padding,
                          const Color&    backgroundColor,
                          const float     cornerRadius);

  const GFont getFont() const {
    return _font;
  }

  const Color getColor() const {
    return _color;
  }

  bool hasShadow() const {
    return !_shadowColor.isFullTransparent();
  }

  const Color getShadowColor() const {
    return _shadowColor;
  }

  float getShadowBlur() const {
    return _shadowBlur;
  }

  const Vector2F getShadowOffset() const {
    return _shadowOffset;
  }

  /** draws the background sized to the text and answers where the text goes */
  const Vector2F initializeCanvas(ICanvas* canvas,
                                  const Vector2F& textExtent) const;

  /** a new background, owned by the caller */
  ImageBackground* copyBackground() const;

  LabelStyle copyWithFont(const GFont& font) const;

  LabelStyle copyWithoutBackground() const;

  const std::string description() const;
#ifdef JAVA_CODE
  @Override
  public String toString() {
    return description();
  }
#endif

};

#endif
