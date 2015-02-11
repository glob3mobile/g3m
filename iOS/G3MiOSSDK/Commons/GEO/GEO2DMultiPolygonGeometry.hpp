//
//  GEO2DMultiPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#ifndef __G3MiOSSDK__GEO2DMultiPolygonGeometry__
#define __G3MiOSSDK__GEO2DMultiPolygonGeometry__

#include "GEOGeometry2D.hpp"

#include <vector>
class GEO2DPolygonData;


class GEO2DMultiPolygonGeometry : public GEOGeometry2D {
private:
  std::vector<GEO2DPolygonData*>* _polygonsData;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

  static std::vector<GEO2DPolygonData*>* copy(const std::vector<GEO2DPolygonData*>* _polygonsData);


public:

  GEO2DMultiPolygonGeometry(std::vector<GEO2DPolygonData*>* polygonsData) :
  _polygonsData(polygonsData)
  {
  }

  ~GEO2DMultiPolygonGeometry();

  const std::vector<GEO2DPolygonData*>* getPolygonsData() const {
    return _polygonsData;
  }

  long long getCoordinatesCount() const {
    return (_polygonsData == NULL) ? 0 : _polygonsData->size();
  }

  GEO2DMultiPolygonGeometry* deepCopy() const;
  
  bool contain(const Geodetic2D& point) const;
};

#endif
