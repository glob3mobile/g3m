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


//class ElevationData;
//class IByteBuffer;
//class Sector;



public class BilParser
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  BilParser();


  public static ElevationData parseBil16(Sector sector, Vector2I extent, short noDataValue, double minValidHeight, IByteBuffer buffer)
  {
  
    final int size = extent._x * extent._y;
    //  const int size = (extent._x + margin) * (extent._y + margin);
  
    final int expectedSizeInBytes = size * 2;
    if (buffer.size() != expectedSizeInBytes)
    {
      ILogger.instance().logError("Invalid buffer size, expected %d bytes, but got %d", expectedSizeInBytes, buffer.size());
      return null;
    }
  
    ByteBufferIterator iterator = new ByteBufferIterator(buffer);
  
    IShortBuffer shortBuffer = IFactory.instance().createShortBuffer(size);
    for (int i = 0; i < size; i++)
    {
      short height = iterator.nextInt16();
  
      int WORKING_NODATA;
      if (height <= minValidHeight)
      {
        height = noDataValue;
      }
      else if (height == -9999)
      {
        height = noDataValue;
      }
      else if (height == -32767)
      {
        height = noDataValue;
      }
      else if (height == -32768)
      {
        height = noDataValue;
      }
      //    if (height < 0) {
      //      height = 0;
      //    }
      shortBuffer.rawPut(i, height);
    }
  
    return new ShortBufferElevationData(sector, extent, noDataValue, shortBuffer);
  }
}