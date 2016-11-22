package org.glob3.mobile.generated; 
//
//  BILParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

//
//  BILParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//


//class ShortBufferElevationData;
//class IByteBuffer;
//class Sector;
//class Vector2I;
//class ShortBufferDEMGrid;

public class BILParser
{
  private BILParser()
  {
  }


  public static ShortBufferElevationData oldParseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer)
  {
     return oldParseBIL16(sector, extent, buffer, 0);
  }
  public static ShortBufferElevationData oldParseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer, double deltaHeight)
  {
  
    final int size = extent._x * extent._y;
  
    final int expectedSizeInBytes = size * 2;
    if (buffer.size() != expectedSizeInBytes)
    {
      ILogger.instance().logError("Invalid buffer size, expected %d bytes, but got %d", expectedSizeInBytes, buffer.size());
      return null;
    }
  
    ByteBufferIterator iterator = new ByteBufferIterator(buffer);
  
    final short minValue = IMathUtils.instance().minInt16();
  
    short[] shortBuffer = new short[size];
    for (int i = 0; i < size; i++)
    {
      short height = iterator.nextInt16();
  
      if ((height == -9999) || (height == minValue))
      {
        height = ShortBufferElevationData.NO_DATA_VALUE;
      }
  
      shortBuffer[i] = height;
    }
  
    return new ShortBufferElevationData(sector, extent, shortBuffer, size, deltaHeight);
  }

  public static ShortBufferDEMGrid parseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer, short noDataValue)
  {
     return parseBIL16(sector, extent, buffer, noDataValue, 0);
  }
  public static ShortBufferDEMGrid parseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer, short noDataValue, double deltaHeight)
  {
    final int size = extent._x * extent._y;
  
    final int expectedSizeInBytes = size * 2;
    if (buffer.size() != expectedSizeInBytes)
    {
      ILogger.instance().logError("Invalid buffer size, expected %d bytes, but got %d", expectedSizeInBytes, buffer.size());
      return null;
    }
  
    ByteBufferIterator iterator = new ByteBufferIterator(buffer);
  
    final short minValue = IMathUtils.instance().minInt16();
  
    short[] shortBuffer = new short[size];
    for (int i = 0; i < size; i++)
    {
      short height = iterator.nextInt16();
  
      if ((height == -9999) || (height == minValue))
      {
        height = noDataValue;
      }
  
      shortBuffer[i] = height;
    }
  
    return new ShortBufferDEMGrid(WGS84Projetion.instance(), sector, extent, shortBuffer, size, deltaHeight, noDataValue);
  }

}