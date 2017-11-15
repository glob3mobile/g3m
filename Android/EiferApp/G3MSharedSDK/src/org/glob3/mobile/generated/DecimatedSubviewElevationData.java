package org.glob3.mobile.generated; 
//
//  DecimatedSubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//

//
//  DecimatedSubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 8/1/13.
//
//



public class DecimatedSubviewElevationData extends SubviewElevationData
{
  private double getElevationBoxAt(ElevationData elevationData, double x0, double y0, double x1, double y1)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final double floorY0 = mu.floor(y0);
    final double ceilY1 = mu.ceil(y1);
    final double floorX0 = mu.floor(x0);
    final double ceilX1 = mu.ceil(x1);
  
    final int parentHeight = elevationData.getExtentHeight();
    final int parentWidth = elevationData.getExtentWidth();
  
    if (floorY0 < 0 || ceilY1 >= parentHeight)
    {
      return 0;
    }
    if (floorX0 < 0 || ceilX1 >= parentWidth)
    {
      return 0;
    }
  
    double heightSum = 0;
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
        final double height = elevationData.getElevationAt((int) mu.min(x, maxX), yy);
  
        if ((height != height))
        {
          return java.lang.Double.NaN;
        }
  
        double size = ysize;
        if (x < x0)
        {
          size *= (1.0 - (x0-x));
        }
        if (x > x1)
        {
          size *= (1.0 - (x-x1));
        }
  
        heightSum += height * size;
        area += size;
      }
    }
  
    return heightSum/area;
  }

  private Vector2D getParentXYAt(ElevationData elevationData, Geodetic2D position)
  {
    final Sector parentSector = elevationData.getSector();
    final Geodetic2D parentLower = parentSector._lower;
  
    final double parentX = ((position._longitude._radians - parentLower._longitude._radians) / parentSector._deltaLongitude._radians * elevationData.getExtentWidth());
  
    final double parentY = ((position._latitude._radians - parentLower._latitude._radians) / parentSector._deltaLatitude._radians * elevationData.getExtentHeight());
  
    return new Vector2D(parentX, parentY);
  }

  public DecimatedSubviewElevationData(ElevationData elevationData, Sector sector, Vector2I extent)
  {
     super(elevationData, sector, extent);
    final Vector2D parentXYAtLower = getParentXYAt(elevationData, _sector._lower);
    final Vector2D parentXYAtUpper = getParentXYAt(elevationData, _sector._upper);
    final Vector2D parentDeltaXY = parentXYAtUpper.sub(parentXYAtLower);

    for (int x = 0; x < _width; x++)
    {
      final double u0 = (double) x / (_width - 1);
      final double u1 = (double)(x+1) / (_width - 1);
      final double x0 = u0 * parentDeltaXY._x + parentXYAtLower._x;
      final double x1 = u1 * parentDeltaXY._x + parentXYAtLower._x;

      for (int y = 0; y < _height; y++)
      {
        final double v0 = (double) y / (_height - 1);
        final double v1 = (double)(y+1) / (_height - 1);
        final double y0 = v0 * parentDeltaXY._y + parentXYAtLower._y;
        final double y1 = v1 * parentDeltaXY._y + parentXYAtLower._y;

        final int index = ((_height-1-y) * _width) + x;

        final double height = getElevationBoxAt(elevationData, x0, y0, x1, y1);
        _buffer[index] = (float) height;

        if (!_hasNoData)
        {
          if ((height != height))
          {
            _hasNoData = true;
          }
        }
      }
    }
  }


}