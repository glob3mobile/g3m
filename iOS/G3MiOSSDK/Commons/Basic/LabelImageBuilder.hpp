//
//  LabelImageBuilder.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/3/14.
//
//

#ifndef __G3MiOSSDK__LabelImageBuilder__
#define __G3MiOSSDK__LabelImageBuilder__

#include "IImageBuilder.hpp"

#include "GFont.hpp"
#include "Color.hpp"

class LabelImageBuilder : public IImageBuilder {
private:
  const std::string _text;
#ifdef C_CODE
  const GFont       _font;
#endif
#ifdef JAVA_CODE
  private final GFont _font;
#endif
  const Color       _color;
  const float       _margin;

public:

  LabelImageBuilder(const std::string& text,
                    const GFont&       font   = GFont::sansSerif(),
                    const Color&       color  = Color::white(),
                    const float        margin = 0) :
  _text(text),
  _font(font),
  _color(color),
  _margin(margin)
  {
  }

  ~LabelImageBuilder() {
  }

  void build(const G3MContext* context,
             IImageBuilderListener* listener,
             bool deleteListener);

  const std::string getImageName();

};

#endif
