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



///#include "Color.hpp"


//class XPCMetadata;
//class IIntBuffer;
//class IByteBuffer;
//class MutableColor;


public abstract class XPCPointColorizer
{
  public void dispose()
  {
  }

  public abstract IIntBuffer initialize(XPCMetadata metadata);

  public abstract void colorize(XPCMetadata metadata, double[] heights, java.util.ArrayList<IByteBuffer> dimensionsValues, int i, MutableColor color);

}