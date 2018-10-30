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

#include "GLConstants.hpp"


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
                      float           lineWidth           = 1,
                      float           pointSize           = 1,
                      bool            depthTest           = true,
                      bool            polygonOffsetFill   = false,
                      float           polygonOffsetFactor = 0,
                      float           polygonOffsetUnits  = 0,
                      bool            cullFace            = false,
                      int             culledFace          = GLCullFace::back());

  IndexedGeometryMesh(const int       primitive,
                      const Vector3D& center,
                      IFloatBuffer*   vertices,
                      bool            ownsVertices,
                      IFloatBuffer*   normals,
                      bool            ownsNormals,
                      IShortBuffer*   indices,
                      bool            ownsIndices,
                      float           lineWidth           = 1,
                      float           pointSize           = 1,
                      bool            depthTest           = true,
                      bool            polygonOffsetFill   = false,
                      float           polygonOffsetFactor = 0,
                      float           polygonOffsetUnits  = 0,
                      bool            cullFace            = false,
                      int             culledFace          = GLCullFace::back());

  ~IndexedGeometryMesh();

  const IShortBuffer* getIndices() const {
    return _indices;
  }
};

#endif
