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
  int           _primitive;
  IFloatBuffer* _vertices;
  IFloatBuffer* _colors;
  IFloatBuffer* _uv;
  IFloatBuffer* _normals;
  IIntBuffer*   _indices;

protected:
//  void prepareRender(const RenderContext* rc);
//
//  void cleanUpRender(const RenderContext* rc);

  void rawRender(const RenderContext* rc);

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
