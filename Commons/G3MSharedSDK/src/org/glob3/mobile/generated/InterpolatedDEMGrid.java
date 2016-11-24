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
      return java.lang.Double.NaN;
    }
  
    final double dX = u * (grid.getExtent()._x - 1);
    final double dY = v * (grid.getExtent()._y - 1);
  
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
        return grid.getElevationAt(x, y);
      }
  
      // linear on Y
      final double heightY = grid.getElevationAt(x, y);
      final double heightNextY = grid.getElevationAt(x, nextY);
      return linearInterpolation(heightY, heightNextY, alphaY);
    }
  
    if (y == dY)
    {
      // linear on X
      final double heightX = grid.getElevationAt(x, y);
      final double heightNextX = grid.getElevationAt(nextX, y);
      return linearInterpolation(heightX, heightNextX, alphaX);
    }
  
    // bilinear
    final double valueNW = grid.getElevationAt(x, y);
    final double valueNE = grid.getElevationAt(nextX, y);
    final double valueSE = grid.getElevationAt(nextX, nextY);
    final double valueSW = grid.getElevationAt(x, nextY);
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

  public final double getElevationAt(int x, int y)
  {
    final double u = (double) x / _extent._x;
    final double v = (double) y / _extent._y;
  
    return getElevationAt(_grid, u, v);
  }

}
