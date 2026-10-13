package org.glob3.mobile.generated;
//
//  MutableColor255.cpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//

//
//  MutableColor255.hpp
//  G3M
//
//  Created by Diego Gomez Deck on 10/17/16.
//
//


public class MutableColor255
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableColor255 operator =(MutableColor255 that);


  // Channels are 0-255 held in int: cpp2java turns unsigned char into Java's signed byte.
  public int _red;
  public int _green;
  public int _blue;
  public int _alpha;

  public MutableColor255(MutableColor255 that)
  {
     _red = that._red;
     _green = that._green;
     _blue = that._blue;
     _alpha = that._alpha;
  }

  public MutableColor255(int red, int green, int blue, int alpha)
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