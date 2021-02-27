package org.glob3.mobile.generated;
//
//  MutableColor.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//

//
//  MutableColor.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez-Deck on 2/10/21.
//




public class MutableColor
{
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  MutableColor operator =(MutableColor that);

  public float _red;
  public float _green;
  public float _blue;
  public float _alpha;

  public MutableColor(MutableColor that)
  {
     _red = that._red;
     _green = that._green;
     _blue = that._blue;
     _alpha = that._alpha;
  }

  public MutableColor()
  {
     _red = 0F;
     _green = 0F;
     _blue = 0F;
     _alpha = 0F;
  }

  public MutableColor(float red, float green, float blue, float alpha)
  {
     _red = red;
     _green = green;
     _blue = blue;
     _alpha = alpha;
  }

  public void dispose()
  {

  }

  public final void set(Color color)
  {
    _red = color._red;
    _green = color._green;
    _blue = color._blue;
    _alpha = color._alpha;
  }

  public final void set(float red, float green, float blue, float alpha)
  {
    _red = red;
    _green = green;
    _blue = blue;
    _alpha = alpha;
  }

}