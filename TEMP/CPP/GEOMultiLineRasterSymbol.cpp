//
//  GEOMultiLineRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

#include "GEOMultiLineRasterSymbol.hpp"

#include "GEO2DLineRasterStyle.hpp"
#include "ICanvas.hpp"
#include "GEO2DCoordinatesArrayData.hpp"

GEOMultiLineRasterSymbol::GEOMultiLineRasterSymbol(const GEO2DCoordinatesArrayData* coordinatesArrayData,
                                                   const GEO2DLineRasterStyle& style,
                                                   const int minTileLevel,
                                                   const int maxTileLevel) :
GEORasterSymbol(minTileLevel, maxTileLevel),
_coordinatesArrayData(coordinatesArrayData),
_style(style)
{
  if (_coordinatesArrayData != NULL) {
    _coordinatesArrayData->_retain();
  }
}

GEOMultiLineRasterSymbol::~GEOMultiLineRasterSymbol() {
  if (_coordinatesArrayData != NULL) {
    _coordinatesArrayData->_release();
  }

#ifdef JAVA_CODE
  super.dispose();
#endif
}

const Sector* GEOMultiLineRasterSymbol::getSector() const {
  return (_coordinatesArrayData == NULL) ? NULL : _coordinatesArrayData->getSector();
}

void GEOMultiLineRasterSymbol::rawRasterize(ICanvas*                   canvas,
                                            const GEORasterProjection* projection) const {
  if (_coordinatesArrayData != NULL) {
    if (_style.apply(canvas)) {
      const size_t coordinatesArrayCount = _coordinatesArrayData->size();
      for (size_t i = 0; i < coordinatesArrayCount; i++) {
        const GEO2DCoordinatesData* coordinates = _coordinatesArrayData->get(i);
        if (coordinates != NULL) {
          rasterLine(coordinates,
                     canvas,
                     projection);
        }
      }
    }
  }
}
