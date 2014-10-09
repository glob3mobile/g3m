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


//class IFloatBuffer;

public class FloatBufferElevationData extends BufferElevationData
{
  private IFloatBuffer _buffer;
  private boolean _hasNoData;

  protected final double getValueInBufferAt(int index)
  {
    final float value = _buffer.get(index);
    if (value == NO_DATA_VALUE)
    {
      return java.lang.Double.NaN;
    }
    return value;
  }


  public static final float NO_DATA_VALUE = java.lang.Float.NaN;

  public FloatBufferElevationData(Sector sector, Vector2I extent, Sector realSector, Vector2I realExtent, IFloatBuffer buffer, double deltaHeight)
  {
     super(sector, extent, realSector, realExtent, buffer.size(), deltaHeight);
     _buffer = buffer;
    if (_buffer.size() != (_width * _height))
    {
      ILogger.instance().logError("Invalid buffer size");
    }
  
    final int size = buffer.size();
    _hasNoData = false;
    for (int i = 0; i < size; i++)
    {
      if (buffer.get(i) == NO_DATA_VALUE)
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
  
    super.dispose();
  
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

  public final Vector3D getMinMaxAverageElevations()
  {
    final IMathUtils mu = IMathUtils.instance();
    float minHeight = mu.maxFloat();
    float maxHeight = mu.minFloat();
    double sumHeight = 0.0;
  
    final int bufferSize = _buffer.size();
    for (int i = 0; i < bufferSize; i++)
    {
      final float height = _buffer.get(i);
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
  
    if (minHeight == mu.maxFloat())
    {
      minHeight = 0F;
    }
    if (maxHeight == mu.minFloat())
    {
      maxHeight = 0F;
    }
  
    return new Vector3D(minHeight, maxHeight, sumHeight / (_width * _height));
  }

  public final boolean hasNoData()
  {
    return _hasNoData;
  }

}