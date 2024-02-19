//
//  GEO2DPolygonGeometry.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#ifndef __G3M__GEO2DPolygonGeometry__
#define __G3M__GEO2DPolygonGeometry__

#include "GEO2DGeometry.hpp"
#include <vector>

class Geodetic2D;
class GEO2DPolygonData;
class GEOSymbol;

class GEO2DPolygonGeometry : public GEO2DGeometry {
private:
  const GEO2DPolygonData* _polygonData;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

public:
  GEO2DPolygonGeometry(const GEO2DPolygonData* polygonData) :
  _polygonData(polygonData)
  {
  }
  
  ~GEO2DPolygonGeometry();

  const GEO2DPolygonData* getPolygonData() const {
    return _polygonData;
  }

  const std::vector<Geodetic2D*>* getCoordinates() const;

  const std::vector<std::vector<Geodetic2D*>*>* getHolesCoordinatesArray() const;

  bool contain(const Geodetic2D& point) const;

};

#endif
