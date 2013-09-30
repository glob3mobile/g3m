//
//  ErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#ifndef __G3MiOSSDK__ErrorRenderer__
#define __G3MiOSSDK__ErrorRenderer__

#include "Renderer.hpp"

class ErrorRenderer : public Renderer {
public:

  virtual void setErrors(const std::vector<std::string>& errors) = 0;

//  virtual void initialize(const G3MContext* context) = 0;
//
//  virtual void render(const G3MRenderContext* rc,
//                      GLState* glState) = 0;
//
//  virtual bool onTouchEvent(const G3MEventContext* ec,
//                            const TouchEvent* touchEvent) = 0;
//
//  virtual void onResizeViewportEvent(const G3MEventContext* ec,
//                                     int width, int height) = 0;
//
//  virtual void start(const G3MRenderContext* rc) = 0;
//
//  virtual void stop(const G3MRenderContext* rc) = 0;

//#ifdef C_CODE
//  virtual ~ErrorRenderer() { }
//#endif
//#ifdef JAVA_CODE
//  public void dispose();
//#endif

#ifdef C_CODE
  virtual ~ErrorRenderer() { }
#else
  void dispose();
#endif


//  virtual void onResume(const G3MContext* context) = 0;
//
//  virtual void onPause(const G3MContext* context) = 0;
//
//  virtual void onDestroy(const G3MContext* context) = 0;

};

#endif
