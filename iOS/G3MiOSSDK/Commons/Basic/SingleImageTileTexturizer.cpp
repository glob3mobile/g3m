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

#include "FloatBufferBuilder.hpp"

IFloatBuffer* SingleImageTileTexturizer::createTextureCoordinates(const RenderContext* rc,
                                                                  Mesh* mesh) const {
  FloatBufferBuilder texCoors;
  
  for (int i = 0; i < mesh->getVertexCount(); i++) {
    const Vector3D pos = mesh->getVertex(i);
    
    const Geodetic2D g = rc->getPlanet()->toGeodetic2D(pos);
    const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(g);
    
    const double s = GMath.atan2(n.y(), n.x()) / (GMath.pi() * 2) + 0.5;
    const double t = GMath.asin(n.z()) / GMath.pi() + 0.5;
    
    // texCoors.add( Vector2D(s, 1-t) );
    texCoors.add(s);
    texCoors.add(1-t);
  }
  
  return texCoors.create();
}

Mesh* SingleImageTileTexturizer::texturize(const RenderContext* rc,
                                           const TileRenderContext* trc,
                                           Tile* tile,
                                           Mesh* mesh,
                                           Mesh* previousMesh) {
  _renderContext = rc; //SAVING CONTEXT
  
  if (!_texId.isValid()) {
    _texId = rc->getTexturesHandler()->getGLTextureId(_image,
                                                      TextureSpec("SINGLE_IMAGE_TEX",
                                                                  _image->getWidth(),
                                                                  _image->getHeight(),
                                                                  true)
                                                      );
    
    if (!_texId.isValid()) {
      rc->getLogger()->logError("Can't upload texture to GPU");
      return NULL;
    }
    
    rc->getFactory()->deleteImage(_image);
  }
  
  TextureMapping* texMap = new SimpleTextureMapping(_texId,
                                                    createTextureCoordinates(rc, mesh),
                                                    true);
  if (previousMesh != NULL) delete previousMesh;
  
  tile->setTextureSolved(true);
  tile->setTexturizerDirty(false);
  
  return new TexturedMesh(mesh, false, texMap, true);
}

void SingleImageTileTexturizer::tileToBeDeleted(Tile* tile,
                                                Mesh* mesh) {
  
}

bool SingleImageTileTexturizer::tileMeetsRenderCriteria(Tile* tile) {
  return false;
}

void SingleImageTileTexturizer::justCreatedTopTile(const RenderContext* rc,
                                                   Tile *tile) {
  
}

void SingleImageTileTexturizer::ancestorTexturedSolvedChanged(Tile* tile,
                                                              Tile* ancestorTile,
                                                              bool textureSolved) {
  
}

void SingleImageTileTexturizer::tileMeshToBeDeleted(Tile* tile,
                                                    Mesh* mesh) {
  
}
