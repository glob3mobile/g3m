//
//  LeafRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#ifndef __G3MiOSSDK__LeafRenderer__
#define __G3MiOSSDK__LeafRenderer__

#include "Renderer.hpp"

class LeafRenderer : public Renderer {
private:
  bool _enable;
  
public:
  LeafRenderer() :
  _enable(true)
  {
    
  }
  
  LeafRenderer(bool enable) :
  _enable(enable)
  {
    
  }
  
  ~LeafRenderer() {
    
  }
  
  bool isEnable() const {
    return _enable;
  }
  
  void setEnable(bool enable) {
    _enable = enable;
  }
  
};

#endif
