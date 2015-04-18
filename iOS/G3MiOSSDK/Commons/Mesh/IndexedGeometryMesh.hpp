//
//  IndexedGeometryMesh.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#ifndef __G3MiOSSDK__IndexedGeometryMesh__
#define __G3MiOSSDK__IndexedGeometryMesh__

#include "AbstractGeometryMesh.hpp"

class IShortBuffer;

class IndexedGeometryMesh : public AbstractGeometryMesh {
private:
  bool                _ownsIndices;
  IShortBuffer*       _indices;
protected:
  void rawRender(const G3MRenderContext* rc) const;

public:
  IndexedGeometryMesh(const int       primitive,
                      const Vector3D& center,
                      IFloatBuffer*   vertices,
                      bool            ownsVertices,
                      IShortBuffer*   indices,
                      bool            ownsIndices,
                      float           lineWidth = 1,
                      float           pointSize = 1,
                      bool            depthTest = true);

  IndexedGeometryMesh(const int       primitive,
                      const Vector3D& center,
                      IFloatBuffer*   vertices,
                      bool            ownsVertices,
                      IFloatBuffer*   normals,
                      bool            ownsNormals,
                      IShortBuffer*   indices,
                      bool            ownsIndices,
                      float           lineWidth = 1,
                      float           pointSize = 1,
                      bool            depthTest = true);

  ~IndexedGeometryMesh();


};

#endif
