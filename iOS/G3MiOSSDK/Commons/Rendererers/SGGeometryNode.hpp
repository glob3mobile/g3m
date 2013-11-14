//
//  SGGeometryNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

#ifndef __G3MiOSSDK__SGGeometryNode__
#define __G3MiOSSDK__SGGeometryNode__

#include "SGNode.hpp"

#include "GLState.hpp"
#include "Box.hpp"

class IFloatBuffer;
class IShortBuffer;
class GPUProgramState;


class SGGeometryNode : public SGNode {
private:
  const int     _primitive;
  IFloatBuffer* _vertices;
  IFloatBuffer* _colors;
  IFloatBuffer* _uv;
  IFloatBuffer* _normals;
  IShortBuffer* _indices;
  
  GLState* _glState;
  void createGLState();
  
  Box* _boundingBox;

public:

  SGGeometryNode(const std::string& id,
                 const std::string& sId,
                 int                primitive,
                 IFloatBuffer*      vertices,
                 IFloatBuffer*      colors,
                 IFloatBuffer*      uv,
                 IFloatBuffer*      normals,
                 IShortBuffer*      indices);
  
  ~SGGeometryNode();

  void rawRender(const G3MRenderContext* rc, const GLState* glState);
  
  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState) {
    _glState->setParent(parentState);
    return _glState;
  }

  std::string description() {
    return "SGGeometryNode";
  }
  
  Box* getCopyBoundingBox() {
    return new Box(_boundingBox);
  }
  
};

#endif
