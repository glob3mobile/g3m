//
//  WrapperNearFrustumRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/27/17.
//
//

#ifndef WrapperNearFrustumRenderer_hpp
#define WrapperNearFrustumRenderer_hpp

#include "NearFrustumRenderer.hpp"


class WrapperNearFrustumRenderer : public NearFrustumRenderer {
private:
  const double _zNear;
  Renderer* _renderer;


public:
  WrapperNearFrustumRenderer(const double zNear,
                             Renderer* renderer);

  ~WrapperNearFrustumRenderer();

  void initialize(const G3MContext* context);

  void start(const G3MRenderContext* rc);

  void stop(const G3MRenderContext* rc);

  void onResume(const G3MContext* context);

  void onPause(const G3MContext* context);

  void onDestroy(const G3MContext* context);

  bool isEnable() const;

  void setEnable(bool enable);

  RenderState getRenderState(const G3MRenderContext* rc);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width,
                             int height);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener,
                                      const size_t rendererID);

  void render(const G3MRenderContext* rc,
              GLState* glState);

  void render(const FrustumData* currentFrustumData,
              FrustumPolicyHandler* handler,
              const G3MRenderContext* rc,
              GLState* glState);
  
};

#endif
