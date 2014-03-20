//
//  HUDRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/17/13.
//
//

#ifndef __G3MiOSSDK__HUDRenderer__
#define __G3MiOSSDK__HUDRenderer__

#include "DefaultRenderer.hpp"

#include <vector>

class HUDWidget;

class HUDRenderer : public DefaultRenderer {
private:

  std::vector<HUDWidget*> _widgets;
  int                     _widgetsSize;

  GLState*                _glState;

  std::vector<std::string> _errors;
  const bool _readyWhenWidgetsReady;

public:
  HUDRenderer(bool readyWhenWidgetsReady=true);

  ~HUDRenderer();

  void addWidget(HUDWidget* widget);

  void onChangedContext();

  RenderState getRenderState(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  void render(const G3MRenderContext* rc,
              GLState* glState);
  
};

#endif
