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
class Geodetic2D;

class GEO2DMultiLineStringGeometry : public GEOMultiLineStringGeometry {
private:
  std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;

public:

  GEO2DMultiLineStringGeometry(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) :
  _coordinatesArray(coordinatesArray) {

  }

  ~GEO2DMultiLineStringGeometry();

};

#endif
