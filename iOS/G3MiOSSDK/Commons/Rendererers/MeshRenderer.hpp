//
//  MeshRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

#ifndef __G3MiOSSDK__MeshRenderer__
#define __G3MiOSSDK__MeshRenderer__

#include "LeafRenderer.hpp"
#include <vector>

class Mesh;


class MeshRenderer : public LeafRenderer {
private:
  std::vector<Mesh*> _meshes;

public:
  ~MeshRenderer();

  void addMesh(Mesh* mesh) {
    _meshes.push_back(mesh);
  }

  void clearMeshes();

  void onResume(const G3MContext* context) {

  }

  void onPause(const G3MContext* context) {

  }

  void onDestroy(const G3MContext* context) {

  }

  void initialize(const G3MContext* context) {

  }

  bool isReadyToRender(const G3MRenderContext* rc) {
    return true;
  }

  void render(const G3MRenderContext* rc,
              const GLState& parentState);

  bool onTouchEvent(const G3MEventContext* ec,
                    const TouchEvent* touchEvent) {
    return false;
  }

  void onResizeViewportEvent(const G3MEventContext* ec,
                             int width, int height) {

  }

  void start(const G3MRenderContext* rc) {

  }
  
  void stop(const G3MRenderContext* rc) {
    
  }
  
};

#endif
