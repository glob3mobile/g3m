//
//  GEO3DPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//

#ifndef GEO3DPolygonGeometry_hpp
#define GEO3DPolygonGeometry_hpp

#include "GEO3DGeometry.hpp"
class Geodetic3D;
#include <vector>

class GEO3DPolygonData;

class GEO3DPolygonGeometry : public GEO3DGeometry {
private:
  const GEO3DPolygonData* _polygonData;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;

  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;

  const Sector* calculateSector() const;

public:
  GEO3DPolygonGeometry(const GEO3DPolygonData* polygonData) :
  _polygonData(polygonData)
  {
  }

  ~GEO3DPolygonGeometry();

  const GEO3DPolygonData* getPolygonData() const {
    return _polygonData;
  }

  const std::vector<Geodetic3D*>* getCoordinates() const;

  const std::vector<std::vector<Geodetic3D*>*>* getHolesCoordinatesArray() const;

  long long getCoordinatesCount() const;

  GEO3DPolygonGeometry* deepCopy() const;

  bool contain(const Geodetic3D& point) const;

};

#endif
