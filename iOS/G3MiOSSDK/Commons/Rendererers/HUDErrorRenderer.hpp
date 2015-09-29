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


class ErrorMessagesCustomizer {
public:
#ifdef C_CODE
  virtual ~ErrorMessagesCustomizer() {}
#endif
  
  virtual std::vector<std::string> customize(const std::vector<std::string>& errors) = 0;
};


class HUDErrorRenderer : public ErrorRenderer {
private:
  HUDImageRenderer* _hudImageRenderer;
  ErrorMessagesCustomizer* _errorMessageCustomizer;

public:

  HUDErrorRenderer(ErrorMessagesCustomizer* errorMessageCustomizer=NULL);

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

  void zRender(const G3MRenderContext* rc, GLState* glState){}
  
};

#endif
