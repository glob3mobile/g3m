//
//  GEO3DPolygonData.hpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//

#ifndef GEO3DPolygonData_hpp
#define GEO3DPolygonData_hpp

#include "GEO3DCoordinatesData.hpp"


class GEO3DPolygonData : public GEO3DCoordinatesData {
private:
  const std::vector<std::vector<Geodetic3D*>*>* _holesCoordinatesArray;

  bool contains(const std::vector<Geodetic3D*>* coordinates, const Geodetic3D& point) const;

protected:
  ~GEO3DPolygonData();

public:
  GEO3DPolygonData(const std::vector<Geodetic3D*>*               coordinates,
                   const std::vector<std::vector<Geodetic3D*>*>* holesCoordinatesArray) :
  GEO3DCoordinatesData(coordinates),
  _holesCoordinatesArray(holesCoordinatesArray)
  {
  }

  const std::vector<std::vector<Geodetic3D*>*>* getHolesCoordinatesArray() const {
    return _holesCoordinatesArray;
  }

  bool contains(const Geodetic3D& point) const;

};

#endif
