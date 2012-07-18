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
#include "TileRenderer.hpp"
#include "Mesh.hpp"

#include <vector>

class SingleImageTileTexturizer : public TileTexturizer {
private:
  
  const RenderContext* _renderContext;
  TileParameters* const _parameters;  
  int _texID;
  IImage * const _image;
  
  std::vector<MutableVector2D> createTextureCoordinates(const RenderContext* rc, Mesh* mesh) const;
  
public:
  
  SingleImageTileTexturizer(TileParameters * const par, IImage *image) :
  _texID(-1),
  _image(image),
  _parameters(par),
  _renderContext(NULL) {
  }
  
  ~SingleImageTileTexturizer() {
    if (_texID > -1){
      if (_renderContext != NULL) {
        _renderContext->getTexturesHandler()->takeTexture(_texID);
      }
    }
  }
  
  Mesh* texturize(const RenderContext* rc,
                  Tile* tile,
                  Mesh* mesh,
                  Mesh* previousMesh);
  
  void tileToBeDeleted(Tile* tile);
  
};


#endif
