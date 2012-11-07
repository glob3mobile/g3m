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

#ifdef C_CODE
  const InitializationContext* _initializationContext;
#endif
#ifdef JAVA_CODE
  private InitializationContext _initializationContext;
#endif

public:

  ShapesRenderer() :
  _initializationContext(NULL)
  {

  }

  ~ShapesRenderer() {
    const int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      delete shape;
    }
  }

  void addShape(Shape* shape) {
    _shapes.push_back(shape);
    if (_initializationContext != NULL) {
      shape->initialize(_initializationContext);
    }
  }

  void onResume(const InitializationContext* ic) {
    _initializationContext = ic;
  }

  void onPause(const InitializationContext* ic) {
  }

  void initialize(const InitializationContext* ic) {
    _initializationContext = ic;

    const int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      shape->initialize(ic);
    }
  }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

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

  void render(const RenderContext* rc);

};

#endif
