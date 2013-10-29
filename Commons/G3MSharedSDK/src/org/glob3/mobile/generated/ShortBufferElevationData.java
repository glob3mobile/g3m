package org.glob3.mobile.generated; 
//
//  ShortBufferElevationData.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//

//
//  ShortBufferElevationData.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/23/13.
//
//


//class IShortBuffer;

public class ShortBufferElevationData extends BufferElevationData
{
  private short[] _buffer;
  private boolean _hasNoData;

  protected final double getValueInBufferAt(int index)
  {
    final short value = _buffer[index];
    if (value == NO_DATA_VALUE)
    {
      return java.lang.Double.NaN;
    }
    return value;
  }


  public static final short NO_DATA_VALUE = -32768;

  public ShortBufferElevationData(Sector sector, Vector2I extent, Sector realSector, Vector2I realExtent, short[] buffer, int bufferSize, double deltaHeight)
  {
     super(sector, extent, realSector, realExtent, bufferSize, deltaHeight);
     _buffer = buffer;
    if (_bufferSize != (_width * _height))
    {
      ILogger.instance().logError("Invalid buffer size");
    }
  
    final int size = _bufferSize;
    _hasNoData = false;
    for (int i = 0; i < size; i++)
    {
      if (buffer[i] == NO_DATA_VALUE)
      {
        _hasNoData = true;
        break;
      }
    }
  }

  public void dispose()
  {
    _buffer = null;
  
    super.dispose();
  }

  public final String description(boolean detailed)
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(ShortBufferElevationData extent=");
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
    short minHeight = mu.maxInt16();
    short maxHeight = mu.minInt16();
    double sumHeight = 0.0;
  
    final int bufferSize = _bufferSize;
    for (int i = 0; i < bufferSize; i++)
    {
      final short height = _buffer[i];
      if (height != NO_DATA_VALUE)
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
  
    if (minHeight == mu.maxInt16())
    {
      minHeight = 0;
    }
    if (maxHeight == mu.minInt16())
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