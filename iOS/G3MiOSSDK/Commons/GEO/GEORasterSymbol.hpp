//
//  GEORasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

#ifndef __G3MiOSSDK__GEORasterSymbol__
#define __G3MiOSSDK__GEORasterSymbol__

#include "GEOSymbol.hpp"
#include <vector>

#include "Vector2F.hpp"
class GEORasterProjection;
class ICanvas;
class GEO2DPolygonData;
#include "QuadTree.hpp"

class GEO2DCoordinatesData;

class GEORasterSymbol : public GEOSymbol, public QuadTree_Content {
private:
  GEORasterSymbol(const GEORasterSymbol& that);

  const int _minTileLevel;
  const int _maxTileLevel;

protected:
  GEORasterSymbol(const int minTileLevel,
                  const int maxTileLevel) :
  _minTileLevel(minTileLevel),
  _maxTileLevel(maxTileLevel)
  {
  }

  void rasterLine(const GEO2DCoordinatesData* coordinates,
                  ICanvas*                    canvas,
                  const GEORasterProjection*  projection) const;

  void rasterPolygon(const GEO2DPolygonData*    polygonData,
                     bool                       rasterSurface,
                     bool                       rasterBoundary,
                     ICanvas*                   canvas,
                     const GEORasterProjection* projection) const;

  void rasterRectangle(const GEO2DPolygonData*  rectangleData,
                       const Vector2F            rectangleSize,
                       bool                       rasterSurface,
                       bool                       rasterBoundary,
                       ICanvas*                   canvas,
                       const GEORasterProjection* projection) const;

  virtual void rawRasterize(ICanvas*                   canvas,
                            const GEORasterProjection* projection) const = 0;


public:
  virtual ~GEORasterSymbol();

  bool symbolize(const G3MRenderContext* rc,
                 const GEOSymbolizer*    symbolizer,
                 MeshRenderer*           meshRenderer,
                 ShapesRenderer*         shapesRenderer,
                 MarksRenderer*          marksRenderer,
                 GEOVectorLayer*         geoVectorLayer) const;

  virtual const Sector* getSector() const = 0;

  void rasterize(ICanvas*                   canvas,
                 const GEORasterProjection* projection,
                 int tileLevel) const;

};

#endif
