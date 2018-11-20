package org.glob3.mobile.generated;
//
//  InterpolatedDEMGrid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

//
//  InterpolatedDEMGrid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//




public class InterpolatedDEMGrid extends DecoratorDEMGrid
{
  private InterpolatedDEMGrid(DEMGrid grid, Sector sector, Vector2I extent)
  {
     super(grid, sector, extent);
  
  }

  private static double getElevationAt(DEMGrid grid, double u, double v)
  {
    if ((u < 0) || (u > 1) || (v < 0) || (v > 1))
    {
      return Double.NaN;
    }
  
    final Vector2I gridExtent = grid.getExtent();
    final double dX = u * (gridExtent._x - 1);
    final double dY = v * (gridExtent._y - 1);
  
    final int x = (int) dX;
    final int y = (int) dY;
    final int nextX = x + 1;
    final int nextY = y + 1;
    final double alphaY = dY - y;
    final double alphaX = dX - x;
  
    if (x == dX)
    {
      if (y == dY)
      {
        // exact on grid point
        return grid.getElevation(x, y);
      }
  
      // linear on Y
      final double heightY = grid.getElevation(x, y);
      final double heightNextY = grid.getElevation(x, nextY);
      return linearInterpolation(heightY, heightNextY, alphaY);
    }
  
    if (y == dY)
    {
      // linear on X
      final double heightX = grid.getElevation(x, y);
      final double heightNextX = grid.getElevation(nextX, y);
      return linearInterpolation(heightX, heightNextX, alphaX);
    }
  
    // bilinear
    final double valueNW = grid.getElevation(x, y);
    final double valueNE = grid.getElevation(nextX, y);
    final double valueSE = grid.getElevation(nextX, nextY);
    final double valueSW = grid.getElevation(x, nextY);
    return bilinearInterpolation(valueSW, valueSE, valueNE, valueNW, alphaX, alphaY);
  }

  private static double bilinearInterpolation(double valueSW, double valueSE, double valueNE, double valueNW, double u, double v)
  {
    final double alphaSW = (1.0 - u) * v;
    final double alphaSE = u * v;
    final double alphaNE = u * (1.0 - v);
    final double alphaNW = (1.0 - u) * (1.0 - v);
  
    return ((alphaSW * valueSW) + (alphaSE * valueSE) + (alphaNE * valueNE) + (alphaNW * valueNW));
  }

  private static double linearInterpolation(double from, double to, double alpha)
  {
    return from + ((to - from) * alpha);
  }


  public static InterpolatedDEMGrid create(DEMGrid grid, Vector2S extent)
  {
    return new InterpolatedDEMGrid(grid, grid.getSector(), new Vector2I(extent._x, extent._y));
  }

  public final double getElevation(int x, int y)
  {
    final double u = (double) x / _extent._x;
    final double v = (double) y / _extent._y;
  
    return getElevationAt(_grid, u, v);
  }

  public final double getElevation(Angle latitude, Angle longitude)
  {
    // const Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
    // const double u = uv._x;
    // const double v = 1.0 - uv._y;
  
    final double u = _sector.getUCoordinate(longitude);
    final double v = 1.0 - _sector.getVCoordinate(latitude);
  
    return getElevationAt(this, u, v);
  }

}
