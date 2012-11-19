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
#include "LeafRenderer.hpp"
#include "Mark.hpp"

class GLState;

class MarkTouchListener {
public:
  virtual ~MarkTouchListener() {
    
  }
  
  virtual bool touchedMark(Mark* mark) = 0;
};


class MarksRenderer : public LeafRenderer {
private:
  const bool         _readyWhenMarksReady;
  std::vector<Mark*> _marks;
  GLState*           _glState;
  
#ifdef C_CODE
  const InitializationContext* _initializationContext;
  const Camera*                _lastCamera;
#endif
#ifdef JAVA_CODE
  private InitializationContext _initializationContext;
  private Camera                _lastCamera;
#endif
  
  MarkTouchListener* _markTouchListener;
  bool               _autoDeleteMarkTouchListener;
  
public:
  
  MarksRenderer(bool readyWhenMarksReady);  
  void setMarkTouchListener(MarkTouchListener* markTouchListener,
                            bool autoDelete) {
    if ( _autoDeleteMarkTouchListener ) {
      delete _markTouchListener;
    }
    
    _markTouchListener = markTouchListener;
    _autoDeleteMarkTouchListener = autoDelete;
  }
  
  virtual ~MarksRenderer();
  
  virtual void initialize(const InitializationContext* ic);
  
  virtual void render(const RenderContext* rc);
  
  void addMark(Mark* mark);
  
  bool onTouchEvent(const EventContext* ec,
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
    _initializationContext = ic;
  }
  
  void onPause(const InitializationContext* ic) {
    
  }
  
};

#endif
