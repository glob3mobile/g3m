//
//  ColumnCanvasElement.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 4/12/13.
//
//

#ifndef __G3MiOSSDK__ColumnCanvasElement__
#define __G3MiOSSDK__ColumnCanvasElement__

#include "GroupCanvasElement.hpp"

class ColumnCanvasElement : public GroupCanvasElement {
protected:
  Vector2F* calculateExtent(ICanvas* canvas);

public:
  ColumnCanvasElement(const Color& color = Color::transparent()) :
  GroupCanvasElement(color)
  {

  }

  virtual ~ColumnCanvasElement() {
    JAVA_POST_DISPOSE
  }

  void drawAt(float left,
              float top,
              ICanvas* canvas);
  
};

#endif
