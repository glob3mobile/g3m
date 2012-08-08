//
//  SingleImageTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 18/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include "SingleImageTileTexturizer.hpp"

#include "TextureMapping.hpp"
#include "TexturedMesh.hpp"
#include "Planet.hpp"


std::vector<MutableVector2D> SingleImageTileTexturizer::createTextureCoordinates(const RenderContext* rc, Mesh* mesh) const {
  std::vector<MutableVector2D> texCoors;

  for (int i = 0; i < mesh->getVertexCount(); i++) {
    
    Vector3D pos = mesh->getVertex(i);
    
    const Geodetic2D g = rc->getPlanet()->toGeodetic2D(pos);
    const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(g);
    
    const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
    const double t = asin(n.z()) / M_PI + 0.5;
    
    texCoors.push_back(MutableVector2D(s, 1-t));
  }
  
  return texCoors;
}

Mesh* SingleImageTileTexturizer::texturize(const RenderContext* rc,
                                           Tile* tile,
                                           const TileTessellator* tessellator,
                                           Mesh* mesh,
                                           Mesh* previousMesh) {
  _renderContext = rc; //SAVING CONTEXT
  
  if (_texID < 0) {
    _texID = rc->getTexturesHandler()->getTextureId(_image,
                                                    "SINGLE_IMAGE_TEX",
                                                    _image->getWidth(),
                                                    _image->getHeight());
    
    if (_texID < 0) {
      rc->getLogger()->logError("Can't upload texture to GPU");
      return NULL;
    }
    
    rc->getFactory()->deleteImage(_image);
  }
  
  const TextureMapping* texMap = new TextureMapping( _texID, createTextureCoordinates(rc, mesh), rc->getTexturesHandler(), "SINGLE_IMAGE_TEX", _image->getWidth(), _image->getHeight() );
  
  if (previousMesh != NULL) delete previousMesh;
  
  tile->setTextureSolved(true);
  
  return new TexturedMesh(mesh, false, texMap, true);
}

void SingleImageTileTexturizer::tileToBeDeleted(Tile* tile) {
  
}

bool SingleImageTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
}

void SingleImageTileTexturizer::justCreatedTopTile(Tile *tile) {
  
}
