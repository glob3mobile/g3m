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

//#include <vector>
//#include "FloatBufferBuilder.hpp"


class EllipsoidalTileTessellator : public TileTessellator {
private:
  
  const unsigned int _resolution;
  const bool         _skirted;
  
//  static void addVertex(const Planet* planet,
//                        FloatBufferBuilder* vertices,
//                        const Geodetic3D& g) {
//    vertices->add( planet->toCartesian(g) );
//  }
//  
//  static void addVertex(const Planet* planet,
//                        FloatBufferBuilder* vertices,
//                        const Geodetic2D& g) {
//    vertices->add( planet->toCartesian(g) );
//  }
  
public:
  Mesh* createDebugMesh(const RenderContext* rc,
                        const Tile* tile) const;
  
  EllipsoidalTileTessellator(const unsigned int resolution,
                             const bool skirted) :
  _resolution(resolution),
  _skirted(skirted)
  {
    int __TODO_width_and_height_resolutions;
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  Mesh* createMesh(const RenderContext* rc,
                   const Tile* tile) const;
  
  bool isReady(const RenderContext *rc) const {
    return true;
  }
  
  std::vector<MutableVector2D>* createUnitTextCoords() const;
  
};

#endif
