//
//  GEO2DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEO2DMultiLineStringGeometry__
#define __G3MiOSSDK__GEO2DMultiLineStringGeometry__

#include "GEOMultiLineStringGeometry.hpp"

#include <vector>
#include "Color.hpp"

class Geodetic2D;

class GEO2DMultiLineStringGeometry : public GEOMultiLineStringGeometry {
private:
  std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;
  Color* _color;
  const float _lineWidth;

protected:
  Mesh* createMesh(const G3MRenderContext* rc);

public:

    GEO2DMultiLineStringGeometry(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray, Color* color = Color::newFromRGBA(1, 1, 1, 1), const float lineWidth = 2) :
  _coordinatesArray(coordinatesArray),
  _color(color),
  _lineWidth(lineWidth)
  {

  }

  ~GEO2DMultiLineStringGeometry();

};

#endif
