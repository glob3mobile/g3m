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
//class ShortBufferTerrainElevationGrid;

public class BILParser
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BILParser();

  private static short pvtParse(int size, IByteBuffer buffer, short noDataValue)
  {
  
    final int expectedSizeInBytes = size * 2;
    if (buffer.size() != expectedSizeInBytes)
    {
      ILogger.instance().logError("Invalid buffer size, expected %d bytes, but got %d", expectedSizeInBytes, buffer.size());
      return null;
    }
  
    ByteBufferIterator iterator = new ByteBufferIterator(buffer);
  
    final short minValue = IMathUtils.instance().minInt16();
  
    short[] result = new short[size];
    for (int i = 0; i < size; i++)
    {
      short height = iterator.nextInt16();
  
      if (height == -9999)
      {
        height = noDataValue;
      }
      else if (height == minValue)
      {
        height = noDataValue;
      }
  
      result[i] = height;
    }
  
    return result;
  }


  public static ShortBufferElevationData oldParseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer)
  {
     return oldParseBIL16(sector, extent, buffer, 0);
  }
  public static ShortBufferElevationData oldParseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer, double deltaHeight)
  {
  
    final int size = extent._x * extent._y;
  
    short shortBuffer = pvtParse(size, buffer, ShortBufferElevationData.NO_DATA_VALUE);
    if (shortBuffer == null)
    {
      return null;
    }
  
    return new ShortBufferElevationData(sector, extent, shortBuffer, size, deltaHeight);
  }

  public static ShortBufferTerrainElevationGrid parseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer, short noDataValue)
  {
     return parseBIL16(sector, extent, buffer, noDataValue, 0);
  }
  public static ShortBufferTerrainElevationGrid parseBIL16(Sector sector, Vector2I extent, IByteBuffer buffer, short noDataValue, double deltaHeight)
  {
    final int size = extent._x * extent._y;
  
    short shortBuffer = pvtParse(size, buffer, noDataValue);
    if (shortBuffer == null)
    {
      return null;
    }
  
    return new ShortBufferTerrainElevationGrid(sector, extent, shortBuffer, size, deltaHeight, noDataValue);
  }

}