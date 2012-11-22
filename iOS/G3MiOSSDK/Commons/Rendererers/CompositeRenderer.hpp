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
  
  const Context* _context;
  
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
  
  void initialize(const Context* context);
  
  bool isReadyToRender(const RenderContext* rc);

  void render(const RenderContext* rc);
  
  bool onTouchEvent(const EventContext* ec,
                    const TouchEvent* touchEvent);

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height);
  
  void addRenderer(Renderer* renderer);
  
  void start();
  
  void stop();

  void onResume(const Context* context);
  
  void onPause(const Context* context);

  void onDestroy(const Context* context);
  
};

#endif
