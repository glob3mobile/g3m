//
//  ShapesRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#ifndef __G3MiOSSDK__ShapesRenderer__
#define __G3MiOSSDK__ShapesRenderer__

#include "LeafRenderer.hpp"
#include "Shape.hpp"
#include <vector>

class ShapesRenderer : public LeafRenderer {
private:
  std::vector<Shape*> _shapes;

public:

  ~ShapesRenderer() {
    const int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      delete shape;
    }
  }

  void addShape(Shape* shape) {
    _shapes.push_back(shape);
  }

  void onResume(const InitializationContext* ic) {

  }

  void onPause(const InitializationContext* ic) {

  }

  void initialize(const InitializationContext* ic) {
    
  }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

  void render(const RenderContext* rc);

  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {

  }

  void start() {
    
  }
  
  void stop() {
    
  }
  
};

#endif
