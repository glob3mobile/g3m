package org.glob3.mobile.generated; 
//
//  MutableColor255.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

//
//  MutableColor255.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//


public class MutableColor255
{
  public byte _red;
  public byte _green;
  public byte _blue;
  public byte _alpha;

  public MutableColor255(MutableColor255 that)
  {
     _red = that._red;
     _green = that._green;
     _blue = that._blue;
     _alpha = that._alpha;
  }

  public MutableColor255(byte red, byte green, byte blue, byte alpha)
  {
     _red = red;
     _green = green;
     _blue = blue;
     _alpha = alpha;
  }

  public void dispose()
  {

  }

}