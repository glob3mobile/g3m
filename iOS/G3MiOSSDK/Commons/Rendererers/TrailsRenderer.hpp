//
//  TrailsRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/23/12.
//
//

#ifndef __G3MiOSSDK__TrailsRenderer__
#define __G3MiOSSDK__TrailsRenderer__

#include "LeafRenderer.hpp"

#include <vector>

class Trail {
private:
  bool _visible;
  const int _maxSteps;
  
public:
  Trail(int maxSteps):
  _maxSteps(maxSteps),
  _visible(true) {
  }
  
  void render(const RenderContext* rc);
  
  void setVisible(bool visible) {
    _visible = visible;
  }
  
  bool isVisible() const {
    return _visible;
  }
  
};


class TrailsRenderer : public LeafRenderer {
private:
  std::vector<Trail*> _trails;
  
public:
  TrailsRenderer() {
  }
  
  void addTrail(Trail* trail);
  
  virtual ~TrailsRenderer();
  
  void onResume(const InitializationContext* ic) {
    
  }
  
  void onPause(const InitializationContext* ic) {
    
  }
  
  void initialize(const InitializationContext* ic) {
    
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
