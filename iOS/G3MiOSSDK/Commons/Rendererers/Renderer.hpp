//
//  IRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IRenderer_h
#define G3MiOSSDK_IRenderer_h

class TouchEvent;
class InitializationContext;
class RenderContext;
class EventContext;

class Renderer {
public:
  
  virtual void initialize(const InitializationContext* ic) = 0;
  
  virtual bool isReadyToRender(const RenderContext* rc) = 0;
  
  virtual void render(const RenderContext* rc) = 0;
  
  virtual bool onTouchEvent(const EventContext* ec,
                            const TouchEvent* touchEvent) = 0;
  
  virtual void onResizeViewportEvent(const EventContext* ec,
                                     int width, int height) = 0;
  
  virtual void start() = 0;
  
  virtual void stop() = 0;
  
  virtual ~Renderer() { };
};


#endif
