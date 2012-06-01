//
//  IRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IRenderer_h
#define G3MiOSSDK_IRenderer_h

#include "Context.h"

#include "TouchEvent.h"

class Renderer {
public:
  virtual void initialize(const InitializationContext& ic) = 0;  
  
  virtual int render(const RenderContext& rc) = 0;
  
  virtual bool onTouchEvent(const TouchEvent& event) = 0;
  
  virtual ~Renderer() { };
};


#endif
