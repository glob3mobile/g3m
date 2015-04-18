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
class Geodetic2D;
class GEO2DCoordinatesArrayData;

class GEO2DMultiLineStringGeometry : public GEOGeometry2D {
private:
  const GEO2DCoordinatesArrayData* _coordinatesArrayData;

  GEO2DMultiLineStringGeometry(const GEO2DCoordinatesArrayData* coordinatesArrayData);

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

public:

  GEO2DMultiLineStringGeometry(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);

  ~GEO2DMultiLineStringGeometry();

  const GEO2DCoordinatesArrayData* getCoordinatesArray() const {
    return _coordinatesArrayData;
  }

  long long getCoordinatesCount() const;

  GEO2DMultiLineStringGeometry* deepCopy() const;

};

#endif
