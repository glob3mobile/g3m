//
//  GEO2DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3MiOSSDK__GEO2DLineStringGeometry__
#define __G3MiOSSDK__GEO2DLineStringGeometry__

#include "GEOGeometry2D.hpp"
class Geodetic2D;
#include <vector>


class GEO2DLineStringGeometry : public GEOGeometry2D {
private:
  std::vector<Geodetic2D*>* _coordinates;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;


public:

  GEO2DLineStringGeometry(std::vector<Geodetic2D*>* coordinates) :
  _coordinates(coordinates)
  {

  }

  ~GEO2DLineStringGeometry();

  const std::vector<Geodetic2D*>* getCoordinates() const {
    return _coordinates;
  }


};

#endif
