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
  public static XPCPoint fromByteBufferIterator(ByteBufferIterator it, float centerLatitudeDegrees, float centerLongitudeDegrees, float centerHeight)
  {
    final double latitudeDegrees = (double) it.nextFloat() + centerLatitudeDegrees;
    final double longitueDegrees = (double) it.nextFloat() + centerLongitudeDegrees;
    final double height = (double) it.nextFloat() + centerHeight;
  
    return new XPCPoint(latitudeDegrees, longitueDegrees, height);
  }


  public final double _latitudeDegrees;
  public final double _longitueDegrees;
  public final double _height;

  public XPCPoint(double latitudeDegrees, double longitueDegrees, double height)
  {
     _latitudeDegrees = latitudeDegrees;
     _longitueDegrees = longitueDegrees;
     _height = height;
  
  }

  public void dispose()
  {
  
  }

}