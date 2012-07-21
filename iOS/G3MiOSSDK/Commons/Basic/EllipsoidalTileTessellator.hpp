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
  
  
public:
  EllipsoidalTileTessellator(const unsigned int resolution,
                             const bool skirted) :
  _resolution(resolution),
  _skirted(skirted)
  {
    
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  virtual Mesh* createMesh(const RenderContext* rc,
                           const Tile* tile) const;
  
  bool isReadyToRender(const RenderContext *rc) const {
    return true;
  }
  
  virtual std::vector<MutableVector2D>* createUnitTextCoords() const;


};

#endif
