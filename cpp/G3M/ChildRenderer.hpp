//
//  ChildRenderer.hpp
//  G3M
//
//  Created by Vidal Toboso on 22/04/14.
//
//

#ifndef __G3M__ChildRenderer__
#define __G3M__ChildRenderer__

#include "Renderer.hpp"

class ChildRenderer {
private:
  Renderer* _renderer;
  std::vector<const Info*> _info;

public:
  
  ChildRenderer(Renderer* renderer) :
  _renderer(renderer)
  {
    
  }
  
  ChildRenderer(Renderer* renderer,
                const std::vector<const Info*>& info):
  _renderer(renderer)
  {
    setInfo(info);
  }
  
  ~ChildRenderer() {
    delete _renderer;
    _info.clear();
  }
  
  void addInfo(const Info* inf);
  
  void setInfo(const std::vector<const Info*>& info);

  Renderer* getRenderer() const {
    return _renderer;
  }
  
  const std::vector<const Info*> getInfo() const {
    return _info;
  }

};

#endif
