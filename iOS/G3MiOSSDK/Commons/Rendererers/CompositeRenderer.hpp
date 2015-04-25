//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CompositeRenderer
#define G3MiOSSDK_CompositeRenderer

#include "ChildRenderer.hpp"
#include "Renderer.hpp"
#include "ILogger.hpp"
#include <vector>

class CompositeRenderer: public Renderer, ChangedRendererInfoListener
{
private:
  std::vector<const Info*> _info;
  std::vector<ChildRenderer*> _renderers;
  int                    _renderersSize;

#ifdef C_CODE
  const G3MContext* _context;
#else
  G3MContext* _context;
#endif

  bool _enable;

  std::vector<std::string> _errors;

  ChangedRendererInfoListener* _changedInfoListener;

  const std::vector<const Info*> getInfo();
  
public:
  CompositeRenderer():
  _context(NULL),
  _enable(true),
  _renderersSize(0),
  _changedInfoListener(NULL)
  {
    //    _renderers = std::vector<Renderer*>();
  }

  virtual ~CompositeRenderer() {

  }

  bool isEnable() const;

  void setEnable(bool enable);

  void initialize(const G3MContext* context);

  RenderState getRenderState(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc, GLState* glState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height);

  void addRenderer(Renderer* renderer);

  void addRenderer(Renderer* renderer,
                   const std::vector<const Info*>& info);
  
  void addChildRenderer(ChildRenderer* renderer);

  void start(const G3MRenderContext* rc);

  void stop(const G3MRenderContext* rc);

  void onResume(const G3MContext* context);

  void onPause(const G3MContext* context);

  void onDestroy(const G3MContext* context);

  SurfaceElevationProvider* getSurfaceElevationProvider();

  PlanetRenderer* getPlanetRenderer();

  virtual bool isPlanetRenderer() {
    return false;
  }
  
  void setChangedRendererInfoListener(ChangedRendererInfoListener* changedInfoListener, const int rendererIdentifier);
  
  void changedRendererInfo(const int rendererIdentifier,
                           const std::vector<const Info*>& info);
  
};

#endif
