//
//  OrientedBox.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 19/02/16.
//

#include "OrientedBox.hpp"
#include "Mesh.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "ShortBufferBuilder.hpp"
#include "IndexedMesh.hpp"


OrientedBox::~OrientedBox() {
  delete _mesh;
  
#ifdef JAVA_CODE
  super.dispose();
#endif
};


Mesh* OrientedBox::createMesh(const Color& color) const {
  double v[] = {
    _SWB._x, _SWB._y, _SWB._z,
    _NWB._x, _NWB._y, _NWB._z,
    _NWT._x, _NWT._y, _NWT._z,
    _SWT._x, _SWT._y, _SWT._z,
    _SEB._x, _SEB._y, _SEB._z,
    _NEB._x, _NEB._y, _NEB._z,
    _NET._x, _NET._y, _NET._z,
    _SET._x, _SET._y, _SET._z,
  };
  
  short i[] = {
    0, 1, 1, 2, 2, 3, 3, 0,
    1, 5, 5, 6, 6, 2, 2, 1,
    5, 4, 4, 7, 7, 6, 6, 5,
    4, 0, 0, 3, 3, 7, 7, 4,
    3, 2, 2, 6, 6, 7, 7, 3,
    0, 1, 1, 5, 5, 4, 4, 0
  };
  
  FloatBufferBuilderFromCartesian3D* vertices = FloatBufferBuilderFromCartesian3D::builderWithFirstVertexAsCenter();
  const int numVertices = 8;
  for (int n = 0; n < numVertices; n++) {
    vertices->add(v[n*3], v[n*3+1], v[n*3+2]);
  }
  
  ShortBufferBuilder indices;
  const int numIndices = 48;
  for (int n = 0; n < numIndices; n++) {
    indices.add(i[n]);
  }
  
  Mesh* mesh = new IndexedMesh(GLPrimitive::lines(),
                               vertices->getCenter(),
                               vertices->create(),
                               true,
                               indices.create(),
                               true,
                               2,
                               1,
                               new Color(color));
  
  delete vertices;
  
  return mesh;
}


void OrientedBox::render(const G3MRenderContext* rc,
                         const GLState* parentState,
                         const Color& color) const {
  if (_mesh == NULL) {
    _mesh = createMesh(color);
  }
  _mesh->render(rc, parentState);
}

