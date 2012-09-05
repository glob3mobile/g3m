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
  std::vector<Mark*> _marks;
  
public:

  virtual void initialize(const InitializationContext* ic);
  
  virtual void render(const RenderContext* rc);
  
  virtual ~MarksRenderer() {
    int marksSize = _marks.size();
    for (int i = 0; i < marksSize; i++) {
      delete _marks[i];
    }
  };

  void addMark(Mark* mark) {
    _marks.push_back(mark);
  }
  
  virtual bool onTouchEvent(const EventContext* ec,
                            const TouchEvent* touchEvent);

  void onResizeViewportEvent(const EventContext* ec,
                             int width, int height) {
    
  }
  
  bool isReadyToRender(const RenderContext* rc) {
    return true;
  }
  
  void start() {
    
  }
  
  void stop() {
    
  }
  
};

#endif
