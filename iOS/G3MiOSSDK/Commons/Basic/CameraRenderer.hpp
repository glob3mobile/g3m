//
//  CameraRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 04/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CameraRenderer_h
#define G3MiOSSDK_CameraRenderer_h

#include "Renderer.hpp"

class CameraRenderer: public Renderer
{
  
  void initialize(const InitializationContext* ic);  
  
  int render(const RenderContext* rc);
  
  bool onTouchEvent(const TouchEvent* event);
  
  
};


#endif
