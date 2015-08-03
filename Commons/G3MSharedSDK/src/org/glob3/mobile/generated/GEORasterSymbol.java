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

  protected GEORasterSymbol(int minTileLevel, int maxTileLevel)
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

  protected final void rasterRectangle(GEO2DPolygonData rectangleData, Vector2F rectangleSize, boolean rasterSurface, boolean rasterBoundary, ICanvas canvas, GEORasterProjection projection)
  {
  
    if (rasterSurface || rasterBoundary)
    {
      final java.util.ArrayList<Geodetic2D> coordinates = rectangleData.getCoordinates();
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
      else if (coordinatesCount == 1)
      {
  
        final Geodetic2D coordinate = coordinates.get(0);
  
        Vector2F center = projection.project(coordinate);
        Vector2F topLeft = new Vector2F(center._x-(rectangleSize._x/2.0f),center._y-(rectangleSize._y/2.0f));
  
        if (rasterBoundary)
        {
          if (rasterSurface)
          {
            canvas.fillAndStrokeRectangle(topLeft._x, topLeft._y, rectangleSize._x, rectangleSize._y);
          }
          else
          {
            canvas.strokeRectangle(topLeft._x, topLeft._y, rectangleSize._x, rectangleSize._y);
          }
        }
        else
        {
          canvas.fillRectangle(topLeft._x, topLeft._y, rectangleSize._x, rectangleSize._y);
        }
      }
  
    }
  }

  protected abstract void rawRasterize(ICanvas canvas, GEORasterProjection projection);


  public void dispose()
  {
    super.dispose();
  }

  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizer symbolizer, MeshRenderer meshRenderer, ShapesRenderer shapesRenderer, MarksRenderer marksRenderer, GEOVectorLayer geoVectorLayer)
  {
    if (geoVectorLayer == null)
    {
      ILogger.instance().logError("Can't symbolize with RasterSymbol, GEOVectorLayer was not set");
      return true;
    }
  
    geoVectorLayer.addSymbol(this);
  
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

}