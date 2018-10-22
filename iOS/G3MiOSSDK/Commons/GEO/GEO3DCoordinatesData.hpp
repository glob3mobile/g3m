//
//  GEO3DCoordinatesData.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//

#ifndef GEO3DCoordinatesData_hpp
#define GEO3DCoordinatesData_hpp

#include "RCObject.hpp"
#include <vector>

class Geodetic3D;
class Sector;


class GEO3DCoordinatesData : public RCObject {
private:
  const std::vector<Geodetic3D*>* _coordinates;

  mutable Sector* _sector;
  Sector* calculateSector() const;

protected:
  ~GEO3DCoordinatesData();

public:
  GEO3DCoordinatesData(const std::vector<Geodetic3D*>* coordinates) :
  _coordinates(coordinates),
  _sector(NULL)
  {
  }

  const std::vector<Geodetic3D*>* getCoordinates() const {
    return _coordinates;
  }

  const Sector* getSector() const;

  const size_t size() const {
    return (_coordinates == NULL) ? 0 : _coordinates->size();
  }

  const Geodetic3D* get(size_t index) const {
    return _coordinates->at(index);
  }

  virtual long long getCoordinatesCount() const;

};

#endif
