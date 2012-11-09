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
class IIntBuffer;

class SGGeometryNode : public SGNode {
private:
  const int           _primitive;
  const IFloatBuffer* _vertices;
  const IFloatBuffer* _colors;
  const IFloatBuffer* _uv;
  const IFloatBuffer* _normals;
  const IIntBuffer*   _indices;

public:

  SGGeometryNode(int           primitive,
                 IFloatBuffer* vertices,
                 IFloatBuffer* colors,
                 IFloatBuffer* uv,
                 IFloatBuffer* normals,
                 IIntBuffer*   indices) :
  _primitive(primitive),
  _vertices(vertices),
  _colors(colors),
  _uv(uv),
  _normals(normals),
  _indices(indices)
  {

  }
  
};

#endif
