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

public abstract class GEORasterSymbol extends GEOSymbol
{
  protected final Sector _sector;

  protected static java.util.ArrayList<Geodetic2D> copy(java.util.ArrayList<Geodetic2D> coordinates)
  {
    return coordinates;
  }
  protected static java.util.ArrayList<java.util.ArrayList<Geodetic2D>> copy(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
    return coordinatesArray;
  }

  protected static Sector calculateSector(java.util.ArrayList<java.util.ArrayList<Geodetic2D>> coordinatesArray)
  {
  
    final IMathUtils mu = IMathUtils.instance();
  
    final double maxDouble = mu.maxDouble();
    final double minDouble = mu.minDouble();
  
    double minLatInDegrees = maxDouble;
    double maxLatInDegrees = minDouble;
  
    double minLonInDegrees = maxDouble;
    double maxLonInDegrees = minDouble;
  
    final int coordinatesArrayCount = coordinatesArray.size();
    for (int i = 0; i < coordinatesArrayCount; i++)
    {
      java.util.ArrayList<Geodetic2D> coordinates = coordinatesArray.get(i);
      final int coordinatesCount = coordinates.size();
      for (int j = 0; j < coordinatesCount; j++)
      {
        final Geodetic2D coordinate = coordinates.get(j);
  
        final double latInDegrees = coordinate.latitude().degrees();
        if (latInDegrees < minLatInDegrees)
        {
          minLatInDegrees = latInDegrees;
        }
        else if (latInDegrees > maxLatInDegrees)
        {
          maxLatInDegrees = latInDegrees;
        }
  
        final double lonInDegrees = coordinate.longitude().degrees();
        if (lonInDegrees < minLonInDegrees)
        {
          minLonInDegrees = lonInDegrees;
        }
        else if (lonInDegrees > maxLonInDegrees)
        {
          maxLonInDegrees = lonInDegrees;
        }
      }
    }
  
    if ((minLatInDegrees == maxDouble) || (maxLatInDegrees == minDouble) || (minLonInDegrees == maxDouble) || (maxLonInDegrees == minDouble))
    {
      return null;
    }
  
    return new Sector(Geodetic2D.fromDegrees(minLatInDegrees, minLonInDegrees), Geodetic2D.fromDegrees(maxLatInDegrees, maxLonInDegrees));
  
  }
  protected static Sector calculateSector(java.util.ArrayList<Geodetic2D> coordinates)
  {
    final int size = coordinates.size();
    if (size == 0)
    {
      return null;
    }
  
    final Geodetic2D coordinate0 = coordinates.get(0);
  
    double minLatInDegrees = coordinate0.latitude().degrees();
    double maxLatInDegrees = minLatInDegrees;
  
    double minLonInDegrees = coordinate0.longitude().degrees();
    double maxLonInDegrees = minLonInDegrees;
  
    for (int i = 1; i < size; i++)
    {
      final Geodetic2D coordinate = coordinates.get(i);
  
      final double latInDegrees = coordinate.latitude().degrees();
      if (latInDegrees < minLatInDegrees)
      {
        minLatInDegrees = latInDegrees;
      }
      else if (latInDegrees > maxLatInDegrees)
      {
        maxLatInDegrees = latInDegrees;
      }
  
      final double lonInDegrees = coordinate.longitude().degrees();
      if (lonInDegrees < minLonInDegrees)
      {
        minLonInDegrees = lonInDegrees;
      }
      else if (lonInDegrees > maxLonInDegrees)
      {
        maxLonInDegrees = lonInDegrees;
      }
    }
  
    return new Sector(Geodetic2D.fromDegrees(minLatInDegrees, minLonInDegrees), Geodetic2D.fromDegrees(maxLatInDegrees, maxLonInDegrees));
  }

  protected GEORasterSymbol(Sector sector)
  {
     _sector = sector;
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

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    if (_sector != null)
    {
      sc.getGEOTileRasterizer().addSymbol(createSymbol());
    }
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public abstract GEORasterSymbol createSymbol();


  public abstract void rasterize(ICanvas canvas, GEORasterProjection projection);
}