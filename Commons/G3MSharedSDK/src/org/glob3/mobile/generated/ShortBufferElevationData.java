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
  private IShortBuffer _buffer;
  private boolean _hasNoData;
  private short _noDataValue;

  protected final double getValueInBufferAt(int index)
  {
    short s = _buffer.get(index);
    if (s == _noDataValue)
    {
      return IMathUtils.instance().NanD();
    }
    else
    {
      return s;
    }
  }

  public ShortBufferElevationData(Sector sector, Vector2I resolution, short noDataValue, IShortBuffer buffer)
  {
     super(sector, resolution, buffer.size());
     _buffer = buffer;
     _noDataValue = noDataValue;
    if (_buffer.size() != (_width * _height))
    {
      ILogger.instance().logError("Invalid buffer size");
    }
  
    int size = buffer.size();
    _hasNoData = false;
    for (int i = 0; i < size; i++)
    {
      if (buffer.get(i) == noDataValue)
      {
        _hasNoData = true;
        break;
      }
    }
  }

  public void dispose()
  {
    if (_buffer != null)
       _buffer.dispose();
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
    int unusedType = -1;
    if (detailed)
    {
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

  public final Vector3D getMinMaxAverageHeights()
  {
    final IMathUtils mu = IMathUtils.instance();
    short minHeight = mu.maxInt16();
    short maxHeight = mu.minInt16();
    double sumHeight = 0.0;
  
    final int bufferSize = _buffer.size();
    for (int i = 0; i < bufferSize; i++)
    {
      final short height = _buffer.get(i);
      if (height != _noDataValue)
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