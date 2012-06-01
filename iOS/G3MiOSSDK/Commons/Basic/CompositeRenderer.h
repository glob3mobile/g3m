//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CompositeRenderer_h
#define G3MiOSSDK_CompositeRenderer_h

#include "Renderer.h"

#include <vector>

class CompositeRenderer: public Renderer
{
private:
  std::vector<Renderer*> _renderers;
  
  InitializationContext _ic;
  
public:
  CompositeRenderer(): _ic(NULL) {
    
  }
  
  void initialize(InitializationContext& ic);  
  
  int render(RenderContext &rc);
  
  bool onTouchEvent(TouchEvent &event);
  
  void addRenderer(Renderer* renderer);
};

#endif
