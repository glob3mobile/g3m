//
//  GEO2DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEO2DMultiLineStringGeometry__
#define __G3MiOSSDK__GEO2DMultiLineStringGeometry__

#include "GEOGeometry2D.hpp"
#include <vector>
class Geodetic2D;


class GEO2DMultiLineStringGeometry : public GEOGeometry2D {
private:
  std::vector<std::vector<Geodetic2D*>*>* _coordinatesArray;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

public:

  GEO2DMultiLineStringGeometry(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) :
  _coordinatesArray(coordinatesArray) {

  }

  ~GEO2DMultiLineStringGeometry();

  const std::vector<std::vector<Geodetic2D*>*>* getCoordinatesArray() const {
    return _coordinatesArray;
  }

  
};

#endif
