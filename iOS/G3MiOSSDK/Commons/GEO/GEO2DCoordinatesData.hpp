//
//  GEO2DCoordinatesData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 5/11/14.
//
//

#ifndef __G3MiOSSDK__GEO2DCoordinatesData__
#define __G3MiOSSDK__GEO2DCoordinatesData__


#include "RCObject.hpp"
#include <vector>
class Geodetic2D;
class Sector;

class GEO2DCoordinatesData : public RCObject {
private:
  const std::vector<Geodetic2D*>* _coordinates;

  mutable Sector* _sector;
  Sector* calculateSector() const;

protected:
  ~GEO2DCoordinatesData();

public:
  GEO2DCoordinatesData(const std::vector<Geodetic2D*>* coordinates) :
  _coordinates(coordinates),
  _sector(NULL)
  {
  }

  const std::vector<Geodetic2D*>* getCoordinates() const {
    return _coordinates;
  }

  const Sector* getSector() const;

  size_t size() const {
    return (_coordinates == NULL) ? 0 : _coordinates->size();
  }

  const Geodetic2D* get(size_t index) const {
    return _coordinates->at(index);
  }

  virtual long long getCoordinatesCount() const;
  
};


#endif
