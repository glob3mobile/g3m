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

  public FloatBufferElevationData(Sector sector, Vector2I resolution, double noDataValue, IFloatBuffer buffer)
  {
     super(sector, resolution, noDataValue);
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

  public final double getElevationAt(int x, int y, int type)
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
    type = 1;
    return _buffer.get(index);
  }

  public final double getElevationAt(Angle latitude, Angle longitude, int type)
  {
  
    IMathUtils mu = IMathUtils.instance();
  
    if (!_sector.contains(latitude, longitude))
    {
      ILogger.instance().logError("Sector %s doesn't contain lat=%s lon=%s", _sector.description(), latitude.description(), longitude.description());
  
  //    return mu->NanD();
      return _noDataValue;
    }
  
    final Vector2D uv = _sector.getUVCoordinates(latitude, longitude);
    final double dX = uv._x * (_width - 1);
    final double dY = (1.0 - uv._y) * (_height - 1);
  
    final int x = (int) dX;
    final int y = (int) dY;
    final int nextX = (int)(dX + 1.0);
    final int nextY = (int)(dY + 1.0);
    final double alphaY = dY - y;
    final double alphaX = dX - x;
  
    int unusedType = 0;
    double result;
    if (x == dX)
    {
      if (y == dY)
      {
        // exact on grid point
        type = 1;
        result = getElevationAt(x, y, unusedType);
      }
      else
      {
        // linear on Y
        final double heightY = getElevationAt(x, y, unusedType);
        final double heightNextY = getElevationAt(x, nextY, unusedType);
        type = 2;
        result = mu.lerp(heightY, heightNextY, alphaY);
      }
    }
    else
    {
      if (y == dY)
      {
        // linear on X
        final double heightX = getElevationAt(x, y, unusedType);
        final double heightNextX = getElevationAt(nextX, y, unusedType);
        type = 3;
        result = mu.lerp(heightX, heightNextX, alphaX);
      }
      else
      {
        // bilinear
        final double valueSW = getElevationAt(x, y, unusedType);
        final double valueSE = getElevationAt(nextX, y, unusedType);
        final double valueNE = getElevationAt(nextX, nextY, unusedType);
        final double valueNW = getElevationAt(x, nextY, unusedType);
  
        type = 4;
        result = getInterpolator().interpolate(valueSW, valueSE, valueNE, valueNW, alphaY, alphaX);
      }
    }
  
    return result;
  
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
      int unusedType = 0;
      isb.addString("\n");
      for (int row = 0; row < _width; row++)
      {
        //isb->addString("   ");
        for (int col = 0; col < _height; col++)
        {
          isb.addDouble(getElevationAt(col, row, unusedType));
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