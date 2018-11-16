//
//  GEO3DPolygonData.cpp
//  G3MiOSSDK
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 10/22/18.
//

#include "GEO3DPolygonData.hpp"

#include "Geodetic3D.hpp"
#include "Sector.hpp"


GEO3DPolygonData::~GEO3DPolygonData() {
#ifdef C_CODE
  if (_holesCoordinatesArray != NULL) {
    const size_t holesCoordinatesArraySize = _holesCoordinatesArray->size();
    for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
      const std::vector<Geodetic3D*>* holeCoordinates = _holesCoordinatesArray->at(j);

      const size_t holeCoordinatesCount = holeCoordinates->size();
      for (size_t i =0; i < holeCoordinatesCount; i++) {
        const Geodetic3D* holeCoordinate = holeCoordinates->at(i);

        delete holeCoordinate;
      }

      delete holeCoordinates;
    }
    delete _holesCoordinatesArray;
  }
#endif
#ifdef JAVA_CODE
  super.dispose();
#endif
}

long long GEO3DPolygonData::getCoordinatesCount() const {
  long long result = GEO3DCoordinatesData::getCoordinatesCount();
  if (_holesCoordinatesArray != NULL) {
    const size_t holesCoordinatesArraySize = _holesCoordinatesArray->size();
    for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
      const std::vector<Geodetic3D*>* holeCoordinates = _holesCoordinatesArray->at(j);
      result += holeCoordinates->size();
    }
  }
  return result;
}

bool GEO3DPolygonData::contains(const std::vector<Geodetic3D*>* coordinates, const Geodetic3D& point) const {
  int sidesCrossedMovingRight = 0;
  const size_t coordinatesCount = coordinates->size();
  size_t previousIndex = coordinatesCount - 1;

  for (size_t index = 0; index < coordinatesCount; index++) {

    Geodetic3D* firstCoordinate = coordinates->at(previousIndex);
    Geodetic3D* secondCoordinate = coordinates->at(index);

    if (!firstCoordinate->isEquals(*secondCoordinate)) {
      if (firstCoordinate->isEquals(point)) {
        return true;
      }

      const double pointLatInDegrees = point._latitude._degrees;
      const double firstCoorLatInDegrees = firstCoordinate->_latitude._degrees;
      const double secondCoorLatInDegrees = secondCoordinate->_latitude._degrees;

      const double pointLonInDegrees = point._longitude._degrees;
      const double firstCoorLonInDegrees = firstCoordinate->_longitude._degrees;
      const double secondCoorLonInDegrees = secondCoordinate->_longitude._degrees;


      if ((firstCoorLatInDegrees <= pointLatInDegrees && pointLatInDegrees < secondCoorLatInDegrees) ||
          (secondCoorLatInDegrees <= pointLatInDegrees && pointLatInDegrees < firstCoorLatInDegrees)) {

        double intersectLongitudInDegrees = (secondCoorLonInDegrees - firstCoorLonInDegrees) * (pointLatInDegrees - firstCoorLatInDegrees) / (secondCoorLatInDegrees - firstCoorLatInDegrees) + firstCoorLonInDegrees;


        if (pointLonInDegrees <= intersectLongitudInDegrees) {
          sidesCrossedMovingRight++;
        }
      }
    }
    previousIndex = index;
  }
  if (sidesCrossedMovingRight == 0) {
    return false;
  }

  return sidesCrossedMovingRight % 2 == 1;
}

bool GEO3DPolygonData::contains(const Geodetic3D& point) const {
  if (getSector()->contains(point._latitude, point._longitude)) {
    if (_holesCoordinatesArray != NULL) {
      const size_t holesCoordinatesArraySize = _holesCoordinatesArray->size();
      for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
        const std::vector<Geodetic3D*>* holeCoordinates = _holesCoordinatesArray->at(j);
        if (contains(holeCoordinates, point)) {
          return false;
        }
      }
    }

    return contains(getCoordinates(), point);
  }

  return false;
}
