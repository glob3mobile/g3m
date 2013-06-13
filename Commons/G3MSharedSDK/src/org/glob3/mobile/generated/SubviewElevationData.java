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
  private final IFloatBuffer _buffer;

  private boolean _hasNoData;

//  const Geodetic2D _realResolution;

  private IFloatBuffer createDecimatedBuffer(ElevationData elevationData)
  {
    IFloatBuffer buffer = IFactory.instance().createFloatBuffer(_width * _height);
  
    final Vector2D parentXYAtLower = getParentXYAt(elevationData, _sector.lower());
    final Vector2D parentXYAtUpper = getParentXYAt(elevationData, _sector.upper());
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
  
        final double height = getElevationBoxAt(elevationData, x0, y0, x1, y1);
        buffer.rawPut(index, (float) height);
  
        if (!_hasNoData)
        {
          if (mu.isNan(height))
          {
            _hasNoData = true;
          }
        }
      }
    }
  
    return buffer;
  }
  private IFloatBuffer createInterpolatedBuffer(ElevationData elevationData)
  {
    IFloatBuffer buffer = IFactory.instance().createFloatBuffer(_width * _height);
  
    IMathUtils mu = IMathUtils.instance();
  
    for (int x = 0; x < _width; x++)
    {
      final double u = (double) x / (_width - 1);
  
      final Angle longitude = _sector.getInnerPointLongitude(u);
  
      for (int y = 0; y < _height; y++)
      {
        final double v = 1.0 - ((double) y / (_height - 1));
  
        final Angle latitude = _sector.getInnerPointLatitude(v);
  
        final int index = ((_height-1-y) * _width) + x;
  
        final double height = elevationData.getElevationAt(latitude, longitude);
  
        buffer.rawPut(index, (float) height);
  
        if (!_hasNoData)
        {
          if (mu.isNan(height))
          {
            _hasNoData = true;
          }
        }
      }
    }
  
    return buffer;
  }

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
  
        if (mu.isNan(height))
        {
          return mu.NanD();
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
    final Geodetic2D parentLower = parentSector.lower();
  
    final double parentX = ((position.longitude().radians() - parentLower.longitude().radians()) / parentSector.getDeltaLongitude().radians() * elevationData.getExtentWidth());
  
    final double parentY = ((position.latitude().radians() - parentLower.latitude().radians()) / parentSector.getDeltaLatitude().radians() * elevationData.getExtentHeight());
  
    return new Vector2D(parentX, parentY);
  }

  public SubviewElevationData(ElevationData elevationData, Sector sector, Vector2I extent, boolean useDecimation)
                                             //bool ownsElevationData,
  //_realResolution( elevationData->getRealResolution() )
  {
     super(sector, extent);
    if ((elevationData == null) || (elevationData.getExtentWidth() < 1) || (elevationData.getExtentHeight() < 1))
    {
      ILogger.instance().logError("SubviewElevationData can't subview given elevation data.");
      _buffer = null;
      return;
    }
  
    _hasNoData = false;
    if (useDecimation)
    {
      _buffer = createDecimatedBuffer(elevationData);
    }
    else
    {
      _buffer = createInterpolatedBuffer(elevationData);
    }
  
    //isEquivalentTo(elevationData);
  }
                       //bool ownsElevationData,

  public void dispose()
  {
    //  if (_ownsElevationData) {
  //  delete _elevationData;
    //  }
    if (_buffer != null)
       _buffer.dispose();
  }

//  const Geodetic2D getRealResolution() const {
//    return _realResolution;
//  }

  public final double getElevationAt(int x, int y)
  {
  
    //  if (_buffer != NULL) {
    final int index = ((_height-1-y) * _width) + x;
  
    if ((index < 0) || (index >= _buffer.size()))
    {
      System.out.print("break point on me\n");
      return IMathUtils.instance().NanD();
    }
  
    return _buffer.get(index);
    //  }
    //
    //
    //  const double u = (double) x / (_width - 1);
    //  const double v = (double) y / (_height - 1);
    //  const Geodetic2D position = _sector.getInnerPoint(u, v);
    //
    //  return getElevationAt(position.latitude(),
    //                        position.longitude());
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
    if (detailed)
    {
      isb.addString("\n");
      for (int row = 0; row < _width; row++)
      {
        //isb->addString("   ");
        for (int col = 0; col < _height; col++)
        {
          isb.addDouble(getElevationAt(col, row));
          isb.addString(",");
        }
        isb.addString("\n");
      }
    }
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  
  }

  public final Vector3D getMinMaxAverageElevations()
  {
    final IMathUtils mu = IMathUtils.instance();
  
    double minHeight = mu.maxDouble();
    double maxHeight = mu.minDouble();
    double sumHeight = 0.0;
  
    for (int x = 0; x < _width; x++)
    {
      for (int y = 0; y < _height; y++)
      {
        final double height = getElevationAt(x, y);
        if (!mu.isNan(height))
        {
          if (height < minHeight)
          {
            minHeight = height;
          }
          if (height > maxHeight)
          {
            maxHeight = height;
          }
          sumHeight += height;
        }
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