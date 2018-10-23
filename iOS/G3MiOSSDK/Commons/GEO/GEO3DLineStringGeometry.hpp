//
//  GEO3DLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Nico on 22/10/2018.
//

#ifndef __G3MiOSSDK__GEO3DLineStringGeometry__
#define __G3MiOSSDK__GEO3DLineStringGeometry__

#include "GEO3DGeometry.hpp"
class Geodetic3D;
#include "GEO3DCoordinatesData.hpp"

class GEO3DLineStringGeometry : public GEO3DGeometry {
private:
  const GEO3DCoordinatesData* _coordinatesData;
  
  GEO3DLineStringGeometry(const GEO3DCoordinatesData* coordinatesData) :
  _coordinatesData(coordinatesData)
  {
    if (_coordinatesData != NULL) {
      _coordinatesData->_retain();
    }
  }
  
protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;
  
  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;
  
public:
  
  GEO3DLineStringGeometry(std::vector<Geodetic3D*>* coordinates)
  {
    _coordinatesData = (coordinates == NULL) ? NULL : new GEO3DCoordinatesData(coordinates);
  }
  
  ~GEO3DLineStringGeometry();
  
  const GEO3DCoordinatesData* getCoordinates() const {
    return _coordinatesData;
  }
  
  long long getCoordinatesCount() const {
    return _coordinatesData->size();
  }
  
  GEO3DLineStringGeometry* deepCopy() const;
  
};

#endif
