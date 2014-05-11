package org.glob3.mobile.generated; 
//
//  GEORasterSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//

//
//  GEORasterSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/9/13.
//
//



///#include "Sector.hpp"
//class GEORasterProjection;
//class ICanvas;
//class GEO2DPolygonData;

//class GEO2DCoordinatesData;

public abstract class GEORasterSymbol extends GEOSymbol implements QuadTree_Content
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GEORasterSymbol(GEORasterSymbol that);

  private final int _minTileLevel;
  private final int _maxTileLevel;

//  const Sector* _sector;

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning remove copyCoordinates method
//  static std::vector<Geodetic2D*>* copyCoordinates(const std::vector<Geodetic2D*>* coordinates);
//  static std::vector<std::vector<Geodetic2D*>*>* copyCoordinatesArray(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);
//
//  static Sector* calculateSectorFromCoordinates(const std::vector<Geodetic2D*>* coordinates);
//  static Sector* calculateSectorFromCoordinatesArray(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray);

  protected GEORasterSymbol(int minTileLevel, int maxTileLevel) //const Sector* sector,
//  _sector(sector),
  {
     _minTileLevel = minTileLevel;
     _maxTileLevel = maxTileLevel;
//    if (_sector == NULL) {
//      printf("break point on me\n");
//    }
  }

  protected final void rasterLine(GEO2DCoordinatesData coordinatesData, ICanvas canvas, GEORasterProjection projection)
  {
    if (coordinatesData != null)
    {
      final java.util.ArrayList<Geodetic2D> coordinates = coordinatesData.getCoordinates();
  
      final int coordinatesCount = coordinates.size();
      if (coordinatesCount > 0)
      {
        canvas.beginPath();
  
        canvas.moveTo(projection.project(coordinates.get(0)));
  
        for (int i = 1; i < coordinatesCount; i++)
        {
          final Geodetic2D coordinate = coordinates.get(i);
  
          canvas.lineTo(projection.project(coordinate));
        }
  
        canvas.stroke();
      }
    }
  }

