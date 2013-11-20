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



//class GEORasterProjection;
//class ICanvas;


public abstract class GEORasterSymbol extends GEOSymbol implements QuadTree_Content
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GEORasterSymbol(GEORasterSymbol that);

  private final int _minTileLevel;
  private final int _maxTileLevel;

  protected final Sector _sector;

  protected static java.util.ArrayList<Geodetic2D> copyCoordinates(java.util.ArrayList<Geodetic2D> coordinates)
  {
    return coordinates;
  }
  protected static java.util.ArrayList<java.util.ArrayList<Geodetic2D>> copyCoordinatesArray(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
    return coordinatesArray;
  }

  protected static Sector calculateSectorFromCoordinates(java.util.ArrayList<Geodetic2D> coordinates)
  {
    final int size = coordinates.size();
    if (size == 0)
    {
      return null;
    }
  
    final Geodetic2D coordinate0 = coordinates.get(0);
  
    double minLatInRadians = coordinate0._latitude._radians;
    double maxLatInRadians = minLatInRadians;
  
    double minLonInRadians = coordinate0._longitude._radians;
    double maxLonInRadians = minLonInRadians;
  
    for (int i = 1; i < size; i++)
    {
      final Geodetic2D coordinate = coordinates.get(i);
  
      final double latInRadians = coordinate._latitude._radians;
      if (latInRadians < minLatInRadians)
      {
        minLatInRadians = latInRadians;
      }
      if (latInRadians > maxLatInRadians)
      {
        maxLatInRadians = latInRadians;
      }
  
      final double lonInRadians = coordinate._longitude._radians;
      if (lonInRadians < minLonInRadians)
      {
        minLonInRadians = lonInRadians;
      }
      if (lonInRadians > maxLonInRadians)
      {
        maxLonInRadians = lonInRadians;
      }
    }
  
    Sector result = new Sector(Geodetic2D.fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001), Geodetic2D.fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  
    //  int __REMOVE_DEBUG_CODE;
    //  for (int i = 0; i < size; i++) {
    //    const Geodetic2D* coordinate = coordinates->at(i);
    //    if (!result->contains(*coordinate)) {
    //      printf("xxx\n");
    //    }
    //  }
  
    return result;
  }
  protected static Sector calculateSectorFromCoordinatesArray(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
  
    final IMathUtils mu = IMathUtils.instance();
  
    final double maxDouble = mu.maxDouble();
    final double minDouble = mu.minDouble();
  
    double minLatInRadians = maxDouble;
    double maxLatInRadians = minDouble;
  
    double minLonInRadians = maxDouble;
    double maxLonInRadians = minDouble;
  
    final int coordinatesArrayCount = coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        final Geodetic2D coordinate = coordinates.get(j);
  
        final double latInRadians = coordinate._latitude._radians;
        if (latInRadians < minLatInRadians)
        {
          minLatInRadians = latInRadians;
        }
        if (latInRadians > maxLatInRadians)
        {
          maxLatInRadians = latInRadians;
        }
  
        final double lonInRadians = coordinate._longitude._radians;
        if (lonInRadians < minLonInRadians)
        {
          minLonInRadians = lonInRadians;
        }
        if (lonInRadians > maxLonInRadians)
        {
          maxLonInRadians = lonInRadians;
        }
      }
    }
  
    if ((minLatInRadians == maxDouble) || (maxLatInRadians == minDouble) || (minLonInRadians == maxDouble) || (maxLonInRadians == minDouble))
    {
      return null;
    }
  
    Sector result = new Sector(Geodetic2D.fromRadians(minLatInRadians - 0.0001, minLonInRadians - 0.0001), Geodetic2D.fromRadians(maxLatInRadians + 0.0001, maxLonInRadians + 0.0001));
  
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

  protected GEORasterSymbol(Sector sector, int minTileLevel, int maxTileLevel)
  {
     _sector = sector;
     _minTileLevel = minTileLevel;
     _maxTileLevel = maxTileLevel;
//    if (_sector == NULL) {
//      printf("break point on me\n");
//    }
  }

  protected final void rasterLine(java.util.ArrayList<Geodetic2D> coordinates, ICanvas canvas, GEORasterProjection projection)
  {
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

  protected final void rasterPolygon(java.util.ArrayList<Geodetic2D> coordinates, java.util.ArrayList<java.util.ArrayList<Geodetic2D>> holesCoordinatesArray, boolean rasterSurface, boolean rasterBoundary, ICanvas canvas, GEORasterProjection projection)
  {
    if (rasterSurface || rasterBoundary)
    {
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


  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
  
    super.dispose();
  
  }

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

  public final Sector getSector()
  {
    return _sector;
  }

  public final void rasterize(ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
    if (((_minTileLevel < 0) || (tileLevel >= _minTileLevel)) && ((_maxTileLevel < 0) || (tileLevel <= _maxTileLevel)))
    {
      rawRasterize(canvas, projection);
    }
  }

  public abstract void rawRasterize(ICanvas canvas, GEORasterProjection projection);

  // useless, it's here only to make the C++ => Java translator creates an interface intead of an empty class
  public final void unusedMethod()
  {
  }

}