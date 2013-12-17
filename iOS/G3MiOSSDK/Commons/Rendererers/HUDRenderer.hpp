//
//  HUDRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3MiOSSDK__HUDRenderer__
#define __G3MiOSSDK__HUDRenderer__

#include "LeafRenderer.hpp"

#include <vector>

class HUDWidget;

class HUDRenderer : public LeafRenderer {
private:
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  std::vector<HUDWidget*> _widgets;

  GLState*                _glState;

  std::vector<std::string> _errors;
  const bool _readyWhenWidgetsReady;

public:
  HUDRenderer(bool readyWhenWidgetsReady=true);

  ~HUDRenderer();

  void addWidget(HUDWidget* widget);

  void initialize(const G3MContext* context);

  RenderState getRenderState(const G3MRenderContext* rc);

  void onResume(const G3MContext* context) {
  }

  void onPause(const G3MContext* context) {
  }

  void onDestroy(const G3MContext* context) {
  }

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void start(const G3MRenderContext* rc) {
  }

  void stop(const G3MRenderContext* rc) {
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  void render(const G3MRenderContext* rc,
              GLState* glState);
  
};

#endif
