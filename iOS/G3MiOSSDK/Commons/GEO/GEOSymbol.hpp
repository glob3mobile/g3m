//
//  GEOSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/25/13.
//
//

#ifndef __G3MiOSSDK__GEOSymbol__
#define __G3MiOSSDK__GEOSymbol__

#include <vector>

class Mesh;
class G3MRenderContext;
class Ellipsoid;
class Color;
class Geodetic2D;


class GEOSymbol {
protected:
  Mesh* createLine2DMesh(const std::vector<Geodetic2D*>* coordinates,
                         const Color& lineColor,
                         float lineWidth,
                         double deltaHeight,
                         const Ellipsoid* ellipsoid);

  Mesh* createLines2DMesh(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray,
                          const Color& lineColor,
                          float lineWidth,
                          double deltaHeight,
                          const Ellipsoid* ellipsoid);


public:
  virtual ~GEOSymbol() { }

  virtual Mesh* createMesh(const G3MRenderContext* rc) = 0;

};

#endif
