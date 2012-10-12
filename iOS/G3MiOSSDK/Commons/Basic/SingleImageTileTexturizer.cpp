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
//#include <iostream>
#include "TextureBuilder.hpp"

#include "FloatBufferBuilderFromCartesian2D.hpp"

IFloatBuffer* SingleImageTileTexturizer::createTextureCoordinates(const RenderContext* rc,
                                                                  Mesh* mesh) const {
  FloatBufferBuilderFromCartesian2D texCoors;
  
  for (int i = 0; i < mesh->getVertexCount(); i++) {
    const Vector3D pos = mesh->getVertex(i);
    
    const Geodetic2D g = rc->getPlanet()->toGeodetic2D(pos);

    if (_isMercatorImage){
      
      const double latRad = g.latitude().radians();
      const double sec = 1.0/(GMath.cos(g.latitude().radians()));
      
      //double tLatRad = 2*atan(exp(latRad)) - M_PI/2.0;
      
      //double tLatRad = log(tan(latRad)+sec);
      //double tLatRad = 0.5*log((1.0+sin(latRad))/(1.0-sin(latRad)));
      //double tLatRad = atanh(sin(latRad));
      //double tLatRad = asinh(tan(latRad));
      
      
      double tLatRad = GMath.log(GMath.tan(GMath.pi()/4.0)+latRad/2.0);
      
      tLatRad = tLatRad*sec;
      //std::cout<<" Lat: "<<g.latitude().degrees()<<"\n";
      //std::cout<<"tLatRad: "<<tLatRad<<"\n";
      double limit = 85.5 * GMath.pi()/180.0;
      //std::cout<<"limit: "<<limit<<"\n";
      if (tLatRad > limit) {
        tLatRad = limit;
      }
      if (tLatRad<-limit) {
        tLatRad = -limit;
      }
      
      const Geodetic2D mercg(Angle::fromRadians(tLatRad), g.longitude());
      //const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(mercg);
      const Vector3D m = rc->getPlanet()->toCartesian(mercg);
      const Vector3D n = rc->getPlanet()->centricSurfaceNormal(m);
      
      const double s = GMath.atan2(n._y, n._x) / (GMath.pi() * 2) + 0.5;
      
      //double t = (tLatRad*sec + M_PI/2.0)/M_PI ;
      double t = GMath.asin(n._z) / GMath.pi() + 0.5 ;
      //texCoors.push_back(MutableVector2D(s, 1-t));
      texCoors.add((float)s, (float)(1.0-t));
      
    }
    else {
      const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(g);
      //const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
      //double t = asin(n.z()) / M_PI + 0.5 ;
      //texCoors.push_back(MutableVector2D(s, 1-t));
      
      const double s = GMath.atan2(n._y, n._x) / (GMath.pi() * 2) + 0.5;
      const double t = GMath.asin(n._z) / GMath.pi() + 0.5;
      
      texCoors.add((float)s, (float)(1.0-t));
    }

  }
  
  return texCoors.create();
}

Mesh* SingleImageTileTexturizer::texturize(const RenderContext* rc,
                                           const TileRenderContext* trc,
                                           Tile* tile,
                                           Mesh* mesh,
                                           Mesh* previousMesh) {
  _renderContext = rc; //SAVING CONTEXT
  
  if (_texId == NULL) {
    _texId = rc->getTexturesHandler()->getGLTextureId(_image, GLFormat::rgba(),
                                                      "SINGLE_IMAGE_TEX", false);
    
    rc->getFactory()->deleteImage(_image);
    
    if (_texId == NULL) {
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
