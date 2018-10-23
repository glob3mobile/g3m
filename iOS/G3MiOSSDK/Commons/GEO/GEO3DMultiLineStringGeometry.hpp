//
//  GEO3DMultiLineStringGeometry.hpp
//  G3MiOSSDK
//
//  Created by Nico on 23/10/2018.
//

#ifndef __G3MiOSSDK__GEO3DMultiLineStringGeometry__
#define __G3MiOSSDK__GEO3DMultiLineStringGeometry__

#include "GEO3DGeometry.hpp"
class Geodetic3D;
class GEO3DCoordinatesArrayData;

class GEO3DMultiLineStringGeometry : public GEO3DGeometry {
private:
  const GEO3DCoordinatesArrayData* _coordinatesArrayData;
  
  GEO3DMultiLineStringGeometry(const GEO3DCoordinatesArrayData* coordinatesArrayData);
  
protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer* symbolizer) const;
  
  std::vector<GEORasterSymbol*>* createRasterSymbols(const GEORasterSymbolizer* symbolizer) const;
  
public:
  
  GEO3DMultiLineStringGeometry(std::vector<std::vector<Geodetic3D*>*>* coordinatesArray);
  
  ~GEO3DMultiLineStringGeometry();
  
  const GEO3DCoordinatesArrayData* getCoordinatesArray() const {
    return _coordinatesArrayData;
  }
  
  long long getCoordinatesCount() const;
  
  GEO3DMultiLineStringGeometry* deepCopy() const;
  
};

#endif
