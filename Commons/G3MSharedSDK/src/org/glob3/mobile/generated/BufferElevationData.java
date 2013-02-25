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

  //
  //  FloatBufferElevationData.cpp
  //  G3MiOSSDK
  //
  //  Created by Diego Gomez Deck on 2/21/13.
  //
  //
  
  
  
  protected final Interpolator getInterpolator()
  {
    if (_interpolator == null)
    {
      _interpolator = new BilinearInterpolator();
    }
    return _interpolator;
  }

  protected abstract double getValueInBufferAt(int index);

  public BufferElevationData(Sector sector, Vector2I resolution, double noDataValue, int bufferSize)
  {
     super(sector, resolution, noDataValue);
     _bufferSize = bufferSize;
     _interpolator = null;
  
  }

  public void dispose()
  {
    if (_interpolator != null)
       _interpolator.dispose();
  }

  public final double getElevationAt(Angle latitude, Angle longitude)
  {
  
  
    if (!_sector.contains(latitude, longitude))
    {
      //    ILogger::instance()->logError("Sector %s doesn't contain lat=%s lon=%s",
      //                                  _sector.description().c_str(),
      //                                  latitude.description().c_str(),
      //                                  longitude.description().c_str());
      return _noDataValue;
    }
  
    IMathUtils mu = IMathUtils.instance();
  
    final Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
    final double dX = uv._x * (_width - 1);
    final double dY = (1.0 - uv._y) * (_height - 1);
  
    final int x = (int) dX;
    final int y = (int) dY;
    final int nextX = (int)(dX + 1.0);
    final int nextY = (int)(dY + 1.0);
    final double alphaY = dY - y;
    final double alphaX = dX - x;
  
    double result;
    if (x == dX)
    {
      if (y == dY)
      {
        // exact on grid point
        result = getElevationAt(x, y);
      }
      else
      {
        // linear on Y
        final double heightY = getElevationAt(x, y);
        final double heightNextY = getElevationAt(x, nextY);
        result = mu.lerp(heightY, heightNextY, alphaY);
      }
    }
    else
    {
      if (y == dY)
      {
        // linear on X
        final double heightX = getElevationAt(x, y);
        final double heightNextX = getElevationAt(nextX, y);
        result = mu.lerp(heightX, heightNextX, alphaX);
      }
      else
      {
        // bilinear
        final double valueSW = getElevationAt(x, y);
        final double valueSE = getElevationAt(nextX, y);
        final double valueNE = getElevationAt(nextX, nextY);
        final double valueNW = getElevationAt(x, nextY);
  
        result = getInterpolator().interpolate(valueSW, valueSE, valueNE, valueNW, alphaY, alphaX);
      }
    }
  
    return result;
  }

  public double getElevationAt(int x, int y)
  {
    final int index = ((_height-1-y) * _width) + x;
    if ((index < 0) || (index >= _bufferSize))
    {
      System.out.print("break point on me\n");
      return _noDataValue;
    }
    return getValueInBufferAt(index);
  }

}