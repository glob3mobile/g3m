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
  
  MarksRenderer(bool readyWhenMarksReady) :
  _readyWhenMarksReady(readyWhenMarksReady),
  _initializationContext(NULL),
  _lastCamera(NULL),
  _markTouchListener(NULL),
  _autoDeleteMarkTouchListener(false)
  {
  }
  
  void setMarkTouchListener(MarkTouchListener* markTouchListener,
                            bool autoDelete) {
    if ( _autoDeleteMarkTouchListener ) {
      delete _markTouchListener;
    }
    
    _markTouchListener = markTouchListener;
    _autoDeleteMarkTouchListener = autoDelete;
  }
  
  virtual ~MarksRenderer() {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      delete _marks[i];
    }
    
    if ( _autoDeleteMarkTouchListener ) {
      delete _markTouchListener;
    }
    _markTouchListener = NULL;
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
