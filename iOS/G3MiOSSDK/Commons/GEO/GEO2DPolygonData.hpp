//
//  GEO2DPolygonData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#ifndef __G3MiOSSDK__GEO2DPolygonData__
#define __G3MiOSSDK__GEO2DPolygonData__

#include "GEO2DCoordinatesData.hpp"
//#include <vector>
//#include "RCObject.hpp"
//class Geodetic2D;

class GEO2DPolygonData : public GEO2DCoordinatesData {
private:
  const std::vector<std::vector<Geodetic2D*>*>* _holesCoordinatesArray;
  
  bool contains(const std::vector<Geodetic2D*>* coordinates, const Geodetic2D& point) const;
  
protected:
  ~GEO2DPolygonData();

public:
  GEO2DPolygonData(const std::vector<Geodetic2D*>*               coordinates,
                   const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray) :
  GEO2DCoordinatesData(coordinates),
  _holesCoordinatesArray(holesCoordinatesArray)
  {
  }

  const std::vector<std::vector<Geodetic2D*>*>* getHolesCoordinatesArray() const {
    return _holesCoordinatesArray;
  }

  long long getCoordinatesCount() const;

  bool contains(const Geodetic2D& point) const;

};


#endif
