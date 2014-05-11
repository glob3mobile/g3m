package org.glob3.mobile.generated; 
//
//  GEOPolygonRasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEOPolygonRasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//




//class GEO2DPolygonData;


public class GEOPolygonRasterSymbol extends GEORasterSymbol
{
  private final GEO2DPolygonData _polygonData;
//  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _lineStyle;
  private final GEO2DSurfaceRasterStyle   _surfaceStyle;

//  const std::vector<std::vector<Geodetic2D*>*>* _holesCoordinatesArray;

  protected final void rawRasterize(ICanvas canvas, GEORasterProjection projection)
  {
    final boolean rasterSurface = _surfaceStyle.apply(canvas);
    final boolean rasterBoundary = _lineStyle.apply(canvas);
  
  //  rasterPolygon(_coordinates,
  //                _holesCoordinatesArray,
  //                rasterSurface,
  //                rasterBoundary,
  //                canvas,
  //                projection);
  
  ///#warning TODO pass _polygonData
  //  rasterPolygon(_polygonData->getCoordinates(),
  //                _polygonData->getHolesCoordinatesArray(),
  //                rasterSurface,
  //                rasterBoundary,
  //                canvas,
  //                projection);
  
    rasterPolygon(_polygonData, rasterSurface, rasterBoundary, canvas, projection);
  }


  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel)
  {
     this(polygonData, lineStyle, surfaceStyle, minTileLevel, -1);
  }
  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
  {
     this(polygonData, lineStyle, surfaceStyle, -1, -1);
  }
  public GEOPolygonRasterSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle, int minTileLevel, int maxTileLevel)
  //_coordinates( copyCoordinates(polygonData->getCoordinates()) ),
  //_holesCoordinatesArray( copyCoordinatesArray(polygonData->getHolesCoordinatesArray()) ),
  {
     super(minTileLevel, maxTileLevel);
     _polygonData = polygonData;
     _lineStyle = lineStyle;
     _surfaceStyle = surfaceStyle;
    if (_polygonData != null)
    {
      _polygonData._retain();
    }
  }

  public void dispose()
  {
  ///#ifdef C_CODE
  //  if (_coordinates != NULL) {
  //    const int coordinatesSize = _coordinates->size();
  //    for (int i = 0; i < coordinatesSize; i++) {
  //      Geodetic2D* coordinate = _coordinates->at(i);
  //      delete coordinate;
  //    }
  //    delete _coordinates;
  //  }
  //
  //  if (_holesCoordinatesArray != NULL) {
  //    const int holesCoordinatesArraySize = _holesCoordinatesArray->size();
  //    for (int j = 0; j < holesCoordinatesArraySize; j++) {
  //      const std::vector<Geodetic2D*>* holeCoordinates = _holesCoordinatesArray->at(j);
  //
  //      const int holeCoordinatesCount = holeCoordinates->size();
  //      for (int i = 0; i < holeCoordinatesCount; i++) {
  //        const Geodetic2D* holeCoordinate = holeCoordinates->at(i);
  //        delete holeCoordinate;
  //      }
  //
  //      delete holeCoordinates;
  //    }
  //    delete _holesCoordinatesArray;
  //  }
  ///#endif
    if (_polygonData != null)
    {
      _polygonData._release();
    }
  
    super.dispose();
  }

  public final Sector getSector()
  {
    return (_polygonData == null) ? null : _polygonData.getSector();
  }

}