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

class LabelImageBuilder : public AbstractImageBuilder {
private:
  std::string _text;
#ifdef C_CODE
  const GFont _font;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
#endif
  const float _margin;
  const Color _color;

  const Color _shadowColor;
  const float _shadowBlur;
  const float _shadowOffsetX;
  const float _shadowOffsetY;

  const Color _backgroundColor;
  const float _cornerRadius;

  const bool  _isMutable;

  const std::string getImageName() const;

public:

  LabelImageBuilder(const std::string& text,
                    const GFont&       font            = GFont::sansSerif(),
                    const float        margin          = 0,
                    const Color&       color           = Color::white(),
                    const Color&       shadowColor     = Color::transparent(),
                    const float        shadowBlur      = 0,
                    const float        shadowOffsetX   = 0,
                    const float        shadowOffsetY   = 0,
                    const Color&       backgroundColor = Color::transparent(),
                    const float        cornerRadius    = 0,
                    const bool         isMutable       = false) :
  _text(text),
  _font(font),
  _margin(margin),
  _color(color),
  _shadowColor(shadowColor),
  _shadowBlur(shadowBlur),
  _shadowOffsetX(shadowOffsetX),
  _shadowOffsetY(shadowOffsetY),
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
