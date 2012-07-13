//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_EllipsoidalTileTessellator_hpp
#define G3MiOSSDK_EllipsoidalTileTessellator_hpp

#include "TileTessellator.hpp"
#include <string>
#include <vector>
#include "MutableVector3D.hpp"
#include "MutableVector2D.hpp"
#include "Geodetic2D.hpp"
#include "Planet.hpp"

class EllipsoidalTileTessellator : public TileTessellator {
private:
  
  const std::string  _textureFilename;
  const unsigned int _resolution;
  const bool         _skirted;
  const bool         _debugMode;
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        std::vector<MutableVector2D>* texCoords,
                        const Geodetic3D& g) {
    
    vertices->push_back( planet->toVector3D(g).asMutableVector3D() );
    
    
    const Vector3D n = planet->geodeticSurfaceNormal(g);
    
    const double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
    const double t = asin(n.z()) / M_PI + 0.5;
    
    // const double s = (g.longitude().degrees() + 180) / 360;
    // const double t = (g.latitude().degrees() + 90) / 180;
    
    texCoords->push_back(MutableVector2D(s, 1-t));
  }
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        std::vector<MutableVector2D>* texCoords,
                        const Geodetic2D& g) {
    addVertex(planet, vertices, texCoords, Geodetic3D(g, 0.0));
  }
  
  Mesh* createDebugMesh(const RenderContext* rc,
                        const Tile* tile) const;
  
  EllipsoidalTileTessellator(const std::string textureFilename,
                             const unsigned int resolution,
                             const bool skirted,
                             const bool debugMode) :
  _textureFilename(textureFilename),
  _resolution(resolution),
  _skirted(skirted),
  _debugMode(debugMode)
  {
    
  }
  
public:
  static EllipsoidalTileTessellator* create(const std::string textureFilename,
                                            const unsigned int resolution,
                                            const bool skirted) {
    return new EllipsoidalTileTessellator(textureFilename, resolution, skirted, false);
  }
  
  static EllipsoidalTileTessellator* createForDebug(const unsigned int resolution) {
    return new EllipsoidalTileTessellator("", resolution, true, true);
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  virtual Mesh* createMesh(const RenderContext* rc,
                           const Tile* tile) const;
  
};

#endif
