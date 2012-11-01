//
//  LeafRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#ifndef __G3MiOSSDK__LeafRenderer__
#define __G3MiOSSDK__LeafRenderer__

#include "Renderer.hpp"

class LeafRenderer : public Renderer {
private:
  bool _enable;
  
public:
  LeafRenderer() :
  _enable(true)
  {
    
  }
  
  LeafRenderer(bool enable) :
  _enable(enable)
  {
    
  }
  
  ~LeafRenderer() {
    
  }
  
  bool isEnable() const {
    return _enable;
  }

#ifdef C_CODE
  void setEnable(bool enable) {
    _enable = enable;
  }
#endif
#ifdef JAVA_CODE
  public void setEnable(final boolean enable) {
    _enable = enable;
  }
#endif
  
  virtual void onResume(const InitializationContext* ic) = 0;
  
  virtual void onPause(const InitializationContext* ic) = 0;
  
  virtual void initialize(const InitializationContext* ic) = 0;
  
  virtual bool isReadyToRender(const RenderContext* rc) = 0;
  
  virtual void render(const RenderContext* rc) = 0;
  
  virtual bool onTouchEvent(const EventContext* ec,
                            const TouchEvent* touchEvent) = 0;
  
  virtual void onResizeViewportEvent(const EventContext* ec,
                                     int width, int height) = 0;
  
  virtual void start() = 0;
  
  virtual void stop() = 0;

};

#endif
