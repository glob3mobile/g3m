//
//  MeshRenderer.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 02/08/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_MeshRenderer_h
#define G3MiOSSDK_MeshRenderer_h

#include "Renderer.hpp"


class MeshRenderer: public Renderer {
 
public:
  ~MeshRenderer() {}
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* touchEvent) {
    return false;
  };
  
  void onResizeViewportEvent(int width, int height) {}
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }

};

#endif
