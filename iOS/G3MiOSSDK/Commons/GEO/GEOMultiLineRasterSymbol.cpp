//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOMultiLineRasterSymbol.hpp"

Sector* GEOMultiLineRasterSymbol::calculateSector(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {

  const IMathUtils* mu = IMathUtils::instance();

  const double maxDouble = mu->maxDouble();
  const double minDouble = mu->minDouble();

  double minLatInDegrees = maxDouble;
  double maxLatInDegrees = minDouble;

  double minLonInDegrees = maxDouble;
  double maxLonInDegrees = minDouble;

  const int coordinatesArrayCount = coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
    const int coordinatesCount = coordinates->size();
    for (int j = 0; j < coordinatesCount; j++) {
      const Geodetic2D* coordinate = coordinates->at(j);

      const double latInDegrees = coordinate->latitude().degrees();
      if (latInDegrees < minLatInDegrees) {
        minLatInDegrees = latInDegrees;
      }
      else if (latInDegrees > maxLatInDegrees) {
        maxLatInDegrees = latInDegrees;
      }

      const double lonInDegrees = coordinate->longitude().degrees();
      if (lonInDegrees < minLonInDegrees) {
        minLonInDegrees = lonInDegrees;
      }
      else if (lonInDegrees > maxLonInDegrees) {
        maxLonInDegrees = lonInDegrees;
      }
    }
  }

  if ((minLatInDegrees == maxDouble) ||
      (maxLatInDegrees == minDouble) ||
      (minLonInDegrees == maxDouble) ||
      (maxLonInDegrees == minDouble)) {
    return NULL;
  }

  return new Sector(Geodetic2D::fromDegrees(minLatInDegrees, minLonInDegrees),
                    Geodetic2D::fromDegrees(maxLatInDegrees, maxLonInDegrees));
  
}

GEOMultiLineRasterSymbol* GEOMultiLineRasterSymbol::createSymbol() const {
  return new GEOMultiLineRasterSymbol(_coordinatesArray, new Sector(*_sector) );
}
