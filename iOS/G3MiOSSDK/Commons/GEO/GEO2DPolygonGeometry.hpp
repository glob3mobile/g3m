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
class Geodetic2D;
#include <vector>

class GEO2DPolygonGeometry : public GEOGeometry2D {
private:
  std::vector<Geodetic2D*>*               _coordinates;
  std::vector<std::vector<Geodetic2D*>*>* _holesCoordinatesArray;

protected:
  std::vector<GEOSymbol*>* createSymbols(const G3MRenderContext* rc,
                                         const GEOSymbolizationContext& sc) const;

public:
  GEO2DPolygonGeometry(std::vector<Geodetic2D*>* coordinates) :
  _coordinates(coordinates),
  _holesCoordinatesArray(NULL)
  {
  }

  GEO2DPolygonGeometry(std::vector<Geodetic2D*>* coordinates,
                       std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray) :
  _coordinates(coordinates),
  _holesCoordinatesArray(holesCoordinatesArray)
  {
  }

  ~GEO2DPolygonGeometry();

  const std::vector<Geodetic2D*>* getCoordinates() const {
    return _coordinates;
  }

  const std::vector<std::vector<Geodetic2D*>*>* getHolesCoordinatesArray() const {
    return _holesCoordinatesArray;
  }

};

#endif
