//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Mesh
#define G3MiOSSDK_Mesh

#include "Context.hpp"
#include "BoundingVolume.hpp"

#include "GLState.hpp"

class Vector3D;
class GPUProgramState;

class Mesh {
private:
  bool _enable;
public:
  Mesh() :
  _enable(true)
  {
  }

  void setEnable(bool enable) {
    _enable = enable;
  }

  bool isEnable() const {
    return _enable;
  }
  
  virtual ~Mesh() {
  }
  
  virtual int getVertexCount() const = 0;
  
  virtual const Vector3D getVertex(int i) const = 0;
    
  virtual BoundingVolume* getBoundingVolume() const = 0;
  
  virtual bool isTransparent(const G3MRenderContext* rc) const = 0;
  
  virtual void rawRender(const G3MRenderContext* rc,
                         const GLState* parentGLState) const = 0;

  void render(const G3MRenderContext* rc,
              const GLState* parentGLState) const {
    if (_enable) {
      rawRender(rc, parentGLState);
    }
  }

  virtual void showNormals(bool v) const = 0;

};


#endif
