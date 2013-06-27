//
//  IndexedGeometryMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

#ifndef __G3MiOSSDK__IndexedGeometryMesh__
#define __G3MiOSSDK__IndexedGeometryMesh__

#include "AbstractGeometryMesh.hpp"

class IShortBuffer;

class IndexedGeometryMesh : public AbstractGeometryMesh{
private:
  IShortBuffer*       _indices;
protected:
  void rawRender(const G3MRenderContext* rc) const;
  
public:
  IndexedGeometryMesh(const int primitive,
              bool owner,
              const Vector3D& center,
              IFloatBuffer* vertices,
              IShortBuffer* indices,
              float lineWidth,
              float pointSize = 1,
              bool depthTest = true);
  
  ~IndexedGeometryMesh();
  
  
};

#endif /* defined(__G3MiOSSDK__IndexedGeometryMesh__) */
