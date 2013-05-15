//
//  SGTranslateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGTranslateNode__
#define __G3MiOSSDK__SGTranslateNode__

#include "SGNode.hpp"

class SGTranslateNode : public SGNode {
private:
  const double _x;
  const double _y;
  const double _z;
  
  MutableMatrix44D _translationMatrix;

public:

  SGTranslateNode(const std::string& id,
                  const std::string& sId,
                  double x,
                  double y,
                  double z) :
  SGNode(id, sId),
  _x(x),
  _y(y),
  _z(z),
  _translationMatrix(MutableMatrix44D::createTranslationMatrix(_x, _y, _z))
  {

  }

//  GLState* createState(const G3MRenderContext* rc,
//                       const GLState& parentState);
//  
//  GPUProgramState* createGPUProgramState(const G3MRenderContext* rc,
//                                         const GPUProgramState* parentState);
  
  void modifyGLState(GLState& glState) const;
  
  void modifyGPUProgramState(GPUProgramState& progState) const;

};

#endif
