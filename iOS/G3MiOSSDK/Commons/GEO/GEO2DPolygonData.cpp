//
//  GEO2DPolygonData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/25/13.
//
//

#include "GEO2DPolygonData.hpp"

#include "Geodetic2D.hpp"
#include "Sector.hpp"


GEO2DPolygonData::~GEO2DPolygonData() {
#ifdef C_CODE
  if (_holesCoordinatesArray != NULL) {
    const size_t holesCoordinatesArraySize = _holesCoordinatesArray->size();
    for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);

      const size_t holeCoordinatesCount = holeCoordinates->size();
      for (size_t i =0; i < holeCoordinatesCount; i++) {
        const Geodetic2D* holeCoordinate = holeCoordinates->at(i);

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

long long GEO2DPolygonData::getCoordinatesCount() const {
  long long result = GEO2DCoordinatesData::getCoordinatesCount();
  if (_holesCoordinatesArray != NULL) {
    const size_t holesCoordinatesArraySize = _holesCoordinatesArray->size();
    for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);
      result += holeCoordinates->size();
    }
  }
  return result;
}

bool GEO2DPolygonData::contains(const std::vector<Geodetic2D*>* coordinates, const Geodetic2D& point) const {
  int sidesCrossedMovingRight = 0;
  const size_t coordinatesCount = coordinates->size();
  size_t previousIndex = coordinatesCount - 1;
  
  for (size_t index = 0; index < coordinatesCount; index++) {
    
    Geodetic2D* firstCoordinate = coordinates->at(previousIndex);
    Geodetic2D* secondCoordinate = coordinates->at(index);
    
    if (!firstCoordinate->isEquals(*secondCoordinate)) {
      if (firstCoordinate->isEquals(point)){
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



bool GEO2DPolygonData::contains(const Geodetic2D& point) const {
  if (getSector()->contains(point)) {
    if (_holesCoordinatesArray != NULL) {
      const size_t holesCoordinatesArraySize = _holesCoordinatesArray->size();
      for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
        const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);
        if (contains(holeCoordinates, point)) {
          return false;
        }
      }
    }
    
    return contains(getCoordinates(), point);
  }
  
  return false;
}

