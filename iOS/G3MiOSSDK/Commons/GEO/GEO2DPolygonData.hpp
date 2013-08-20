//
//  GEO2DPolygonData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#ifndef __G3MiOSSDK__GEO2DPolygonData__
#define __G3MiOSSDK__GEO2DPolygonData__

#include <vector>
class Geodetic2D;


class GEO2DPolygonData {
private:
  const std::vector<Geodetic2D*>*               _coordinates;
  const std::vector<std::vector<Geodetic2D*>*>* _holesCoordinatesArray;

  GEO2DPolygonData(const GEO2DPolygonData& that);

public:
  GEO2DPolygonData(const std::vector<Geodetic2D*>*               coordinates,
                   const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray) :
  _coordinates(coordinates),
  _holesCoordinatesArray(holesCoordinatesArray)
  {
  }

  ~GEO2DPolygonData();

  const std::vector<Geodetic2D*>* getCoordinates() const {
    return _coordinates;
  }

  const std::vector<std::vector<Geodetic2D*>*>* getHolesCoordinatesArray() const {
    return _holesCoordinatesArray;
  }

};


#endif
