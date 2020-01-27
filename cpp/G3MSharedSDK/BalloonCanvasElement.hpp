//
//  BalloonCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#ifndef __G3MiOSSDK__BalloonCanvasElement__
#define __G3MiOSSDK__BalloonCanvasElement__

#include "CanvasElement.hpp"
#include "Color.hpp"

class BalloonCanvasElement : public CanvasElement {
private:
  CanvasElement* _child;

  const Color _color;
  const float _arrowLength;
  const float _margin;
  const float _radius;
  const float _arrowPointSize;

public:
  BalloonCanvasElement(CanvasElement* child,
                       const Color& color = Color::white(),
                       float margin = 5,
                       float radius = 10,
                       float arrowLength = 5,
                       float arrowPointSize = 12) :
  _child(child),
  _color(color),
  _margin(margin),
  _radius(radius),
  _arrowLength(arrowLength),
  _arrowPointSize(arrowPointSize)
  {

  }

  ~BalloonCanvasElement() {
    delete _child;
    
#ifdef JAVA_CODE
  super.dispose();
#endif

  }

  const Vector2F getExtent(ICanvas* canvas);

  void drawAt(float left,
              float top,
              ICanvas* canvas);

};

#endif
