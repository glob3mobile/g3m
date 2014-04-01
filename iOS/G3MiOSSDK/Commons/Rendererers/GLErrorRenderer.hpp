//
//  GLErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_GLErrorRenderer
#define G3MiOSSDK_GLErrorRenderer

#include "DefaultRenderer.hpp"

class GLErrorRenderer : public DefaultRenderer {
public:
  
  void render(const G3MRenderContext* rc,
              GLState* glState);
  
  ~GLErrorRenderer() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }
  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height){

  }

  
  
};

#endif
