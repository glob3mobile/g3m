//
//  GEO2DPolygonData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#include "GEO2DPolygonData.hpp"

#include "Geodetic2D.hpp"


GEO2DPolygonData::~GEO2DPolygonData() {
#ifdef C_CODE
  const int coordinatesCount = _coordinates->size();
  for (int i = 0; i < coordinatesCount; i++) {
    Geodetic2D* coordinate = _coordinates->at(i);
    delete coordinate;
  }
  delete _coordinates;


  if (_holesCoordinatesArray != NULL) {
    const int holesCoordinatesArraySize = _holesCoordinatesArray->size();
    for (int j = 0; j < holesCoordinatesArraySize; j++) {
      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);

      const int holeCoordinatesCount = holeCoordinates->size();
      for (int i =0; i < holeCoordinatesCount; i++) {
        const Geodetic2D* holeCoordinate = holeCoordinates->at(i);

        delete holeCoordinate;
      }

      delete holeCoordinates;
    }
    delete _holesCoordinatesArray;
  }
#endif
}
