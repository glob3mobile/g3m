package org.glob3.mobile.generated;
//
//  AbsoluteImageSizer.cpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//

//
//  AbsoluteImageSizer.hpp
//  G3M
//
//  Created by DIEGO RAMIRO GOMEZ-DECK on 2/11/19.
//




public class AbsoluteImageSizer extends ImageSizer
{
  private final int _size;


  public AbsoluteImageSizer(int size)
  {
     _size = size;
  
  }

  public void dispose()
  {
    super.dispose();
  }

  public final int calculate()
  {
    return _size;
  }

  public final AbsoluteImageSizer copy()
  {
    return new AbsoluteImageSizer(_size);
  }

}
