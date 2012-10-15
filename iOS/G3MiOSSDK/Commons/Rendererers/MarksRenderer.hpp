//
//  MarksRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 05/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_MarksRenderer_hpp
#define G3MiOSSDK_MarksRenderer_hpp

#include <vector>
#include "Renderer.hpp"
#include "Mark.hpp"

class MarksRenderer : public Renderer {
private:
  bool               _readyWhenMarksReady;
  std::vector<Mark*> _marks;
  
#ifdef C_CODE
  const InitializationContext* _initializationContext;
#endif
#ifdef JAVA_CODE
  private InitializationContext _initializationContext;
#endif
  
public:
  
  MarksRenderer(bool readyWhenMarksReady) :
  _readyWhenMarksReady(readyWhenMarksReady),
  _initializationContext(NULL)
  {
  }
  
  virtual ~MarksRenderer() {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      delete _marks[i];
    }
  };
  
  virtual void initialize(const InitializationContext* ic);
  
  virtual void render(const RenderContext* rc);
  
  void addMark(Mark* mark);
  
  virtual bool onTouchEvent(const EventContext* ec,
                            const TouchEvent* touchEvent);
  
  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {
    
  }
  
  bool isReadyToRender(const RenderContext* rc);
  
  void start() {
    
  }
  
  void stop() {
    
  }
  
  void onResume(const InitializationContext* ic) {
    
  }
  
  void onPause(const InitializationContext* ic) {
    
  }
  
};

#endif
