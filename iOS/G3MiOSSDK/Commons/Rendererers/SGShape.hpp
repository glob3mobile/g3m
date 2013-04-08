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

#include <string>

class SGNode;

class SGShape : public Shape {
private:
  SGNode* _node;
  const std::string _uriPrefix;
  
public:

  SGShape(SGNode* node,
          const std::string& uriPrefix) :
  Shape(NULL),
  _node(node),
  _uriPrefix(uriPrefix)
  {

  }

  const std::string getURIPrefix() const {
    return _uriPrefix;
  }

  void initialize(const G3MContext* context);
  
  bool isReadyToRender(const G3MRenderContext* rc);

  void rawRender(const G3MRenderContext* rc,
                 const GLState& parentState, const GPUProgramState* gpuParentProgramState);

  bool isTransparent(const G3MRenderContext* rc);

};

#endif
