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

#include "IImage.hpp"
#include "TexturesHandler.hpp"

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
  
  void initialize(const InitializationContext* ic) {
    
  }
  
  Mesh* texturize(const RenderContext* rc,
                  Tile* tile,
                  const TileTessellator* tessellator,
                  Mesh* mesh,
                  Mesh* previousMesh);
  
  void tileToBeDeleted(Tile* tile);
  
  bool tileMeetsRenderCriteria(Tile* tile);

  void justCreatedTopTile(Tile* tile);
  
  bool isReady(const RenderContext *rc) {
    return true;
  }

};


#endif
