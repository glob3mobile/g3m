//
//  TextCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#ifndef __G3MiOSSDK__TextCanvasElement__
#define __G3MiOSSDK__TextCanvasElement__

#include "CanvasElement.hpp"

#include <string>
#include "GFont.hpp"
#include "Color.hpp"

class TextCanvasElement : public CanvasElement {
private:
  const std::string _text;
  const GFont*      _font;
  const Color       _color;

public:
  TextCanvasElement(const std::string& text,
                    const GFont& font,
                    const Color& color) :
  _text(text),
  _font(new GFont(font)),
  _color(color)
  {

  }

  virtual ~TextCanvasElement() {
    delete _font;
    JAVA_POST_DISPOSE
  }

  const Vector2F getExtent(ICanvas* canvas);

  void drawAt(float left,
              float top,
              ICanvas* canvas);
  
};

#endif
