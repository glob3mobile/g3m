//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CompositeRenderer
#define G3MiOSSDK_CompositeRenderer

#include "Renderer.hpp"
#include <vector>

class CompositeRenderer: public Renderer
{
private:
  std::vector<Renderer*> _renderers;
  int                    _renderersSize;

#ifdef C_CODE
  const G3MContext* _context;
#else
  G3MContext* _context;
#endif

  bool _enable;

  std::vector<std::string> _errors;

public:
  CompositeRenderer():
  _context(NULL),
  _enable(true),
  _renderersSize(0)
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

};

#endif
