package org.glob3.mobile.generated;
//
//  XPCPoint.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/19/21.
//

//
//  XPCPoint.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/19/21.
//


//class ByteBufferIterator;


public class XPCPoint
{
  public static XPCPoint fromByteBufferIterator(ByteBufferIterator it)
  {
    final double x = it.nextDouble();
    final double y = it.nextDouble();
    final double z = it.nextDouble();
  
    return new XPCPoint(x, y, z);
  }


  public final double _x;
  public final double _y;
  public final double _z;

  public XPCPoint(double x, double y, double z)
  {
     _x = x;
     _y = y;
     _z = z;
  
  }

  public void dispose()
  {
  
  }

}