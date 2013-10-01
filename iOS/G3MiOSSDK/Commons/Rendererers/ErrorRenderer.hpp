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

  virtual ~ErrorRenderer() {
#ifdef JAVA_CODE
    super.dispose();
#endif
  }

};

#endif
