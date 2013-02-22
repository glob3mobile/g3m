package org.glob3.mobile.generated; 
//
//  FloatBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//

//
//  FloatBufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/21/13.
//
//


//class Interpolator;
//class IFloatBuffer;

public class FloatBufferElevationData extends ElevationData
{
  private IFloatBuffer _buffer;

  private Interpolator _interpolator;
  private Interpolator getInterpolator()
  {
    if (_interpolator == null)
    {
      _interpolator = new BilinearInterpolator();
    }
    return _interpolator;
  }

  public FloatBufferElevationData(Sector sector, Vector2I resolution, IFloatBuffer buffer)
  {
     super(sector, resolution);
     _buffer = buffer;
     _interpolator = null;
    if (_buffer.size() != (_width * _height))
    {
      ILogger.instance().logError("Invalid buffer size");
    }
  
  }

  public void dispose()
  {
    if (_buffer != null)
       _buffer.dispose();
    if (_interpolator != null)
       _interpolator.dispose();
  }

  public final double getElevationAt(int x, int y)
  {
    //return _buffer->get( (x * _width) + y );
    //return _buffer->get( (x * _height) + y );
  
    //        const double height = elevationData->getElevationAt(x, extent._y-1-y);
  
    //  const int a = (_height-1-(y+_margin)) * _width;
    //  const int a1 = _height-1-(y+_margin);
  
    //  const int index = ((_height-1-(y+_margin)) * _width) + (x+_margin);
    final int index = ((_height-1-y) * _width) + x;
    if ((index < 0) || (index >= _buffer.size()))
    {
      System.out.print("break point on me\n");
    }
    return _buffer.get(index);
  }

  public final double getElevationAt(Angle latitude, Angle longitude, int type)
  {
  
    IMathUtils mu = IMathUtils.instance();
  
    if (!_sector.contains(latitude, longitude))
    {
      ILogger.instance().logError("Sector %s doesn't contain lat=%s lon=%s", _sector.description(), latitude.description(), longitude.description());
  
  //    return mu->NanD();
      return -5000;
    }
  
  //  const double dX = (longitude.radians() - _sector.lower().longitude().radians()) / _stepInLongitudeRadians;
  //  const double dY = (latitude.radians()  - _sector.lower().latitude().radians()) / _stepInLatitudeRadians;
  
    final Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
    final double dX = uv._x * _width;
    final double dY = (1.0 - uv._y) * _height;
  
    final int x = (int) dX;
    final int y = (int) dY;
  
    if (x == dX)
    {
      if (y == dY)
      {
        // exact on grid point
        type = 1;
        return getElevationAt(x, y);
      }
      else
      {
        // linear on Y
        final int nextY = (int)(dY + 1.0);
  
        final double heightY = getElevationAt(x, y);
        final double heightNextY = getElevationAt(x, nextY);
        final double alphaY = dY - y;
        type = 2;
        return mu.lerp(heightY, heightNextY, alphaY);
      }
    }
    else
    {
      if (y == dY)
      {
        // linear on X
        final int nextX = (int)(dX + 1.0);
        final double heightX = getElevationAt(x, y);
        final double heightNextX = getElevationAt(nextX, y);
        final double alphaX = dX - x;
        type = 3;
        return mu.lerp(heightX, heightNextX, alphaX);
      }
      else
      {
        // bilinear
        int _WORKING;
        final int nextX = (int)(dX + 1.0);
        final int nextY = (int)(dY + 1.0);
  
        final double valueSW = getElevationAt(x, y);
        final double valueSE = getElevationAt(nextX, y);
        final double valueNE = getElevationAt(nextX, nextY);
        final double valueNW = getElevationAt(x, nextY);
  
        final double alphaY = dY - y;
        final double alphaX = dX - x;
  
        type = 4;
        return getInterpolator().interpolate(valueSW, valueSE, valueNE, valueNW, alphaY, alphaX);
  //      return 0;
      }
    }
  
    //  return IMathUtils::instance()->NanD();
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(FloatBufferElevationData extent=");
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

}