//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_EllipsoidalTileTessellator_hpp
#define G3MiOSSDK_EllipsoidalTileTessellator_hpp

#include "TileTessellator.hpp"
class Sector;

//#include "MutableVector3D.hpp"
//#include "Planet.hpp"

class EllipsoidalTileTessellator : public TileTessellator {
private:

  const unsigned int _resolutionX;
  const unsigned int _resolutionY;
  const bool         _skirted;

  Vector2I calculateResolution(const Sector& sector) const;

public:

  EllipsoidalTileTessellator(const Vector2I& resolution,
                             const bool skirted) :
  _resolutionX(resolution._x),
  _resolutionY(resolution._y),
  _skirted(skirted)
  {
    //    int __TODO_width_and_height_resolutions;
  }

  virtual ~EllipsoidalTileTessellator() { }

  Vector2I getTileMeshResolution(const Planet* planet,
                                 const Tile* tile,
                                 bool debug) const;


  Mesh* createTileMesh(const Planet* planet,
                       const Tile* tile,
                       const ElevationData* elevationData,
                       float verticalExaggeration,
                       bool debug) const;

  Mesh* createTileDebugMesh(const Planet* planet,
                            const Tile* tile) const;

  bool isReady(const G3MRenderContext *rc) const {
    return true;
  }

  IFloatBuffer* createUnitTextCoords(const Tile* tile) const;
  
};

#endif
