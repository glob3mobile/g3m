//
//  CompositeRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_CompositeRenderer_h
#define G3MiOSSDK_CompositeRenderer_h

#include "Renderer.hpp"
#include <vector>

class CompositeRenderer: public Renderer
{
private:
  std::vector<Renderer*> _renderers;
  int                    _renderersSize;
  
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  private G3MContext _context;
#endif

  bool _enable;
  
public:
  CompositeRenderer():
  _context(NULL),
  _enable(true),
  _renderersSize(0)
  {
//    _renderers = std::vector<Renderer*>();
  }
  
  virtual ~CompositeRenderer() {
    JAVA_POST_DISPOSE
  }
  
  bool isEnable() const;

  void setEnable(bool enable);
  
  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const G3MRenderContext* rc);

  void render(const G3MRenderContext* rc);
  
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

};

#endif
