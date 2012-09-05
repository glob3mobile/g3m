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
  
  const RenderContext*         _renderContext;
  TilesRenderParameters* const _parameters;
  GLTextureId _texId;
  IImage * const _image;
  
  
  IFloatBuffer* createTextureCoordinates(const RenderContext* rc,
                                         Mesh* mesh) const;
  
public:
  
  SingleImageTileTexturizer(TilesRenderParameters* const parameters,
                            IImage* image) :
  _texId(-1),
  _image(image),
  _parameters(parameters),
  _renderContext(NULL)
  {
  }
  
  ~SingleImageTileTexturizer() {
    if (_texId.isValid()){
      if (_renderContext != NULL) {
        _renderContext->getTexturesHandler()->releaseGLTextureId(_texId);
      }
    }
  }
  
  void initialize(const InitializationContext* ic,
                  const TilesRenderParameters* parameters) {
    
  }
  
  Mesh* texturize(const RenderContext* rc,
                  const TileRenderContext* trc,
                  Tile* tile,
                  Mesh* mesh,
                  Mesh* previousMesh);
  
  void tileToBeDeleted(Tile* tile,
                       Mesh* mesh);
  
  bool tileMeetsRenderCriteria(Tile* tile);
  
  void justCreatedTopTile(const RenderContext* rc,
                          Tile* tile);
  
  bool isReady(const RenderContext *rc) {
    return true;
  }
  
  void ancestorTexturedSolvedChanged(Tile* tile,
                                     Tile* ancestorTile,
                                     bool textureSolved);
  
  void onTerrainTouchEvent(const EventContext* ec,
                           const Geodetic3D& position,
                           const Tile* tile) {
  }
  
  
  void tileMeshToBeDeleted(Tile* tile,
                           Mesh* mesh);
  
};


#endif
