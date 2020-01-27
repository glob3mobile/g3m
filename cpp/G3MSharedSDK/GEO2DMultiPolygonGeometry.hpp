//
//  GEO2DMultiPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#ifndef __G3MiOSSDK__GEO2DMultiPolygonGeometry__
#define __G3MiOSSDK__GEO2DMultiPolygonGeometry__

#include "GEO2DGeometry.hpp"

#include <vector>
class GEO2DPolygonData;


class GEO2DMultiPolygonGeometry : public GEO2DGeometry {
private:
  std::vector<GEO2DPolygonData*>* _polygonsData;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

public:

  GEO2DMultiPolygonGeometry(std::vector<GEO2DPolygonData*>* polygonsData) :
  _polygonsData(polygonsData)
  {
  }

  ~GEO2DMultiPolygonGeometry();

  const std::vector<GEO2DPolygonData*>* getPolygonsData() const {
    return _polygonsData;
  }
  
  bool contain(const Geodetic2D& point) const;
};

#endif
