//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_EllipsoidalTileTessellator_hpp
#define G3MiOSSDK_EllipsoidalTileTessellator_hpp

#include "TileTessellator.hpp"

class EllipsoidalTileTessellator : public TileTessellator {
public:
  virtual ~EllipsoidalTileTessellator() { }
  
  virtual Mesh* createMesh(const RenderContext* rc,
                           const Tile* tile) const;
};

#endif
