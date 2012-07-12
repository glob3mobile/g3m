//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "Tile.hpp"
#include "Mesh.hpp"
#include "Camera.hpp"

#include "TileTessellator.hpp"
#include "TileTexturizer.hpp"

Tile::~Tile() {
  delete _mesh;
}

Mesh* Tile::getMesh(const RenderContext* rc,
                    const TileTessellator* tessellator) {
  if (_mesh == NULL) {
    _mesh = tessellator->createMesh(rc, this);
  }
  return _mesh;
}

bool Tile::isVisible(const RenderContext *rc) {
  
  return true;
}

void Tile::render(const RenderContext* rc,
                  const TileTessellator* tessellator,
                  const TileTexturizer* texturizer) {
  int ___diego_at_work;
  
  //  Camera* camera = rc->getCamera();
  //  Vector3D pos = camera->getPos();
  //
  //  double distance = pos.length();
  //
  //  rc->getLogger()->logInfo("distance to camera: %f", distance);
  
  if (isVisible(rc)) {
    Mesh* mesh = getMesh(rc, tessellator);
    if (mesh != NULL) {
      if (!isTextureSolved()) {
        mesh = texturizer->texturize(rc, this, mesh);
      }
      
      if (mesh != NULL) {
        mesh->render(rc);
      }
    }
  }
}
