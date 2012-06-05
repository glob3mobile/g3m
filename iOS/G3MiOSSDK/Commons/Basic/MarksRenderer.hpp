//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_MarksRenderer_hpp
#define G3MiOSSDK_MarksRenderer_hpp


#include "Renderer.hpp"

class MarksRenderer : public Renderer {
public:

  virtual void initialize(const InitializationContext* ic);
  
  virtual int render(const RenderContext* rc);
  
  virtual bool onTouchEvent(const TouchEvent* event);
  
  virtual ~MarksRenderer() { };

};

#endif
