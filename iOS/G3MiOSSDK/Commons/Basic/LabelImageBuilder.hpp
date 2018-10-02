//
//  LabelImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3MiOSSDK__LabelImageBuilder__
#define __G3MiOSSDK__LabelImageBuilder__

#include "AbstractImageBuilder.hpp"

#include "GFont.hpp"
#include "Color.hpp"
#include "Vector2F.hpp"


class LabelImageBuilder : public AbstractImageBuilder {
private:
  std::string _text;
#ifdef C_CODE
  const GFont _font;
  const Vector2F _margin;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
  private final Vector2F _margin;
#endif
  const Color _color;

  const Color _shadowColor;
  const float _shadowBlur;
#ifdef C_CODE
  const Vector2F _shadowOffset;
#endif
#ifdef JAVA_CODE
  private final Vector2F _shadowOffset;
#endif

  const Color _backgroundColor;
  const float _cornerRadius;

  const bool  _isMutable;

  const std::string getImageName() const;

public:

  LabelImageBuilder(const std::string& text,
                    const GFont&       font            = GFont::sansSerif(),
                    const Vector2F&    margin          = Vector2F::zero(),
                    const Color&       color           = Color::white(),
                    const Color&       shadowColor     = Color::transparent(),
                    const float        shadowBlur      = 0,
                    const Vector2F&    shadowOffset    = Vector2F::zero(),
                    const Color&       backgroundColor = Color::transparent(),
                    const float        cornerRadius    = 0,
                    const bool         isMutable       = false) :
  _text(text),
  _font(font),
  _margin(margin),
  _color(color),
  _shadowColor(shadowColor),
  _shadowBlur(shadowBlur),
  _shadowOffset(shadowOffset),
  _backgroundColor(backgroundColor),
  _cornerRadius(cornerRadius),
  _isMutable(isMutable)
  {
  }

  bool isMutable() const {
    return _isMutable;
  }

  void setText(const std::string& text);

  ~LabelImageBuilder() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);


};

#endif
