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

class IFloatBuffer;
class IShortBuffer;


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
                 const bool         depthTest);

  ~SGGeometryNode();

  void rawRender(const G3MRenderContext* rc,
                 const GLState* glState);

  const GLState* createState(const G3MRenderContext* rc,
                             const GLState* parentState);

  const std::string description() {
    return "SGGeometryNode";
  }
  
};

#endif
