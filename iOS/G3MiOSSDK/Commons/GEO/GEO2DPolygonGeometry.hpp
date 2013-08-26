//
//  GEO2DPolygonGeometry.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

#ifndef __G3MiOSSDK__GEO2DPolygonGeometry__
#define __G3MiOSSDK__GEO2DPolygonGeometry__

#include "GEOGeometry2D.hpp"
//class Geodetic2D;
//#include <vector>
class Geodetic2D;
#include <vector>

class GEO2DPolygonData;

class GEO2DPolygonGeometry : public GEOGeometry2D {
private:
//  std::vector<Geodetic2D*>*               _coordinates;
//  std::vector<std::vector<Geodetic2D*>*>* _holesCoordinatesArray;
  const GEO2DPolygonData* _polygonData;

protected:
  std::vector<GEOSymbol*>* createSymbols(const GEOSymbolizer*    symbolizer) const;

public:
//  GEO2DPolygonGeometry(std::vector<Geodetic2D*>* coordinates,
//                       std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray) :
//  _coordinates(coordinates),
//  _holesCoordinatesArray(holesCoordinatesArray)
//  {
//  }

  GEO2DPolygonGeometry(const GEO2DPolygonData* polygonData) :
  _polygonData(polygonData)
  {
  }
  

  ~GEO2DPolygonGeometry();

  const GEO2DPolygonData* getPolygonData() const {
    return _polygonData;
  }

  const std::vector<Geodetic2D*>* getCoordinates() const;

  const std::vector<std::vector<Geodetic2D*>*>* getHolesCoordinatesArray() const;

};

#endif
