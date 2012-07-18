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


std::vector<MutableVector2D> SingleImageTileTexturizer::createTextureCoordinates(const RenderContext* rc, Mesh* mesh) const {
  std::vector<MutableVector2D> texCoors;
  const float* vertex = mesh->getVertex();

  for (int i = 0; i < mesh->getVertexCount(); i++) {
    
    Vector3D pos(vertex[i*3], vertex[i*3+1], vertex[i*3+2]);
    
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
  
  const TextureMapping* texMap = new TextureMapping( _texID, createTextureCoordinates(rc, mesh) );
  
  if (previousMesh != NULL) delete previousMesh;
  
  tile->setTextureSolved(true);
  
  return new TexturedMesh(mesh, false, texMap, true);
}

void SingleImageTileTexturizer::tileToBeDeleted(Tile* tile) {
  
}
