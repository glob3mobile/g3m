//
//  GEO3DMultiPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Nico on 24/10/2018.
//

#ifndef __G3MiOSSDK__GEO3DMultiPolygonGeometry__
#define __G3MiOSSDK__GEO3DMultiPolygonGeometry__

#include "GEO3DGeometry.hpp"

#include <vector>
class GEO3DPolygonData;


class GEO3DMultiPolygonGeometry : public GEO3DGeometry {
private:
  std::vector<GEO3DPolygonData*>* _polygonsData;
  
protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;
  
  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;
  
  static std::vector<GEO3DPolygonData*>* copy(const std::vector<GEO3DPolygonData*>* _polygonsData);
  
  const Sector* calculateSector() const;
  
public:
  
  GEO3DMultiPolygonGeometry(std::vector<GEO3DPolygonData*>* polygonsData) :
  _polygonsData(polygonsData)
  {
  }
  
  ~GEO3DMultiPolygonGeometry();
  
  const std::vector<GEO3DPolygonData*>* getPolygonsData() const {
    return _polygonsData;
  }
  
  long long getCoordinatesCount() const {
    return (_polygonsData == NULL) ? 0 : _polygonsData->size();
  }
  
  GEO3DMultiPolygonGeometry* deepCopy() const;
  
  bool contain(const Geodetic3D& point) const;
};

#endif
