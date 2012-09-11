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
#include <iostream>
#include "TextureBuilder.hpp"

#include "FloatBufferBuilderFromCartesian2D.hpp"

IFloatBuffer* SingleImageTileTexturizer::createTextureCoordinates(const RenderContext* rc,
                                                                  Mesh* mesh) const {
  FloatBufferBuilderFromCartesian2D texCoors;
  
  //double originShift = 2 * M_PI *6378137.0 /2.0;
  //const double originShift = 6378137.0;
  IMathUtils *math = IMathUtils::instance();
  
  for (int i = 0; i < mesh->getVertexCount(); i++) {
    const Vector3D pos = mesh->getVertex(i);
    
    const Geodetic2D g = rc->getPlanet()->toGeodetic2D(pos);
    
//<<<<<<< HEAD
    if (_isMercatorImage){
      
      const double latRad = g.latitude().radians();
      const double sec = 1.0/(math->cos(g.latitude().radians()));
      
      //double tLatRad = 2*atan(exp(latRad)) - M_PI/2.0;
      
      //double tLatRad = log(tan(latRad)+sec);
      //double tLatRad = 0.5*log((1.0+sin(latRad))/(1.0-sin(latRad)));
      //double tLatRad = atanh(sin(latRad));
      //double tLatRad = asinh(tan(latRad));
      
      
      double tLatRad = math->log(math->tan(math->pi()/4.0)+latRad/2.0);
      
      tLatRad = tLatRad*sec;
      std::cout<<" Lat: "<<g.latitude().degrees()<<"\n";
      std::cout<<"tLatRad: "<<tLatRad<<"\n";
      double limit = 85.5 * math->pi()/180.0;
      //std::cout<<"limit: "<<limit<<"\n";
      if (tLatRad > limit) {
        tLatRad = limit;
      }
      if (tLatRad<-limit) {
        tLatRad = -limit;
      }
      
      
      Geodetic2D mercg = *new Geodetic2D(Angle::fromRadians(tLatRad),g.longitude());
      //const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(mercg);
      const Vector3D m = rc->getPlanet()->toCartesian(mercg);
      const Vector3D n = rc->getPlanet()->centricSurfaceNormal(m);
      
    
      
      const double s = math->atan2(n.y(), n.x()) / (math->pi() * 2) + 0.5;
      
      //double t = (tLatRad*sec + M_PI/2.0)/M_PI ;
      double t = GMath.asin(n.z()) / math->pi() + 0.5 ;
      //texCoors.push_back(MutableVector2D(s, 1-t));
      texCoors.add((float)s, (float)(1.0-t));
      
    }
    else {
      const Vector3D n = rc->getPlanet()->geodeticSurfaceNormal(g);
      //const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
      //double t = asin(n.z()) / M_PI + 0.5 ;
      //texCoors.push_back(MutableVector2D(s, 1-t));
      
      const double s = math->atan2(n.y(), n.x()) / (GMath.pi() * 2) + 0.5;
      const double t = math->asin(n.z()) / math->pi() + 0.5;
      
      texCoors.add((float)s, (float)(1.0-t));
    }
    
    /*const double seq = 1.0/(cos(g.latitude().radians()));
    const double limit = log(tan(M_PI/4.0 + rad82/2.0));
    originShift = 1.0;//originShift;//*(cos(g.latitude().radians()));
    
    std::cout<<"limit : "<<limit<<"\n";
      
    
    if (_isMercatorImage) {
        
      
      const double latDeg = g.latitude().degrees();
      const double lonDeg = g.longitude().degrees();
      const double latRad = g.latitude().radians();
      const double lonRad = g.longitude().radians();
      
      const double metersX = lonDeg * originShift /360.0;
      double s = (metersX / originShift)+0.5;
      
      double tLatRad = 2*atan(exp(latRad)) - M_PI/2.0;
      std::cout << "tLatRad: "<<tLatRad <<"\n";
      std::cout << "LatRad: "<<latRad <<"\n";
      
      
      Geodetic2D mercg = *new Geodetic2D(Angle::fromDegrees(lonDeg), Angle::fromRadians(tLatRad*180.0/M_PI));
      
      
      
      
      
      std::cout << "lat: " << latDeg;
      std::cout << ", lon: " << lonDeg << "\n";
      
      //const double metersX = lonDeg * originShift /360.0;
      double metersY = log(tan((90+latDeg)*M_PI/360.0)) / (M_PI/180.0);
      //metersY =log(seq+tan(latRad)) / (M_PI/180.0);
      
      //double x = lonRad /2;
      //double y = log(tan(M_PI/4.0 + latRad/2));
      
      //std::cout<<"x: "<<x;
      //std::cout<<", y: "<<y <<"\n";
      //double gudermannian = log(seq + tan(latRad));
      //metersY = gudermannian / (M_PI/180.0);
      metersY = metersY /180.0;
      //std::cout << "metersX: " << metersX;
      //std::cout << ", metersY: " << metersY << "\n";
      
      //double s = (metersX / originShift)+0.5;
      double t = (metersY *seq)+0.5 ;
      //double t = (y/2.0)*(seq)+0.5 ;
      
      
      //t=gudermannian/2.0 +0.5 ;
      //std::cout<<"gudermann: "<<gudermannian << "\n";
      //std::cout<<"gudermann scaled: "<<gudermannian/limit<<"\n";
      
      std::cout << "s: " << s;
      std::cout << ", t: " << t << "\n";
      std::cout << "------------\n";
      
      
      
        
      //const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
      //const double t = asin(n.z()) / M_PI + 0.5;
      texCoors.push_back(MutableVector2D(s, 1-t));
    }
    //else {*/
      
    //}
    
    
    
/*=======
    const double s = GMath.atan2(n.y(), n.x()) / (GMath.pi() * 2) + 0.5;
    const double t = GMath.asin(n.z()) / GMath.pi() + 0.5;
    
    texCoors.add((float)s, (float)(1.0-t));
>>>>>>> android-port*/
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
    
    const GLImage* glImage = rc->getTextureBuilder()->createTextureFromImage(rc->getGL(),
                                                                             rc->getFactory(),
                                                                             RGBA, _image,
                                                                             _image->getWidth(),
                                                                             _image->getHeight());
    
    _texId = rc->getTexturesHandler()->getGLTextureId(glImage, "SINGLE_IMAGE_TEX", false);
    
    rc->getFactory()->deleteImage(_image);
    delete glImage;
    
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
