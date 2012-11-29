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
  const int     _primitive;
  IFloatBuffer* _vertices;
  IFloatBuffer* _colors;
  IFloatBuffer* _uv;
  IFloatBuffer* _normals;
  IIntBuffer*   _indices;

protected:
  void rawRender(const G3MRenderContext* rc);

public:

  SGGeometryNode(const std::string& id,
                 const std::string& sId,
                 int                primitive,
                 IFloatBuffer*      vertices,
                 IFloatBuffer*      colors,
                 IFloatBuffer*      uv,
                 IFloatBuffer*      normals,
                 IIntBuffer*        indices) :
  SGNode(id, sId),
  _primitive(primitive),
  _vertices(vertices),
  _colors(colors),
  _uv(uv),
  _normals(normals),
  _indices(indices)
  {

  }

  ~SGGeometryNode();

};

#endif
