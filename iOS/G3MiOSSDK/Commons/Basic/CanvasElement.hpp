//
//  CanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#ifndef __G3MiOSSDK__CanvasElement__
#define __G3MiOSSDK__CanvasElement__


#include "Vector2F.hpp"
class ICanvas;

class CanvasElement {
private:
  CanvasElement(const CanvasElement& that);

public:
  CanvasElement() {

  }

  virtual ~CanvasElement() {
  }

  virtual const Vector2F getExtent(ICanvas* canvas) = 0;

  virtual void drawAt(float left,
                      float top,
                      ICanvas* canvas) = 0;

  virtual void drawCentered(ICanvas* canvas);
  
};

#endif
