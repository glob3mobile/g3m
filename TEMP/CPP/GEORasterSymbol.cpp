//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#include "GEORasterSymbol.hpp"
#include "ICanvas.hpp"
#include "GEORasterProjection.hpp"
#include "GEO2DPolygonData.hpp"
#include "GEOVectorLayer.hpp"

bool GEORasterSymbol::symbolize(const G3MRenderContext* rc,
                                const GEOSymbolizer*    symbolizer,
                                MeshRenderer*           meshRenderer,
                                ShapesRenderer*         shapesRenderer,
                                MarksRenderer*          marksRenderer,
                                GEOVectorLayer*         geoVectorLayer) const {
  if (geoVectorLayer == NULL) {
    ILogger::instance()->logError("Can't symbolize with RasterSymbol, GEOVectorLayer was not set");
    return true;
  }

  geoVectorLayer->addSymbol( this );

  return false;
}

GEORasterSymbol::~GEORasterSymbol() {
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GEORasterSymbol::rasterLine(const GEO2DCoordinatesData* coordinatesData,
                                 ICanvas*                    canvas,
                                 const GEORasterProjection*  projection) const {
  if (coordinatesData != NULL) {
    const std::vector<Geodetic2D*>* coordinates = coordinatesData->getCoordinates();

    const size_t coordinatesCount = coordinates->size();
    if (coordinatesCount > 0) {
      canvas->beginPath();

      canvas->moveTo( projection->project(coordinates->at(0)) );

      for (size_t i = 1; i < coordinatesCount; i++) {
        const Geodetic2D* coordinate = coordinates->at(i);

        canvas->lineTo( projection->project(coordinate) );
      }

      canvas->stroke();
    }
  }
}

void GEORasterSymbol::rasterPolygon(const GEO2DPolygonData*    polygonData,
                                    bool                       rasterSurface,
                                    bool                       rasterBoundary,
                                    ICanvas*                   canvas,
                                    const GEORasterProjection* projection) const {
  if (rasterSurface || rasterBoundary) {
    const std::vector<Geodetic2D*>* coordinates = polygonData->getCoordinates();
    const size_t coordinatesCount = coordinates->size();
    if (coordinatesCount > 1) {
      canvas->beginPath();

      canvas->moveTo( projection->project(coordinates->at(0)) );

      for (size_t i = 1; i < coordinatesCount; i++) {
        const Geodetic2D* coordinate = coordinates->at(i);

        canvas->lineTo( projection->project(coordinate) );
      }

      canvas->closePath();

      const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray = polygonData->getHolesCoordinatesArray();
      if (holesCoordinatesArray != NULL) {
        const size_t holesCoordinatesArraySize = holesCoordinatesArray->size();
        for (size_t j = 0; j < holesCoordinatesArraySize; j++) {
          const std::vector<Geodetic2D*>* holeCoordinates = holesCoordinatesArray->at(j);

          const size_t holeCoordinatesCount = holeCoordinates->size();
          if (holeCoordinatesCount > 1) {
            canvas->moveTo( projection->project(holeCoordinates->at(0)) );

            for (size_t i = 1; i < holeCoordinatesCount; i++) {
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

void GEORasterSymbol::rasterRectangle(const GEO2DPolygonData*    rectangleData,
                                      const Vector2F            rectangleSize,
                                      bool                       rasterSurface,
                                      bool                       rasterBoundary,
                                      ICanvas*                   canvas,
                                      const GEORasterProjection* projection) const {

  if (rasterSurface || rasterBoundary) {
    const std::vector<Geodetic2D*>* coordinates = rectangleData->getCoordinates();
    const size_t coordinatesCount = coordinates->size();

    if (coordinatesCount > 1) {
      canvas->beginPath();

      canvas->moveTo( projection->project(coordinates->at(0)) );

      for (size_t i = 1; i < coordinatesCount; i++) {
        const Geodetic2D* coordinate = coordinates->at(i);

        canvas->lineTo( projection->project(coordinate) );
      }

      canvas->closePath();

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
    else if (coordinatesCount == 1) {

      const Geodetic2D* coordinate = coordinates->at(0);

      Vector2F center = projection->project(coordinate);
      Vector2F topLeft = Vector2F(center._x-(rectangleSize._x/2.0f),center._y-(rectangleSize._y/2.0f));

      if (rasterBoundary) {
        if (rasterSurface) {
          canvas->fillAndStrokeRectangle(topLeft._x, topLeft._y,
                                         rectangleSize._x, rectangleSize._y);
        }
        else {
          canvas->strokeRectangle(topLeft._x, topLeft._y,
                                  rectangleSize._x, rectangleSize._y);
        }
      }
      else {
        canvas->fillRectangle(topLeft._x, topLeft._y,
                              rectangleSize._x, rectangleSize._y);
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
