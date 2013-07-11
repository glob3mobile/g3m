//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#include "GEORasterSymbol.hpp"

#include "GEOSymbolizationContext.hpp"
#include "GEOTileRasterizer.hpp"
#include "ICanvas.hpp"
#include "GEORasterProjection.hpp"


std::vector<Geodetic2D*>* GEORasterSymbol::copy(const std::vector<Geodetic2D*>* coordinates) {
#ifdef C_CODE
  std::vector<Geodetic2D*>* result = new std::vector<Geodetic2D*>();
  
  const int size = coordinates->size();
  for (int i = 0; i < size; i++) {
    const Geodetic2D* coordinate = coordinates->at(i);
    result->push_back( new Geodetic2D(*coordinate) );
  }
  return result;
#endif
#ifdef JAVA_CODE
  return coordinates;
#endif
}

std::vector<std::vector<Geodetic2D*>*>* GEORasterSymbol::copy(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {
#ifdef C_CODE
  std::vector<std::vector<Geodetic2D*>*>* result = new std::vector<std::vector<Geodetic2D*>*>();
  const int coordinatesArrayCount = coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
    result->push_back( copy(coordinates) );
  }

  return result;
#endif
#ifdef JAVA_CODE
  return coordinatesArray;
#endif
}


Sector* GEORasterSymbol::calculateSector(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {

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

Sector* GEORasterSymbol::calculateSector(const std::vector<Geodetic2D*>* coordinates) {
  const int size = coordinates->size();
  if (size == 0) {
    return NULL;
  }

  const Geodetic2D* coordinate0 = coordinates->at(0);

  double minLatInDegrees = coordinate0->latitude().degrees();
  double maxLatInDegrees = minLatInDegrees;

  double minLonInDegrees = coordinate0->longitude().degrees();
  double maxLonInDegrees = minLonInDegrees;

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



void GEORasterSymbol::symbolize(const G3MRenderContext* rc,
                                const GEOSymbolizationContext& sc) const {
  if (_sector != NULL) {
    sc.getGEOTileRasterizer()->addSymbol( createSymbol() );
  }
}

GEORasterSymbol::~GEORasterSymbol() {
  delete _sector;
}

void GEORasterSymbol::rasterLine(const std::vector<Geodetic2D*>* coordinates,
                                 ICanvas*                        canvas,
                                 const GEORasterProjection*      projection) const {
  const int coordinatesCount = coordinates->size();
  if (coordinatesCount > 0) {
    canvas->beginPath();

    canvas->moveTo( projection->project(coordinates->at(0)) );

    for (int i = 1; i < coordinatesCount; i++) {
      const Geodetic2D* coordinate = coordinates->at(i);

      canvas->lineTo( projection->project(coordinate) );
    }

    canvas->stroke();
  }
}

