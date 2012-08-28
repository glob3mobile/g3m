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

#include "MutableVector3D.hpp"
#include "Planet.hpp"

#include <vector>

class EllipsoidalTileTessellator : public TileTessellator {
private:
  
  const unsigned int _resolution;
  const bool         _skirted;
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        const Geodetic3D& g) {
    vertices->push_back( planet->toCartesian(g).asMutableVector3D() );
  }
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        const Geodetic2D& g) {
    vertices->push_back( planet->toCartesian(g).asMutableVector3D() );
  }
  
public:
  EllipsoidalTileTessellator(const unsigned int resolution,
                             const bool skirted) :
  _resolution(resolution),
  _skirted(skirted)
  {
    int __TODO_with_and_height_resolutions;
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  Mesh* createMesh(const RenderContext* rc,
                   const Tile* tile) const;
  
  Mesh* createDebugMesh(const RenderContext* rc,
                        const Tile* tile) const;
  
  bool isReady(const RenderContext *rc) const {
    return true;
  }
  
  std::vector<MutableVector2D>* createUnitTextCoords() const;
  
};

#endif
