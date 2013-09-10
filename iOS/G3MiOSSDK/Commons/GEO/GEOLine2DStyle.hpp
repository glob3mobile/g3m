//
//  GEOLine2DStyle.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/26/13.
//
//

#ifndef __G3MiOSSDK__GEOLine2DStyle__
#define __G3MiOSSDK__GEOLine2DStyle__

#include "GEOStyle.hpp"

#include "Color.hpp"


class GEOLine2DStyle : public GEOStyle {
private:
  const Color _color;
  const float _width;

public:
  GEOLine2DStyle(const Color& color,
                 const float width) :
  _color(color),
  _width(width)
  {

  }

  virtual ~GEOLine2DStyle() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  const Color getColor() const {
    return _color;
  }

  const float getWidth() const {
    return _width;
  }
  
};

#endif
