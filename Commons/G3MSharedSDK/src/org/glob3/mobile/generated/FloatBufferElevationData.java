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
  private float _noDataValue;

  protected final double getValueInBufferAt(int index)
  {
    float f = _buffer.get(index);
    if (f == _noDataValue)
    {
      return IMathUtils.instance().NanD();
    }
    else
    {
      return f;
    }
  }

  public FloatBufferElevationData(Sector sector, Vector2I resolution, float noDataValue, IFloatBuffer buffer)
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
    isb.addString("(FloatBufferElevationData extent=");
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
    float minHeight = mu.maxFloat();
    float maxHeight = mu.minFloat();
    double sumHeight = 0.0;
  
    final int bufferSize = _buffer.size();
    for (int i = 0; i < bufferSize; i++)
    {
      final float height = _buffer.get(i);
  //    if (height != _noDataValue) {
      if (height < minHeight)
      {
        minHeight = height;
      }
      if (height > maxHeight)
      {
        maxHeight = height;
      }
      sumHeight += height;
  //    }
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