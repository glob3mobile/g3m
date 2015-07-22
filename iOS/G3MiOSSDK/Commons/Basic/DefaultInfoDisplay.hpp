//
//  DefaultInfoDisplay.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 11/08/14.
//
//

#ifndef __G3MiOSSDK__DefaultInfoDisplay__
#define __G3MiOSSDK__DefaultInfoDisplay__

#include "InfoDisplay.hpp"
#include "DefaultRenderer.hpp"
#include "HUDImageRenderer.hpp"


class DefaultHUDInfoRenderer_ImageFactory : public HUDImageRenderer::CanvasImageFactory {
private:
  std::vector<const Info*> _info;
  
protected:
  
  void drawOn(ICanvas* canvas,
              int width,
              int height);
  
  bool isEquals(const std::vector<const Info*>& v1,
                const std::vector<const Info*>& v2) const;
  
public:
  ~DefaultHUDInfoRenderer_ImageFactory() {
  }
  
  bool setInfo(const std::vector<const Info*>& info);
};

class Default_HUDRenderer : public DefaultRenderer {
private:
 
  HUDImageRenderer* _hudImageRenderer;

public:
  
  Default_HUDRenderer();
  
  ~Default_HUDRenderer();
  
  void updateInfo(const std::vector<const Info*>& info);
  
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


class DefaultInfoDisplay : public InfoDisplay {
private:
  Default_HUDRenderer* _defaultHUDRenderer;
public:
  
  DefaultInfoDisplay(Default_HUDRenderer* defaultHUDRenderer):
  _defaultHUDRenderer(defaultHUDRenderer)
  {
    
  }
  
  void changedInfo(const std::vector<const Info*>& info){
    _defaultHUDRenderer->updateInfo(info);
  }
  
  void showDisplay() {
    _defaultHUDRenderer->setEnable(true);
  }
  
  void hideDisplay() {
    _defaultHUDRenderer->setEnable(false);
  }
  
  bool isShowing() {
    return _defaultHUDRenderer->isEnable();
  }
  
#ifdef C_CODE
  virtual ~DefaultInfoDisplay() { }
#endif
#ifdef JAVA_CODE
  public void dispose() { }
#endif
  
};


#endif /* defined(__G3MiOSSDK__DefaultInfoDisplay__) */
