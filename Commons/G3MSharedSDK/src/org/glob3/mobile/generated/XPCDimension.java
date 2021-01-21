package org.glob3.mobile.generated;
//
//  XPCDimension.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCDimension.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//



//class IByteBuffer;
//class ByteBufferIterator;


public class XPCDimension
{
  public final String _name;
  public final byte _size;
  public final String _type;

  public XPCDimension(String name, byte size, String type)
  {
     _name = name;
     _size = size;
     _type = type;
  
  }

  public void dispose()
  {
  
  }

  public final IByteBuffer readValues(ByteBufferIterator it)
  {
    final int pointsCount = it.nextInt32();
  
  //  for (int i = 0; i < pointsCount; i++) {
  //    dimensionValue;
  //  }
  
    //  UNSIGNED 8, 4, 2, 1
    //  FLOATING 8, 4
    //
    ///#error DIEGO AT WORK
  
    final IByteBuffer dimensionValues = it.nextBuffer(pointsCount * _size);
    return dimensionValues;
  }

}