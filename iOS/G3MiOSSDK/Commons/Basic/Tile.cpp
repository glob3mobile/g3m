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
#include "TileRenderer.hpp"


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
  // TODO: check collition with frustum
  // TODO: check for tiles backfacing to the camera
  
  return true;
}

bool Tile::meetsRenderCriteria(const RenderContext *rc,
                               const TileParameters* parameters) {
  
  if (_level >= parameters->_maxLevel) {
    return true;
  }
  
//  31890685.000000
//   7083288.848839
  
//  const Vector3D radii = rc->getPlanet()->getRadii();
//  const double rad = (radii.x() + radii.y() + radii.z()) / 3;
//  
//  const double ratio = (distanceToCamera - rad) / rad;
//  
//  rc->getLogger()->logInfo("Distance to camera: %f - %f", distanceToCamera, ratio);
  
//  const Vector3D center = rc->getPlanet()->toVector3D(_sector.getCenter());
//  
//  const double distanceToCamera = rc->getCamera()->getPos().sub(center).length();
//  rc->getLogger()->logInfo("Distance to camera: %f", distanceToCamera);
  
  return _level >= 0;
}

void Tile::rawRender(const RenderContext *rc,
                     const TileTessellator *tessellator,
                     const TileTexturizer *texturizer) {
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

void Tile::render(const RenderContext* rc,
                  const TileTessellator* tessellator,
                  const TileTexturizer* texturizer,
                  const TileParameters* parameters) {
  int ___diego_at_work;
  
  if (isVisible(rc)) {
    if (meetsRenderCriteria(rc, parameters)) {
      rawRender(rc, tessellator, texturizer);
    }
    else {
      std::vector<Tile*> subTiles = createSubTiles();
      for (int i = 0; i < subTiles.size(); i++) {
        Tile* subTile = subTiles[i];
        subTile->render(rc, tessellator, texturizer, parameters);
        
        delete subTile;
      }
    }
  }
}

std::vector<Tile*> Tile::createSubTiles() {
  const Geodetic2D lower = _sector.lower();
  const Geodetic2D upper = _sector.upper();
  
  const Angle lat0 = lower.latitude();
  const Angle lat2 = upper.latitude();
  const Angle lat1 = Angle::midAngle(lat0, lat2);
  
  const Angle lon0 = lower.longitude();
  const Angle lon2 = upper.longitude();
  const Angle lon1 = Angle::midAngle(lon0, lon2);
  
  const int nextLevel = _level + 1;
  Tile* fallback = isTextureSolved() ? this : _fallbackTextureTile;
  
  std::vector<Tile*> subTiles(4);
  subTiles[0] = new Tile(Sector(Geodetic2D(lat0, lon0), Geodetic2D(lat1, lon1)), nextLevel, 2 * _row    , 2 * _column    , fallback);
  subTiles[1] = new Tile(Sector(Geodetic2D(lat0, lon1), Geodetic2D(lat1, lon2)), nextLevel, 2 * _row    , 2 * _column + 1, fallback);
  subTiles[2] = new Tile(Sector(Geodetic2D(lat1, lon0), Geodetic2D(lat2, lon1)), nextLevel, 2 * _row + 1, 2 * _column    , fallback);
  subTiles[3] = new Tile(Sector(Geodetic2D(lat1, lon1), Geodetic2D(lat2, lon2)), nextLevel, 2 * _row + 1, 2 * _column + 1, fallback);
  
  return subTiles;
}
