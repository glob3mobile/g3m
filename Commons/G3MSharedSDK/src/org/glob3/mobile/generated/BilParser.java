package org.glob3.mobile.generated; 
//
//  BilParser.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//

//
//  BilParser.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/19/13.
//
//


//class ShortBufferElevationData;
//class IByteBuffer;
//class Sector;
//class Vector2I;


public class BilParser
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BilParser();


  public static ShortBufferElevationData parseBil16(Sector sector, Vector2I extent, IByteBuffer buffer)
  {
     return parseBil16(sector, extent, buffer, 0);
  }
  public static ShortBufferElevationData parseBil16(Sector sector, Vector2I extent, IByteBuffer buffer, double deltaHeight)
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
  
      if (height == -9999)
      {
        height = ShortBufferElevationData.NO_DATA_VALUE;
      }
      else if (height == minValue)
      {
        height = ShortBufferElevationData.NO_DATA_VALUE;
      }
  
      shortBuffer[i] = height;
    }
  
    return new ShortBufferElevationData(sector, extent, sector, extent, shortBuffer, size, deltaHeight);
  }
}