//  void rasterPolygon(const std::vector<Geodetic2D*>*               coordinates,
//                     const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray,
//                     bool                                          rasterSurface,
//                     bool                                          rasterBoundary,
//                     ICanvas*                                      canvas,
//                     const GEORasterProjection*                    projection) const;

  protected final void rasterPolygon(GEO2DPolygonData polygonData, boolean rasterSurface, boolean rasterBoundary, ICanvas canvas, GEORasterProjection projection)
  {
    if (rasterSurface || rasterBoundary)
    {
      final java.util.ArrayList<Geodetic2D> coordinates = polygonData.getCoordinates();
      final int coordinatesCount = coordinates.size();
      if (coordinatesCount > 1)
      {
        canvas.beginPath();
  
        canvas.moveTo(projection.project(coordinates.get(0)));
  
        for (int i = 1; i < coordinatesCount; i++)
        {
          final Geodetic2D coordinate = coordinates.get(i);
  
          canvas.lineTo(projection.project(coordinate));
        }
  
        canvas.closePath();
  
        final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray = polygonData.getHolesCoordinatesArray();
        if (holesCoordinatesArray != null)
        {
          final int holesCoordinatesArraySize = holesCoordinatesArray.size();
          for (int j = 0; j < holesCoordinatesArraySize; j++)
          {
            final java.util.ArrayList<Geodetic2D> holeCoordinates = holesCoordinatesArray.get(j);
  
            final int holeCoordinatesCount = holeCoordinates.size();
            if (holeCoordinatesCount > 1)
            {
              canvas.moveTo(projection.project(holeCoordinates.get(0)));
  
              for (int i = 1; i < holeCoordinatesCount; i++)
              {
                final Geodetic2D holeCoordinate = holeCoordinates.get(i);
  
                canvas.lineTo(projection.project(holeCoordinate));
              }
  
              canvas.closePath();
            }
          }
        }
  
  
        if (rasterBoundary)
        {
          if (rasterSurface)
          {
            canvas.fillAndStroke();
          }
          else
          {
            canvas.stroke();
          }
        }
        else
        {
          canvas.fill();
        }
      }
    }
  
  }


  protected abstract void rawRasterize(ICanvas canvas, GEORasterProjection projection);


  public void dispose()
  {
  //  delete _sector;
  
    super.dispose();
  
  }


  //std::vector<Geodetic2D*>* GEORasterSymbol::copyCoordinates(const std::vector<Geodetic2D*>* coordinates) {
  ///#ifdef C_CODE
  //  if (coordinates == NULL) {
  //    return NULL;
  //  }
  //
  //  std::vector<Geodetic2D*>* result = new std::vector<Geodetic2D*>();
  //
  //  const int size = coordinates->size();
  //  for (int i = 0; i < size; i++) {
  //    const Geodetic2D* coordinate = coordinates->at(i);
  //    result->push_back( new Geodetic2D(*coordinate) );
  //  }
  //  return result;
  ///#endif
  ///#ifdef JAVA_CODE
  //  return coordinates;
  ///#endif
  //}
  //
  //std::vector<std::vector<Geodetic2D*>*>* GEORasterSymbol::copyCoordinatesArray(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {
  ///#ifdef C_CODE
  //  if (coordinatesArray == NULL) {
  //    return NULL;
  //  }
  //
  //  std::vector<std::vector<Geodetic2D*>*>* result = new std::vector<std::vector<Geodetic2D*>*>();
  //  const int coordinatesArrayCount = coordinatesArray->size();
  //  for (int i = 0; i < coordinatesArrayCount; i++) {
  //    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
  //    result->push_back( copyCoordinates(coordinates) );
  //  }
  //
  //  return result;
  ///#endif
  ///#ifdef JAVA_CODE
  //  return coordinatesArray;
  ///#endif
  //}
  //
  //
  //Sector* GEORasterSymbol::calculateSectorFromCoordinatesArray(const std::vector<std::vector<Geodetic2D*>*>* coordinatesArray) {
  //
  //  const IMathUtils* mu = IMathUtils::instance();
  //
  //  const double maxDouble = mu->maxDouble();
  //  const double minDouble = mu->minDouble();
  //
  //  double minLatInRadians = maxDouble;
  //  double maxLatInRadians = minDouble;
  //
  //  double minLonInRadians = maxDouble;
  //  double maxLonInRadians = minDouble;
  //
  //  const int coordinatesArrayCount = coordinatesArray->size();
  //  for (int i = 0; i < coordinatesArrayCount; i++) {
  //    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
  //    const int coordinatesCount = coordinates->size();
  //    for (int j = 0; j < coordinatesCount; j++) {
  //      const Geodetic2D* coordinate = coordinates->at(j);
  //
  //      const double latInRadians = coordinate->_latitude._radians;
  //      if (latInRadians < minLatInRadians) {
  //        minLatInRadians = latInRadians;
  //      }
  //      if (latInRadians > maxLatInRadians) {
  //        maxLatInRadians = latInRadians;
  //      }
  //
  //      const double lonInRadians = coordinate->_longitude._radians;
  //      if (lonInRadians < minLonInRadians) {
  //        minLonInRadians = lonInRadians;
  //      }
  //      if (lonInRadians > maxLonInRadians) {
  //        maxLonInRadians = lonInRadians;
  //      }
  //    }
  //  }
  //
  //  if ((minLatInRadians == maxDouble) ||
  //      (maxLatInRadians == minDouble) ||
  //      (minLonInRadians == maxDouble) ||
  //      (maxLonInRadians == minDouble)) {
  //    return NULL;
  //  }
  //
  //  Sector* result = new Sector(Geodetic2D::fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001),
  //                              Geodetic2D::fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  //
  //  //  int __REMOVE_DEBUG_CODE;
  //  //  for (int i = 0; i < coordinatesArrayCount; i++) {
  //  //    std::vector<Geodetic2D*>* coordinates = coordinatesArray->at(i);
  //  //    const int coordinatesCount = coordinates->size();
  //  //    for (int j = 0; j < coordinatesCount; j++) {
  //  //      const Geodetic2D* coordinate = coordinates->at(j);
  //  //      if (!result->contains(*coordinate)) {
  //  //        printf("xxx\n");
  //  //      }
  //  //    }
  //  //  }
  //
  //  return result;
  //}
  //
  //Sector* GEORasterSymbol::calculateSectorFromCoordinates(const std::vector<Geodetic2D*>* coordinates) {
  //  const int size = coordinates->size();
  //  if (size == 0) {
  //    return NULL;
  //  }
  //
  //  const Geodetic2D* coordinate0 = coordinates->at(0);
  //
  //  double minLatInRadians = coordinate0->_latitude._radians;
  //  double maxLatInRadians = minLatInRadians;
  //
  //  double minLonInRadians = coordinate0->_longitude._radians;
  //  double maxLonInRadians = minLonInRadians;
  //
  //  for (int i = 1; i < size; i++) {
  //    const Geodetic2D* coordinate = coordinates->at(i);
  //
  //    const double latInRadians = coordinate->_latitude._radians;
  //    if (latInRadians < minLatInRadians) {
  //      minLatInRadians = latInRadians;
  //    }
  //    if (latInRadians > maxLatInRadians) {
  //      maxLatInRadians = latInRadians;
  //    }
  //
  //    const double lonInRadians = coordinate->_longitude._radians;
  //    if (lonInRadians < minLonInRadians) {
  //      minLonInRadians = lonInRadians;
  //    }
  //    if (lonInRadians > maxLonInRadians) {
  //      maxLonInRadians = lonInRadians;
  //    }
  //  }
  //
  //  Sector* result = new Sector(Geodetic2D::fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001),
  //                              Geodetic2D::fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  //
  //  //  int __REMOVE_DEBUG_CODE;
  //  //  for (int i = 0; i < size; i++) {
  //  //    const Geodetic2D* coordinate = coordinates->at(i);
  //  //    if (!result->contains(*coordinate)) {
  //  //      printf("xxx\n");
  //  //    }
  //  //  }
  //
  //  return result;
  //}
  
  
  
  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOTileRasterizer geoTileRasterizer)
  {
    if (geoTileRasterizer == null)
    {
      ILogger.instance().logError("Can't simbolize with RasterSymbol, GEOTileRasterizer was not set");
      return true;
    }
  
    geoTileRasterizer.addSymbol(this);
  
    return false;
  }

  public abstract Sector getSector();


  //void GEORasterSymbol::rasterPolygon(const std::vector<Geodetic2D*>*               coordinates,
  //                                    const std::vector<std::vector<Geodetic2D*>*>* holesCoordinatesArray,
  //                                    bool                                          rasterSurface,
  //                                    bool                                          rasterBoundary,
  //                                    ICanvas*                                      canvas,
  //                                    const GEORasterProjection*                    projection) const {
  //  if (rasterSurface || rasterBoundary) {
  //    const int coordinatesCount = coordinates->size();
  //    if (coordinatesCount > 1) {
  //      canvas->beginPath();
  //
  //      canvas->moveTo( projection->project(coordinates->at(0)) );
  //
  //      for (int i = 1; i < coordinatesCount; i++) {
  //        const Geodetic2D* coordinate = coordinates->at(i);
  //
  //        canvas->lineTo( projection->project(coordinate) );
  //      }
  //
  //      canvas->closePath();
  //
  //      if (holesCoordinatesArray != NULL) {
  //        const int holesCoordinatesArraySize = holesCoordinatesArray->size();
  //        for (int j = 0; j < holesCoordinatesArraySize; j++) {
  //          const std::vector<Geodetic2D*>* holeCoordinates = holesCoordinatesArray->at(j);
  //
  //          const int holeCoordinatesCount = holeCoordinates->size();
  //          if (holeCoordinatesCount > 1) {
  //            canvas->moveTo( projection->project(holeCoordinates->at(0)) );
  //
  //            for (int i = 1; i < holeCoordinatesCount; i++) {
  //              const Geodetic2D* holeCoordinate = holeCoordinates->at(i);
  //
  //              canvas->lineTo( projection->project(holeCoordinate) );
  //            }
  //
  //            canvas->closePath();
  //          }
  //        }
  //      }
  //
  //
  //      if (rasterBoundary) {
  //        if (rasterSurface) {
  //          canvas->fillAndStroke();
  //        }
  //        else {
  //          canvas->stroke();
  //        }
  //      }
  //      else {
  //        canvas->fill();
  //      }
  //    }
  //  }
  //}
  
  public final void rasterize(ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
    if (((_minTileLevel < 0) || (tileLevel >= _minTileLevel)) && ((_maxTileLevel < 0) || (tileLevel <= _maxTileLevel)))
    {
      rawRasterize(canvas, projection);
    }
  }
}