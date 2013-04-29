package org.glob3.mobile.generated; 
//
//  BufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

//
//  BufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//


//class Interpolator;

public abstract class BufferElevationData extends ElevationData
{
  private Interpolator _interpolator;

  private final int _bufferSize;
  protected final Interpolator getInterpolator()
  {
    if (_interpolator == null)
    {
      _interpolator = new BilinearInterpolator();
    }
    return _interpolator;
  }

  protected abstract double getValueInBufferAt(int index);

  public BufferElevationData(Sector sector, Vector2I resolution, int bufferSize)
  {
     super(sector, resolution);
     _bufferSize = bufferSize;
     _interpolator = null;
  
  }

  public void dispose()
  {
    if (_interpolator != null)
       _interpolator.dispose();
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
      return valueForNoData;
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
    final double u = mu.clamp(uv._x, 0, 1);
    final double v = mu.clamp(uv._y, 0, 1);
    final double dX = u * (_width - 1);
    //const double dY = (1.0 - v) * (_height - 1);
    final double dY = v * (_height - 1);
  
    final int x = (int) dX;
    final int y = (int) dY;
  //  const int nextX = (int) (dX + 1.0);
  //  const int nextY = (int) (dY + 1.0);
    final int nextX = x + 1;
    final int nextY = y + 1;
    final double alphaY = dY - y;
    final double alphaX = dX - x;
  
  //  if (alphaX < 0 || alphaX > 1 ||
  //      alphaY < 0 || alphaY > 1) {
  //    printf("break point\n");
  //  }
  
  
    IMathUtils m = IMathUtils.instance();
    int unsedType = -1;
    double result;
    if (x == dX)
    {
      if (y == dY)
      {
        // exact on grid point
        result = getElevationAt(x, y, type);
      }
      else
      {
  
        type = 2;
        // linear on Y
        final double heightY = getElevationAt(x, y, unsedType);
        if (m.isNan(heightY))
        {
           return valueForNoData;
        }
        final double heightNextY = getElevationAt(x, nextY, unsedType);
        if (m.isNan(heightNextY))
        {
           return valueForNoData;
        }
  
        if (m.isNan(heightY) || m.isNan(heightNextY))
        {
          return valueForNoData;
        }
  
        result = mu.linearInterpolation(heightNextY, heightY, alphaY);
      }
    }
    else
    {
      if (y == dY)
      {
  
        type = 3;
        // linear on X
        final double heightX = getElevationAt(x, y, unsedType);
        if (m.isNan(heightX))
        {
           return valueForNoData;
        }
        final double heightNextX = getElevationAt(nextX, y, unsedType);
        if (m.isNan(heightNextX))
        {
           return valueForNoData;
        }
  
        result = mu.linearInterpolation(heightX, heightNextX, alphaX);
      }
      else
      {
  
        type = 4;
        // bilinear
        final double valueNW = getElevationAt(x, y, unsedType);
        if (m.isNan(valueNW))
        {
           return valueForNoData;
        }
        final double valueNE = getElevationAt(nextX, y, unsedType);
        if (m.isNan(valueNE))
        {
           return valueForNoData;
        }
        final double valueSE = getElevationAt(nextX, nextY, unsedType);
        if (m.isNan(valueSE))
        {
           return valueForNoData;
        }
        final double valueSW = getElevationAt(x, nextY, unsedType);
        if (m.isNan(valueSW))
        {
           return valueForNoData;
        }
  
        result = getInterpolator().interpolation(valueSW, valueSE, valueNE, valueNW, alphaX, alphaY);
      }
    }
  
    return result;
  }

  public double getElevationAt(int x, int y, int type)
  {
     return getElevationAt(x, y, type, IMathUtils.instance().NanD());
  }
  public double getElevationAt(int x, int y, int type, double valueForNoData)
  {
    final int index = ((_height-1-y) * _width) + x;
  //  const int index = ((_width-1-x) * _height) + y;
  
    if ((index < 0) || (index >= _bufferSize))
    {
      System.out.print("break point on me\n");
      type = 0;
      return valueForNoData;
    }
    type = 1;
  
    double d = getValueInBufferAt(index);
    if (IMathUtils.instance().isNan(d))
    {
      return valueForNoData;
    }
    else
    {
      return d;
    }
  }

}