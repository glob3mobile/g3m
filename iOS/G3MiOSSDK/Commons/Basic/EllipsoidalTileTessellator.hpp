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

//#include "MutableVector3D.hpp"
//#include "Planet.hpp"

class EllipsoidalTileTessellator : public TileTessellator {
private:
  
  const unsigned int _resolution;
  const bool         _skirted;
  
public:
  Mesh* createDebugMesh(const RenderContext* rc,
                        const Tile* tile) const;
  
  EllipsoidalTileTessellator(const unsigned int resolution,
                             const bool skirted) :
  _resolution(resolution),
  _skirted(skirted)
  {
//    int __TODO_width_and_height_resolutions;
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  Mesh* createMesh(const RenderContext* rc,
                   const Tile* tile) const;
  
  bool isReady(const RenderContext *rc) const {
    return true;
  }
  
  IFloatBuffer* createUnitTextCoords() const;
  
};

#endif
