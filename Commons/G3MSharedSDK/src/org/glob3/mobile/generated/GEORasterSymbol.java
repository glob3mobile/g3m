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
//class GEO2DPolygonData;

//class GEO2DCoordinatesData;

public abstract class GEORasterSymbol extends GEOSymbol implements QuadTree_Content
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  GEORasterSymbol(GEORasterSymbol that);

  private final int _minTileLevel;
  private final int _maxTileLevel;

<<<<<<< HEAD
  protected final Sector _sector;

  protected static java.util.ArrayList<java.util.ArrayList<Geodetic2D>> copyCoordinatesArray(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
=======
  protected GEORasterSymbol(int minTileLevel, int maxTileLevel)
>>>>>>> origin/senderos-gc
  {
     _minTileLevel = minTileLevel;
     _maxTileLevel = maxTileLevel;
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

  public abstract Sector getSector();

  public final void rasterize(ICanvas canvas, GEORasterProjection projection, int tileLevel)
  {
    if (((_minTileLevel < 0) || (tileLevel >= _minTileLevel)) && ((_maxTileLevel < 0) || (tileLevel <= _maxTileLevel)))
    {
      rawRasterize(canvas, projection);
    }
  }

<<<<<<< HEAD
  // useless, it's here only to make the C++ => Java translator creates an interface intead of an empty class
  public final void unusedMethod()
  {
  }

  public static java.util.ArrayList<Geodetic2D> copyCoordinates(java.util.ArrayList<Geodetic2D> coordinates)
  {
    return coordinates;
  }

=======
>>>>>>> origin/senderos-gc
}