package org.glob3.mobile.generated;
//
//  XPCPointColorizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//

//
//  XPCPointColorizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 1/15/21.
//




//class XPCMetadata;
//class XPCPoint;
//class IIntBuffer;
//class IByteBuffer;


public abstract class XPCPointColorizer
{
  public void dispose()
  {
  }

  public abstract IIntBuffer initialize(XPCMetadata metadata);

  public abstract Color colorize(XPCMetadata metadata, java.util.ArrayList<XPCPoint> points, java.util.ArrayList<IByteBuffer> dimensionsValues, int i);

}