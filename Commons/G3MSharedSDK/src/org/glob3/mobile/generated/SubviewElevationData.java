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

  private boolean _hasNoData;

  private IFloatBuffer createDecimatedBuffer()
  {
    IFloatBuffer buffer = IFactory.instance().createFloatBuffer(_width * _height);
  
    final Vector2D parentXYAtLower = getParentXYAt(_sector.lower());
    final Vector2D parentXYAtUpper = getParentXYAt(_sector.upper());
    final Vector2D parentDeltaXY = parentXYAtUpper.sub(parentXYAtLower);
  
    IMathUtils mu = IMathUtils.instance();
  
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
        buffer.rawPut(index, (float) height);
  
        if (mu.isNan(height))
        {
          _hasNoData = true;
        }
      }
    }
  
    return buffer;
  }
  private IFloatBuffer createInterpolatedBuffer()
  {
    IFloatBuffer buffer = IFactory.instance().createFloatBuffer(_width * _height);
  
  
    IMathUtils mu = IMathUtils.instance();
  
    int unusedType = -1;
    for (int x = 0; x < _width; x++)
    {
      final double u = (double) x / (_width - 1);
      for (int y = 0; y < _height; y++)
      {
        final double v = (double) y / (_height - 1);
        final Geodetic2D position = _sector.getInnerPoint(u, v);
  
        final int index = ((_height-1-y) * _width) + x;
  
        final double height = _elevationData.getElevationAt(position.latitude(), position.longitude(), unusedType);
  
        buffer.rawPut(index, (float) height);
  
        if (mu.isNan(height))
        {
          _hasNoData = true;
        }
  
      }
    }
  
    return buffer;
  }

  private double getElevationBoxAt(double x0, double y0, double x1, double y1)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final double floorY0 = mu.floor(y0);
    final double ceilY1 = mu.ceil(y1);
    final double floorX0 = mu.floor(x0);
    final double ceilX1 = mu.ceil(x1);
  
    final int parentHeight = _elevationData.getExtentHeight();
    final int parentWidth = _elevationData.getExtentWidth();
  
    if (floorY0 < 0 || ceilY1 >= parentHeight)
    {
      return 0;
    }
    if (floorX0 < 0 || ceilX1 >= parentWidth)
    {
      return 0;
    }
  
    int unusedType = -1;
  
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
        final double height = _elevationData.getElevationAt((int) mu.min(x, maxX), yy, unusedType);
  
        if (IMathUtils.instance().isNan(height))
        {
          return IMathUtils.instance().NanD();
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

  private Vector2D getParentXYAt(Geodetic2D position)
  {
    final Sector parentSector = _elevationData.getSector();
    final Geodetic2D parentLower = parentSector.lower();
  
    final double parentX = ((position.longitude().radians() - parentLower.longitude().radians()) / parentSector.getDeltaLongitude().radians() * _elevationData.getExtentWidth());
  
    final double parentY = ((position.latitude().radians() - parentLower.latitude().radians()) / parentSector.getDeltaLatitude().radians() * _elevationData.getExtentHeight());
  
    return new Vector2D(parentX, parentY);
  }

  public SubviewElevationData(ElevationData elevationData, boolean ownsElevationData, Sector sector, Vector2I resolution, boolean useDecimation)
  {
     super(sector, resolution);
     _elevationData = elevationData;
     _ownsElevationData = ownsElevationData;
    if (_elevationData == null || _elevationData.getExtentWidth() < 1 || _elevationData.getExtentHeight() < 1)
    {
      ILogger.instance().logError("SubviewElevationData can't subview given elevation data.");
      return;
    }
  
    _hasNoData = false;
    if (useDecimation)
    {
      _buffer = createDecimatedBuffer();
    }
    else
    {
      _buffer = createInterpolatedBuffer();
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
     return getElevationAt(x, y, type, IMathUtils.instance().NanD());
  }
  public final double getElevationAt(int x, int y, int type, double valueForNoData)
  {
  
    if (_buffer != null)
    {
      final int index = ((_height-1-y) * _width) + x;
  
      if ((index < 0) || (index >= _buffer.size()))
      {
        System.out.print("break point on me\n");
        type = 0;
        return IMathUtils.instance().NanD();
      }
      type = 1;
  
      double h = _buffer.get(index);
      if (IMathUtils.instance().isNan(h))
      {
        return valueForNoData;
      }
      else
      {
        return h;
      }
    }
  
  
    final double u = (double) x / (_width - 1);
    final double v = (double) y / (_height - 1);
    final Geodetic2D position = _sector.getInnerPoint(u, v);
  
    return getElevationAt(position.latitude(), position.longitude(), type, valueForNoData);
  }

  public final double getElevationAt(Angle latitude, Angle longitude, int type)
  {
     return getElevationAt(latitude, longitude, type, IMathUtils.instance().NanD());
  }
  public final double getElevationAt(Angle latitude, Angle longitude, int type, double valueForNoData)
  {
    if (!_sector.contains(latitude, longitude))
    {
      //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
      //                                  _sector.description().c_str(),
      //                                  latitude.description().c_str(),
      //                                  longitude.description().c_str());
      return IMathUtils.instance().NanD();
    }
  
    double h = _elevationData.getElevationAt(latitude, longitude, type);
    if (IMathUtils.instance().isNan(h))
    {
      return valueForNoData;
    }
    else
    {
      return h;
    }
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

  public final Vector3D getMinMaxAverageHeights()
  {
    final IMathUtils mu = IMathUtils.instance();
  
    double minHeight = mu.maxDouble();
    double maxHeight = mu.minDouble();
    double sumHeight = 0.0;
  
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
        sumHeight += height;
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
  
    return new Vector3D(minHeight, maxHeight, sumHeight / (_width * _height));
  }

  public final boolean hasNoData()
  {
     return _hasNoData;
  }

}