//
//  ErrorRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/28/13.
//
//

#ifndef __G3MiOSSDK__ErrorRenderer__
#define __G3MiOSSDK__ErrorRenderer__

#include "ProtoRenderer.hpp"
#include <vector>


class ErrorRenderer : public ProtoRenderer {
public:
#ifdef C_CODE
  virtual ~ErrorRenderer() { }
#endif
#ifdef JAVA_CODE
  void dispose();
#endif

  virtual void setErrors(const std::vector<std::string>& errors) = 0;

};

#endif
