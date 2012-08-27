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


std::vector<MutableVector2D> SingleImageTileTexturizer::createTextureCoordinates(const RenderContext* rc,
                                                                                 Mesh* mesh) const {
  std::vector<MutableVector2D> texCoors;
  
  for (int i = 0; i < mesh->getVertexCount(); i++) {
    
    Vector3D pos = mesh->getVertex(i);
    
    const Geodetic2D g = rc->getPlanet()->toGeodetic2D(pos);
    const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(g);
      
    
    if (_isMercatorImage) {
      printf("mercator Image");
        
      const Angle lat = g.latitude();
      const Angle lon = g.longitude();
      const double metersX = lon.degrees() * 20027508.342789244 / 180.0;
      const double metersY = (log(tan(90+lat.degrees() * M_PI/360.0))/(M_PI/180.0))*20027508.342789244 / 180.0;
      
      const double s = (metersX / 20027508.342789244) + 0.5;
      const double t = (metersY / 20027508.342789244) +0.5;
      
        
      //const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
      //const double t = asin(n.z()) / M_PI + 0.5;
      texCoors.push_back(MutableVector2D(s, 1-t));
    }
    else {
      const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
      const double t = asin(n.z()) / M_PI + 0.5;
      texCoors.push_back(MutableVector2D(s, 1-t));
    }
    
    
    
  }
  
  return texCoors;
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
  
  const TextureMapping* texMap = new SimpleTextureMapping(_texId,
                                                          createTextureCoordinates(rc, mesh));
  
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
