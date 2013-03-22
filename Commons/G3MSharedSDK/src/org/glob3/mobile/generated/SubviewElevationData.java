package org.glob3.mobile.generated; 
//
//  SubviewElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  SubviewElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//


//class IFloatBuffer;

public class SubviewElevationData extends ElevationData
{
  private final ElevationData _elevationData;
  private final boolean _ownsElevationData;
  private final IFloatBuffer _buffer;

  private IFloatBuffer createDecimatedBuffer()
  {
    IFloatBuffer buffer = IFactory.instance().createFloatBuffer(_width * _height);
  
    final Vector2D parentXYAtLower = getParentXYAt(_sector.lower());
    final Vector2D parentXYAtUpper = getParentXYAt(_sector.upper());
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
  
        final double height = getElevationBoxAt(x0, y0, x1, y1);
        buffer.put(index, (float) height);
      }
    }
  
    return buffer;
  }

  private double getElevationBoxAt(double x0, double y0, double x1, double y1)
  {
  //  aaa;
  
    final IMathUtils mu = IMathUtils.instance();
  
    final double floorY0 = mu.floor(y0);
    final double ceilY1 = mu.ceil(y1);
    final double floorX0 = mu.floor(x0);
    final double ceilX1 = mu.ceil(x1);
  
    if (floorY0 < 0 || ceilY1 >= _elevationData.getExtentHeight())
    {
      return 0;
    }
    if (floorX0 < 0 || ceilX1 >= _elevationData.getExtentWidth())
    {
      return 0;
    }
  
    int unusedType = -1;
  
    double heightSum = 0;
    double area = 0;
  
    final double maxX = _elevationData.getExtentWidth() - 1;
    final double maxY = _elevationData.getExtentHeight() - 1;
  
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
  
      for (double x = floorX0; x <= ceilX1; x++)
      {
        double size = ysize;
        final double height = _elevationData.getElevationAt((int) mu.min(x, maxX), (int) mu.min(y, maxY), unusedType);
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

  private Vector2D getParentXYAt(Geodetic2D position)
  {
    final Sector parentSector = _elevationData.getSector();
    final Geodetic2D parentLower = parentSector.lower();
  
    final double parentX = ((position.longitude().radians() - parentLower.longitude().radians()) / parentSector.getDeltaLongitude().radians() * _elevationData.getExtentWidth());
  
    final double parentY = ((position.latitude().radians() - parentLower.latitude().radians()) / parentSector.getDeltaLatitude().radians() * _elevationData.getExtentHeight());
  
    return new Vector2D(parentX, parentY);
  }

  public SubviewElevationData(ElevationData elevationData, boolean ownsElevationData, Sector sector, Vector2I resolution, double noDataValue, boolean useDecimation)
  {
     super(sector, resolution, noDataValue);
     _elevationData = elevationData;
     _ownsElevationData = ownsElevationData;
    if (useDecimation)
    {
      _buffer = createDecimatedBuffer();
    }
    else
    {
      _buffer = null;
    }
  }

  public void dispose()
  {
    if (_ownsElevationData)
    {
      if (_elevationData != null)
         _elevationData.dispose();
    }
    if (_buffer != null)
       _buffer.dispose();
  }

  public final double getElevationAt(int x, int y, int type)
  {
  
    if (_buffer != null)
    {
      final int index = ((_height-1-y) * _width) + x;
  
      if ((index < 0) || (index >= _buffer.size()))
      {
        System.out.print("break point on me\n");
        type = 0;
        return _noDataValue;
      }
      type = 1;
      return _buffer.get(index);
    }
  
    final double u = (double) x / (_width - 1);
    final double v = (double) y / (_height - 1);
    final Geodetic2D position = _sector.getInnerPoint(u, v);
  
    return getElevationAt(position.latitude(), position.longitude(), type);
  }

  public final double getElevationAt(Angle latitude, Angle longitude, int type)
  {
    if (!_sector.contains(latitude, longitude))
    {
      //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
      //                                  _sector.description().c_str(),
      //                                  latitude.description().c_str(),
      //                                  longitude.description().c_str());
      return _noDataValue;
    }
    return _elevationData.getElevationAt(latitude, longitude, type);
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(SubviewElevationData extent=");
    isb.addInt(_width);
    isb.addString("x");
    isb.addInt(_height);
    isb.addString(" sector=");
    isb.addString(_sector.description());
    isb.addString(" on ElevationData=");
    isb.addString(_elevationData.description(detailed));
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final Vector2D getMinMaxHeights()
  {
  
    final IMathUtils mu = IMathUtils.instance();
  
    double minHeight = mu.maxDouble();
    double maxHeight = mu.minDouble();
  
    int unusedType = 0;
  
    for (int x = 0; x < _width; x++)
    {
      for (int y = 0; y < _height; y++)
      {
        final double height = getElevationAt(x, y, unusedType);
  //      if (height != _noDataValue) {
        if (height < minHeight)
        {
          minHeight = height;
        }
        if (height > maxHeight)
        {
          maxHeight = height;
        }
  //      }
      }
    }
  
    if (minHeight == mu.maxDouble())
    {
      minHeight = 0;
    }
    if (maxHeight == mu.minDouble())
    {
      maxHeight = 0;
    }
  
    return new Vector2D(minHeight, maxHeight);
  }

}