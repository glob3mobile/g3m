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

  public final float getNormalizedValue(IByteBuffer values, int i)
  {
  
    if (_type.equals("unsigned"))
    {
      if (_size == 1)
      {
        return values.get(i) / 255.0f;
      }
      else if (_size == 2)
      {
        final byte b1 = values.get(i *2);
        final byte b2 = values.get(i *2 + 1);
  
        return IMathUtils.instance().toUInt16(b1, b2) / 65535.0f;
      }
  //    else if (_size == 4) {
  //
  //    }
  //    else if (_size == 8) {
  //
  //    }
    }
    else if (_type.equals("floating"))
    {
  //    if (_size == 4) {
  //
  //    }
  //    else if (_size == 8) {
  //
  //    }
    }
  
    throw new RuntimeException("Unsupported dimension type or size.");
  }

}