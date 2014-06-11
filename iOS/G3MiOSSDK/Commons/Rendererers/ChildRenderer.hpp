//
//  ChildRenderer.hpp
//  G3MiOSSDK
//
//  Created by Vidal Toboso on 22/04/14.
//
//

#ifndef __G3MiOSSDK__ChildRenderer__
#define __G3MiOSSDK__ChildRenderer__

#include "Renderer.hpp"

class ChildRenderer {
private:
  Renderer* _renderer;
  std::vector<std::string> _info;
  
public:
  
  ChildRenderer(Renderer* renderer):
  _renderer(renderer)
  {
    
  }
  
  ChildRenderer(Renderer* renderer, const std::vector<std::string>& info):
  _renderer(renderer),
  _info(info)
  {
    
  }
  
  ~ChildRenderer() {
    _renderer = NULL;
    _info.clear();
  }
  
  void addInfo(std::string& inf);
  
  void setInfo(const std::vector<std::string>& info);

  Renderer* getRenderer() const {
    return _renderer;
  }
  
  const std::vector<std::string> getInfo() const {
    return _info;
  }

};

#endif /* defined(__G3MiOSSDK__ChildRenderer__) */
