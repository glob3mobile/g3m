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
#include "ICanvasUtils.hpp"

class ColumnCanvasElement : public GroupCanvasElement {
private:
  const HorizontalAlignment _elementAlign;
protected:
  Vector2F* calculateExtent(ICanvas* canvas);

public:
  ColumnCanvasElement(const Color& color = Color::transparent(),
                      float margin = 0,
                      float padding = 0,
                      float cornerRadius = 0,
                      const HorizontalAlignment elementAlign = Center) :
  GroupCanvasElement(color, margin, padding, cornerRadius),
  _elementAlign(elementAlign)
  {
  }

  virtual ~ColumnCanvasElement() {
#ifdef JAVA_CODE
  super.dispose();
#endif
  }

  void rawDrawAt(float left,
                 float top,
                 const Vector2F& extent,
                 ICanvas* canvas);
  
};

#endif
