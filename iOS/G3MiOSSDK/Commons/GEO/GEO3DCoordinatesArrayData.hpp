//
//  GEO3DCoordinatesArrayData.hpp
//  G3MiOSSDK
//
//  Created by Nico on 23/10/2018.
//


#ifndef __G3MiOSSDK__GEO3DCoordinatesArrayData__
#define __G3MiOSSDK__GEO3DCoordinatesArrayData__

#include "RCObject.hpp"
#include <vector>
class GEO3DCoordinatesData;
class Sector;
class Geodetic3D;

class GEO3DCoordinatesArrayData : public RCObject {
private:
  std::vector<const GEO3DCoordinatesData*>* _coordinatesArray;
  
  mutable Sector* _sector;
  Sector* calculateSector() const;
  
protected:
  ~GEO3DCoordinatesArrayData();
  
public:
  GEO3DCoordinatesArrayData(std::vector<std::vector<Geodetic3D*>*>* coordinatesArray);
  
  size_t size() const {
    return (_coordinatesArray == NULL) ? 0 : _coordinatesArray->size();
  }
  
  const GEO3DCoordinatesData* get(size_t index) const {
    return _coordinatesArray->at(index);
  }
  
  const Sector* getSector() const;
  
  long long getCoordinatesCount() const;
  
};

#endif
