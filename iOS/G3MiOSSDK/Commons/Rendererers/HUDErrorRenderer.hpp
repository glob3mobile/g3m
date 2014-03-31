//
//  HUDErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#ifndef __G3MiOSSDK__HUDErrorRenderer__
#define __G3MiOSSDK__HUDErrorRenderer__

#include "ErrorRenderer.hpp"

class HUDImageRenderer;


class HUDErrorRenderer : public ErrorRenderer {
private:
  HUDImageRenderer* _hudImageRenderer;

public:

  HUDErrorRenderer();

  ~HUDErrorRenderer() {

  }

  void setErrors(const std::vector<std::string>& errors);

  void initialize(const G3MContext* context);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void start(const G3MRenderContext* rc);

  void stop(const G3MRenderContext* rc);


  void onResume(const G3MContext* context);

  void onPause(const G3MContext* context);

  void onDestroy(const G3MContext* context);
  
};

#endif
