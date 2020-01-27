//
//  LeveledMesh.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 16/04/13.
//
//

#ifndef __G3MiOSSDK__LeveledMesh__
#define __G3MiOSSDK__LeveledMesh__

#include "Mesh.hpp"


class LeveledMesh : public Mesh{
private:
  Mesh* _mesh;
  
  int _currentLevel;
  
public:
  LeveledMesh(Mesh* mesh, int level) :
  _mesh(mesh),
  _currentLevel(level)
  {
    
  }
  
  void setMesh(Mesh* mesh, int level) {
    if (_mesh != mesh && level >= _currentLevel) {
      delete _mesh;
      _mesh = mesh;
      _currentLevel = level;
    }
  }
  
  ~LeveledMesh() {
    delete _mesh;
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  size_t getVertexCount() const {
    return _mesh->getVertexCount();
  }
  
  const Vector3D getVertex(const size_t index) const;
  
  void rawRender(const G3MRenderContext* rc,
                 const GLState* parentState) const {
    _mesh->render(rc, parentState);
  }
  
  BoundingVolume* getBoundingVolume() const {
    return _mesh->getBoundingVolume();
  }
  
  bool isTransparent(const G3MRenderContext* rc) const {
    return _mesh->isTransparent(rc);
  }
  
  int getLevel() const {
    return _currentLevel;
  }
  
};

#endif
