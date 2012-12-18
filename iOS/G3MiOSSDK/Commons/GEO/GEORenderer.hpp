//
//  GEORenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEORenderer__
#define __G3MiOSSDK__GEORenderer__

#include "LeafRenderer.hpp"

#include <vector>
class GEOObject;

class GEORenderer : public LeafRenderer {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif
  std::vector<GEOObject*> _children;

public:

  void addGEOObject(GEOObject* geoObject);
  
  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc,
              const GLState& parentState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void start() {

  }
  
  void stop() {
    
  }
  
};

#endif
