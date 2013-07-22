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
  
    double minLatInRadians = coordinate0.latitude().radians();
    double maxLatInRadians = minLatInRadians;
  
    double minLonInRadians = coordinate0.longitude().radians();
    double maxLonInRadians = minLonInRadians;
  
    for (int i = 1; i < size; i++)
    {
      final Geodetic2D coordinate = coordinates.get(i);
  
      final double latInRadians = coordinate.latitude().radians();
      if (latInRadians < minLatInRadians)
      {
        minLatInRadians = latInRadians;
      }
      if (latInRadians > maxLatInRadians)
      {
        maxLatInRadians = latInRadians;
      }
  
      final double lonInRadians = coordinate.longitude().radians();
      if (lonInRadians < minLonInRadians)
      {
        minLonInRadians = lonInRadians;
      }
      if (lonInRadians > maxLonInRadians)
      {
        maxLonInRadians = lonInRadians;
      }
    }
  
  //  return new Sector(Geodetic2D::fromDegrees(minLatInDegrees, minLonInDegrees),
  //                    Geodetic2D::fromDegrees(maxLatInDegrees, maxLonInDegrees));
  
  
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
  
        final double latInRadians = coordinate.latitude().radians();
        if (latInRadians < minLatInRadians)
        {
          minLatInRadians = latInRadians;
        }
        if (latInRadians > maxLatInRadians)
        {
          maxLatInRadians = latInRadians;
        }
  
        final double lonInRadians = coordinate.longitude().radians();
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
  
  //  return new Sector(Geodetic2D::fromDegrees(minLatInDegrees, minLonInDegrees),
  //                    Geodetic2D::fromDegrees(maxLatInDegrees, maxLonInDegrees));
  
  
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

  protected GEORasterSymbol(Sector sector)
  {
     _sector = sector;
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

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
  }

  public final boolean symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
  
    if (_sector == null)
    {
      System.out.print("break point on me\n");
      return true;
    }
  
    sc.getGEOTileRasterizer().addSymbol(this);
    return false;
  }

  public final boolean deleteAfterSymbolize()
  {
    return false;
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public abstract void rasterize(ICanvas canvas, GEORasterProjection projection);

}