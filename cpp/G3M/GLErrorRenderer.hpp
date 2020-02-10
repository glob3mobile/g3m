//
//  GLErrorRenderer.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 20/06/12.
//

#ifndef G3M_GLErrorRenderer
#define G3M_GLErrorRenderer

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
                             int width, int height) {

  }

  
  
};

#endif
