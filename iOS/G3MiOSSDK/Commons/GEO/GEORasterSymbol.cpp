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
#include "GEO2DPolygonData.hpp"

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
#ifdef JAVA_CODE
  super.dispose();
#endif
}

void GEORasterSymbol::rasterLine(const GEO2DCoordinatesData* coordinatesData,
                                 ICanvas*                    canvas,
                                 const GEORasterProjection*  projection) const {
  if (coordinatesData != NULL) {
    const std::vector<Geodetic2D*>* coordinates = coordinatesData->getCoordinates();

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
}

void GEORasterSymbol::rasterPolygon(const GEO2DPolygonData*    polygonData,
                                    bool                       rasterSurface,
                                    bool                       rasterBoundary,
                                    ICanvas*                   canvas,
                                    const GEORasterProjection* projection) const {
  if (rasterSurface || rasterBoundary) {
    const std::vector<Geodetic2D*>* coordinates = polygonData->getCoordinates();
    const int coordinatesCount = coordinates->size();
    if (coordinatesCount > 1) {
      canvas->beginPath();

      canvas->moveTo( projection->project(coordinates->at(0)) );

      for (int i = 1; i < coordinatesCount; i++) {
        const Geodetic2D* coordinate = coordinates->at(i);

        canvas->lineTo( projection->project(coordinate) );
      }

      canvas->closePath();

      const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray = polygonData->getHolesCoordinatesArray();
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
