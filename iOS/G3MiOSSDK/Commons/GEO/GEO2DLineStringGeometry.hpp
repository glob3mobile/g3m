//
//  GEO2DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEO2DLineStringGeometry__
#define __G3MiOSSDK__GEO2DLineStringGeometry__

#include "GEOLineStringGeometry.hpp"

#include <vector>
#include "Color.hpp"

class Geodetic2D;

class GEO2DLineStringGeometry : public GEOLineStringGeometry {
private:
  std::vector<Geodetic2D*>* _coordinates;
  Color* _color;
  float _lineWidth;

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:

  GEO2DLineStringGeometry(std::vector<Geodetic2D*>* coordinates, Color* color = Color::newFromRGBA(1, 1, 0, 1),
                            float lineWidth = 2) :
  _coordinates(coordinates),
  _color(color),
  _lineWidth(lineWidth)
  {
        
  }  
    
  ~GEO2DLineStringGeometry();

};

#endif
