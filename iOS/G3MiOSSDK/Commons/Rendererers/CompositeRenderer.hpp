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
  
#ifdef C_CODE
  const G3MContext* _context;
#endif
#ifdef JAVA_CODE
  protected G3MContext _context;
#endif

  bool _enable;
  
public:
  CompositeRenderer():
  _context(NULL),
  _enable(true)
  {
    _renderers = std::vector<Renderer*>();
  }
  
  virtual ~CompositeRenderer() {
  }
  
  bool isEnable() const;

  void setEnable(bool enable);
  
  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const RenderContext* rc);

  void render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height);
  
  void addRenderer(Renderer* renderer);
  
  void start();
  
  void stop();

  void onResume(const G3MContext* context);
  
  void onPause(const G3MContext* context);

  void onDestroy(const G3MContext* context);
  
};

#endif
