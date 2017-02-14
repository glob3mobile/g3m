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
  const bool    _depthTest;
  
  GLState* _glState;
  void createGLState();

public:

  SGGeometryNode(const std::string& id,
                 const std::string& sID,
                 int                primitive,
                 IFloatBuffer*      vertices,
                 IFloatBuffer*      colors,
                 IFloatBuffer*      uv,
                 IFloatBuffer*      normals,
                 IShortBuffer*      indices,
                 const bool         depthTest) :
  SGNode(id, sID),
  _primitive(primitive),
  _vertices(vertices),
  _colors(colors),
  _uv(uv),
  _normals(normals),
  _indices(indices),
  _depthTest(depthTest),
  _glState(new GLState())
  {
    createGLState();
  }

  ~SGGeometryNode();

  void rawRender(const G3MRenderContext* rc, const GLState* glState);
  
  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState) {
    _glState->setParent(parentState);
    return _glState;
  }

  const std::string description() {
    return "SGGeometryNode";
  }
  
};

#endif
