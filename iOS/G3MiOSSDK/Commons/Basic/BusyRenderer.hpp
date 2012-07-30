//
//  BusyRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_BusyRenderer_hpp
#define G3MiOSSDK_BusyRenderer_hpp

#include "Renderer.hpp"

class BusyRenderer : public Renderer {
public:
  void initialize(const InitializationContext* ic) {
    
  }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent) {
    return false;
  }
  
  void onResizeViewportEvent(int width, int height) {
    
  }
  
  virtual ~BusyRenderer() { };

};

#endif
