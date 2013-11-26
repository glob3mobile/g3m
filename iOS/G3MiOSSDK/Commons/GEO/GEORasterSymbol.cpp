//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#include "GEORasterSymbol.hpp"

#include "GEOTileRasterizer.hpp"
#include "ICanvas.hpp"
#include "GEORasterProjection.hpp"


std::vector<Geodetic2D*>* GEORasterSymbol::copyCoordinates(const std::vector<Geodetic2D*>* coordinates) {
#ifdef C_CODE
  if (coordinates == NULL) {
    return NULL;
  }

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

std::vector<std::vector<Geodetic2D*>*>* GEORasterSymbol::copyCoordinatesArray(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {
#ifdef C_CODE
  if (coordinatesArray == NULL) {
    return NULL;
  }

  std::vector<std::vector<Geodetic2D*>*>* result = new std::vector<std::vector<Geodetic2D*>*>();
  const int coordinatesArrayCount = coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
    result->push_back( copyCoordinates(coordinates) );
  }

  return result;
#endif
#ifdef JAVA_CODE
  return coordinatesArray;
#endif
}


Sector* GEORasterSymbol::calculateSectorFromCoordinatesArray(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {

  const IMathUtils* mu = IMathUtils::instance();

  const double maxDouble = mu->maxDouble();
  const double minDouble = mu->minDouble();

  double minLatInRadians = maxDouble;
  double maxLatInRadians = minDouble;

  double minLonInRadians = maxDouble;
  double maxLonInRadians = minDouble;

  const int coordinatesArrayCount = coordinatesArray->size();
  for (int i = 0; i < coordinatesArrayCount; i++) {
    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
    const int coordinatesCount = coordinates->size();
    for (int j = 0; j < coordinatesCount; j++) {
      const Geodetic2D* coordinate = coordinates->at(j);

      const double latInRadians = coordinate->_latitude._radians;
      if (latInRadians < minLatInRadians) {
        minLatInRadians = latInRadians;
      }
      if (latInRadians > maxLatInRadians) {
        maxLatInRadians = latInRadians;
      }

      const double lonInRadians = coordinate->_longitude._radians;
      if (lonInRadians < minLonInRadians) {
        minLonInRadians = lonInRadians;
      }
      if (lonInRadians > maxLonInRadians) {
        maxLonInRadians = lonInRadians;
      }
    }
  }

  if ((minLatInRadians == maxDouble) ||
      (maxLatInRadians == minDouble) ||
      (minLonInRadians == maxDouble) ||
      (maxLonInRadians == minDouble)) {
    return NULL;
  }

  Sector* result = new Sector(Geodetic2D::fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001),
                              Geodetic2D::fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));

  //  int __REMOVE_DEBUG_CODE;
  //  for (int i = 0; i < coordinatesArrayCount; i++) {
  //    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
  //    const int coordinatesCount = coordinates->size();
  //    for (int j = 0; j < coordinatesCount; j++) {
  //      const Geodetic2D* coordinate = coordinates->at(j);
  //      if (!result->contains(*coordinate)) {
  //        printf("xxx\n");
  //      }
  //    }
  //  }

  return result;
}

Sector* GEORasterSymbol::calculateSectorFromCoordinates(const std::vector<Geodetic2D*>* coordinates) {
  const int size = coordinates->size();
  if (size == 0) {
    return NULL;
  }

  const Geodetic2D* coordinate0 = coordinates->at(0);

  double minLatInRadians = coordinate0->_latitude._radians;
  double maxLatInRadians = minLatInRadians;

  double minLonInRadians = coordinate0->_longitude._radians;
  double maxLonInRadians = minLonInRadians;

  for (int i = 1; i < size; i++) {
    const Geodetic2D* coordinate = coordinates->at(i);

    const double latInRadians = coordinate->_latitude._radians;
    if (latInRadians < minLatInRadians) {
      minLatInRadians = latInRadians;
    }
    if (latInRadians > maxLatInRadians) {
      maxLatInRadians = latInRadians;
    }

    const double lonInRadians = coordinate->_longitude._radians;
    if (lonInRadians < minLonInRadians) {
      minLonInRadians = lonInRadians;
    }
    if (lonInRadians > maxLonInRadians) {
      maxLonInRadians = lonInRadians;
    }
  }

  Sector* result = new Sector(Geodetic2D::fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001),
                              Geodetic2D::fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));

  //  int __REMOVE_DEBUG_CODE;
  //  for (int i = 0; i < size; i++) {
  //    const Geodetic2D* coordinate = coordinates->at(i);
  //    if (!result->contains(*coordinate)) {
  //      printf("xxx\n");
  //    }
  //  }

  return result;
}



bool GEORasterSymbol::symbolize(const G3MRenderContext* rc,
                                const GEOSymbolizer*    symbolizer,
                                MeshRenderer*           meshRenderer,
                                ShapesRenderer*         shapesRenderer,
                                MarksRenderer*          marksRenderer,
                                GEOTileRasterizer*      geoTileRasterizer) const {
  if (geoTileRasterizer == NULL) {
    ILogger::instance()->logError("Can't simbolize with RasterSymbol, GEOTileRasterizer was not set");
    return true;
  }

  geoTileRasterizer->addSymbol( this );

  return false;
}

GEORasterSymbol::~GEORasterSymbol() {
  delete _sector;

#ifdef JAVA_CODE
  super.dispose();
#endif

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

int __REMOVE_INCLUDE;
#include "Color.hpp"

void GEORasterSymbol::rasterPolygon(const std::vector<Geodetic2D*>*               coordinates,
                                    const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray,
                                    bool                                          rasterSurface,
                                    bool                                          rasterBoundary,
                                    ICanvas*                                      canvas,
                                    const GEORasterProjection*                    projection) const {
  if (rasterSurface || rasterBoundary) {
    const int coordinatesCount = coordinates->size();
    if (coordinatesCount > 1) {
      canvas->beginPath();

      canvas->moveTo( projection->project(coordinates->at(0)) );

      for (int i = 1; i < coordinatesCount; i++) {
        const Geodetic2D* coordinate = coordinates->at(i);

        canvas->lineTo( projection->project(coordinate) );
      }

      canvas->closePath();

      if (holesCoordinatesArray != NULL) {
        const int holesCoordinatesArraySize = holesCoordinatesArray->size();
        for (int j = 0; j < holesCoordinatesArraySize; j++) {
          const std::vector<Geodetic2D*>* holeCoordinates = holesCoordinatesArray->at(j);

          const int holeCoordinatesCount = holeCoordinates->size();
          if (holeCoordinatesCount > 1) {
            canvas->moveTo( projection->project(holeCoordinates->at(0)) );

            for (int i = 1; i < holeCoordinatesCount; i++) {
              const Geodetic2D* holeCoordinate = holeCoordinates->at(i);

              canvas->lineTo( projection->project(holeCoordinate) );
            }

            canvas->closePath();
          }
        }
      }


      if (rasterBoundary) {
        if (rasterSurface) {
          canvas->fillAndStroke();
        }
        else {
          canvas->stroke();
        }
      }
      else {
        canvas->fill();
      }
    }
  }
}

void GEORasterSymbol::rasterize(ICanvas*                   canvas,
                                const GEORasterProjection* projection,
                                int tileLevel) const {
  if (((_minTileLevel < 0) || (tileLevel >= _minTileLevel)) &&
      ((_maxTileLevel < 0) || (tileLevel <= _maxTileLevel))) {
    rawRasterize(canvas, projection);
  }
}
