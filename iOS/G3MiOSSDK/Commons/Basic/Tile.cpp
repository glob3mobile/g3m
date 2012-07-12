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

bool Tile::hasEnoughDetail(const RenderContext *rc) {
  
  return _level >= 0;
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
    if (hasEnoughDetail(rc)) {
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
    else {
      std::vector<Tile*> subTiles = createSubTiles();
      for (int i = 0; i < subTiles.size(); i++) {
        Tile* subTile = subTiles[i];
        subTile->render(rc,
                        tessellator,
                        texturizer);
        
        delete subTile;
      }
    }
  }
}


std::vector<Tile*> Tile::createSubTiles() {
  const Geodetic2D lower = getSector().lower();
  const Geodetic2D upper = getSector().upper();
  
  const Angle p0 = lower.latitude();
  const Angle p2 = upper.latitude();
  const Angle p1 = Angle::midAngle(p0, p2);
  
  const Angle t0 = lower.longitude();
  const Angle t2 = upper.longitude();
  const Angle t1 = Angle::midAngle(t0, t2);
  
  const int row = getRow();
  const int col = getColumn();
  const int nextLevel = getLevel() + 1;
  
  std::vector<Tile*> subTiles(4);
  subTiles[0] = new Tile(Sector(Geodetic2D(p0, t0), Geodetic2D(p1, t1)), nextLevel, 2 * row, 2 * col);
  subTiles[1] = new Tile(Sector(Geodetic2D(p0, t1), Geodetic2D(p1, t2)), nextLevel, 2 * row, 2 * col + 1);
  subTiles[2] = new Tile(Sector(Geodetic2D(p1, t0), Geodetic2D(p2, t1)), nextLevel, 2 * row + 1, 2 * col);
  subTiles[3] = new Tile(Sector(Geodetic2D(p1, t1), Geodetic2D(p2, t2)), nextLevel, 2 * row + 1, 2 * col + 1);
  
  return subTiles;
}
