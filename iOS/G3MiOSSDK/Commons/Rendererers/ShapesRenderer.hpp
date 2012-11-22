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
  const Context* _context;
#endif
#ifdef JAVA_CODE
  private Context _context;
#endif

public:

  ShapesRenderer() :
  _context(NULL)
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
    if (_context != NULL) {
      shape->initialize(_context);
    }
  }

  void onResume(const Context* context) {
    _context = context;
  }

  void onPause(const Context* context) {

  }

  void onDestroy(const Context* context) {

  }

  void initialize(const Context* context) {
    _context = context;

    const int shapesCount = _shapes.size();
    for (int i = 0; i < shapesCount; i++) {
      Shape* shape = _shapes[i];
      shape->initialize(context);
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
