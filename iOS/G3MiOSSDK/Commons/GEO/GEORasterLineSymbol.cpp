//
//  GEORasterLineSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEORasterLineSymbol.hpp"

Sector* GEORasterLineSymbol::calculateSector(const std::vector<Geodetic2D*>* coordinates) {
  const int size = coordinates->size();
  if (size == 0) {
    return NULL;
  }

  const Geodetic2D* coordinate0 = coordinates->at(0);

  double minLatInDegrees = coordinate0->latitude().degrees();
  double maxLatInDegrees = minLatInDegrees;

  double minLonInDegrees = coordinate0->longitude().degrees();
  double maxLonInDegrees = minLatInDegrees;

  for (int i = 1; i < size; i++) {
    const Geodetic2D* coordinate = coordinates->at(i);

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

  return new Sector(Geodetic2D::fromDegrees(minLatInDegrees, minLonInDegrees),
                    Geodetic2D::fromDegrees(maxLatInDegrees, maxLonInDegrees));
}

GEORasterLineSymbol* GEORasterLineSymbol::createSymbol() const {
  return new GEORasterLineSymbol(_coordinates, new Sector(*_sector));
}
