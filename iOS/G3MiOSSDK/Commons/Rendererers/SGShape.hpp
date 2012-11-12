//
//  SGShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGShape__
#define __G3MiOSSDK__SGShape__

#include "Shape.hpp"

class SGNode;

class SGShape : public Shape {
private:
  SGNode* _node;

public:

  SGShape(SGNode* node) :
  Shape(NULL),
  _node(node)
  {

  }

  void initialize(const InitializationContext* ic);
  
  bool isReadyToRender(const RenderContext* rc);

  void rawRender(const RenderContext* rc);

};

#endif
