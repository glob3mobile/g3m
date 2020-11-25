package org.glob3.mobile.generated;
//
//  DecimatedDEMGrid.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//

//
//  DecimatedDEMGrid.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 11/18/16.
//
//



public class DecimatedDEMGrid extends DecoratorDEMGrid
{

  private DecimatedDEMGrid(DEMGrid grid, Sector sector, Vector2I extent)
  {
     super(grid, sector, extent);
  }

  private double getElevationBoxAt(double x0, double y0, double x1, double y1)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final double floorY0 = mu.floor(y0);
    final double ceilY1 = mu.ceil(y1);
    final double floorX0 = mu.floor(x0);
    final double ceilX1 = mu.ceil(x1);
  
    final int parentHeight = _grid.getExtent()._y;
    final int parentWidth = _grid.getExtent()._x;
  
    if ((floorY0 < 0) || (ceilY1 > parentHeight))
    {
      return 0;
    }
    if ((floorX0 < 0) || (ceilX1 > parentWidth))
    {
      return 0;
    }
  
    double elevationSum = 0;
    double area = 0;
  
    final double maxX = parentWidth - 1;
    final double maxY = parentHeight - 1;
  
    for (double y = floorY0; y <= ceilY1; y++)
    {
      double ysize = 1.0;
      if (y < y0)
      {
        ysize *= (1.0 - (y0-y));
      }
      if (y > y1)
      {
        ysize *= (1.0 - (y-y1));
      }
  
      final int yy = (int) mu.min(y, maxY);
  
      for (double x = floorX0; x <= ceilX1; x++)
      {
        final double elevation = _grid.getElevation((int) mu.min(x, maxX), yy);
  
        if (!(elevation != elevation))
        {
          double size = ysize;
          if (x < x0)
          {
            size *= (1.0 - (x0-x));
          }
          if (x > x1)
          {
            size *= (1.0 - (x-x1));
          }
  
          elevationSum += elevation * size;
          area += size;
        }
      }
    }
  
    return elevationSum/area;
  }

  public static DecimatedDEMGrid create(DEMGrid grid, Vector2S extent)
  {
    return new DecimatedDEMGrid(grid, grid.getSector(), new Vector2I(extent._x, extent._y));
  }

  public final double getElevation(int x, int y)
  {
    final double ratioX = (double) _extent._x / _grid.getExtent()._x;
    final double ratioY = (double) _extent._y / _grid.getExtent()._y;
  
    final double u0 = x * (1.0 / ratioX);
    final double v0 = y * (1.0 / ratioY);
    final double u1 = (x+1) * (1.0 / ratioX);
    final double v1 = (y+1) * (1.0 / ratioY);
  
    return getElevationBoxAt(u0, v0, u1, v1);
  }

}