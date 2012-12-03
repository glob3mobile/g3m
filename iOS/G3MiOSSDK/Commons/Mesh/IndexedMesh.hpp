//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_IndexedMesh_h
#define G3MiOSSDK_IndexedMesh_h

#include "AbstractMesh.hpp"

class IIntBuffer;

class IndexedMesh : public AbstractMesh {
private:
  IIntBuffer*       _indices;
protected:
  void rawRender(const G3MRenderContext* rc,
                 const GLState& parentState) const;

public:
  IndexedMesh(const int primitive,
              bool owner,
              const Vector3D& center,
              IFloatBuffer* vertices,
              IIntBuffer* indices,
              float lineWidth,
              Color* flatColor = NULL,
              IFloatBuffer* colors = NULL,
              const float colorsIntensity = (float)0.0);

  ~IndexedMesh();

};

#endif
