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
  Vector2F* _rawExtent;

protected:
  Color _color;
  float _margin;
  float _padding;
  float _cornerRadius;

  std::vector<CanvasElement*> _children;

  GroupCanvasElement(const Color& color,
                     float margin,
                     float padding,
                     float cornerRadius) :
  _color(color),
  _margin(margin),
  _padding(padding),
  _cornerRadius(cornerRadius),
  _extent(NULL),
  _rawExtent(NULL)
  {

  }

  virtual void clearCaches();

  virtual Vector2F* calculateExtent(ICanvas* canvas) = 0;

public:
  virtual ~GroupCanvasElement();

  void add(CanvasElement* child);

  const Vector2F getExtent(ICanvas* canvas);

  void drawAt(float left,
              float top,
              ICanvas* canvas);

  virtual void rawDrawAt(float left,
                         float top,
                         const Vector2F& extent,
                         ICanvas* canvas) = 0;

};

#endif
