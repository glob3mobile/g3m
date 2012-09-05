//
//  GLErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_GLErrorRenderer_hpp
#define G3MiOSSDK_GLErrorRenderer_hpp

#include "Renderer.hpp"

class GLErrorRenderer : public Renderer {
public:
  
  void initialize(const InitializationContext* ic);
  
  void render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                            const TouchEvent* touchEvent);
  
  virtual ~GLErrorRenderer();
  
  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height);
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  void start() {
    
  }
  
  void stop() {
    
  }

};

#endif
