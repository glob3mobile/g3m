//
//  OrderedRenderable.h
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/13/12.
//
//

#ifndef __G3MiOSSDK__OrderedRenderable__
#define __G3MiOSSDK__OrderedRenderable__

#include "Context.hpp"
class GLGlobalState;
class GPUProgramState;
class GLState;

class OrderedRenderable {
public:
  virtual double squaredDistanceFromEye() const = 0;

  virtual void render(const G3MRenderContext* rc) = 0;

  virtual ~OrderedRenderable() {
  }
};

#endif
