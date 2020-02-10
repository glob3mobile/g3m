//
//  IndexedMesh.hpp
//  G3M
//
//  Created by José Miguel S N on 22/06/12.
//

#ifndef G3M_IndexedMesh
#define G3M_IndexedMesh

#include "AbstractMesh.hpp"

#include "GLConstants.hpp"

class IShortBuffer;

class IndexedMesh : public AbstractMesh {
private:
  IShortBuffer* _indices;
  const bool    _ownsIndices;

protected:
  void renderMesh(const G3MRenderContext* rc,
                  GLState* glState) const;

public:
  IndexedMesh(const int primitive,
              const Vector3D& center,
              IFloatBuffer* vertices,
              bool ownsVertices,
              IShortBuffer* indices,
              bool ownsIndices,
              float lineWidth           = 1,
              float pointSize           = 1,
              const Color* flatColor    = NULL,
              IFloatBuffer* colors      = NULL,
              bool depthTest            = true,
              IFloatBuffer* normals     = NULL,
              bool polygonOffsetFill    = false,
              float polygonOffsetFactor = 0,
              float polygonOffsetUnits  = 0,
              bool cullFace             = false,
              int  culledFace           = GLCullFace::back());

  ~IndexedMesh();

  const IShortBuffer* getIndices() const {
    return _indices;
  }

};

#endif
