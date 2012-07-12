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
  
  const std::string _textureFilename;
  const unsigned int _resolution;
  const bool _skirted;
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        std::vector<MutableVector2D>* texCoords,
                        const Geodetic3D& g) {
    vertices->push_back( planet->toVector3D(g).asMutableVector3D() );
    
    
    Vector3D n = planet->geodeticSurfaceNormal(g);
    
    double s = atan2(n.y(), n.x()) / (M_PI * 2) + 0.5;
    double t = asin(n.z()) / M_PI + 0.5;
    
    //    const double s = (g.longitude().degrees() + 180) / 360;
    //    const double t = (g.latitude().degrees() + 90) / 180;
    
    MutableVector2D texCoord(s, 1-t);
    texCoords->push_back(texCoord);
  }
  
  static void addVertex(const Planet* planet,
                        std::vector<MutableVector3D>* vertices,
                        std::vector<MutableVector2D>* texCoords,
                        const Geodetic2D& g) {
    addVertex(planet, vertices, texCoords, Geodetic3D(g, 0.0));
  }
  
  
public:
  EllipsoidalTileTessellator(const std::string textureFilename, const unsigned int resolution, const bool skirted) :
  _textureFilename(textureFilename), _resolution(resolution), _skirted(skirted)
  {
    
  }
  
  virtual ~EllipsoidalTileTessellator() { }
  
  virtual Mesh* createMesh(const RenderContext* rc,
                           const Tile* tile) const;
};

#endif
