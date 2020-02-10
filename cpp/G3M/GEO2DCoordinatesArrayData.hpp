//
//  GEO2DCoordinatesArrayData.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

#ifndef __G3M__GEO2DCoordinatesArrayData__
#define __G3M__GEO2DCoordinatesArrayData__

#include "RCObject.hpp"
#include <vector>
class GEO2DCoordinatesData;
class Sector;
class Geodetic2D;

class GEO2DCoordinatesArrayData : public RCObject {
private:
  std::vector<const GEO2DCoordinatesData*>* _coordinatesArray;

#ifdef C_CODE
  mutable const Sector* _sector;
#endif
#ifdef JAVA_CODE
  private Sector _sector;
#endif
  const Sector* calculateSector() const;

protected:
  ~GEO2DCoordinatesArrayData();

public:
  GEO2DCoordinatesArrayData(std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);

  size_t size() const {
    return (_coordinatesArray == NULL) ? 0 : _coordinatesArray->size();
  }

  const GEO2DCoordinatesData* get(size_t index) const {
    return _coordinatesArray->at(index);
  }

  const Sector* getSector() const;

};

#endif
