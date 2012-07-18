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
#include <string>
#include <vector>
#include "MutableVector3D.hpp"
#include "MutableVector2D.hpp"
#include "Geodetic2D.hpp"
#include "Planet.hpp"

class EllipsoidalTileTessellator : public TileTessellator {
private:
  
  const unsigned int _resolution;
  const bool         _skirted;
  const bool         _debugMode;
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        const Geodetic3D& g) {
    vertices->push_back( planet->toVector3D(g).asMutableVector3D() );
  }
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        const Geodetic2D& g) {
    vertices->push_back( planet->toVector3D(g).asMutableVector3D() );
  }
  
  Mesh* createDebugMesh(const RenderContext* rc,
                        const Tile* tile) const;
  
  EllipsoidalTileTessellator(const unsigned int resolution,
                             const bool skirted,
                             const bool debugMode) :
  _resolution(resolution),
  _skirted(skirted),
  _debugMode(debugMode)
  {
    
  }
  
public:
  static EllipsoidalTileTessellator* create(const unsigned int resolution,
                                            const bool skirted) {
    return new EllipsoidalTileTessellator(resolution, skirted, false);
  }
  
  static EllipsoidalTileTessellator* createForDebug(const unsigned int resolution) {
    return new EllipsoidalTileTessellator(resolution, true, true);
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  virtual Mesh* createMesh(const RenderContext* rc,
                           const Tile* tile) const;
  
};

#endif
