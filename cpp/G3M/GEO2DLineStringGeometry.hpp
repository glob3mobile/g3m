//
//  GEO2DLineStringGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/30/12.
//
//

#ifndef __G3M__GEO2DLineStringGeometry__
#define __G3M__GEO2DLineStringGeometry__

#include "GEO2DGeometry.hpp"
class Geodetic2D;
#include "GEO2DCoordinatesData.hpp"

class GEO2DLineStringGeometry : public GEO2DGeometry {
private:
  const GEO2DCoordinatesData* _coordinatesData;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

public:

  GEO2DLineStringGeometry(std::vector<Geodetic2D*>* coordinates)
  {
    _coordinatesData = (coordinates == NULL) ? NULL : new GEO2DCoordinatesData(coordinates);
  }

  ~GEO2DLineStringGeometry();

  const GEO2DCoordinatesData* getCoordinates() const {
    return _coordinatesData;
  }

};

#endif
