//
//  SingleImageTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_SingleImageTileTexturizer_hpp
#define G3MiOSSDK_SingleImageTileTexturizer_hpp

#include "TileTexturizer.hpp"
#include "MutableVector2D.hpp"
#include "Mesh.hpp"

#include <vector>

class SingleImageTileTexturizer : public TileTexturizer {
private:
  
  const RenderContext* _rc;
  
  std::vector<MutableVector2D> createTextureCoordinates() const;

  const int _resolution;
  
public:
  
  SingleImageTileTexturizer(int resolution): _resolution(resolution){}
  
  Mesh* texturize(const RenderContext* rc,
                  Tile* tile,
                  Mesh* mesh,
                  Mesh* previousMesh);
  
  void tileToBeDeleted(Tile* tile);
  
};


#endif
