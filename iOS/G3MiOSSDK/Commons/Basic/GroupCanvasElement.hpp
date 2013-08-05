//
//  GroupCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#ifndef __G3MiOSSDK__GroupCanvasElement__
#define __G3MiOSSDK__GroupCanvasElement__

#include "CanvasElement.hpp"

#include <vector>
#include "Color.hpp"

class GroupCanvasElement : public CanvasElement {
private:
  Vector2F* _extent;

protected:
  const Color                 _color;
  std::vector<CanvasElement*> _children;

  GroupCanvasElement(const Color& color) :
  _color(color),
  _extent(NULL)
  {

  }

  virtual void clearCaches();

  virtual Vector2F* calculateExtent(ICanvas* canvas) = 0;

public:
  virtual ~GroupCanvasElement();

  void add(CanvasElement* child);

  const Vector2F getExtent(ICanvas* canvas);

  virtual void drawAt(float left,
                      float top,
                      ICanvas* canvas) = 0;
  
};

#endif
